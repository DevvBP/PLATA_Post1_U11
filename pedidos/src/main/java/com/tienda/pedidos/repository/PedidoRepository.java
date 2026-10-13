package com.tienda.pedidos.repository;

import com.tienda.pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Busca todos los pedidos asociados a la direccion de correo del cliente.
     *
     * @param clienteEmail Correo electronico del cliente.
     * @return Lista de pedidos encontrados.
     */
    List<Pedido> findByClienteEmail(String clienteEmail);
}
