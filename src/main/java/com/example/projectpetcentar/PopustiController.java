package com.example.projectpetcentar;

import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import entiteti.Discount;
import entiteti.Item;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.util.List;

public class PopustiController {
    @FXML
    private TableView<Item> tableView;
    @FXML
    private TableColumn<Item, Long> idColumn;
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, String> descriptionColumn;
    @FXML
    private TableColumn<Item, String> categoryColumn;
    @FXML
    private TableColumn<Item, BigDecimal> priceColumn;
    @FXML
    private Label label;

    @FXML
    public void initialize() {
        List<Item> itemList = DatabaseRead.getAllItemsFromDB();

        idColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getId());
        });
        nameColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getName());
        });
        descriptionColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getDescription());
        });
        categoryColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getCategory().getName());
        });
        priceColumn.setCellValueFactory(cell -> {
            return new SimpleObjectProperty<>(cell.getValue().getPrice());
        });

        Item item = itemList.get(0);
        List<Item> wantedList = item.distributeDiscountToItems(itemList);

        ObservableList<Item> observableList = FXCollections.observableList(wantedList);
        tableView.setItems(observableList);
    }
}
