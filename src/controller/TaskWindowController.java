package controller;

import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskWindowController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private Label task, cook, start, end, estTime, doses;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        Task currentTask = Model.getModel().getCurrentEvent().getCurrentTask();
        if (task != null) task.setText(currentTask.getName());

        if (currentTask.getCook() != null) cook.setText(currentTask.getCook().getName());
        else cook.setText("Not specified.");
        if (currentTask.getStartTime() != null) start.setText(currentTask.getStartTime());
        else start.setText("Not specified.");
        if (currentTask.getEndTime() != null) end.setText(currentTask.getEndTime());
        else end.setText("Not specified.");
        if (currentTask.getEstimatedTime() != null && (currentTask.getEstimatedTime().compareTo("0") != 0)) estTime.setText(currentTask.getEstimatedTime());
        else estTime.setText("Not specified.");

        doses.setText(String.valueOf(currentTask.getDoses() - currentTask.getPreparedDoses()));
    }

    private void initializeButtons() {
        back.setOnAction((ActionEvent e) -> stage.close());

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

}
