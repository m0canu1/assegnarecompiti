import classfiles.Task;
import com.sun.webkit.Timer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SummaryEditorController implements Initializable {

    private Stage stage;
    private Task tempTask;


    @FXML
    private ListView<String> taskListView;

    @FXML
    private Button bindTask, removeTask, addNewTask, orderTasks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeButtons();
        initializeList();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    private void initializeList() {
        taskListView.setItems(Model.getModel().getCurrentEvent().getTaskListAsString());
        taskListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        taskListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!taskListView.getSelectionModel().isEmpty()) {
//                System.out.println("New task selected" + newIndex);
                Model.getModel().getCurrentEvent().setCurrentTask(Model.getModel().getCurrentEvent().getCurrentTaskByIndex(newIndex));
//                System.out.println("Current task: " + Model.getModel().getCurrentEvent().getCurrentTaskByIndex(newIndex).getName());
            }
        }));

        taskListView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.DELETE) {
                taskRemover(Model.getModel().getCurrentEvent().getCurrentTask());
            } else if (keyEvent.getCode() == KeyCode.ENTER) {
                if (!taskListView.getSelectionModel().isEmpty())
                    taskBinder();
                else
                    taskAdder();
            }
        });
    }

    private void initializeButtons() {
        bindTask.setOnAction((ActionEvent e) -> {
            tempTask = Model.getModel().getCurrentEvent().getCurrentTask();
            taskBinder();
        });
        removeTask.setOnAction((ActionEvent e) -> {
            tempTask = Model.getModel().getCurrentEvent().getCurrentTask();
            taskRemover(tempTask);
        });
        addNewTask.setOnAction((ActionEvent e) -> {
            tempTask = Model.getModel().getCurrentEvent().getCurrentTask();
            taskAdder();
        });
        orderTasks.setOnAction((ActionEvent e) -> {
            Model.getModel().putTasksInOrder();
            taskListView.setItems(Model.getModel().getCurrentEvent().getTaskListAsString());
        });
    }

    private void taskBinder() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TaskBinder.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            TaskBinderController taskBinderController = fxmlLoader.getController();
            taskBinderController.setStage(stage);
            stage.setTitle("Task Binder per " + Model.getModel().getCurrentEvent().getCurrentTask().getName());
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(windowEvent -> taskListView.setItems(Model.getModel().getCurrentEvent().getTaskListAsString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void taskRemover(Task task) {
//        Model.getModel().removeTaskFromView(task);
        Model.getModel().getCurrentEvent().deleteTask(task);
//        taskListView.setItems(Model.getModel().getTaskObservableList());
        taskListView.setItems(Model.getModel().getCurrentEvent().getTaskListAsString());
        Model.getModel().removeTask(task);
//        System.out.println("remove that task" + task.getName());
        Model.getModel().getCurrentEvent().setCurrentTask(null);
    }

    private void taskAdder() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("TaskAdder.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            stage = new Stage();
            TaskAdderController taskAdderController = fxmlLoader.getController();
            taskAdderController.setStage(stage);
            stage.setTitle("Add New Task");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(windowEvent -> taskListView.setItems(Model.getModel().getCurrentEvent().getTaskListAsString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
