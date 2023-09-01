package com.example.projectpetcentar;

import Database.DatabaseChange;
import Database.DatabaseRead;
import entiteti.Boss;
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

public class SearchBossesController {
    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TableView<Boss> bossTableView;
    @FXML
    private TableColumn<Boss, Long> idColumn;
    @FXML
    private TableColumn<Boss, String> nameColumn;
    @FXML
    private TableColumn<Boss, String> surnnameColumn;
    @FXML
    private TableColumn<Boss, LocalDate> dateColumn;
    @FXML
    private TableColumn<Boss, Enum<Gender>> genderColumn;
    @FXML
    private DatePicker datePicker;
    @FXML
    private RadioButton m;
    @FXML
    private RadioButton f;

    static Boss chosenWorker;
    static boolean updateBossIsTriggered;

    @FXML
    public void initialize(){
        //List<Boss> bosses = ReadingFiles.getAllBosses();
        List<Boss> bosses = DatabaseRead.getBossesFromDB();
        boolean updateBossIsTriggered = false;

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
        ObservableList<Boss> bossObservableList = FXCollections.observableList(bosses);
        bossTableView.setItems(bossObservableList);

        ToggleGroup toggleGroup = new ToggleGroup();

        f.setToggleGroup(toggleGroup);

        m.setToggleGroup(toggleGroup);
    }

    @FXML
    protected void searchBoss(){
        //List<Boss> list = ReadingFiles.getAllBosses();
        List<Boss> list = DatabaseRead.getBossesFromDB();

        if(!name.getText().isEmpty()){
            list = list.stream().filter(boss -> boss.getName().toLowerCase().contains(name.getText().toLowerCase())).collect(Collectors.toList());
        }
        if(!surname.getText().isEmpty()){
            list = list.stream().filter(boss -> boss.getSurname().toLowerCase().contains(surname.getText().toLowerCase())).collect(Collectors.toList());
        }
        if(m.isSelected()){
            list = list.stream().filter(boss -> boss.getGenderEnum().name().equals("MALE")).collect(Collectors.toList());
        }
        if(f.isSelected()){
            list = list.stream().filter(boss -> boss.getGenderEnum().name().equals("FEMALE")).collect(Collectors.toList());
        }
        if(Optional.ofNullable(datePicker.getValue()).isPresent()){
            LocalDate ld = datePicker.getValue();
            list = list.stream().filter(boss -> boss.getDateOfBirth().equals(ld)).collect(Collectors.toList());
        }

        ObservableList<Boss> bossObservableList = FXCollections.observableList(list);
        bossTableView.setItems(bossObservableList);
    }

    @FXML
    protected void ClearButton(){
        //List<Boss> list = ReadingFiles.getAllBosses();
        List<Boss> list = DatabaseRead.getBossesFromDB();
        ObservableList<Boss> observableList = FXCollections.observableList(list);
        bossTableView.setItems(observableList);
    }

    @FXML
    protected void deleteBoss() {

        Boss boss = bossTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Zelite li izbrisati odabranog sefa?");
        alert.setContentText("Jeste li sigurni?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get().getText().equals("Yes")) {

            DatabaseChange.deleteBossFromDB(boss);

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Brisanje sefa");
            alert.setHeaderText("Uspješno izbrisan objekt sefa!");
            alert.setContentText("Sef: " + boss.getName() + " " + boss.getSurname() + " uspjesno izbrisan!");
            alert.showAndWait();
            logger.info("Korisnik je izbriso sefa: " + boss.getName() + " " + boss.getSurname() + ".");

            ClearButton();
        }
    }
    @FXML
    protected void updateBoss() {
        chosenWorker = bossTableView.getSelectionModel().getSelectedItem();
        updateBossIsTriggered = true;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewBoss.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 700, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HelloApplication.getStage().setTitle("Promijenite podatke o odabranomSefu");
        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();
    }
}
