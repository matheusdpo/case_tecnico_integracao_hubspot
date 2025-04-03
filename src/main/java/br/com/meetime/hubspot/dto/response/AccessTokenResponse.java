package br.com.meetime.hubspot.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public AccessTokenResponse() {
    }

    public AccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    //getter
    public String getAccessToken() {
        return accessToken;
    }

    //setter
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
