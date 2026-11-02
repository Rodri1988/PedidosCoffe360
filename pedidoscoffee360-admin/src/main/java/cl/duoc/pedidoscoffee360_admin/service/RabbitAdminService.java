package cl.duoc.pedidoscoffee360_admin.service;

import cl.duoc.pedidoscoffee360_admin.dto.BindingRequest;
import cl.duoc.pedidoscoffee360_admin.dto.ExchangeRequest;
import cl.duoc.pedidoscoffee360_admin.dto.QueueRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class RabbitAdminService {

    private final RestClient client;
    private final String vhost;

    public RabbitAdminService(RestClient rabbitAdminRestClient,
                              @Value("${rabbit.admin.vhost:/}") String vhost) {
        this.client = rabbitAdminRestClient;
        this.vhost = vhost;
    }

    //Consultar todas las colas de RabbitMQ
    public String listarColas() {
        return client.get()
            .uri("/queues")
            .retrieve() //traer
            .body(String.class);
    }

    //Consultar todos los Exchanges de RabbitMQ
    public String listarExchanges() {
        return client.get()
            .uri("/exchanges")
            .retrieve()
            .body(String.class);
    }

    public String listarBindings() {
        return client.get()
            .uri("/bindings")
            .retrieve()
            .body(String.class);
    }

    //Consultar el Overview de RabbitMQ (en la API es "overview", singular)
    public String listarOverviews() {
        return client.get()
            .uri("/overview")
            .retrieve()
            .body(String.class);
    }

    public void crearCola(QueueRequest req) {
        Map<String, Object> arguments = new LinkedHashMap<>();
        if (tieneTexto(req.deadLetterExchange())) {
            arguments.put("x-dead-letter-exchange", req.deadLetterExchange());
        }
        if (tieneTexto(req.deadLetterRoutingKey())) {
            arguments.put("x-dead-letter-routing-key", req.deadLetterRoutingKey());
        }
        if (req.messageTtl() != null) {
            arguments.put("x-message-ttl", req.messageTtl());
        }
        if (tieneTexto(req.queueType())) {
            arguments.put("x-queue-type", req.queueType());
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("durable", valorOpcional(req.durable(), true));
        body.put("auto_delete", valorOpcional(req.autoDelete(), false));
        body.put("arguments", arguments);

        client.put()
            .uri("/queues/{vhost}/{name}", vhost, req.name())
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }

    public void eliminarCola(String nombre) {
        exigirTexto(nombre, "El nombre de la cola es obligatorio");
        client.delete()
            .uri("/queues/{vhost}/{name}", vhost, nombre)
            .retrieve()
            .toBodilessEntity();
    }

    public void crearExchange(ExchangeRequest req) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", req.type());
        body.put("durable", valorOpcional(req.durable(), true));
        body.put("auto_delete", valorOpcional(req.autoDelete(), false));
        body.put("internal", false);
        body.put("arguments", Map.of());

        client.put()
            .uri("/exchanges/{vhost}/{name}", vhost, req.name())
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }

    public void eliminarExchange(String nombre) {
        exigirTexto(nombre, "El nombre del exchange es obligatorio");
        client.delete()
            .uri("/exchanges/{vhost}/{name}", vhost, nombre)
            .retrieve()
            .toBodilessEntity();
    }

    public void crearBinding(BindingRequest req) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("routing_key", req.routingKey() == null ? "" : req.routingKey());
        body.put("arguments", Map.of());

        client.post()
            .uri("/bindings/{vhost}/e/{exchange}/q/{queue}", vhost, req.exchange(), req.queue())
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .toBodilessEntity();
    }

    public void eliminarBinding(String exchange, String cola, String routingKey) {
        exigirTexto(exchange, "El exchange es obligatorio");
        exigirTexto(cola, "La cola es obligatoria");
        String buscada = routingKey == null ? "" : routingKey;

        List<Map<String, Object>> bindings = client.get()
            .uri("/bindings/{vhost}/e/{exchange}/q/{queue}", vhost, exchange, cola)
            .retrieve()
            .body(new ParameterizedTypeReference<List<Map<String, Object>>>() { });

        String propertiesKey = bindings == null ? null : bindings.stream()
            .filter(b -> buscada.equals(String.valueOf(b.get("routing_key"))))
            .map(b -> String.valueOf(b.get("properties_key")))
            .findFirst()
            .orElse(null);

        if (propertiesKey == null) {
            throw new NoSuchElementException("No existe un binding de '" + exchange + "' a '" + cola
                + "' con la routing key '" + buscada + "'");
        }

        client.delete()
            .uri("/bindings/{vhost}/e/{exchange}/q/{queue}/{props}", vhost, exchange, cola, propertiesKey)
            .retrieve()
            .toBodilessEntity();
    }

    private static boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static boolean valorOpcional(Boolean valor, boolean porDefecto) {
        return valor == null ? porDefecto : valor;
    }

    private static void exigirTexto(String valor, String mensaje) {
        if (!tieneTexto(valor)) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
