package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ApuestaDTORequest {
    private String fecha_sorteo;  // Fecha del sorteo para la apuesta
    private String id_cliente;    // ID del cliente que hace la apuesta
    private String numero;        // Número que el cliente apuesta
    private int montoApostado;  // Monto apostado por el cliente
}
