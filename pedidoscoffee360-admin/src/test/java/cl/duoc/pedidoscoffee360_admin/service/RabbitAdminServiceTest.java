package cl.duoc.pedidoscoffee360_admin.service;

import cl.duoc.pedidoscoffee360_admin.dto.BindingRequest;
import cl.duoc.pedidoscoffee360_admin.dto.QueueRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class RabbitAdminServiceTest {

    private static final String BASE = "http://localhost:15672/api";

    private MockRestServiceServer server;
    private RabbitAdminService service;

    @BeforeEach
    void preparar() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE);
        server = MockRestServiceServer.bindTo(builder).build();
        service = new RabbitAdminService(builder.build(), "/");
    }

    @Test
    void listarColasHaceGetAQueues() {
        server.expect(requestTo(BASE + "/queues"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        service.listarColas();
        server.verify();
    }

    @Test
    void crearColaUsaPutConVhostCodificadoYArgumentosDlq() {
        server.expect(requestTo(BASE + "/queues/%2F/pedidos.barista.espresso"))
            .andExpect(method(HttpMethod.PUT))
            .andExpect(content().string(containsString("\"durable\":true")))
            .andExpect(content().string(containsString("x-dead-letter-exchange")))
            .andRespond(withNoContent());

        service.crearCola(new QueueRequest("pedidos.barista.espresso", null, null,
            "pedidos.dlx", "pedidos.barista.espresso", null, null));
        server.verify();
    }

    @Test
    void eliminarColaUsaDelete() {
        server.expect(requestTo(BASE + "/queues/%2F/cola.prueba"))
            .andExpect(method(HttpMethod.DELETE))
            .andRespond(withNoContent());

        service.eliminarCola("cola.prueba");
        server.verify();
    }

    @Test
    void crearBindingUsaPostConRoutingKey() {
        server.expect(requestTo(BASE + "/bindings/%2F/e/pedidos.exchange/q/pedidos.auditoria"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().string(containsString("\"routing_key\":\"pedido.#\"")))
            .andRespond(withSuccess());

        service.crearBinding(new BindingRequest("pedidos.exchange", "pedidos.auditoria", "pedido.#"));
        server.verify();
    }
}
