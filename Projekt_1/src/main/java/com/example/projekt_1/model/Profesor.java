package com.example.projekt_1.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Profesor extends User {

    Logger logger = LoggerFactory.getLogger(Profesor.class);

    private String jmbag;

    public Profesor(Integer id, String username, String password, String role, String jmbag) {
        super(id, username, password, role);
        this.jmbag = jmbag;
    }

    public String getJmbag() {
        return jmbag;
    }

    public void setJmbag(String jmbag) {
        this.jmbag = jmbag;
    }

    @Override
    public void logDetails() {
        logger.info("Kreiran objekt profesora");
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s %s", getId(), getUsername(), getPassword(), getRole(), getJmbag());
    }
}
