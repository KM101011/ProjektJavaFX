package com.example.projekt_1.util;

import com.example.projekt_1.model.ScientificWorkChange;

import java.nio.file.Path;


public class ScientificWorkChangeWriter extends ObjectWriter<ScientificWorkChange> {

    private static Path TRACK_CHANGE_PATH = Path.of("dat/scientificWorkTracker.ser");

    public ScientificWorkChangeWriter() {
        super(TRACK_CHANGE_PATH);
    }

}
