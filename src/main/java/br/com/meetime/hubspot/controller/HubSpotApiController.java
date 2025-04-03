package br.com.meetime.hubspot.controller;

import br.com.meetime.hubspot.dto.response.AccessTokenResponse;
import br.com.meetime.hubspot.dto.response.AuthorizationUrlResponse;
import br.com.meetime.hubspot.dto.response.InternalServerErrorResponse;
import br.com.meetime.hubspot.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.exceptions.SerializationUtilsException;
import br.com.meetime.hubspot.utils.SerializationUtils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/hubspot")
public class HubSpotApiController {

    @Value("${hubspot.redirectUri}")
    private String redirectUri;

    @Value("${hubspot.auth.url}")
    private String authUrl;

    @Value("${hubspot.auth.scope}")
    private String scope;

    @Value("${hubspot.token.url}")
    private String tokenUrl;

    @Value("${hubspot.token.grantType}")
    private String grantType;

    @Autowired
    private SerializationUtils serializationUtils;

    @GetMapping("/authorize-url")
    public ResponseEntity<?> getAuthorizationUrl(@RequestParam(required = false) String clientId) {
        try {
            if (Objects.isNull(clientId) || clientId.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorResponse(
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
            }

            String url = String.format("%s?client_id=%s&redirect_uri=%s&scope=%s",
                    authUrl, clientId, redirectUri, scope);

            AuthorizationUrlResponse authorizationUrlResponse = new AuthorizationUrlResponse(url);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authorizationUrlResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorResponse(e.getMessage()));
        }

    }

    @GetMapping("/token-access")
    public ResponseEntity<?> getTokenAccess(@RequestParam String clientId,
                                            @RequestParam String clientSecret,
                                            @RequestParam String code) {

        if (Objects.isNull(clientId) || clientId.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorResponse(
                            StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
        }

        if (Objects.isNull(clientSecret) || clientSecret.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorResponse(
                            StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatus()));
        }

        if (Objects.isNull(code) || code.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorResponse(
                            StatusHubSpotApiEnum.CODE_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CODE_OBRIGATORIO.getStatus()));
        }

        try {
            HttpResponse<String> response = Unirest.post(tokenUrl)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .field("grant_type", grantType)
                    .field("client_id", clientId)
                    .field("client_secret", clientSecret)
                    .field("redirect_uri", redirectUri)
                    .field("code", code)
                    .asString();

            AccessTokenResponse accessTokenResponse = serializationUtils.jsonToObject(response.getBody(), AccessTokenResponse.class);

            if (Objects.isNull(accessTokenResponse.getAccessToken()) || accessTokenResponse.getAccessToken().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorResponse(
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatus())
                        );
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(accessTokenResponse);
        } catch (SerializationUtilsException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorResponse(
                            StatusHubSpotApiEnum.ERRO_SERIALIZACAO.getStatusCode(),
                            StatusHubSpotApiEnum.ERRO_SERIALIZACAO.getStatus())
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalServerErrorResponse(e.getMessage()));
        }
    }
}