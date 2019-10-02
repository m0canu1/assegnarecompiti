import classfiles.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskBinderController implements Initializable {

    private Stage stage;
    private Task currentTask;
    private Cook tempCook;

    @FXML
    private ListView<String> cookListView;

    @FXML
    private Button assignCook;

    @FXML
    private ChoiceBox<String> startShiftHour = new ChoiceBox<String>();

    @FXML
    private ChoiceBox<String> endShiftHour = new ChoiceBox<String>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startShiftHour.getItems().addAll("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
                "08:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        startShiftHour.setValue("12:00");
        endShiftHour.getItems().addAll("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
                "08:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        endShiftHour.setValue("13:00");
        initializeList();
        initializeButtons();
    }

    private void initializeList() {
        cookListView.setItems(Model.getModel().getCookObservableList());
        cookListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cookListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!cookListView.getSelectionModel().isEmpty()) {
                Model.getModel().setCurrentTask(Model.getModel().getCurrentTaskByIndex(newIndex));
            }
        }));
    }

    private void initializeButtons() {
        assignCook.setOnAction((ActionEvent e) -> {
            tempCook = Model.getModel().getCurrentCook();
            System.out.println("Assign the cook to the turn");
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentTask(Task task) {
        this.currentTask = task;
    }
}
