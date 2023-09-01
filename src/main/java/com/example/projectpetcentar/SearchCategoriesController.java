package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import entiteti.Boss;
import entiteti.Category;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static Datoteke.ReadingFiles.logger;

public class SearchCategoriesController {
    @FXML
    TableView<Category> categoryTableColumn;
    @FXML
    TableColumn<Category, Long> id;
    @FXML
    TableColumn<Category, String> name;
    @FXML
    TableColumn<Category, String> description;

    static Category chosenCategory;
    static boolean updateCategoryIsTriggered;

    public void initialize(){
        id.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getId());
        });
        name.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getName());
        });
        description.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getDescription());
        });

        //List<Category> cList = ReadingFiles.getAllCategories();
        List<Category> cList = DatabaseRead.getAllCategoriesFromDB();
        ObservableList<Category> ccList = FXCollections.observableList(cList);
        categoryTableColumn.setItems(ccList);

        updateCategoryIsTriggered = false;
    }
    @FXML
    protected void updateCategory() {

        chosenCategory = categoryTableColumn.getSelectionModel().getSelectedItem();
        updateCategoryIsTriggered = true;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewCategory.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 650, 650);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HelloApplication.getStage().setTitle("Promijenite podatke o odabranoj kategoriji");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    @FXML
    protected void deleteCategory() {

        Category category = categoryTableColumn.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Zelite li izbrisati odabranu kategoriju?");
        alert.setContentText("Jeste li sigurni?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().getText().equals("Yes")) {

            DatabaseChange.deleteCategoryFromDB(category);

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje kategorije");
            alert.setHeaderText("Uspjesno izbrisan objekt kategorije!");
            alert.setContentText("Kategorija: " + category.getName() +  " uspjesno izbrisana!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso kategoriju: " + category.getName());

            List<Category> cList = DatabaseRead.getAllCategoriesFromDB();
            ObservableList<Category> ccList = FXCollections.observableList(cList);
            categoryTableColumn.setItems(ccList);

        }
    }
}
