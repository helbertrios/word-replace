package br.br.helbert.word.replace.dto;

public class Fone {

    private String contryCode;
    private String contyCode;
    private String number;

    public Fone(String contryCode, String contyCode, String number) {
        this.contryCode = contryCode;
        this.contyCode = contyCode;
        this.number = number;
    }


    public String getContryCode() {
        return contryCode;
    }

    public void setContryCode(String contryCode) {
        this.contryCode = contryCode;
    }

    public String getContyCode() {
        return contyCode;
    }

    public void setContyCode(String contyCode) {
        this.contyCode = contyCode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
