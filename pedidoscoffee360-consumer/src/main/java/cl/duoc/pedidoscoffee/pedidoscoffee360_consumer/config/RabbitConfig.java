package cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig{
    //configuracion para caso topic Echange
    @Bean
    public TopicExchange pedidosExchange(){
        return new TopicExchange(RabbitNames.EXCHANGE,true,false);
    }
    @Bean
    public Queue espressoQueue(){
        return QueueBuilder.durable(RabbitNames.ESPRESSO_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitNames.ESPRESSO_DLX)
                .withArgument("x-dead-letter-routing-key", RabbitNames.ESPRESSO_QUEUE)
                .build();
    }
    @Bean
    public Queue lecheQueue(){
        return QueueBuilder.durable(RabbitNames.LECHE_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitNames.LECHE_DLX)
                .withArgument("x-dead-letter-routing-key", RabbitNames.LECHE_QUEUE)
                .build();
    }
    @Bean
    public Queue notificacionesQueue(){
        return QueueBuilder.durable(RabbitNames.NOTIFICACIONES_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitNames.NOTIFICACIONES_DLX)
                .withArgument("x-dead-letter-routing-key", RabbitNames.NOTIFICACIONES_QUEUE)
                .build();
    }
    @Bean
    public Queue auditoriaQueue(){
        return QueueBuilder.durable(RabbitNames.AUDITORIA_QUEUE).build();
    }
    @Bean
    public Binding espressoBinding(Queue espressoQueue, TopicExchange pedidosExchange){
        return BindingBuilder
        .bind(espressoQueue)
        .to(pedidosExchange)
        .with(RabbitNames.RK_ESPRESSO);
    }
    @Bean
    public Binding americanoBinding(Queue espressoQueue, TopicExchange pedidosExchange){
        return BindingBuilder
        .bind(espressoQueue)
        .to(pedidosExchange)
        .with(RabbitNames.RK_AMERICANO);
    }
    @Bean
    public Binding latteBinding(Queue lecheQueue, TopicExchange pedidosExchange){
        return BindingBuilder
        .bind(lecheQueue)
        .to(pedidosExchange)
        .with(RabbitNames.RK_LATTE);
    }
    @Bean
    public Binding capuccinoBinding(Queue lecheQueue, TopicExchange pedidosExchange){
        return BindingBuilder
        .bind(lecheQueue)
        .to(pedidosExchange)
        .with(RabbitNames.RK_CAPUCCINO);
    }
    @Bean
    public Binding mochaBinding(Queue lecheQueue, TopicExchange pedidosExchange){
        return BindingBuilder
        .bind(lecheQueue)
        .to(pedidosExchange)
        .with(RabbitNames.RK_MOCHA);
    }
    @Bean
    public Binding notificacionesBinding(Queue notificacionesQueue, TopicExchange pedidosExchange){
        return BindingBuilder
        .bind(notificacionesQueue)
        .to(pedidosExchange)
        .with(RabbitNames.RK_LISTOS);
    }
    @Bean
    public Binding auditoriaBinding(Queue auditoriaQueue, TopicExchange pedidosExchange){
        return BindingBuilder
        .bind(auditoriaQueue)
        .to(pedidosExchange)
        .with(RabbitNames.RK_TODOS);
    }

    //Actualizar ACK y NACK para que el consumidor pueda manejar los mensajes de manera manual, en caso de que falle el procesamiento del mensaje, se puede rechazar el mensaje y no reencolarlo, para que no se pierda el mensaje. Esto se hace con la propiedad spring.rabbitmq.listener.simple.acknowledge-mode=manual en application.properties
    @Bean 
    public DirectExchange espressoDlx(){
        return new DirectExchange(RabbitNames.ESPRESSO_DLX,true,false);
    }
    @Bean
    public Queue espressoDlq(){
        return QueueBuilder.durable(RabbitNames.ESPRESSO_DLQ).build();
    }
    @Bean
    public Binding espressoDlqBinding(Queue espressoDlq, DirectExchange espressoDlx){
        return BindingBuilder
        .bind(espressoDlq)
        .to(espressoDlx)
        .with(RabbitNames.ESPRESSO_QUEUE);
    }

    //Actualizar ACK y NACK para que el consumidor pueda manejar los mensajes de manera manual, en caso de que falle el procesamiento del mensaje, se puede rechazar el mensaje y no reencolarlo, para que no se pierda el mensaje. Esto se hace con la propiedad spring.rabbitmq.listener.simple.acknowledge-mode=manual en application.properties
    @Bean 
    public DirectExchange lecheDlx(){
        return new DirectExchange(RabbitNames.LECHE_DLX,true,false);
    }
    @Bean
    public Queue lecheDlq(){
        return QueueBuilder.durable(RabbitNames.LECHE_DLQ).build();
    }
    @Bean
    public Binding lecheDlqBinding(Queue lecheDlq, DirectExchange lecheDlx){
        return BindingBuilder
        .bind(lecheDlq)
        .to(lecheDlx)
        .with(RabbitNames.LECHE_QUEUE);
    }

    //Actualizar ACK y NACK para que el consumidor pueda manejar los mensajes de manera manual, en caso de que falle el procesamiento del mensaje, se puede rechazar el mensaje y no reencolarlo, para que no se pierda el mensaje. Esto se hace con la propiedad spring.rabbitmq.listener.simple.acknowledge-mode=manual en application.properties
    @Bean 
    public DirectExchange notificacionesDlx(){
        return new DirectExchange(RabbitNames.NOTIFICACIONES_DLX,true,false);
    }
    @Bean
    public Queue notificacionesDlq(){
        return QueueBuilder.durable(RabbitNames.NOTIFICACIONES_DLQ).build();
    }
    @Bean
    public Binding notificacionesDlqBinding(Queue notificacionesDlq, DirectExchange notificacionesDlx){
        return BindingBuilder
        .bind(notificacionesDlq)
        .to(notificacionesDlx)
        .with(RabbitNames.NOTIFICACIONES_QUEUE);
    }
}
