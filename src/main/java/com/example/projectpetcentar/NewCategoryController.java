package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import Datoteke.WritingInFile;
import entiteti.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.OptionalLong;

public class NewCategoryController {

    @FXML
    TextField name;
    @FXML
    TextArea des;

    public void initialize(){
        if(SearchCategoriesController.updateCategoryIsTriggered) updateIsTriggered();
    }

    @FXML
    protected void newCategory(){
        if(!SearchCategoriesController.updateCategoryIsTriggered) {
            if (!name.getText().isEmpty() && !des.getText().isEmpty()) {
                //OptionalLong maxId = ReadingFiles.getAllCategories().stream().mapToLong(category -> category.getId()).max();
                OptionalLong maxId = DatabaseRead.getAllCategoriesFromDB().stream().mapToLong(category -> category.getId()).max();
                Category category = new Category(maxId.getAsLong() + 1, name.getText(), des.getText());
                //WritingInFile.writeNewCategory(category);
                DatabaseInsert.createNewCategory(category);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Objekt kategorije uspjesno se stvorio");
                alert.setContentText(category.getName());
                alert.show();

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchCategories.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 650, 650);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.getStage().setTitle("Pretraga kategorija");
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Neuspjelo spremanje");
                alert.setHeaderText("Objekt kategorije neuspjesno se stvorio");
                alert.show();
            }
        }else {
            Category newCat = new Category(SearchCategoriesController.chosenCategory.getId(), name.getText(),des.getText());
            DatabaseChange.updateCategoryFromDB(newCat);

            String oldCategoryString = SearchCategoriesController.chosenCategory.getName() + "\n" +
                    SearchCategoriesController.chosenCategory.getDescription();
            String newCategoryString = newCat.getName() + "\n" +
                    newCat.getDescription();

            User user = DatabaseInsert.getLastLogIn().getUser();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            String timeString = LocalDateTime.now().format(formatter);

            ChangesLog.write(oldCategoryString, newCategoryString, user.getName(), timeString);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Objekt kategorija uspjesno se promijenio");
            alert.show();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchCategories.fxml"));

            try {
                Scene scene = new Scene(fxmlLoader.load(), 700, 500);
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().setTitle("Pretraga kategorija");
                HelloApplication.getStage().show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void updateIsTriggered(){
        Category c = SearchCategoriesController.chosenCategory;

        name.setText(c.getName());
        des.setText(c.getDescription());

    }
}
