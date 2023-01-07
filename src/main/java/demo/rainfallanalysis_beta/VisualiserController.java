package demo.rainfallanalysis_beta;

import demo.rainfallanalysis_beta.rainfallanalyser.FileHandleHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class VisualiserController {


    @FXML
    private ChoiceBox fileList;

    @FXML
    private BarChart barChart;

    private String option;

    //use integer to flag calculated file
    private int flag = 0;

    //handle error messages and show an alert box
    private void showErrorMessage(String error) {
        if (Platform.isFxApplicationThread()) {
            Stage s = new Stage();
            VBox v = new VBox();

            //create a new label element
            Label label = new Label(error);
            label.setFont(new Font("Arial", 20));
            label.setMinWidth(50);
            label.setMinHeight(50);
            v.getChildren().add(label);

            //run alert window
            Scene sc = new Scene(v,300,50);
            s.setTitle("Application Failure");
            s.setScene(sc);
            s.show();
        }
    }

    @FXML
    protected void onUploadButtonClick() {

        //open file choosing dialog to choose a CSV file
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);

        //check file variable
        if (file != null) {
            if (file.length() == 0) {
                //file is empty
                showErrorMessage("File is empty!");
            } else {
                //file is not empty
                if (FileHandleHelper.saveNewFile(file)) {
                    //successfully upload a file
                    fileList.getItems().add(file.getName());
                } else {
                    //file already exist, alert user
                    showErrorMessage("File already exist!");
                }
            }
        }

        System.out.println("File size: " + FileHandleHelper.getSize());

    }
    @FXML
    protected void onSelect() {

        option = fileList.getSelectionModel().getSelectedItem().toString();
        System.out.println("file list index: " + fileList.getSelectionModel().getSelectedIndex());
        System.out.println("file list selected option: " + fileList.getSelectionModel().getSelectedItem());

        //flag the current file
        flag = flag - 1;
    }
    @FXML
    protected void onCalculateButtonClick() {

        //check option variable
        if (option == null) {
            //no file is selected
            showErrorMessage("Please select a file!");
            return;
        } else {
            if (fileList.getSelectionModel().getSelectedItem().equals(option) && flag == 0) {
                showErrorMessage("Already calculated!");
                return;
            } else {
                //flag the current file
                flag = flag + 1;

                final String austria = "Austria";
                final String brazil = "Brazil";
                final String france = "France";
                final String italy = "Italy";
                final String usa = "USA";

                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("Country");
                yAxis.setLabel("Value");


                XYChart.Series series1 = new XYChart.Series();
                series1.setName("2003");
                series1.getData().add(new XYChart.Data(austria, 25601.34));
                series1.getData().add(new XYChart.Data(brazil, 20148.82));
                series1.getData().add(new XYChart.Data(france, 10000));
                series1.getData().add(new XYChart.Data(italy, 35407.15));
                series1.getData().add(new XYChart.Data(usa, 12000));

                XYChart.Series series2 = new XYChart.Series();
                series2.setName("2004");
                series2.getData().add(new XYChart.Data(austria, 57401.85));
                series2.getData().add(new XYChart.Data(brazil, 41941.19));
                series2.getData().add(new XYChart.Data(france, 45263.37));
                series2.getData().add(new XYChart.Data(italy, 117320.16));
                series2.getData().add(new XYChart.Data(usa, 14845.27));

                XYChart.Series series3 = new XYChart.Series();
                series3.setName("2005");
                series3.getData().add(new XYChart.Data(austria, 45000.65));
                series3.getData().add(new XYChart.Data(brazil, 44835.76));
                series3.getData().add(new XYChart.Data(france, 18722.18));
                series3.getData().add(new XYChart.Data(italy, 17557.31));
                series3.getData().add(new XYChart.Data(usa, 92633.68));

                //replacing bar chart values
                barChart.setData(FXCollections.observableArrayList(series1, series2, series3));

            }
        }
    }


}



