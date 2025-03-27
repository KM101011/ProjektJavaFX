package com.example.projekt_1.util.threads;

import com.example.projekt_1.model.ScientificWorkChange;
import com.example.projekt_1.util.ScientificWorkChangeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReadChangeThread extends Thread{
    public Logger logger = LoggerFactory.getLogger(ReadChangeThread.class);
    @Override
    public void run() {

        while (true){

            logger.info("Pokrenut thread za citanje podataka...");
            ScientificWorkChangeWriter objectWriter = new ScientificWorkChangeWriter();
            List<ScientificWorkChange> scientificWorkChanges  = objectWriter.readAll();

            logger.info("Dosad se dogodilo " + scientificWorkChanges.size() + " promjena!");
            logger.info("Završio thread za citanje podataka...");

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.error("Dogodila se greska", e);
            }
        }
    }
}
