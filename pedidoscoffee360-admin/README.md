# pedidoscoffee360-admin

Microservicio administrador de RabbitMQ para PedidosCoffee360 (DSY1107). Consume la
Management API de RabbitMQ (HTTP, puerto 15672) con `RestClient`.

Estructura: `config` (cliente HTTP) -> `service` (logica de administracion) -> `controller` (API REST),
mas `dto` (validacion de entrada) y `exception` (respuestas de error claras).

## Ejecutar

1. Levantar RabbitMQ (`docker compose up -d` en `infraestructura`).
2. `./mvnw spring-boot:run` (puerto **8081**).

Credenciales: variables de entorno `RABBIT_ADMIN_USER` y `RABBIT_ADMIN_PASSWORD`
(si no existen se usa `guest/guest`, solo para laboratorio local).

## Endpoints

| Metodo | Ruta | Descripcion |
|---|---|---|
| GET | `/Admin/queues` `/Admin/exchanges` `/Admin/bindings` `/Admin/overview` | Consultas |
| POST | `/Admin/queues` | Crear cola |
| DELETE | `/Admin/queues/{name}` | Eliminar cola |
| POST | `/Admin/exchanges` | Crear exchange (direct, topic, fanout, headers) |
| DELETE | `/Admin/exchanges/{name}` | Eliminar exchange |
| POST | `/Admin/bindings` | Crear binding exchange -> cola |
| DELETE | `/Admin/bindings?exchange=&queue=&routingKey=` | Eliminar binding |

Ejemplos de cuerpo (POST):

```json
{ "name": "pedidos.barista.espresso", "durable": true,
  "deadLetterExchange": "pedidos.dlx", "deadLetterRoutingKey": "pedidos.barista.espresso" }
```
```json
{ "name": "pedidos.exchange", "type": "topic" }
```
```json
{ "exchange": "pedidos.exchange", "queue": "pedidos.auditoria", "routingKey": "pedido.#" }
```

Un nombre vacio o un dato invalido responde `400` con el detalle por campo.
