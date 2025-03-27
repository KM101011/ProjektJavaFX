package com.example.projekt_1.util.threads;

import com.example.projekt_1.model.ScientificWorkChange;
import com.example.projekt_1.util.ScientificWorkChangeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WriteChangeThread extends Thread{

    public Logger logger = LoggerFactory.getLogger(WriteChangeThread.class);

    ScientificWorkChange changes;

    public WriteChangeThread(ScientificWorkChange changes) {
        super();
       this.changes = changes;
    }

    @Override
    public void run() {
       logger.info("Pokrenut thread za upis podataka...");
        ScientificWorkChangeWriter objectWriter = new ScientificWorkChangeWriter();
        objectWriter.write(changes);
        logger.info("Završio thread za upis podataka...");
    }
}
