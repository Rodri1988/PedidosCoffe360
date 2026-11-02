package cl.duoc.pedidoscoffee.pedidoscoffee360_producer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class RabbitConfig{
    @Bean
    public TopicExchange pedidosExchange(){
        return new TopicExchange(
            RabbitNames.EXCHANGE,
            true,
            false
        );
    }
}
