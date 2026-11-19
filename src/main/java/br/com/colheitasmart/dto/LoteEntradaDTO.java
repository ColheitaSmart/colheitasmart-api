package br.com.colheitasmart.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class LoteEntradaDTO {
    @NotNull @Positive
    private Long cdAgricultor;

    @NotNull @Positive
    private Long cdCultura;

    @NotNull @DecimalMin(value = "0", inclusive = false) @Digits(integer = 10, fraction = 3)
    private BigDecimal qtPesoInicialKg;

    @NotNull @DecimalMin("0") @Digits(integer = 10, fraction = 3)
    private BigDecimal qtPesoDisponivelKg;

    @NotNull @PastOrPresent
    private LocalDate dtColheita;

    @NotBlank @Size(max = 200)
    private String dsArmazenamento;

    @NotNull @DecimalMin("0") @Digits(integer = 10, fraction = 2)
    private BigDecimal vlPrecoKg;

    @NotBlank @Size(max = 20) @Pattern(regexp = "DISPONIVEL|ESGOTADO|ENCERRADO")
    private String dsStatus;

    public Long getCdAgricultor() { return cdAgricultor; }
    public void setCdAgricultor(Long cdAgricultor) { this.cdAgricultor = cdAgricultor; }

    public Long getCdCultura() { return cdCultura; }
    public void setCdCultura(Long cdCultura) { this.cdCultura = cdCultura; }

    public BigDecimal getQtPesoInicialKg() { return qtPesoInicialKg; }
    public void setQtPesoInicialKg(BigDecimal qtPesoInicialKg) { this.qtPesoInicialKg = qtPesoInicialKg; }

    public BigDecimal getQtPesoDisponivelKg() { return qtPesoDisponivelKg; }
    public void setQtPesoDisponivelKg(BigDecimal qtPesoDisponivelKg) { this.qtPesoDisponivelKg = qtPesoDisponivelKg; }

    public LocalDate getDtColheita() { return dtColheita; }
    public void setDtColheita(LocalDate dtColheita) { this.dtColheita = dtColheita; }

    public String getDsArmazenamento() { return dsArmazenamento; }
    public void setDsArmazenamento(String dsArmazenamento) { this.dsArmazenamento = dsArmazenamento; }

    public BigDecimal getVlPrecoKg() { return vlPrecoKg; }
    public void setVlPrecoKg(BigDecimal vlPrecoKg) { this.vlPrecoKg = vlPrecoKg; }

    public String getDsStatus() { return dsStatus; }
    public void setDsStatus(String dsStatus) { this.dsStatus = dsStatus; }
}
