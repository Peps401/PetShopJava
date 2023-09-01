module com.example.projectpetcentar {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;


    opens com.example.projectpetcentar to javafx.fxml;
    exports com.example.projectpetcentar;
}