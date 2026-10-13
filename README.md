# Laboratorio Unidad 11: Arquitectura Limpia, Patrones de Diseño y Refactorización

## Proyecto: Módulo de Gestión de Pedidos (`pedidos`)

Este proyecto corresponde a la refactorización integral del módulo de pedidos para el curso de Programación Web. El código original presentaba alto acoplamiento, SQL embebido, violaciones a los principios SOLID (específicamente SRP y DIP), y exponía la entidad JPA directamente hacia los clientes HTTP, comprometiendo datos sensibles.

La versión actual implementa **Clean Architecture**, **Patrón Strategy**, **Inversión de Dependencias (DIP)**, **Objetos de Transferencia de Datos (DTO)**, **Logging Centralizado con SLF4J/Logback** y **Documentación Interactiva con SpringDoc OpenAPI (Swagger)**.

---

## 1. Decisiones de Diseño y Arquitectura

### 1.1. Patrón Strategy vs. Factory Pattern
* **Decisión tomada:** Se implementó el patrón **Strategy** utilizando la inyección polimórfica de Spring Framework (`Map<String, EstrategiaDescuento>`).
* **Justificación técnica:**
  * **Principio Abierto/Cerrado (OCP):** Permite añadir nuevos tipos de clientes y esquemas de descuento (ejemplo: `CORPORATIVO`, `ESTUDIANTE`) simplemente creando una nueva clase anotada con `@Component("NUEVO_TIPO")` que implemente la interfaz `EstrategiaDescuento`, sin necesidad de modificar el código del servicio ni recurrir a sentencias condicionales `if-else` o `switch-case`.
  * **Diferencia con Factory:** Un Factory clásico suele centralizar la creación del objeto mediante condicionales internos. El patrón Strategy apoyado en el contenedor IoC de Spring desacopla completamente tanto la instanciación como la selección, permitiendo que Spring resuelva el bean correspondiente por nombre automáticamente en tiempo de ejecución.

### 1.2. Logging Centralizado y Control de Auditoría
* **Decisión tomada:** Configuración de **SLF4J** con **Logback** mediante `logback-spring.xml`.
* **Justificación técnica:**
  * **Separación de flujos:** Se implementaron dos appenders: `ConsoleAppender` para visualización inmediata en tiempo de desarrollo y `RollingFileAppender` para persistencia en disco con rotación diaria (`logs/pedidos-%d{yyyy-MM-dd}.log`) y política de retención de 30 días.
  * **Trazabilidad sin impacto de rendimiento:** El uso de loggers parametrizados (`log.info("Pedido registrado: {}", id)`) evita la concatenación innecesaria de cadenas en memoria cuando los niveles de log están desactivados.
  * **Seguridad operativa:** El logging está centralizado en la capa de servicio y controlador de excepciones, asegurando uniformidad en los eventos críticos (creación, avisos por tipos de cliente no reconocidos y captura global de excepciones).

### 1.3. Ocultamiento de `notasInternas` y Capa DTO (Seguridad y Menor Exposición)
* **Decisión tomada:** Separación estricta entre la entidad de persistencia (`Pedido`) y el modelo de transporte (`PedidoRequestDTO` y `PedidoResponseDTO`), con mapeo manual en la capa de servicio.
* **Justificación técnica:**
  * **Prevención de Information Disclosure (OWASP):** Las `notasInternas` pueden contener información sensible del negocio (motivos de autorización de descuentos, historial crediticio, anotaciones de cobro). Exponer la entidad JPA directamente enviaría dicho campo en formato JSON al cliente. Al utilizar `PedidoResponseDTO`, este atributo queda completamente omitido de la respuesta.
  * **No-fuga en Logs:** En `PedidoServiceImpl`, el registro de logs explícitamente audita el ID, email, tipo de cliente y total facturado, garantizando que las notas internas nunca queden impresas en los archivos de texto ni en consolas de monitoreo.

### 1.4. Principios SOLID Aplicados
* **Single Responsibility Principle (SRP):** Cada clase tiene una única razón de cambio: el repositorio solo persiste; las estrategias solo calculan montos; el servicio orquestará reglas de negocio; el servicio de notificaciones gestiona avisos; el controlador atiende peticiones HTTP; y el `GlobalExceptionHandler` captura errores.
* **Open/Closed Principle (OCP):** El sistema está abierto a la extensión (nuevas estrategias de descuento) y cerrado a la modificación.
* **Dependency Inversion Principle (DIP):** El servicio depende de abstracciones (`PedidoRepository`, `NotificacionService`, `EstrategiaDescuento`), permitiendo sustituir implementaciones o mockear dependencias en pruebas unitarias con facilidad.

---

## 2. Estructura del Proyecto

```text
plata-post1-u11/
├── capturas/                     # Evidencias graficas del laboratorio
│   └── .gitkeep
├── pedidos/                      # Proyecto Spring Boot
│   ├── pom.xml
│   ├── logs/                     # Directorio de logs rotados
│   │   └── .gitkeep
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── tienda/
│           │           └── pedidos/
│           │               ├── PedidosApplication.java
│           │               ├── controller/
│           │               │   └── PedidoController.java
│           │               ├── dto/
│           │               │   ├── PedidoRequestDTO.java
│           │               │   └── PedidoResponseDTO.java
│           │               ├── entity/
│           │               │   └── Pedido.java
│           │               ├── exception/
│           │               │   ├── ApiError.java
│           │               │   ├── GlobalExceptionHandler.java
│           │               │   └── RecursoNoEncontradoException.java
│           │               ├── repository/
│           │               │   └── PedidoRepository.java
│           │               ├── service/
│           │               │   ├── NotificacionEmailServiceImpl.java
│           │               │   ├── NotificacionService.java
│           │               │   ├── PedidoService.java
│           │               │   └── PedidoServiceImpl.java
│           │               └── strategy/
│           │                   ├── DescuentoMayorista.java
│           │                   ├── DescuentoRegular.java
│           │                   ├── DescuentoVip.java
│           │                   └── EstrategiaDescuento.java
│           └── resources/
│               ├── application.properties
│               └── logback-spring.xml
├── .gitignore
├── README.md
└── script_git.sh
```

---

## 3. Instrucciones de Compilación y Ejecución

### Prerrequisitos
* Java Development Kit (JDK) 17 o superior instalado y configurado en el `PATH`.
* Apache Maven 3.8+ (o el wrapper de Maven).

### Compilación y Construcción
Desde la carpeta `pedidos`:
```bash
cd pedidos
mvn clean install
```

### Ejecución de la Aplicación
```bash
mvn spring-boot:run
```
La aplicación iniciará en el puerto local `8080`.

---

## 4. Pruebas y Verificación

### 4.1. Swagger UI y Documentación OpenAPI
Una vez iniciada la aplicación, acceda a la documentación interactiva:
* **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **Especificación OpenAPI (JSON):** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)
* **Consola H2 Database:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  * **JDBC URL:** `jdbc:h2:mem:pedidosdb`
  * **User Name:** `sa`
  * **Password:** *(en blanco)*

### 4.2. Ejemplos de Peticiones HTTP

#### Crear Pedido (Cliente VIP - 15% Descuento)
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteEmail": "andrea.vip@tienda.com",
    "tipoCliente": "VIP",
    "subtotal": 200000.0,
    "notasInternas": "Cliente preferencial desde 2021"
  }'
```
**Respuesta esperada (HTTP 201 Created):**
```json
{
  "id": 1,
  "clienteEmail": "andrea.vip@tienda.com",
  "tipoCliente": "VIP",
  "subtotal": 200000.0,
  "descuento": 30000.0,
  "iva": 32300.0,
  "total": 202300.0,
  "estado": "PROCESADO",
  "fechaCreacion": "2026-10-13 16:30:00"
}
```
*(Nótese que `notasInternas` no aparece en la respuesta).*

#### Crear Pedido (Cliente Mayorista - 25% Descuento)
```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "clienteEmail": "distribuciones@comercio.com",
    "tipoCliente": "MAYORISTA",
    "subtotal": 1000000.0,
    "notasInternas": "Pedido con despacho urgente a bodega principal"
  }'
```

#### Consultar Pedido por ID
```bash
curl -X GET http://localhost:8080/api/pedidos/1
```

#### Probar Manejo Global de Excepciones (404 Not Found)
```bash
curl -X GET http://localhost:8080/api/pedidos/999
```
**Respuesta:**
```json
{
  "status": 404,
  "error": "Not Found",
  "mensaje": "No se encontro ningun pedido registrado con el ID: 999",
  "timestamp": "2026-10-13 16:35:12",
  "path": "/api/pedidos/999"
}
```

### 4.3. Verificación de Logging y Rotación
Al realizar las peticiones, revise el archivo de logs generado automáticamente en `pedidos/logs/pedidos.log`:
```text
2026-10-13 16:30:00.125 [http-nio-8080-exec-1] INFO  c.t.p.s.PedidoServiceImpl - Pedido registrado exitosamente con ID #1. Cliente: andrea.vip@tienda.com, Tipo: VIP, Total: $202300.0
2026-10-13 16:30:00.126 [http-nio-8080-exec-1] INFO  c.t.p.s.NotificacionEmailServiceImpl - Simulando envio de correo electronico de confirmacion a 'andrea.vip@tienda.com' para el pedido ID #1 por valor de $202300.0
```
Se evidencia que el nivel de severidad se cumple y en ningún momento se compromete información confidencial.
