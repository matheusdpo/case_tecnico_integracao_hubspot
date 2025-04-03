package br.com.meetime.hubspot.dto.response;

public class AuthorizationUrlResponse {
    private String url;

    public AuthorizationUrlResponse() {
    }

    public AuthorizationUrlResponse(String url) {
        this.url = url;
    }

    //getter

    public String getUrl() {
        return url;
    }

    //setter

    public void setUrl(String url) {
        this.url = url;
    }


}
