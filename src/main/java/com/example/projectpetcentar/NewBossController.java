package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseInsert;
import Database.DatabaseRead;
import entiteti.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.OptionalLong;

public class NewBossController {
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private DatePicker datePicker;
    @FXML
    private RadioButton m;
    @FXML
    private RadioButton f;
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize(){
        f.setSelected(true);
        datePicker.setValue(LocalDate.now());
        try {
            imageView.setImage(new Image(new FileInputStream("src/main/resources/img/student.jpeg")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (SearchBossesController.updateBossIsTriggered) updateIsTriggered();
    }

    @FXML
    protected void newPicture(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if(file != null) {
            imageView.setImage(new Image(file.getAbsolutePath()));
        }
    }

    @FXML
    protected void newBoss(){

        if(!SearchBossesController.updateBossIsTriggered) {
            if (name.getText().isEmpty() || surname.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText("Objekt sefa ne moze se stvoriti jer su vazna polja ostala nepopunjena");
                alert.show();
            } else {
                OptionalLong id = DatabaseRead.getBossesFromDB().stream().mapToLong(boss -> boss.getId()).max();
                Gender gender = Gender.FEMALE;
                if (m.isSelected()) {
                    gender = Gender.MALE;
                }
                Boss boss = new Boss(0L, name.getText(), surname.getText(), datePicker.getValue(), gender);
                //WritingInFile.writeNewBoss(boss);
                DatabaseInsert.createNewBoss(boss);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Objekt sefa uspjesno se stvorio");
                alert.setContentText(boss.getSurname());
                alert.show();

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchBosses.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 700, 500);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.getStage().setTitle("Pretraga sefova");
                HelloApplication.getStage().setScene(scene);
                HelloApplication.getStage().show();
            }
        }
        else{
            Gender gender = Gender.FEMALE;
            if (m.isSelected()) {
                gender = Gender.MALE;
            }

            Boss newBoss = new Boss(SearchBossesController.chosenWorker.getId(), name.getText(), surname.getText(), datePicker.getValue(), gender);
            DatabaseChange.updateBossFromDB(newBoss);

            String oldBossString = SearchBossesController.chosenWorker.getName() + "\n" +
                    SearchBossesController.chosenWorker.getSurname() + "\n" + SearchBossesController.chosenWorker.getDateOfBirth().toString() + "\n" + SearchBossesController.chosenWorker.getGenderEnum().name();
            String newBossString = newBoss.getName() + "\n" +
                    newBoss.getSurname() + "\n" + newBoss.getDateOfBirth().toString() + "\n" + newBoss.getGenderEnum().name();

            User user = DatabaseInsert.getLastLogIn().getUser();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            String timeString = LocalDateTime.now().format(formatter);

            ChangesLog.write(oldBossString, newBossString, user.getName(), timeString);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Objekt sefa uspjesno se promijenio");
            alert.show();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchBosses.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.getStage().setTitle("Pretraga sefova");
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        }
    }

    public void updateIsTriggered(){
        Boss b = SearchBossesController.chosenWorker;

        name.setText(b.getName());
        surname.setText(b.getSurname());
        datePicker.setValue(b.getDateOfBirth());

    }

}
