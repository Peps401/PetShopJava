package com.example.projectpetcentar;

import Exceptions.RequiredNoNotString;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static Datoteke.ReadingFiles.logger;

public class MeniController {

    public void searchStores (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStores.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga trgovina");
        HelloApplication.getStage().show();
    }

    public void newStore (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewStore.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Stvaranje nove trgovine");
        HelloApplication.getStage().show();
    }

    public void newAddress (ActionEvent actionEvent) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewAddress.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);

            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().setTitle("Stvaranje nove adrese");
            HelloApplication.getStage().show();

    }

    public void newCity (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewCity.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Stvaranje novog grada");
        HelloApplication.getStage().show();
    }

    public void searchStorage (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchStorage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga skladista");
        HelloApplication.getStage().show();
    }

    public void newStorage (ActionEvent actionEvent) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewStorage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Stvaranje novog skladista");
        HelloApplication.getStage().show();
    }

    public void addToShhelf (ActionEvent actionEvent) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddToShelf.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Dodaj na police");
        HelloApplication.getStage().show();
    }

    public void searchBosses (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchBosses.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga sefova");
        HelloApplication.getStage().show();
    }

    public void newBoss (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewBoss.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Stvaranje novog sefa");
        HelloApplication.getStage().show();
    }

    public void searchWorkers (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchWorker.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga radnika");
        HelloApplication.getStage().show();
    }

    public void newWorker (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewWorker.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Stvaranje novog radnika");
        HelloApplication.getStage().show();
    }

    public void searchItems (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchItems.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga proizvoda");
        HelloApplication.getStage().show();
    }

    public void newItem (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewItem.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Stvaranje novog proizvoda");
        HelloApplication.getStage().show();
    }

    public void searchCategories (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchCategories.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga kategorija");
        HelloApplication.getStage().show();
    }

    public void newCategory (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Stvaranje nove kategroije");
        HelloApplication.getStage().show();
    }

    public void searchDiscount (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Discount.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Popusti");
        HelloApplication.getStage().show();
    }

    public void LogIn (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("LogIn");
        HelloApplication.getStage().show();
    }

    public void SignUp (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SignUp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("SignUp");
        HelloApplication.getStage().show();
    }

    public void BuyCart (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Cart.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Kosarica");
        HelloApplication.getStage().show();
    }
    public void Buy(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Buy.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Kupuj");
        HelloApplication.getStage().show();
    }

    public void SeeAllTransactions (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SeeAllTransactions.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga svih tranzakcija");
        HelloApplication.getStage().show();
    }

    public void SeeAllChanges (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ChangesLog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().setTitle("Pretraga svih promjena");
        HelloApplication.getStage().show();
    }

}
