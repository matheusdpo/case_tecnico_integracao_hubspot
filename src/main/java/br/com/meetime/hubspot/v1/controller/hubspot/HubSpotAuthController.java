package br.com.meetime.hubspot.v1.controller.hubspot;

import br.com.meetime.hubspot.v1.controller.hubspot.swagger.HubSpotAuthControllerSwagger;
import br.com.meetime.hubspot.v1.dto.response.AuthorizationUrlDTO;
import br.com.meetime.hubspot.v1.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.v1.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.v1.service.HubSpotService;
import br.com.meetime.hubspot.v1.utils.LogUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "HubSpot Auth API", description = "API for managing authentication")
public class HubSpotAuthController implements HubSpotAuthControllerSwagger {

    private static final Logger log = LoggerFactory.getLogger(HubSpotAuthController.class);
    @Autowired
    private HubSpotService hubSpotService;

    @Autowired
    private LogUtils logger;

    @GetMapping("/auth-url")
    @Override
    public ResponseEntity<?> getAuthorizationUrl(@RequestParam(required = false) String clientId) {
        try {
            logger.info("Iniciando a geração da URL de autorização.");

            if (Objects.isNull(clientId) || clientId.isEmpty()) {
                logger.error("Client ID não fornecido.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
            }

            String url = hubSpotService.getUrl(clientId);

            AuthorizationUrlDTO authorizationUrlDTO = new AuthorizationUrlDTO(url);

            logger.info("URL de autorização gerada com sucesso: " + url);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authorizationUrlDTO);
        } catch (Exception e) {
            logger.error("Erro ao gerar a URL de autorização: " + e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(e.getMessage()));
        }
    }
}