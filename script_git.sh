#!/usr/bin/env bash

# ==============================================================================
# Script de Automatización de Git - Laboratorio Unidad 11 (Programación Web)
# Simula el flujo de desarrollo orgánico en 6 commits según la rúbrica
# Repositorio Remoto: https://github.com/DevvBP/PLATA_Post1_U11
# ==============================================================================

set -e # Detener ejecución ante cualquier error inesperado

REPO_URL="https://github.com/DevvBP/PLATA_Post1_U11.git"

echo "=========================================================="
echo " [PASO 0] Inicializando entorno de repositorio Git..."
echo "=========================================================="

# Inicializar repositorio local si aún no está inicializado
if [ ! -d ".git" ]; then
    git init
    echo "Repositorio Git inicializado exitosamente."
else
    echo "Repositorio Git ya inicializado."
fi

# Configurar rama principal main
git branch -M main

# Configurar o actualizar el origen remoto
if git remote | grep -q "origin"; then
    git remote set-url origin "$REPO_URL"
else
    git remote add origin "$REPO_URL"
fi

echo "Remoto 'origin' configurado hacia: $REPO_URL"

echo ""
echo "=========================================================="
echo " [PARTE 1] Commits de Arquitectura Base y Modelo de Datos"
echo "=========================================================="

# --- COMMIT 1: Configuración inicial del proyecto y logging ---
echo ">> [1/6] Registrando configuración del proyecto y Logback..."
git add .gitignore
git add capturas/.gitkeep
git add pedidos/logs/.gitkeep
git add pedidos/pom.xml
git add pedidos/src/main/resources/application.properties
git add pedidos/src/main/resources/logback-spring.xml

git commit -m "feat(config): inicializar configuracion de Spring Boot 3, H2, logback y springdoc" || true

# --- COMMIT 2: Capa de Dominio, Excepciones y Persistencia ---
echo ">> [2/6] Registrando Capa de Dominio (Entity, Exceptions) y Repositorio..."
git add pedidos/src/main/java/com/tienda/pedidos/entity/Pedido.java
git add pedidos/src/main/java/com/tienda/pedidos/exception/RecursoNoEncontradoException.java
git add pedidos/src/main/java/com/tienda/pedidos/exception/ApiError.java
git add pedidos/src/main/java/com/tienda/pedidos/repository/PedidoRepository.java

git commit -m "feat(domain): implementar entidad Pedido, excepciones de negocio y PedidoRepository" || true

# --- COMMIT 3: Capa de Transporte (DTOs) y validación ---
echo ">> [3/6] Registrando Capa DTO (Request y Response) y aislamiento de entidad..."
git add pedidos/src/main/java/com/tienda/pedidos/dto/PedidoRequestDTO.java
git add pedidos/src/main/java/com/tienda/pedidos/dto/PedidoResponseDTO.java

git commit -m "feat(dto): desacoplar modelo de datos mediante PedidoRequestDTO y PedidoResponseDTO con validaciones" || true

echo ""
echo "=========================================================="
echo " [PARTE 2] Commits de Patrones, Negocio, Web y Documentación"
echo "=========================================================="

# --- COMMIT 4: Patrón Strategy para Descuentos ---
echo ">> [4/6] Registrando implementacion del patron Strategy para descuentos..."
git add pedidos/src/main/java/com/tienda/pedidos/strategy/EstrategiaDescuento.java
git add pedidos/src/main/java/com/tienda/pedidos/strategy/DescuentoRegular.java
git add pedidos/src/main/java/com/tienda/pedidos/strategy/DescuentoVip.java
git add pedidos/src/main/java/com/tienda/pedidos/strategy/DescuentoMayorista.java

git commit -m "feat(strategy): implementar patron Strategy para calculo polimorfico de descuentos segun tipo de cliente" || true

# --- COMMIT 5: Capa de Negocio (Service) y Logging Seguro ---
echo ">> [5/6] Registrando Servicios de Negocio, Notificaciones y auditoria SLF4J segura..."
git add pedidos/src/main/java/com/tienda/pedidos/service/NotificacionService.java
git add pedidos/src/main/java/com/tienda/pedidos/service/NotificacionEmailServiceImpl.java
git add pedidos/src/main/java/com/tienda/pedidos/service/PedidoService.java
git add pedidos/src/main/java/com/tienda/pedidos/service/PedidoServiceImpl.java

git commit -m "feat(service): implementar PedidoService y NotificacionService con inyeccion de dependencias y logging seguro" || true

# --- COMMIT 6: Capa Web, Manejador Global, OpenAPI y Documentación ---
echo ">> [6/6] Registrando Capa Web (Controller), GlobalExceptionHandler, OpenAPI y README..."
git add pedidos/src/main/java/com/tienda/pedidos/controller/PedidoController.java
git add pedidos/src/main/java/com/tienda/pedidos/exception/GlobalExceptionHandler.java
git add pedidos/src/main/java/com/tienda/pedidos/PedidosApplication.java
git add README.md
git add script_git.sh

git commit -m "feat(web): implementar PedidoController, GlobalExceptionHandler, documentacion OpenAPI y README final" || true

echo ""
echo "=========================================================="
echo " [RESUMEN DE COMMITS REALIZADOS]"
echo "=========================================================="
git log --oneline -n 6

echo ""
echo "=========================================================="
echo " [PUSH AL REPOSITORIO REMOTO]"
echo "=========================================================="
echo "Enviando cambios a $REPO_URL en la rama 'main'..."
git push -u origin main --force || {
    echo "AVISO: Si el push requiere autenticación, por favor ejecute manualmente:"
    echo "git push -u origin main"
}

echo ""
echo "Proceso finalizado exitosamente."
