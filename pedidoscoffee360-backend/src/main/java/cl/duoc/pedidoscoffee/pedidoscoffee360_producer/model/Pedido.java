package cl.duoc.pedidoscoffee.pedidoscoffee360_producer.model;

public class Pedido {

    private Integer id;
    private String tipo;
    private String producto;
    private String estado;

    public Pedido() { }

    public Pedido(Integer id, String tipo, String producto, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.producto = producto;
        this.estado = estado;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
