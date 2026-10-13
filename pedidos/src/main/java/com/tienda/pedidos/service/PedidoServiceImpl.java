package com.tienda.pedidos.service;

import com.tienda.pedidos.dto.PedidoRequestDTO;
import com.tienda.pedidos.dto.PedidoResponseDTO;
import com.tienda.pedidos.entity.Pedido;
import com.tienda.pedidos.exception.RecursoNoEncontradoException;
import com.tienda.pedidos.repository.PedidoRepository;
import com.tienda.pedidos.strategy.EstrategiaDescuento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);
    private static final double PORCENTAJE_IVA = 0.19; // 19% IVA

    private final PedidoRepository pedidoRepository;
    private final NotificacionService notificacionService;
    private final Map<String, EstrategiaDescuento> estrategiasDescuento;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             NotificacionService notificacionService,
                             Map<String, EstrategiaDescuento> estrategiasDescuento) {
        this.pedidoRepository = pedidoRepository;
        this.notificacionService = notificacionService;
        this.estrategiasDescuento = estrategiasDescuento;
    }

    @Override
    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO) {
        log.debug("Iniciando procesamiento de pedido para el cliente: {}", requestDTO.getClienteEmail());

        String tipoCliente = (requestDTO.getTipoCliente() != null)
                ? requestDTO.getTipoCliente().trim().toUpperCase()
                : "REGULAR";

        // Seleccion polimorfica de estrategia inyectada en el mapa de beans de Spring
        EstrategiaDescuento estrategia = estrategiasDescuento.get(tipoCliente);
        if (estrategia == null) {
            log.warn("Estrategia no encontrada para el tipo de cliente '{}'. Se asignara la estrategia REGULAR por defecto", tipoCliente);
            estrategia = estrategiasDescuento.get("REGULAR");
        }

        // Calculo de valores financieros
        double subtotal = requestDTO.getSubtotal();
        double descuento = redondear(estrategia.calcularDescuento(subtotal));
        double baseImponible = subtotal - descuento;
        double iva = redondear(baseImponible * PORCENTAJE_IVA);
        double total = redondear(baseImponible + iva);

        log.debug("Calculo financiero completado - Subtotal: {}, Descuento: {}, Base: {}, IVA: {}, Total: {}",
                subtotal, descuento, baseImponible, iva, total);

        // Construccion y persistencia de la entidad
        Pedido pedido = new Pedido();
        pedido.setClienteEmail(requestDTO.getClienteEmail());
        pedido.setTipoCliente(tipoCliente);
        pedido.setSubtotal(subtotal);
        pedido.setDescuento(descuento);
        pedido.setIva(iva);
        pedido.setTotal(total);
        pedido.setEstado("PROCESADO");
        // Se preserva la nota interna en base de datos pero NUNCA se registra en logs ni en DTO de respuesta
        pedido.setNotasInternas(requestDTO.getNotasInternas());
        pedido.setFechaCreacion(LocalDateTime.now());

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Registro de log seguro SIN exponer notasInternas
        log.info("Pedido registrado exitosamente con ID #{}. Cliente: {}, Tipo: {}, Total: ${}",
                pedidoGuardado.getId(), pedidoGuardado.getClienteEmail(), pedidoGuardado.getTipoCliente(), pedidoGuardado.getTotal());

        // Disparo de notificacion desacoplada
        notificacionService.enviarConfirmacion(pedidoGuardado.getClienteEmail(), pedidoGuardado.getId(), pedidoGuardado.getTotal());

        return mapearAResponseDTO(pedidoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        log.debug("Consultando pedido por ID #{}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Consulta fallida: No existe pedido con ID #{}", id);
                    return new RecursoNoEncontradoException("No se encontro ningun pedido registrado con el ID: " + id);
                });

        return mapearAResponseDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosPorEmail(String email) {
        log.debug("Consultando historial de pedidos para el email: {}", email);
        return pedidoRepository.findByClienteEmail(email)
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapeo explicito y seguro de Entidad a DTO de Respuesta.
     * Garantiza que el campo notasInternas quede estrictamente aislado de la capa publica.
     */
    private PedidoResponseDTO mapearAResponseDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getClienteEmail(),
                pedido.getTipoCliente(),
                pedido.getSubtotal(),
                pedido.getDescuento(),
                pedido.getIva(),
                pedido.getTotal(),
                pedido.getEstado(),
                pedido.getFechaCreacion()
        );
    }

    private double redondear(double valor) {
        return BigDecimal.valueOf(valor)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
