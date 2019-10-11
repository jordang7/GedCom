package com.gedcom.application;

import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.file.GedcomFileReader;
import com.gedcom.processor.GedcomProcessor;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.util.*;

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

        gdp.printBirthBeforeDeathError(indiFamilyResponse);
        gdp.printBirthBeforeMarriageError(indiFamilyResponse);
        gdp.printListOfIndividualsBornBeforeParentsMarriage(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());
        gdp.printIndividualsWithAgeMoreThan150(indiFamilyResponse.getIndividualList());
        gdp.printMarriageBefore14Error(indiFamilyResponse);
   gdp.printIndividualsWithDivorceBeforeDeath(indiFamilyResponse);
       gdp.printIndividualsWithBirthBeforeCurrentData(indiFamilyResponse.getIndividualList());    }

}
