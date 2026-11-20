
package br.com.colheitasmart.model;

public class Agricultor {

    private Long cdAgricultor;
    private String nmAgricultor;
    private String dsEmail;
    private String nrTelefone;
    private String nmCidade;

    // Armazena somente o hash da senha, nunca a senha original.
    private String dsSenhaHash;

    public Long getCdAgricultor() {
        return cdAgricultor;
    }

    public void setCdAgricultor(Long cdAgricultor) {
        this.cdAgricultor = cdAgricultor;
    }

    public String getNmAgricultor() {
        return nmAgricultor;
    }

    public void setNmAgricultor(String nmAgricultor) {
        this.nmAgricultor = nmAgricultor;
    }

    public String getDsEmail() {
        return dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    public String getNrTelefone() {
        return nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    public String getNmCidade() {
        return nmCidade;
    }

    public void setNmCidade(String nmCidade) {
        this.nmCidade = nmCidade;
    }

    public String getDsSenhaHash() {
        return dsSenhaHash;
    }

    public void setDsSenhaHash(String dsSenhaHash) {
        this.dsSenhaHash = dsSenhaHash;
    }
}
