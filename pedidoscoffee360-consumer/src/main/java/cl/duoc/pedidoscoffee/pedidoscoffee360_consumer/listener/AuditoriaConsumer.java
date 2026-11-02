package cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.listener;
import cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.config.RabbitNames;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import java.io.IOException;

@Component
public class AuditoriaConsumer{
    @RabbitListener(queues = RabbitNames.AUDITORIA_QUEUE)
    public void recibir(String mensaje,
        Message message,
        Channel channel)throws IOException{

        
        System.out.println("[AUDITORIA] Pedido Recibido: " + mensaje);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //Comfirmamos con rabitmq que el mensaje fue procesado correctamente
        channel.basicAck(deliveryTag, false);
        System.out.println("[AUDITORIA] ACK Enviada: ");
    
    }
}
