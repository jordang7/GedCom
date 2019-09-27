package com.gedcom.models;

import java.util.List;

/**
 * Created by Meghana on 9/26/2019.
 */
public class IndiFamilyResponse {
    List<Family> familyList;

    public IndiFamilyResponse(List<Family> familyList, List<Individual> individualList) {
        this.familyList = familyList;
        this.individualList = individualList;
    }

    List<Individual> individualList;

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
