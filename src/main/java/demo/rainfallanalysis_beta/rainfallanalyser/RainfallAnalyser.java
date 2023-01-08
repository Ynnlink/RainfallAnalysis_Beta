package demo.rainfallanalysis_beta.rainfallanalyser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import demo.rainfallanalysis_beta.rainfallanalyser.textio.TextIO;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class RainfallAnalyser {
    public static void main(String[] args) {
        System.out.print("Enter path name: ");
        var path = TextIO.getln();

        try {
            TextIO.readFile(path);
            String savePath = generateSavePath(path);
            analyseDataset(savePath);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: failed to process file");
        }
    }

    //generic function to handle file open exceptions
    static String processData(String path) {
        String savePath = null;
        try {
            TextIO.readFile(path);
            savePath = RainfallAnalyser.generateSavePath(path);
            String flag = RainfallAnalyser.analyseDataset(savePath);
            if (flag.equals("empty") || flag.equals("corrupt")) {
                return "fail";
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("ERROR: failed to process file");
        }
        return savePath;
    }

    private static String generateSavePath(String path) {
        var pathElements = path.trim().split("/");
        var filenameElements = pathElements[2].trim().split("\\.");
        return String.format("%s/%s_analysed.%s", pathElements[0],
                filenameElements[0], filenameElements[1]);
    }

    private static String analyseDataset(String savePath) {
        var header = extractRecord(); // ignore header record

        if (header == null) {
            //throw new IllegalArgumentException("ERROR: file is empty");
            System.out.println("ERROR: file is empty");
            return "empty";
        }

        // setup accumulation
        var totalRainfall = 0.0;
        var minRainfall = Double.POSITIVE_INFINITY;
        var maxRainfall = 0.0;

        var currentMonth = 1; // sentinel
        var currentYear = 0; // sentinel

        var record = extractRecord(); // get record

        // setup output file
        TextIO.writeFile(savePath);

        // write header record
        TextIO.putln("year,month,total,min,max");

        // create new records for save file
        while (record != null) {
            // convert important fields to correct data types
            var year = Integer.parseInt(record[2]);
            var month = Integer.parseInt(record[3]);
            var day = Integer.parseInt(record[4]);
            var rainfall = record.length < 6 ? 0 : Double.parseDouble(record[5]);

            if ((month < 1 || month > 12) || (day < 1 || day > 31)) {
                System.out.println("ERROR: failed to process file");
                return "corrupt";
            }

            if (month != currentMonth) {

                // new month detected - save record and reset accumulation
                writeRecord(totalRainfall, minRainfall, maxRainfall, currentMonth, currentYear == 0 ? year : currentYear);

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

            record = extractRecord(); // get record
        }

        if (currentMonth < 12) {
            // last month is incomplete - save record
            writeRecord(totalRainfall, minRainfall, maxRainfall, currentMonth, currentYear);
        }
        return "success";
    }

    //analyse dataset
    public static Boolean analyseData(File file){

        //open csv file
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                //analyse dataset and save records


                System.out.println(Arrays.toString(lineInArray));
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }



        return true;
    }



    private static void writeRecord(double totalRainfall, double minRainfall, double maxRainfall, int currentMonth, int year) {
        var newRecord = String.format("%d,%d,%1.2f,%1.2f,%1.2f%s", year, currentMonth,
                totalRainfall, minRainfall, maxRainfall, System.lineSeparator());
        TextIO.putf(newRecord);
    }

    private static String[] extractRecord() {
        if (TextIO.eof()) return null; // convert EOF to null

        var text = TextIO.getln();
        return text.trim().split(",");
    }
}
