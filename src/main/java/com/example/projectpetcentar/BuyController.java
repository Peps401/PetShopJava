package com.example.projectpetcentar;

import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import Datoteke.WritingInFile;
import entiteti.Item;
import entiteti.LogIn;
import entiteti.Transaction;
import entiteti.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class BuyController {
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
    ListView<Item> chosenItemList;
    @FXML
    TextField price;
    @FXML
    Button button;

    public void initialize(){
        //List<Item> itemList = ReadingFiles.getAllItems();
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

        ObservableList<Item> observableList = FXCollections.observableList(itemList);
        tableView.setItems(observableList);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void addInCart(){
        List<Item> chosenItemslist = tableView.getSelectionModel().getSelectedItems();

        ObservableList<Item> ob = FXCollections.observableList(chosenItemslist);
        chosenItemList.setItems(ob);

        BigDecimal b = new BigDecimal(0);
        for (Item i:
             chosenItemslist) {
            b = b.add(i.getPrice());
        }

        price.setText(String.valueOf(b));
    }

    public void buy(){
        //User user = ReadingFiles.lastLogInData().getUser();
        //LocalDateTime logInTime = ReadingFiles.lastLogInData().getTime();
        LogIn lastLogIn = DatabaseInsert.getLastLogIn();
        User lastUser = lastLogIn.getUser();

        LocalDateTime now = LocalDateTime.now();

        int minute = lastLogIn.getTime().getMinute() + lastLogIn.getTime().getHour() * 60;
        int minute2 = now.getMinute() + now.getHour() * 60;

        if (lastLogIn.getTime().toLocalDate().equals(LocalDate.now())) {
            if (minute > minute2 - 20 && minute < minute2 + 20) {

                //List<String> lines = Files.readAllLines(Path.of("dat/trackLogins.txt"));

                //int finalPosition = lines.size();
                //BigDecimal finalDiscount = new BigDecimal(lines.get(finalPosition - 1));
                BigDecimal finalDiscount = lastLogIn.getDiscount();

                if (finalDiscount.compareTo(BigDecimal.valueOf(0)) > 0) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Prilika ");
                    alert.setContentText("Zelite li iskoristiti popust od " + finalDiscount);
                    ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
                    alert.getButtonTypes().setAll(okButton, noButton);

                    alert.showAndWait().ifPresent(type -> {
                        if (type.getText() == "Yes") {
                            System.out.println("USLI");

                            DatabaseInsert.discountUsed(lastLogIn);
                            BigDecimal oldPrice = new BigDecimal(price.getText());
                            BigDecimal newPrice = oldPrice.subtract(oldPrice.multiply(finalDiscount).divide(BigDecimal.valueOf(100)));
                            //OptionalLong maxId = ReadingFiles.getAllTransactions().stream().mapToLong(transaction->transaction.getId()).max();
                            OptionalLong maxId = DatabaseRead.getAllTransactionsFromDB().stream().mapToLong(transaction -> transaction.getId()).max();
                            Transaction transaction = new Transaction(maxId.getAsLong() + 1, lastUser, chosenItemList.getItems(), newPrice);
                            //WritingInFile.writeNewTransaction(transaction);
                            DatabaseInsert.createNewTransaction(transaction);

                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Cart.fxml"));
                            Scene scene = null;
                            try {
                                scene = new Scene(fxmlLoader.load(), 700, 500);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            HelloApplication.getStage().setTitle("Vasa kosarica");
                            HelloApplication.getStage().setScene(scene);
                            HelloApplication.getStage().show();

                        }
                        if (type.getText() == "No") {
                            //OptionalLong maxId = ReadingFiles.getAllTransactions().stream().mapToLong(transaction->transaction.getId()).max();
                            OptionalLong maxId = DatabaseRead.getAllTransactionsFromDB().stream().mapToLong(transaction -> transaction.getId()).max();
                            Transaction transaction = new Transaction(maxId.getAsLong() + 1, lastUser, chosenItemList.getItems(), new BigDecimal(price.getText()));
                            //WritingInFile.writeNewTransaction(transaction);
                            DatabaseInsert.createNewTransaction(transaction);
                        }
                    });
                } else {
                    //OptionalLong maxId = ReadingFiles.getAllTransactions().stream().mapToLong(transaction->transaction.getId()).max();
                    OptionalLong maxId = DatabaseRead.getAllTransactionsFromDB().stream().mapToLong(transaction -> transaction.getId()).max();
                    Transaction transaction = new Transaction(maxId.getAsLong() + 1, lastUser, chosenItemList.getItems(), new BigDecimal(price.getText()));
                    //WritingInFile.writeNewTransaction(transaction);
                    DatabaseInsert.createNewTransaction(transaction);
                }

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setContentText("Uspjesno ste kupili proizvode");
                alert1.show();
            }
        }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Kako biste mogli dalje nastaviti kupovati potrebna je prijava");
                alert.show();

            }


    }

}
