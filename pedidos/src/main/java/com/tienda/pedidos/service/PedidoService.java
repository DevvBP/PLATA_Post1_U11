package com.tienda.pedidos.service;

import com.tienda.pedidos.dto.PedidoRequestDTO;
import com.tienda.pedidos.dto.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

    /**
     * Procesa y registra un nuevo pedido calculando descuentos e impuestos correspondientes.
     *
     * @param requestDTO Datos de la peticion del pedido.
     * @return DTO con la informacion publica y calculada del pedido.
     */
    PedidoResponseDTO crearPedido(PedidoRequestDTO requestDTO);

    /**
     * Recupera un pedido por su identificador unico.
     *
     * @param id Identificador unico del pedido.
     * @return DTO con la informacion publica del pedido.
     */
    PedidoResponseDTO obtenerPedidoPorId(Long id);

    /**
     * Recupera todos los pedidos asociados al correo del cliente.
     *
     * @param email Correo electronico del cliente.
     * @return Lista de pedidos en formato DTO.
     */
    List<PedidoResponseDTO> obtenerPedidosPorEmail(String email);
}
