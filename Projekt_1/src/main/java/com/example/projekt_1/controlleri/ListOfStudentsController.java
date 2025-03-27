package com.example.projekt_1.controlleri;

import com.example.projekt_1.model.Applicants;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListOfStudentsController {

    @FXML
    private TableView<Applicants> tableViewForStudents;
    @FXML
    private TableColumn<Applicants, String> studentNames;

    public void initialize() {
        studentNames.setCellValueFactory(cellData -> {
            String name = cellData.getValue().name();
            return new SimpleStringProperty(name);
        });
    }

    public void setData(ObservableList<Applicants> applicants) {
        tableViewForStudents.setItems(applicants);

    }
}
