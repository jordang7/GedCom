package com.gedcom.application;

import com.gedcom.models.Family;
import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.file.GedcomFileReader;
import com.gedcom.models.Individual;
import com.gedcom.processor.GedcomProcessor;
import com.gedcom.processor.GedcomValidator;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Meghana on 9/12/2019.
 */
public class Application {
    public static void main(String[] args) throws ParseException, java.text.ParseException {
        GedcomFileReader gfr = new GedcomFileReader();
        List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
//        List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/AGILE_SPRINT_1_SSHERKAR.ged");
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

        System.out.println("---- GEDCOM ERRORS -----");

        gdp.printMarriageBeforeDeathError(indiFamilyResponse);
        gdp.printMarriageBeforeDivorceError(indiFamilyResponse);
        
        
        gdp.printIndividualsWithAgeLessThan150(indiFamilyResponse.getIndividualList()); 
        gdp.printListOfIndividualsBornBeforeParentsMarriage(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());

    }

}
