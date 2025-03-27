package com.example.projekt_1.controlleri;

import com.example.projekt_1.exceptions.PasswordHashingException;
import com.example.projekt_1.model.Profesor;
import com.example.projekt_1.model.Student;
import com.example.projekt_1.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordRepeat;
    @FXML
    private ComboBox<String> userRole;
    @FXML
    private TextField userJmbg;
    @FXML
    private Button registerButton;
    @FXML
    private Label redLabel;
    @FXML
    private Label greenLabel;

    private List<User> profesorList = new ArrayList<>();
    private List<User> studentList = new ArrayList<>();

    private static final String FILE_STUDENT = "dat/studenti.txt";
    private static final String FILE_PROFESSOR = "dat/profesori.txt";

    public void initialize(){

        ObservableList<String> options = FXCollections.observableArrayList(
                "odaberite rolu",
                "student",
                "profesor"
        );

        userRole.setItems(options);
        userRole.getSelectionModel().selectFirst();

        registerButton.setOnAction(event -> {
            try {
                saveDataToFile();
            } catch (PasswordHashingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void saveDataToFile() throws PasswordHashingException {

        String usernameText = username.getText();
        String passwordText = password.getText();
        String passwordRepeatText = passwordRepeat.getText();
        String userRoleValue = userRole.getValue();
        String userJmbgText = userJmbg.getText();

        if (usernameText.isEmpty() || passwordText.isEmpty() || passwordRepeatText.isEmpty() || userRoleValue.isEmpty() || userJmbgText.isEmpty()) {
            redLabel.setText("Morate popuniti sva polja");
            return;
        }

        if (!passwordText.equals(passwordRepeatText)) {
            redLabel.setText("Lozinke nisu jednake");
            return;
        }

        String hashedPassword;

        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(passwordText.getBytes(StandardCharsets.UTF_8));
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error hashing password.", e);
            throw new PasswordHashingException("Error hashing password.", e);
        }

        hashedPassword = sb.toString();

        User newUser = createUser(usernameText, hashedPassword, userRoleValue, userJmbgText);

        if (newUser != null) {
            saveUserToFile(newUser, userRoleValue);
            redLabel.setText("");
            greenLabel.setText("Uspjesno unesen korisnik");
            clearFields();
        } else {
            greenLabel.setText("");
            redLabel.setText("Gresnka pri unosu korisnika");
        }
    }

    private User createUser(String username, String password, String role, String jmbg) {
        int id;
        if (role.equals("student")) {
            id = countRows(FILE_STUDENT) + 1;
            Student student = new Student(id, username, password, role, jmbg);
            studentList.add(student);
            return student;
        } else if (role.equals("profesor")) {
            id = countRows(FILE_PROFESSOR) + 1;
            Profesor profesor = new Profesor(id, username, password, role, jmbg);
            profesorList.add(profesor);
            return profesor;
        }
        return null;
    }

    private int countRows(String filename) {
        int count = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            while (bufferedReader.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private void saveUserToFile(User user, String role) {
        String filePath = (role.equals("student")) ? FILE_STUDENT : FILE_PROFESSOR;

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath, true))) {
            bufferedWriter.write(user.toString());
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        username.clear();
        password.clear();
        passwordRepeat.clear();
        userRole.getSelectionModel().selectFirst();
        userJmbg.clear();
    }
}