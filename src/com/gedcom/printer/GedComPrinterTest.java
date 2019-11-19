package com.gedcom.printer;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

import com.gedcom.models.Family;
import com.gedcom.models.Individual;

import com.gedcom.printer.GedcomPrinter;
import com.gedcom.processor.GedcomValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GedComPrinterTest {
	
	GedcomPrinter printer = new GedcomPrinter();
	 @org.junit.jupiter.api.BeforeEach
	    void setUp() {
	    }

	    @org.junit.jupiter.api.AfterEach
	    void tearDown() {
	    }
	@org.junit.jupiter.api.Test
	public void testPrintListOfUpcAnniversary() throws ParseException {
		 Family fam1 = new Family("id1");
	     fam1.setHusbandId("H1");
	     fam1.setWifeId("W1");
	     fam1.setMarried(LocalDate.parse("5 MAY 2014 ",GedcomValidator.formatter));
	     List<Family> familyList = new ArrayList<>();
	     familyList.add(fam1);
	     
	     Family fam2 = new Family("id2");
	     fam2.setHusbandId("H2");
	     fam2.setWifeId("W2");
	     fam2.setHusbandName("jack");
	     fam2.setWifeName("jill");
	     familyList.add(fam2);
	     fam1.setMarried(LocalDate.parse("1 DEC 2010 ",GedcomValidator.formatter));
	     List<String>  upc= new ArrayList<>();
	     upc.add("id2");
	     List<String> correct= printer.printListOfUpcAnniversary(familyList);
	     assertEquals(correct.size(),upc.size());
	     
	}
	@org.junit.jupiter.api.Test
	public void testPrintListOfUpcBDay() throws ParseException {
	     List<Individual> individualList = new ArrayList<>();
	     
	     Individual h1 = new Individual("H1");
         h1.setBdate(LocalDate.parse("31 DEC 2018 ", GedcomValidator.formatter));
         individualList.add(h1);
	        
         Individual h2 = new Individual("H2");
         h1.setBdate(LocalDate.parse("24 DEC 2018 ", GedcomValidator.formatter));
         individualList.add(h2);
         
	     List<String>  upc= new ArrayList<>();
	     upc.add("H2");
	     List<String> correct= printer.printListOfUpcBday(individualList);
	     assertEquals(correct.size(),upc.size());
	     
	}

}
