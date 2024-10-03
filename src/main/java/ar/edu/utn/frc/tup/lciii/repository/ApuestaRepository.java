package ar.edu.utn.frc.tup.lciii.repository;

import ar.edu.utn.frc.tup.lciii.entities.ApuestaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApuestaRepository extends JpaRepository<ApuestaEntity, Long> {
    List<ApuestaEntity> findByIdSorteo(int id_sorteo);
}
