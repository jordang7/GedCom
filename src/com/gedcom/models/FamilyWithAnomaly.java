package com.gedcom.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sri on 10/17/2019.
 */
public class FamilyWithAnomaly {
    Family family;
    Individual husband;
    Individual wife;
    Family anotherFamilyOfAPerson;
    private String husbandId;
    private String wifeId;
    private Set<String> nocorrespondingEntry;
    private Set<String> duplicateNamesInFamily;
public FamilyWithAnomaly(){

}
public FamilyWithAnomaly(Family family, String husbandId, String wifeId){
    this.family=family;
    this.husbandId = husbandId;
    this.wifeId = wifeId;
}
    public FamilyWithAnomaly(Family family, Individual husband, Individual wife) {
        this.family = family;
        this.husband = husband;
        this.wife = wife;
        this.setAnotherFamilyOfAPerson(anotherFamilyOfAPerson);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FamilyWithChildrenMarriedToEachOther)) return false;

        FamilyWithChildrenMarriedToEachOther that = (FamilyWithChildrenMarriedToEachOther) o;

        if (getFamily() != null ? !getFamily().equals(that.getFamily()) : that.getFamily() != null) return false;
        if (getHusband() != null ? !getHusband().equals(that.getHusband()) : that.getHusband() != null) return false;
        return getWife() != null ? getWife().equals(that.getWife()) : that.getWife() == null;
    }

    @Override
    public int hashCode() {
        int result = getFamily() != null ? getFamily().hashCode() : 0;
        result = 31 * result + (getHusband() != null ? getHusband().hashCode() : 0);
        result = 31 * result + (getWife() != null ? getWife().hashCode() : 0);
        return result;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Individual getHusband() {
        return husband;
    }

    public void setHusband(Individual husband) {
        this.husband = husband;
    }

    public Individual getWife() {
        return wife;
    }

    public void setWife(Individual wife) {
        this.wife = wife;
    }

    public Family getAnotherFamilyOfAPerson() {
        return anotherFamilyOfAPerson;
    }

    public void setAnotherFamilyOfAPerson(Family anotherFamilyOfAPerson) {
        this.anotherFamilyOfAPerson = anotherFamilyOfAPerson;
    }

    public String getHusbandId() {
        return husbandId;
    }

    public void setHusbandId(String husbandId) {
        this.husbandId = husbandId;
    }

    public String getWifeId() {
        return wifeId;
    }

    public void setWifeId(String wifeId) {
        this.wifeId = wifeId;
    }

    public Set<String> getDuplicateNamesInFamily() {
    if(duplicateNamesInFamily != null)
        return duplicateNamesInFamily;
    else {
        duplicateNamesInFamily = new HashSet<String>();
    return duplicateNamesInFamily;
    }
    }

    public void setDuplicateNamesInFamily(Set<String> duplicateNamesInFamily) {
        this.duplicateNamesInFamily = duplicateNamesInFamily;
    }
    public Set<String> getNocorrespondingEntry() {
        if(nocorrespondingEntry != null)
            return nocorrespondingEntry;
        else {
            nocorrespondingEntry = new HashSet<String>();
            return nocorrespondingEntry;
        }
   }

    public void setNocorrespondingEntry(Set<String> nocorrespondingEntry) {
        this.nocorrespondingEntry = nocorrespondingEntry;
    }


}
