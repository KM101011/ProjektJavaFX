package com.example.projekt_1.main;

import com.example.projekt_1.controlleri.HelloApplication;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        Application.launch(HelloApplication.class, args);

    }
}
