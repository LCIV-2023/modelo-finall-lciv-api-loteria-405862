package ar.edu.utn.frc.tup.lciii.configs;

import ar.edu.utn.frc.tup.lciii.entities.ApuestaEntity;
import ar.edu.utn.frc.tup.lciii.models.Apuesta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MappersConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapeo explícito para omitir el id
        modelMapper.addMappings(new PropertyMap<Apuesta, ApuestaEntity>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // Omitir el id
                map(source.getId_sorteo(), destination.getIdSorteo());
                map(source.getFecha_sorteo(), destination.getFechaSorteo());
                map(source.getId_cliente(), destination.getIdCliente());
                map(source.getNumero(), destination.getNumero()); // Asegúrate de que esto coincida en tipo
                map(source.getResultado(), destination.getResultado());
                map(source.getMontoApostado(), destination.getMontoApostado());
                map(source.getPremio(), destination.getPremio());
            }
        });

        return modelMapper;
    }

    @Bean("mergerMapper")
    public ModelMapper mergerMapper() {
        ModelMapper mapper =  new ModelMapper();
        mapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull());
        return mapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}
