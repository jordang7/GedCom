import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.gedcom.file.GedcomFileReader;
import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.processor.GedcomProcessor;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class Sprint_1_Test_Cases_Shrikant {

	
	@Test
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
        
        gdp.printIndividualsWithAgeLessThan150(indiFamilyResponse.getIndividualList()); 
		fail("Not yet implemented");
	}
	
	@Test
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
        
        gdp.printListOfIndividualsBornBeforeParentsMarriage(indiFamilyResponse.getFamilyList(), indiFamilyResponse.getIndividualList());
		fail("Not yet implemented");
	}

}
