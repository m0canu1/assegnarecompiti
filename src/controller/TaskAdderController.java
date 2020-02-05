package controller;

import classfiles.Recipe;
import classfiles.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskAdderController implements Initializable {

    public Label header;
    private Stage stage;

    private Recipe tempRecipe;

    @FXML
    private ListView<String> recipeListView;

    @FXML
    private Button selectRecipe, cancel;

    @FXML
 //   private Button bindToCook;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        header.setText("Add Task to " + Model.getModel().getCurrentEvent().getName());
        initializeList();
        initializeButtons();
    }

    void setStage(Stage stage) {
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
                Model.getModel().setCurrentRecipe(Model.getModel().getCurrentRecipeByIndex(newIndex));
            }
        }));

        recipeListView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                selectRecipe();
            }
        });
    }

    private void initializeButtons() {
        selectRecipe.setOnAction((ActionEvent e) -> selectRecipe());
        cancel.setOnAction((ActionEvent e) -> stage.close());
    }

    private void selectRecipe() {
        tempRecipe = Model.getModel().getCurrentRecipe();
        if (tempRecipe != null) {
            Task newTask = new Task(tempRecipe);
            stage = (Stage) selectRecipe.getScene().getWindow();
            Model.getModel().addTaskToEvent(newTask);
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }else{
            System.out.println("No recipe selected");
        }
    }
}
