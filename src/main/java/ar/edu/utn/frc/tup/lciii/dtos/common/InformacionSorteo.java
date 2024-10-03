package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InformacionSorteo {
    private int id_sorteo;                       // ID del sorteo
    private String fecha_sorteo;                        // Fecha del sorteo
    private int totalEnReserva;                  // Total en reserva del sorteo
    private List<ApuestoInfoSorteoDto> apuestoInfoSorteoDtos; // Lista de apuestas realizadas en el sorteo
}
