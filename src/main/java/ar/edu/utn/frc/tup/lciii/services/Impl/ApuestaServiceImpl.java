package ar.edu.utn.frc.tup.lciii.services.Impl;

import ar.edu.utn.frc.tup.lciii.dtos.common.*;
import ar.edu.utn.frc.tup.lciii.entities.ApuestaEntity;
import ar.edu.utn.frc.tup.lciii.models.Apuesta;
import ar.edu.utn.frc.tup.lciii.models.SorteoPorFecha;
import ar.edu.utn.frc.tup.lciii.repository.ApuestaRepository;
import ar.edu.utn.frc.tup.lciii.services.ApuestaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApuestaServiceImpl implements ApuestaService {

    private final String URL = "http://java_api:8080/sorteos";

    @Autowired
    private ApuestaRepository apuestaRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ApuestaDTOResponse createApuesta(ApuestaDTORequest apuestaDTORequest) {
        SorteoPorFecha sorteoPorFecha = ObtenerSorteoPorFecha(apuestaDTORequest.getFecha_sorteo())[0];

        Apuesta apuesta = new Apuesta();
        apuesta.setId_sorteo(sorteoPorFecha.getNumeroSorteo());
        apuesta.setFecha_sorteo(apuestaDTORequest.getFecha_sorteo());
        apuesta.setId_cliente(apuestaDTORequest.getId_cliente());
        apuesta.setNumero(apuestaDTORequest.getNumero());

        int coincidencias = ObtenerCoincidencias(apuestaDTORequest.getNumero(), sorteoPorFecha.getNumerosSorteados());


        if (apuestaDTORequest.getMontoApostado() <= (sorteoPorFecha.getDineroTotalAcumulado() * 0.01)) {
            apuesta.setMontoApostado(apuestaDTORequest.getMontoApostado());
        } else {
            ErrorApi errorApi = new ErrorApi();
            errorApi.setMessage("La apuesta no puede ser mayores al 1% del total en reserva del día del sorteo");
        }

        apuesta.setPremio(calcularPremio(coincidencias, apuesta.getMontoApostado()));

        if (apuesta.getPremio() == 0) {
            apuesta.setResultado("PERDEDOR");
        }else {
            apuesta.setResultado("GANADOR");
        }

        ApuestaEntity apuestaEntity = modelMapper.map(apuesta, ApuestaEntity.class);

        apuestaRepository.save(apuestaEntity);

        ApuestaDTOResponse apuestaDTOResponse = new ApuestaDTOResponse();
        apuestaDTOResponse.setId_sorteo(apuesta.getId_sorteo());
        apuestaDTOResponse.setFecha_sorteo(apuesta.getFecha_sorteo());
        apuestaDTOResponse.setId_cliente(apuesta.getId_cliente());
        apuestaDTOResponse.setNumero(apuesta.getNumero());
        apuestaDTOResponse.setResultado(apuesta.getResultado());

        return apuestaDTOResponse;
    }

    public int calcularPremio(int coincidencias, int montoApostado) {
        int premio = 0;

        switch (coincidencias) {
            case 2:
                premio = montoApostado * 7; // 700% -> multiplicar por 7
                break;
            case 3:
                premio = montoApostado * 70; // 7000% -> multiplicar por 70
                break;
            case 4:
                premio = montoApostado * 600; // 60000% -> multiplicar por 600
                break;
            case 5:
                premio = montoApostado * 3500; // 350000% -> multiplicar por 3500
                break;
            default:
                premio = 0; // No hay premio si hay menos de 2 coincidencias
                break;
        }

        return premio;
    }


    public int ObtenerCoincidencias(String numero, List<List<Integer>> numeros) {
        int maxCoincidencias = 0;

        // Recorremos la lista de números sorteados
        for (List<Integer> par : numeros) {
            // Convertimos el número sorteado a String
            String numeroSorteado = String.valueOf(par.get(1));

            // Calculamos cuántas cifras coinciden desde el final
            int coincidencias = 0;
            int minLength = Math.min(numero.length(), numeroSorteado.length());

            // Comparamos los dígitos desde el final de ambos números
            for (int i = 0; i < minLength; i++) {
                if (numero.charAt(numero.length() - 1 - i) == numeroSorteado.charAt(numeroSorteado.length() - 1 - i)) {
                    coincidencias++;
                } else {
                    // Si no coincide, paramos la comparación
                    break;
                }
            }

            // Actualizamos el máximo número de coincidencias
            maxCoincidencias = Math.max(maxCoincidencias, coincidencias);
        }

        return maxCoincidencias;
    }


    public SorteoPorFecha[] ObtenerSorteoPorFecha(String fecha) {
        return restTemplate.getForEntity(URL + "?fecha=" + fecha, SorteoPorFecha[].class).getBody();
    }

    public List<ApuestoInfoSorteoDto> ObtenerApuestoInfoSorteo(int id_sorteo) {

        List<ApuestaEntity> Apuestas = apuestaRepository.findByIdSorteo(id_sorteo);
        List<ApuestoInfoSorteoDto> ApuestoInfoSorteoDtos = new ArrayList<>();

        for (ApuestaEntity apuestaEntity : Apuestas) {
            ApuestoInfoSorteoDto apuestoInfoSorteoDto = new ApuestoInfoSorteoDto();
            apuestoInfoSorteoDto.setId_cliente(apuestaEntity.getIdCliente());
            apuestoInfoSorteoDto.setNumero(apuestaEntity.getNumero());
            apuestoInfoSorteoDto.setResultado(apuestaEntity.getResultado());
            apuestoInfoSorteoDto.setMontoApostado(apuestaEntity.getMontoApostado());
            apuestoInfoSorteoDto.setPremio(apuestaEntity.getPremio());

            ApuestoInfoSorteoDtos.add(apuestoInfoSorteoDto);
        }

        return ApuestoInfoSorteoDtos;
    }

    @Override
    public InformacionSorteo ObtenerInformacionSorteo(int id_sorteo) {

        InformacionSorteo informacionSorteo = new InformacionSorteo();
        List<ApuestoInfoSorteoDto> apuestas = ObtenerApuestoInfoSorteo(id_sorteo);

        SorteoPorFecha[] sorteoPorFechas = restTemplate.getForEntity(URL, SorteoPorFecha[].class).getBody();

        for (SorteoPorFecha sorteo : sorteoPorFechas) {
            if (sorteo.getNumeroSorteo() == id_sorteo) {
                informacionSorteo.setId_sorteo(id_sorteo);
                informacionSorteo.setFecha_sorteo(sorteo.getFecha());
                informacionSorteo.setTotalEnReserva(sorteo.getDineroTotalAcumulado());
                informacionSorteo.setApuestoInfoSorteoDtos(apuestas);
                break;
            }
        }

        return informacionSorteo;
    }

    @Override
    public InformacionApuestasGanadasDto ObtenerInformacionApuestasGanadas(int id_sorteo) {
        InformacionApuestasGanadasDto apuestasGanadasDto = new InformacionApuestasGanadasDto();
        InformacionSorteo informacionSorteo = ObtenerInformacionSorteo(id_sorteo);

        apuestasGanadasDto.setId_sorteo(id_sorteo);
        apuestasGanadasDto.setFecha_sorteo(informacionSorteo.getFecha_sorteo());

        int totalDeApuestas = 0;
        int totalPagado = 0;

        for (ApuestoInfoSorteoDto apuesta: informacionSorteo.getApuestoInfoSorteoDtos()) {
            totalDeApuestas += apuesta.getMontoApostado();
            totalPagado += apuesta.getPremio();
        }

        apuestasGanadasDto.setTotalDeApuestas(totalDeApuestas);
        apuestasGanadasDto.setTotalPagado(totalPagado);
        apuestasGanadasDto.setTotalEnReserva(informacionSorteo.getTotalEnReserva());

        return apuestasGanadasDto;

    }
}