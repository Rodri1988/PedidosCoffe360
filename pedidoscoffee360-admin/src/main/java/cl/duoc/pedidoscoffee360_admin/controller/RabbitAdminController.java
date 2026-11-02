package cl.duoc.pedidoscoffee360_admin.controller;

import cl.duoc.pedidoscoffee360_admin.dto.BindingRequest;
import cl.duoc.pedidoscoffee360_admin.dto.ExchangeRequest;
import cl.duoc.pedidoscoffee360_admin.dto.QueueRequest;
import cl.duoc.pedidoscoffee360_admin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/Admin")
public class RabbitAdminController {

    private final RabbitAdminService service;

    public RabbitAdminController(RabbitAdminService service) {
        this.service = service;
    }

    @GetMapping(value = "/queues", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listarColas() {
        return ResponseEntity.ok(service.listarColas());
    }

    @GetMapping(value = "/exchanges", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listarExchanges() {
        return ResponseEntity.ok(service.listarExchanges());
    }

    @GetMapping(value = "/bindings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listarBindings() {
        return ResponseEntity.ok(service.listarBindings());
    }

    @GetMapping(value = "/overview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listarOverviews() {
        return ResponseEntity.ok(service.listarOverviews());
    }

    @PostMapping(value = "/queues", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> crearCola(@Valid @RequestBody QueueRequest request) {
        service.crearCola(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("mensaje", "Cola '" + request.name() + "' creada"));
    }

    @DeleteMapping("/queues/{name}")
    public ResponseEntity<Void> eliminarCola(@PathVariable("name") String name) {
        service.eliminarCola(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/exchanges", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> crearExchange(@Valid @RequestBody ExchangeRequest request) {
        service.crearExchange(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("mensaje", "Exchange '" + request.name() + "' (" + request.type() + ") creado"));
    }

    @DeleteMapping("/exchanges/{name}")
    public ResponseEntity<Void> eliminarExchange(@PathVariable("name") String name) {
        service.eliminarExchange(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/bindings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> crearBinding(@Valid @RequestBody BindingRequest request) {
        service.crearBinding(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("mensaje", "Binding '" + request.exchange() + "' -> '" + request.queue() + "' creado"));
    }

    @DeleteMapping("/bindings")
    public ResponseEntity<Void> eliminarBinding(@RequestParam("exchange") String exchange,
                                                @RequestParam("queue") String queue,
                                                @RequestParam(name = "routingKey", defaultValue = "") String routingKey) {
        service.eliminarBinding(exchange, queue, routingKey);
        return ResponseEntity.noContent().build();
    }
}
