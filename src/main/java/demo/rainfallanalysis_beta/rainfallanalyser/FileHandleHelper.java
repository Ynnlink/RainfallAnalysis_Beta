package demo.rainfallanalysis_beta.rainfallanalyser;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class FileHandleHelper {

    //original files
    private static Hashtable<String, File> files = new Hashtable<>();

    //calculated data
    private static Hashtable<String, ArrayList<String[]>> calculatedRecords = new Hashtable<>();


    public static Boolean saveNewFile(File newFile) {
        if (files.containsKey(newFile.getName())) {
            //file already exist
            return false;
        } else {
            //add file to hash table
            files.put(newFile.getName(),newFile);
            return true;
        }
    }

    public static File getNewFile(String key) {
        return files.get(key);
    }

    public static Boolean saveRecords(String fileName,ArrayList<String[]> records) {

        if (calculatedRecords.containsKey(fileName)) {
            //already contains records
            return false;
        } else {
            //save records
            calculatedRecords.put(fileName, records);
            return true;
        }
    }

    public static ArrayList<String[]> getRecords(String fileName) {
        return calculatedRecords.get(fileName);
    }
}
