package br.com.meetime.hubspot.dto.response;

public class InternalServerErrorDTO {

    private int statusCode = 0;
    private boolean error = true;
    private String message;

    public InternalServerErrorDTO() {

    }

    public InternalServerErrorDTO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public InternalServerErrorDTO(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
