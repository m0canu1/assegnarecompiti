package controller;

import classfiles.Cook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskBinderController implements Initializable {

    private Stage stage;

    @FXML
    private ListView<String> cookListView;

    @FXML
    private Button assignCook, cancel;

    @FXML
    private ChoiceBox<String> startShiftHour = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> estimatedTime = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> endShiftHour = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> doses = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> prepDoses = new ChoiceBox<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startShiftHour.getItems().addAll(null, "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
                "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        startShiftHour.setValue("12:00");
        endShiftHour.getItems().addAll(null, "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
                "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        endShiftHour.setValue("13:00");
        doses.getItems().addAll(null, "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20", "25", "30", "40", "50", "60");
        doses.setValue(null);
        prepDoses.getItems().addAll(null, "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20", "25", "30", "40", "50", "60");
        prepDoses.setValue(null);
        estimatedTime.getItems().addAll(null,"00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00");
        estimatedTime.setValue(null);
        initializeList();
        initializeButtons();
    }

    private void initializeList() {
        cookListView.setItems(Model.getModel().getCookObservableList());
        cookListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cookListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!cookListView.getSelectionModel().isEmpty()) {
                Model.getModel().setCurrentCook(Model.getModel().getCurrentCookByIndex(newIndex));
            }
        }));

        cookListView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                assignCook();
            }
        });
    }

    private void initializeButtons() {
        assignCook.setOnAction((ActionEvent e) -> assignCook());
        cancel.setOnAction((ActionEvent e) -> {
            Model.getModel().setCurrentCook(null);
            stage.close();
        });
    }

    private void assignCook() {
        if (((endShiftHour.getValue() != null && startShiftHour.getValue() != null) && endShiftHour.getValue().compareTo(startShiftHour.getValue()) > 0)) {
            Cook tempCook = Model.getModel().getCurrentCook();
            String start = startShiftHour.getValue();
            String end = endShiftHour.getValue();
            String estTime = estimatedTime.getValue();
            String dosesPrepared = prepDoses.getValue();
            if (dosesPrepared == null) dosesPrepared = "0";
            if (estTime == null) estTime = "0";
            String nof_doses = doses.getValue();
            if (nof_doses == null) nof_doses = "0";

            if ((Integer.parseInt(dosesPrepared) > Integer.parseInt(nof_doses))) {
                System.out.println("Prepared doses maggiori delle dosi da preparare!");
            } else {
                if (Model.getModel().getCurrentEvent().getCurrentTask() != null) {
                    if (tempCook != null) {
                        Model.getModel().getCurrentEvent().getCurrentTask().setCook(tempCook);
                        Model.getModel().bindCookToTask(tempCook);
                    }
//                    Model.getModel().getCurrentEvent().getCurrentTask().setPreparedDoses(dosesPrepared);
                    Model.getModel().bindTimeToTask(start, end, estTime, nof_doses, dosesPrepared);
                }
                stage = (Stage) assignCook.getScene().getWindow();
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        } else {
            System.out.println("MARIA SALVADOR ORARI SBALLATI");
        }
        Model.getModel().setCurrentCook(null);
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
