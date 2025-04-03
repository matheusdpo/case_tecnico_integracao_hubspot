package br.com.meetime.hubspot.utils;

import br.com.meetime.hubspot.dto.request.AccountHubSpotDTO;
import br.com.meetime.hubspot.exceptions.SerializationUtilsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class SerializationUtils {

    public <T> T jsonToObject(String json, Class<T> clazz) throws SerializationUtilsException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new SerializationUtilsException(e.getMessage());
        }
    }

    public String objectToJson(AccountHubSpotDTO accountHubSpotDTO) throws SerializationUtilsException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(accountHubSpotDTO);
        } catch (Exception e) {
            throw new SerializationUtilsException(e.getMessage());
        }
    }
}
