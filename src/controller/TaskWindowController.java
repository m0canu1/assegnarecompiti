package controller;

import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Model;

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
        if (currentTask.getEstimatedTime() != null || (currentTask.getEstimatedTime().compareTo("0") == 0) ) estTime.setText(currentTask.getEstimatedTime());
        else estTime.setText("Non specificato.");
//        if (currentTask.getDoses() != null){
//            int intDoses = Integer.parseInt(currentTask.getDoses()) - Integer.parseInt(currentTask.getPreparedDoses());
//            String finalDoses = Integer.toString(intDoses);
//            if(finalDoses.compareTo("0") == 0){
//                doses.setText("Non specificato.");
//            }else {
//                doses.setText(finalDoses);
//            }
//        }
//        else doses.setText("Non specificato.");
        doses.setText(String.valueOf(currentTask.getDoses()));
    }

    private void initializeButtons() {
        back.setOnAction((ActionEvent e) -> stage.close());

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

}
