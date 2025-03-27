package com.example.projekt_1.controlleri;


import com.example.projekt_1.model.ScientificWork;
import com.example.projekt_1.model.ScientificWorkChange;
import com.example.projekt_1.util.ScientificWorkChangeWriter;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChangesController {

    @FXML
    private TableView<ScientificWorkChange> tableView;
    @FXML
    private TableColumn<ScientificWorkChange, String> oldValueColumn;
    @FXML
    private TableColumn<ScientificWorkChange, String> newValueColumn;
    @FXML
    private TableColumn<ScientificWorkChange, String> dateColumn;
    @FXML
    private TableColumn<ScientificWorkChange, String> user;
    @FXML
    private TextFlow oldValueDetails;
    @FXML
    private TextFlow newValueDetails;


    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void initialize() {
        oldValueColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getOldValue() != null) {
                return new SimpleStringProperty(cellData.getValue().getOldValue().getName());
            } else {
                return new SimpleStringProperty("");
            }
        });
        newValueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNewValue().getName()));
        user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamee()));
        dateColumn.setCellValueFactory(cellData ->  {
            LocalDateTime localDateTime = cellData.getValue().getDate();
            String formattedTime = localDateTime.format(timeFormatter);
            return new SimpleStringProperty(formattedTime);
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayChangeDetails(newValue);
            } else {
                clearChangeDetails();
            }
        });

        loadChanges();
    }

    private void loadChanges() {
        ScientificWorkChangeWriter writer = new ScientificWorkChangeWriter();
        List<ScientificWorkChange> changes = writer.readAll();

        tableView.setItems(FXCollections.observableArrayList(changes));
    }

    private void displayChangeDetails(ScientificWorkChange change) {

        String oldDetails = change.getOldValue() != null ? change.getOldValue().printChange() : "";
        System.out.println(oldDetails);
        oldValueDetails.getChildren().clear();
        oldValueDetails.getChildren().add(new Text(oldDetails));

        String newDetails = change.getNewValue() != null ? change.getNewValue().printChange() : "";
        System.out.println(newDetails);
        newValueDetails.getChildren().clear();
        newValueDetails.getChildren().add(new Text(newDetails));
    }

    private void clearChangeDetails() {
        oldValueDetails.getChildren().clear();
        newValueDetails.getChildren().clear();
    }
}
