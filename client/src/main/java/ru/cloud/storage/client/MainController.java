package ru.cloud.storage.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import ru.cloud.storage.common.Command;
import ru.cloud.storage.common.FileListMsg;
import ru.cloud.storage.common.FileMsg;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    @FXML
    TableView<FileView> localFilesTableView;

    @FXML
    TableView<FileView> cloudFilesTableView;

    private ObservableList<FileView> localFilesList;
    private ObservableList<FileView> cloudFilesList;

    private String localFolder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        localFilesList = FXCollections.observableArrayList();
        cloudFilesList = FXCollections.observableArrayList();

        initTableView(localFilesTableView);
        initTableView(cloudFilesTableView);

        localFilesTableView.setItems(localFilesList);
        cloudFilesTableView.setItems(cloudFilesList);

        localFolder = Network.getInstance().getLocalFolder().toString();
        refreshLocalFilesList(localFolder);
        Network.getInstance().setMainController(this);
        FileListMsg fileListMsg = new FileListMsg(Network.getInstance().getLogin(), Command.GET_FILELIST);
        try {
            Network.getInstance().sendMsg(fileListMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void refreshLocalFilesList(String s) {
        localFilesList.clear();
        try {
            localFilesList.addAll(Files.list(Paths.get(s)).map(Path::toFile).map(FileView::new).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void refreshCloudFilesList(FileListMsg msg) {
        Platform.runLater(() -> {
            cloudFilesList.clear();
            cloudFilesList.addAll(msg.getFileList().stream().map(Paths::get).map(Path::toFile).map(FileView::new).collect(Collectors.toList()));
        });
    }

    private void initTableView(TableView<FileView> tableView) {
        TableColumn<FileView, String> columnFileName = new TableColumn<>("File name");
        columnFileName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnFileName.setPrefWidth(250);


        TableColumn<FileView, String> columnFileSize = new TableColumn<>("Size");
        columnFileSize.setCellValueFactory(new PropertyValueFactory<>("size"));

        tableView.getColumns().addAll(columnFileName, columnFileSize);

    }

    public void btnUploadAction(ActionEvent actionEvent) throws IOException {
        FileView fileView = localFilesTableView.getSelectionModel().getSelectedItem();
        FileMsg fileMsg = new FileMsg(Network.getInstance().getLogin(), fileView.getFile().toPath(), Command.PUT_FILE);
        Network.getInstance().sendMsg(fileMsg);
    }

    public void btnRenameLocalFileAction(ActionEvent actionEvent) {
        FileView fileView = localFilesTableView.getSelectionModel().getSelectedItem();
        //FileChooser
        //fileView.getFile().renameTo();
    }

    public void btnDeleteLocalFileAction(ActionEvent actionEvent) {
        FileView fileView = localFilesTableView.getSelectionModel().getSelectedItem();
        try {
            Files.deleteIfExists(Paths.get(fileView.getFile().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshLocalFilesList(localFolder);
    }

    public void btnRefreshOnClientAction(ActionEvent actionEvent) {
        refreshLocalFilesList(localFolder);
    }

    public void btnDownload(ActionEvent actionEvent) throws IOException {
        FileView fileView = cloudFilesTableView.getSelectionModel().getSelectedItem();

        FileMsg msg = new FileMsg(Network.getInstance().getLogin(), fileView.getName(), Command.GET_FILE);
        Network.getInstance().sendMsg(msg);
    }

    public void btnRefreshOnServerAction(ActionEvent actionEvent) throws IOException {
        FileListMsg msg = new FileListMsg(Network.getInstance().getLogin(), Command.GET_FILELIST);
        Network.getInstance().sendMsg(msg);
    }

    public void btnDeleteOnServerAction(ActionEvent actionEvent) throws IOException {
        FileView fileView = cloudFilesTableView.getSelectionModel().getSelectedItem();
        FileMsg msg = new FileMsg(Network.getInstance().getLogin(), fileView.getName(), Command.DELETE_FILE);
        Network.getInstance().sendMsg(msg);
    }

    public void LocalFilesTableOnDragOver(DragEvent dragEvent) {
        if (dragEvent.getGestureSource() != localFilesTableView && dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
    }

    public void LocalFilesTableOnDragDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            for (int i = 0; i < db.getFiles().size(); i++) {
                try {
                    Files.copy(Paths.get(db.getFiles().get(i).getAbsolutePath()), Paths.get(localFolder + "\\" + db.getFiles().get(i).getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            success = true;
        }
        dragEvent.setDropCompleted(success);
        dragEvent.consume();
    }
}
