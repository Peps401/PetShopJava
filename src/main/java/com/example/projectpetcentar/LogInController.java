package com.example.projectpetcentar;

import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import Datoteke.WritingInFile;
import entiteti.LogIn;
import entiteti.PasswordHashing;
import entiteti.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.random.RandomGenerator;

public class LogInController {
    @FXML
    TextField name;
    @FXML
    PasswordField passwordField;

    public void ifSuccessfullyLoggedIn(){
        System.out.println(name.getText());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Starting.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
            HelloApplication.setMainStageTitle("Dobrodosli " + name.getText());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void LogInButton(){
        //List<User> users = ReadingFiles.getAllUsers();
        //Map<User, List> map = ReadingFiles.getUsersLogins();

        List<User> users = DatabaseRead.getAllUsersFromDB();
        List<LogIn> logIns = DatabaseRead.getAllLogInsFromDB();


        String nameUser = name.getText();
        String pass = passwordField.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        boolean sameUser = false;

        User user = new User(0L, "PROVJERA", "PROVJERA");
        boolean foundUser = false;
        boolean alreadyLoggedIn = false;
        for (User u:
             users) {
            if(nameUser.equals(u.getName()) && pass.equals(u.getPass())){
                foundUser = true;
                user = u;
                break;
            }

        }

        sameUser = PasswordHashing.SamePassword(new User(0L, nameUser, pass));
        System.out.println(sameUser);


        if(!foundUser){
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle("Neuspjesna prijava");
            alert1.setHeaderText("Ne postoji takav korisnik");
            alert1.show();
        }
        else {
            for (LogIn l :
                    logIns) {
                if (l.getUser().equals(user)) {
                    LocalDateTime logInTime = l.getTime();
                    LocalDateTime now = LocalDateTime.now();

                    BigDecimal discount = l.getDiscount();

                    int minute = logInTime.getMinute() + logInTime.getHour() * 60;
                    int minute2 = now.getMinute() + now.getHour() * 60;

                    if ((logInTime).toLocalDate().equals(now.toLocalDate())) {
                        if (minute > minute2 - 20 && minute < minute2 + 20) {
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
                            String time = logInTime.plusMinutes(20).format(dtf);
                            alreadyLoggedIn = true;
                            alert.setTitle("Prijava uspjesna");
                            alert.setHeaderText("Vec ste se prijavili: " + time);
                            alert.setContentText("Ostvarili ste pravo na popust od:" +
                                    discount + "%" + " koji traje do: " + time);
                            alert.show();
                            ifSuccessfullyLoggedIn();
                        }
                    }
                }

            }
        }
        if(!alreadyLoggedIn && foundUser) {
            //WritingInFile.writeNewLogIn(user.getId(), LocalDateTime.now(), 10);
            OptionalLong id = DatabaseRead.getAllLogInsFromDB().stream().mapToLong(l -> l.getId()).max();
            List<LogIn> list = DatabaseRead.getAllLogInsFromDB();
            BigDecimal discount = BigDecimal.valueOf(10);
            for (LogIn l:
                 list) {
                if(l.getUser().equals(foundUser)){
                    discount = l.getDiscount();
                    break;
                }
            }
            LogIn l = new LogIn(id.getAsLong() + 1, user, LocalDateTime.now(), discount);
            DatabaseInsert.createNewLogIn(l);
            alert.setTitle("Prijava uspjesna");
            alert.setHeaderText("Uspjesno ste se prijavili");
            alert.setContentText("Hvala vam na prijavi");
            alert.show();
            ifSuccessfullyLoggedIn();

        }
    }
}
