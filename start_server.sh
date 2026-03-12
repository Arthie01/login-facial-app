#!/bin/bash
# Script para iniciar la API del servidor

echo "🚀 Iniciando API de Login Facial..."
echo ""

# Verificar que exista el entorno virtual
if [ ! -d "venv" ]; then
    echo "❌ Error: No se encontró el entorno virtual."
    echo "   Ejecuta primero: python3 -m venv venv"
    exit 1
fi

# Activar entorno virtual
source venv/bin/activate

# Verificar que exista .env
if [ ! -f ".env" ]; then
    echo "⚠️  Advertencia: No se encontró archivo .env"
    echo "   Copia .env.example a .env y configúralo"
    echo ""
fi

# Iniciar servidor
echo "✅ Servidor iniciando en http://0.0.0.0:8000"
echo "   Acceso local: http://localhost:8000"
echo "   Presiona Ctrl+C para detener"
echo ""

uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
