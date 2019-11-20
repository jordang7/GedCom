import java.util.HashSet;
import java.util.List;
import com.gedcom.file.GedcomFileReader;
import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.printer.GedcomPrinter;
import com.gedcom.processor.GedcomProcessor;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class Sprint_1_Test_Cases_Shrikant {


    @org.junit.jupiter.api.Test
	public void testprintListOfIndividualsBornBeforeParentsMarriage() {
		
		GedcomFileReader gfr = new GedcomFileReader();
        List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
        HashSet<String> tagSet = new HashSet<String>();
        List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

        for (String tagName: tagNames ) {
            tagSet.add(tagName);
        }
        GedcomProcessor gdp = new GedcomProcessor();
        GedcomResponse response= gdp.parser(gedcomLines,tagSet);
        IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();

        gedcomPrinter.printIndividualsWithAgeMoreThan150(indiFamilyResponse.getIndividualList());
		fail("Not yet implemented");
	}

    @org.junit.jupiter.api.Test
	public void testprintIndividualsWithAgeLessThan150() throws ParseException, java.text.ParseException {
		
		GedcomFileReader gfr = new GedcomFileReader();
        List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
        HashSet<String> tagSet = new HashSet<String>();
        List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

        for (String tagName: tagNames ) {
            tagSet.add(tagName);
        }
        GedcomProcessor gdp = new GedcomProcessor();
        GedcomResponse response= gdp.parser(gedcomLines,tagSet);
        IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
        gedcomPrinter.printListOfIndividualsBornBeforeParentsMarriage(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());
		fail("Not yet implemented");
	}

    @Test
   	public void testprintListOfIndividualsBornAfterParentsDeath() throws ParseException, java.text.ParseException {
   		
   		GedcomFileReader gfr = new GedcomFileReader();
           List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
           HashSet<String> tagSet = new HashSet<String>();
           List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

           for (String tagName: tagNames ) {
               tagSet.add(tagName);
           }
           GedcomProcessor gdp = new GedcomProcessor();
           GedcomResponse response= gdp.parser(gedcomLines,tagSet);
           IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
           gedcomPrinter.printListOfIndividualsBornAfterParentsDeath(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());
   		fail("Not yet implemented");
   	}
    
    
    @Test
   	public void testprintSiblingSpacingErrors() throws ParseException, java.text.ParseException {
   		
   		GedcomFileReader gfr = new GedcomFileReader();
           List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
           HashSet<String> tagSet = new HashSet<String>();
           List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

           for (String tagName: tagNames ) {
               tagSet.add(tagName);
           }
           GedcomProcessor gdp = new GedcomProcessor();
           GedcomResponse response= gdp.parser(gedcomLines,tagSet);
           IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
           gedcomPrinter.printSiblingSpacingErrors(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());
   		fail("Not yet implemented");
   	}
    
    
    
    @Test
   	public void testprintMultipleBirthsLessThan5Errors() throws ParseException, java.text.ParseException {
   		
   		GedcomFileReader gfr = new GedcomFileReader();
           List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
           HashSet<String> tagSet = new HashSet<String>();
           List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

           for (String tagName: tagNames ) {
               tagSet.add(tagName);
           }
           GedcomProcessor gdp = new GedcomProcessor();
           GedcomResponse response= gdp.parser(gedcomLines,tagSet);
           IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
           gedcomPrinter.printMultipleBirthsLessThan5Errors(indiFamilyResponse.getFamilyList());
   		fail("Not yet implemented");
   	}
    
    
    @Test
   	public void testCasesForUniqueNameAndBirthDate() throws ParseException, java.text.ParseException {
   		
   		GedcomFileReader gfr = new GedcomFileReader();
           List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
           HashSet<String> tagSet = new HashSet<String>();
           List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

           for (String tagName: tagNames ) {
               tagSet.add(tagName);
           }
           GedcomProcessor gdp = new GedcomProcessor();
           GedcomResponse response= gdp.parser(gedcomLines,tagSet);
           IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
           gedcomPrinter.printCasesForUniqueNameAndBirthDate(indiFamilyResponse.getIndividualList());
   		fail("Not yet implemented");
   	}
    
    
    @Test
   	public void testCasesForUniqueFamilyWithSpouses() throws ParseException, java.text.ParseException {
   		
   		GedcomFileReader gfr = new GedcomFileReader();
           List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
           HashSet<String> tagSet = new HashSet<String>();
           List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

           for (String tagName: tagNames ) {
               tagSet.add(tagName);
           }
           GedcomProcessor gdp = new GedcomProcessor();
           GedcomResponse response= gdp.parser(gedcomLines,tagSet);
           IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
           gedcomPrinter.printCasesForUniqueFamilyWithSpouses(indiFamilyResponse.getFamilyList());
   		fail("Not yet implemented");
   	}
    
    @Test
   	public void printMultipleBirthsList() throws ParseException, java.text.ParseException {
   		
   		GedcomFileReader gfr = new GedcomFileReader();
           List<String> gedcomLines = gfr.gedcomReader("./gedcomfiles/Test.ged");
           HashSet<String> tagSet = new HashSet<String>();
           List<String> tagNames = gfr.gedcomReader("./gedcomfiles/TagSet.txt");

           for (String tagName: tagNames ) {
               tagSet.add(tagName);
           }
           GedcomProcessor gdp = new GedcomProcessor();
           GedcomResponse response= gdp.parser(gedcomLines,tagSet);
           IndiFamilyResponse indiFamilyResponse= gdp.createIndiAndFamilyList(response.getValidLines());
        GedcomPrinter gedcomPrinter = new GedcomPrinter();
           gedcomPrinter.printMultipleBirthsList(indiFamilyResponse.getFamilyList());
   		fail("Not yet implemented");
   	}
    
    
}
