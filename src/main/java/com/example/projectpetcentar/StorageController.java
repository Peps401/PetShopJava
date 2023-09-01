package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseInsert;
import Database.DatabaseRead;
import entiteti.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import entiteti.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class StorageController {
    @FXML
    private TextField name;
    @FXML
    private ComboBox<Address> adressComboBox;
    @FXML
    private ComboBox<Worker> workersComboBox;
    @FXML
    private ComboBox<Worker> chosenworkersComboBox;
    @FXML
    private ComboBox<Item> itemsComboBox;
    @FXML
    private ComboBox<Item> chosenitemsComboBox;
    @FXML
    private ListView<Worker> workerListView;
    @FXML
    private ListView<Item> itemListView;


    private List<Worker> chosenWorkers = new ArrayList<>(0);
    private  List<Item> chosenItems = new ArrayList<>(0);

    static String oldIStorageString;

    @FXML
    public void initialize(){
        /*List<Address> adresses = ReadingFiles.getAllAddresses();
        List<Worker> workers = ReadingFiles.getAllWorkers();
        List<Item> items = ReadingFiles.getAllItems();*/

        List<Address> adresses = DatabaseRead.getAddressesFromDB();
        List<Worker> workers = DatabaseRead.getAllWorkersFromDB();
        List<Item> items = DatabaseRead.getAllItemsFromDB();

        ObservableList<Address> adressObservableList = FXCollections.observableList(adresses);
        adressComboBox.setItems(adressObservableList);

        ObservableList<Worker> workerObservableList = FXCollections.observableList(workers);
        workersComboBox.setItems(workerObservableList);

        ObservableList<Item> itemObservableList = FXCollections.observableList(items);
        itemsComboBox.setItems(itemObservableList);

        if(SearchStorageController.updateStorageIsTriggered) updateIsTriggered();
    }

    @FXML
    protected void addWorker() {
        if(!chosenWorkers.contains(workersComboBox.getValue())) {
            chosenWorkers.add(workersComboBox.getValue());
            ObservableList<Worker> oc = FXCollections.observableList(chosenWorkers);
            workerListView.setItems(oc);
            chosenworkersComboBox.setItems(oc);
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Objekt radnika koji pokusavate dodati vec postoji");
            alert.show();
        }
    }
    @FXML
    protected void removeWorker(){
        Worker w = chosenworkersComboBox.getValue();
        chosenWorkers.remove(w);
        workerListView.refresh();
    }
    @FXML
    protected void addItem(){
        chosenItems.add(itemsComboBox.getValue());
        ObservableList<Item> oi = FXCollections.observableList(chosenItems);
        itemListView.setItems(oi);
        chosenitemsComboBox.setItems(oi);
    }
    @FXML
    protected void removeItem(){
        Item i = chosenitemsComboBox.getValue();
        chosenItems.remove(i);
        itemListView.refresh();
    }

    @FXML
    protected void addNewStorage(){
        if(!SearchStorageController.updateStorageIsTriggered) {
            if (chosenWorkers.isEmpty() || chosenItems.isEmpty() || name.getText().isEmpty()
                    || adressComboBox.getSelectionModel().getSelectedIndex() == -1) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText("Objekt skladista ne moze se stvoriti jer su vazna polja ostala nepopunjena");
                alert.show();
            } else {
                //OptionalLong id = ReadingFiles.getAllStorages().stream().mapToLong(storage -> storage.getId()).max();
                OptionalLong id = DatabaseRead.getAllStoragesFromDB().stream().mapToLong(storage -> storage.getId()).max();
                Storage storage = new Storage(id.getAsLong() + 1, name.getText(), chosenItems, chosenWorkers, adressComboBox.getValue(), new ArrayList<>(0));
                //WritingInFile.writeNewStorage(storage);
                DatabaseInsert.createNewStorage(storage);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Objekt skladista uspjesno se stvorio");
                alert.show();
            }
        }
        else{
            Storage oldStorage = SearchStorageController.chosenStorage;
            Storage storage = new Storage(SearchStorageController.chosenStorage.getId(), name.getText(), chosenItems, chosenWorkers, adressComboBox.getValue(), SearchStorageController.chosenStorage.getBoxesOf());
            DatabaseChange.updateStorageFromDB(storage);

            String newStorageString = storage.getName() + "\n" + "PROIZVODI; ";
            for (int i = 0; i < chosenItems.size(); i++) {
                newStorageString = newStorageString + chosenItems.get(i).getName() + " ";
                DatabaseInsert.insertNewItemIntoStorage(oldStorage.getId(), storage.getItems().get(i).getId());
            }
            newStorageString = newStorageString + "\n" + "RADNICI: ";
            for (int i = 0; i < chosenWorkers.size(); i++) {
                newStorageString = newStorageString + chosenWorkers.get(i).getName() + " ";
                DatabaseInsert.insertNewWorkerIntoStorage(oldStorage.getId(), storage.getWorkers().get(i).getId());
            }
            newStorageString = newStorageString + "\n" + adressComboBox.getValue().getCity()  + "\n" + adressComboBox.getValue().getName() + "\n" + adressComboBox.getValue().getStreetNo();

            User user = DatabaseInsert.getLastLogIn().getUser();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            String timeString = LocalDateTime.now().format(formatter);

            ChangesLog.write(oldIStorageString, newStorageString, user.getName(), timeString);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Objekt proizvoda uspjesno se promijenio");
            alert.show();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStorage.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.getStage().setTitle("Pretraga skladista");
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        }
    }

    public void updateIsTriggered(){
        Storage oldStorage = SearchStorageController.chosenStorage;

        name.setText(oldStorage.getName());
        adressComboBox.getSelectionModel().select(oldStorage.getAddress());

        oldIStorageString = "";
        oldIStorageString = SearchStorageController.chosenStorage.getName() + "\n" + "PROIZVODI; " + "\n" ;
        for (int i = 0; i < oldStorage.getItems().size(); i++) {
            oldIStorageString = oldIStorageString + oldStorage.getItems().get(i).getName() + " ";
        }
        oldIStorageString = oldIStorageString + "\n" + "RADNICI: " + "\n" ;
        for (int i = 0; i < oldStorage.getWorkers().size(); i++) {
            oldIStorageString = oldIStorageString + oldStorage.getWorkers().get(i).getName() + " ";
        }
        oldIStorageString = oldIStorageString + "\n" + adressComboBox.getValue().getCity()  + "\n" + adressComboBox.getValue().getName() + "\n" + adressComboBox.getValue().getStreetNo();

        for (int i = 0; i < oldStorage.getItems().size(); i++) {
            DatabaseChange.deleteItemFromStorage(oldStorage.getItems().get(i));
        }

        DatabaseChange.deleteWorkersFromStorage(oldStorage);

    }
}
