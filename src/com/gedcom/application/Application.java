package com.gedcom.application;

import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.file.GedcomFileReader;
import com.gedcom.processor.GedcomProcessor;

import java.text.ParseException;
import java.util.*;

/**
 * Created by Meghana on 9/12/2019.
 */
public class Application {
    public static void main(String[] args) throws ParseException {
        GedcomFileReader gfr = new GedcomFileReader();
        List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/AGILE_SPRINT_1_SSHERKAR.ged");
//        List<String> gedcomLines = gfr.gedcomReader("./PROJECT_1.ged");
        HashSet<String> tagSet = new HashSet<String>();
        List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

        for (String tagName: tagNames ) {
            tagSet.add(tagName);
        }
        GedcomProcessor gdp = new GedcomProcessor();
        GedcomResponse response= gdp.parser(gedcomLines,tagSet);
        IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        gdp.printIndividuals(indiFamilyResponse.getIndividualList());
        gdp.printFamily(indiFamilyResponse.getFamilyList());

        System.out.println("Printing Ambiguos Family List");
        gdp.printFamily(indiFamilyResponse.getAmbiguosFamilyMarrDeathList());
        gdp.printFamily(indiFamilyResponse.getAmbiguousFamilyMarrDivList());
        
        System.out.println("Printing List of Individuals with age less than 150");
        gdp.printIndividualsWithAgeLessThan150(indiFamilyResponse.getIndividualList());
        
        gdp.printListOfIndividualsBornBeforeParentsMarriage(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());

    }
}
