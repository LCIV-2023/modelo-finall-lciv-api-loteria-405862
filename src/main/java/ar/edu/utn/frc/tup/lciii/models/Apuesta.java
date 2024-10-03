package ar.edu.utn.frc.tup.lciii.models;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class Apuesta {
    private int id_sorteo;
    private String fecha_sorteo;
    private String id_cliente;
    private String numero;
    private String resultado;
    private int montoApostado;
    private int premio;
}