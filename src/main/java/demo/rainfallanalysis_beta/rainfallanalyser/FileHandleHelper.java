package demo.rainfallanalysis_beta.rainfallanalyser;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public class FileHandleHelper {

    //original file
    private static Hashtable<String, File> files = new Hashtable<>();

    //calculated file
    private static Hashtable<String, OutputStream> calculatedFiles = new Hashtable<>();


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

    public static Boolean saveStream(String name, OutputStream stream) {
        calculatedFiles.put(name, stream);
        return true;
    }





}
