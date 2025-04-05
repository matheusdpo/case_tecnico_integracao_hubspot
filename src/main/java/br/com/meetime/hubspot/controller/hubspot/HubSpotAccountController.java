package br.com.meetime.hubspot.controller.hubspot;

import br.com.meetime.hubspot.dto.request.AccountHubSpotDTO;
import br.com.meetime.hubspot.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.service.HubSpotService;
import br.com.meetime.hubspot.utils.SerializationUtils;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotAccountController {

    @Autowired
    private SerializationUtils serializationUtils;

    @Autowired
    private HubSpotService hubSpotService;

    //todo rate limit
    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@RequestBody(required = false) AccountHubSpotDTO accountHubSpotDTO,
                                           @RequestHeader(name = "authorization", required = false) String token) {
        try {
            if (Objects.isNull(token) || token.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.BEARER_INFO_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.BEARER_INFO_NULL.getStatus())
                        );
            }

            if (accountHubSpotDTO.hasEmptyOrNullFields()) {
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
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(response.getBody()));
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(accountHubSpotDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorDTO(e.getMessage()));
        }
    }
}