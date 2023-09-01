package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import Datoteke.WritingInFile;
import Exceptions.RequiredNoNotString;
import entiteti.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.OptionalLong;

import static Datoteke.ReadingFiles.logger;

public class NewItemController {
    @FXML
    TextField name;
    @FXML
    ComboBox<Category> categoryComboBox;
    @FXML
    TextArea textArea;
    @FXML
    TextField price;

    public void initialize(){
        ObservableList<Category> observableList = FXCollections.observableList(DatabaseRead.getAllCategoriesFromDB());
        categoryComboBox.setItems(observableList);
        categoryComboBox.getSelectionModel().select(0);

        if(SearchItemsController.updateItemIsTriggered) updateIsTriggered();

        Dog dog = new Dog.Builder().withName("Octavio").build();
        System.out.println("In Newitem" + dog);
    }
    @FXML
    protected void createNewItem(){
        if(!SearchItemsController.updateItemIsTriggered) {
            String itemName = name.getText();
            Category category = categoryComboBox.getValue();
            String description = textArea.getText();

            //boolean isNumeric = price.getText().chars().allMatch( Character::isDigit);
            boolean isNumeric = price.getText().matches("[-+]?[0-9]*\\.?[0-9]+");
            BigDecimal bigDecimal = BigDecimal.valueOf(0);

            // Check if int
            /*try {
                bigDecimal = new BigDecimal(Integer.parseInt(price.getText()));
            } catch(NumberFormatException e) {
                // Not int
            }

            // Check if float
            try {
                bigDecimal = new BigDecimal(Float.parseFloat(price.getText()));
            } catch(NumberFormatException e) {
                // Not float
            }

            System.out.println(bigDecimal);*/

            try {
                OptionalLong maxId = ReadingFiles.getAllItems().stream().mapToLong(item -> item.getId()).max();

                if (!isNumeric) throw new RequiredNoNotString();


                System.out.println(bigDecimal);

                BigDecimal bgPrice = new BigDecimal(price.getText());

                Item item = new Item(0L, name.getText(), description, category, bgPrice);
                //WritingInFile.writeNewItem(item);
                DatabaseInsert.createNewItem(item);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Uspjesno stvaranje");
                alert.setHeaderText("Uspjesno stvaranje objekta proizvoda");
                alert.setContentText(String.valueOf(item));
                alert.show();
                logger.info("Uspjesno stvaranje proizvoda");

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchItems.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 700, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.getStage().setTitle("Pretraga proizvoda");
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().show();

            } catch (RequiredNoNotString ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogreska");
                alert.setHeaderText("Neuspjelo stvaranje objekta proizvoda");
                alert.setContentText("U polje cijena je potrebno upisati broj");
                alert.show();
                logger.info("Neuspjesno stvaranje proizvoda");

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewItem.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 700, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.getStage().setTitle("Stvaranje novog proizvoda");
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().show();
            }
        }else {
            Item newI = new Item(SearchItemsController.chosenItem.getId(), name.getText(), textArea.getText(), categoryComboBox.getValue(), new BigDecimal(price.getText()));
            System.out.println(name.getText());
            DatabaseChange.updateItemFromDB(newI);

            String oldItemString = SearchItemsController.chosenItem.getName() + "\n" +
                    SearchItemsController.chosenItem.getDescription() + "\n" + SearchItemsController.chosenItem.getCategory().getName()+ "\n" + SearchItemsController.chosenItem.getPrice();
            String newItemString = newI.getName() + "\n" +
                    newI.getDescription() + "\n" + newI.getCategory().getName()+ "\n" + newI.getPrice();

            User user = DatabaseInsert.getLastLogIn().getUser();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            String timeString = LocalDateTime.now().format(formatter);

            ChangesLog.write(oldItemString, newItemString, user.getName(), timeString);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Objekt proizvoda uspjesno se promijenio");
            alert.show();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchItems.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.getStage().setTitle("Pretraga proizvoda");
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        }
    }

    public void updateIsTriggered(){
        Item i = SearchItemsController.chosenItem;

        name.setText(i.getName());
        categoryComboBox.setValue(i.getCategory());
        textArea.setText(i.getDescription());
        price.setText(String.valueOf(i.getPrice()));

    }
}
