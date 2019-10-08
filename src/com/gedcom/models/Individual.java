package com.gedcom.models;

import java.time.LocalDate;

/**
 * Created by Meghana on 9/26/2019.
 */
public class Individual {
    private String id;
    private String name = "";
    private String gender = "";
    private String birthDay = "";
    private LocalDate bdate;
    private String age = "";
    private String alive = "";
    private String death = "";
    private String child = "";
    private String spouse = "";
 private String deathDate = "";
    private String divorceDate = "";


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

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public LocalDate getBdate() {
        return bdate;
    }

    public void setBdate(LocalDate bdate) {
        this.bdate = bdate;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Individual)) return false;

        Individual that = (Individual) o;

        if (!getId().equals(that.getId())) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getGender() != null ? !getGender().equals(that.getGender()) : that.getGender() != null) return false;
        if (getBirthDay() != null ? !getBirthDay().equals(that.getBirthDay()) : that.getBirthDay() != null)
            return false;
        if (getBdate() != null ? !getBdate().equals(that.getBdate()) : that.getBdate() != null) return false;
        if (getAge() != null ? !getAge().equals(that.getAge()) : that.getAge() != null) return false;
        if (getAlive() != null ? !getAlive().equals(that.getAlive()) : that.getAlive() != null) return false;
        if (getDeath() != null ? !getDeath().equals(that.getDeath()) : that.getDeath() != null) return false;
        if (getChild() != null ? !getChild().equals(that.getChild()) : that.getChild() != null) return false;
        return getSpouse() != null ? getSpouse().equals(that.getSpouse()) : that.getSpouse() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getGender() != null ? getGender().hashCode() : 0);
        result = 31 * result + (getBirthDay() != null ? getBirthDay().hashCode() : 0);
        result = 31 * result + (getBdate() != null ? getBdate().hashCode() : 0);
        result = 31 * result + (getAge() != null ? getAge().hashCode() : 0);
        result = 31 * result + (getAlive() != null ? getAlive().hashCode() : 0);
        result = 31 * result + (getDeath() != null ? getDeath().hashCode() : 0);
        result = 31 * result + (getChild() != null ? getChild().hashCode() : 0);
        result = 31 * result + (getSpouse() != null ? getSpouse().hashCode() : 0);
        return result;
    }
      public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public String getDivorceDate() {
        return divorceDate;
    }

    public void setDivorceDate(String divorceDate) {
        this.divorceDate = divorceDate;
    }
}
