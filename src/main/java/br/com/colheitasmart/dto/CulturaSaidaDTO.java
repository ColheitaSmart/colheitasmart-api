package br.com.colheitasmart.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CulturaSaidaDTO {
    private Long cdCultura;
    private String nmCultura;
    private String dsCategoria;

    public Long getCdCultura() { return cdCultura; }
    public void setCdCultura(Long cdCultura) { this.cdCultura = cdCultura; }

    public String getNmCultura() { return nmCultura; }
    public void setNmCultura(String nmCultura) { this.nmCultura = nmCultura; }

    public String getDsCategoria() { return dsCategoria; }
    public void setDsCategoria(String dsCategoria) { this.dsCategoria = dsCategoria; }
}
