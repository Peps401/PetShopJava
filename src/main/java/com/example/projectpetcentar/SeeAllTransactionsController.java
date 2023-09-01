package com.example.projectpetcentar;

import Datoteke.ReadingFiles;
import entiteti.Item;
import entiteti.Transaction;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeeAllTransactionsController {
    @FXML
    Label nameLabel;
    @FXML
    Label passLabel;
    @FXML
    Label adminSeen;
    @FXML
    Label adminSeen2;
    @FXML
    TextField mostWanted;
    @FXML
    TextField leastWanted;
    @FXML
    TextField name;
    @FXML
    PasswordField pass;
    @FXML
    Button button;

    @FXML
    TableView<Transaction> transactionTableView;
    @FXML
    TableColumn<Transaction, String> userStringTableColumn;
    @FXML
    TableColumn<Transaction, List<Item>> itemsTableColumn;
    @FXML
    TableColumn<Transaction, BigDecimal> priceTableColumn;

    private static final Logger logger = LoggerFactory.getLogger(ReadingFiles.class);

    @FXML
    public void initialize(){
        transactionTableView.setVisible(false);

        userStringTableColumn.setCellValueFactory(cell->{ return new SimpleObjectProperty<>(cell.getValue().getConsumer().getName());
        });
        itemsTableColumn.setCellValueFactory(cell->{ return new SimpleObjectProperty<>(cell.getValue().getItemList());
        });
        priceTableColumn.setCellValueFactory(cell->{ return new SimpleObjectProperty<>(cell.getValue().getPrice());
        });

        ObservableList<Transaction> observableList = FXCollections.observableList(ReadingFiles.getAllTransactions());
        transactionTableView.setItems(observableList);

        adminSeen.setVisible(false);
        adminSeen2.setVisible(false);
        mostWanted.setVisible(false);
        leastWanted.setVisible(false);
    }

    @FXML
    protected void logInAsAdmin(){
        if(name.getText().equals("admin") && pass.getText().equals("admin")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Uspjesna prijava");
            alert.setHeaderText("Uspjesno ste se prijavili kao Admin");
            alert.setContentText("Kao Admin imate pravo na uvid svih transakcija");
            alert.show();

            logger.info("Uspjesna prijava admina");

            nameLabel.setVisible(false);
            passLabel.setVisible(false);
            name.setVisible(false);
            pass.setVisible(false);
            button.setVisible(false);

            transactionTableView.setVisible(true);
            adminSeen.setVisible(true);
            adminSeen2.setVisible(true);
            mostWanted.setVisible(true);
            leastWanted.setVisible(true);

            List<Transaction> transactions = ReadingFiles.getAllTransactions();
            Map<Item, Integer> map = new HashMap<>();
            int min = 100;
            int max = 0;

            Item mostWantedItem = new Item(0L, "PROBA", "PROBA", ReadingFiles.getCategoryFromId(1L), new BigDecimal(0));
            Item leastWantedItem = new Item(0L, "PROBA", "PROBA", ReadingFiles.getCategoryFromId(1L), new BigDecimal(0));

            for (int i = 0; i < transactions.size(); i++) {
                for (Item item:
                     transactions.get(i).getItemList()) {
                    if(map.containsKey(item)){
                        Integer no = map.get(item) + 1;
                        map.put(item, no);
                        if(no > max){
                            max = no;
                            mostWantedItem = item;
                        }

                    }
                    else map.put(item, 1);
                }
            }

            for (Item item:
                    map.keySet()) {
                if(map.get(item)<min){
                    min = map.get(item);
                    leastWantedItem = item;
                }
            }

            mostWanted.setText(String.valueOf(mostWantedItem));
            leastWanted.setText(String.valueOf(leastWantedItem));

        }
    }
}
