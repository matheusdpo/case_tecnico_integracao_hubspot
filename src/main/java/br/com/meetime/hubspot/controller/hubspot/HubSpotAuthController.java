package br.com.meetime.hubspot.controller.hubspot;

import br.com.meetime.hubspot.dto.response.AuthorizationUrlDTO;
import br.com.meetime.hubspot.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.service.HubSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotAuthController {

    @Autowired
    private HubSpotService hubSpotService;

    @GetMapping("/auth-url")
    public ResponseEntity<?> getAuthorizationUrl(@RequestParam(required = false) String clientId) {
        try {

            if (Objects.isNull(clientId) || clientId.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
            }

            String url = hubSpotService.getUrl(clientId);

            AuthorizationUrlDTO authorizationUrlDTO = new AuthorizationUrlDTO(url);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authorizationUrlDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(e.getMessage()));
        }

    }
}