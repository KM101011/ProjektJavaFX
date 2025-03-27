package com.example.projekt_1.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ScientificWorkChange implements Serializable {
    private ScientificWork oldValue;
    private ScientificWork newValue;
    LocalDateTime date;
    private String namee;

    public ScientificWorkChange(String namee, LocalDateTime date, ScientificWork oldValue, ScientificWork newValue) {
        this.namee = namee;
        this.date = date;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getNamee() {
        return namee;
    }

    public void setName(String name) {
        this.namee = name;
    }

    public ScientificWork getOldValue() {
        return oldValue;
    }

    public void setOldValue(ScientificWork oldValue) {
        this.oldValue = oldValue;
    }

    public ScientificWork getNewValue() {
        return newValue;
    }

    public void setNewValue(ScientificWork newValue) {
        this.newValue = newValue;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
