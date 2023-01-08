package demo.rainfallanalysis_beta.rainfallanalyser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class FileHandleHelper {

    //original file
    private static Hashtable<String, File> files = new Hashtable<>();

    //calculated file
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

    public static int getSize() {
        return files.size();
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

    public static void test() {
        //data structure
        //String[] test = new String[0];
        //records.add(test);

    }




}
