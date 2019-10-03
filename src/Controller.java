import classfiles.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Stage stage;

    private Event tempEvent;


    @FXML
    private Button openSummary;

    @FXML
    private ListView<String> eventListView;

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
//                System.out.println("New event selected" + newIndex);
                Model.getModel().setCurrentEvent(Model.getModel().getCurrentEventByIndex(newIndex));
//                System.out.println("Current selected event: " + Model.getModel().getCurrentEventByIndex(newIndex).getName());
            }
        }));

        eventListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    openSummary();
                }
            }
        });
    }

    /**
     *
     */
    //TODO quando clicco su openSummary devo sapere qual era l'evento selezionato
    private void initializeButtons() {
        openSummary.setOnAction((ActionEvent e) -> {
            openSummary();
        });
    }

    /**
     *
     * @param
     */
    private void openSummary() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SummaryEditor.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 350, 400);
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
