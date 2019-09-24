package com.gedcom.processor;

import java.util.*;

/**
 * Created by Meghana on 9/13/2019.
 */
public class GedcomProcessor {
    public List<String> parser(List<String> gedcomLines, HashSet<String> tagSet) {

        List<String> resultList = new ArrayList<>();
        for( String gedcomLine : gedcomLines){
            System.out.println( "-->" + gedcomLine);
            String parsedLine;
            String gedComSplit[] = gedcomLine.split(" ");
            if( gedComSplit.length >= 2) {
                if (tagSet.contains(gedComSplit[1])) {
                    if(isNameAndDateValid(gedComSplit)){
                        parsedLine = getGedcomLine(gedComSplit, "Y");
                    }else if(gedComSplit[1].equals("DATE") || gedComSplit[1].equals("NAME") ){
                        parsedLine = getGedcomLine(gedComSplit, "N");
                    }
                    else
                        parsedLine = getGedcomLine(gedComSplit, "Y");
                } else if (isIndiOrFam(gedComSplit)) {
                    parsedLine = (gedComSplit[0] + "|" + gedComSplit[2] + "|Y|" + gedComSplit[1]);
                } else {
                    if(gedComSplit.length<=2)
                        parsedLine = (gedComSplit[0]  +"|N|" + gedComSplit[1]);
                    else
                        parsedLine = (gedComSplit[0] + "|" + gedComSplit[2] + "|N|" + gedComSplit[1]);
                }

            }else {
                parsedLine = gedComSplit[0] +"|N|";
            }
            resultList.add(parsedLine);
            System.out.println("<--" + parsedLine);

        }
        return resultList;
    }

    private boolean isIndiOrFam(String[] gedComSplit) {
        return gedComSplit[0].equals("0") && (gedComSplit[2].equals("INDI") || gedComSplit[2].equals("FAM"));
    }

    private String getGedcomLine(String[] gedComSplit, String valid) {
        return gedComSplit[0] + "|" + gedComSplit[1] + "|"+ valid +"|" + String.join(" ", Arrays.copyOfRange(gedComSplit, 2, gedComSplit.length));
    }

    private boolean isNameAndDateValid(String[] gedComSplit) {
        return (gedComSplit[1].equals("DATE") && gedComSplit[0].equals("2")) ||
                gedComSplit[1].equals("NAME") && gedComSplit[0].equals("1");
    }
}
