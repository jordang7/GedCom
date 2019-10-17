
package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.FamilyWithChildrenMarriedToEachOther;
import com.gedcom.models.Individual;
import com.sun.org.apache.bcel.internal.generic.LUSHR;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GedComValidatorTest {

    GedcomValidator validator = new GedcomValidator();
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void testMarriageBeforeDivorce() {
        List<Family> ambiguousFamily = validator.marriageBeforeDivorce( new ArrayList<Family>());
        assertEquals( 0, ambiguousFamily.size());

        Family fam1 = new Family("id1");
        fam1.setDivorced(LocalDate.parse("10 MAY 2018 ", GedcomValidator.formatter));
        fam1.setMarried(LocalDate.parse("02 MAY 2018 ", GedcomValidator.formatter));

        Family fam2 = new Family("id2");
        fam2.setDivorced(LocalDate.parse("10 MAY 2018 ",GedcomValidator.formatter));
        fam2.setMarried(LocalDate.parse("10 MAY 2019 ",GedcomValidator.formatter));

        List<Family> families = new ArrayList<>();
        families.add(fam1);
        families.add(fam2);
        List<Family> ambiguousFamily2 = validator.marriageBeforeDivorce( families );
        assertEquals( 1, ambiguousFamily2.size());

        assertEquals("id2",ambiguousFamily2.get(0).getId());
    }

    @org.junit.jupiter.api.Test
    void testMarriageBeforeDeath() {
        List<Family> ambiguousFamily = validator.marriageBeforeDeath(new ArrayList<Individual>(),new ArrayList<Family>());
        assertEquals( 0, ambiguousFamily.size());
        Family fam1 = new Family("id1");
        fam1.setHusbandId("H1");
        fam1.setWifeId("W1");
        fam1.setMarried(LocalDate.parse("5 MAY 2019 ",GedcomValidator.formatter));

        List<Family> familyList = new ArrayList<>();
        familyList.add(fam1);
        List<Individual> individualList = new ArrayList<>();

        Individual h1 = new Individual("H1");
        h1.setDeathDate(LocalDate.parse("5 OCT 2018 ",GedcomValidator.formatter));
        individualList.add(h1);

        Individual w1 = new Individual("W1");
        w1.setDeathDate(LocalDate.parse("4 OCT 2018 ",GedcomValidator.formatter));
        individualList.add(w1);
        List<Family> ambiguousFamily1 = validator.marriageBeforeDeath(individualList,familyList);
        assertEquals( 1, ambiguousFamily1.size());

    }
    @org.junit.jupiter.api.Test
    void testBirthBeforeMarriage() {
        List<Family> ambiguousFamily = validator.birthBeforeMarriage(new ArrayList<Individual>(),new ArrayList<Family>());
        assertEquals( 0, ambiguousFamily.size());
        Family fam1 = new Family("id1");
        fam1.setHusbandId("H1");
        fam1.setWifeId("W1");
        fam1.setMarried( LocalDate.parse("5 MAY 2010 ", GedcomValidator.formatter));

        List<Family> familyList = new ArrayList<>();
        familyList.add(fam1);
        List<Individual> individualList = new ArrayList<>();

        Individual h1 = new Individual("H1");
        h1.setBdate(LocalDate.parse("5 OCT 2018 ", GedcomValidator.formatter));
        individualList.add(h1);

        Individual w1 = new Individual("W1");
        w1.setBdate(LocalDate.parse("4 OCT 2009 ", GedcomValidator.formatter));
        individualList.add(w1);
        List<Family> ambiguousFamily1 = validator.birthBeforeMarriage(individualList,familyList);
        assertEquals( 1, ambiguousFamily1.size());

    }
    @org.junit.jupiter.api.Test
    void testBirthBeforeDeath() {
        List<Individual> ambiguousINDI = validator.birthBeforeDeath(new ArrayList<Individual>());
        assertEquals(0,ambiguousINDI.size());
        List<Individual> individualList = new ArrayList<>();

        Individual h1 = new Individual("H1");
        h1.setBdate(LocalDate.parse("5 OCT 2018 ", GedcomValidator.formatter));
        h1.setDeathDate(LocalDate.parse( "4 OCT 2018 ", GedcomValidator.formatter));
        individualList.add(h1);

        Individual w1 = new Individual("W1");
        w1.setBdate(LocalDate.parse("5 OCT 2018 ", GedcomValidator.formatter));
        w1.setDeathDate(LocalDate.parse("6 OCT 2018 ", GedcomValidator.formatter));
        
        individualList.add(w1);
        List<Individual> ambiguousFamily1 = validator.birthBeforeDeath(individualList);
        assertEquals( 1, ambiguousFamily1.size());

    }
    @org.junit.jupiter.api.Test
    void testMarriageBefore14(){
       List<Family> ambiguousMarriagebefore14 = validator.marriageBefore14(new ArrayList<Individual>(), new ArrayList<Family>());
       assertEquals(0,ambiguousMarriagebefore14.size());

       List<Family> families = new ArrayList<>();
       List<Individual> individuals = new ArrayList<>();

       Individual husband = new Individual("Id1");
       husband.setBdate(LocalDate.parse("5 OCT 1980 ", GedcomValidator.formatter));

       Individual wife = new Individual("Id2");
       wife.setBdate(LocalDate.parse("6 OCT 2000 ",GedcomValidator.formatter));

       individuals.add(husband);
       individuals.add(wife);

       Family family= new Family("FID1");
       family.setWifeId(wife.getId());
       family.setHusbandId(husband.getId());
       family.setMarried(LocalDate.parse("6 OCT 2012 ",GedcomValidator.formatter));

       families.add(family);

        List<Family> ambiguousMarriagebefore14Second = validator.marriageBefore14(individuals,families);
        assertEquals(1,ambiguousMarriagebefore14Second.size());




    }

    //US16 Meghana
    @org.junit.jupiter.api.Test
    void testMaleLastNames(){
        List<Family> ambiguousMaleLastName = validator.maleNamesSameCheck(new ArrayList<Family>());
        assertEquals(0,ambiguousMaleLastName.size());
        List<Family> families = new ArrayList<>();

        Individual husband = new Individual("I1US16");
        husband.setName("Sri /Madhava/");
        husband.setGender("M");

        Individual wife = new Individual("I2US16");
        wife.setName("Meg /Madhava/");
        wife.setGender("F");

        Individual child = new Individual("I3US16");
        child.setName("Megsri /Mad/");
        child.setGender("M");

        Family family = new Family("F1US16");

        family.setHusbandIndi(Optional.of(husband));
        family.setWifeIndi(Optional.of(wife));
        family.addChildrenIndi(Optional.of(child));

        families.add(family);

        List<Family> ambiguousMaleLastName1 = validator.maleNamesSameCheck(families);
        assertEquals(1,ambiguousMaleLastName1.size());

    }

    //US18 Meghana
    @org.junit.jupiter.api.Test
    void testSiblingsShouldNotMArry(){
        List<FamilyWithChildrenMarriedToEachOther> ambigousSiblingMArriage = validator.siblingsShouldNotMarry(new ArrayList<Family>());
        assertEquals(0,ambigousSiblingMArriage.size());
        List<Family> families = new ArrayList<>();

        Individual husband = new Individual("I1US16");
        husband.setName("Sri /Madhava/");
        husband.setGender("M");

        Individual wife = new Individual("I2US16");
        wife.setName("Meg /Madhava/");
        wife.setGender("F");

        Individual child = new Individual("I3US16");
        child.setName("Megsri /Mad/");
        child.setGender("M");

        Individual child2 = new Individual("I3US16");
        child2.setName("SriMeg /Mad/");
        child2.setGender("F");

        Family family = new Family("F1US16");
        family.setHusbandId(husband.getId());
        family.setWifeId(wife.getId());
        family.setHusbandIndi(Optional.of(husband));
        family.setWifeIndi(Optional.of(wife));
        family.addChildrenIndi(Optional.of(child));
        family.addChildrenIndi(Optional.of(child2));

        families.add(family);

        Family family1 = new Family("F1US18");
        family1.setHusbandId(child.getId());
        family1.setWifeId(child2.getId());
        family1.setHusbandIndi(Optional.of(child));
        family1.setWifeIndi(Optional.of(child2));

        families.add(family1);

        List<FamilyWithChildrenMarriedToEachOther> ambigousSiblingMArriage1 = validator.siblingsShouldNotMarry(families);
        assertEquals(1,ambigousSiblingMArriage1.size());



    }


}
