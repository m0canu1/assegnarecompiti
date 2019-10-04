import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskWindowController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private Text task, cook, start, end, estTime, doses;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        Task currentTask = Model.getModel().getCurrentEvent().getCurrentTask();
        task.setText(currentTask.getName());
        if (currentTask.getCook() != null) cook.setText(currentTask.getCook().getName());
        else cook.setText("");
        if (currentTask.getStartTime() != null) start.setText(currentTask.getStartTime());
        else start.setText("");
        if (currentTask.getEndTime() != null) end.setText(currentTask.getEndTime());
        else end.setText("");
        if (currentTask.getEstimatedTime() != null) estTime.setText(currentTask.getEstimatedTime());
        else estTime.setText("");
        if (currentTask.getDoses() != null) doses.setText(currentTask.getDoses());
        else doses.setText("");
    }

    private void initializeButtons() {
        back.setOnAction((ActionEvent e) -> stage.close());

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

}
