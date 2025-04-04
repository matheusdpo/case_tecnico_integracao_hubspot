package br.com.meetime.hubspot.v1.controller.hubspot;

import br.com.meetime.hubspot.v1.controller.hubspot.swagger.HubSpotTokenControllerSwagger;
import br.com.meetime.hubspot.v1.dto.response.AccessTokenDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.v1.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.v1.exceptions.SerializationUtilsException;
import br.com.meetime.hubspot.v1.service.HubSpotService;
import br.com.meetime.hubspot.v1.utils.LogUtils;
import br.com.meetime.hubspot.v1.utils.SerializationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import kong.unirest.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(HubSpotTokenController.class);
    @Autowired
    private HubSpotService hubSpotService;

    @Autowired
    private SerializationUtils serializationUtils;

    @Autowired
    private LogUtils logger;

    @GetMapping("/token-access")
    @Override
    public ResponseEntity<?> getTokenAccess(@RequestParam(required = false) String clientId,
                                            @RequestParam(required = false) String clientSecret,
                                            @RequestParam(required = false) String code) {
        try {
            logger.info("Iniciando a geração do token de acesso.");

            if (Objects.isNull(clientId) || clientId.isEmpty()) {
                logger.error("Client ID não fornecido.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
            }

            if (Objects.isNull(clientSecret) || clientSecret.isEmpty()) {
                logger.error("Client Secret não fornecido.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatusCode(),
                                StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatus()));
            }

            if (Objects.isNull(code) || code.isEmpty()) {
                logger.error("Code não fornecido.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.CODE_OBRIGATORIO.getStatusCode(),
                                StatusHubSpotApiEnum.CODE_OBRIGATORIO.getStatus()));
            }

            HttpResponse<String> response = hubSpotService.postTokenAccess(clientId, clientSecret, code);

            if (response.getStatus() != HttpStatus.OK.value()) {
                logger.error("Erro ao gerar o token de acesso: " + response.getBody());
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(response.getBody()));
            }

            AccessTokenDTO accessTokenDTO = serializationUtils.jsonToObject(response.getBody(), AccessTokenDTO.class);

            if (Objects.isNull(accessTokenDTO.getAccessToken()) || accessTokenDTO.getAccessToken().isEmpty()) {
                logger.error("Token de acesso nulo ou vazio.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatus())
                        );
            }

            logger.info("Token de acesso gerado com sucesso");

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