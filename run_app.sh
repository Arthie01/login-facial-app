#!/bin/bash
# Script para compilar y ejecutar la aplicación de login

echo "🔨 Compilando aplicación Java..."
javac AppLogin.java

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo "🚀 Iniciando aplicación..."
    echo ""
    java AppLogin
else
    echo "❌ Error en la compilación"
    exit 1
fi
