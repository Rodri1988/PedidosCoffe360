package cl.duoc.pedidoscoffee360_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BindingRequest(

        @NotBlank(message = "El exchange es obligatorio")
        String exchange,

        @NotBlank(message = "La cola es obligatoria")
        String queue,

        @Size(max = 255, message = "La routing key no puede superar 255 caracteres")
        String routingKey
) { }
