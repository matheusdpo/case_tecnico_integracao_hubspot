package br.com.meetime.hubspot.v1.controller.hubspot.swagger;

import br.com.meetime.hubspot.v1.dto.request.AccountHubSpotDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface HubSpotAccountControllerSwagger {
    @Operation(summary = "Create a new HubSpot account", description = "Creates a new HubSpot account with the provided information.",
            tags = {"HubSpot Account API"}, responses = {
            @ApiResponse(description = "Success", responseCode = "201", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AccountHubSpotDTO.class))
            }),

            @ApiResponse(description = "Bad Request", responseCode = "400", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorDTO.class))
            }),

            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorDTO.class))
            })}
    )
    @PostMapping("/create-account")
    ResponseEntity<?> createAccount(@RequestBody(required = false) AccountHubSpotDTO accountHubSpotDTO,
                                    @RequestHeader(name = "authorization", required = false) String token);
}
