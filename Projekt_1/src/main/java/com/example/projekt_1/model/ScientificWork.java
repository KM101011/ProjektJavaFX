package com.example.projekt_1.model;

import com.example.projekt_1.interfaces.ChangeTracker;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public non-sealed class ScientificWork implements Serializable, ChangeTracker {

    private static final long serialVersionID = 1L;

    private Integer id;
    private String name;
    private String details;
    private String category;
    private String mentor;
    private LocalDate date;
    private String path;
    private Integer idKorisnika;

    public ScientificWork(Integer id, String name, String details, String category, String mentor, LocalDate date, String path, Integer idKorisnika) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.category = category;
        this.mentor = mentor;
        this.date = date;
        this.path = path;
        this.idKorisnika = idKorisnika;
    }

    public ScientificWork(Integer id, String name, Integer idKorisnika){
        this.id = id;
        this.name = name;
        this.idKorisnika = idKorisnika;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getIdKorisnika() {
        return idKorisnika;
    }

    public void setIdKorisnika(Integer idKorisnika) {
        this.idKorisnika = idKorisnika;
    }

    @Override
    public String printChange() {

        return "Naziv: " + getName() + "\nDetalji: " + getDetails() + "\nDate: " + getDate();
    }
}
