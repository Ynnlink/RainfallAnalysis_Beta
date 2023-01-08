package demo.rainfallanalysis_beta.rainfallanalyser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.scene.chart.XYChart;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RainfallAnalyser {

    /**
     * analyse dataset, core function
     * @param file
     * @return
     * unsupported: unsupported file format
     * corrupt: corrupt csv file
     * fail: unknown failure
     * success: success condition
     */
    public static String analyseData(File file){
        //open csv file
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            //one line record each
            String[] lineInArray;
            ArrayList<String[]> records = new ArrayList<>();

            //check header
            if ((lineInArray = reader.readNext()) != null) {
                //unsupported file
                if (!lineInArray[0].equalsIgnoreCase("product code")) {
                    return "unsupported";
                }
            }

            //analyse dataset and save records
            //data structure for lineInArray:
            //[Product code, Bureau of Meteorology station number, Year, Month, Day, Rainfall amount (millimetres), Period over which rainfall was measured (days), Quality]
            //[IDCJAC0009, 31205, 2001, 04, 30, 11.0, 1, N]

            //data structure after processing (same as alpha version)
            //[year, month, total, min, max]
            //[2000,2, 1380.8, 0, 209.4]

            // setup accumulation
            var totalRainfall = 0.0;
            var minRainfall = Double.POSITIVE_INFINITY;
            var maxRainfall = 0.0;

            var currentMonth = 1; // sentinel
            var currentYear = 0; // sentinel

            while ((lineInArray = reader.readNext()) != null) {

                //ignore header
                if (lineInArray[0].equalsIgnoreCase("product code")) {
                    continue;
                }

                // convert important fields to correct data types
                var year = Integer.parseInt(lineInArray[2]);
                var month = Integer.parseInt(lineInArray[3]);
                var day = Integer.parseInt(lineInArray[4]);

                //update check condition
                var rainfall = 0.0;
                if (!lineInArray[5].equals("")) {
                    rainfall = Double.parseDouble(lineInArray[5]);
                }

                //file has corrupt record
                if ((month < 1 || month > 12) || (day < 1 || day > 31)) {
                    System.out.println("ERROR: failed to process file");
                    return "corrupt";
                }

                if (month != currentMonth) {
                    // new month detected - save record and reset accumulation
                    records.add(writeRecord(totalRainfall, minRainfall, maxRainfall, currentMonth, currentYear == 0 ? year : currentYear));

                    currentMonth = month;
                    currentYear = year;
                    totalRainfall = 0;
                    minRainfall = Double.POSITIVE_INFINITY;
                    maxRainfall = 0;
                    continue;
                }

                // update accumulation
                totalRainfall += rainfall;
                if (rainfall < minRainfall) minRainfall = rainfall;
                if (rainfall > maxRainfall) maxRainfall = rainfall;

            }

            if (currentMonth < 12) {
                // last month is incomplete - save record
                records.add(writeRecord(totalRainfall, minRainfall, maxRainfall, currentMonth, currentYear));
            }

            //save updated records
            FileHandleHelper.saveRecords(file.getName(), records);
            System.out.println("Records save successfully!");
            return "success";

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static ArrayList<String> getAxisYears(String filename) {
        ArrayList<String> years = new ArrayList<>();
        //get all records
        ArrayList<String[]> allRecords = new ArrayList<>(FileHandleHelper.getRecords(filename));
        for (String[] record: allRecords) {
            //axis year
            if (!years.contains(record[0])) {
                years.add(record[0]);
            }
        }
        return years;
    }

    public static ArrayList<XYChart.Series<String, Number>> organizeStaticalData(String filename) {

        //get all records
        ArrayList<String[]> allRecords = new ArrayList<>(FileHandleHelper.getRecords(filename));

        //creating data series using months
        //Jan Feb Mar Apr May June July Aug Sept Oct Nov Dec
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Jan");
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Feb");
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        series3.setName("Mar");
        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        series4.setName("Apr");
        XYChart.Series<String, Number> series5 = new XYChart.Series<>();
        series5.setName("May");
        XYChart.Series<String, Number> series6 = new XYChart.Series<>();
        series6.setName("June");
        XYChart.Series<String, Number> series7 = new XYChart.Series<>();
        series7.setName("July");
        XYChart.Series<String, Number> series8 = new XYChart.Series<>();
        series8.setName("Aug");
        XYChart.Series<String, Number> series9 = new XYChart.Series<>();
        series9.setName("Sept");
        XYChart.Series<String, Number> series10 = new XYChart.Series<>();
        series10.setName("Oct");
        XYChart.Series<String, Number> series11 = new XYChart.Series<>();
        series11.setName("Nov");
        XYChart.Series<String, Number> series12 = new XYChart.Series<>();
        series12.setName("Dec");

        //assign bar chart data
        for (String[] record: allRecords) {
            //extract data
            var year = record[0];
            var month = record[1];
            var monthlyTotal = Double.parseDouble(record[2]);

            //fill in data according to month
            switch (month) {
                case "1" -> series1.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "2" -> series2.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "3" -> series3.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "4" -> series4.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "5" -> series5.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "6" -> series6.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "7" -> series7.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "8" -> series8.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "9" -> series9.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "10" -> series10.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "11" -> series11.getData().add(new XYChart.Data<>(year, monthlyTotal));
                case "12" -> series12.getData().add(new XYChart.Data<>(year, monthlyTotal));
            }
        }

        //collect all the series data
        ArrayList<XYChart.Series<String, Number>> data = new ArrayList<>();
        data.add(series1);
        data.add(series2);
        data.add(series3);
        data.add(series4);
        data.add(series5);
        data.add(series6);
        data.add(series7);
        data.add(series8);
        data.add(series9);
        data.add(series10);
        data.add(series11);
        data.add(series12);
        return data;
    }

    //updated writeRecord function
    private static String[] writeRecord(double totalRainfall, double minRainfall, double maxRainfall, int currentMonth, int year) {
        //format all the data
        String[] temp = new String[5];
        temp[0] = String.format("%d",year);
        temp[1] = String.format("%d", currentMonth);
        temp[2] = String.format("%1.2f", totalRainfall);
        temp[3] = String.format("%1.2f", minRainfall);
        temp[4] = String.format("%1.2f", maxRainfall);
        return temp;
    }

}
