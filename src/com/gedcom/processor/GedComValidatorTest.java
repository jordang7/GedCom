
package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.FamilyWithAnomaly;
import com.gedcom.models.FamilyWithChildrenMarriedToEachOther;
import com.gedcom.models.Individual;

//import com.sun.org.apache.bcel.internal.generic.LUSHR;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
    //US21 Meghana
    @org.junit.jupiter.api.Test
    void testGenderForRoles(){
        List<Family> ambiguousGenderForRoles = validator.checkCorrectGenderforRoles(new ArrayList<Family>());
        assertEquals(0,ambiguousGenderForRoles.size());

        List<Family> family = new ArrayList<>();

        Individual husband = new Individual("I1US21");
        husband.setGender("F");

        Individual wife = new Individual("I2US21");
        wife.setGender("M");

        Family family1 = new Family("F1US21");

        family1.setHusbandIndi(Optional.of(husband));
        family1.setWifeIndi(Optional.of(wife));


        family.add(family1);
        List<Family> ambiguousGenderForRoles1 = validator.checkCorrectGenderforRoles(family);
        assertEquals(1,ambiguousGenderForRoles1.size());

    }
    //US22 - Meghana
    @org.junit.jupiter.api.Test
    void testUniqueIndividualFamilyID(){
        List<Individual> ambiguousIndividualIDList = validator.uniqueID(new ArrayList<Individual>());
        assertEquals(0,ambiguousIndividualIDList.size());

        List<Individual> individualList = new ArrayList<>();
        Individual husband = new Individual("I1US21");
        Individual wife = new Individual("I1US21");

        ambiguousIndividualIDList = validator.uniqueID(Arrays.asList(husband, wife));
        assertEquals(1,ambiguousIndividualIDList.size());
        List<Family> ambiguousFamilyIDList = validator.uniqueFamilyID(new ArrayList<Family>());
        assertEquals(0,ambiguousFamilyIDList.size());

        Family family1 = new Family("F1US21");
        Family family2 = new Family("F1US21");

        ambiguousFamilyIDList = validator.uniqueFamilyID(Arrays.asList(family1, family2));
        assertEquals(1,ambiguousFamilyIDList.size());

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

    @org.junit.jupiter.api.Test
    void testFewerThan15Children() {
        List<Family> ambiguousMoreThan15Children = validator.fewerThan15Children(new ArrayList<Family>());
        assertEquals(0,ambiguousMoreThan15Children.size());
        Family family = new Family("F1US15");
        List<Family> families = new ArrayList<>();
        
        Individual child1 = new Individual("I1US15");
        child1.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child1));
        Individual child2 = new Individual("I2US15");
        child2.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child2));
        Individual child3 = new Individual("I3US15");
        child3.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child3));
        Individual child4 = new Individual("I4US15");
        child4.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child4));
        Individual child5 = new Individual("I5US15");
        child5.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child5));
        Individual child6 = new Individual("I6US15");
        child6.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child6));
        Individual child7 = new Individual("I7US15");
        child7.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child7));
        Individual child8 = new Individual("I8US15");
        child8.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child8));
        Individual child9 = new Individual("I9US15");
        child9.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child9));
        Individual child10 = new Individual("I10US15");
        child10.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child10));
        Individual child11 = new Individual("I11US15");
        child11.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child11));
        Individual child12 = new Individual("I12US15");
        child12.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child12));
        Individual child13 = new Individual("I13US15");
        child13.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child13));
        Individual child14 = new Individual("I14US15");
        child14.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child14));
        Individual child15 = new Individual("I15US15");
        child15.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child15));
        Individual child16 = new Individual("I16US15");
        child15.setName("Megsri /Mad/");
        family.addChildrenIndi(Optional.of(child16));
		families.add(family);
        
        List<Family> ambiguousMoreThan15Children1 = validator.fewerThan15Children(families);
        assertEquals(1,ambiguousMoreThan15Children1.size());
    }
    @org.junit.jupiter.api.Test
    void testUniqueFirstNameInFamily(){
        List<FamilyWithAnomaly> familyWithAnomalyList = validator.firstNamesShouldBeUniqueInTheFamily(new ArrayList<Individual>(),new ArrayList<Family>());
        assertEquals(0,familyWithAnomalyList.size());
        Individual husband = new Individual("I1US25");
        husband.setName("I1/ US25 ");
        Individual wife = new Individual("I2US25");
        wife.setName("I1/US25");
        Family family = new Family("F1US25");
        family.setHusbandIndi(Optional.of(husband));
        family.setWifeIndi(Optional.of(wife));
        familyWithAnomalyList = validator.firstNamesShouldBeUniqueInTheFamily(Arrays.asList(husband, wife),Arrays.asList(family));
        assertEquals(1,familyWithAnomalyList.size());
    }
    @org.junit.jupiter.api.Test
    void testBigami(){
        List<FamilyWithAnomaly> familyWithAnomalyList = validator.noBigamyIsAllowed(new ArrayList<Individual>(),new ArrayList<Family>());
        assertEquals(0,familyWithAnomalyList.size());
        Individual person1 = new Individual("I1US11");
        person1.setName("I1/ US11");
        person1.setGender("M");
        person1.setSpouse("F1US11");

        Individual person2 = new Individual("I2US11");
        person2.setName("I2/US25");
        person2.setGender("F");
        person2.setSpouse("F1US11");
        Individual person3 = new Individual("I3US11");
        person3.setName("I3/US25");
        person3.setGender("F");
        person3.setSpouse("F2US11");
        Family family1 = new Family("F1US11");
        family1.setHusbandIndi(Optional.of(person1));
        family1.setWifeIndi(Optional.of(person2));

        Family family2 = new Family("F2US11");
        family2.setHusbandIndi(Optional.of(person1));
        family2.setWifeIndi(Optional.of(person3));
        familyWithAnomalyList = validator.noBigamyIsAllowed(Arrays.asList(person1, person1,person3),Arrays.asList(family1,family2));
        assertEquals(0,familyWithAnomalyList.size());
        family1.setHusbandId(person1.getId());
        family1.setWifeId(person2.getId());
        family2.setHusbandId(person1.getId());
        family2.setWifeId(person3.getId());
        familyWithAnomalyList = validator.noBigamyIsAllowed(Arrays.asList(person1, person1,person3),Arrays.asList(family1,family2));
        assertEquals(2,familyWithAnomalyList.size());
    }

    @org.junit.jupiter.api.Test
    void testOrphanChildren(){
        List<Individual> orphanList = validator.orphanChildren(new ArrayList<Individual>(),new ArrayList<Family>());
        assertEquals(0,orphanList.size());

        Individual indi1 = new Individual("I1");
        Individual indi2 = new Individual("I2");
        Individual indi3 = new Individual("I3");
        Individual indi4 = new Individual("I4");

        indi1.setDeathDate(LocalDate.parse("02 MAY 2018 ", GedcomValidator.formatter));
        indi2.setDeathDate(LocalDate.parse("02 MAY 2018 ", GedcomValidator.formatter));
        indi3.setAge(15);
        indi4.setAge(15);

        List<Individual> allIndividual = new ArrayList<>();
        allIndividual.add(indi1);
        allIndividual.add(indi2);
        allIndividual.add(indi3);
        allIndividual.add(indi4);

        List<Family> fam = new ArrayList<>();
        Family f = new Family("F1");
        f.setHusbandId("I1");
        f.setWifeId("I2");
        List<Individual> children = new ArrayList<>();
        children.add(indi3);
        children.add(indi4);
        f.setChildrenIndis(children);
        fam.add(f);
        List<Individual> orphanList1 = validator.orphanChildren(new ArrayList<Individual>(allIndividual),new ArrayList<Family>(fam));
        assertEquals(2,orphanList1.size());

    }
    @org.junit.jupiter.api.Test
    void testLivingSingle(){
        List<Individual> livingSingleList = validator.livingSingle(new ArrayList<Individual>());
        assertEquals(0, livingSingleList.size());

        Individual indi1 = new Individual("I1US31");
        indi1.setAge(35);
        List<Individual> singles = new ArrayList<>();
        singles.add(indi1);
        List<Individual> livingSingleList1 = validator.livingSingle(new ArrayList<Individual>(singles));
        assertEquals(1,livingSingleList1.size());

        Individual indi2 = new Individual("I1US31");
        indi2.setAge(35);
        indi2.setSpouse("F1US31");

        List<Individual> notSingles = new ArrayList<>();
        notSingles.add(indi2);
        List<Individual> livingSingleList2 = validator.livingSingle(new ArrayList<Individual>(notSingles));
        assertEquals(0,livingSingleList2.size());


    }

    @org.junit.jupiter.api.Test
    void testAncestorsChildMarriage(){
        List<FamilyWithAnomaly> familyWithAnomalyList = validator.noBigamyIsAllowed(new ArrayList<Individual>(),new ArrayList<Family>());
        assertEquals(0,familyWithAnomalyList.size());
        Individual person1 = new Individual("I1US11");
        person1.setName("I1/ US11");
        person1.setGender("M");
        person1.setSpouse("F1US11");

        Individual person2 = new Individual("I2US11");
        person2.setName("I2/US25");
        person2.setGender("F");
        person2.setSpouse("F1US11");
        Individual person3 = new Individual("I3US11");
        person3.setName("I3/US25");
        person3.setGender("F");
        person3.setSpouse("F2US11");
        Family family1 = new Family("F1US11");
        family1.setHusbandIndi(Optional.of(person1));
        family1.setWifeIndi(Optional.of(person2));

        Family family2 = new Family("F2US11");
        family2.setHusbandIndi(Optional.of(person1));
        family2.setWifeIndi(Optional.of(person3));
        familyWithAnomalyList = validator.noBigamyIsAllowed(Arrays.asList(person1, person2,person3),Arrays.asList(family1,family2));
        assertEquals(0,familyWithAnomalyList.size());
        family1.setHusbandId(person1.getId());
        family1.setWifeId(person2.getId());
        family2.setHusbandId(person1.getId());
        family2.setWifeId(person3.getId());
        familyWithAnomalyList = validator.noBigamyIsAllowed(Arrays.asList(person1, person2,person3),Arrays.asList(family1,family2));
        assertEquals(2,familyWithAnomalyList.size());
    }

    @org.junit.jupiter.api.Test
    void testNoCorrespoindingEntries(){
        List<FamilyWithAnomaly> familyWithAnomalyList = validator.noBigamyIsAllowed(new ArrayList<Individual>(),new ArrayList<Family>());
        assertEquals(0,familyWithAnomalyList.size());
        Individual person1 = new Individual("I1US26");
        person1.setName("I1/ US26");
        person1.setGender("M");
        person1.setSpouse("F1US26");
        Family family1 = new Family("F2US26");
        family1.setHusbandIndi(Optional.of(new Individual("I2US26")));
        familyWithAnomalyList = validator.validateCorrespondingEntry(Arrays.asList(person1), Arrays.asList(family1));
        assertEquals(1,familyWithAnomalyList.size());
    }
    @org.junit.jupiter.api.Test
    void testAgeDifference(){
        List<FamilyWithAnomaly> familyWithAnomalyList = validator.loadCouplesWithLargeAgeDifference(new ArrayList<Family>());
        assertEquals(0,familyWithAnomalyList.size());
        Individual husband = new Individual("I1US34");
        husband.setName("I1/ US34");
        husband.setGender("M");
        husband.setSpouse("F1US34");
    //   husband.setBdate(LocalDate.parse("5 OCT 1975", GedcomValidator.formatter));

        Individual wife = new Individual("I2US34");
        wife.setName("I2/ US34");
        wife.setGender("F");
        wife.setSpouse("F1US34");
     //   wife.setBdate(LocalDate.parse("05 OCT 1999", GedcomValidator.formatter));


        Family family1 = new Family("F1US34");
        family1.setHusbandIndi(Optional.of(husband));
        family1.setWifeIndi(Optional.of(wife));
    //    family1.setMarried(LocalDate.parse("05 May 2019", GedcomValidator.formatter));
        familyWithAnomalyList = validator.loadCouplesWithLargeAgeDifference( Arrays.asList(family1));
        assertEquals(1,familyWithAnomalyList.size());

    }
    @org.junit.jupiter.api.Test
    void testListOfDeathsInLast30Days(){
        List<Individual> deaths = validator.listPeopleWhoDiedWithin30Days(new ArrayList<Individual>());
        assertEquals(0,deaths.size());
        Individual person1 = new Individual("I1US36");
        person1.setName("I1/ US36");
        person1.setGender("M");
        person1.setDeathDate(LocalDate.now());
    //    person1.setDeathDate(LocalDate.parse("5 Nov 2019",GedcomValidator.formatter));
        deaths = validator.listPeopleWhoDiedWithin30Days(Arrays.asList(person1));
        assertEquals(1,deaths.size());
    }

    @org.junit.jupiter.api.Test
    void testListOfBirthsInLast30Days(){
        List<Individual> births = validator.listPeopleWhoBornWithin30Days(new ArrayList<Individual>());
        assertEquals(1,births.size());
        Individual person1 = new Individual("I1US36");
        person1.setName("I1/ US36");
        person1.setGender("M");
        person1.setBdate(LocalDate.now());
        //    person1.setDeathDate(LocalDate.parse("5 Nov 2019",GedcomValidator.formatter));
        births = validator.listPeopleWhoBornWithin30Days(Arrays.asList(person1));
        assertEquals(2,births.size());
    }

    @org.junit.jupiter.api.Test
    void testListOfAliveSpouseAndChildOfPeopleDeadIn30ays(){
        List<Individual> people = validator.listSpouseAndChildOfPeopleDeadIn30Days(new ArrayList<Individual>(), new ArrayList<Family>());
        assertEquals(0,people.size());
        Individual person1 = new Individual("I1US36");
        Family family1= new Family("I9896");
        person1.setName("I1/ US36");
        person1.setGender("M");
        person1.setBdate(LocalDate.now());
        //    person1.setDeathDate(LocalDate.parse("5 Nov 2019",GedcomValidator.formatter));
        people = validator.listSpouseAndChildOfPeopleDeadIn30Days(Arrays.asList(person1),Arrays.asList(family1));
        assertEquals(0,people.size());
    }
}
