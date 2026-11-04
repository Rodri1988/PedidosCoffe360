package cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.listener;
import cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.config.RabbitNames;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import java.io.IOException;

@Component
public class LecheConsumer{

    //Cantidad maxima de intentos sobre UNA misma entrega (si todos fallan, llega UN mensaje a la DLQ)
    private static final int MAX_INTENTOS = 3;

    @RabbitListener(queues = RabbitNames.LECHE_QUEUE)
    public void recibir(String mensaje,
        Message message,
        Channel channel)throws IOException{

        System.out.println("[LECHE] Pedido Recibido: " + mensaje);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        for(int intento = 1; intento <= MAX_INTENTOS; intento++){
            try{
                procesar(mensaje);
                //Confirmamos con rabbitmq que el mensaje fue procesado correctamente
                channel.basicAck(deliveryTag, false);
                System.out.println("[LECHE] ACK Enviada: ");
                return;
            }
            catch(IllegalArgumentException ex){
                //Error NO recuperable: el pedido es invalido, reintentar no lo arregla
                System.out.println("[LECHE] Error no recuperable: " + ex.getMessage());
                //Rechazamos el mensaje y no lo reencolamos, va a la DLQ
                channel.basicNack(deliveryTag, false, false);
                System.out.println("[LECHE] NACK Enviada: pedido enviado a la DLQ");
                return;
            }
            catch(RuntimeException ex){
                //Error recuperable: puede ser una falla temporal, se reintenta
                System.out.println("[LECHE] Intento " + intento + " fallo: " + ex.getMessage());
                if(intento == MAX_INTENTOS){
                    //Se acabaron los intentos, rechazamos el mensaje y no lo reencolamos, va a la DLQ
                    channel.basicNack(deliveryTag, false, false);
                    System.out.println("[LECHE] NACK Enviada: sin intentos, pedido enviado a la DLQ");
                    return;
                }
                try{
                    Thread.sleep(1000L * intento);
                }
                catch(InterruptedException interrumpido){
                    Thread.currentThread().interrupt();
                    channel.basicNack(deliveryTag, false, false);
                    System.out.println("[LECHE] NACK Enviada: proceso interrumpido, pedido enviado a la DLQ");
                    return;
                }
            }
        }
    }

    //Simulamos el procesamiento del pedido
    private void procesar(String mensaje){
        if(mensaje.contains("ERROR")){
            throw new IllegalArgumentException("Error al procesar el pedido");
        }
        if(mensaje.contains("REINTENTO")){
            throw new IllegalStateException("Falla temporal simulada");
        }
        System.out.println("[LECHE] Pedido fue procesado...");
    }
}
