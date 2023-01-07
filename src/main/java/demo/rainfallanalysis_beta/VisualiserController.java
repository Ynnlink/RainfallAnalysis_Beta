package demo.rainfallanalysis_beta;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class VisualiserController {
    @FXML
    private Label welcomeText;

    @FXML
    private VBox vbox0;

    @FXML
    protected void onUploadButtonClick() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        System.out.println(file);





    }


}



