package com.example.projekt_1.controlleri;

import com.example.projekt_1.exceptions.Projekt1Exception;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;

import java.io.IOException;

public class MenuController {

    @FXML
    private MenuBar student;
    @FXML
    private MenuBar profesor;

    public void initialize() {
        System.out.println(LoginController.role);

        if (LoginController.role.equals("profesor")){
            student.setVisible(false);
            profesor.setVisible(true);
        }else {
            student.setVisible(true);
            profesor.setVisible(false);
        }
    }

    @FXML
    public void showMyConferencesReview() {
        showReviewForConference(false);
    }

    @FXML
    public void showAllConferencesReview() {
        showReviewForConference(true);
    }

    private void showReviewForConference(boolean reviewAllConferences) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/myConferenceReview.fxml"));
        Parent root;
        try {
            ReviewMyConferencesController.reviewAllConferencesRequested = reviewAllConferences;
            root = fxmlLoader.load();
        } catch (IOException e) {
            //TODO dodati logger koji logira exception
            throw new Projekt1Exception("Greška u aplikaciji", e);
        }

        Scene scene = new Scene(root, 600, 700);
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    @FXML
    public void showMyWorksInput(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/myWorksInput.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        } catch (IOException e) {
            throw new Projekt1Exception("Greška u aplikaciji");
        }
    }

    @FXML
    public void showMyWorksReview() {
        showReview(false);
    }

    @FXML
    public void showAllWorksReview() {
        showReview(true);
    }

    private void showReview(boolean reviewAllWorks) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/myWorksReview.fxml"));
        Parent root;
        try {
            ReviewMyWorksController.reviewAllWorksRequested = reviewAllWorks;
            root = fxmlLoader.load();
        } catch (IOException e) {
            //TODO dodati logger koji logira exception
            throw new Projekt1Exception("Greška u aplikaciji");
        }

        Scene scene = new Scene(root, 600, 700);
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    @FXML
    public void showMyConferencesInput(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/myConferenceInput.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        } catch (IOException e) {
            throw new Projekt1Exception("Greška u aplikaciji");
        }
    }

    @FXML
    public void showApplyToConference(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/applyToConference.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        } catch (IOException e) {
            throw new Projekt1Exception("Greška u aplikaciji");
        }
    }

    @FXML
    public void showChanges(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/changes.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 600, 700);
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        } catch (IOException e) {
            throw new Projekt1Exception("Greška u aplikaciji");
        }
    }

    @FXML
    public void handlelogout(){

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/projekt_1/login.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 256, 260);
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        } catch (IOException e) {
            throw new Projekt1Exception("Greška u aplikaciji");
        }
    }
}
