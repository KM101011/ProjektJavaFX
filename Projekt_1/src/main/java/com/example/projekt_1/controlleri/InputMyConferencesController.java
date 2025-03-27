package com.example.projekt_1.controlleri;

import com.example.projekt_1.database.Database;
import com.example.projekt_1.exceptions.BazaPodatakaException;
import com.example.projekt_1.main.Main;
import com.example.projekt_1.model.Conference;
import com.example.projekt_1.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Optional;

public class InputMyConferencesController {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public AnchorPane menu;

    @FXML
    private ComboBox<String> predmet;
    @FXML
    private TextField name;
    @FXML
    private TextArea details;
    @FXML
    private TextField profesor;
    @FXML
    private DatePicker selectedDate;
    @FXML
    private Label myLabel;
    @FXML
    private Label myLabel2;

    private Conference konferencija;

    private Integer id;
    private String nameCopy;
    private String detailsCopy;
    private String predmetCopy;
    private String profesorCopy;
    private LocalDate selectedDateCopy;
    private Integer idProfesoraCopy;

    public void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "odaberite predmet",
                "Biologija",
                "Fizika",
                "Psihologija",
                "Informatika",
                "Ekonomija"
        );

        predmet.setItems(options);
        predmet.getSelectionModel().selectFirst();

    }

    private boolean isEditMode = false;

    public void setIsEditMode(boolean isEditMode){
        this.isEditMode = isEditMode;
    }

    @FXML
    private void saveButton() {

        if (isEditMode){

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Save");
            confirmationAlert.setHeaderText("Are you sure you want to save the changes?");
            confirmationAlert.setContentText("Saving will overwrite the current details.");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK){
                return;
            }
        }

        nameCopy = name.getText();
        detailsCopy = details.getText();
        predmetCopy = predmet.getValue();
        profesorCopy = profesor.getText();
        selectedDateCopy = selectedDate.getValue();

        String loggedInUsername = LoginController.getName();
        ReviewMyConferencesController reviewMyConferencesController = new ReviewMyConferencesController();
        User loggedInUser = reviewMyConferencesController.getUserIDs().getUser(loggedInUsername);

        if (loggedInUser != null) {
            idProfesoraCopy = loggedInUser.getId();
        } else {
            logger.error("Logged-in user ID not found.");
            System.err.println("Logged-in user ID not found.");
            return;
        }

        Conference konferencija = new Conference.Builder()
                .setIdKonferencije(id)
                .setName(nameCopy)
                .setDetails(detailsCopy)
                .setPredmet(predmetCopy)
                .setProfesor(profesorCopy)
                .setDate(selectedDateCopy)
                .setIdProfesora(idProfesoraCopy)
                .build();

        try {
            if (id == null) {
                Database.insertConference(konferencija);
                logger.info("Scientific work saved successfully.");
                myLabel2.setText("Uspješno spremljeno");
            } else {
                Database.updateConference(konferencija);
                logger.info("Scientific work updated successfully.");
                myLabel2.setText("Uspješno ažurirano");
            }
            myLabel.setText("");
        } catch (BazaPodatakaException e) {
            String errorMessage = "Error saving scientific work";
            logger.error(errorMessage, e);
            myLabel.setText("Greška spajanja s bazom");
            myLabel2.setText("");
        }

        System.out.println("Conference: " + konferencija.getName() + " " + konferencija.getPredmet() + " ");
    }

    public void takeDataa(int selectedId) {
        try {
            konferencija = Database.getConferenceById(selectedId);
            id = konferencija.getIdKonferencije();
            name.setText(konferencija.getName());
            details.setText(konferencija.getDetails());
            profesor.setText(konferencija.getProfesor());
            predmet.setValue(konferencija.getPredmet());
            selectedDate.setValue(konferencija.getDate());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
