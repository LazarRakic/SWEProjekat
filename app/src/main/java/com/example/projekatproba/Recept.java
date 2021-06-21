package com.example.projekatproba;

import java.sql.Timestamp;

public class Recept {
    private String idRecepta;
    private String naziv;
    private String priprema;
    private String sastojci;
    private Long datum;
    private String idKorisnika;
    private String slika;
    private String prosecnaOCENA;
    private String usernameKorisnika;

    public Recept(String idRecepta, String naziv, String priprema, String sastojci, Long datum, String idKorisnika, String slika,String prosecnaOCENA,String usernameKorisnika) {
        this.idRecepta= idRecepta;
        this.naziv = naziv;
        this.priprema = priprema;
        this.sastojci = sastojci;
        this.datum = datum;
        this.idKorisnika= idKorisnika;
        this.slika= slika;
        this.prosecnaOCENA=prosecnaOCENA;
        this.usernameKorisnika=usernameKorisnika;
    }

    public String getUsernameKorisnika() {
        return usernameKorisnika;
    }

    public void setUsernameKorisnika(String usernameKorisnika) {
        this.usernameKorisnika = usernameKorisnika;
    }

    public String getIdRecepta() {return idRecepta;}

    public void setIdRecepta(String idRecepta) {this.idRecepta= idRecepta;}

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPriprema() {
        return priprema;
    }

    public void setPriprema(String priprema) {
        this.priprema = priprema;
    }

    public String getSastojci() {
        return sastojci;
    }

    public void setSastojci(String sastojci) {
        this.sastojci = sastojci;
    }

    public Long getDatum() {
        return datum;
    }

    public void setDatum(Long datum) {
        this.datum = datum;
    }

    public String getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(String idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getProsecnaOCENA() {
        return prosecnaOCENA;
    }

    public void setProsecnaOCENA(String prosecnaOCENA) {
        this.prosecnaOCENA = prosecnaOCENA;
    }
}
