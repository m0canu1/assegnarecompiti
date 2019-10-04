package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button openSummary, closeEverything;

    @FXML
    private ListView<String> eventListView;

    @FXML
    Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeButtons();
        initializeList();
    }

    private void initializeList() {
        eventListView.setItems(Model.getModel().getEventObservableList());
        eventListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        eventListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!eventListView.getSelectionModel().isEmpty()) {
                Model.getModel().setCurrentEvent(Model.getModel().getCurrentEventByIndex(newIndex));
            }
        }));

        eventListView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                openSummary();
            }
        });
    }

    private void initializeButtons() {
        openSummary.setOnAction((ActionEvent e) -> openSummary());
        closeEverything.setOnAction((ActionEvent e) -> {
            stage = (Stage) closeEverything.getScene().getWindow();
            stage.close();
        });
    }

    private void openSummary() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("view/SummaryEditor.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            SummaryEditorController summaryEditorController = fxmlLoader.getController();
            summaryEditorController.setStage(stage);
            stage.setTitle("Summary Sheet Home: " + Model.getModel().getCurrentEvent().getName());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
