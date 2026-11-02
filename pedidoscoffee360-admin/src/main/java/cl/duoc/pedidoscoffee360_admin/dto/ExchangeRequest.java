package cl.duoc.pedidoscoffee360_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ExchangeRequest(

        @NotBlank(message = "El nombre del exchange es obligatorio")
        @Size(max = 255, message = "El nombre del exchange no puede superar 255 caracteres")
        @Pattern(regexp = "^(?!amq\\.)[A-Za-z0-9._:-]*$",
                 message = "El nombre solo admite letras, numeros, '.', '_', ':' y '-', y no puede empezar con 'amq.'")
        String name,

        @NotBlank(message = "El tipo de exchange es obligatorio")
        @Pattern(regexp = "^(direct|topic|fanout|headers)?$",
                 message = "El tipo debe ser direct, topic, fanout o headers")
        String type,

        Boolean durable,

        Boolean autoDelete
) { }
