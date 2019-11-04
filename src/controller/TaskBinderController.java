package controller;

import classfiles.Cook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Model;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TaskBinderController implements Initializable {

    public Label error;
    public Label header;
    public Label info;
    private Stage stage;

    private final List<String> shiftHours = Arrays.asList(
            null, "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
            "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
            "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
    private final List<Integer> dosesQuantities = Arrays.asList(0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50, 60);

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
    private ChoiceBox<Integer> doses = new ChoiceBox<>();

    @FXML
    private ChoiceBox<Integer> prepDoses = new ChoiceBox<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        header.setText("TASK BINDER: " + Model.getModel().getCurrentEvent().getCurrentTask().getName());
        error.setVisible(false);
        startShiftHour.getItems().addAll(shiftHours);
        startShiftHour.setValue(Model.getModel().getCurrentEvent().getCurrentTask().getStartTime());
        endShiftHour.getItems().addAll(shiftHours);
        endShiftHour.setValue(Model.getModel().getCurrentEvent().getCurrentTask().getEndTime());
        doses.getItems().addAll(dosesQuantities);
        doses.setValue(Model.getModel().getCurrentEvent().getCurrentTask().getDoses());
        prepDoses.getItems().addAll(dosesQuantities);
        prepDoses.setValue(Model.getModel().getCurrentEvent().getCurrentTask().getPreparedDoses());
        estimatedTime.getItems().addAll(null, "00:05", "00:10", "00:15", "00:20", "00:25", "00:30", "00:45", "01:00", "01:15", "01:30", "01:45", "02:00");
        estimatedTime.setValue(Model.getModel().getCurrentEvent().getCurrentTask().getEstimatedTime());
        initializeList();
        Model.getModel().setCurrentCook(null);
        initializeButtons();
    }

    private void initializeList() {
        info.setVisible(false);
        cookListView.setItems(Model.getModel().getCookObservableList());
        cookListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cookListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!cookListView.getSelectionModel().isEmpty()) {
                Model.getModel().setCurrentCook(Model.getModel().getCurrentCookByIndex(newIndex));
                info.setText(Model.getModel().getCurrentCook().showAvailability());
                info.setVisible(true);
                error.setVisible(false);
            }
        }));

        cookListView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                assignCook();
            }
        });
    }

    private void initializeButtons() {
        assignCook.setOnAction((ActionEvent e) -> {
            assignCook();
        });
        cancel.setOnAction((ActionEvent e) -> {
            Model.getModel().setCurrentCook(null);
            stage.close();
        });
    }

    private void assignCook() {
        error.setVisible(false);
        info.setVisible(false);

        Cook tempCook = Model.getModel().getCurrentCook();
        String start = startShiftHour.getValue();
        String end = endShiftHour.getValue();
        String estTime = estimatedTime.getValue();
        if (estTime == null) estTime = "0";
        int dosesPrepared = prepDoses.getValue();
        int nof_doses = doses.getValue();

        if (((end != null && start != null) && end.compareTo(start) > 0)) {
            if (tempCook != null && !tempCook.isCookAvailable(start, end)) {
                error.setText("Error! Cook not available.");
                error.setVisible(true);
                info.setText(tempCook.showAvailability());
                info.setVisible(true);
            }
            else if ((dosesPrepared > nof_doses)) {
                error.setText("Error! Check the doses.");
                error.setVisible(true);
                System.out.println("Prepared doses greater than doses to be prepared!");
            } else {
                if (Model.getModel().getCurrentEvent().getCurrentTask() != null) {
                    if (tempCook != null) {
                        Model.getModel().getCurrentEvent().getCurrentTask().setCook(tempCook);
                        Model.getModel().bindCookToTask(tempCook);
                    }
                    Model.getModel().bindTimeToTask(start, end, estTime, nof_doses, dosesPrepared);
                }
                stage = (Stage) assignCook.getScene().getWindow();
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        }
        else {
            error.setText("Error! Check the shift hours.");
            error.setVisible(true);
        }

        //TODO rimosso perché causava NullPointerException dopo aver
        // dato un orario in cui il cuoco non era disponibile e averlo corretto con l'orario giusto
//        Model.getModel().setCurrentCook(null);

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
