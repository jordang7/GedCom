package com.gedcom.processor;
import com.gedcom.models.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
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

                            ambiguousSblingsMarriageList.add(new FamilyWithChildrenMarriedToEachOther(family, sibling, ambiguousSibling.get()));
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

    //US17 - Jinal
    public List<FamilyWithParentMarriedToDescendants> parentShouldNotMarryADescendant(List<Individual> individualList, List<Family> familyArrayList) {
        List<FamilyWithParentMarriedToDescendants> ambiguousParentDescendantMarriageList = new ArrayList<>();
        HashMap<String, Family> childToFamilyMap = new HashMap<>();
        for (Family fam : familyArrayList) {
            if (fam.getChildren() != null) {
                for (String child : fam.getChildren().split(",")) {
                    childToFamilyMap.put(child, fam);
                }
            }
        }
        for (Family family : familyArrayList) {
            List<Individual> childrenOfTheFamily = family.getChildrenIndis();
            for (Individual child : childrenOfTheFamily) {
                String childId = child.getId();
                Set<String> ancestors = new HashSet<String>();
                loadAncestors(family, ancestors, childToFamilyMap);
                if (child.getGender().equals("M")) {
                    Optional<Family> childIsHusbandInThisFamily = familyArrayList.stream().filter(fam -> fam.getHusbandId().equals(childId)).findFirst();
                    if (childIsHusbandInThisFamily.isPresent()) {
                        String wifeId = childIsHusbandInThisFamily.get().getWifeId();
                        if (ancestors.contains(wifeId)) {
                            Optional<Individual> ancestorWife = individualList.stream().filter(ind -> ind.getId().equals(wifeId)).findFirst();
                            if (ancestorWife.isPresent())
                                ambiguousParentDescendantMarriageList.add(new FamilyWithParentMarriedToDescendants(family, child, ancestorWife.get()));
                        }
                    }

                } else if (child.getGender().equals("F")) {
                    Optional<Family> childIsWifeInThisFamily = familyArrayList.stream().filter(fam -> fam.getWifeId().equals(childId)).findFirst();
                    if (childIsWifeInThisFamily.isPresent()) {
                        String husbandId = childIsWifeInThisFamily.get().getHusbandId();
                        if (ancestors.contains(husbandId)) {
                            Optional<Individual> ancestorHusband = individualList.stream().filter(ind -> ind.getId().equals(husbandId)).findFirst();
                            if (ancestorHusband.isPresent())
                                ambiguousParentDescendantMarriageList.add(new FamilyWithParentMarriedToDescendants(family, child, ancestorHusband.get()));
                        }
                    }

                }
            }
        }
        return ambiguousParentDescendantMarriageList;
    }

    public void loadAncestors(Family family, Set<String> ancestors, HashMap<String, Family> childToFamilyMap) {
        if (family == null) {
            return;
        } else {
            if (family.getHusbandId() != null) {
                ancestors.add(family.getHusbandId());
                loadAncestors(childToFamilyMap.get(family.getHusbandId()), ancestors, childToFamilyMap);
            }
            if (family.getWifeId() != null) {
                ancestors.add(family.getWifeId());
                loadAncestors(childToFamilyMap.get(family.getWifeId()), ancestors, childToFamilyMap);
            }
        }
    }

    //US19 - Jinal
    public List<FamilyWithChildrenMarriedToEachOther> firstCousinsShouldNotMarryOneAnother(List<Individual> individualList, List<Family> familyArrayList) {
        List<FamilyWithChildrenMarriedToEachOther> ambiguousFirstCousinsMarriageList = new ArrayList<>();
        HashMap<String, Family> childToFamilyMap = new HashMap<>();
        for (Family fam : familyArrayList) {
            if (fam.getChildren() != null) {
                for (String child : fam.getChildren().split(",")) {
                    childToFamilyMap.put(child, fam);
                }
            }
        }
        for (Family family : familyArrayList) {
            List<Individual> childrenOfTheFamily = family.getChildrenIndis();
            Set<String> firstCousins = new HashSet<String>();
            Family mothersFamily = childToFamilyMap.get(family.getWifeId());
            Family fathersFamily = childToFamilyMap.get(family.getHusbandId());
            if (mothersFamily != null)
                loadFirstCousins(mothersFamily, firstCousins, familyArrayList);
            if (fathersFamily != null)
                loadFirstCousins(fathersFamily, firstCousins, familyArrayList);
            for (Individual child : childrenOfTheFamily) {

                if (child.getGender().equals("M")) {
                    Optional<Family> childIsHusbandInThisFamily = familyArrayList.stream().filter(fam -> fam.getHusbandId().equals(child.getId())).findFirst();
                    if (childIsHusbandInThisFamily.isPresent()) {
                        String wifeId = childIsHusbandInThisFamily.get().getWifeId();
                        if (firstCousins.contains(wifeId)) {
                            Optional<Individual> cousinWife = individualList.stream().filter(ind -> ind.getId().equals(wifeId)).findFirst();
                            if (cousinWife.isPresent())
                                ambiguousFirstCousinsMarriageList.add(new FamilyWithChildrenMarriedToEachOther(family, child, cousinWife.get()));
                        }
                    }
                } else {
                    Optional<Family> childIsWifeInThisFamily = familyArrayList.stream().filter(fam -> fam.getWifeId().equals(child.getId())).findFirst();
                    if (childIsWifeInThisFamily.isPresent()) {
                        String husbandId = childIsWifeInThisFamily.get().getHusbandId();
                        if (firstCousins.contains(husbandId)) {
                            Optional<Individual> cousinHusband = individualList.stream().filter(ind -> ind.getId().equals(husbandId)).findFirst();
                            if (cousinHusband.isPresent())
                                ambiguousFirstCousinsMarriageList.add(new FamilyWithChildrenMarriedToEachOther(family, child, cousinHusband.get()));
                        }
                    }
                }
            }
        }
        return ambiguousFirstCousinsMarriageList;
    }

    public void loadFirstCousins(Family family, Set<String> firstCousins, List<Family> familyArrayList) {
        for (Individual sibling : family.getChildrenIndis()) {
            if (sibling.getGender().equals("M")) {
                Optional<Family> relatives = familyArrayList.stream().filter(fam -> fam.getHusbandId().equals(sibling.getId())).findFirst();
                if (relatives.isPresent()) {
                    firstCousins.addAll(Arrays.asList(relatives.get().getChildren().split(",")));
                }
            } else if (sibling.getGender().equals("F")) {
                Optional<Family> relatives = familyArrayList.stream().filter(fam -> fam.getWifeId().equals(sibling.getId())).findFirst();
                if (relatives.isPresent()) {
                    firstCousins.addAll(Arrays.asList(relatives.get().getChildren().split(",")));
                }
            }
        }
    }
    // US15
    public List<Family> fewerThan15Children(List<Family> familyList){
        List<Family> ambiguousMoreThan15Children = new ArrayList<>();
        for (Family family : familyList) {
            if(family.getChildrenIndis().size()>15) {
                ambiguousMoreThan15Children.add(family);
            }
        }
        return ambiguousMoreThan15Children;
    }

    // US12
    public List<FamilyWithOlderParents> getFamiliesWithOlderParents( List<Family> familyList ){

        List<FamilyWithOlderParents> famWithOlderParents = new ArrayList<>();
        for( Family fam : familyList ) {
            FamilyWithOlderParents familyWithOlderParent = new FamilyWithOlderParents();
            familyWithOlderParent.setFamily(fam);
            boolean oldParent = false;
            List<LocalDate> childrenBdates = fam.getChildrenIndis().stream().filter(i -> i.getBdate().isPresent()).map( i -> i.getBdate().get() ).collect(Collectors.toList());
            if( fam.getHusbandIndi().isPresent()) {
                Optional<LocalDate> hBdate = fam.getHusbandIndi().get().getBdate();

                if( hBdate.isPresent() ){
                    boolean fatherIsReallyOld = childrenBdates.stream().filter( d -> Period.between(hBdate.get(), d).getYears() > 80).findFirst().isPresent();
                    if( fatherIsReallyOld ){
                        familyWithOlderParent.setOlderHusband( fam.getHusbandIndi());
                        oldParent = true;
                    }
                }
            }
            if( fam.getWifeIndi().isPresent()) {
                Optional<LocalDate> wBdate = fam.getWifeIndi().get().getBdate();
                if( wBdate.isPresent() ){
                    boolean motherIsReallyOld = childrenBdates.stream().filter( d -> Period.between(wBdate.get(), d).getYears() > 60).findFirst().isPresent();
                    if( motherIsReallyOld ){
                        familyWithOlderParent.setOlderWife( fam.getWifeIndi());
                        oldParent = true;
                    }
                }
            }
            if( oldParent )
                famWithOlderParents.add( familyWithOlderParent);
        }
        return famWithOlderParents;
    }

    public void loadAuntUncles(Family family,Set<String> auntUncles,List<Family> familyArrayList) {
        for(Individual sibling:family.getChildrenIndis()){
            if(sibling.getGender().equals("M")) {
                Optional<Family> relatives = familyArrayList.stream().filter(fam -> fam.getHusbandId().equals(sibling.getId())).findFirst();
                if(relatives.isPresent())
                {
                    auntUncles.addAll(Arrays.asList(relatives.get().getId().split(",")));
                }
            }else if(sibling.getGender().equals("F")){
                Optional<Family> relatives = familyArrayList.stream().filter(fam -> fam.getWifeId().equals(sibling.getId())).findFirst();
                if(relatives.isPresent())
                {
                    auntUncles.addAll(Arrays.asList(relatives.get().getId().split(",")));
                }
            }
        }
    }

// US20
/*
public List<Family> AuntUncleMarryNN(List<Family> familyArrayList, List<Individual> individualList){
    List<Family> ambiguousAuntUncleMarryNN = new ArrayList<>();
    HashMap<String, Family> childToFamilyMap = new HashMap<>();
    for (Family fam : familyArrayList) {
        if (fam.getChildren() != null) {
            for (String child : fam.getChildren().split(",")) {
                childToFamilyMap.put(child, fam);
            }
        }
    }
    for (Family family : familyArrayList) {
        List<Individual> childrenOfTheFamily = family.getChildrenIndis();
        Set<String> auntUncles = new HashSet<String>();
        Family mothersFamily = childToFamilyMap.get(family.getWifeId());
        Family fathersFamily = childToFamilyMap.get(family.getHusbandId());
        if(mothersFamily != null)
        	loadAuntUncles(mothersFamily,auntUncles,familyArrayList);
        if(fathersFamily != null)
        	loadAuntUncles(fathersFamily,auntUncles,familyArrayList);

        for (Individual child : childrenOfTheFamily) {
            if (child.getGender().equals("M")) {
                Optional<Family> childIsHusbandInThisFamily = familyArrayList.stream().filter(fam -> fam.getHusbandId().equals(child.getId())).findFirst();
                if (childIsHusbandInThisFamily.isPresent()) {
                    String wifeId = childIsHusbandInThisFamily.get().getWifeId();
                    if (auntUncles.contains(wifeId)) {
                        Optional<Individual> auntWife = individualList.stream().filter(ind -> ind.getId().equals(wifeId)).findFirst();
                        if (auntWife.isPresent())
                        	ambiguousAuntUncleMarryNN.add(new AuntUncleMarriedNN(family, child, auntWife.get()));
                    }
                }
            }
            else{
                Optional<Family> childIsWifeInThisFamily = familyArrayList.stream().filter(fam -> fam.getWifeId().equals(child.getId())).findFirst();
                if (childIsWifeInThisFamily.isPresent()) {
                    String husbandId = childIsWifeInThisFamily.get().getWifeId();
                    if (auntUncles.contains(husbandId)) {
                        Optional<Individual> uncleHusband = individualList.stream().filter(ind -> ind.getId().equals(husbandId)).findFirst();
                        if (uncleHusband.isPresent())
                        	ambiguousAuntUncleMarryNN.add(new AuntUncleMarriedNN(family, child, uncleHusband.get()));
                    }
                }
            }
        }
    }
    return  ambiguousAuntUncleMarryNN;
} */

//    public List<FamilyWithAnomaly> noBigamyIsAllowed(List<Individual> individualList, List<Family> familyArrayList){
//        List<FamilyWithAnomaly> familyWithBigamy = new ArrayList<FamilyWithAnomaly>();
//        for (Family family : familyArrayList) {
//            String husbandId= family.getHusbandId();
//            String wifeId = family.getWifeId();
//            Optional<Family> anotherFamilyOfThisMan = familyArrayList.stream().filter(famitr -> famitr.getHusbandId().equals((husbandId)) && !famitr.getId().equals(family.getId())).findFirst();
//            Optional<Family> anotherFamilyOfThisWoman = familyArrayList.stream().filter(famitr -> famitr.getHusbandId().equals((wifeId)) && !famitr.getId().equals(family.getId())).findFirst();
//            if(anotherFamilyOfThisMan.isPresent()){
//                loadFamilyWithBigamy(anotherFamilyOfThisMan.get(),family,familyWithBigamy);
//            }
//            if(anotherFamilyOfThisWoman.isPresent()){
//                loadFamilyWithBigamy(anotherFamilyOfThisWoman.get(),family,familyWithBigamy);
//            }
//        }
//        return familyWithBigamy;
//    }
//
//    public void loadFamilyWithBigamy(Family anotherFamily,Family family,List<FamilyWithAnomaly> familyWitBigamy){
//        if(anotherFamily.getDivorced().isEmpty()){
//            FamilyWithAnomaly familyWithAnomaly = new FamilyWithAnomaly();
//            familyWithAnomaly.setFamily(family);
//            familyWithAnomaly.setHusbandId(family.getHusbandId());
//            familyWithAnomaly.setAnotherFamilyOfAPerson(anotherFamily);
//            familyWitBigamy.add(familyWithAnomaly);
//        }
//
//    }
}

