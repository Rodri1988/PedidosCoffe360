package cl.duoc.pedidoscoffee.pedidoscoffee360_producer.config;

public final class RabbitNames{

    private RabbitNames(){
    }
    public static final String EXCHANGE = "pedidos.exchange";
    public static final String ESPRESSO_QUEUE = "pedidos.barista.espresso";
    public static final String LECHE_QUEUE = "pedidos.barista.leche";
    public static final String NOTIFICACIONES_QUEUE = "pedidos.notificaciones";
    public static final String AUDITORIA_QUEUE = "pedidos.auditoria";
}
