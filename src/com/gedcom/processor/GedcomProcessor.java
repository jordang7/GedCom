package com.gedcom.processor;

import com.gedcom.models.*;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

        List<Individual> individualList = new ArrayList<>();
        List<Family> familyArrayList = new ArrayList<>();
        IndiFamilyResponse response = new IndiFamilyResponse();
        try {

            generateIndividualFamilyObjects(validLines, individualList, familyArrayList);

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

            List<Family> ambiguousMaleLastNames = gvalidator.maleNamesSameCheck(familyArrayList);
            response.setAmbiguousMaleLastNames(ambiguousMaleLastNames);

            List<FamilyWithChildrenMarriedToEachOther> ambiguousSblingsMarriageList = gvalidator.siblingsShouldNotMarry(familyArrayList);
            response.setAmbiguousSblingsMarriageList(ambiguousSblingsMarriageList);

            List<FamilyWithParentMarriedToDescendants> ambiguousParentDescendantMarriageList = gvalidator.parentShouldNotMarryADescendant(individualList,familyArrayList);
            response.setAmbiguousParentDescendantMarriageList(ambiguousParentDescendantMarriageList);
            List<FamilyWithChildrenMarriedToEachOther> ambiguousCousinsMarriageList = gvalidator.firstCousinsShouldNotMarryOneAnother(individualList,familyArrayList);
            response.setAmbiguousFirstCousinsMarriageList(ambiguousCousinsMarriageList);
            
            List<Family> ambiguousMoreThan15Children = gvalidator.fewerThan15Children(familyArrayList);
            response.setAmbiguousMoreThan15Children(ambiguousMoreThan15Children);

            List<FamilyWithOlderParents> familyWithOlderParents = gvalidator.getFamiliesWithOlderParents(familyArrayList);
            response.setFamilyWithOlderParents( familyWithOlderParents );
            List<FamilyWithAnomaly> ambiguousBigamyList = gvalidator.noBigamyIsAllowed(individualList,familyArrayList);
            response.setAmbiguousBigamyList(ambiguousBigamyList);

            List<Family> ambiguousGenderForRoles = gvalidator.checkCorrectGenderforRoles(familyArrayList);
            response.setAmbiguousGenderForRoles(ambiguousGenderForRoles);

            List<Individual> ambiguousIndividualIDList = gvalidator.uniqueID(individualList);
            response.setAmbiguousIndividualIDList(ambiguousIndividualIDList);

            List<Family> ambiguousFamilyIDList = gvalidator.uniqueFamilyID(familyArrayList);
            response.setAmbiguousFamilyIDList(ambiguousFamilyIDList);

            List<FamilyWithAnomaly> ambiguousFamilyWithUncleAunt = gvalidator.AuntUncleMarryNN(familyArrayList, individualList);
            response.setAuntUncleMarriedNN(ambiguousFamilyWithUncleAunt);
            List<FamilyWithAnomaly> ambiguousFamilyWithDuplicateFirstNames = gvalidator.firstNamesShouldBeUniqueInTheFamily(individualList, familyArrayList);
            response.setAmbiguousDuplicateFirstNameFamilies(ambiguousFamilyWithDuplicateFirstNames);
            List<FamilyWithAnomaly> listIndiFamWithMissingCorrespondingEntries = gvalidator.validateCorrespondingEntry(individualList, familyArrayList);
            response.setMissingCorrespondingEntries(listIndiFamWithMissingCorrespondingEntries);
            //US31
            List<Individual> livingSingleList =gvalidator.livingSingle(individualList);
            response.setLivingSingleList(livingSingleList);

            List<Individual> orphanChildrenList =gvalidator.orphanChildren(individualList,familyArrayList);
            response.setOrphanChildrenList(orphanChildrenList);
            List<FamilyWithAnomaly> listLargeAgeDifference = gvalidator.loadCouplesWithLargeAgeDifference(familyArrayList);
            response.setAmbiguousFamiliesWithLargeAgeDifference(listLargeAgeDifference);
            List<Individual> peopleDiedInLast30Days = gvalidator.listPeopleWhoDiedWithin30Days(individualList);
            response.setPeopleDiedInLast30Days(peopleDiedInLast30Days);

            List<Individual> peopleBornInLast30Days = gvalidator.listPeopleWhoBornWithin30Days(individualList);
            response.setPeopleBornInLast30Days(peopleBornInLast30Days);

            List<Individual> spouseAndChildOfPeopleDeadIn30Days = gvalidator.listSpouseAndChildOfPeopleDeadIn30Days(individualList,familyArrayList);
            response.setSpouseAndChildOfPeopleDeadIn30Days(spouseAndChildOfPeopleDeadIn30Days);

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setFamilyList(familyArrayList);
        response.setIndividualList(individualList);

        return response;


    }

    private void generateIndividualFamilyObjects(List<String> validLines, List<Individual> individualList, List<Family> familyArrayList) {
        Individual indi = null;
        Family family = null;
        boolean indiStarted = false;
        boolean isBirthDay = false;
        boolean isDeathDay = false;
        boolean familyStarted = false;
        boolean isMarried = false;
        boolean isDivorced = false;
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

                            family.setHusbandIndi( individualList.stream().filter( ind -> ind.getId().equals(splitted[2])).findFirst());

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
                            family.setWifeIndi( individualList.stream().filter( ind -> ind.getId().equals(splitted[2])).findFirst());
                        }

                        if (splitted[1].contentEquals("CHIL")) {
                            family.setChildren((family.getChildren().equals("") ? family.getChildren() : family.getChildren() + ",") + splitted[2]);

                            family.addChildrenIndi(individualList.stream().filter( ind -> ind.getId().equals(splitted[2])).findFirst());


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
    }



}


