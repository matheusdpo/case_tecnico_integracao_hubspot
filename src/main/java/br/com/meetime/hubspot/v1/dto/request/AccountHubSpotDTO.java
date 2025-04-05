package br.com.meetime.hubspot.v1.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AccountHubSpotDTO {

    @JsonProperty("firstname")
    private String nome;

    @JsonProperty("lastname")
    private String sobrenome;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String telefone;

    @JsonProperty("company")
    private String empresa;


    public AccountHubSpotDTO() {
    }

    public AccountHubSpotDTO(String nome, String sobrenome, String email, String telefone, String empresa) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.empresa = empresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public boolean hasEmptyOrNullFields() {
        return isNullOrEmpty(nome) ||
                isNullOrEmpty(sobrenome) ||
                isNullOrEmpty(email) ||
                isNullOrEmpty(telefone) ||
                isNullOrEmpty(empresa);
    }

    private boolean isNullOrEmpty(String value) {
        return Objects.isNull(value) || value.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "AccountHubSpotDTO{" +
                "nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", empresa='" + empresa + '\'' +
                '}';
    }

}
