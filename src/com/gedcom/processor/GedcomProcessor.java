package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.models.Individual;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.gedcom.processor.GedcomValidator.formatter;

/**
 * Created by Meghana on 9/12/2019.
 */

public class GedcomProcessor {
    public GedcomResponse parser(List<String> gedcomLines, HashSet<String> tagSet) {


        List<String> resultList = new ArrayList<>();
        List<String> errorList = new ArrayList<>();
        for (String gedcomLine : gedcomLines) {
//            System.out.println("-->" + gedcomLine);
            String parsedLine;
            String errorLine;
            String gedComSplit[] = gedcomLine.split(" ");
            if (gedComSplit.length >= 2) {
                if (tagSet.contains(gedComSplit[1])) {
                    if (isNameAndDateValid(gedComSplit)) {
                        parsedLine = getGedcomLine(gedComSplit, "Y");
                        resultList.add(gedcomLine);
                    } else if (gedComSplit[1].equals("DATE") || gedComSplit[1].equals("NAME")) {
                        parsedLine = getGedcomLine(gedComSplit, "N");
                        errorList.add(gedcomLine);
                    } else {
                        parsedLine = getGedcomLine(gedComSplit, "Y");
                        resultList.add(gedcomLine);
                    }
                } else if (isIndiOrFam(gedComSplit)) {
                    parsedLine = (gedComSplit[0] + "|" + gedComSplit[2] + "|Y|" + gedComSplit[1]);
                    resultList.add(gedcomLine);
                } else {
                    if (gedComSplit.length <= 2) {
                        parsedLine = (gedComSplit[0] + "|N|" + gedComSplit[1]);
                        errorList.add(gedcomLine);
                    } else {
                        parsedLine = (gedComSplit[0] + "|" + gedComSplit[2] + "|N|" + gedComSplit[1]);
                        errorList.add(gedcomLine);
                    }

                }

            } else {
                parsedLine = gedComSplit[0] + "|N|";
                errorList.add(gedcomLine);
            }

//            System.out.println("<--" + parsedLine);

        }
        return new GedcomResponse(resultList, errorList);
    }

    private boolean isIndiOrFam(String[] gedComSplit) {
        return gedComSplit[0].equals("0") && (gedComSplit[2].equals("INDI") || gedComSplit[2].equals("FAM"));
    }

    private String getGedcomLine(String[] gedComSplit, String valid) {
        return gedComSplit[0] + "|" + gedComSplit[1] + "|" + valid + "|" + String.join(" ", Arrays.copyOfRange(gedComSplit, 2, gedComSplit.length));
    }

    private boolean isNameAndDateValid(String[] gedComSplit) {
        return (gedComSplit[1].equals("DATE") && gedComSplit[0].equals("2")) ||
                gedComSplit[1].equals("NAME") && gedComSplit[0].equals("1");
    }

    public IndiFamilyResponse createIndiAndFamilyList(List<String> validLines) {
        IndiFamilyResponse response = new IndiFamilyResponse();
        Individual indi = null;
        Family family = null;
        boolean indiStarted = false;
        boolean isBirthDay = false;
        boolean isDeathDay = false;
        boolean familyStarted = false;
        boolean isMarried = false;
        boolean isDivorced = false;
        List<Individual> individualList = new ArrayList<>();
        List<Family> familyArrayList = new ArrayList<>();

        try {
            for (String gedcomLine : validLines) {
                //    System.out.println("-->" + gedcomLine);
                String parsedLine;
                //  String gedComSplit[] = gedcomLine.split(" ");


                String[] splitted = gedcomLine.split(" ");
                if (splitted[1].contentEquals("BIRT")) {
                    isBirthDay = true;
                }

                if (splitted[1].contentEquals("DEAT") && splitted[2].contentEquals("Y")) {
                    isDeathDay = true;
                }

                if (splitted[1].contentEquals("MARR")) {
                    isMarried = true;
                }
                if (splitted[1].contentEquals("DIV")) {
                    isDivorced = true;
                }


                if (splitted.length > 2) {
                    if (splitted[2].contentEquals("INDI")) {
                        indi = new Individual(splitted[1]);

                        indiStarted = true;
                    } else {
                        if (indiStarted == true) {
                            if (splitted[1].contentEquals("NAME")) {

                                for (int j = 2; j < splitted.length; j++) {
                                    indi.setName(indi.getName() + " " + splitted[j]);
                                }

                            }

                            if (splitted[1].contentEquals("SEX")) {
                                indi.setGender(splitted[2]);
                            }

                            if (splitted[1].contentEquals("DATE")) {
                                if (isBirthDay == true) {
                                    StringBuilder birthDay = new StringBuilder();
                                    for (int j = 2; j < splitted.length; j++) {
                                        birthDay.append(splitted[j] + " ");

                                    }
                                    indi.setBdate( Optional.of(LocalDate.parse(birthDay, GedcomValidator.formatter)));
                                    isBirthDay = false;

                                    int age = 0;

                                    if( indi.getDeathDate().isPresent() ){
                                        age = Period.between(indi.getBdate().get(), indi.getDeathDate().get()).getYears();
                                    } else {
                                        age = Period.between(indi.getBdate().get(), LocalDate.now()).getYears();
                                    }
                                    indi.setAge(age);
                                }
                                if (isDeathDay == true) {
                                    StringBuilder deathDay = new StringBuilder();

                                    for (int j = 2; j < splitted.length; j++) {
                                        deathDay.append(splitted[j] + " ");

                                    }
                                    indi.setDeathDate( Optional.of( LocalDate.parse( deathDay.toString(), GedcomValidator.formatter) ));
                                    //indi.setDeath(indi.getDeath() + " " + splitted[j]);
                                    isDeathDay = false;

                                    int age = 0;
                                    if( indi.getDeathDate().isPresent() ){
                                        age = Period.between(indi.getBdate().get(), indi.getDeathDate().get()).getYears();
                                    }
                                    indi.setAge(age);
                                }
                            }

                            if (splitted[1].contentEquals("CHIL")) {
                                indi.setChild(splitted[2]);
                            }

                            if (splitted[1].contentEquals("CHIL")) {
                                indi.setChild(splitted[2]);
                            }

                            if ((splitted[1].contentEquals("FAMC") || splitted[1].contentEquals("FAMS")) && indiStarted == true) {
                                if (splitted[1].contentEquals("FAMS")) {
                                    indi.setChild(splitted[2]);
                                }

                                individualList.add(indi);
                                indiStarted = false;
                            }
                        }


                    }

                    // String[] splitted = gedcomLine.split(" ");


                    if (splitted[2].contentEquals("FAM") && !familyStarted)  {
                        family = new Family(splitted[1]);


                        familyStarted = true;
                    } else {
//                    	System.out.println(familyStarted);
                        if (familyStarted == true) {
                            if (splitted[1].contentEquals("HUSB")) {
                                family.setHusbandId(splitted[2]);
//                                System.out.println(family.getId() + "husb");
                            }


                            if (splitted[1].contentEquals("DATE")) {
                                if (isMarried == true) {
                                    StringBuilder marridDate = new StringBuilder();
                                    for (int j = 2; j < splitted.length; j++) {
                                        marridDate.append( splitted[j] + " ");

                                    }
                                    family.setMarried(LocalDate.parse(marridDate.toString(), GedcomValidator.formatter));
                                    isMarried = false;
                                }
//                                System.out.println(family.getId() + "Date1");
                            }
                            if (splitted[1].contentEquals("DATE")) {
                                if (isDivorced == true) {
                                    StringBuilder divorceDate = new StringBuilder();
                                    for (int j = 2; j < splitted.length; j++) {
                                        divorceDate.append( splitted[j] + " ");
                                    }
                                    family.setDivorced(LocalDate.parse(divorceDate.toString(), GedcomValidator.formatter));
                                    isDivorced = false;
                                }
//                                System.out.println(family.getId() + "date2");
                            }
                            if (splitted[1].contentEquals("WIFE")) {
                                family.setWifeId(splitted[2]);
//                                System.out.println(family.getId() + "wife");
                            }

                            if (splitted[1].contentEquals("CHIL")) {
                                family.setChildren((family.getChildren().equals("") ? family.getChildren() : family.getChildren() + ",") + splitted[2]);
//                                System.out.println(family.getId() + "chil");
                            }


                            if (splitted[0].contentEquals("0") && familyStarted) {
//                                if (splitted[2] == "N") {
//                                    family.setDivorced("YES");
//                                }
//
//                                if (splitted[2] == "Y") {
//                                    family.setDivorced("NA");
//                                }
//                                System.out.println(family.getId() + "end");
                                familyArrayList.add(family);

                                if(splitted.length < 2)
                                {
                                    if(!splitted[1].contentEquals("FAM") || !splitted[1].contentEquals("MARR"))
                                    {
                                        familyStarted = false;
//                                		System.out.println(familyStarted);
//                                		System.out.println(family.getId() + "end1");
                                    }
                                }
                                else
                                {
                                    family = new Family(splitted[1]);
//                                	System.out.println(family.getId() + "new Begins");
                                }

                            }

                        }


                    }


                    if (splitted[1].contentEquals("BIRT")) {
                        isBirthDay = true;
                    }

                    if (splitted[1].contentEquals("DEAT") && splitted[2].contentEquals("Y")) {
                        isDeathDay = true;
                        indi.setAlive("N");
                    }
                }
                else
                {
                    if(splitted[0].contentEquals("0") && splitted[1].contentEquals("TRLR"))
                    {
                        familyArrayList.add(family);
                        familyStarted = false;
//                		System.out.println(familyStarted);
//                		System.out.println(family.getId() + "end1");
                    }
                }

            }


            for (int i = 0; i < familyArrayList.size(); i++) {
                for (int j = 0; j < individualList.size(); j++) {
                    if (familyArrayList.get(i).getHusbandId().equals(individualList.get(j).getId())) {
                        familyArrayList.get(i).setHusbandName(individualList.get(j).getName());
                    }

                    if (familyArrayList.get(i).getWifeId().equals(individualList.get(j).getId())) {
                        familyArrayList.get(i).setWifeName(individualList.get(j).getName());
                    }
                }

                // System.out.println(familyArrayList.get(i).wifeName);

            }
            GedcomValidator gvalidator = new GedcomValidator();
            List<Family> ambiguosFamilyMarrDeathList = gvalidator.marriageBeforeDeath(individualList, familyArrayList);
            response.setAmbiguosFamilyMarrDeathList(ambiguosFamilyMarrDeathList);

            List<Family> ambiguosFamilyMarrDivList = gvalidator.marriageBeforeDivorce(familyArrayList);
            response.setAmbiguousFamilyMarrDivList(ambiguosFamilyMarrDivList);

            List<Individual> ambiguousIndividuals = gvalidator.birthBeforeDeath(individualList);
            response.setAmbiguousIndividuals(ambiguousIndividuals);

            List<Family> ambiguosbirthBeforeMarriageList = gvalidator.birthBeforeMarriage(individualList, familyArrayList);
            response.setAmbiguosbirthBeforeMarriage(ambiguosbirthBeforeMarriageList);

            List<Family> ambiguousFamilyMarrBefore14 = gvalidator.marriageBefore14(individualList, familyArrayList);
            response.setAmbiguousFamilyMarrBefore14(ambiguousFamilyMarrBefore14);
            List<Family> ambiguousFamilyDivBeforeDeath = gvalidator.divorceBeforeDeath(individualList, familyArrayList);
            response.setAmbiguosFamilyDivorceDeathList(ambiguousFamilyDivBeforeDeath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setFamilyList(familyArrayList);
        response.setIndividualList(individualList);

        return response;


    }

    public void printFamily(List<Family> familyArrayList) {
        System.out.println("FAMILY INFORMATION");
        System.out.println();
        System.out.println("+---------+--------------------------------+-------------------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+");
        System.out.println("|  ID     |                 Married        |   Divorced              |  HusbandId        |   Husband Name     |    wife Id      |     wife Name      |         Children  |");
        System.out.println("+---------+--------------------------------+-------------------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+");

        for (Family family : familyArrayList) {
            System.out.format(" %5s       %30s   %10s               %10s           %5s        %7s       %7s           %7s      ",
                    family.getId(), family.getMarried(), family.getDivorced(), family.getHusbandId(), family.getHusbandName(), family.getWifeId(), family.getWifeName(), family.getChildren());
            System.out.println();
        }
        System.out.println("+---------+--------------------------------+--------------------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+");

    }


    public void printListOfIndividualsBornBeforeParentsMarriage(List<Family> familyArrayList, List<Individual> individualArrayList) throws ParseException, java.text.ParseException {

        for (Family family : familyArrayList) {
            Optional<LocalDate> marriageDate = family.getMarried();
            String childrenString = family.getChildren();
            for(Individual indi : individualArrayList)
            {
                if(indi.getBdate().isPresent() && childrenString.contains( indi.getId()))
                {
                    LocalDate birthDate = indi.getBdate().get();
                    if( marriageDate.isPresent() && marriageDate.get().isBefore(birthDate))
                    {
                        System.out.println("ANOMALY: " + "FAMILY : US08: " +family.getId() + ": CHILD "+ indi.getName() + "BORN "  + indi.getId() + " Before MARRIAGE: "+ formatDate(family.getMarried()));
                    }
                }
            }
        }
    }
    
    
    public void printListOfIndividualsBornAfterParentsDeath(List<Family> familyArrayList, List<Individual> individualArrayList) throws ParseException, java.text.ParseException {

        for (Family family : familyArrayList) {

            Optional<LocalDate> mDt = family.getMarried();

            String child = family.getChildren();
            
            String childArr[] = child.split(",");
            
            for(int i=0; i<individualArrayList.size(); i++)
            {
            	if(individualArrayList.get(i).getId().equals(family.getHusbandId()))
            	{
            		if(individualArrayList.get(i).getAlive().equals("N"))
            		{
            			 Optional<LocalDate> deathDate = individualArrayList.get(i).getDeathDate();
            			

            			for(String childId : childArr)
                        {
                        	if(childId != null && childId != "")
                        	{
                        		for(int k=0; k<individualArrayList.size(); k++)
                                {
//                        			System.out.println(individualArrayList.get(k).getId() + " - " + childId);
                        			if(individualArrayList.get(k).getId().equals(childId))
                                	{
                        				Optional<LocalDate> bDate = individualArrayList.get(k).getBdate();

                        				
                        				if(bDate.isPresent() && deathDate.isPresent() && bDate.get().isAfter(deathDate.get()))
                        				{
                        					System.out.println("ANOMALY: FAMILY: US09 : "+family.getId() + individualArrayList.get(k).getId() + " BORN AFTER PARENT'S " + individualArrayList.get(i).getId() + " DEATH");
                        				}
                                	}
                                }
                        		
                        	}
                        }
            		}
            	}
            	
            	
            	if(individualArrayList.get(i).getId().equals(family.getWifeId()))
            	{
            		if(individualArrayList.get(i).getAlive().equals("N"))
            		{
            			Optional<LocalDate> deathDate = individualArrayList.get(i).getDeathDate();
            			for(String childId : childArr)
                        {
                        	if(childId != null && childId != "")
                        	{
                        		for(int k=0; k<individualArrayList.size(); k++)
                                {
//                        			System.out.println(individualArrayList.get(k).getId() + " - " + childId);
                        			if(individualArrayList.get(k).getId().equals(childId))
                                	{
                        				Optional<LocalDate> bDate = individualArrayList.get(k).getBdate();
                        				
                        				if(bDate.get().isAfter(deathDate.get()))
                        				{
                        					System.out.println("ANOMALY: FAMILY : US09 "+family.getId() + individualArrayList.get(k).getId() + " BORN AFTER PARENT'S " + individualArrayList.get(i).getId() + " DEATH");
                        				}
                                	}
                                }
                        		
                        	}
                        }
            		}
            	}
            }
        }

    }

    public void printIndividualsWithAgeMoreThan150(List<Individual> individualArrayList) {

        for (Individual indi : individualArrayList) {
            if(indi.getAge() > 150)
            {
                System.out.println("ERROR: INDIVIDUAL : US07 :"+ indi.getId() +": MORE THAN 150 YEARS OLD - BIRTH DATE "+formatDate(indi.getBdate()));
            }
        }
    }

    public void printIndividuals(List<Individual> individualArrayList) {
        System.out.println("INDIVIDUAL INFORMATION");
        System.out.println();
        System.out.println("+---------+--------------------------------+-------------+-------------------+---------------+-----------------+--------------------+--------------------" +
                "+-------------------+");
        System.out.println("|  ID     |                 NAME           |   GENDER    |  Birthday         |   Age         |    Alive        |            Death   |    Child           |        Spouse     |");
        System.out.println("+---------+--------------------------------+-------------+-------------------+---------------+-----------------+--------------------+--------------------" +
                "+-------------------+");

        for (Individual indi : individualArrayList) {
           /* String child = indi.Child;
            if(indi.Child.equals(""))
                child = "N/A";*/
            System.out.format("   %5s   %30s %10s          %10s     %5s %7s                     %7s                                             %7s                                 %5s",
                    indi.getId(), indi.getName(), indi.getGender(), formatDate(indi.getBdate()), indi.getAge(), indi.getAlive(), formatDate(indi.getDeathDate()), indi.getChild(), indi.getSpouse());
            System.out.println();
        }
        System.out.println("+---------+--------------------------------+-------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+---------------------+");

    }

    public void printMarriageBeforeDivorceError(IndiFamilyResponse indiFamilyResponse) {
        for( Family family : indiFamilyResponse.getAmbiguousFamilyMarrDivList()) {
            System.out.println("ERROR: FAMILY:US04:" +family.getId()+ " DIVORCED " + formatDate(family.getDivorced())+" BEFORE MARRIAGE" +formatDate(family.getMarried()));
        }
    }

    public void printMarriageBefore14Error(IndiFamilyResponse indiFamilyResponse){
        for (Family family : indiFamilyResponse.getAmbiguousFamilyMarrBefore14()) {

            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getWifeId())).findFirst().get();
            if (wife.getBdate().isPresent() && husband.getBdate().isPresent() && family.getMarried().isPresent()) {

                LocalDate familyMarriage = family.getMarried().get();
                Period p = Period.between(wife.getBdate().get(), familyMarriage);
                Period p1 = Period.between(husband.getBdate().get(), familyMarriage);
                if (p.getYears() <= 14)
                    System.out.println("ANOMALY: FAMILY :US10:" + family.getId() + " WIFE AGE ( BDATE " +formatDate(wife.getBdate()) + " )DURING MARRIAGE WAS" + "BELOW 14 ( MARRIAGE DATE " + formatDate(family.getMarried()) + " )");
                if (p1.getYears() <= 14)
                    System.out.println("ANOMALY: FAMILY :US10:" + family.getId() + " HUSBAND AGE ( BDATE " +formatDate(husband.getBdate()) + " )DURING MARRIAGE WAS" + "BELOW 14 ( MARRIAGE DATE " + formatDate(family.getMarried()) + " )");
            }
        }
    }
    public void printMarriageBeforeDeathError(IndiFamilyResponse indiFamilyResponse) {
        for (Family family : indiFamilyResponse.getAmbiguosFamilyMarrDeathList()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            Optional<LocalDate> wifeDeath = wife.getDeathDate();
            Optional<LocalDate> husbandDeath = husband.getDeathDate();


            if( wifeDeath.isPresent() && family.getMarried().isPresent() && wifeDeath.get().isBefore( family.getMarried().get()) ){
                System.out.println("ERROR: FAMILY: US05:"+family.getId()+" MARRIED " +formatDate(family.getMarried())+ " AFTER WIFE'S DEATH ( "+wife.getId() +")"+ formatDate(wife.getDeathDate()));
            }
            if ( husbandDeath.isPresent() && family.getMarried().isPresent() && husbandDeath.get().isBefore( family.getMarried().get()) ){
                System.out.println("ERROR: FAMILY: US05:"+family.getId()+" MARRIED " + formatDate(family.getMarried()) + " AFTER HUSBANDS'S DEATH (" +husband.getId() +")"+ formatDate(husband.getDeathDate()));
            }
        }
    }

    public void printBirthBeforeDeathError(IndiFamilyResponse indiFamilyResponse) {
        for( Individual indi : indiFamilyResponse.getAmbiguousIndividuals()) {
            System.out.println("ERROR: INDIVIDUAL:US03 " +indi.getId()+ " BIRTH " +formatDate(indi.getBdate()) +" AFTER DEATH " +formatDate(indi.getDeathDate()));
        }
    }
    public void printBirthBeforeMarriageError(IndiFamilyResponse indiFamilyResponse) {
        for (Family family : indiFamilyResponse.getAmbiguosbirthBeforeMarriage()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            Optional<LocalDate> wifeBirth = wife.getBdate();
            Optional<LocalDate> husbandBirth = husband.getBdate();


            if( wifeBirth.isPresent() && family.getMarried().isPresent() && wifeBirth.get().isAfter(family.getMarried().get()) ){
                System.out.println("ERROR: FAMILY: US02:"+family.getId()+" MARRIED " + formatDate(family.getMarried()) + " BEFORE WIFE'S BIRTH ( "+wife.getId() +") "+formatDate(wife.getBdate()));
            }
            if ( husbandBirth.isPresent() && family.getMarried().isPresent() && husbandBirth.get().isAfter(family.getMarried().get()) ){
                System.out.println("ERROR: FAMILY: US02:"+family.getId()+" MARRIED " + formatDate(family.getMarried()) + " BEFORE WIFE'S BIRTH (" +husband.getId() +")"+formatDate(husband.getBdate()));
            }
        }
    }

    public void printIndividualsWithDivorceBeforeDeath(IndiFamilyResponse indiFamilyResponse) {

        for (Family family : indiFamilyResponse.getAmbiguosFamilyDivorceDeathList()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            Optional<LocalDate> wifeDeath = wife.getDeathDate();
            Optional<LocalDate> husbandDeath = husband.getDeathDate();

            if( wifeDeath.isPresent() && family.getDivorced().isPresent() && wifeDeath.get().isBefore( family.getDivorced().get()) ){
                System.out.println("ERROR: FAMILY: US06:"+family.getId()+" Divorced " + family.getDivorced() + " AFTER WIFE'S DEATH ( "+wife.getId() +")"+ wife.getDeathDate());
            }
            if ( husbandDeath.isPresent() && family.getDivorced().isPresent() && husbandDeath.get().isBefore( family.getDivorced().get()) ){
                System.out.println("ERROR: FAMILY: US06:"+family.getId()+" Divorced " + formatDate(family.getDivorced()) + " AFTER HUSBANDS'S DEATH (" +husband.getId() +") "+ formatDate(husband.getDeathDate()));
            }
        }
    }
    public void printIndividualsWithBirthBeforeCurrentData(List<Family> individualFamList,List<Individual> individualArrayList) throws ParseException, java.text.ParseException {

        for (Individual indi : individualArrayList) {
            Optional<LocalDate> birthDate = indi.getBdate();
            Optional<LocalDate> deathDate = indi.getDeathDate();

            if ( birthDate.isPresent() ){
                if(LocalDate.now().isBefore(birthDate.get()))
                    System.out.println("ERROR: " + "INDIVIDUAL: US01: " +indi.getId() + ": BIRTH DATE IS AFTER CURRENTDATE");

            }
            if (deathDate.isPresent()){
                if(LocalDate.now().isBefore(deathDate.get()))
                    System.out.println("ERROR: " + "INDIVIDUAL: US01: " +indi.getId() + ": Death DATE IS AFTER CURRENTDATE");

            }

        }
        for (Family fam : individualFamList) {
            Optional<LocalDate> marrDate = fam.getMarried();
            Optional<LocalDate> divDate = fam.getDivorced();

            if ( marrDate.isPresent() ){
                if(LocalDate.now().isBefore(marrDate.get()))
                    System.out.println("ERROR: " + "INDIVIDUAL: US01: " +fam.getId() + ": Marriage DATE IS AFTER CURRENTDATE");

            }
            if ( divDate.isPresent() ){;
                if(LocalDate.now().isBefore(divDate.get()))
                    System.out.println("ERROR: " + "INDIVIDUAL: US01: " +fam.getId() + ": Divorce DATE IS AFTER CURRENTDATE");

            }

        }

    }

    public String formatDate( Optional<LocalDate> date ){
        if( date.isPresent() ) {
            return date.get().format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        }
        return "-NA-";
    }

}


