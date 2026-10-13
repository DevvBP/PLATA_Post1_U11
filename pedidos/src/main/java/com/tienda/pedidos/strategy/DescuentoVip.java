package com.tienda.pedidos.strategy;

import org.springframework.stereotype.Component;

@Component("VIP")
public class DescuentoVip implements EstrategiaDescuento {

    private static final double PORCENTAJE_DESCUENTO = 0.15; // 15%

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * PORCENTAJE_DESCUENTO;
    }
}
