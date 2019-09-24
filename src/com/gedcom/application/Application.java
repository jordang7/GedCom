package com.gedcom.application;

import com.gedcom.file.GedcomFileReader;
import com.gedcom.processor.GedcomProcessor;
import java.util.*;

/**
 * Created by Meghana on 9/12/2019.
 */
public class Application {
    public static void main(String[] args) {
        GedcomFileReader gfr = new GedcomFileReader();
        List<String> gedcomLines = gfr.gedcomReader(args[0]);



        HashSet<String> tagSet = new HashSet<String>();
        List<String> tagNames = gfr.gedcomReader(args[1]);

        for (String tagName: tagNames ) {
            tagSet.add(tagName);
        }
        GedcomProcessor gdp = new GedcomProcessor();
        gdp.parser(gedcomLines,tagSet);

    }
}
