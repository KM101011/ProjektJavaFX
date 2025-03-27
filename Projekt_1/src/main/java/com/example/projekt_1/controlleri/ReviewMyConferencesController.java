package com.example.projekt_1.controlleri;

import com.example.projekt_1.database.Database;
import com.example.projekt_1.database.DatabaseConnection;
import com.example.projekt_1.exceptions.BazaPodatakaException;
import com.example.projekt_1.main.Main;
import com.example.projekt_1.model.*;
import com.example.projekt_1.util.UsersUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReviewMyConferencesController {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static boolean reviewAllConferencesRequested;

    @FXML
    private TableView<Conference> tableView;
    @FXML
    private TableColumn<Conference, Integer> idColumn;
    @FXML
    private TableColumn<Conference, String> nameColumn;
    @FXML
    private TableColumn<Conference, Integer> idUserColumn;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button listButton;
    @FXML
    private TextField search;

    ObservableList<Conference> conferences = FXCollections.observableArrayList();
    ObservableList<Applicants> applicants = FXCollections.observableArrayList();
    ObservableList<Conference> filteredScientificWorks = FXCollections.observableArrayList();
    private UserMap<String, User> userIDs = new UserMap<>();

    public UserMap<String, User> getUserIDs() {
        return userIDs;
    }

    private String loggedInUsername;
    private Integer selectedId;

    public ReviewMyConferencesController() {
        UsersUtil usersUtil = new UsersUtil();
        userIDs = usersUtil.getAllUsers();
    }

    @FXML
    public void initialize() {

        editButton.setVisible(!reviewAllConferencesRequested);
        deleteButton.setVisible(!reviewAllConferencesRequested);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("idKonferencije"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idProfesora"));

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            searchScientificWorks(newValue);
        });

        if (reviewAllConferencesRequested) {

            try {
                loadConference(null);
                tableView.setItems(conferences);
            }catch (BazaPodatakaException e) {
                String tGreska = "Error saving scientific work";
                logger.error(tGreska, e);
                System.err.println("Error saving scientific work: " + e.getMessage());
            }
        }else {
            try {
                loggedInUsername = LoginController.getName();
                User loggedInUserId = userIDs.getUser(loggedInUsername);
                if (loggedInUserId != null) {
                    Integer userId = loggedInUserId.getId();
                    loadConference(userId);
                } else {
                    logger.info("Logged-in user ID not found.");
                    System.err.println("Logged-in user ID not found.");
                }
                tableView.setItems(conferences);
            } catch (BazaPodatakaException e) {
                String tGreska = "Error saving scientific work";
                logger.error(tGreska, e);
                System.err.println("Error saving scientific work: " + e.getMessage());
            }
        }
    }

    @FXML
    private void rowSelection() {

        Conference selectedConference = tableView.getSelectionModel().getSelectedItem();

        if (selectedConference != null) {
            int selectedId = selectedConference.getIdKonferencije();
            openEditScreen(selectedId);
        }
    }

    @FXML
    private void deleteWork() {

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Delete");
        confirmationAlert.setHeaderText("Are you sure you want to delete this work?");
        confirmationAlert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() != ButtonType.OK){
            return;
        }


        Conference conference = tableView.getSelectionModel().getSelectedItem();

        if (conference != null) {
            int selectedId = conference.getIdKonferencije();

            try {
                Database.deleteConference(selectedId);
                tableView.getItems().remove(conference);
                logger.info("Scientific work with ID " + selectedId + " deleted successfully.");
            } catch (BazaPodatakaException e) {
                String errorMessage = "Error deleting scientific work";
                logger.error(errorMessage, e);
            }
        } else {
            System.out.println("No work selected to delete.");
        }

    }

    private void openEditScreen(int selectedId) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projekt_1/myConferenceInput.fxml"));
            Parent editScreenParentt = fxmlLoader.load();
            InputMyConferencesController controller = fxmlLoader.getController();

            controller.menu.setVisible(false);
            controller.takeDataa(selectedId);

            if (selectedId == 0){
                controller.setIsEditMode(false);
            }else {
                controller.takeDataa(selectedId);
                controller.setIsEditMode(true);
            }

            Stage editStage = new Stage();
            Scene editScene = new Scene(editScreenParentt);
            editStage.setScene(editScene);
            editStage.showAndWait();

            if (reviewAllConferencesRequested) {
                loadConference(null);
            } else {
                User loggedInUserId = userIDs.getUser(loggedInUsername);
                if (loggedInUserId != null) {
                    loadConference(loggedInUserId.getId());
                }
            }
            tableView.refresh();
            controller.menu.setVisible(true);
        } catch (IOException | BazaPodatakaException e) {
            logger.error("Error opening edit screen", e);
            e.printStackTrace();
        }
    }

    private void loadConference(Integer userId) throws BazaPodatakaException {

        conferences.clear();
        String query;

        if (userId != null){
            query = "SELECT ID_KONFERENCIJE, NASLOV, ID_PROFESORA FROM KONFERENCIJE WHERE ID_PROFESORA = " + userId;

        }else {
            query = "SELECT ID_KONFERENCIJE, NASLOV, ID_PROFESORA FROM KONFERENCIJE";
        }

        try (Connection connection = DatabaseConnection.establishConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID_KONFERENCIJE");
                String name = resultSet.getString("NASLOV");
                int idProfesora = resultSet.getInt("ID_PROFESORA");
                Conference conf = new Conference.Builder()
                        .setIdKonferencije(id)
                        .setName(name)
                        .setIdProfesora(idProfesora)
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
    private void rowSelectionForStudents() {

        Conference selectedConference2 = tableView.getSelectionModel().getSelectedItem();
        selectedId = selectedConference2.getIdKonferencije();

        if (selectedConference2 != null) {
            openListOfStudents();
        }
    }

    @FXML
    private void openListOfStudents() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projekt_1/listOfStudents.fxml"));
            Parent editScreenParent = fxmlLoader.load();

            ListOfStudentsController listOfStudentsController = fxmlLoader.getController();
            loadStudents(selectedId);
            listOfStudentsController.setData(applicants);

            Stage editStage = new Stage();
            Scene editScene = new Scene(editScreenParent);
            editStage.setScene(editScene);
            editStage.showAndWait();

        } catch (IOException e) {
            logger.error("Error opening edit screen", e);
            e.printStackTrace();
        }
    }

    @FXML
    public void loadStudents(Integer selectedId){

        applicants.clear();
        String query;

        if (selectedId != null){
            query = "SELECT ID_KONFERENCIJE, ID_STUDENTI, STUDENT FROM KONF_STUDENTI WHERE ID_KONFERENCIJE = " + selectedId;
        }else {
            query = "SELECT ID_KONFERENCIJE, ID_STUDENTI, STUDENT FROM KONF_STUDENTI";
        }

        try (Connection connection = DatabaseConnection.establishConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSett = statement.executeQuery()) {
            while (resultSett.next()) {
                int id = resultSett.getInt("ID_KONFERENCIJE");
                int idProfesora = resultSett.getInt("ID_STUDENTI");
                String ime = resultSett.getString("STUDENT");
                Applicants app = new Applicants(id, idProfesora, ime);
                applicants.add(app);
            }
        }
        catch(SQLException | IOException e){
            String tGreska = "Error with getting user";
            logger.error(tGreska, e);
        }
    }

    private void searchScientificWorks(String query) {
        if (query == null || query.isEmpty()) {
            filteredScientificWorks.setAll(conferences);
        } else {
            filteredScientificWorks.setAll(conferences.stream()
                    .filter(conference -> conference.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        tableView.setItems(filteredScientificWorks);
    }
}
