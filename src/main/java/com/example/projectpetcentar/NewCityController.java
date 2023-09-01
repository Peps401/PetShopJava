package com.example.projectpetcentar;

import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import Datoteke.WritingInFile;
import entiteti.City;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.OptionalLong;

import static Datoteke.ReadingFiles.logger;

public class NewCityController {
    @FXML
    TextField nameCountry;
    @FXML
    TextField name;
    @FXML
    TextField postalNo;

    @FXML
    protected void newCity(){
        try {
            if (!nameCountry.getText().isEmpty() && !name.getText().isEmpty() && !postalNo.getText().isEmpty()) {
                OptionalLong id = ReadingFiles.getAllAddresses().stream().mapToLong(city -> city.getId()).max();

                City city = new City(id.getAsLong() + 1, name.getText(), nameCountry.getText().toUpperCase(), Integer.parseInt(postalNo.getText()));
                //WritingInFile.writeNewCity(city);
                DatabaseInsert.createNewCity(city);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Uspjesno stvaranje");
                alert.setHeaderText("Uspjesno stvaranje objekta grada");
                alert.setContentText(String.valueOf(city));
                alert.show();
                logger.info("Uspjesno stvaranje grada");

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStore.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 650, 650);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.getStage().setTitle("Pretraga ducana");
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().show();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Pogreska");
                alert.setHeaderText("Neuspjelo stvaranje objekta grada");
                alert.setContentText("Jedno ili vise polja ostalo je prazno");
                alert.show();
            }
        }catch (RuntimeException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreska");
            alert.setHeaderText("Neuspjelo stvaranje objekta grada");
            //alert.setContentText("U polje Postanski broj je potrebno upisati broj");
            String no = postalNo.getText();
            if(no.length() > 5){
                alert.setContentText("U polje Postanski broj je potrebno upisati broj koji ima najvise 5 znamenki");
            }
            else{
                alert.setContentText("U polje Postanski broj je potrebno upisati broj");
            }
            alert.show();
            logger.info("Neuspjesno stvaranje grada");
        }
    }
}
