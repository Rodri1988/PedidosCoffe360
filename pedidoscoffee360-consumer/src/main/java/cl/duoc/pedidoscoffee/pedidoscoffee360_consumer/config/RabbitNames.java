package cl.duoc.pedidoscoffee.pedidoscoffee360_consumer.config;

public final class RabbitNames{

    private RabbitNames(){
    }
    public static final String EXCHANGE = "pedidos.exchange";
    public static final String ESPRESSO_QUEUE = "pedidos.barista.espresso";
    public static final String LECHE_QUEUE = "pedidos.barista.leche";
    public static final String NOTIFICACIONES_QUEUE = "pedidos.notificaciones";
    public static final String AUDITORIA_QUEUE = "pedidos.auditoria";
    public static final String ESPRESSO_DLX = "pedidos.barista.espresso.dlx";
    public static final String ESPRESSO_DLQ = "pedidos.barista.espresso.dlq";
    public static final String LECHE_DLX = "pedidos.barista.leche.dlx";
    public static final String LECHE_DLQ = "pedidos.barista.leche.dlq";
    public static final String NOTIFICACIONES_DLX = "pedidos.notificaciones.dlx";
    public static final String NOTIFICACIONES_DLQ = "pedidos.notificaciones.dlq";

    public static final String RK_ESPRESSO = "pedido.espresso.*";
    public static final String RK_LATTE = "pedido.latte.*";
    public static final String RK_CAPUCCINO = "pedido.capuccino.*";
    public static final String RK_MOCHA = "pedido.mocha.*";
    public static final String RK_LISTOS = "pedido.*.listo";
    public static final String RK_TODOS = "pedido.#";
}
