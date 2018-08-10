package ru.cloud.storage.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TableView<FileView> localFilesTableView, cloudFilesTableView;

    private ObservableList<FileView> localFilesList, cloudFilesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localFilesList = FXCollections.observableArrayList();
        cloudFilesList = FXCollections.observableArrayList();

        initTableView(localFilesTableView);
        initTableView(cloudFilesTableView);

        localFilesTableView.setItems(localFilesList);
        cloudFilesTableView.setItems(cloudFilesList);

    }

    private void initTableView(TableView<FileView> tableView) {
        TableColumn<FileView, String> columnFileName = new TableColumn<>("File name");
        columnFileName.setCellValueFactory(new PropertyValueFactory<>("name"));


        TableColumn<FileView, String> columnFileSize = new TableColumn<>("Size");
        columnFileName.setCellValueFactory(new PropertyValueFactory<>("size"));

        tableView.getColumns().addAll(columnFileName, columnFileSize);
    }
}
