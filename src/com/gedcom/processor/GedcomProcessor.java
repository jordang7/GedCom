package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.models.Individual;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
                                    for (int j = 2; j < splitted.length; j++) {
                                        indi.setBirthDay(indi.getBirthDay() + " " + splitted[j]);
                                    }
                                    isBirthDay = false;

                                    Date date = new Date();

                                    Date date1 = new SimpleDateFormat(" dd MMM yyyy").parse(indi.getBirthDay());
                                    java.util.Date date2 = new java.util.Date();

                                    long time = getDateDiff(date1, date2, TimeUnit.MINUTES);
                                    Calendar c = Calendar.getInstance();
                                    c.setTimeInMillis(time);
                                    int mYear = c.get(Calendar.YEAR) - 1970;
                                    indi.setAge(Integer.toString(mYear));
                                }
                                if (isDeathDay == true) {
                                    for (int j = 2; j < splitted.length; j++) {
                                        indi.setDeath(indi.getDeath() + " " + splitted[j]);
                                    }
                                    isDeathDay = false;
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
                                    for (int j = 2; j < splitted.length; j++) {
                                        family.setMarried(family.getMarried() + " " + splitted[j]);
                                    }
                                    isMarried = false;
                                }
//                                System.out.println(family.getId() + "Date1");
                            }
                            if (splitted[1].contentEquals("DATE")) {
                                if (isDivorced == true) {
                                    for (int j = 2; j < splitted.length; j++) {
                                        family.setDivorced(family.getDivorced() + " " + splitted[j]);
                                    }
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
                    indi.getId(), indi.getName(), indi.getGender(), indi.getBirthDay(), indi.getAge(), indi.getAlive(), indi.getDeath(), indi.getChild(), indi.getSpouse());
            System.out.println();
        }
        System.out.println("+---------+--------------------------------+-------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+---------------------+");

    }

    public void printMarriageBeforeDivorceError(IndiFamilyResponse indiFamilyResponse) {
        for( Family family : indiFamilyResponse.getAmbiguousFamilyMarrDivList()) {
            System.out.println("ERROR: FAMILY:US04:" +family.getId()+ " DIVORCED " + family.getDivorced()+"BEFORE MARRIAGE" + family.getMarried());
        }
    }

    public void printMarriageBeforeDeathError(IndiFamilyResponse indiFamilyResponse) {
        for (Family family : indiFamilyResponse.getAmbiguosFamilyMarrDeathList()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            LocalDate wifeDeath = null;
            if( wife.getDeath() == null || wife.getDeath().equals("")){

            } else {
                wifeDeath = LocalDate.parse(wife.getDeath(), GedcomValidator.formatter);
            }


            LocalDate husbandDeath = null;

            if( husband.getDeath() == null || husband.getDeath().equals("")){

            } else {
                husbandDeath = LocalDate.parse(husband.getDeath(), GedcomValidator.formatter);
            }

            if( wifeDeath != null && wifeDeath.isBefore( LocalDate.parse(family.getMarried(), GedcomValidator.formatter)) ){
                System.out.println("ERROR: FAMILY: US05:"+family.getId()+" MARRIED " + family.getMarried() + " AFTER WIFE'S DEATH ( "+wife.getId() +")"+ wife.getDeath());
            }
            if ( husbandDeath != null && husbandDeath.isBefore( LocalDate.parse(family.getMarried(), GedcomValidator.formatter)) ){
                System.out.println("ERROR: FAMILY: US05:"+family.getId()+" MARRIED " + family.getMarried() + " AFTER HUSBANDS'S DEATH (" +husband.getId() +")"+ husband.getDeath());
            }
        }
    }
    
    public void printBirthBeforeDeathError(IndiFamilyResponse indiFamilyResponse) {
        for( Individual indi : indiFamilyResponse.getAmbiguousIndividuals()) {
            System.out.println("ERROR: INDIVIDUAL:US03" +indi.getId()+ " BIRTH " + indi.getBirthDay()+"AFTER DEATH" + indi.getDeath());
        }
    }
    public void printBirthBeforeMarriageError(IndiFamilyResponse indiFamilyResponse) {
        for (Family family : indiFamilyResponse.getAmbiguosbirthBeforeMarriage()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            LocalDate wifeBirth = null;
            if( wife.getBirthDay() == null || wife.getBirthDay().equals("")){

            } else {
                wifeBirth = LocalDate.parse(wife.getBirthDay(), GedcomValidator.formatter);
            }


            LocalDate husbandBirth = null;

            if( husband.getBirthDay() == null || husband.getBirthDay().equals("")){

            } else {
            	husbandBirth = LocalDate.parse(husband.getBirthDay(), GedcomValidator.formatter);
            }

            if( wifeBirth != null && wifeBirth.isAfter(LocalDate.parse(family.getMarried(), GedcomValidator.formatter)) ){
                System.out.println("ERROR: FAMILY: US02:"+family.getId()+" MARRIED " + family.getMarried() + " BEFORE WIFE'S BIRTH ( "+wife.getId() +")"+ wife.getBirthDay());
            }
            if ( husbandBirth != null && husbandBirth.isAfter( LocalDate.parse(family.getMarried(), GedcomValidator.formatter)) ){
                System.out.println("ERROR: FAMILY: US02:"+family.getId()+" MARRIED " + family.getMarried() + " BEFORE WIFE'S BIRTH (" +husband.getId() +")"+ husband.getBirthDay());
            }
        }
    }

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        //return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
        return diffInMillies;
    }
    
}
