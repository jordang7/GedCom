package com.gedcom.models;

import java.time.LocalDate;
import java.util.Optional;

public class Family {
    private String id;
    private Optional<LocalDate> married = Optional.empty();
    private String husbandId = "";
    private String husbandName = "";
    private String wifeId = "";
    private String wifeName = "";
    private String children = "";
    private Optional<LocalDate> divorced = Optional.empty();

    public Family(String id) {
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Optional<LocalDate> getMarried() {
        return married;
    }

    public void setMarried(LocalDate married) {
        this.married = Optional.of(married);
    }

    public String getHusbandId() {
        return husbandId;
    }

    public void setHusbandId(String husbandId) {
        this.husbandId = husbandId;
    }

    public String getHusbandName() {
        return husbandName;
    }

    public void setHusbandName(String husbandName) {
        this.husbandName = husbandName;
    }

    public String getWifeId() {
        return wifeId;
    }

    public void setWifeId(String wifeId) {
        this.wifeId = wifeId;
    }

    public String getWifeName() {
        return wifeName;
    }

    public void setWifeName(String wifeName) {
        this.wifeName = wifeName;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public Optional<LocalDate> getDivorced() {
        return divorced;
    }

    public void setDivorced(LocalDate divorced) {
        this.divorced = Optional.of(divorced);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Family)) return false;

        Family family = (Family) o;

        if (getId() != null ? !getId().equals(family.getId()) : family.getId() != null) return false;
        if (getMarried() != null ? !getMarried().equals(family.getMarried()) : family.getMarried() != null)
            return false;
        if (getHusbandId() != null ? !getHusbandId().equals(family.getHusbandId()) : family.getHusbandId() != null)
            return false;
        if (getHusbandName() != null ? !getHusbandName().equals(family.getHusbandName()) : family.getHusbandName() != null)
            return false;
        if (getWifeId() != null ? !getWifeId().equals(family.getWifeId()) : family.getWifeId() != null) return false;
        if (getWifeName() != null ? !getWifeName().equals(family.getWifeName()) : family.getWifeName() != null)
            return false;
        if (getChildren() != null ? !getChildren().equals(family.getChildren()) : family.getChildren() != null)
            return false;
        return getDivorced() != null ? getDivorced().equals(family.getDivorced()) : family.getDivorced() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getMarried() != null ? getMarried().hashCode() : 0);
        result = 31 * result + (getHusbandId() != null ? getHusbandId().hashCode() : 0);
        result = 31 * result + (getHusbandName() != null ? getHusbandName().hashCode() : 0);
        result = 31 * result + (getWifeId() != null ? getWifeId().hashCode() : 0);
        result = 31 * result + (getWifeName() != null ? getWifeName().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getDivorced() != null ? getDivorced().hashCode() : 0);
        return result;
    }
}
