package com.tienda.pedidos.strategy;

import org.springframework.stereotype.Component;

@Component("MAYORISTA")
public class DescuentoMayorista implements EstrategiaDescuento {

    private static final double PORCENTAJE_DESCUENTO = 0.25; // 25%

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * PORCENTAJE_DESCUENTO;
    }
}
