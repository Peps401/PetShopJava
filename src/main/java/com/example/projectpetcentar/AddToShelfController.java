package com.example.projectpetcentar;

import Database.DatabaseInsert;
import Database.DatabaseRead;
import entiteti.Category;
import entiteti.Item;
import entiteti.Shelf;
import entiteti.Storage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddToShelfController {
    @FXML
    ComboBox<Storage> storageComboBox;
    @FXML
    ComboBox<Item> itemsComboBox;
    @FXML
    ComboBox<Category> categoryComboBox;
    @FXML
    ListView<Item> listView;

    private List<Item> chosenItems = new ArrayList<>(0);
    public void initialize(){
        chosenItems.clear();
        ObservableList<Storage> observableList = FXCollections.observableList(DatabaseRead.getAllStoragesFromDB());
        storageComboBox.setItems(observableList);

        ObservableList<Category> observableListC = FXCollections.observableList(DatabaseRead.getAllCategoriesFromDB());
        categoryComboBox.setItems(observableListC);

    }

    public void choseItems(){
        List<Item> list = DatabaseRead.getAllItemsFromDB();
        List<Item> l = new ArrayList<>(0);
        for (Item i:
             list) {
            if(i.getCategory().getId().equals(categoryComboBox.getValue().getId())){
                l.add(i);
            }
        }
        ObservableList<Item> observableListI = FXCollections.observableList(l);
        itemsComboBox.setItems(observableListI);
    }

    public void addProduct(){
        chosenItems.add(itemsComboBox.getValue());

        ObservableList<Item> observableListI = FXCollections.observableList(chosenItems);
        listView.setItems(observableListI);
        listView.refresh();
    }

    public void putOnShelves(){
        Storage storage = storageComboBox.getValue();
        List<Shelf> listOfShelves = storage.getBoxesOf();

        Item item = itemsComboBox.getValue();

        Category category = item.getCategory();

        Shelf<Item> addToShelf = new Shelf<>(chosenItems);
        listOfShelves.add(addToShelf);


        for (int i = 0; i < storage.getBoxesOf().size(); i++) {
            if(storage.getBoxesOf().get(i).getBoxesOf().size() == 0) {
                for (int j = 0; j < chosenItems.size(); j++) {
                    DatabaseInsert.addItemsOnShelfInStorage(storage, chosenItems.get(j), i);
                }
            }
        }


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda");
        alert.setHeaderText("Uspjesno ste dodali proizvode na policu");
        alert.setContentText(chosenItems.toString());
        alert.show();
    }
}
