package ar.edu.utn.frc.tup.lciii.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "apuestas")
@AllArgsConstructor
@NoArgsConstructor
public class ApuestaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name = "id_sorteo", nullable = false)
    private int idSorteo;

    @Column(name = "fecha_sorteo", nullable = false)
    private String fechaSorteo;

    @Column(name = "id_cliente", nullable = false)
    private String idCliente;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "resultado")
    private String resultado;

    @Column(name = "monto_apostado", nullable = false)
    private int montoApostado;

    @Column(name = "premio")
    private int premio;
}
