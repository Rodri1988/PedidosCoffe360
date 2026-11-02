package cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.listener;
import cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.config.RabbitNames;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import java.io.IOException;

@Component
public class EspressoConsumer{
    @RabbitListener(queues = RabbitNames.ESPRESSO_QUEUE)
    public void recibir(String mensaje,
        Message message,
        Channel channel)throws IOException{

        
        System.out.println("[ESPRESSO] Pedido Recibido: " + mensaje);
        long deliveryTag = 0   ;

        try{
            deliveryTag = message.getMessageProperties().getDeliveryTag();
            //Simulamos el procesamiento del pedido
            if(mensaje.contains("ERROR")){
                throw new RuntimeException("Error al procesar el pedido");
                //Comfirmamos con rabitmq que el mensaje fue procesado correctamente
            }
            System.out.println("[ESPRESSO] Pedido fue procesado...");

        }

        catch(RuntimeException ex){
            System.out.println("[ESPRESSO] Error al procesar el pedido: " + ex.getMessage());
            //Rechazamos el mensaje y no lo reencolamos
            channel.basicNack(deliveryTag, false, false);
            System.out.println("[ESPRESSO] NACK Enviada: ");
            return;
            //Si queremos reencolar el mensaje, podemos usar basicNack con requeue = true
            
        }
        //Comfirmamos con rabitmq que el mensaje fue procesado correctamente
        channel.basicAck(deliveryTag, false);
        System.out.println("[ESPRESSO] ACK Enviada: ");
    
    }
}
