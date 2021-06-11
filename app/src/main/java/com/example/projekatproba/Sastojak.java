package com.example.projekatproba;

public class Sastojak {

    private String ime;
    private String urlSlike;

    public Sastojak() {
    }

    public Sastojak(String ime, String urlSlike) {
        this.ime = ime;
        this.urlSlike = urlSlike;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getUrlSlike() {
        return urlSlike;
    }

    public void setUrlSlike(String urlSlike) {
        this.urlSlike = urlSlike;
    }
}
