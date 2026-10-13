package com.tienda.pedidos.strategy;

public interface EstrategiaDescuento {

    /**
     * Calcula el monto de descuento aplicable sobre un subtotal dado.
     *
     * @param subtotal Monto base antes de impuestos.
     * @return Monto exacto a descontar.
     */
    double calcularDescuento(double subtotal);
}
