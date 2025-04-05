package br.com.meetime.hubspot.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenDTO {

    @JsonProperty("access_token")
    private String accessToken;

    public AccessTokenDTO() {
    }

    public AccessTokenDTO(String accessToken) {
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
