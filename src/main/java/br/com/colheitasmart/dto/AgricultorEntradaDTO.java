package br.com.colheitasmart.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class AgricultorEntradaDTO {
    @NotBlank @Size(max = 100)
    private String nmAgricultor;

    @NotBlank @Size(max = 150) @Email
    private String dsEmail;

    @NotBlank @Size(max = 20)
    private String nrTelefone;

    @NotBlank @Size(max = 100)
    private String nmCidade;

    public String getNmAgricultor() { return nmAgricultor; }
    public void setNmAgricultor(String nmAgricultor) { this.nmAgricultor = nmAgricultor; }

    public String getDsEmail() { return dsEmail; }
    public void setDsEmail(String dsEmail) { this.dsEmail = dsEmail; }

    public String getNrTelefone() { return nrTelefone; }
    public void setNrTelefone(String nrTelefone) { this.nrTelefone = nrTelefone; }

    public String getNmCidade() { return nmCidade; }
    public void setNmCidade(String nmCidade) { this.nmCidade = nmCidade; }
}
