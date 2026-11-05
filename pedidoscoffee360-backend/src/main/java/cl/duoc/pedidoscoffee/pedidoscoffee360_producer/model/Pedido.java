package cl.duoc.pedidoscoffee.pedidoscoffee360_producer.model;

public class Pedido {

    private Integer id;
    private String tipo;
    private String ubicacion;
    private String descripcion;
    private boolean decaf;
    private String estado;

    public Pedido() { }

    public Pedido(Integer id, String tipo, String ubicacion, String descripcion, boolean decaf, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.decaf = decaf;
        this.estado = estado;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isDecaf() { return decaf; }
    public void setDecaf(boolean decaf) { this.decaf = decaf; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
