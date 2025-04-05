package br.com.meetime.hubspot.v1.controller.hubspot;

import br.com.meetime.hubspot.v1.controller.hubspot.swagger.HubSpotAccountControllerSwagger;
import br.com.meetime.hubspot.v1.dto.request.AccountHubSpotDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.v1.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.v1.service.HubSpotService;
import br.com.meetime.hubspot.v1.utils.LogUtils;
import br.com.meetime.hubspot.v1.utils.SerializationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/hubspot")
@Tag(name = "HubSpot Account API", description = "API for managing HubSpot accounts")
public class HubSpotAccountController implements HubSpotAccountControllerSwagger {

    @Autowired
    private SerializationUtils serializationUtils;

    @Autowired
    private HubSpotService hubSpotService;

    @Autowired
    private LogUtils logger;

    @PostMapping("/create-account")
    @Override
    public ResponseEntity<?> createAccount(@RequestBody(required = false) AccountHubSpotDTO accountHubSpotDTO,
                                           @RequestHeader(name = "authorization", required = false) String token) {
        try {
            logger.info("Iniciando a criação de uma nova conta no HubSpot.");

            if (Objects.isNull(token) || token.isEmpty()) {
                logger.error("Token de autorização não fornecido.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.BEARER_INFO_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.BEARER_INFO_NULL.getStatus())
                        );
            }

            if (accountHubSpotDTO.hasEmptyOrNullFields()) {
                logger.error("Ha campos nulos ou vazios no DTO de conta do HubSpot.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.ACCOUNT_INFO_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.ACCOUNT_INFO_NULL.getStatus())
                        );
            }

            String jsonBody = serializationUtils.objectToJson(accountHubSpotDTO);

            HttpResponse<String> response = hubSpotService.postCreateAccount(token, jsonBody);

            if (response.getStatus() != HttpStatus.CREATED.value()) {
                logger.error("Erro ao criar conta no HubSpot: " + response.getBody());
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(response.getBody()));
            }

            logger.info("Conta criada com sucesso no HubSpot.");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(accountHubSpotDTO);
        } catch (Exception e) {
            logger.error("Erro ao criar conta no HubSpot: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(e.getMessage()));
        }
    }
}