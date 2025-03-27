package com.example.projekt_1.controlleri;

import com.example.projekt_1.database.Database;
import com.example.projekt_1.database.DatabaseConnection;
import com.example.projekt_1.exceptions.BazaPodatakaException;
import com.example.projekt_1.main.Main;
import com.example.projekt_1.model.Applicants;
import com.example.projekt_1.model.Conference;
import com.example.projekt_1.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ApplyToConference {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    @FXML
    private TableView<Conference> tableView;
    @FXML
    private TableColumn<Conference, String> nameConference;
    @FXML
    private Button applyButton;
    @FXML
    private TableColumn<Conference, String> profesor;
    @FXML
    private Label greenLabel;

    private Integer idKorisnika;
    private String nameOfUser;
    ObservableList<Conference> conferences = FXCollections.observableArrayList();

    @FXML
    public void initialize(){

        nameConference.setCellValueFactory(new PropertyValueFactory<>("name"));
        profesor.setCellValueFactory(new PropertyValueFactory<>("profesor"));

        try {
            loadConference(null);
            tableView.setItems(conferences);
        }catch (BazaPodatakaException e) {
            String tGreska = "Error saving scientific work";
            logger.error(tGreska, e);
            System.err.println("Error saving scientific work: " + e.getMessage());
        }
    }

    private void loadConference(Integer userId) throws BazaPodatakaException {

        conferences.clear();
        String query;

        if (userId != null){
            query = "SELECT ID_KONFERENCIJE, NASLOV, ID_PROFESORA, PROFESOR FROM KONFERENCIJE WHERE ID_PROFESORA = " + userId;

        }else {
            query = "SELECT ID_KONFERENCIJE, NASLOV, ID_PROFESORA, PROFESOR FROM KONFERENCIJE";
        }

        try (Connection connection = DatabaseConnection.establishConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID_KONFERENCIJE");
                String name = resultSet.getString("NASLOV");
                int idProfesora = resultSet.getInt("ID_PROFESORA");
                String profesor = resultSet.getString("PROFESOR");
                Conference conf = new Conference.Builder()
                        .setIdKonferencije(id)
                        .setName(name)
                        .setIdProfesora(idProfesora)
                        .setProfesor(profesor)
                        .build();
                conferences.add(conf);
            }
        }
        catch(SQLException | IOException e){
            String tGreska = "Error with getting user";
            logger.error(tGreska, e);
            throw new BazaPodatakaException("Error with getting user");
        }
    }

    @FXML
    private void rowSelection() {

        Conference selectedConference = tableView.getSelectionModel().getSelectedItem();

        if (selectedConference != null) {
            int conferenceID = selectedConference.getIdKonferencije();
            applyButton(conferenceID);
        }
    }

    @FXML
    private void applyButton(int conferenceID) {

        String loggedinUsername = LoginController.getName();
        ReviewMyWorksController reviewMyWorksController = new ReviewMyWorksController();
        User loggedInUser = reviewMyWorksController.getUserIDs().getUser(loggedinUsername);

        if (loggedInUser != null){
            idKorisnika = loggedInUser.getId();
            nameOfUser = loggedInUser.getUsername();
        }

        Applicants applicants = new Applicants(conferenceID, idKorisnika, nameOfUser);

        try {
            Database.applyConference(applicants);
            greenLabel.setText("Uspješno ste prijavljeni");
        }catch (BazaPodatakaException e) {
            String errorMessage = "Error applying to conference";
            logger.error(errorMessage, e);
        }
    }
}
