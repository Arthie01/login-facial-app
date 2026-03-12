from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import face_recognition
import numpy as np
from supabase import create_client, Client
import os
from dotenv import load_dotenv
import jwt
from datetime import datetime, timedelta
import base64
import pickle
import traceback

# Cargar variables de entorno
load_dotenv()

app = FastAPI(title="Login Facial API")

# Configurar CORS para permitir acceso desde cualquier origen
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Cliente de Supabase
SUPABASE_URL = os.getenv("SUPABASE_URL")
SUPABASE_KEY = os.getenv("SUPABASE_KEY")
SECRET_KEY = os.getenv("SECRET_KEY", "tu-secreto-super-seguro-cambialo")

supabase: Client = create_client(SUPABASE_URL, SUPABASE_KEY)

class LoginRequest(BaseModel):
    image_base64: str

class LoginResponse(BaseModel):
    success: bool
    message: str
    token: str = None
    user_id: str = None
    user_name: str = None

@app.get("/")
def read_root():
    return {
        "message": "API de Login Facial - PC1",
        "endpoints": {
            "/login": "POST - Autenticar usuario con foto facial",
            "/verify": "POST - Verificar token de sesión"
        }
    }

@app.post("/login", response_model=LoginResponse)
async def login_facial(request: LoginRequest):
    """
    Endpoint de login facial.
    Recibe una imagen en base64, extrae el encoding facial,
    compara con todos los usuarios en la BD y retorna un token JWT si hay match.
    """
    try:
        # 1. Decodificar imagen base64
        try:
            image_data = base64.b64decode(request.image_base64)
            # Guardar temporalmente
            temp_path = "/tmp/login_temp.jpg"
            with open(temp_path, "wb") as f:
                f.write(image_data)
        except Exception as e:
            raise HTTPException(status_code=400, detail=f"Error al decodificar imagen: {str(e)}")

        # 2. Detectar rostro y extraer encoding
        try:
            imagen = face_recognition.load_image_file(temp_path)
            encodings = face_recognition.face_encodings(imagen)
            
            if len(encodings) == 0:
                return LoginResponse(
                    success=False,
                    message="No se detectó ningún rostro en la imagen"
                )
            
            if len(encodings) > 1:
                return LoginResponse(
                    success=False,
                    message="Se detectaron múltiples rostros. Por favor, asegúrate de que solo apareces tú."
                )
            
            encoding_login = encodings[0]
            
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error al procesar rostro: {str(e)}")

        # 3. Obtener todos los usuarios de la BD
        try:
            response = supabase.table("usuarios").select("*").execute()
            usuarios = response.data
            
            if not usuarios:
                return LoginResponse(
                    success=False,
                    message="No hay usuarios registrados en el sistema"
                )
        except Exception as e:
            raise HTTPException(status_code=500, detail=f"Error al consultar BD: {str(e)}")

        # 4. Comparar con cada usuario
        mejor_match = None
        mejor_distancia = 1.0  # Distancia máxima (peor caso)
        UMBRAL = 0.6  # Umbral de similitud (menor = más estricto)

        for usuario in usuarios:
            try:
                encoding_raw = usuario["encoding"]
                
                # Formato bytea de Postgres: \x + hex(\x + base64(pickle))
                if encoding_raw.startswith("\\x"):
                    raw_hex = encoding_raw[2:]  # Quitar \x
                    decoded_bytes = bytes.fromhex(raw_hex)
                    decoded_str = decoded_bytes.decode("ascii", errors="replace")
                    # Quitar segundo \\x
                    if decoded_str.startswith("\\x"):
                        b64_data = decoded_str[2:]
                    else:
                        b64_data = decoded_str
                    pickle_bytes = base64.b64decode(b64_data)
                    encoding_bd = np.array(pickle.loads(pickle_bytes))
                elif "," in encoding_raw:
                    # Formato CSV: "0.1,0.2,0.3,..."
                    encoding_bd = np.array([float(x) for x in encoding_raw.split(",")])
                else:
                    # Base64 directo
                    encoding_bd = np.array(pickle.loads(base64.b64decode(encoding_raw)))
                
                # Calcular distancia euclidiana
                distancia = face_recognition.face_distance([encoding_bd], encoding_login)[0]
                
                # Si es el mejor match hasta ahora
                if distancia < mejor_distancia:
                    mejor_distancia = distancia
                    mejor_match = usuario
            except Exception as e:
                print(f"Error procesando usuario {usuario.get('nombre', '?')}: {e}")
                continue

        # 5. Verificar si hay un match válido
        if mejor_match and mejor_distancia < UMBRAL:
            # Generar token JWT
            token_data = {
                "user_id": mejor_match["id"],
                "user_name": mejor_match["nombre"],
                "exp": datetime.utcnow() + timedelta(hours=24)
            }
            token = jwt.encode(token_data, SECRET_KEY, algorithm="HS256")
            
            return LoginResponse(
                success=True,
                message=f"Bienvenido, {mejor_match['nombre']}!",
                token=token,
                user_id=mejor_match["id"],
                user_name=mejor_match["nombre"]
            )
        else:
            return LoginResponse(
                success=False,
                message=f"Rostro no reconocido. Mejor coincidencia: {mejor_distancia:.2f} (umbral: {UMBRAL})"
            )

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error interno: {str(e)}")
    finally:
        # Limpiar archivo temporal
        if os.path.exists("/tmp/login_temp.jpg"):
            os.remove("/tmp/login_temp.jpg")

@app.post("/verify")
async def verify_token(token: str):
    """Verificar si un token JWT es válido"""
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=["HS256"])
        return {
            "valid": True,
            "user_id": payload.get("user_id"),
            "user_name": payload.get("user_name")
        }
    except jwt.ExpiredSignatureError:
        return {"valid": False, "message": "Token expirado"}
    except jwt.InvalidTokenError:
        return {"valid": False, "message": "Token inválido"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
