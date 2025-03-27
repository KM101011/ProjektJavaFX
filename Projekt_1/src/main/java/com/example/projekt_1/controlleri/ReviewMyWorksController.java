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
import java.util.*;

public class ReviewMyWorksController {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static boolean reviewAllWorksRequested;

    @FXML
    private TableView<ScientificWork> tableView;
    @FXML
    private TableColumn<ScientificWork, Integer> idColumn;
    @FXML
    private TableColumn<ScientificWork, String> nameColumn;
    @FXML
    private TableColumn<ScientificWork, Integer> idUserColumn;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField search;
    @FXML
    private Button sortButton;
    @FXML
    private Spinner<Integer> idFilterSpinner;
    @FXML
    private Button filterButton;

    ObservableList<ScientificWork> scientificWorks = FXCollections.observableArrayList();
    ObservableList<ScientificWork> filteredScientificWorks = FXCollections.observableArrayList();
    ObservableList<ScientificWork> filteredWorks = FXCollections.observableArrayList();
    private UserMap<String, User> userIDs = new UserMap<>();

    public UserMap<String, User> getUserIDs() {
        return userIDs;
    }

    private String loggedInUsername;

    public ReviewMyWorksController() {
        UsersUtil usersUtil = new UsersUtil();
        userIDs = usersUtil.getAllUsers();
    }

    @FXML
    public void initialize() {

        editButton.setVisible(!reviewAllWorksRequested);
        deleteButton.setVisible(!reviewAllWorksRequested);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idKorisnika"));

        tableView.setItems(FXCollections.observableArrayList(scientificWorks));

        search.textProperty().addListener(actionEvent -> searchScientificWorks());

        sortButton.setOnAction(actionEvent -> sortScientificWorksByName());

        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0);
        idFilterSpinner.setValueFactory(spinner);
        filterButton.setOnAction(actionEvent -> filter());

        if (reviewAllWorksRequested) {
            try {
                loadScientificWorks(null);
                tableView.setItems(scientificWorks);
            }catch (BazaPodatakaException e) {
                String errorMessage = "Error saving scientific work";
                logger.error(errorMessage, e);
                System.err.println("Error saving scientific work: " + e.getMessage());
            }
        }else {
            try {
                loggedInUsername = LoginController.getName();
                User loggedInUserId = userIDs.getUser(loggedInUsername);
                if (loggedInUserId != null) {
                    Integer userId = loggedInUserId.getId();
                    loadScientificWorks(userId);
                } else {
                    logger.info("Logged-in user ID not found.");
                    System.err.println("Logged-in user ID not found.");
                }
                tableView.setItems(scientificWorks);

            } catch (BazaPodatakaException e) {
                String errorMessage = "Error saving scientific work";
                logger.error(errorMessage, e);
                System.err.println("Error saving scientific work: " + e.getMessage());
            }
        }
    }

    private void searchScientificWorks() {

        String searchWriting = search.getText().toLowerCase().trim();
        filteredScientificWorks.clear();

        if (searchWriting.isEmpty()) {
            filteredScientificWorks.addAll(scientificWorks);
        } else {
            filteredScientificWorks.addAll(scientificWorks.stream()
                    .filter(work -> work.getName().toLowerCase().contains(searchWriting))
                    .toList());
        }
        tableView.setItems(filteredScientificWorks);
    }

    private void sortScientificWorksByName() {
        tableView.getItems().sort((work1, work2) -> work1.getName().compareToIgnoreCase(work2.getName()));
    }

    @FXML
    private void filter(){
        int id = idFilterSpinner.getValue();
        filterByScientificWorkId(id);
    }

    private void filterByScientificWorkId(int id) {
        filteredWorks.clear();

        if (id == 0){
            filteredWorks.addAll(scientificWorks);
        }else {
            for (ScientificWork work : scientificWorks){
                if (work.getIdKorisnika() == id){
                    filteredWorks.add(work);
                }
            }
        }
       tableView.setItems(filteredWorks);
    }

    private void loadScientificWorks(Integer userId) throws BazaPodatakaException {

        scientificWorks.clear();
        String query;

        if (userId != null){
            query = "SELECT ID, NASLOV, ID_KORISNIKA FROM ZNANSTVENI_RADOVI WHERE ID_KORISNIKA = " + userId;

        }else {
            query = "SELECT ID, NASLOV, ID_KORISNIKA FROM ZNANSTVENI_RADOVI";
        }

        try (Connection connection = DatabaseConnection.establishConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NASLOV");
                int idKorisnika = resultSet.getInt("ID_KORISNIKA");
                ScientificWork work = new ScientificWork(id, name, idKorisnika);
                scientificWorks.add(work);
            }
        }
        catch(SQLException | IOException e){
            String errorMessage = "Error with getting user";
            logger.error(errorMessage, e);
            throw new BazaPodatakaException("Error with getting user");
        }
    }

    @FXML
    private void rowSelection() throws BazaPodatakaException{

        ScientificWork selectedWork = tableView.getSelectionModel().getSelectedItem();

        if (selectedWork != null) {
            int selectedId = selectedWork.getId();
            openEditScreen(selectedId);
        }
    }

    private void openEditScreen(int selectedId) throws BazaPodatakaException{

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/projekt_1/myWorksInput.fxml"));
            Parent editScreenParent = fxmlLoader.load();

            InputMyWorksController controller = fxmlLoader.getController();
            controller.takeData(selectedId);
            controller.menu.setVisible(false);

            if (selectedId == 0) {
                controller.setEditMode(false);
            } else {
                controller.setEditMode(true);
            }

            Stage editStage = new Stage();
            Scene editScene = new Scene(editScreenParent);
            editStage.setScene(editScene);
            editStage.showAndWait();

            if (reviewAllWorksRequested) {
                loadScientificWorks(null);
            } else {
                User loggedInUserId = userIDs.getUser(loggedInUsername);
                if (loggedInUserId != null) {
                    loadScientificWorks(loggedInUserId.getId());
                }
            }
            tableView.refresh();

            controller.menu.setVisible(true);
        } catch (IOException e) {
            logger.error("Error opening edit screen", e);
            throw new BazaPodatakaException("Error opening edit screen", e);
        }
    }

    @FXML
    private void deleteWork() {

        ScientificWork selectedWork = tableView.getSelectionModel().getSelectedItem();

        if (selectedWork != null) {

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Delete");
            confirmationAlert.setHeaderText("Are you sure you want to delete this work?");
            confirmationAlert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() != ButtonType.OK) {
                return;
            }

            int selectedId = selectedWork.getId();

            try {
                Database.deleteScientificWork(selectedId);
                tableView.getItems().remove(selectedWork);
                logger.info("Scientific work with ID " + selectedId + " deleted successfully.");
            }catch (BazaPodatakaException e) {
                String errorMessage = "Error deleting scientific work";
                logger.error(errorMessage, e);
            }
        } else {
            System.out.println("No work selected to delete.");

            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No Scientific Work Selected");
            warningAlert.setContentText("Please select a scientific work to delete.");
            warningAlert.showAndWait();
            System.out.println("No work selected to delete.");
        }
    }
}