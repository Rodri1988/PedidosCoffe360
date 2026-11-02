package cl.duoc.pedidoscoffee360_admin.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record QueueRequest(

        @NotBlank(message = "El nombre de la cola es obligatorio")
        @Size(max = 255, message = "El nombre de la cola no puede superar 255 caracteres")
        @Pattern(regexp = "^(?!amq\\.)[A-Za-z0-9._:-]*$",
                 message = "El nombre solo admite letras, numeros, '.', '_', ':' y '-', y no puede empezar con 'amq.'")
        String name,

        Boolean durable,

        Boolean autoDelete,

        @Size(max = 255, message = "El dead-letter exchange no puede superar 255 caracteres")
        String deadLetterExchange,

        @Size(max = 255, message = "La dead-letter routing key no puede superar 255 caracteres")
        String deadLetterRoutingKey,

        @Positive(message = "El TTL de mensaje debe ser mayor que 0 (milisegundos)")
        Long messageTtl,

        @Pattern(regexp = "^(classic|quorum)?$", message = "El tipo de cola debe ser 'classic' o 'quorum'")
        String queueType
) {

    @AssertTrue(message = "Si se indica deadLetterRoutingKey, tambien debe indicarse deadLetterExchange")
    public boolean isDeadLetterConsistente() {
        boolean hayRk = deadLetterRoutingKey != null && !deadLetterRoutingKey.isBlank();
        boolean hayDlx = deadLetterExchange != null && !deadLetterExchange.isBlank();
        return !hayRk || hayDlx;
    }
}
