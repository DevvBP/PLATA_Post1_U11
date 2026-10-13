package com.tienda.pedidos.strategy;

import org.springframework.stereotype.Component;

@Component("REGULAR")
public class DescuentoRegular implements EstrategiaDescuento {

    @Override
    public double calcularDescuento(double subtotal) {
        // Los clientes regulares no tienen descuento adicional (0%)
        return 0.0;
    }
}
