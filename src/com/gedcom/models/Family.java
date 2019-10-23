package com.gedcom.models;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    private Optional<Individual> husbandIndi = Optional.empty();
    private Optional<Individual> wifeIndi = Optional.empty();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Family)) return false;

        Family family = (Family) o;

        if (!getId().equals(family.getId())) return false;
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
        if (getDivorced() != null ? !getDivorced().equals(family.getDivorced()) : family.getDivorced() != null)
            return false;
        if (getHusbandIndi() != null ? !getHusbandIndi().equals(family.getHusbandIndi()) : family.getHusbandIndi() != null)
            return false;
        if (getWifeIndi() != null ? !getWifeIndi().equals(family.getWifeIndi()) : family.getWifeIndi() != null)
            return false;
        return getChildrenIndis() != null ? getChildrenIndis().equals(family.getChildrenIndis()) : family.getChildrenIndis() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getMarried() != null ? getMarried().hashCode() : 0);
        result = 31 * result + (getHusbandId() != null ? getHusbandId().hashCode() : 0);
        result = 31 * result + (getHusbandName() != null ? getHusbandName().hashCode() : 0);
        result = 31 * result + (getWifeId() != null ? getWifeId().hashCode() : 0);
        result = 31 * result + (getWifeName() != null ? getWifeName().hashCode() : 0);
        result = 31 * result + (getChildren() != null ? getChildren().hashCode() : 0);
        result = 31 * result + (getDivorced() != null ? getDivorced().hashCode() : 0);
        result = 31 * result + (getHusbandIndi() != null ? getHusbandIndi().hashCode() : 0);
        result = 31 * result + (getWifeIndi() != null ? getWifeIndi().hashCode() : 0);
        result = 31 * result + (getChildrenIndis() != null ? getChildrenIndis().hashCode() : 0);
        return result;
    }

    public Optional<Individual> getHusbandIndi() {
        return husbandIndi;
    }

    public void setHusbandIndi(Optional<Individual> husbandIndi) {
        this.husbandIndi = husbandIndi;
    }

    public Optional<Individual> getWifeIndi() {
        return wifeIndi;
    }

    public void setWifeIndi(Optional<Individual> wifeIndi) {
        this.wifeIndi = wifeIndi;
    }

    public List<Individual> getChildrenIndis() {
        return childrenIndis;
    }

    public void setChildrenIndis(List<Individual> childrenIndis) {
        this.childrenIndis = childrenIndis;
    }

    public void addChildrenIndi( Optional<Individual> child ){
        if( child.isPresent() )
        this.childrenIndis.add( child.get() );
    }

    private List<Individual> childrenIndis = new ArrayList<>();

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

    public String getFamilyLastName(){
        if( husbandIndi.isPresent() ){
            return husbandIndi.get().getLastName();
        } else {
            return "";
        }
    }


}
