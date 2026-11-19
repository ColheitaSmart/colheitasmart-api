package br.com.colheitasmart.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Lote {
    private Long cdLote;
    private Long cdAgricultor;
    private Long cdCultura;
    private BigDecimal qtPesoInicialKg;
    private BigDecimal qtPesoDisponivelKg;
    private LocalDate dtColheita;
    private String dsArmazenamento;
    private BigDecimal vlPrecoKg;
    private String dsStatus;

    public Long getCdLote() {
        return cdLote;
    }

    public void setCdLote(Long cdLote) {
        this.cdLote = cdLote;
    }

    public Long getCdAgricultor() {
        return cdAgricultor;
    }

    public void setCdAgricultor(Long cdAgricultor) {
        this.cdAgricultor = cdAgricultor;
    }

    public Long getCdCultura() {
        return cdCultura;
    }

    public void setCdCultura(Long cdCultura) {
        this.cdCultura = cdCultura;
    }

    public BigDecimal getQtPesoInicialKg() {
        return qtPesoInicialKg;
    }

    public void setQtPesoInicialKg(BigDecimal qtPesoInicialKg) {
        this.qtPesoInicialKg = qtPesoInicialKg;
    }

    public BigDecimal getQtPesoDisponivelKg() {
        return qtPesoDisponivelKg;
    }

    public void setQtPesoDisponivelKg(BigDecimal qtPesoDisponivelKg) {
        this.qtPesoDisponivelKg = qtPesoDisponivelKg;
    }

    public LocalDate getDtColheita() {
        return dtColheita;
    }

    public void setDtColheita(LocalDate dtColheita) {
        this.dtColheita = dtColheita;
    }

    public String getDsArmazenamento() {
        return dsArmazenamento;
    }

    public void setDsArmazenamento(String dsArmazenamento) {
        this.dsArmazenamento = dsArmazenamento;
    }

    public BigDecimal getVlPrecoKg() {
        return vlPrecoKg;
    }

    public void setVlPrecoKg(BigDecimal vlPrecoKg) {
        this.vlPrecoKg = vlPrecoKg;
    }

    public String getDsStatus() {
        return dsStatus;
    }

    public void setDsStatus(String dsStatus) {
        this.dsStatus = dsStatus;
    }
}
