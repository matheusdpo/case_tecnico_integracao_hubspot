package br.com.meetime.hubspot.v1.controller.hubspot.swagger;

import br.com.meetime.hubspot.v1.dto.response.AuthorizationUrlDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface HubSpotAuthControllerSwagger {
    @GetMapping("/auth-url")
    @Operation(summary = "Return URL authentication", description = "Return URL authentication",
            tags = {"HubSpot Auth API"}, responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AuthorizationUrlDTO.class))
            }),

            @ApiResponse(description = "Bad Request", responseCode = "400", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorDTO.class))
            }),

            @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = InternalServerErrorDTO.class))
            })}
    )
    ResponseEntity<?> getAuthorizationUrl(@RequestParam(required = false) String clientId);
}
