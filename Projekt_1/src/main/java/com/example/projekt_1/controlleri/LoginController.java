package com.example.projekt_1.controlleri;

import com.example.projekt_1.exceptions.BazaPodatakaException;
import com.example.projekt_1.exceptions.PasswordHashingException;
import com.example.projekt_1.model.User;
import com.example.projekt_1.model.UserMap;
import com.example.projekt_1.util.UsersUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label label3;
    @FXML
    private Button registerButton;

    static String name;
    private UserMap<String, User> users = new UserMap<>();
    public static String role;

    public LoginController() {
        UsersUtil usersUtil = new UsersUtil();
        users = usersUtil.getAllUsers();
    }

    @FXML
    private void initialize() {

        loginButton.setOnAction(event -> {

            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            setName(username);

            try {
                if (checkingCredentials(username, password)) {
                    LoginController.role = users.getUser(username).getRole();
                    loadFirstScreen();

                } else {
                    logger.debug("Invalid username or password");
                    label3.setText("Krivo korisnicko ime ili sifra");
                }
            } catch (PasswordHashingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private boolean checkingCredentials(String username, String password) throws PasswordHashingException {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error hashing password.", e);
            throw new PasswordHashingException("Error hashing password.", e);
        }

        if (users.containsKey(username)) {
            logger.info("Validating credentials for username: " + username);
            logger.info("Valid username " + username);
            return users.getUser(username).getPassword().equals(sb.toString());
        }
        return false;
    }

    private void loadFirstScreen() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projekt_1/firstScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Cant load first screen");
            String tGreska = "Cant load first screen";
            logger.error(tGreska, e);
        }
    }

    @FXML
    private void register() throws BazaPodatakaException{

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projekt_1/registration.fxml"));
            Parent registerParent = fxmlLoader.load();

            RegistrationController controller = fxmlLoader.getController();
            controller.initialize();

            Stage stage = new Stage();
            Scene scene = new Scene(registerParent);
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            logger.error("Error opening registration screen", e);
            throw new BazaPodatakaException("Error opening registration screen", e);
        }
    }

    public void setName(String name){
        this.name = name;
    }

    static public String getName(){
        return name;
    }
}
