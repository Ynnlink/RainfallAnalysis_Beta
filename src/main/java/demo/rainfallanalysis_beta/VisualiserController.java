package demo.rainfallanalysis_beta;

import demo.rainfallanalysis_beta.rainfallanalyser.FileHandleHelper;
import demo.rainfallanalysis_beta.rainfallanalyser.RainfallAnalyser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.*;
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

    @FXML
    private CategoryAxis barChartXAxis;

    @FXML
    private NumberAxis barChartYAxis;

    //indicate current file
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
        //diagnose
        //System.out.println("File size: " + FileHandleHelper.getSize());
    }

    @FXML
    protected void onSelect() {
        //update select option
        option = fileList.getSelectionModel().getSelectedItem().toString();
        //flag the current file
        flag = flag - 1;
        //diagnose
        //System.out.println("file list index: " + fileList.getSelectionModel().getSelectedIndex());
        //System.out.println("file list selected option: " + fileList.getSelectionModel().getSelectedItem());
    }

    @FXML
    protected void onCalculateButtonClick() {

        //check option variable
        if (option == null) {
            //no file is selected
            showErrorMessage("Please select a file!");

        } else {
            if (fileList.getSelectionModel().getSelectedItem().equals(option) && flag == 0) {
                showErrorMessage("Already calculated!");

            } else {
                //flag the current file
                flag = flag + 1;
                try {
                    //open csv file
                    String condition = RainfallAnalyser.analyseData(FileHandleHelper.getNewFile(option));
                    switch (condition) {
                        case "corrupt" -> showErrorMessage("File is corrupt!");
                        case "unsupported" -> showErrorMessage("Unsupported file format!");
                        case "fail" -> showErrorMessage("Unknown error!");
                        default -> {
                            //collect data and draw bar chart
                            barChartXAxis.setLabel("Year");
                            barChartYAxis.setLabel("Units = mm");
                            //fill in categories
                            barChartXAxis.setCategories(FXCollections.observableArrayList(RainfallAnalyser.getAxisYears(option)));
                            //replacing bar chart data
                            barChart.setData(FXCollections.observableArrayList(RainfallAnalyser.organizeStaticalData(option)));
                            barChart.setTitle(option.trim().split("\\.")[0] + " Rainfall Analysis");
                            barChart.setBarGap(0.5);
                            //diagnose
                            //System.out.println("A correct file is open!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}