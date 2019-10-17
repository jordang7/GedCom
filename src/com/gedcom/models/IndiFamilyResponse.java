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

    List<Family>  ambiguosFamilyMaleLastNames;

    public List<Family> getAmbiguosFamilyMaleLastNames() {
        return ambiguosFamilyMaleLastNames;
    }

    public void setAmbiguosFamilyMaleLastNames(List<Family> ambiguosFamilyMaleLastNames) {
        this.ambiguosFamilyMaleLastNames = ambiguosFamilyMaleLastNames;
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
}
