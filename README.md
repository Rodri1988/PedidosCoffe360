# PedidosCoffee360 - Backend (DSY1107)

Microservicios del sistema de pedidos de cafe con RabbitMQ. El front esta en otro repositorio
(PedidosCoffe360_Front).

| Carpeta | Descripcion | Puerto |
|---|---|---|
| `pedidoscoffee360-backend` | Producer: API REST `/api/pedidos` y publicacion en el Topic Exchange | 8888 |
| `pedidoscoffee360-consumer` | Declara exchanges, colas, bindings y DLQ; consume con ACK/NACK manual | - |
| `pedidoscoffee360-admin` | Administrador de RabbitMQ (Management API con RestClient), prefijo `/Admin` | 8081 |
| `infraestructura` | `docker-compose.yml` con RabbitMQ | 5672 / 15672 |

## Orden para levantar

1. `docker compose up -d` en `infraestructura`
2. `./mvnw spring-boot:run` en `pedidoscoffee360-consumer`
3. `./mvnw spring-boot:run` en `pedidoscoffee360-backend`
4. `./mvnw spring-boot:run` en `pedidoscoffee360-admin`

Prueba: `POST http://localhost:8888/api/pedidos` con `{"tipo":"latte","producto":"Latte Vainilla"}`.
Un producto que contenga `ERROR` termina en la DLQ de su cola.

La version original del backend (EA1, sin RabbitMQ) esta en la rama `main` y en la etiqueta `ea1-original`.
