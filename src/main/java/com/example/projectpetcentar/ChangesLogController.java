package com.example.projectpetcentar;

import Database.DatabaseRead;
import entiteti.ChangesLog;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.util.List;

public class ChangesLogController {
    @FXML
    private TableView<ChangesLog> changesTableView;
    @FXML
    private TableColumn<ChangesLog, String> oldDataColumn;
    @FXML
    private TableColumn<ChangesLog, String> newDataColumn;
    @FXML
    private TableColumn<ChangesLog, String> userColumn;
    @FXML
    private TableColumn<ChangesLog, String> timeColumn;

    public void initialize(){

        oldDataColumn.setCellValueFactory(cell-> {return new SimpleObjectProperty(cell.getValue().getOldData());
        });
        newDataColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getNewData());
        });
        userColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getUser());
        });
        timeColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getTime());
        });

        ObservableList<ChangesLog> list = FXCollections.observableList(ChangesLog.getChangesList());
        changesTableView.setItems(list);


    }
}
