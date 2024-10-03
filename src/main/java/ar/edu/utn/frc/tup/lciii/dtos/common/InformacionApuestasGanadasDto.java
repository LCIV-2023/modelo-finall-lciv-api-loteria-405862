package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InformacionApuestasGanadasDto {
    private int id_sorteo;
    private String fecha_sorteo;
    private int totalDeApuestas;
    private int totalPagado;
    private int totalEnReserva;
}