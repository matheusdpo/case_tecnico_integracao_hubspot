package br.com.meetime.hubspot.controller.hubspot;

import br.com.meetime.hubspot.dto.request.EventSubscriptionProcessDTO;
import br.com.meetime.hubspot.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.dto.response.webhook.WebhookEvent;
import br.com.meetime.hubspot.dto.response.webhook.WebhookEventResponse;
import br.com.meetime.hubspot.enums.EventTypeSubscriptionEnum;
import br.com.meetime.hubspot.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.exceptions.SerializationUtilsException;
import br.com.meetime.hubspot.service.HubSpotService;
import br.com.meetime.hubspot.utils.DateAndHourUtils;
import br.com.meetime.hubspot.utils.SerializationUtils;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotEventSubscriptionController {

    @Autowired
    private HubSpotService hubSpotService;

    @Autowired
    private SerializationUtils serializationUtils;

    @PostMapping("/process-webhook")
    public ResponseEntity<?> getTokenAccess(@RequestParam(required = false) String apikey,
                                            @RequestBody(required = false) EventSubscriptionProcessDTO eventSubscriptionProcessDTO) {

        try {
            if (Objects.isNull(apikey) || apikey.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.APIKEY_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.APIKEY_NULL.getStatus()));
            }

            HttpResponse<String> response = hubSpotService.getSubscriptions(apikey);

            if (response.getStatus() != HttpStatus.OK.value()) {
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
            }

            String jsonBody = serializationUtils.objectToJson(eventSubscriptionProcessDTO);

            HttpResponse<String> responsePatch = hubSpotService.patchUpdateSubscriptions(webhookEvent.getId(), apikey, jsonBody);

            if (responsePatch.getStatus() != HttpStatus.OK.value()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.PARAMETER_INVALID.getStatusCode(),
                                StatusHubSpotApiEnum.PARAMETER_INVALID.getStatus())
                        );
            }

            WebhookEvent webhookEventResponsePatch = serializationUtils.jsonToObject(responsePatch.getBody(), WebhookEvent.class);
            webhookEventResponsePatch.setUpdatedAt(ZonedDateTime.parse(DateAndHourUtils.getDateAndHourNow()));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(webhookEventResponsePatch);
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