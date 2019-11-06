package com.gedcom.models;

import java.util.List;

/**
 * Created by Meghana on 9/26/2019.
 */
public class IndiFamilyResponse {
    List<Family> familyList;
    
    List<Individual> ambiguousIndividuals;

    List<Individual> individualList;

    List<Family> ambiguosFamilyMarrDeathList;

    List<Family> ambiguousFamilyMarrDivList;
    
    List<Family> ambiguosbirthBeforeMarriageList;
    
    

    List<Family> ambiguousFamilyMarrBefore14;

   List<Family>  ambiguosFamilyDivorceDeathList;

    List<Family> ambiguousMaleLastNames;

    List<FamilyWithChildrenMarriedToEachOther> ambiguousSblingsMarriageList;
    
    List<FamilyWithAnomaly> ambiguousAuntUncleMarriedNNList;
    
    List<Family> ambiguousMoreThan15Children;

    List<FamilyWithOlderParents> familyWithOlderParents;


    private List<FamilyWithAnomaly> ambiguousDuplicateFirstNameFamilies;
    private List<FamilyWithAnomaly> missingCorrespondingEntries;

    public List<Family> getAmbiguousGenderForRoles() {
        return ambiguousGenderForRoles;
    }

    public void setAmbiguousGenderForRoles(List<Family> ambiguousGenderForRoles) {
        this.ambiguousGenderForRoles = ambiguousGenderForRoles;
    }

    List<Family> ambiguousGenderForRoles;

    public List<Individual> getAmbiguousIndividualIDList() {
        return ambiguousIndividualIDList;
    }

    public void setAmbiguousIndividualIDList(List<Individual> ambiguousIndividualIDList) {
        this.ambiguousIndividualIDList = ambiguousIndividualIDList;
    }

    List<Individual> ambiguousIndividualIDList;

    public List<Family> getAmbiguousFamilyIDList() {
        return ambiguousFamilyIDList;
    }

    public void setAmbiguousFamilyIDList(List<Family> ambiguousFamilyIDList) {
        this.ambiguousFamilyIDList = ambiguousFamilyIDList;
    }

    List<Family> ambiguousFamilyIDList;


    public List<FamilyWithOlderParents> getFamilyWithOlderParents() {
        return familyWithOlderParents;
    }

    public void setFamilyWithOlderParents(List<FamilyWithOlderParents> familyWithOlderParents) {
        this.familyWithOlderParents = familyWithOlderParents;
    }

    public List<FamilyWithChildrenMarriedToEachOther> getAmbiguousFirstCousinsMarriageList() {
        return ambiguousFirstCousinsMarriageList;
    }

    public void setAmbiguousFirstCousinsMarriageList(List<FamilyWithChildrenMarriedToEachOther> ambiguousFirstCousinsMarriageList) {
        this.ambiguousFirstCousinsMarriageList = ambiguousFirstCousinsMarriageList;
    }
    
    public List<FamilyWithAnomaly> getAmbiguousAuntUncleMarriedNN() {
        return ambiguousAuntUncleMarriedNNList;
    }

    public void setAuntUncleMarriedNN(List<FamilyWithAnomaly> ambiguousAuntUncleMarriedNNList) {
        this.ambiguousAuntUncleMarriedNNList = ambiguousAuntUncleMarriedNNList;
    }


    List<FamilyWithChildrenMarriedToEachOther> ambiguousFirstCousinsMarriageList;
    public List<Family> getAmbiguosbirthBeforeMarriageList() {
        return ambiguosbirthBeforeMarriageList;
    }

    public void setAmbiguosbirthBeforeMarriageList(List<Family> ambiguosbirthBeforeMarriageList) {
        this.ambiguosbirthBeforeMarriageList = ambiguosbirthBeforeMarriageList;
    }

    public List<FamilyWithParentMarriedToDescendants> getAmbiguousParentDescendantMarriageList() {
        return ambiguousParentDescendantMarriageList;
    }

    public void setAmbiguousParentDescendantMarriageList(List<FamilyWithParentMarriedToDescendants> ambiguousParentDescendantMarriageList) {
        this.ambiguousParentDescendantMarriageList = ambiguousParentDescendantMarriageList;
    }

    List<FamilyWithParentMarriedToDescendants> ambiguousParentDescendantMarriageList;

    public List<FamilyWithChildrenMarriedToEachOther> getAmbiguousSblingsMarriageList() {
        return ambiguousSblingsMarriageList;
    }

    public void setAmbiguousSblingsMarriageList(List<FamilyWithChildrenMarriedToEachOther> ambiguousSblingsMarriageList) {
        this.ambiguousSblingsMarriageList = ambiguousSblingsMarriageList;
    }


    public List<Family> getAmbiguousMaleLastNames() {
        return ambiguousMaleLastNames;
    }

    public void setAmbiguousMaleLastNames(List<Family> ambiguousMaleLastNames) {
        this.ambiguousMaleLastNames = ambiguousMaleLastNames;
    }


    public List<Family> getAmbiguousFamilyMarrBefore14() {
        return ambiguousFamilyMarrBefore14;
    }

    public void setAmbiguousFamilyMarrBefore14(List<Family> ambiguousFamilyMarrBefore14) {
        this.ambiguousFamilyMarrBefore14 = ambiguousFamilyMarrBefore14;
    }

    public List<Family> getAmbiguousFamilyMarrDivList() {
        return ambiguousFamilyMarrDivList;
    }

    public void setAmbiguousFamilyMarrDivList(List<Family> ambiguousFamilyMarrDivList) {
        this.ambiguousFamilyMarrDivList = ambiguousFamilyMarrDivList;
    }


    public IndiFamilyResponse() {

    }

    public List<Family> getAmbiguosFamilyMarrDeathList() {
        return ambiguosFamilyMarrDeathList;
    }

    public void setAmbiguosFamilyMarrDeathList(List<Family> ambiguosFamilyMarrDeathList) {
        this.ambiguosFamilyMarrDeathList = ambiguosFamilyMarrDeathList;
    }
    
    public List<Individual> getAmbiguousIndividuals() {
        return ambiguousIndividuals;
    }

    public void setAmbiguousIndividuals(List<Individual> ambiguousIndividuals) {
        this.ambiguousIndividuals = ambiguousIndividuals;
    }
    
    public List<Family> getAmbiguosbirthBeforeMarriage() {
        return ambiguosbirthBeforeMarriageList;
    }

    public void setAmbiguosbirthBeforeMarriage(List<Family> ambiguosbirthBeforeMarriageList) {
        this.ambiguosbirthBeforeMarriageList = ambiguosbirthBeforeMarriageList;
    }
    
    public List<Family> getFamilyList() {
        return familyList;
    }

    public void setFamilyList(List<Family> familyList) {
        this.familyList = familyList;
    }

    public List<Individual> getIndividualList() {
        return individualList;
    }

    public void setIndividualList(List<Individual> individualList) {
        this.individualList = individualList;
    }
      public List<Family> getAmbiguosFamilyDivorceDeathList() {
        return ambiguosFamilyDivorceDeathList;
    }

    public void setAmbiguosFamilyDivorceDeathList(List<Family> ambiguosFamilyDivorceDeathList) {
        this.ambiguosFamilyDivorceDeathList = ambiguosFamilyDivorceDeathList;
    }
    public List<Family> getAmbiguousMoreThan15Children() {
        return ambiguousMoreThan15Children;
    }

    public void setAmbiguousMoreThan15Children(List<Family> ambiguousMoreThan15Children) {
        this.ambiguousMoreThan15Children = ambiguousMoreThan15Children;
    }
    List<FamilyWithAnomaly> ambiguousBigamyList;

    public List<FamilyWithAnomaly> getAmbiguousBigamyList() {
        return ambiguousBigamyList;
    }

    public void setAmbiguousBigamyList(List<FamilyWithAnomaly> ambiguousBigamyList) {
        this.ambiguousBigamyList = ambiguousBigamyList;
    }
    public List<FamilyWithAnomaly> getAmbiguousDuplicateFirstNameFamilies() {
        return ambiguousDuplicateFirstNameFamilies;
    }

    public void setAmbiguousDuplicateFirstNameFamilies(List<FamilyWithAnomaly> ambiguousDuplicateFirstNameFamilies) {
        this.ambiguousDuplicateFirstNameFamilies = ambiguousDuplicateFirstNameFamilies;
    }
    public List<FamilyWithAnomaly> getMissingCorrespondingEntries() {
        return missingCorrespondingEntries;
    }

    public void setMissingCorrespondingEntries(List<FamilyWithAnomaly> missingCorrespondingEntries) {
        this.missingCorrespondingEntries = missingCorrespondingEntries;
    }
}
