import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskWindowController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private TextField task, cook, start, end, estTime, doses;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        Task currentTask = Model.getModel().getCurrentEvent().getCurrentTask();
        task.setEditable(false);
        cook.setEditable(false);
        start.setEditable(false);
        end.setEditable(false);
        estTime.setEditable(false);
        doses.setEditable(false);
        task.setText(currentTask.getName());
        if (currentTask.getCook() != null) cook.setText(currentTask.getCook().getName());
        else cook.setText("Non specificato.");
        if (currentTask.getStartTime() != null) start.setText(currentTask.getStartTime());
        else start.setText("Da specificare.");
        if (currentTask.getEndTime() != null) end.setText(currentTask.getEndTime());
        else end.setText("Da specificare.");
        if (currentTask.getEstimatedTime() != null) estTime.setText(currentTask.getEstimatedTime());
        else estTime.setText("Non specificato.");
        if (currentTask.getDoses() != null) doses.setText(currentTask.getDoses());
        else doses.setText("Non specificato.");
    }

    private void initializeButtons() {
        back.setOnAction((ActionEvent e) -> stage.close());

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

}
