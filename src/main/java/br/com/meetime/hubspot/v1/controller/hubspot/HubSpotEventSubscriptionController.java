package br.com.meetime.hubspot.v1.controller.hubspot;

import br.com.meetime.hubspot.v1.controller.hubspot.swagger.HubSpotEventSubscriptionControllerSwagger;
import br.com.meetime.hubspot.v1.dto.request.EventSubscriptionProcessDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.v1.dto.response.WebhookEventDTO;
import br.com.meetime.hubspot.v1.dto.response.webhook.WebhookEvent;
import br.com.meetime.hubspot.v1.dto.response.webhook.WebhookEventResponse;
import br.com.meetime.hubspot.v1.enums.EventTypeSubscriptionEnum;
import br.com.meetime.hubspot.v1.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.v1.exceptions.SerializationUtilsException;
import br.com.meetime.hubspot.v1.service.HubSpotService;
import br.com.meetime.hubspot.v1.utils.DateAndHourUtils;
import br.com.meetime.hubspot.v1.utils.LogUtils;
import br.com.meetime.hubspot.v1.utils.SerializationUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/hubspot")
@Tag(name = "HubSpot Event Webhook Subscriptions API", description = "API for managing Event Webhook subscriptions")
public class HubSpotEventSubscriptionController implements HubSpotEventSubscriptionControllerSwagger {

    @Autowired
    private HubSpotService hubSpotService;

    @Autowired
    private SerializationUtils serializationUtils;

    @Autowired
    private LogUtils logger;

    @PostMapping("/process-webhook")
    @Override
    public ResponseEntity<?> getTokenAccess(@RequestParam(required = false) String apikey,
                                            @RequestBody(required = false) EventSubscriptionProcessDTO eventSubscriptionProcessDTO) {

        try {
            logger.info("Iniciando o processamento do webhook.");

            if (Objects.isNull(apikey) || apikey.isEmpty()) {
                logger.error("API Key não fornecida.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.APIKEY_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.APIKEY_NULL.getStatus()));
            }

            HttpResponse<String> response = hubSpotService.getSubscriptions(apikey);

            if (response.getStatus() != HttpStatus.OK.value()) {
                logger.error("Erro ao obter as assinaturas do webhook: " + response.getBody());
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.APIKEY_INVALID.getStatusCode(),
                                StatusHubSpotApiEnum.APIKEY_INVALID.getStatus())
                        );
            }

            WebhookEventResponse webhookEventResponse = serializationUtils.jsonToObject(response.getBody(), WebhookEventResponse.class);

            WebhookEvent webhookEvent = webhookEventResponse
                    .getResults()
                    .stream()
                    .filter(e -> e.getEventType().equalsIgnoreCase(EventTypeSubscriptionEnum.CONTACT_CREATION.getType()))
                    .findFirst()
                    .orElse(null);

            if (Objects.isNull(webhookEvent)) {
                logger.error("Evento de webhook não encontrado.");
                logger.error("Eventos disponíveis: " + webhookEventResponse.getResults());
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.EVENT_NOT_FOUND.getStatusCode(),
                                StatusHubSpotApiEnum.EVENT_NOT_FOUND.getStatus())
                        );
            }

            if (Objects.isNull(eventSubscriptionProcessDTO)) {
                boolean isActive = webhookEvent.isActive();
                eventSubscriptionProcessDTO = new EventSubscriptionProcessDTO(!isActive);

                logger.info("DTO vazio: Criando novo evento de webhook com status: " + eventSubscriptionProcessDTO.isActive());
            }

            String jsonBody = serializationUtils.objectToJson(eventSubscriptionProcessDTO);

            HttpResponse<String> responsePatch = hubSpotService.patchUpdateSubscriptions(webhookEvent.getId(), apikey, jsonBody);

            if (responsePatch.getStatus() != HttpStatus.OK.value()) {
                logger.error("Erro ao atualizar a assinatura do webhook: " + responsePatch.getBody());
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.PARAMETER_INVALID.getStatusCode(),
                                StatusHubSpotApiEnum.PARAMETER_INVALID.getStatus())
                        );
            }

            WebhookEventDTO webhookEventDTO = serializationUtils.jsonToObject(responsePatch.getBody(), WebhookEventDTO.class);
            webhookEventDTO.setUpdatedAt(ZonedDateTime.parse(DateAndHourUtils.getDateAndHourNow()));

            logger.info("Assinatura do webhook atualizada com sucesso");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(webhookEventDTO);
        } catch (SerializationUtilsException e) {
            logger.error("Erro de serialização: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(
                            StatusHubSpotApiEnum.ERRO_SERIALIZACAO.getStatusCode(),
                            StatusHubSpotApiEnum.ERRO_SERIALIZACAO.getStatus())
                    );
        } catch (Exception e) {
            logger.error("Erro ao processar o webhook: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(e.getMessage()));
        }
    }
}