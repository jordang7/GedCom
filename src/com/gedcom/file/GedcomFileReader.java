package com.gedcom.file;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meghana on 9/12/2019.
 */

public class GedcomFileReader {
    public List<String> gedcomReader(String filePath) {
        List<String> gedcomeLines = new ArrayList<>();
        try  {

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                gedcomeLines.add( sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gedcomeLines;
    }
}