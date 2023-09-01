package com.example.projectpetcentar;

import Database.DatabaseInsert;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import Datoteke.WritingInFile;
import Exceptions.AlreadyExists;
import Exceptions.RequiredNoNotString;
import entiteti.Address;
import entiteti.City;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.OptionalLong;

public class NewAddressController {
    @FXML
    TextField name;
    @FXML
    TextField noOfStreet;
    @FXML
    ComboBox<City> cityComboBox;
    private static final Logger logger = LoggerFactory.getLogger(ReadingFiles.class);

    public void initialize(){
        ObservableList<City> cityObservableList = FXCollections.observableList(DatabaseRead.getCitiesFromDB());
        cityComboBox.setItems(cityObservableList);

    }

    public void newAddress(){

        boolean isNumeric = noOfStreet.getText().chars().allMatch( Character::isDigit);

        try {
            OptionalLong id = DatabaseRead.getAddressesFromDB().stream().mapToLong(addresss -> addresss.getId()).max();

            if(!isNumeric) throw new RequiredNoNotString();

            Address address = new Address(id.getAsLong() + 1, name.getText(), Integer.parseInt(noOfStreet.getText()), cityComboBox.getValue());
            //WritingInFile.writeNewAddress(address);

            for (int i = 0; i < DatabaseRead.getAddressesFromDB().size(); i++) {
                if(DatabaseRead.getAddressesFromDB().get(i).equals(address)){
                    throw new AlreadyExists();
                }
            }

            DatabaseInsert.createNewAddress(address);
            DatabaseInsert.insertNewAddressIntoCity(address);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Uspjesno stvaranje");
            alert.setHeaderText("Uspjesno stvaranje objekta adrese");
            alert.setContentText(String.valueOf(address));
            alert.show();
            logger.info("Uspjesno stvaranje adrese");

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStores.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.getStage().setTitle("Pretraga ducana");
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();

        }catch (AlreadyExists | RequiredNoNotString ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Pogreska");
            alert.setHeaderText("Neuspjelo stvaranje objekta adrese");
            alert.setContentText("U polje Broj ulice je potrebno upisati broj");
            alert.show();
            logger.info("Neuspjesno stvaranje adrese, u polje se upisao string ne broj");

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewAddress.fxml"));
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.getStage().setTitle("Stvori novu adresu");
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        }
    }
}
