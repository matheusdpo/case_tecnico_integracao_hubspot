package br.com.meetime.hubspot.utils;

import br.com.meetime.hubspot.dto.request.AccountHubSpotDTO;
import br.com.meetime.hubspot.exceptions.SerializationUtilsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class SerializationUtils {

    public <T> T jsonToObject(String json, Class<T> clazz) throws SerializationUtilsException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new SerializationUtilsException(e.getMessage());
        }
    }

    public String objectToJson(Object obj) throws SerializationUtilsException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new SerializationUtilsException(e.getMessage());
        }
    }
}
