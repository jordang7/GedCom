package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.GedcomResponse;
import com.gedcom.models.IndiFamilyResponse;
import com.gedcom.models.Individual;

import java.text.SimpleDateFormat;
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
            System.out.println("-->" + gedcomLine);
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

            System.out.println("<--" + parsedLine);

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


                    if (splitted[2].contentEquals("FAM")) {
                        family = new Family(splitted[1]);


                        familyStarted = true;
                    } else {
                        if (familyStarted == true) {
                            if (splitted[1].contentEquals("HUSB")) {
                                family.setHusbandId(splitted[2]);
                            }


                            if (splitted[1].contentEquals("DATE")) {
                                if (isMarried == true) {
                                    for (int j = 2; j < splitted.length; j++) {
                                        family.setMarried(family.getMarried() + " " + splitted[j]);
                                    }
                                    isMarried = false;
                                }
                            }
                            if (splitted[1].contentEquals("DATE")) {
                                if (isDivorced == true) {
                                    for (int j = 2; j < splitted.length; j++) {
                                        family.setDivorced(family.getDivorced() + " " + splitted[j]);
                                    }
                                    isDivorced = false;
                                }
                            }
                            if (splitted[1].contentEquals("WIFE")) {
                                family.setWifeId(splitted[2]);
                            }

                            if (splitted[1].contentEquals("CHIL")) {
                                family.setChildren((family.getChildren().equals("") ? family.getChildren() : family.getChildren() + ",") + splitted[2]);
                            }


                            if (splitted[1].contentEquals("_CURRENT")) {
                                if (splitted[2] == "N") {
                                    family.setDivorced("YES");
                                }

                                if (splitted[2] == "Y") {
                                    family.setDivorced("NA");
                                }
                                familyArrayList.add(family);
                                familyStarted = false;

                            }

                        }


                    }


                    if (splitted[1].contentEquals("BIRT")) {
                        isBirthDay = true;
                    }

                    if (splitted[1].contentEquals("DEAT") && splitted[2].contentEquals("Y")) {
                        isDeathDay = true;
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

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        //return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
        return diffInMillies;
    }
}
