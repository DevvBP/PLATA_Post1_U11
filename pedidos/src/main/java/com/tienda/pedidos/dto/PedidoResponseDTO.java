package com.tienda.pedidos.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta publica con los datos consolidados del pedido (sin notas internas)")
public class PedidoResponseDTO {

    @Schema(description = "Identificador unico del pedido", example = "1")
    private Long id;

    @Schema(description = "Correo electronico del cliente", example = "juan.perez@empresa.com")
    private String clienteEmail;

    @Schema(description = "Tipo o categoria del cliente", example = "VIP")
    private String tipoCliente;

    @Schema(description = "Subtotal base antes de descuentos e impuestos", example = "150000.0")
    private Double subtotal;

    @Schema(description = "Monto de descuento aplicado segun la categoria del cliente", example = "22500.0")
    private Double descuento;

    @Schema(description = "Impuesto sobre las ventas (IVA 19%) calculado sobre la base imponible", example = "24225.0")
    private Double iva;

    @Schema(description = "Monto total a pagar por el cliente", example = "151725.0")
    private Double total;

    @Schema(description = "Estado actual del pedido", example = "PROCESADO")
    private String estado;

    @Schema(description = "Fecha y hora de registro del pedido", example = "2026-10-13 16:35:10")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;

    public PedidoResponseDTO() {
    }

    public PedidoResponseDTO(Long id, String clienteEmail, String tipoCliente, Double subtotal,
                             Double descuento, Double iva, Double total, String estado, LocalDateTime fechaCreacion) {
        this.id = id;
        this.clienteEmail = clienteEmail;
        this.tipoCliente = tipoCliente;
        this.subtotal = subtotal;
        this.descuento = descuento;
        this.iva = iva;
        this.total = total;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
