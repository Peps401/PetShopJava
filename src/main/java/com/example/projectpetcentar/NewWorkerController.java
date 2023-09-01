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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.OptionalLong;

public class NewWorkerController {
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
        if(SearchWorkerController.updateWorkerIsTriggered) updateIsTriggered();
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
    protected void newWorker(){

        if(!SearchWorkerController.updateWorkerIsTriggered) {
            if (name.getText().isEmpty() || surname.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText("Objekt radnika ne moze se stvoriti jer su vazna polja ostala nepopunjena");
                alert.show();
            } else {
                OptionalLong id = DatabaseRead.getAllWorkersFromDB().stream().mapToLong(worker -> worker.getId()).max();
                Gender gender = Gender.FEMALE;
                if (m.isSelected()) {
                    gender = Gender.MALE;
                }
                OptionalLong maxId = DatabaseRead.getAllWorkersFromDB().stream().mapToLong(i->i.getId()).max();
                Worker worker = new Worker(maxId.getAsLong() + 1, name.getText(), surname.getText(), datePicker.getValue(), gender);
                //WritingInFile.writeNewWorker(worker);
                DatabaseInsert.createNewWorker(worker);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potvrda");
                alert.setHeaderText("Objekt radnika uspjesno se stvorio");
                alert.setContentText(worker.getSurname());
                alert.show();

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchWorker.fxml"));
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
        }
        else{
            Gender gender = Gender.FEMALE;
            if (m.isSelected()) {
                gender = Gender.MALE;
            }

            Worker w = new Worker(SearchWorkerController.chosenWorker.getId(), name.getText(), surname.getText(), datePicker.getValue(), gender);
            DatabaseChange.updateWorkerFromDB(w);

            String oldWorkerString = SearchWorkerController.chosenWorker.getName() + "\n" +
                    SearchWorkerController.chosenWorker.getSurname() + "\n" + SearchWorkerController.chosenWorker.getDateOfBirth().toString() + "\n" + SearchWorkerController.chosenWorker.getGenderEnum().name();
            String newWorkerString = w.getName() + "\n" +
                    w.getSurname() + "\n" + w.getDateOfBirth().toString() + "\n" + w.getGenderEnum().name();

            User user = DatabaseInsert.getLastLogIn().getUser();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
            String timeString = LocalDateTime.now().format(formatter);

            ChangesLog.write(oldWorkerString, newWorkerString, user.getName(), timeString);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda");
            alert.setHeaderText("Objekt radnika uspjesno se promijenio");
            alert.show();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SearchWorker.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 700, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HelloApplication.getStage().setTitle("Pretraga radnika");
            HelloApplication.getStage().setScene(scene);
            HelloApplication.getStage().show();
        }
    }

    public void updateIsTriggered(){
        Worker w = SearchWorkerController.chosenWorker;

        name.setText(w.getName());
        surname.setText(w.getSurname());
        datePicker.setValue(w.getDateOfBirth());

    }
}
