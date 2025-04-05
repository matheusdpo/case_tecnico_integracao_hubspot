package br.com.meetime.hubspot.v1.dto.response;

public class AuthorizationUrlDTO {
    private String url;

    public AuthorizationUrlDTO() {
    }

    public AuthorizationUrlDTO(String url) {
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
