package com.example.projekt_1.util;

import com.example.projekt_1.interfaces.UsersUtilInterface;
import com.example.projekt_1.main.Main;
import com.example.projekt_1.model.Profesor;
import com.example.projekt_1.model.Student;
import com.example.projekt_1.model.User;
import com.example.projekt_1.model.UserMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class UsersUtil implements UsersUtilInterface {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final String filenameStudents = "dat/studenti.txt";
    private final String filenameProfessors = "dat/profesori.txt";

    @Override
    public UserMap<String, User> getAllUsers() {

        UserMap<String, User> userIDs = new UserMap<>();

        try (Stream<String> stream = Files.lines(Paths.get(filenameStudents))) {
            stream.forEach(line -> {
                String[] parts = line.split(" ");
                if (parts.length >= 5 && parts[3].equals("student")) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String password = parts[2];
                    String type = parts[3];
                    Student user = new Student(id, username, password, type, parts[4]);
                    userIDs.addUser(username, user);
                }
            });
        } catch (IOException e) {
            System.out.println("An error occurred while reading user data from the file: " + filenameStudents);
            String tGreska = "An error occurred while reading user data from the file: "+ filenameStudents;
            logger.error(tGreska, e);
        }

        try (Stream<String> stream = Files.lines(Paths.get(filenameProfessors))) {
            stream.forEach(line -> {
                String[] parts = line.split(" ");
                if (parts.length >= 5 && parts[3].equals("profesor")) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String password = parts[2];
                    String type = parts[3];
                    Profesor user2 = new Profesor(id, username, password, type, parts[4]);
                    userIDs.addUser(username, user2);
                }
            });
        } catch (IOException e) {
            System.out.println("An error occurred while reading user data from the file: " + filenameProfessors);
            String tGreska = "An error occurred while reading user data from the file: "+ filenameProfessors;
            logger.error(tGreska, e);
        }

        return userIDs;
    }
}
