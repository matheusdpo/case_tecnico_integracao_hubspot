package br.com.meetime.hubspot.v1.enums;

public enum StatusHubSpotApiEnum {

    CLIENTID_OBRIGATORIO(1, "Parametro clientId obrigatorio"),
    ERRO_SERIALIZACAO(2, "Erro ao realizar serializacao do Objeto, favor consultar administrador de sistemas"),
    ACCESS_TOKEN_NULL(3, "AccessToken retornado vazio, verifique se os parametros estao corretos ou consultar administrador de sistemas"),
    CLIENTSECRET_OBRIGATORIO(4, "Parametro clientSecret obrigatorio"),
    CODE_OBRIGATORIO(5, "Parametro code obrigatorio"),
    ACCOUNTID_OBRIGATORIO(6, "Parametro accountId obrigatorio"),
    ACCOUNT_INFO_NULL(7, "Parametros de conta (nome, sobrenome, email, telefone e empresa) sao obrigatorios"),
    BEARER_INFO_NULL(8, "Parametro Authorization obrigatorio"),
    APIKEY_NULL(9, "Parametro apikey obrigatorio"),
    APIKEY_INVALID(10, "apikey invalida, favor verificar se os parametros estao corretos ou consultar administrador de sistemas"),
    EVENT_NOT_FOUND(11, "Evento nao encontrado"),
    PARAMETER_INVALID(12, "Parametro invalido, favor verificar se os parametros estao corretos ou consultar administrador de sistemas");


    private final int statusCode;
    private final String status;

    StatusHubSpotApiEnum(int statusCode, String status) {
        this.statusCode = statusCode;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
