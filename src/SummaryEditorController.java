import classfiles.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SummaryEditorController implements Initializable {

    private Stage stage;
    private Summary currentSummary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    public Summary getCurrentSummary() {
        return currentSummary;
    }

    void setCurrentSummary(Summary currentSummary) {
        this.currentSummary = currentSummary;
    }
}
