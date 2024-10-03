package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ApuestoInfoSorteoDto {
    private String id_cliente;       // ID del cliente que hizo la apuesta
    private String numero;           // Número apostado
    private String resultado;        // Resultado de la apuesta (GANADOR o PERDEDOR)
    private int montoApostado;// Monto que el cliente apostó
    private int premio;       // Premio en caso de ganar
}
