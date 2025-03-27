package com.example.projekt_1.model;

import java.time.LocalDate;

public class Conference {

    private Integer idKonferencije;
    private String name;
    private String details;
    private String predmet;
    private String profesor;
    private LocalDate date;
    private Integer idProfesora;

    private Conference(Builder builder) {
        this.idKonferencije = builder.idKonferencije;
        this.name = builder.name;
        this.details = builder.details;
        this.predmet = builder.predmet;
        this.profesor = builder.profesor;
        this.date = builder.date;
        this.idProfesora = builder.idProfesora;
    }

    public Integer getIdKonferencije() {
        return idKonferencije;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getPredmet() {
        return predmet;
    }

    public String getProfesor() {
        return profesor;
    }

    public LocalDate getDate() {
        return date;
    }

    public Integer getIdProfesora() {
        return idProfesora;
    }

    public static class Builder {
        private Integer idKonferencije;
        private String name;
        private String details;
        private String predmet;
        private String profesor;
        private LocalDate date;
        private Integer idProfesora;

        public Builder setIdKonferencije(Integer idKonferencije) {
            this.idKonferencije = idKonferencije;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder setPredmet(String predmet) {
            this.predmet = predmet;
            return this;
        }

        public Builder setProfesor(String profesor) {
            this.profesor = profesor;
            return this;
        }

        public Builder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setIdProfesora(Integer idProfesora) {
            this.idProfesora = idProfesora;
            return this;
        }

        public Conference build() {
            return new Conference(this);
        }
    }
}
