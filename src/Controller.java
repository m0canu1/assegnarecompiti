import classfiles.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Stage stage;

    @FXML
    private Button openSummary, generateSummary;

    @FXML
    private ListView<Event> eventListView;

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
    }

    /**
     *
     */
    private void initializeButtons() {
        Event tempEvent = Model.getModel().getCurrentEvent();
        openSummary.setOnAction((ActionEvent e) -> {
                    if (tempEvent.hasSummary()) {
                        editSummary(tempEvent.getSummary());
                    } else {
                        //TODO da saltare
                    }
        });
        generateSummary.setOnAction((ActionEvent e) -> {
            if (!tempEvent.hasSummary()) {
                editSummary(null);
            } else {
                //TODO errore
            }
        });
    }

    /**
     *
     * @param summary
     */
    private void editSummary(Summary summary) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SummaryEditor.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 350, 400);
            Stage stage = new Stage();
            SummaryEditorController summaryEditorController = fxmlLoader.getController();
            summaryEditorController.setStage(stage);

            if (summary != null) {
                stage.setTitle("Modifica Compiti");
                summaryEditorController.setCurrentSummary(summary);
            } else {
                stage.setTitle("Nuovi Compiti");
                summaryEditorController.setCurrentSummary(null);
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
