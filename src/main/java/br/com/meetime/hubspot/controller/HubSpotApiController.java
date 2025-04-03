package br.com.meetime.hubspot.controller;

import br.com.meetime.hubspot.dto.request.AccountHubSpotDTO;
import br.com.meetime.hubspot.dto.response.AccessTokenDTO;
import br.com.meetime.hubspot.dto.response.AuthorizationUrlDTO;
import br.com.meetime.hubspot.dto.response.InternalServerErrorDTO;
import br.com.meetime.hubspot.enums.StatusHubSpotApiEnum;
import br.com.meetime.hubspot.exceptions.SerializationUtilsException;
import br.com.meetime.hubspot.utils.SerializationUtils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Value("${hubspot.contacts.url}")
    private String contactsUrl;

    @Value("${hubspot.token.grantType}")
    private String grantType;

    @Autowired
    private SerializationUtils serializationUtils;

    @GetMapping("/authorize-url")
    public ResponseEntity<?> getAuthorizationUrl(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String clientId) {
        try {
//            if (Objects.isNull(accountId) || accountId.isEmpty()) {
//                return ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body(new InternalServerErrorResponse(
//                                StatusHubSpotApiEnum.ACCOUNTID_OBRIGATORIO.getStatusCode(),
//                                StatusHubSpotApiEnum.ACCOUNTID_OBRIGATORIO.getStatus()));
//            }
            if (Objects.isNull(clientId) || clientId.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                                StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
            }

//            String url = String.format("%s/%s/authorize/?client_id=%s&redirect_uri=%s&scope=%s", authUrl, accountId, clientId, redirectUri, scope);
            String url = String.format("%s/?client_id=%s&redirect_uri=%s&scope=%s", authUrl, clientId, redirectUri, scope);

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

    @GetMapping("/token-access")
    public ResponseEntity<?> getTokenAccess(@RequestParam(required = false) String clientId,
                                            @RequestParam(required = false) String clientSecret,
                                            @RequestParam(required = false) String code) {

        if (Objects.isNull(clientId) || clientId.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorDTO(
                            StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CLIENTID_OBRIGATORIO.getStatus()));
        }

        if (Objects.isNull(clientSecret) || clientSecret.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorDTO(
                            StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatusCode(),
                            StatusHubSpotApiEnum.CLIENTSECRET_OBRIGATORIO.getStatus()));
        }

        if (Objects.isNull(code) || code.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new InternalServerErrorDTO(
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

            AccessTokenDTO accessTokenDTO = serializationUtils.jsonToObject(response.getBody(), AccessTokenDTO.class);

            if (Objects.isNull(accessTokenDTO.getAccessToken()) || accessTokenDTO.getAccessToken().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new InternalServerErrorDTO(
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatusCode(),
                                StatusHubSpotApiEnum.ACCESS_TOKEN_NULL.getStatus())
                        );
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(accessTokenDTO);
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

            HttpResponse<String> response = Unirest.post(contactsUrl)
                    .header("Authorization", token)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

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