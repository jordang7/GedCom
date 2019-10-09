
package com.gedcom.processor;

import com.gedcom.models.Family;
import com.gedcom.models.Individual;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Meghana on 10/8/2019.
 */

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
        fam1.setDivorced(" 10 MAY 2018");
        fam1.setMarried(" 02 MAY 2018");

        Family fam2 = new Family("id2");
        fam2.setDivorced(" 10 MAY 2018");
        fam2.setMarried(" 10 MAY 2019");

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
        fam1.setMarried(" 5 MAY 2019");

        List<Family> familyList = new ArrayList<>();
        familyList.add(fam1);
        List<Individual> individualList = new ArrayList<>();

        Individual h1 = new Individual("H1");
        h1.setDeath(" 5 OCT 2018");
        individualList.add(h1);

        Individual w1 = new Individual("W1");
        w1.setDeath(" 4 OCT 2018");
        individualList.add(w1);
        List<Family> ambiguousFamily1 = validator.marriageBeforeDeath(individualList,familyList);
        assertEquals( 1, ambiguousFamily1.size());

    }

}
