# ==============================================================================
# Script de Automatización de Git (PowerShell) - Laboratorio Unidad 11
# Simula el flujo de desarrollo orgánico en 6 commits según la rúbrica
# Repositorio Remoto: https://github.com/DevvBP/PLATA_Post1_U11
# ==============================================================================

$RepoUrl = "https://github.com/DevvBP/PLATA_Post1_U11.git"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " [PASO 0] Inicializando entorno de repositorio Git..." -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

if (-not (Test-Path ".git")) {
    git init
    Write-Host "Repositorio Git inicializado exitosamente." -ForegroundColor Green
} else {
    Write-Host "Repositorio Git ya inicializado." -ForegroundColor Yellow
}

git branch -M main

$remotes = git remote
if ($remotes -contains "origin") {
    git remote set-url origin $RepoUrl
} else {
    git remote add origin $RepoUrl
}

Write-Host "Remoto 'origin' configurado hacia: $RepoUrl" -ForegroundColor Green
Write-Host ""

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " [PARTE 1] Commits de Arquitectura Base y Modelo de Datos" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# --- COMMIT 1: Configuración inicial del proyecto y logging ---
Write-Host ">> [1/6] Registrando configuracion del proyecto y Logback..." -ForegroundColor Yellow
git add .gitignore
git add capturas/.gitkeep
git add pedidos/logs/.gitkeep
git add pedidos/pom.xml
git add pedidos/src/main/resources/application.properties
git add pedidos/src/main/resources/logback-spring.xml

git commit -m "feat(config): inicializar configuracion de Spring Boot 3, H2, logback y springdoc"

# --- COMMIT 2: Capa de Dominio, Excepciones y Persistencia ---
Write-Host ">> [2/6] Registrando Capa de Dominio (Entity, Exceptions) y Repositorio..." -ForegroundColor Yellow
git add pedidos/src/main/java/com/tienda/pedidos/entity/Pedido.java
git add pedidos/src/main/java/com/tienda/pedidos/exception/RecursoNoEncontradoException.java
git add pedidos/src/main/java/com/tienda/pedidos/exception/ApiError.java
git add pedidos/src/main/java/com/tienda/pedidos/repository/PedidoRepository.java

git commit -m "feat(domain): implementar entidad Pedido, excepciones de negocio y PedidoRepository"

# --- COMMIT 3: Capa de Transporte (DTOs) y validación ---
Write-Host ">> [3/6] Registrando Capa DTO (Request y Response) y aislamiento de entidad..." -ForegroundColor Yellow
git add pedidos/src/main/java/com/tienda/pedidos/dto/PedidoRequestDTO.java
git add pedidos/src/main/java/com/tienda/pedidos/dto/PedidoResponseDTO.java

git commit -m "feat(dto): desacoplar modelo de datos mediante PedidoRequestDTO y PedidoResponseDTO con validaciones"

Write-Host ""
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " [PARTE 2] Commits de Patrones, Negocio, Web y Documentacion" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# --- COMMIT 4: Patrón Strategy para Descuentos ---
Write-Host ">> [4/6] Registrando implementacion del patron Strategy para descuentos..." -ForegroundColor Yellow
git add pedidos/src/main/java/com/tienda/pedidos/strategy/EstrategiaDescuento.java
git add pedidos/src/main/java/com/tienda/pedidos/strategy/DescuentoRegular.java
git add pedidos/src/main/java/com/tienda/pedidos/strategy/DescuentoVip.java
git add pedidos/src/main/java/com/tienda/pedidos/strategy/DescuentoMayorista.java

git commit -m "feat(strategy): implementar patron Strategy para calculo polimorfico de descuentos segun tipo de cliente"

# --- COMMIT 5: Capa de Negocio (Service) y Logging Seguro ---
Write-Host ">> [5/6] Registrando Servicios de Negocio, Notificaciones y auditoria SLF4J segura..." -ForegroundColor Yellow
git add pedidos/src/main/java/com/tienda/pedidos/service/NotificacionService.java
git add pedidos/src/main/java/com/tienda/pedidos/service/NotificacionEmailServiceImpl.java
git add pedidos/src/main/java/com/tienda/pedidos/service/PedidoService.java
git add pedidos/src/main/java/com/tienda/pedidos/service/PedidoServiceImpl.java

git commit -m "feat(service): implementar PedidoService y NotificacionService con inyeccion de dependencias y logging seguro"

# --- COMMIT 6: Capa Web, Manejador Global, OpenAPI y Documentación ---
Write-Host ">> [6/6] Registrando Capa Web (Controller), GlobalExceptionHandler, OpenAPI y README..." -ForegroundColor Yellow
git add pedidos/src/main/java/com/tienda/pedidos/controller/PedidoController.java
git add pedidos/src/main/java/com/tienda/pedidos/exception/GlobalExceptionHandler.java
git add pedidos/src/main/java/com/tienda/pedidos/PedidosApplication.java
git add README.md
git add script_git.sh
git add script_git.ps1

git commit -m "feat(web): implementar PedidoController, GlobalExceptionHandler, documentacion OpenAPI y README final"

Write-Host ""
Write-Host "==========================================================" -ForegroundColor Green
Write-Host " [RESUMEN DE COMMITS REALIZADOS]" -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Green
git log --oneline -n 6
