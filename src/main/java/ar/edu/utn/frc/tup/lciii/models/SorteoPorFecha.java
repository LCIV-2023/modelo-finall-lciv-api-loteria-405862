package ar.edu.utn.frc.tup.lciii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SorteoPorFecha {
    private int numeroSorteo;
    private String fecha;
    private int dineroTotalAcumulado;
    private List<List<Integer>> numerosSorteados;
}