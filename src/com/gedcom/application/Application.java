package com.gedcom.application;

import com.gedcom.models.FamilyWithAnomaly;
import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.file.GedcomFileReader;
import com.gedcom.models.Individual;
import com.gedcom.printer.GedcomPrinter;
import com.gedcom.processor.GedcomProcessor;
import java.text.ParseException;

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
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
        gedcomPrinter.setGedcomLines(gedcomLines);
        

        gedcomPrinter.printIndividuals(indiFamilyResponse.getIndividualList());
        gedcomPrinter.printFamily(indiFamilyResponse.getFamilyList());



        System.out.println("---- GEDCOM ERRORS -----");
        gedcomPrinter.printIndividualsWithBirthBeforeCurrentData(indiFamilyResponse.getFamilyList(),indiFamilyResponse.getIndividualList()); //US01
        gedcomPrinter.printBirthBeforeMarriageError(indiFamilyResponse);//US02
        gedcomPrinter.printBirthBeforeDeathError(indiFamilyResponse);//US03
        gedcomPrinter.printMarriageBeforeDivorceError(indiFamilyResponse);//US04
        gedcomPrinter.printMarriageBeforeDeathError(indiFamilyResponse);//US05
        gedcomPrinter.printIndividualsWithDivorceBeforeDeath(indiFamilyResponse);//US06
        gedcomPrinter.printIndividualsWithAgeMoreThan150(indiFamilyResponse.getIndividualList());//US07
        gedcomPrinter.printListOfIndividualsBornBeforeParentsMarriage(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());//US08
        gedcomPrinter.printListOfIndividualsBornAfterParentsDeath(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());//US09
        gedcomPrinter.printMarriageBefore14Error(indiFamilyResponse);//US10
        gedcomPrinter.printAmbiguousBigamyLIst(indiFamilyResponse);//US11
        gedcomPrinter.printFamilyWithOlderParents(indiFamilyResponse);//US12;
        gedcomPrinter.printSiblingSpacingErrors(indiFamilyResponse.getFamilyList(),indiFamilyResponse.getIndividualList()); //US13
        gedcomPrinter.printMultipleBirthsLessThan5Errors(indiFamilyResponse.getFamilyList()); // US14
        gedcomPrinter.printAmbiguousMoreThan15Children(indiFamilyResponse); //US15
        gedcomPrinter.printAmbiguosMaleLastNames(indiFamilyResponse);//US16
        gedcomPrinter.printAmbiguousParentDescendantMarriageList(indiFamilyResponse);//US17
        gedcomPrinter.printAmbiguosSiblingMarriageList(indiFamilyResponse);//US18
        gedcomPrinter.printAmbiguousFirstCousinsMarriageList(indiFamilyResponse); //US19

        gedcomPrinter.printAmbiguousAuntUncleNNList(indiFamilyResponse); //US20
        gedcomPrinter.printAmbiguousGenderForRoles(indiFamilyResponse); //US21
        gedcomPrinter.printambiguousIndividualId(indiFamilyResponse); // US22
        gedcomPrinter.printambiguousFamilyId(indiFamilyResponse); // US22

        gedcomPrinter.printCasesForUniqueNameAndBirthDate(indiFamilyResponse.getIndividualList()); //US23
        gedcomPrinter.printCasesForUniqueFamilyWithSpouses(indiFamilyResponse.getFamilyList()); //US24
        gedcomPrinter.printDuplicateFirstNameList(indiFamilyResponse); //US25
        gedcomPrinter.printMissingCorrespondingEntries(indiFamilyResponse); //US26

        System.out.println("---- GEDCOM LISTS ----");
        gedcomPrinter.printIndividualswithAge(indiFamilyResponse.getIndividualList()); //US27
        gedcomPrinter.printSiblingsByAge(indiFamilyResponse.getFamilyList()); //US28
        gedcomPrinter.printListOfDeceased(indiFamilyResponse.getIndividualList()); //US29
        gedcomPrinter.printListOfLivingMarried(indiFamilyResponse.getIndividualList(),indiFamilyResponse.getFamilyList()); //US30
        gedcomPrinter.printLivingSingle(indiFamilyResponse);//US31
        gedcomPrinter.printMultipleBirthsList(indiFamilyResponse.getFamilyList());//US32
        gedcomPrinter.printOrphanChildren(indiFamilyResponse);//US33
        gedcomPrinter.printLargreAgeDifferences(indiFamilyResponse); //US34
        gedcomPrinter.printPeopleWhoAreBornInLast30Days(indiFamilyResponse);//US35
        gedcomPrinter.printPeopleWhoDiedInLast30Days(indiFamilyResponse); //US36
        gedcomPrinter.printListOfUpcBday(indiFamilyResponse.getIndividualList()); //US38
        gedcomPrinter.printListOfUpcAnniversary(indiFamilyResponse.getFamilyList()); //US39
        


    	}


}
