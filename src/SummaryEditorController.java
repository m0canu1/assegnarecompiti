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
    Task tempTask;


    @FXML
    private ListView<String> taskListView;

    @FXML
    private Button bindTask, removeTask, addNewTask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Model.getModel().updateTaskObservableList();
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
                System.out.println("New task selected" + newIndex);
                Model.getModel().getCurrentEvent().setCurrentTask(Model.getModel().getCurrentEvent().getCurrentTaskByIndex(newIndex));
                System.out.println("Current task: " + Model.getModel().getCurrentEvent().getCurrentTaskByIndex(newIndex).getName());
            }
        }));
    }

    private void initializeButtons() {
        bindTask.setOnAction((ActionEvent e) -> {
            tempTask = Model.getModel().getCurrentEvent().getCurrentTask();
            taskBinder(tempTask);
        });
        removeTask.setOnAction((ActionEvent e) -> {
            tempTask = Model.getModel().getCurrentEvent().getCurrentTask();
            taskRemover(tempTask);
        });
        addNewTask.setOnAction((ActionEvent e) -> {
            tempTask = Model.getModel().getCurrentEvent().getCurrentTask();
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
        System.out.println("remove that task" + task.getName());
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
            stage.setOnCloseRequest(windowEvent -> taskListView.setItems(Model.getModel().getCurrentEvent().getTaskListAsString()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        // cleanup code here...
        System.out.println("\n\nCHIUSO SUMMARY EDIT");
//        Model.getModel().updateTaskObservableList();
//        taskListView.getItems().clear();
        // note that typically (i.e. if Platform.isImplicitExit() is true, which is the default)
        // closing the last open window will invoke Platform.exit() anyway
//        Platform.exit();
    }

}
