package com.gedcom.models;

import java.util.List;

/**
 * Created by Meghana on 9/26/2019.
 */
public class IndiFamilyResponse {
    List<Family> familyList;

    List<Individual> individualList;

    List<Family> ambiguosFamilyMarrDeathList;

    List<Family> ambiguousFamilyMarrDivList;

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
}
