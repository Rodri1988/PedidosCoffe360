package cl.duoc.pedidoscoffee.pedidoscoffee360_producer.controller;

import cl.duoc.pedidoscoffee.pedidoscoffee360_producer.model.Pedido;
import cl.duoc.pedidoscoffee.pedidoscoffee360_producer.service.PedidosProducer;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidosController{

    private final PedidosProducer pedidosProducer;
    private final List<Pedido> ordenes = new ArrayList<>();
    private final AtomicInteger secuencia = new AtomicInteger(0);

    public PedidosController(PedidosProducer pedidosProducer){
        this.pedidosProducer = pedidosProducer;
        ordenes.add(new Pedido(secuencia.incrementAndGet(), "latte", "Mesa 1", "Latte Vainilla", false, "PREPARANDO"));
        ordenes.add(new Pedido(secuencia.incrementAndGet(), "espresso", "Barra", "Espresso", false, "LISTO"));
        ordenes.add(new Pedido(secuencia.incrementAndGet(), "capuccino", "Mesa 3", "Cappuccino", true, "ENTREGADO"));
    }

    @GetMapping
    public List<Pedido> obtenerPedidos(){
        return ordenes;
    }

    @GetMapping("/{id}")
    public Pedido obtenerPedidoPorId(@PathVariable int id){
        return ordenes.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @PostMapping
    public Pedido crearPedido(@RequestBody Pedido nuevo){
        nuevo.setId(secuencia.incrementAndGet());
        if (nuevo.getEstado() == null) {
            nuevo.setEstado("CREADO");
        }
        ordenes.add(nuevo);
        pedidosProducer.enviar(nuevo);
        return nuevo;
    }

    @PutMapping("/{id}")
    public Pedido actualizarPedido(@PathVariable int id, @RequestBody Pedido cambio){
        for (Pedido p : ordenes) {
            if (p.getId() == id) {
                p.setEstado(cambio.getEstado());
                pedidosProducer.enviar(p);
                return p;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String eliminarPedido(@PathVariable int id){
        boolean borrado = ordenes.removeIf(p -> p.getId() == id);
        return borrado ? "Pedido eliminado correctamente" : "Pedido no encontrado";
    }
}
