package com.example.projectpetcentar;

import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import Datoteke.WritingInFile;
import entiteti.LogIn;
import entiteti.PasswordHashing;
import entiteti.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalLong;
import java.util.Scanner;

public class SignUpController {
    @FXML
    TextField name;
    @FXML
    TextField password;

    @FXML
    protected void newUser(){
        //List<User> users = ReadingFiles.getAllUsers();
        List<User> users = DatabaseRead.getAllUsersFromDB();
        String chosenName = name.getText();
        String chosenPass = password.getText();

        boolean alreadyExists = false;
        for (User u:
             users) {
            if(u.getName().equals(chosenName) && u.getPass().equals(chosenPass)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Greska");
                alert.setHeaderText("Objekt korisnika kojeg pokusavate dodati vec postoji");
                alert.setContentText("Pokusajte s novim imenom i lozinkom");
                alert.show();
                alreadyExists = true;
                break;
            }
        }

        if(!alreadyExists){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Uspjesno stvaranje");
            alert.setHeaderText("Objekt korisnika se uspjesno stvorio");
            alert.setContentText("Stvorili ste korisnika: " + chosenName + " lozinka: " + chosenPass);
            alert.show();
            //WritingInFile.writeNewUser(chosenName, chosenPass);
            OptionalLong id = DatabaseRead.getAllUsersFromDB().stream().mapToLong(u -> u.getId()).max();
            User user = new User(id.getAsLong() + 1 , chosenName, chosenPass);

            DatabaseInsert.createNewUser(user);
            String hashedPass = PasswordHashing.doHash(chosenPass);
            System.out.println(chosenPass + " " + hashedPass  );
            WritingInFile.writeNewUser(chosenName, hashedPass);


            OptionalLong idD = DatabaseRead.getAllLogInsFromDB().stream().mapToLong(l -> l.getId()).max();
            LogIn l = new LogIn(idD.getAsLong() + 1, user, LocalDateTime.now(), BigDecimal.valueOf(10));

            DatabaseInsert.createNewLogIn(l);
        }
    }
}
