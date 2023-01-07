package demo.rainfallanalysis_beta;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VisualiserController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}