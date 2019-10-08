package com.gedcom.util;

import com.gedcom.models.Individual;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GedComUtil {
    public boolean checkIfDivorcedBeforeDeath(Individual individual ){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
           return sdf.parse(individual.getDivorceDate()).before(sdf.parse(individual.getDeathDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
