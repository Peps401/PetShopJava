package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseRead;
import Datoteke.ReadingFiles;
import entiteti.Storage;
import entiteti.Worker;
import entiteti.Gender;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Datoteke.ReadingFiles.logger;

public class SearchWorkerController {
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TableView<Worker> workersTableView;
    @FXML
    private TableColumn<Worker, Long> idColumn;
    @FXML
    private TableColumn<Worker, String> nameColumn;
    @FXML
    private TableColumn<Worker, String> surnnameColumn;
    @FXML
    private TableColumn<Worker, LocalDate> dateColumn;
    @FXML
    private TableColumn<Worker, Enum<Gender>> genderColumn;
    @FXML
    private DatePicker datePicker;
    @FXML
    private RadioButton m;
    @FXML
    private RadioButton f;

    static Worker chosenWorker;
    static boolean updateWorkerIsTriggered;

    @FXML
    public void initialize(){
        //List<Worker> workers = ReadingFiles.getAllWorkers();
        List<Worker> workers = DatabaseRead.getAllWorkersFromDB();
        updateWorkerIsTriggered = false;

        idColumn.setCellValueFactory(cell-> {return new SimpleObjectProperty(cell.getValue().getId());
        });
        nameColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getName());
        });
        surnnameColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getSurname());
        });
        dateColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getDateOfBirth());
        });
        genderColumn.setCellValueFactory(cell -> {return new SimpleObjectProperty<>(cell.getValue().getGenderEnum());
        });

        ObservableList<Worker> workersObservableList = FXCollections.observableList(workers);
        workersTableView.setItems(workersObservableList);

        ToggleGroup toggleGroup = new ToggleGroup();

        f.setToggleGroup(toggleGroup);

        m.setToggleGroup(toggleGroup);
    }

    @FXML
    protected void searchWorker(){
        //List<Worker> list = ReadingFiles.getAllWorkers();
        List<Worker> list = DatabaseRead.getAllWorkersFromDB();

        if(!name.getText().isEmpty()){
            list = list.stream().filter(worker -> worker.getName().toLowerCase().contains(name.getText().toLowerCase())).collect(Collectors.toList());
        }
        if(!surname.getText().isEmpty()){
            list = list.stream().filter(worker -> worker.getSurname().toLowerCase().contains(surname.getText().toLowerCase())).collect(Collectors.toList());
        }
        if(m.isSelected()){
            list = list.stream().filter(worker -> worker.getGenderEnum().name().equals("MALE")).collect(Collectors.toList());
        }
        if(f.isSelected()){
            list = list.stream().filter(worker -> worker.getGenderEnum().name().equals("FEMALE")).collect(Collectors.toList());
        }
        if(Optional.ofNullable(datePicker.getValue()).isPresent()){
            LocalDate ld = datePicker.getValue();
            list = list.stream().filter(boss -> boss.getDateOfBirth().equals(ld)).collect(Collectors.toList());
        }

        ObservableList<Worker> workerObservableList = FXCollections.observableList(list);
        workersTableView.setItems(workerObservableList);
    }

    @FXML
    protected void ClearButton(){
        //List<Worker> list = ReadingFiles.getAllWorkers();
        List<Worker> list = DatabaseRead.getAllWorkersFromDB();
        ObservableList<Worker> observableList = FXCollections.observableList(list);
        workersTableView.setItems(observableList);
    }

    @FXML
    protected void deleteWorker() {

        Worker worker = workersTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Zelite li izbrisati odabranog radnika?");
        alert.setContentText("Jeste li sigurni?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().getText().equals("Yes")) {
            boolean workerInStore = false;
            for (int i = 0; i < DatabaseRead.getAllStoragesFromDB().size(); i++) {
                if(DatabaseRead.getAllStoragesFromDB().get(i).getWorkers().contains(worker)){
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Brisanje radnika");
                    alert.setHeaderText("Radnik se ne moze izbrisati jer je povezan s jednim od skladista");
                    alert.setContentText("Radnik: " + worker.getName() + " " + worker.getSurname());
                    alert.showAndWait();
                    workerInStore = true;
                    break;
                }
            }
            for (int i = 0; i < DatabaseRead.getAllStoresFromDB().size(); i++) {
                if(DatabaseRead.getAllStoresFromDB().get(i).getWorkers().contains(worker)){
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Brisanje radnika");
                    alert.setHeaderText("Radnik se ne moze izbrisati jer je povezan s jednim od ducana");
                    alert.setContentText("Radnik: " + worker.getName() + " " + worker.getSurname());
                    alert.showAndWait();
                    workerInStore = true;
                    break;
                }
            }
            if(!workerInStore) {
                DatabaseChange.deleteWorkerFromDB(worker);

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Brisanje radnika");
                alert.setHeaderText("Uspješno izbrisan objekt radnika!");
                alert.setContentText("Radnik: " + worker.getName() + " " + worker.getSurname() + " uspjesno izbrisan!");
                alert.showAndWait();
                logger.info("Korisnik je izbriso radnika: " + worker.getName() + " " + worker.getSurname() + ".");

            }
            ClearButton();
        }
    }
    @FXML
    protected void updateWorker() {
        updateWorkerIsTriggered = true;
        chosenWorker = workersTableView.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewWorker.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 700, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HelloApplication.getStage().setTitle("Promijenite podatke o odabranom Radniku");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
}
