package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDTORequest;
import ar.edu.utn.frc.tup.lciii.dtos.common.ApuestaDTOResponse;
import ar.edu.utn.frc.tup.lciii.dtos.common.InformacionApuestasGanadasDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.InformacionSorteo;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loteria")
public class ApuestaController {

    @Autowired
    private ApuestaService apuestaService;

    @PostMapping("/apuesta")
    public ResponseEntity<ApuestaDTOResponse> createApuesta(ApuestaDTORequest dto) {
        ApuestaDTOResponse dtoResponse = apuestaService.createApuesta(dto);
        return ResponseEntity.ok(dtoResponse);
    }

    @GetMapping("/sorteo/{id_sorteo}")
    public ResponseEntity<InformacionSorteo> getInformacionSorteo(@PathVariable int id_sorteo) {
        InformacionSorteo informacionSorteo = apuestaService.ObtenerInformacionSorteo(id_sorteo);
        return ResponseEntity.ok(informacionSorteo);
    }

    @GetMapping("total/{id_sorteo}")
    public ResponseEntity<InformacionApuestasGanadasDto> getInformacionApuestasGanadas(@PathVariable int id_sorteo) {
        InformacionApuestasGanadasDto info = apuestaService.ObtenerInformacionApuestasGanadas(id_sorteo);
        return ResponseEntity.ok(info);
    }
}
