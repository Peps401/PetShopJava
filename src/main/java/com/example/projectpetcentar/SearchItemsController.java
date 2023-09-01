package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import entiteti.Category;
import entiteti.Item;
import entiteti.Worker;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Datoteke.ReadingFiles.logger;

public class SearchItemsController {
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
    private Slider slider;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField searchedName;
    @FXML
    private TextField percentage;
    @FXML
    private Label priceLabel;

    static Item chosenItem;
    static boolean updateItemIsTriggered;

    @FXML
    public void initialize(){
        updateItemIsTriggered = false;
        List<Item> itemList = DatabaseRead.getAllItemsFromDB();

        idColumn.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getId());
        });
        nameColumn.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getName());
        });
        descriptionColumn.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getDescription());
        });
        categoryColumn.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getCategory().getName());
        });
        priceColumn.setCellValueFactory(cell-> {return new SimpleObjectProperty<>(cell.getValue().getPrice());
        });

        final BigDecimal[] searchedPrice = {BigDecimal.valueOf(slider.getValue())};
        priceLabel.setText(String.valueOf((searchedPrice[0])));

        Comparator<Item> comparator = Comparator.comparing( Item::getPrice );
        //BigDecimal maxPrice = ReadingFiles.getAllItems().stream().max(comparator).get().getPrice();
        BigDecimal maxPrice = DatabaseRead.getAllItemsFromDB().stream().max(comparator).get().getPrice();
        //BigDecimal minPrice = ReadingFiles.getAllItems().stream().min(comparator).get().getPrice();
        BigDecimal minPrice = DatabaseRead.getAllItemsFromDB().stream().min(comparator).get().getPrice();

        slider.setMax(maxPrice.doubleValue());
        slider.setMin(minPrice.doubleValue());
        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldNumber, Number newNumber) {

                searchedPrice[0] = BigDecimal.valueOf(slider.getValue());
                priceLabel.setText(String.format("%.2f",searchedPrice[0]));

            }
        });

        ObservableList<Item> observableList = FXCollections.observableList(itemList);
        tableView.setItems(observableList);

        //List<Category> cList = ReadingFiles.getAllCategories();
        List<Category> cList = DatabaseRead.getAllCategoriesFromDB();
        ObservableList<Category> ccList = FXCollections.observableList(cList);
        categoryComboBox.setItems(ccList);
    }
    @FXML
    protected void SearchButton() {
        if(percentage.getText().isEmpty()){
            percentage.setText("10");
        }
        else if (!percentage.getText().matches("[0-9]+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Objekt se ne moze se pretrazivati jer je u polje postotka uneseno slovo ");
            alert.show();
        } else {
            //List<Item> list = ReadingFiles.getAllItems();
            List<Item> list = DatabaseRead.getAllItemsFromDB();
            if (!searchedName.getText().isEmpty()) {
                list = list.stream().filter(item -> item.getName().toLowerCase().contains(searchedName.getText().toLowerCase())).collect(Collectors.toList());
            }

            if (!priceLabel.getText().equals("0.0")) {
                BigDecimal targetedPrice = BigDecimal.valueOf(slider.getValue());
                BigDecimal percent = BigDecimal.valueOf(Long.parseLong(percentage.getText()));
                percent = percent.divide(BigDecimal.valueOf(100));
                BigDecimal priceUp = targetedPrice.multiply(percent).add(targetedPrice);
                BigDecimal priceDown = targetedPrice.subtract(targetedPrice.multiply(percent));
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getPrice().compareTo(priceDown) > 0 && list.get(i).getPrice().compareTo(priceUp) < 0) {
                        System.out.println(list.get(i).getPrice());
                        System.out.println(priceDown);
                    }
                    else {
                        list.remove(i);
                    }

                }
            }

            if(categoryComboBox.getSelectionModel().getSelectedIndex() != -1){
                list = list.stream().filter(item -> item.getCategory().getName().equals(categoryComboBox.getValue().getName())).collect(Collectors.toList());
            }

            ObservableList<Item> observableList = FXCollections.observableList(list);
            tableView.setItems(observableList);
        }
    }

    @FXML
    protected void ClearButton(){
        //List<Item> list = ReadingFiles.getAllItems();
        List<Item> list = DatabaseRead.getAllItemsFromDB();
        ObservableList<Item> observableList = FXCollections.observableList(list);
        tableView.setItems(observableList);

        categoryComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    protected void DeleteButton(){
        Item i = tableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Zelite li izbrisati odabrani proizvod?");
        alert.setContentText("Jeste li sigurni?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().getText().equals("Yes")) {

            DatabaseChange.deleteItemFromStorage(i);
            DatabaseChange.deleteItemFromStore(i);
            DatabaseChange.deleteItemFromTransactions(i);
            DatabaseChange.deleteItemFromUsersTransaction(i);
            DatabaseChange.deleteItemFromDB(i);

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje proizvoda");
            alert.setHeaderText("Uspješno izbrisan objekt proizvoda!");
            alert.setContentText("Proizvod: " + i.getName() + " " + i.getPrice() + " uspjesno izbrisan!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso proizvod: " + i.getName());

            ClearButton();
        }

    }

    @FXML
    protected void updateItem() {
        chosenItem = tableView.getSelectionModel().getSelectedItem();
        updateItemIsTriggered = true;

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewItem.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 700, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HelloApplication.getStage().setTitle("Promijenite podatke o odabranom prozvodu");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
}
