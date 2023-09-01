package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class NewStoreController {
    @FXML
    private TextField name;
    @FXML
    private ComboBox<City> cityComboBox;
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
    @FXML
    private ComboBox<Boss> bossComboBox;

    private List<Worker> chosenWorkers = new ArrayList<>(0);
    private  List<Item> chosenItems = new ArrayList<>(0);

    @FXML
    public void initialize(){
        /*List<City> cities = ReadingFiles.getAllCities();
        List<Address> adresses = ReadingFiles.getAllAddresses();
        List<Worker> workers = ReadingFiles.getAllWorkers();
        List<Item> items = ReadingFiles.getAllItems();
        List<Boss> bosses = ReadingFiles.getAllBosses();*/

        if(SearchStoreController.updateStoreIsTriggered) updateIsTriggered();

        List<City> cities = DatabaseRead.getCitiesFromDB();
        List<Worker> workers = DatabaseRead.getAllWorkersFromDB();
        List<Item> items = DatabaseRead.getAllItemsFromDB();
        List<Boss> bosses = DatabaseRead.getBossesFromDB();


        ObservableList<City> cityObservableList = FXCollections.observableList(cities);
        cityComboBox.setItems(cityObservableList);

        ObservableList<Worker> workerObservableList = FXCollections.observableList(workers);
        workersComboBox.setItems(workerObservableList);

        ObservableList<Item> itemObservableList = FXCollections.observableList(items);
        itemsComboBox.setItems(itemObservableList);

        ObservableList<Boss> bossObservableList = FXCollections.observableList(bosses);
        bossComboBox.setItems(bossObservableList);
    }

    @FXML
    protected void addAddresses(){
        List<Address> addresses = DatabaseRead.getAddressesForSpecificCity(cityComboBox.getValue().getId());

        ObservableList<Address> observableList = FXCollections.observableList(addresses);
        adressComboBox.setItems(observableList);
    }

    @FXML
    protected void addWorker() {
        if(!chosenWorkers.contains(workersComboBox.getValue()) & workersComboBox.getSelectionModel().getSelectedIndex() != -1) {
            chosenWorkers.add(workersComboBox.getValue());
            ObservableList<Worker> oc = FXCollections.observableList(chosenWorkers);
            workerListView.setItems(oc);
            chosenworkersComboBox.setItems(oc);
        }
        else if(workersComboBox.getSelectionModel().getSelectedIndex() == -1){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Niste odabrali niti jednog radnika");
            alert.show();
        }
        else {
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

        ObservableList<Worker> oc = FXCollections.observableList(chosenWorkers);
        chosenworkersComboBox.setItems(oc);
        workerListView.refresh();
    }
    @FXML
    protected void addItem(){

        if(!chosenItems.contains(itemsComboBox.getValue()) & itemsComboBox.getSelectionModel().getSelectedIndex() != -1) {
            chosenItems.add(itemsComboBox.getValue());
            ObservableList<Item> oi = FXCollections.observableList(chosenItems);
            itemListView.setItems(oi);
            chosenitemsComboBox.setItems(oi);
        }
        else if(itemsComboBox.getSelectionModel().getSelectedIndex() == -1){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Niste odabrali niti jedan proizvod");
            alert.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Objekt proizvoda koji pokusavate dodati vec postoji");
            alert.show();
        }
    }
    @FXML
    protected void removeItem(){
        Item i = chosenitemsComboBox.getValue();
        chosenItems.remove(i);

        ObservableList<Item> observableList = FXCollections.observableList(chosenItems);
        chosenitemsComboBox.setItems(observableList);
        itemListView.refresh();

    }

    public boolean checkIfAddressisInChosenCity(){
        if(adressComboBox.getValue().getCity().getId().equals(cityComboBox.getValue().getId())){
            return true;
        }
        else return false;
    }

    @FXML
    protected void addNewStore(){
        boolean addressInCity = checkIfAddressisInChosenCity();
        if(!SearchStoreController.updateStoreIsTriggered) {
            if (chosenWorkers.isEmpty() || chosenItems.isEmpty() || name.getText().isEmpty() || cityComboBox.getSelectionModel().getSelectedIndex() == -1
                    || adressComboBox.getSelectionModel().getSelectedIndex() == -1 || bossComboBox.getSelectionModel().getSelectedIndex() == -1
                    || !addressInCity) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText("Objekt ducana ne moze se stvoriti jer su vazna polja ostala nepopunjena ili se adresa ne nalazi u tom gradu");
                alert.show();
            } else {
                OptionalLong id = DatabaseRead.getAllStoresFromDB().stream().mapToLong(store -> store.getId()).max();
                Store store = new Store(id.getAsLong() + 1, name.getText(),cityComboBox.getValue(), adressComboBox.getValue(), chosenWorkers, chosenItems, bossComboBox.getValue());
                //WritingInFile.writeNewStore(store);

                DatabaseInsert.createNewStore(store);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Objekt ducana uspjesno se stvorio");
                alert.show();

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStores.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 700, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.getStage().setTitle("Pretraga Ducana");
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().show();
            }
        }else {
            Store oldStore = SearchStoreController.chosenStore;
            OptionalLong id = DatabaseRead.getAllStoresFromDB().stream().mapToLong(store -> store.getId()).max();
            Store store = new Store(oldStore.getId(), name.getText(),cityComboBox.getValue(), adressComboBox.getValue(), chosenWorkers, chosenItems, bossComboBox.getValue());
            DatabaseChange.updateStoreFromDB(store);

            String oldStoreString = SearchStoreController.chosenStore.getName() + "\n" + SearchStoreController.chosenStore.getAddress().getCity().getName()
                    + "\n" + SearchStoreController.chosenStore.getAddress().getCity().getCountry() + "\n" + SearchStoreController.chosenStore.getAddress().getCity().getPostalCode()
                    + "\n" + adressComboBox.getValue().getCity()  + "\n" + adressComboBox.getValue().getName() + "\n"
                    + adressComboBox.getValue().getStreetNo() + "\n" + "PROIZVODI; ";

            for (int i = 0; i < chosenItems.size(); i++) {
                oldStoreString = chosenItems.get(i).getName() + " ";
                DatabaseInsert.insertNewItemIntoStore(store.getId(), chosenItems.get(i).getId());
            }
            oldStoreString = oldStoreString + "\n" + "RADNICI: ";
            for (int i = 0; i < chosenWorkers.size(); i++) {
                oldStoreString = chosenWorkers.get(i).getName() + " ";
                DatabaseInsert.insertNewWorkerIntoStore(store.getId(), chosenWorkers.get(i).getId());
            }
            oldStoreString = oldStore + "\n" + bossComboBox.getValue().getName() + "\n" + bossComboBox.getValue().getSurname();

            String newStoreString = store.getName() + "\n" + store.getAddress().getCity().getName()
                    + "\n" + store.getAddress().getCity().getCountry() + "\n" + store.getAddress().getCity().getPostalCode()
                    + "\n" + adressComboBox.getValue().getCity()  + "\n" + adressComboBox.getValue().getName() + "\n"
                    + adressComboBox.getValue().getStreetNo() + "\n" + "PROIZVODI; ";;
            for (int i = 0; i < chosenItems.size(); i++) {
                newStoreString = chosenItems.get(i).getName() + " ";
            }
            newStoreString = newStoreString + "\n" + "RADNICI: ";
            for (int i = 0; i < chosenWorkers.size(); i++) {
                oldStoreString = chosenWorkers.get(i).getName() + " ";
            }
            newStoreString = newStoreString + "\n" + adressComboBox.getValue().getCity()  + "\n" + adressComboBox.getValue().getName() + "\n" + adressComboBox.getValue().getStreetNo();

            User user = DatabaseInsert.getLastLogIn().getUser();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            String timeString = LocalDateTime.now().format(formatter);

            ChangesLog.write(oldStoreString, newStoreString, user.getName(), timeString);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Objekt ducana uspjesno se promijenio");
            alert.show();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStores.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.getStage().setTitle("Pretraga ducana");
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        }
    }

    public void updateIsTriggered(){
        Store s = SearchStoreController.chosenStore;

        name.setText(s.getName());
        adressComboBox.getSelectionModel().select(s.getAddress());
        cityComboBox.getSelectionModel().select(s.getCity());
        bossComboBox.getSelectionModel().select(s.getBoss());

        for (int i = 0; i < s.getItems().size(); i++) {
            DatabaseChange.deleteItemFromStore(s.getItems().get(i));
        }

        DatabaseChange.deleteWorkersFromStore(s);

    }

}
