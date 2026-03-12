# Sistema de Login Facial - PC1

Sistema de autenticación mediante reconocimiento facial desarrollado con Java (interfaz) y Python (procesamiento).

## 🎯 Características

- ✅ Login facial con reconocimiento de rostros
- ✅ Interfaz gráfica en Java Swing
- ✅ API REST con FastAPI
- ✅ Autenticación con tokens JWT
- ✅ Base de datos Supabase
- ✅ Procesamiento de imágenes con OpenCV y face_recognition
- ✅ Logs en tiempo real

## 📋 Requisitos

### Software necesario
- **Python 3.8+** con pip
- **Java 11+** (JDK)
- **Webcam** funcionando
- **Git** (para clonar el repositorio)

### Cuentas necesarias
- Cuenta de **Supabase** (gratis) para la base de datos

## 🚀 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/login-facial-app.git
cd login-facial-app
```

### 2. Configurar entorno virtual de Python

```bash
# Crear entorno virtual
python3 -m venv venv

# Activar entorno virtual
# En Linux/Mac:
source venv/bin/activate
# En Windows:
venv\Scripts\activate
```

### 3. Instalar dependencias de Python

```bash
pip install -r requirements.txt
```

### 4. Configurar Supabase

#### 4.1. Crear proyecto en Supabase
1. Ve a https://supabase.com y crea una cuenta
2. Crea un nuevo proyecto
3. Anota la URL y la API Key (anon public)

#### 4.2. Crear tabla de usuarios
Ejecuta este SQL en el editor de Supabase:

```sql
CREATE TABLE usuarios (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    nombre TEXT NOT NULL,
    encoding TEXT NOT NULL,
    foto_url TEXT,
    fecha_registro TIMESTAMP DEFAULT NOW()
);
```

### 5. Configurar variables de entorno

```bash
# Copiar archivo de ejemplo
cp .env.example .env

# Editar .env con tus credenciales
nano .env  # o usa tu editor favorito
```

Contenido del `.env`:
```env
SUPABASE_URL=https://tu-proyecto.supabase.co
SUPABASE_KEY=tu_key_publica_aqui
API_URL=http://localhost:8000
SECRET_KEY=cambia-esto-por-algo-muy-seguro
```

### 6. Compilar la aplicación Java

```bash
javac AppLogin.java
```

## 💻 Uso

### Paso 1: Iniciar la API (Servidor)

```bash
# Asegúrate de tener el entorno virtual activado
source venv/bin/activate  # Linux/Mac
# o venv\Scripts\activate en Windows

# Iniciar el servidor
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
```

El servidor estará disponible en:
- Local: http://localhost:8000
- Red local: http://TU_IP_LOCAL:8000

### Paso 2: Ejecutar la aplicación de Login

```bash
java AppLogin
```

### Paso 3: Autenticarse

1. Haz clic en **"🔐 Iniciar Login Facial"**
2. Se abrirá la cámara
3. Presiona **ESPACIO** para capturar tu foto
4. El sistema te reconocerá y te dará acceso si estás registrado

## 📱 Usar desde otro dispositivo

### En el dispositivo servidor (donde está la API):

1. Averigua tu IP local:
```bash
# Linux/Mac
hostname -I | awk '{print $1}'

# Windows
ipconfig
```

2. Asegúrate de que el firewall permita conexiones en el puerto 8000

3. Inicia la API con:
```bash
uvicorn app.main:app --host 0.0.0.0 --port 8000
```

### En el dispositivo cliente:

1. Clona el repositorio
2. Edita el archivo `.env`:
```env
API_URL=http://IP_DEL_SERVIDOR:8000
```
Por ejemplo: `API_URL=http://192.168.1.100:8000`

3. Ejecuta la aplicación:
```bash
java AppLogin
```

## 🔧 Solución de problemas

### La cámara no se abre
- Verifica que no esté siendo usada por otra aplicación
- Verifica permisos de la cámara en tu sistema operativo

### Error de conexión a la API
- Verifica que la API esté corriendo: http://localhost:8000
- Verifica el archivo `.env` tenga la URL correcta
- Si es desde otro dispositivo, verifica que ambos estén en la misma red

### No se reconoce mi rostro
- Asegúrate de estar registrado primero en el sistema
- Mejora la iluminación
- Acércate más a la cámara
- Asegúrate de que solo aparezca un rostro en la imagen

### Error con Supabase
- Verifica que las credenciales en `.env` sean correctas
- Verifica que la tabla `usuarios` exista
- Revisa que el proyecto de Supabase esté activo

## 📁 Estructura del proyecto

```
login-facial-app/
├── app/
│   └── main.py          # API FastAPI con endpoint /login
├── AppLogin.java        # Interfaz gráfica Java
├── login_api.py         # Cliente Python para captura y envío
├── requirements.txt     # Dependencias de Python
├── .env.example         # Plantilla de configuración
├── .env                 # Configuración real (no incluir en git)
├── .gitignore          # Archivos ignorados por git
└── README.md           # Este archivo
```

## 🔐 Seguridad

- **NO** subas el archivo `.env` a GitHub
- Cambia el `SECRET_KEY` en producción
- Usa HTTPS en producción
- Los tokens expiran en 24 horas

## 📄 Licencia

Proyecto académico - PC1

## 👨‍💻 Autor

Tu nombre aquí

## 🤝 Contribuciones

Este es un proyecto académico, pero puedes hacer fork y mejorarlo.
