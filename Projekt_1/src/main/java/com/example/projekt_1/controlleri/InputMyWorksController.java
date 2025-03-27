package com.example.projekt_1.controlleri;

import com.example.projekt_1.database.Database;
import com.example.projekt_1.exceptions.BazaPodatakaException;
import com.example.projekt_1.model.ScientificWork;
import com.example.projekt_1.model.ScientificWorkChange;
import com.example.projekt_1.model.User;
import com.example.projekt_1.util.threads.WriteChangeThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class InputMyWorksController {

    private static final Logger logger = LoggerFactory.getLogger(InputMyWorksController.class);

    public AnchorPane menu;

    @FXML
    private ComboBox<String> kategorije;
    @FXML
    private TextField name;
    @FXML
    private TextArea details;
    @FXML
    private TextField mentor;
    @FXML
    private DatePicker selectedDate;
    @FXML
    private Label myLabel;
    @FXML
    private Button uploadButton;
    @FXML
    private Label myLabel2;

    private Integer id;
    private String nameCopy;
    private String detailsCopy;
    private String categoryCopy;
    private String mentorCopy;
    private LocalDate selectedDateCopy;
    private String filePathCopy;
    private Integer idKorisnikaCopy;

    private ScientificWork oldValue;
    private ScientificWork newValue;

    public void initialize() {
        Set<String> optionsSet = new HashSet<>(Arrays.asList(
                "Biologija",
                "Fizika",
                "Psihologija",
                "Informatika",
                "Ekonomija",
                "Medicina"
        ));

        ObservableList<String> options = FXCollections.observableArrayList(optionsSet);

        kategorije.setItems(options);
        kategorije.getSelectionModel().selectFirst();

    }

    private boolean isEditMode = false;

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @FXML
    private void uploadButton() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
                //new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                //new FileChooser.ExtensionFilter("Word FIles", "*.docx*")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null){
            String fileName = selectedFile.getName();
            filePathCopy = selectedFile.getAbsolutePath();
            uploadButton.setText(fileName);
        }
    }

    @FXML
    private void saveButton() {

        if (isEditMode) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Save");
            confirmationAlert.setHeaderText("Are you sure you want to save the changes?");
            confirmationAlert.setContentText("Saving will overwrite the current details.");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK) {
                return;
            }
        }

        nameCopy = name.getText();
        detailsCopy = details.getText();
        categoryCopy = kategorije.getValue();
        mentorCopy = mentor.getText();
        selectedDateCopy = selectedDate.getValue();

        String loggedInUsername = LoginController.getName();
        ReviewMyWorksController reviewMyWorksController = new ReviewMyWorksController();
        User loggedInUser = reviewMyWorksController.getUserIDs().getUser(loggedInUsername);

        if (loggedInUser != null) {
            idKorisnikaCopy = loggedInUser.getId();
        } else {
            logger.error("Logged-in user ID not found.");
            System.err.println("Logged-in user ID not found.");
            return;
        }

        newValue = new ScientificWork(id, nameCopy, detailsCopy, categoryCopy, mentorCopy, selectedDateCopy, filePathCopy, idKorisnikaCopy);

        ScientificWorkChange change = new ScientificWorkChange(loggedInUsername, LocalDateTime.now(), oldValue, newValue);

        WriteChangeThread changeThread = new WriteChangeThread(change);
        logger.info("Pokrećem thread za upis promjene");
        changeThread.start();
        ScientificWork scientificWork = new ScientificWork(id, nameCopy, detailsCopy, categoryCopy, mentorCopy, selectedDateCopy, filePathCopy, idKorisnikaCopy);

        try {
            if (id == null) {
                Database.insertScientificWork(scientificWork);
                logger.info("Scientific work saved successfully.");
                myLabel2.setText("Uspješno spremljeno");
            } else {
                Database.updateScientificWork(scientificWork);
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
    }

    public void takeData(int selectedId) {

        try {
            oldValue = Database.getScientificWorkById(selectedId);
            id = oldValue.getId();
            name.setText(oldValue.getName());
            details.setText(oldValue.getDetails());
            mentor.setText(oldValue.getMentor());
            kategorije.setValue(oldValue.getCategory());
            selectedDate.setValue(oldValue.getDate());
            uploadButton.setText(oldValue.getPath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
