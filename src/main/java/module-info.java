module demo.rainfallanalysis_beta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.opencsv;

    opens demo.rainfallanalysis_beta to javafx.fxml;
    exports demo.rainfallanalysis_beta;
}