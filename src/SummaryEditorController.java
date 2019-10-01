import classfiles.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SummaryEditorController implements Initializable {

    private Stage stage;
    private Summary currentSummary;

    @FXML
    private VBox staffBox, turnBox;

    @FXML
    private Button bindTask, removeTask, addNewTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindTask = new Button("Assegna compito");
        removeTask = new Button("Togli compito");
        addNewTask = new Button("Crea compito");

        staffBox = new VBox();
        turnBox = new VBox();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    public Summary getCurrentSummary() {
        return currentSummary;
    }

    void setCurrentSummary(Summary currentSummary) {
        this.currentSummary = currentSummary;
    }
}
