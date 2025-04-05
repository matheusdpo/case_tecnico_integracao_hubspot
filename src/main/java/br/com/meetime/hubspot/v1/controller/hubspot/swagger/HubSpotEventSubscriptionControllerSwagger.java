package br.com.meetime.hubspot.v1.controller.hubspot.swagger;

import br.com.meetime.hubspot.v1.dto.request.EventSubscriptionProcessDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.v1.dto.response.WebhookEventDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface HubSpotEventSubscriptionControllerSwagger {
    @PostMapping("/process-webhook")
    @Operation(summary = "HubSpot Event Webhook Subscriptions API", description = "Get all subscriptions type contact.creation and update the status",
            tags = {"HubSpot Event Webhook Subscriptions API"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = WebhookEventDTO.class))
            }),

            @ApiResponse(description = "Bad Request", responseCode = "400", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorDTO.class))
            }),

            @ApiResponse(description = "Unauthorized", responseCode = "401", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorDTO.class))
            }),

            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorDTO.class))
            })}
    )
    ResponseEntity<?> getTokenAccess(@RequestParam(required = false) String apikey,
                                     @RequestBody(required = false) EventSubscriptionProcessDTO eventSubscriptionProcessDTO);
}
