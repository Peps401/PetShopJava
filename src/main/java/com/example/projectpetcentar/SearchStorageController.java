package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseRead;
import entiteti.*;
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

public class SearchStorageController {
    @FXML
    TableView<Storage> storageTableView;
    @FXML
    TableColumn<Storage, Long> idColumn;
    @FXML
    TableColumn<Storage, String> nameColumn;
    @FXML
    TableColumn<Storage, List<Item>> itemColumn;
    @FXML
    TableColumn<Storage, List<Shelf>> shelfColumn;

    static Storage chosenStorage;
    static boolean updateStorageIsTriggered;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getId());
        });
        nameColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getName());
        });
        itemColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getItems());
        });
        shelfColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getBoxesOf());
        });

        ObservableList<Storage> observableList = FXCollections.observableList(DatabaseRead.getAllStoragesFromDB());
        storageTableView.setItems(observableList);
        updateStorageIsTriggered = false;
    }

    @FXML
    protected void deleteStorage(){
        Storage storage = storageTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Zelite li izbrisati odabrano skladiste?");
        alert.setContentText("Jeste li sigurni?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().getText().equals("Yes")) {

            DatabaseChange.deleteWorkersFromStorage(storage);
            DatabaseChange.deleteItemsFromStorageItem(storage);
            DatabaseChange.deleteShelvesFromStorage(storage);
            DatabaseChange.deleteStorageFromDB(storage);

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje skladista");
            alert.setHeaderText("Uspjesno izbrisan objekt skladista!");
            alert.setContentText("Skladiste: " + storage.getName() +  " uspjesno izbrisana!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso skladiste: " + storage.getName());

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStorage.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(),700, 500);
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    protected void updateStorage(){

        chosenStorage = storageTableView.getSelectionModel().getSelectedItem();
        updateStorageIsTriggered = true;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewStorage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 700, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HelloApplication.getStage().setTitle("Promijenite podatke o odabranom skladistu");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
}
