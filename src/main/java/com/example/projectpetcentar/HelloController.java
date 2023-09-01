package com.example.projectpetcentar;

import Datoteke.ReadingFiles;
import entiteti.AdoptionStore;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        List<AdoptionStore> list = ReadingFiles.getAllAdoptionStores();
    }
}