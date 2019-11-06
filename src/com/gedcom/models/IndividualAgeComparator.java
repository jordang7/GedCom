package com.gedcom.models;

import java.util.Comparator;

/**
 * Created by Meghana on 11/5/2019.
 */

public class IndividualAgeComparator implements Comparator<Individual>{

    public int compare(Individual a, Individual b)
    {
        return b.getAge() - a.getAge();
    }

}
