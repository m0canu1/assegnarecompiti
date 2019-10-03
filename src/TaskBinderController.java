import classfiles.Cook;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskBinderController implements Initializable {

    private Stage stage;

    @FXML
    private ListView<String> cookListView;

    @FXML
    private Button assignCook;

    @FXML
    private ChoiceBox<String> startShiftHour = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> endShiftHour = new ChoiceBox<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startShiftHour.getItems().addAll(null, "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
                "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        startShiftHour.setValue(null);
        endShiftHour.getItems().addAll(null, "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00",
                "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        endShiftHour.setValue(null);
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

        cookListView.setMinWidth(500);


        cookListView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    assignCook();
                }
            }
        });
    }

    private void initializeButtons() {
        assignCook.setOnAction((ActionEvent e) -> assignCook());
    }

    private void assignCook() {
        if ((endShiftHour.getValue() == null && startShiftHour.getValue() == null) || ((endShiftHour.getValue() != null && startShiftHour.getValue() != null) && endShiftHour.getValue().compareTo(startShiftHour.getValue()) > 0)) {
            Cook tempCook = Model.getModel().getCurrentCook();
//                System.out.println("Assign the cook to the turn");
            if (tempCook != null) {
                if (Model.getModel().getCurrentEvent().getCurrentTask() != null) {
                    Model.getModel().getCurrentEvent().getCurrentTask().setCook(tempCook);
                    Model.getModel().bindCookToTask(tempCook);

                }
                // Model.getModel().getCurrentTask().setCook(tempCook);
            }
            stage = (Stage) assignCook.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        } else {
            System.out.println("MARIA SALVADOR ORARI SBALLATI");
        }

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

}
