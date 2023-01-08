package demo.rainfallanalysis_beta.rainfallanalyser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import demo.rainfallanalysis_beta.rainfallanalyser.textio.TextIO;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RainfallAnalyser {

    //analyse dataset
    //core function
    public static Boolean analyseData(File file){
        //open csv file
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            //one line record
            String[] lineInArray;
            ArrayList<String[]> records = new ArrayList<>();

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
                if (lineInArray[0].equalsIgnoreCase("product code") || lineInArray[0].equalsIgnoreCase("year")) {
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
                //var rainfall = lineInArray.length < 6 ? 0 : Double.parseDouble(lineInArray[5]);

                if ((month < 1 || month > 12) || (day < 1 || day > 31)) {
                    System.out.println("ERROR: failed to process file");
                    return false;
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
            System.out.println("Records save!");
            return true;

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return false;
    }


    //updated writeRecord function
    private static String[] writeRecord(double totalRainfall, double minRainfall, double maxRainfall, int currentMonth, int year) {
        return new String[]{String.format("%d,%d,%1.2f,%1.2f,%1.2f%s", year, currentMonth,
                totalRainfall, minRainfall, maxRainfall, System.lineSeparator())};
    }

}
