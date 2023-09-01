package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseRead;
import entiteti.*;
import entiteti.Storage;
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
import java.util.stream.Collectors;

import static Datoteke.ReadingFiles.logger;

public class SearchStoreController {

    static Store chosenStore;
    static boolean updateStoreIsTriggered;

    @FXML
    private TableView<Store> storeTableView;
    @FXML
    private TableColumn<Store, String> name;
    @FXML
    private TableColumn<Store, String> city;
    @FXML
    private TableColumn<Store, String> address;
    @FXML
    private TableColumn<Store, String> workers;
    @FXML
    private TableColumn<Store, String> boss;
    @FXML
    private TableColumn<Store, String> items;
    @FXML
    private TextField searchedName;
    @FXML
    private ComboBox<City> cityComboBox;
    @FXML
    private ComboBox<Boss> bossComboBox;


    @FXML
    public void initialize(){
        /*List<Store> list = ReadingFiles.getAllStores();
        List<City> cities = ReadingFiles.getAllCities();
        List<Boss> bosses = ReadingFiles.getAllBosses();*/

        List<Store> list = DatabaseRead.getAllStoresFromDB();
        List<City> cities = DatabaseRead.getCitiesFromDB();
        List<Boss> bosses = DatabaseRead.getBossesFromDB();

        name.setCellValueFactory(cell ->{return new SimpleObjectProperty(cell.getValue().getName());
        });
        city.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getCity().getName());
        });
        address.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getAddress());
        });
        workers.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getWorkers().toString() + "\n");
        });
        boss.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getBoss().getName());
        });
        items.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getItems());
        });

        ObservableList<Store> observableList = FXCollections.observableList(list);
        storeTableView.setItems(observableList);

        ObservableList<City> observableListC = FXCollections.observableList(cities);
        cityComboBox.setItems(observableListC);

        ObservableList<Boss> observableListB = FXCollections.observableList(bosses);
        bossComboBox.setItems(observableListB);
    }

    @FXML
    protected void SearchButton(){
        //List<Store> list = ReadingFiles.getAllStores();
        List<Store> list = DatabaseRead.getAllStoresFromDB();
        if(!searchedName.getText().isEmpty()){
            list = list.stream().filter(store -> store.getName().toLowerCase().contains(searchedName.getText().toLowerCase())).collect(Collectors.toList());
        }
        if(!(cityComboBox.getSelectionModel().getSelectedIndex() == -1)){
            list = list.stream().filter(store -> store.getCity().getName().equals(cityComboBox.getValue().getName())).collect(Collectors.toList());
        }

        if(!(bossComboBox.getSelectionModel().getSelectedIndex() ==-1)){
            Boss b = bossComboBox.getValue();
            list = list.stream().filter(store -> store.getBoss().getId().equals(b.getId())).collect(Collectors.toList());
        }

        ObservableList<Store> observableList = FXCollections.observableList(list);
        storeTableView.setItems(observableList);
    }

    @FXML
        protected void ClearButton(){
            //List<Store> list = ReadingFiles.getAllStores();
            List<Store> list = DatabaseRead.getAllStoresFromDB();
            ObservableList<Store> observableList = FXCollections.observableList(list);
            storeTableView.setItems(observableList);

            cityComboBox.getSelectionModel().clearSelection();
            bossComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    protected void deleteStore(){
        Store store = storeTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Zelite li izbrisati odabrani ducan?");
        alert.setContentText("Jeste li sigurni?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().getText().equals("Yes")) {

            for (int i = 0; i < store.getWorkers().size(); i++) {
                DatabaseChange.deleteWorkersFromStore(store);
            }

            for (int i = 0; i < store.getItems().size(); i++) {
                DatabaseChange.deleteItemsFromStoreItem(store.getItems().get(i).getId());
            }
            DatabaseChange.deleteStoreFromDB(store);

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje ducana");
            alert.setHeaderText("Uspjesno izbrisan objekt ducana!");
            alert.setContentText("Ducan: " + store.getName() +  " uspjesno izbrisana!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso ducan: " + store.getName());

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStores.fxml"));
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
    protected void updateStore(){

        chosenStore = storeTableView.getSelectionModel().getSelectedItem();
        updateStoreIsTriggered = true;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewStore.fxml"));
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
