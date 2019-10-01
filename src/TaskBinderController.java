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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskBinderController implements Initializable {

    private Stage stage;
    private Task currentTask;

    @FXML
    private ListView<String> cookListView;

    @FXML
    private Button assignCook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        Cook tempCook = Model.getModel().getCurrentCook();
        assignCook.setOnAction((ActionEvent e) -> {
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
