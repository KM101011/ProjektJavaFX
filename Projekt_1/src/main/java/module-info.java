module com.example.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;

    opens com.example.projekt_1 to javafx.fxml;
    opens com.example.projekt_1.model to javafx.base;
    //exports com.example.projekt_1;
    exports com.example.projekt_1.controlleri;
    opens com.example.projekt_1.controlleri to javafx.fxml;
}