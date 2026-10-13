package com.tienda.pedidos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificacionEmailServiceImpl implements NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionEmailServiceImpl.class);

    @Override
    public void enviarConfirmacion(String email, Long pedidoId, Double total) {
        // En un entorno productivo real aqui se conectaria con JavaMailSender o SendGrid
        log.info("Simulando envio de correo electronico de confirmacion a '{}' para el pedido ID #{} por valor de ${}",
                email, pedidoId, total);
    }
}
