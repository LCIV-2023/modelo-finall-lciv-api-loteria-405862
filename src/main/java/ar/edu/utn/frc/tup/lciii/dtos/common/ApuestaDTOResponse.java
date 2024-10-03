package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApuestaDTOResponse {
    private int id_sorteo;     // ID del sorteo
    private String fecha_sorteo;      // Fecha del sorteo
    private String id_cliente;        // ID del cliente que apostó
    private String numero;            // Número que el cliente apostó
    private String resultado;         // Resultado de la apuesta (GANADOR o PERDEDOR)// Monto del premio en caso de ganar
}
