package com.example.projekt_1.util;

import com.example.projekt_1.exceptions.ObjectWriterException;
import com.example.projekt_1.util.threads.ReadChangeThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ObjectWriter <T extends Serializable> {

    public static final Logger logger = LoggerFactory.getLogger(ObjectWriter.class);

    private final Path path;

    public ObjectWriter(Path path) {
        this.path = path;
    }

    public void write(T item) {
        List<T> items = readAll();
        items.add(item);
        writeAll(items);
    }

    public synchronized List<T> readAll() {
        List<T> items = new ArrayList<T>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path.toString()))) {
            while (true) {
                items.add((T)in.readObject());
            }
        } catch (EOFException e) {
            String errorMessage = String.format("Reached end of objects file after reading %d objects: %s", items.size(), path);
			logger.info(errorMessage);
        } catch (IOException e1) {
            String errorMessage = "IOException while reading from file";
			logger.info(errorMessage);
            throw new ObjectWriterException(errorMessage, e1);
        } catch (ClassNotFoundException e2) {
            String errorMessage = "ClassNotFoundException while reading an object from the file.";
            logger.error(errorMessage, e2);
            throw new ObjectWriterException(errorMessage, e2);
        }
        return items;
    }

    private void writeAll(List<T> items) {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path.toString(), false))) {
            for (T item : items) {
                out.writeObject(item);
            }
        } catch (FileNotFoundException e) {
            String message = "File not accessible: " + path;
			logger.error(message);
			throw new ObjectWriterException(message, e);
        } catch (IOException e) {
            String message = "Error while writing file: " + path;
			logger.error(message);
			throw new ObjectWriterException(message, e);
        }
    }
}