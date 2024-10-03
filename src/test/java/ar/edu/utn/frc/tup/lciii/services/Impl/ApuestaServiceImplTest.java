package ar.edu.utn.frc.tup.lciii.services.Impl;

import ar.edu.utn.frc.tup.lciii.dtos.common.*;
import ar.edu.utn.frc.tup.lciii.entities.ApuestaEntity;
import ar.edu.utn.frc.tup.lciii.models.Apuesta;
import ar.edu.utn.frc.tup.lciii.models.SorteoPorFecha;
import ar.edu.utn.frc.tup.lciii.repository.ApuestaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ApuestaServiceImplTest {
    @InjectMocks
    private ApuestaServiceImpl apuestaService;

    @Mock
    private ApuestaRepository apuestaRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createApuesta_successful() {
        // Arrange
        ApuestaDTORequest request = new ApuestaDTORequest();
        request.setFecha_sorteo("2024-09-28");
        request.setId_cliente("1");
        request.setNumero("12345");
        request.setMontoApostado(100);

        SorteoPorFecha[] sorteos = {new SorteoPorFecha(1, "2024-09-28",10000, Arrays.asList(Arrays.asList(1, 12345)) )};
        when(restTemplate.getForEntity(anyString(), eq(SorteoPorFecha[].class))).thenReturn(new ResponseEntity<>(sorteos, HttpStatus.OK));

        ApuestaEntity apuestaEntity = new ApuestaEntity();
        apuestaEntity.setIdCliente("1");
        apuestaEntity.setNumero("12345");
        apuestaEntity.setMontoApostado(100);
        apuestaEntity.setPremio(0); // Ganador, suponiendo que no hay coincidencias

        when(modelMapper.map(any(Apuesta.class), eq(ApuestaEntity.class))).thenReturn(apuestaEntity);
        when(apuestaRepository.save(any(ApuestaEntity.class))).thenReturn(apuestaEntity);

        // Act
        ApuestaDTOResponse response = apuestaService.createApuesta(request);

        // Assert
        assertNotNull(response);
        assertEquals("GANADOR", response.getResultado());
        verify(apuestaRepository, times(1)).save(any(ApuestaEntity.class));
    }

    @Test
    void calcularPremio_correctCalculation() {
        // Arrange
        int montoApostado = 100;

        // Act & Assert
        assertEquals(700, apuestaService.calcularPremio(2, montoApostado));
        assertEquals(7000, apuestaService.calcularPremio(3, montoApostado));
        assertEquals(60000, apuestaService.calcularPremio(4, montoApostado));
        assertEquals(350000, apuestaService.calcularPremio(5, montoApostado));
        assertEquals(0, apuestaService.calcularPremio(1, montoApostado));
    }

    @Test
    void obtenerCoincidencias_correctCount() {
        // Arrange
        String numero = "12345";
        List<List<Integer>> numerosSorteados = Arrays.asList(Arrays.asList(1, 12345), Arrays.asList(2, 23456));

        // Act
        int coincidencias = apuestaService.ObtenerCoincidencias(numero, numerosSorteados);

        // Assert
        assertEquals(5, coincidencias);
    }

    @Test
    void obtenerSorteoPorFecha_returnsSorteos() {
        // Arrange
        String fecha = "2024-09-28";
        SorteoPorFecha[] sorteos = {new SorteoPorFecha(1, "2024-09-28", 10000, Arrays.asList(Arrays.asList(1, 12345)))};
        when(restTemplate.getForEntity(anyString(), eq(SorteoPorFecha[].class))).thenReturn(new ResponseEntity<>(sorteos, HttpStatus.OK));

        // Act
        SorteoPorFecha[] resultado = apuestaService.ObtenerSorteoPorFecha(fecha);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.length);
        assertEquals(1, resultado[0].getNumeroSorteo());
    }

    @Test
    void obtenerApuestoInfoSorteo_returnsApuestas() {
        // Arrange
        int idSorteo = 1;
        ApuestaEntity apuestaEntity = new ApuestaEntity();
        apuestaEntity.setIdCliente("1");
        apuestaEntity.setNumero("12345");
        apuestaEntity.setResultado("PERDEDOR");
        apuestaEntity.setMontoApostado(100);
        apuestaEntity.setPremio(0);

        when(apuestaRepository.findByIdSorteo(idSorteo)).thenReturn(Arrays.asList(apuestaEntity));

        // Act
        List<ApuestoInfoSorteoDto> apuestas = apuestaService.ObtenerApuestoInfoSorteo(idSorteo);

        // Assert
        assertNotNull(apuestas);
        assertEquals(1, apuestas.size());
        assertEquals("12345", apuestas.get(0).getNumero());
    }

    @Test
    void obtenerInformacionSorteo_returnsInformacion() {
        // Arrange
        int idSorteo = 1;
        SorteoPorFecha sorteo = new SorteoPorFecha(1, "2024-09-28", 10000, Arrays.asList(Arrays.asList(1, 12345)));
        when(restTemplate.getForEntity(anyString(), eq(SorteoPorFecha[].class))).thenReturn(new ResponseEntity<>(new SorteoPorFecha[]{sorteo}, HttpStatus.OK));
        when(apuestaRepository.findByIdSorteo(idSorteo)).thenReturn(Arrays.asList(new ApuestaEntity()));

        // Act
        InformacionSorteo informacionSorteo = apuestaService.ObtenerInformacionSorteo(idSorteo);

        // Assert
        assertNotNull(informacionSorteo);
        assertEquals(idSorteo, informacionSorteo.getId_sorteo());
    }

}