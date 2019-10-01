import classfiles.Summary;
import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SummaryEditorController implements Initializable {

    private Stage stage;
    private Summary currentSummary;

    @FXML
    private ListView<String> taskListView;

    @FXML
    private Button bindTask, removeTask, addNewTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        initializeList();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    private void initializeList() {
        taskListView.setItems(Model.getModel().getTaskObservableList());
        taskListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        taskListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!taskListView.getSelectionModel().isEmpty()) {
                Model.getModel().setCurrentTask(Model.getModel().getCurrentTaskByIndex(newIndex));
            }
        }));
    }

    private void initializeButtons() {
        Task tempTask = Model.getModel().getCurrentTask();
        bindTask.setOnAction((ActionEvent e) -> {
            taskBinder(tempTask);
        });
        removeTask.setOnAction((ActionEvent e) -> {
            taskRemover(tempTask);
        });
        addNewTask.setOnAction((ActionEvent e) -> {
            taskAdder();
        });
        System.out.print("Initialized SEC Buttons!");
    }

    private void taskBinder(Task task) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TaskBinder.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 350, 400);
            Stage stage = new Stage();
            TaskBinderController taskBinderController = fxmlLoader.getController();
            taskBinderController.setStage(stage);
            stage.setTitle("Task Binder");
            taskBinderController.setCurrentTask(task);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void taskRemover(Task task) {
        System.out.println("remove that task");
    }

    private void taskAdder() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TaskAdder.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 350, 400);
            Stage stage = new Stage();
            TaskAdderController taskAdderController = fxmlLoader.getController();
            taskAdderController.setStage(stage);
            stage.setTitle("Add New Task");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Summary getCurrentSummary() {
        return currentSummary;
    }

    void setCurrentSummary(Summary currentSummary) {
        this.currentSummary = currentSummary;
    }
}
