package com.gedcom.models;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Created by Meghana on 9/26/2019.
 */
public class Individual {
    private String id;
    private String name = "";
    private String gender = "";
    //private String birthDay = "";
    private Optional<LocalDate> bdate = Optional.empty();
    private int age = 0;
    private String alive = "";
    //private String death = "";
    private String child = "";
    private String spouse = "";
    private Optional<LocalDate> deathDate = Optional.empty();
    //private String divorceDate = "";


    public Individual(String id) {
        this.id = id;
        this.alive = "Y";

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Optional<LocalDate> getBdate() {
        return bdate;
    }

    public void setBdate(Optional<LocalDate> bdate) {
        this.bdate = bdate;
    }

    public void setBdate(LocalDate bdate) {
        this.bdate = Optional.of(bdate);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public Optional<LocalDate> getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(Optional<LocalDate> deathDate) {
        this.deathDate = deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = Optional.of(deathDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual)) return false;

        Individual that = (Individual) o;

        if (getAge() != that.getAge()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getGender() != null ? !getGender().equals(that.getGender()) : that.getGender() != null) return false;
        if (getBdate() != null ? !getBdate().equals(that.getBdate()) : that.getBdate() != null) return false;
        if (getAlive() != null ? !getAlive().equals(that.getAlive()) : that.getAlive() != null) return false;
        if (getChild() != null ? !getChild().equals(that.getChild()) : that.getChild() != null) return false;
        if (getSpouse() != null ? !getSpouse().equals(that.getSpouse()) : that.getSpouse() != null) return false;
        return getDeathDate() != null ? getDeathDate().equals(that.getDeathDate()) : that.getDeathDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        result = 31 * result + (getBdate() != null ? getBdate().hashCode() : 0);
        result = 31 * result + getAge();
        result = 31 * result + (getAlive() != null ? getAlive().hashCode() : 0);
        result = 31 * result + (getChild() != null ? getChild().hashCode() : 0);
        result = 31 * result + (getSpouse() != null ? getSpouse().hashCode() : 0);
        result = 31 * result + (getDeathDate() != null ? getDeathDate().hashCode() : 0);
        return result;
    }
}
