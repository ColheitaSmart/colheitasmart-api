package br.com.colheitasmart.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class CulturaEntradaDTO {
    @NotBlank @Size(max = 100)
    private String nmCultura;

    @NotBlank @Size(max = 100)
    private String dsCategoria;

    public String getNmCultura() { return nmCultura; }
    public void setNmCultura(String nmCultura) { this.nmCultura = nmCultura; }

    public String getDsCategoria() { return dsCategoria; }
    public void setDsCategoria(String dsCategoria) { this.dsCategoria = dsCategoria; }
}
