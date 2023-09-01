package com.example.projectpetcentar;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

public class StartingController {
    @FXML
    ImageView imageView;
    public void initialize() throws FileNotFoundException {
        imageView.setImage(new Image(new FileInputStream("src/main/resources/img/img.png")));

    }
}
