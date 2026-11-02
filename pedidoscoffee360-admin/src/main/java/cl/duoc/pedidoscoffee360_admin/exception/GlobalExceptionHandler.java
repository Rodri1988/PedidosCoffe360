package cl.duoc.pedidoscoffee360_admin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors()
            .forEach(e -> errores.put(e.getObjectName(), e.getDefaultMessage()));

        Map<String, Object> cuerpo = cuerpo(HttpStatus.BAD_REQUEST, "Datos de entrada invalidos");
        cuerpo.put("errores", errores);
        return ResponseEntity.badRequest().body(cuerpo);
    }

    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class,
                       MissingServletRequestParameterException.class})
    public ResponseEntity<Map<String, Object>> peticionIncorrecta(Exception ex) {
        String detalle = ex instanceof HttpMessageNotReadableException
            ? "El cuerpo de la peticion no es un JSON valido"
            : ex.getMessage();
        return ResponseEntity.badRequest().body(cuerpo(HttpStatus.BAD_REQUEST, detalle));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(cuerpo(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<Map<String, Object>> errorDeRabbit(RestClientResponseException ex) {
        HttpStatusCode status = ex.getStatusCode();
        Map<String, Object> cuerpo = cuerpo(status, "RabbitMQ rechazo la operacion");
        cuerpo.put("detalleRabbit", ex.getResponseBodyAsString());
        return ResponseEntity.status(status).body(cuerpo);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> rabbitNoDisponible(ResourceAccessException ex) {
        Map<String, Object> cuerpo = cuerpo(HttpStatus.SERVICE_UNAVAILABLE,
            "No se pudo conectar con la API de administracion de RabbitMQ (revisa que este arriba y el puerto 15672)");
        cuerpo.put("detalle", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(cuerpo);
    }

    private static Map<String, Object> cuerpo(HttpStatusCode status, String mensaje) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("status", status.value());
        cuerpo.put("mensaje", mensaje);
        return cuerpo;
    }
}
