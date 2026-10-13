package com.tienda.pedidos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Datos requeridos para la creacion de un nuevo pedido")
public class PedidoRequestDTO {

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "El formato del email no es valido")
    @Schema(description = "Correo electronico del cliente", example = "juan.perez@empresa.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String clienteEmail;

    @NotBlank(message = "El tipo de cliente es obligatorio (REGULAR, VIP, MAYORISTA)")
    @Schema(description = "Tipo o categoria del cliente", example = "VIP", allowableValues = {"REGULAR", "VIP", "MAYORISTA"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoCliente;

    @NotNull(message = "El subtotal no puede ser nulo")
    @Positive(message = "El subtotal debe ser estrictamente mayor a cero")
    @Schema(description = "Monto subtotal de la compra antes de impuestos y descuentos", example = "150000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double subtotal;

    @Schema(description = "Notas de uso interno confidenciales (no se exponen al cliente)", example = "Descuento autorizado por gerencia regional")
    private String notasInternas;

    public PedidoRequestDTO() {
    }

    public PedidoRequestDTO(String clienteEmail, String tipoCliente, Double subtotal, String notasInternas) {
        this.clienteEmail = clienteEmail;
        this.tipoCliente = tipoCliente;
        this.subtotal = subtotal;
        this.notasInternas = notasInternas;
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

    public String getNotasInternas() {
        return notasInternas;
    }

    public void setNotasInternas(String notasInternas) {
        this.notasInternas = notasInternas;
    }
}
