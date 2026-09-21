package cl.duoc.pedidoscoffe360_backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidosCoffeeController {

    // 1. Lista mutable 
    private List<Map<String, Object>> ordenes = new ArrayList<>(List.of(
        Map.of("id", 1, "bebida", "Latte Vainilla", "estado", "PREPARANDO"),
        Map.of("id", 2, "bebida", "Espresso", "estado", "LISTO_PARA_RETIRO"),
        Map.of("id", 3, "bebida", "Cappuccino", "estado", "ENTREGADO"),
        Map.of("id", 4, "bebida", "Mocha", "estado", "PREPARANDO"),
        Map.of("id", 5, "bebida", "Americano", "estado", "LISTO_PARA_RETIRO"),
        Map.of("id", 6, "bebida", "Frappuccino", "estado", "ENTREGADO")


    ));

    // 2. GET: Obtener todos los pedidos
    @GetMapping
    public List<Map<String, Object>> obtenerPedidos() {
        return ordenes;
    }

    // 3. GET con URL: Obtener un pedido específico por su ID
    @GetMapping("/{id}")
    public Map<String, Object> obtenerPedidoPorId(@PathVariable int id) {
        for (Map<String, Object> orden : ordenes) {
            if ((Integer) orden.get("id") == id) {
                return orden;
            }
        }
        return Map.of("error", "Pedido no encontrado");
    }

    // 4. POST: Recibir datos y crear un nuevo pedido
    @PostMapping
    public Map<String, Object> crearPedido(@RequestBody Map<String, Object> nuevaOrden) {
        ordenes.add(nuevaOrden);
        return nuevaOrden;
    }

    // 5. ACTUALIZAR (PUT) Cambiar el estado
    @PutMapping("/{id}")
    public Map<String, Object> actualizarPedido(@PathVariable int id, @RequestBody Map<String, Object> actualizacion) {
        for (Map<String, Object> orden : ordenes) {
            if ((Integer) orden.get("id") == id) {
                orden.put("estado", actualizacion.get("estado")); // Modifica solo el estado
                return orden;
            }
        }
        return Map.of("error", "Pedido no encontrado");
    }

    // 6. ELIMINAR (DELETE)
    @DeleteMapping("/{id}")
    public Map<String, String> eliminarPedido(@PathVariable int id) {
        // Busca si el ID existe y lo borra automáticamente de la lista
        boolean borrado = ordenes.removeIf(orden -> (Integer) orden.get("id") == id);
        
        if (borrado) {
            return Map.of("mensaje", "Pedido eliminado correctamente");
        }
        return Map.of("error", "Pedido no encontrado");
    }


}