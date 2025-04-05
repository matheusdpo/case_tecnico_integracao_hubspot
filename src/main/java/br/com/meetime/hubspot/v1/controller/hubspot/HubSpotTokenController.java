package br.com.meetime.hubspot.v1.controller.hubspot;

import br.com.meetime.hubspot.v1.controller.hubspot.swagger.HubSpotTokenControllerSwagger;
import br.com.meetime.hubspot.v1.dto.response.AccessTokenDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.v1.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.v1.exceptions.SerializationUtilsException;
import br.com.meetime.hubspot.v1.service.HubSpotService;
import br.com.meetime.hubspot.v1.utils.SerializationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/hubspot")
@Tag(name = "HubSpot Token API", description = "API get auth bearer token")
public class HubSpotTokenController implements HubSpotTokenControllerSwagger {

    @Autowired
    private HubSpotService hubSpotService;

    @Autowired
    private SerializationUtils serializationUtils;

    @GetMapping("/token-access")
    @Override
    public ResponseEntity<?> getTokenAccess(@RequestParam(required = false) String clientId,
                                            @RequestParam(required = false) String clientSecret,
                                            @RequestParam(required = false) String code) {

        if (Objects.isNull(clientId) || clientId.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorDTO(
                            StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
        }

        if (Objects.isNull(clientSecret) || clientSecret.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorDTO(
                            StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatus()));
        }

        if (Objects.isNull(code) || code.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorDTO(
                            StatusHubSpotApiEnum.CODE_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CODE_OBRIGATORIO.getStatus()));
        }

        try {
            HttpResponse<String> response = hubSpotService.postTokenAccess(clientId, clientSecret, code);

            AccessTokenDTO accessTokenDTO = serializationUtils.jsonToObject(response.getBody(), AccessTokenDTO.class);

            if (Objects.isNull(accessTokenDTO.getAccessToken()) || accessTokenDTO.getAccessToken().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatus())
                        );
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(accessTokenDTO);
        } catch (SerializationUtilsException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(
                            StatusHubSpotApiEnum.ERRO_SERIALIZACAO.getStatusCode(),
                            StatusHubSpotApiEnum.ERRO_SERIALIZACAO.getStatus())
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(e.getMessage()));
        }
    }
}