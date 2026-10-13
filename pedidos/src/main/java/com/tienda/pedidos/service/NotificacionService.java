package com.tienda.pedidos.service;

public interface NotificacionService {

    /**
     * Envia una notificacion de confirmacion de pedido al cliente.
     *
     * @param email Destinatario de la notificacion.
     * @param pedidoId Identificador unico del pedido.
     * @param total Monto total facturado.
     */
    void enviarConfirmacion(String email, Long pedidoId, Double total);
}
