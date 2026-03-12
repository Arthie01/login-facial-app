#!/usr/bin/env python3
"""
login_api.py - Cliente Python para login facial
Captura foto, la envía al API y retorna el resultado
"""

import os
os.environ["QT_QPA_FONTDIR"] = "/usr/share/fonts"

import cv2
import requests
import base64
import sys
from dotenv import load_dotenv

load_dotenv()

API_URL = os.getenv("API_URL", "http://localhost:8000")

def capturar_y_login():
    """Captura foto de la cámara y hace login"""
    
    print("Iniciando cámara...")
    
    # Abrir cámara
    camara = cv2.VideoCapture(0)
    
    if not camara.isOpened():
        print("ERROR:No se pudo acceder a la cámara")
        return
    
    print("Presiona ESPACIO para capturar o ESC para cancelar")
    
    while True:
        ret, frame = camara.read()
        if not ret:
            print("ERROR:No se pudo leer de la cámara")
            break
        
        # Mostrar preview
        cv2.imshow('Login Facial - Presiona ESPACIO', frame)
        
        tecla = cv2.waitKey(1) & 0xFF
        
        # ESPACIO = capturar
        if tecla == 32:
            print("Foto capturada")
            
            # Guardar imagen temporalmente
            temp_path = "/tmp/login_capture.jpg"
            cv2.imwrite(temp_path, frame)
            print("Foto guardada temporalmente")
            
            # Cerrar cámara y ventana
            camara.release()
            cv2.destroyAllWindows()
            
            # Procesar login
            return procesar_login(temp_path)
        
        # ESC = cancelar
        elif tecla == 27:
            print("Cancelado por el usuario")
            camara.release()
            cv2.destroyAllWindows()
            return
    
    camara.release()
    cv2.destroyAllWindows()

def procesar_login(ruta_imagen):
    """Envía la imagen al API y procesa la respuesta"""
    
    print("Procesando rostro...")
    
    # Leer imagen y convertir a base64
    try:
        with open(ruta_imagen, "rb") as f:
            imagen_bytes = f.read()
            imagen_base64 = base64.b64encode(imagen_bytes).decode('utf-8')
    except Exception as e:
        print(f"ERROR:No se pudo leer la imagen: {e}")
        return
    
    print("Enviando al servidor...")
    
    # Hacer petición al API
    try:
        response = requests.post(
            f"{API_URL}/login",
            json={"image_base64": imagen_base64},
            timeout=30
        )
        
        if response.status_code == 200:
            data = response.json()
            
            if data["success"]:
                print(f"LISTO:{data['user_name']} (ID: {data['user_id']})")
                print(f"TOKEN:{data['token']}")
                return data
            else:
                print(f"ERROR:{data['message']}")
                return None
        else:
            print(f"ERROR:Error del servidor ({response.status_code})")
            return None
            
    except requests.exceptions.ConnectionError:
        print("ERROR:No se pudo conectar al servidor. ¿Está corriendo la API?")
        return None
    except Exception as e:
        print(f"ERROR:{str(e)}")
        return None
    finally:
        # Limpiar archivo temporal
        if os.path.exists(ruta_imagen):
            os.remove(ruta_imagen)

if __name__ == "__main__":
    capturar_y_login()
