package com.example.projectpetcentar;

import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import entiteti.Item;
import entiteti.LogIn;
import entiteti.Transaction;
import entiteti.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


public class CartController {
    @FXML
    Label userName;

    @FXML
    ListView<Transaction> transactionListView;

    public void initialize(){
        LogIn lastlogIn = DatabaseInsert.getLastLogIn();
        User user = lastlogIn.getUser();
        //LocalDateTime logInTime = ReadingFiles.lastLogInData().getTime();

        LocalDateTime now = LocalDateTime.now();

        int minute = lastlogIn.getTime().getMinute() + lastlogIn.getTime().getHour() * 60;
        int minute2 = now.getMinute() + now.getHour() * 60;

        List<Transaction> list = new ArrayList<>(0);
        if (lastlogIn.getTime().toLocalDate().equals(LocalDate.now())) {
            if (minute > minute2 - 20 && minute < minute2 + 20) {
                userName.setText(user.getName());
                list = DatabaseRead.getAllTransactionsForUserFromDB(user.getId());
                /*for (int i = 0; i < ReadingFiles.getAllTransactions().size(); i++) {
                    User checkUser = ReadingFiles.getAllTransactions().get(i).getConsumer();
                    if(checkUser.equals(user)){
                        //list.add(ReadingFiles.getAllTransactions().get(i));
                        list.add(DatabaseRead.getAllTransactionsForUserFromDB(checkUser.getId()));
                    }
                }*/

                ObservableList<Transaction> observableList = FXCollections.observableList(list);
                transactionListView.setItems(observableList);
            }
        }
            else {
                userName.setText("Molimo ponovite LogIn");
                transactionListView.setVisible(false);
            }

    }

}
