import classfiles.Recipe;
import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskAdderController implements Initializable {

    public Stage stage;

    private Recipe tempRecipe;

    @FXML
    private ListView<String> recipeListView;

    @FXML
    private Button selectRecipe;

    @FXML
 //   private Button bindToCook;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeList();
        initializeButtons();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public Stage getStage(){
        return this.stage;
    }

    private void initializeList() {
        recipeListView.setItems(Model.getModel().getRecipeObservableList());
        recipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recipeListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!recipeListView.getSelectionModel().isEmpty()) {
                System.out.println("Selected recipe");
                Model.getModel().setCurrentRecipe(Model.getModel().getCurrentRecipeByIndex(newIndex));
            }
        }));
    }

    private void initializeButtons() {

        selectRecipe.setOnAction((ActionEvent e) -> {
            tempRecipe = Model.getModel().getCurrentRecipe();
            Task newTask = new Task(tempRecipe);
            Model.getModel().getCurrentEvent().addTask(newTask);
            System.out.println("Added a new task with selected recipe");
            Stage stage = (Stage) selectRecipe.getScene().getWindow();
            System.out.println(stage);
          //  stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
          //  getStage().fireEvent();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
        /*bindToCook.setOnAction((ActionEvent e) -> {
            tempRecipe = Model.getModel().getCurrentRecipe();
            System.out.println("Add new task and then open binder");
        });*/
    }
}
