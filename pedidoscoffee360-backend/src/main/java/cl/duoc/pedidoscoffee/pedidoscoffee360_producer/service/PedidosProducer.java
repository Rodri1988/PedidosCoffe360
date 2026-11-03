package cl.duoc.pedidoscoffee.pedidoscoffee360_producer.service;

import cl.duoc.pedidoscoffee.pedidoscoffee360_producer.model.Pedido;
import cl.duoc.pedidoscoffee.pedidoscoffee360_producer.config.RabbitNames;
import org.springframework.stereotype.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Service
public class PedidosProducer{

    private final RabbitTemplate rabbitTemplate;

    public PedidosProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }
    public void enviar(Pedido pedido){
        String tipo = pedido.getTipo() == null ? "desconocido" : pedido.getTipo().toLowerCase();
        String estado = pedido.getEstado() == null ? "creado" : pedido.getEstado().toLowerCase();

        String mensaje = pedido.getId() + "|" +
                    pedido.getTipo() + "|" +
                    pedido.getUbicacion() + "|" +
                    pedido.getDescripcion() + "|" +
                    pedido.getEstado();

        rabbitTemplate.convertAndSend(RabbitNames.EXCHANGE,
                                        "pedido." + tipo + "." + estado,
                                        mensaje);
        System.out.println("[PRODUCER] Pedido publicado: " + mensaje);
    }
}
