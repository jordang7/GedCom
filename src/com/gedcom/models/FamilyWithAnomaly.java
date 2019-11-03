package com.gedcom.models;

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
public FamilyWithAnomaly(){

}
public FamilyWithAnomaly(Family family, String husbandId, String wifeId){
    this.family=family;
    this.husbandId = husbandId;
    this.wifeId = wifeId;
}
    public FamilyWithAnomaly(Family family, Individual husband, Individual wife,Family anotherFamilyOfAPerson) {
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
}
