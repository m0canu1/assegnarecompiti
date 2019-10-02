import classfiles.Recipe;
import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskAdderController implements Initializable {

    public Stage stage;

    @FXML
    private ListView<String> recipeListView;

    @FXML
    private Button selectRecipe;

    @FXML
    private Button bindToCook;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeList();
        initializeButtons();
    }

    public void setStage(Stage stage) { this.stage = stage; }

    private void initializeList() {
        recipeListView.setItems(Model.getModel().getRecipeObservableList());
        recipeListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recipeListView.getSelectionModel().selectedIndexProperty().addListener(((obsValue, oldValue, newValue) -> {
            int newIndex = (int) newValue;
            if (!recipeListView.getSelectionModel().isEmpty()) {
                Model.getModel().setCurrentRecipe(Model.getModel().getCurrentRecipeByIndex(newIndex));
            }
        }));
    }

    private void initializeButtons() {
        Recipe tempRecipe = Model.getModel().getCurrentRecipe();
        selectRecipe.setOnAction((ActionEvent e) -> {
            System.out.println("Add a new task with selected recipe");
        });
        bindToCook.setOnAction((ActionEvent e) -> {
            System.out.println("Add new task and then open binder");
        });
    }
}
