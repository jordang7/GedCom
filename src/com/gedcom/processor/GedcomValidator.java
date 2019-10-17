package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.FamilyWithChildrenMarriedToEachOther;
import com.gedcom.models.Individual;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Meghana on 9/28/2019.
 */
public class GedcomValidator {

    public static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            // case insensitive to parse JAN and FEB
            .parseCaseInsensitive()
            // add pattern
            .appendPattern("d MMM yyyy ")
            // create formatter (use English Locale to parse month names)
            .toFormatter(Locale.ENGLISH);


    //US05 Meghana
    public List<Family> marriageBeforeDeath(List<Individual> individualList, List<Family> familyList) {
        List<Family> ambiguosFamilyMarrDeathList = new ArrayList<>();

        for (Family family : familyList) {
            String husbandId = family.getHusbandId();
            String wifeId = family.getWifeId();

            Optional<Individual> husbandOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(husbandId);
            }).findFirst();

            Optional<Individual> wifeOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(wifeId);
            }).findFirst();
            Optional<LocalDate> marriageDate = family.getMarried();
            if (husbandOpt.isPresent() && wifeOpt.isPresent() && marriageDate.isPresent()) {
                Individual husband = husbandOpt.get();
                Individual wife = wifeOpt.get();

                Optional<LocalDate> husbandDeathDate = husband.getDeathDate();
                Optional<LocalDate> wifeDeathDate = wife.getDeathDate();


                if (husbandDeathDate.isPresent()) {

                    if (husbandDeathDate.get().isBefore(marriageDate.get())) {
                        ambiguosFamilyMarrDeathList.add(family);

                    }
                } else if (wifeDeathDate.isPresent()) {
                    if (wifeDeathDate.get().isBefore(marriageDate.get())) {
                        ambiguosFamilyMarrDeathList.add(family);
                    }

                }
            }
        }

        return ambiguosFamilyMarrDeathList;

    }

    //US04 Meghana
    public List<Family> marriageBeforeDivorce(List<Family> familyList) {

        List<Family> ambiguosFamilyMarrDivList = new ArrayList<>();

        for (Family family : familyList) {
            Optional<LocalDate> marriageDate = family.getMarried();
            Optional<LocalDate> divorceDate = family.getDivorced();
            if (divorceDate.isPresent()) {
                if (marriageDate.isPresent()) {

                    if (divorceDate.get().isBefore(marriageDate.get())) {
                        ambiguosFamilyMarrDivList.add(family);
                    }
                }
            }
        }
        return ambiguosFamilyMarrDivList;
    }

    //US16 Meghana
    public List<Family> maleNamesSameCheck(List<Family> familyList) {
        List<Family> ambiguousMaleLastNames = new ArrayList<>();

        for (Family family : familyList) {
            Optional<Individual> husbandOpt = family.getHusbandIndi();
            if (husbandOpt.isPresent()) {
                String familyLastName = husbandOpt.get().getLastName();
                List<Individual> maleChildren = family.getChildrenIndis().stream().filter(ind -> ind.getGender().equals("M") && !ind.getLastName().equals(familyLastName)).collect(Collectors.toList());

                if (maleChildren.size() > 0) {
                    ambiguousMaleLastNames.add(family);
                }
            }

        }
        return ambiguousMaleLastNames;
    }

    //US18 Meghana
    public List<FamilyWithChildrenMarriedToEachOther> siblingsShouldNotMarry(List<Family> familyList) {
        List<FamilyWithChildrenMarriedToEachOther> ambiguousSblingsMarriageList = new ArrayList<>();

        for (Family family : familyList) {
            List<Individual> siblingList = family.getChildrenIndis();
            for (Individual sibling : siblingList) {
                String silblingId = sibling.getId();
                if (sibling.getGender().equals("M")) {
                    Optional<Family> siblingFamily = familyList.stream().filter(fam -> fam.getHusbandId().equals(silblingId)).findFirst();
                    if (siblingFamily.isPresent()) {
                        String wifeId = siblingFamily.get().getWifeId();

                        Optional<Individual> ambiguousSibling = siblingList.stream().filter(sib -> sib.getId().equals(wifeId)).findFirst();
                        if (ambiguousSibling.isPresent()) {

                            ambiguousSblingsMarriageList.add(new FamilyWithChildrenMarriedToEachOther( family, sibling, ambiguousSibling.get()));
                        }

                    }
                }
            }


        }
        return ambiguousSblingsMarriageList;
    }


    /*
    User Story 16
     */

    //Refactor at some point because this is basically the same as Marriage before Death
    public List<Family> birthBeforeMarriage(List<Individual> individualList, List<Family> familyList) {
        List<Family> ambiguosbirthBeforeMarriageList = new ArrayList<>();

        for (Family family : familyList) {
            String husbandId = family.getHusbandId();
            String wifeId = family.getWifeId();

            Optional<Individual> husbandOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(husbandId);
            }).findFirst();

            Optional<Individual> wifeOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(wifeId);
            }).findFirst();
            Optional<LocalDate> marriageDate = family.getMarried();
            if (husbandOpt.isPresent() && wifeOpt.isPresent() && marriageDate.isPresent()) {
                Individual husband = husbandOpt.get();
                Individual wife = wifeOpt.get();

                Optional<LocalDate> husbandBirthDate = husband.getBdate();
                Optional<LocalDate> wifeBirthDate = wife.getBdate();

                if (husbandBirthDate.isPresent()) {

                    if (husbandBirthDate.get().isAfter(marriageDate.get())) {
                        ambiguosbirthBeforeMarriageList.add(family);

                    }
                }
                if (wifeBirthDate.isPresent()) {

                    if (wifeBirthDate.get().isAfter(marriageDate.get())) {
                        ambiguosbirthBeforeMarriageList.add(family);
                    }
                }
            }
        }

        return ambiguosbirthBeforeMarriageList;
    }

    //Birth before death
    public List<Individual> birthBeforeDeath(List<Individual> individualList) {

        List<Individual> ambiguousIndividuals = new ArrayList<>();

        for (Individual indi : individualList) {
            Optional<LocalDate> birthDate = indi.getBdate();
            Optional<LocalDate> deathDate = indi.getDeathDate();
            if (birthDate.isPresent()) {
                if (deathDate.isPresent()) {

                    if (birthDate.get().isAfter(deathDate.get())) {
                        ambiguousIndividuals.add(indi);
                    }
                }
            }
        }
        return ambiguousIndividuals;
    }

    //US10 Meghana
    public List<Family> marriageBefore14(List<Individual> individualList, List<Family> familyList) {
        List<Family> ambiguosFamilyMarList = new ArrayList<>();

        for (Family family : familyList) {
            String husbandId = family.getHusbandId();
            String wifeId = family.getWifeId();

            Optional<Individual> husbandOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(husbandId);
            }).findFirst();

            Optional<Individual> wifeOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(wifeId);
            }).findFirst();
            Optional<LocalDate> marriageDate = family.getMarried();

            if (husbandOpt.isPresent() && wifeOpt.isPresent() && marriageDate.isPresent()) {

                Optional<LocalDate> husbandBirthDate = husbandOpt.get().getBdate();
                Optional<LocalDate> wifeBirthDate = wifeOpt.get().getBdate();

                if (husbandBirthDate.isPresent()) {

                    Period p = Period.between(husbandBirthDate.get(), marriageDate.get());
                    if (p.getYears() <= 14) {
                        ambiguosFamilyMarList.add(family);
                        break;
                    }
                }

                if (wifeBirthDate.isPresent()) {

                    Period p = Period.between(wifeBirthDate.get(), marriageDate.get());
                    if (p.getYears() <= 14) {
                        ambiguosFamilyMarList.add(family);
                    }
                }
            }


        }
        return ambiguosFamilyMarList;
    }

    public List<Family> divorceBeforeDeath(List<Individual> individualList, List<Family> familyList) {
        List<Family> ambiguousFamDivBeforeDeath = new ArrayList<>();

        for (Family family : familyList) {
            String husbandId = family.getHusbandId();
            String wifeId = family.getWifeId();

            Optional<Individual> husbandOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(husbandId);
            }).findFirst();

            Optional<Individual> wifeOpt = individualList.stream().filter(individual -> {
                return individual.getId().equals(wifeId);
            }).findFirst();
            Optional<LocalDate> divorceDate = family.getDivorced();
            if (husbandOpt.isPresent() && wifeOpt.isPresent() && divorceDate.isPresent()) {
                Individual husband = husbandOpt.get();
                Individual wife = wifeOpt.get();

                Optional<LocalDate> husbandDeathDate = husband.getDeathDate();
                Optional<LocalDate> wifeDeathDate = wife.getDeathDate();

                if (husbandDeathDate.isPresent()) {
                    if (husbandDeathDate.get().isBefore(divorceDate.get())) {
                        ambiguousFamDivBeforeDeath.add(family);

                    }
                } else if (wifeDeathDate.isPresent()) {
                    if (wifeDeathDate.get().isBefore(divorceDate.get())) {
                        ambiguousFamDivBeforeDeath.add(family);
                    }

                }
            }
        }

        return ambiguousFamDivBeforeDeath;

    }


}
