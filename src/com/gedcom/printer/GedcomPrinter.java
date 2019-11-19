package com.gedcom.printer;

import com.gedcom.models.*;


import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Sri on 10/17/2019.
 */
public class GedcomPrinter {
	
	private List<String> gedcomLines;
	
    public List<String> getGedcomLines() {
		return gedcomLines;
	}


	public void setGedcomLines(List<String> gedcomLines2) {
		this.gedcomLines = gedcomLines2;
	}


    
    
    public int getLineNumber(String indiFamId)
    {
    	List<String> gedLinesList = getGedcomLines();
    	
    	int lineNo = 0;
    	
    	for(String line : gedLinesList )
    	{
    		lineNo++;
    		if(line.contains(indiFamId))
    		{
    			return lineNo;
    		}
    	}
		return lineNo;
    }
    
	public void printFamily(List<Family> familyArrayList) {
        System.out.println("FAMILY INFORMATION");
        System.out.println();
        System.out.println("+---------+--------------------------------+-------------------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+");
        System.out.println("|  ID     |                 Married        |   Divorced              |  HusbandId        |   Husband Name     |    wife Id      |     wife Name      |         Children  |");
        System.out.println("+---------+--------------------------------+-------------------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+");

        for (Family family : familyArrayList) {
            System.out.format(" %5s       %30s   %10s               %10s           %5s        %7s       %7s           %7s      ",
                    family.getId(), formatDate(family.getMarried()),formatDate(family.getDivorced()), family.getHusbandId(), family.getHusbandName(), family.getWifeId(), family.getWifeName(), family.getChildren());
            System.out.println();
        }
        System.out.println("+---------+--------------------------------+--------------------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+");

    }


    public void printListOfIndividualsBornBeforeParentsMarriage(List<Family> familyArrayList, List<Individual> individualArrayList) throws ParseException, java.text.ParseException {

        for (Family family : familyArrayList) {
            Optional<LocalDate> marriageDate = family.getMarried();
            String childrenString = family.getChildren();
            for(Individual indi : individualArrayList)
            {
                if(indi.getBdate().isPresent() && childrenString.contains( indi.getId()))
                {
                    LocalDate birthDate = indi.getBdate().get();
                    if( marriageDate.isPresent() && marriageDate.get().isAfter(birthDate))
                    {
                        System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY: " + "FAMILY : US08: " +family.getId() + ": CHILD "+ indi.getName() + "BORN "  + indi.getId() + " Before MARRIAGE: "+ formatDate(family.getMarried()));
                    }
                }
            }
        }
    }


    public void printListOfIndividualsBornAfterParentsDeath(List<Family> familyArrayList, List<Individual> individualArrayList) throws ParseException, java.text.ParseException {

        for (Family family : familyArrayList) {

            Optional<LocalDate> mDt = family.getMarried();

            String child = family.getChildren();

            String childArr[] = child.split(",");

            for(int i=0; i<individualArrayList.size(); i++)
            {
                if(individualArrayList.get(i).getId().equals(family.getHusbandId()))
                {
                    if(individualArrayList.get(i).getAlive().equals("N"))
                    {
                        Optional<LocalDate> deathDate = individualArrayList.get(i).getDeathDate();


                        for(String childId : childArr)
                        {
                            if(childId != null && childId != "")
                            {
                                for(int k=0; k<individualArrayList.size(); k++)
                                {
//                        			System.out.println(individualArrayList.get(k).getId() + " - " + childId);
                                    if(individualArrayList.get(k).getId().equals(childId))
                                    {
                                        Optional<LocalDate> bDate = individualArrayList.get(k).getBdate();


                                        if(bDate.isPresent() && deathDate.isPresent() && bDate.get().isAfter(deathDate.get()))
                                        {
                                            System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY: FAMILY: US09 : "+family.getId() + " " + individualArrayList.get(k).getId() + " BORN AFTER PARENT'S " + individualArrayList.get(i).getId() + " DEATH");
                                        }
                                    }
                                }

                            }
                        }
                    }
                }


                if(individualArrayList.get(i).getId().equals(family.getWifeId()))
                {
                    if(individualArrayList.get(i).getAlive().equals("N"))
                    {
                        Optional<LocalDate> deathDate = individualArrayList.get(i).getDeathDate();
                        for(String childId : childArr)
                        {
                            if(childId != null && childId != "")
                            {
                                for(int k=0; k<individualArrayList.size(); k++)
                                {
//                        			System.out.println(individualArrayList.get(k).getId() + " - " + childId);
                                    if(individualArrayList.get(k).getId().equals(childId))
                                    {
                                        Optional<LocalDate> bDate = individualArrayList.get(k).getBdate();

                                        if(bDate.get().isAfter(deathDate.get()))
                                        {
                                            System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY: FAMILY : US09 "+family.getId() + individualArrayList.get(k).getId() + " BORN AFTER PARENT'S " + individualArrayList.get(i).getId() + " DEATH");
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

    }

    public void printIndividualsWithAgeMoreThan150(List<Individual> individualArrayList) {

        for (Individual indi : individualArrayList) {
            if(indi.getAge() > 150)
            {
                System.out.println(" LINE -----> " + getLineNumber(indi.getId()) + " " +"ERROR: INDIVIDUAL : US07 :"+ indi.getId() +": MORE THAN 150 YEARS OLD - BIRTH DATE "+formatDate(indi.getBdate()));
            }
        }
    }
    
    public void printSiblingSpacingErrors(List<Family> familyArrayList, List<Individual> individualArrayList) {

        for (Family fam : familyArrayList) {
//            System.out.println(fam.getChildren());
        	
        	List<Individual> tempIndiList = new ArrayList<Individual>();
        	HashSet<String> tempHashSet = new HashSet<String>();
            
            for(int i=0; i<fam.getChildren().split(",").length; i++)
            {
            	for(Individual indi : individualArrayList)
                {
                	if(indi.getId().equals(fam.getChildren().split(",")[i]))
                	{
//                		System.out.println("Indi Name US13 ---- " + indi.getName());
                		tempIndiList.add(indi);
                	}
                }
            }
            
            for(Individual indi1 : tempIndiList)
            {
            	for(Individual indi2 : tempIndiList)
            	{
            		if(!indi1.getName().equals(indi2.getName()))
            		{
//            			System.out.println(Math.abs(indi1.getAge() - indi2.getAge()));
            			if(Math.abs(indi1.getAge() - indi2.getAge()) < 1)
            			{
            				if(!tempHashSet.contains(indi1.getName()+indi1.getLastName()) && !tempHashSet.contains(indi2.getName()+indi2.getLastName()))
            				{
            					tempHashSet.add(indi1.getName()+indi1.getLastName());
            					System.out.println(" LINE -----> " + getLineNumber(indi1.getId()) + " " +"ERROR: FAMILY : US13 : SIBLINGS "+ indi1.getName() + " " + indi1.getLastName() + ": AND "+ indi2.getName() + " " + indi2.getLastName() + " HAVE SPACING LESS THAN 1 YEAR");
            				}
            			}
            		}
            	}
            }
            
           
        }
    }
    
    public void printMultipleBirthsLessThan5Errors(List<Family> familyArrayList) {
    		
    	for(Family fam : familyArrayList)
    	{
    		String totalChildren = fam.getChildren();
    		
    		if(totalChildren.split(",").length >= 5)
    		{
    			System.out.println("ERROR: FAMILY : US14 : MULTIPLE BIRTHS "+ fam.getHusbandName() + " AND " + fam.getWifeName() + " GAVE MULTIPLE BIRTHS. THE COUNT IS "+ totalChildren.split(",").length);
    		}
    	}
    }

    public void printIndividuals(List<Individual> individualArrayList) {
        System.out.println("INDIVIDUAL INFORMATION");
        System.out.println();
        System.out.println("+---------+--------------------------------+-------------+-------------------+---------------+-----------------+--------------------+--------------------" +
                "+-------------------+");
        System.out.println("|  ID     |                 NAME           |   GENDER    |  Birthday         |   Age         |    Alive        |            Death   |    Child           |        Spouse     |");
        System.out.println("+---------+--------------------------------+-------------+-------------------+---------------+-----------------+--------------------+--------------------" +
                "+-------------------+");

        for (Individual indi : individualArrayList) {
           /* String child = indi.Child;
            if(indi.Child.equals(""))
                child = "N/A";*/
            System.out.format("   %5s   %30s %10s          %10s     %5s %7s                     %7s                                             %7s                                 %5s",
                    indi.getId(), indi.getName(), indi.getGender(), formatDate(indi.getBdate()), indi.getAge(), indi.getAlive(), formatDate(indi.getDeathDate()), indi.getChild(), indi.getSpouse());
            System.out.println();
        }
        System.out.println("+---------+--------------------------------+-------------+-------------------+--------------------+-----------------+--------------------+--------------------" +
                "+---------------------+");

    }

    public void printMarriageBeforeDivorceError(IndiFamilyResponse indiFamilyResponse) {
        for( Family family : indiFamilyResponse.getAmbiguousFamilyMarrDivList()) {
            System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR: FAMILY:US04:" +family.getId()+ " DIVORCED " + formatDate(family.getDivorced())+" BEFORE MARRIAGE" +formatDate(family.getMarried()));
        }
    }

    public void printMarriageBefore14Error(IndiFamilyResponse indiFamilyResponse){
        for (Family family : indiFamilyResponse.getAmbiguousFamilyMarrBefore14()) {

            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getWifeId())).findFirst().get();
            if (wife.getBdate().isPresent() && husband.getBdate().isPresent() && family.getMarried().isPresent()) {

                LocalDate familyMarriage = family.getMarried().get();
                Period p = Period.between(wife.getBdate().get(), familyMarriage);
                Period p1 = Period.between(husband.getBdate().get(), familyMarriage);
                if (p.getYears() <= 14)
                    System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY: FAMILY :US10:" + family.getId() + " WIFE AGE ( BDATE " +formatDate(wife.getBdate()) + " )DURING MARRIAGE WAS" + "BELOW 14 ( MARRIAGE DATE " + formatDate(family.getMarried()) + " )");
                if (p1.getYears() <= 14)
                    System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY: FAMILY :US10:" + family.getId() + " HUSBAND AGE ( BDATE " +formatDate(husband.getBdate()) + " )DURING MARRIAGE WAS" + "BELOW 14 ( MARRIAGE DATE " + formatDate(family.getMarried()) + " )");
            }
        }
    }

    public void printFamilyWithOlderParents(IndiFamilyResponse indiFamilyResponse){

        for(FamilyWithOlderParents famWithOlderParent : indiFamilyResponse.getFamilyWithOlderParents()){

            if( famWithOlderParent.getOlderHusband().isPresent() )
                System.out.println(" LINE -----> " + getLineNumber(famWithOlderParent.getFamily().getId()) + " " +"ANOMALY: FAMILY: US12: " + famWithOlderParent.getFamily().getId() + " HUSBAND " + famWithOlderParent.getOlderHusband().get().getName() + " WAS OLDER THAN 80 YEARS WHEN ONE OF HIS CHILD WAS BORN");
            if( famWithOlderParent.getOlderWife().isPresent() )
                System.out.println(" LINE -----> " + getLineNumber(famWithOlderParent.getFamily().getId()) + " " +"ANOMALY: FAMILY: US12: " + famWithOlderParent.getFamily().getId() + " WIFE " + famWithOlderParent.getOlderWife().get().getName() + " WAS OLDER THAN 60 YEARS WHEN ONE OF HER CHILD WAS BORN");


        }
    }

    public void printMarriageBeforeDeathError(IndiFamilyResponse indiFamilyResponse) {
        for (Family family : indiFamilyResponse.getAmbiguosFamilyMarrDeathList()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            Optional<LocalDate> wifeDeath = wife.getDeathDate();
            Optional<LocalDate> husbandDeath = husband.getDeathDate();


            if( wifeDeath.isPresent() && family.getMarried().isPresent() && wifeDeath.get().isBefore( family.getMarried().get()) ){
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR: FAMILY: US05:"+family.getId()+" MARRIED " +formatDate(family.getMarried())+ " AFTER WIFE'S DEATH ( "+wife.getId() +")"+ formatDate(wife.getDeathDate()));
            }
            if ( husbandDeath.isPresent() && family.getMarried().isPresent() && husbandDeath.get().isBefore( family.getMarried().get()) ){
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR: FAMILY: US05:"+family.getId()+" MARRIED " + formatDate(family.getMarried()) + " AFTER HUSBANDS'S DEATH (" +husband.getId() +")"+ formatDate(husband.getDeathDate()));
            }
        }
    }

    public void printBirthBeforeDeathError(IndiFamilyResponse indiFamilyResponse) {
        for( Individual indi : indiFamilyResponse.getAmbiguousIndividuals()) {
            System.out.println(" LINE -----> " + getLineNumber(indi.getId()) + " " +"ERROR: INDIVIDUAL:US03 " +indi.getId()+ " BIRTH " +formatDate(indi.getBdate()) +" AFTER DEATH " +formatDate(indi.getDeathDate()));
        }
    }
    public void printAmbiguosMaleLastNames(IndiFamilyResponse indiFamilyResponse) {
        for( Family family : indiFamilyResponse.getAmbiguousMaleLastNames()) {
            String familyLastName = family.getFamilyLastName();
            List<Individual> maleChildren = family.getChildrenIndis().stream().filter(ind -> ind.getGender().equals("M") && !ind.getLastName().equals(familyLastName)).collect(Collectors.toList());

            for( Individual child : maleChildren ) {
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY: FAMILY: US16 " + family.getId() + " MALE " + child.getLastName() + " LAST NAME IS DIFFERENT FROM FAMILY LASTNAME " + family.getFamilyLastName());
            }
        }
    }

    public void printAmbiguousGenderForRoles(IndiFamilyResponse indiFamilyResponse){
        for(Family family : indiFamilyResponse.getAmbiguousGenderForRoles()){

            Optional<Individual> husbandOpt = family.getHusbandIndi();
            Optional<Individual> wifeOpt = family.getWifeIndi();

            if(husbandOpt.isPresent()&& wifeOpt.isPresent()){
                String husbGender = husbandOpt.get().getGender();

                if(husbGender.equals("F") ){
                    System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY : FAMILY : US21 " + family.getId() + " Husband's Gender is Female Instead of Male");
                }
                String wifeGender = wifeOpt.get().getGender();
                if(wifeGender.equals("M")){
                    System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ANOMALY : FAMILY : US21 " + family.getId() + " Wife's Gender is Male Instead of Female");
                }
            }

        }
    }

    //US28 - Meghana
    public void printSiblingsByAge(List<Family> familyList){
        System.out.println("US28 : Siblings List with their name and age");
        for(Family family : familyList){
            List<Individual> childrenIndi = family.getChildrenIndis();
            Collections.sort(childrenIndi,new IndividualAgeComparator());
            if(!childrenIndi.isEmpty()) {
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"FAMILY ID" + family.getId());
                System.out.println("                       Name    :    Age       : DOB ");
                for (Individual siblings : childrenIndi) {
                    System.out.format("%30s   %5s    %6s", siblings.getName(), siblings.getAge(), formatDate(siblings.getBdate()));
                    System.out.println();
                }
            }
        }

    }
    public void printambiguousIndividualId(IndiFamilyResponse indiFamilyResponse){
        for(Individual individual : indiFamilyResponse.getAmbiguousIndividualIDList()){

            String indi = individual.getId();
            if(!indi.isEmpty()){
                System.out.println(" LINE -----> " + getLineNumber(individual.getId()) + " " +"ERROR : INDIVIDUAL : US22 " + individual.getId() + " already exists and used by other individual");
            }
        }
    }

    public void printambiguousFamilyId(IndiFamilyResponse indiFamilyResponse){
        for(Family family : indiFamilyResponse.getAmbiguousFamilyIDList()){

            String indi = family.getId();
            if(!indi.isEmpty()){
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR : FAMILY : US22 " + family.getId() + " already exists and used by other individual");
            }
        }
    }
    //US31 Meghana
    public void printLivingSingle(IndiFamilyResponse indiFamilyResponse){
        System.out.println(" US31 : List of living singles");
        for(Individual indi: indiFamilyResponse.getLivingSingleList()){
            System.out.println(" LINE -----> " + getLineNumber(indi.getId()) + " " +"INDIVIDUAL : " + indi.getId() +" Name :  "+ indi.getName() + "  Age :"+indi.getAge());
        }
    }
    //US33 Meghana
    public void printOrphanChildren(IndiFamilyResponse indiFamilyResponse){
        System.out.println("US33 : List of Orphan chidlren");
        for(Individual indi : indiFamilyResponse.getOrphanChildrenList()){
            System.out.println(" LINE -----> " + getLineNumber(indi.getChild()) + " " +"Individual : " + indi.getId() + " Name : " + indi.getName() + "AGE : "+
                    indi.getAge() +"  Family Id :" + indi.getChild() );
        }
    }
    public void printAmbiguosSiblingMarriageList(IndiFamilyResponse indiFamilyResponse){
        for(FamilyWithChildrenMarriedToEachOther familyWithChildrenMarriedToEachOther : indiFamilyResponse.getAmbiguousSblingsMarriageList()){
            System.out.println(" LINE -----> " + getLineNumber(familyWithChildrenMarriedToEachOther.getFamily().getId()) + " " +"ANOMALY : FAMILY : US18 "+ familyWithChildrenMarriedToEachOther.getFamily().getId() + " SIBLINGS " + familyWithChildrenMarriedToEachOther.getHusband().getName() + ", "+familyWithChildrenMarriedToEachOther.getWife().getName()+ " ARE MARRIED TO EACH OTHER");
        }
    }

    public void printBirthBeforeMarriageError(IndiFamilyResponse indiFamilyResponse) {
        for (Family family : indiFamilyResponse.getAmbiguosbirthBeforeMarriage()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            Optional<LocalDate> wifeBirth = wife.getBdate();
            Optional<LocalDate> husbandBirth = husband.getBdate();


            if( wifeBirth.isPresent() && family.getMarried().isPresent() && wifeBirth.get().isAfter(family.getMarried().get()) ){
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR: FAMILY: US02:"+family.getId()+" MARRIED " + formatDate(family.getMarried()) + " BEFORE WIFE'S BIRTH ( "+wife.getId() +") "+formatDate(wife.getBdate()));
            }
            if ( husbandBirth.isPresent() && family.getMarried().isPresent() && husbandBirth.get().isAfter(family.getMarried().get()) ){
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR: FAMILY: US02:"+family.getId()+" MARRIED " + formatDate(family.getMarried()) + " BEFORE WIFE'S BIRTH (" +husband.getId() +")"+formatDate(husband.getBdate()));
            }
        }
    }

    public void printIndividualsWithDivorceBeforeDeath(IndiFamilyResponse indiFamilyResponse) {

        for (Family family : indiFamilyResponse.getAmbiguosFamilyDivorceDeathList()) {
            Individual husband = indiFamilyResponse.getIndividualList().stream().filter(indi -> indi.getId().equals(family.getHusbandId())).findFirst().get();
            Individual wife = indiFamilyResponse.getIndividualList().stream().filter( indi -> indi.getId().equals(family.getWifeId())).findFirst().get();

            Optional<LocalDate> wifeDeath = wife.getDeathDate();
            Optional<LocalDate> husbandDeath = husband.getDeathDate();

            if( wifeDeath.isPresent() && family.getDivorced().isPresent() && wifeDeath.get().isBefore( family.getDivorced().get()) ){
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR: FAMILY: US06:"+family.getId()+" Divorced " + family.getDivorced() + " AFTER WIFE'S DEATH ( "+wife.getId() +")"+ wife.getDeathDate());
            }
            if ( husbandDeath.isPresent() && family.getDivorced().isPresent() && husbandDeath.get().isBefore( family.getDivorced().get()) ){
                System.out.println(" LINE -----> " + getLineNumber(family.getId()) + " " +"ERROR: FAMILY: US06:"+family.getId()+" Divorced " + formatDate(family.getDivorced()) + " AFTER HUSBANDS'S DEATH (" +husband.getId() +") "+ formatDate(husband.getDeathDate()));
            }
        }
    }
    public void printIndividualsWithBirthBeforeCurrentData(List<Family> individualFamList,List<Individual> individualArrayList) throws ParseException, java.text.ParseException {

        for (Individual indi : individualArrayList) {
            Optional<LocalDate> birthDate = indi.getBdate();
            Optional<LocalDate> deathDate = indi.getDeathDate();

            if ( birthDate.isPresent() ){
                if(LocalDate.now().isBefore(birthDate.get()))
                    System.out.println(" LINE -----> " + getLineNumber(indi.getId()) + " " +"ERROR: " + "INDIVIDUAL: US01: " +indi.getId() + ": BIRTH DATE IS AFTER CURRENTDATE");

            }
            if (deathDate.isPresent()){
                if(LocalDate.now().isBefore(deathDate.get()))
                    System.out.println(" LINE -----> " + getLineNumber(indi.getId()) + " " +"ERROR: " + "INDIVIDUAL: US01: " +indi.getId() + ": Death DATE IS AFTER CURRENTDATE");

            }

        }
        for (Family fam : individualFamList) {
            Optional<LocalDate> marrDate = fam.getMarried();
            Optional<LocalDate> divDate = fam.getDivorced();

            if ( marrDate.isPresent() ){
                if(LocalDate.now().isBefore(marrDate.get()))
                    System.out.println(" LINE -----> " + getLineNumber(fam.getId()) + " " +"ERROR: " + "INDIVIDUAL: US01: " +fam.getId() + ": Marriage DATE IS AFTER CURRENTDATE");

            }
            if ( divDate.isPresent() ){;
                if(LocalDate.now().isBefore(divDate.get()))
                    System.out.println(" LINE -----> " + getLineNumber(fam.getId()) + " " +"ERROR: " + "INDIVIDUAL: US01: " +fam.getId() + ": Divorce DATE IS AFTER CURRENTDATE");

            }

        }

    }

    public String formatDate( Optional<LocalDate> date ){
        if( date.isPresent() ) {
            return date.get().format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        }
        return "-NA-";
    }

    public void printAmbiguousParentDescendantMarriageList(IndiFamilyResponse indiFamilyResponse) {
            for(FamilyWithParentMarriedToDescendants familyWithParentMarriedToDescendants : indiFamilyResponse.getAmbiguousParentDescendantMarriageList()){
                System.out.println(" LINE -----> " + getLineNumber(familyWithParentMarriedToDescendants.getFamily().getId()) + " " +"ERROR : FAMILY : US17 "+ familyWithParentMarriedToDescendants.getFamily().getId() + " PARENT SHOULD NOT BE MARRIED TO A DESCENDANT" + familyWithParentMarriedToDescendants.getHusband().getName() + ", "+familyWithParentMarriedToDescendants.getWife().getName());
            }
    }

    public void printAmbiguousFirstCousinsMarriageList(IndiFamilyResponse indiFamilyResponse) {
        for(FamilyWithChildrenMarriedToEachOther familyWithFirstCousinsMarried : indiFamilyResponse.getAmbiguousFirstCousinsMarriageList()){
            System.out.println(" LINE -----> " + getLineNumber(familyWithFirstCousinsMarried.getFamily().getId()) + " " +"ANOMALY : FAMILY : US19 "+ familyWithFirstCousinsMarried.getFamily().getId() + " FIRST COUSINS SHOULD NOT BE MARRIED" + familyWithFirstCousinsMarried.getHusband().getName() + ", "+familyWithFirstCousinsMarried.getWife().getName());
        }
    }
    public void printAmbiguousMoreThan15Children(IndiFamilyResponse indiFamilyResponse) {
        for(Family fam : indiFamilyResponse.getAmbiguousMoreThan15Children()){
            System.out.println(" LINE -----> " + getLineNumber(fam.getId()) + " " +"ANOMALY : FAMILY : US15 "+ fam.getId() + " CANNOT HAVE MORE THAN 15 CHILDREN");
        }
    }

    public void printAmbiguousAuntUncleNNList(IndiFamilyResponse indiFamilyResponse) {
        for(FamilyWithAnomaly au : indiFamilyResponse.getAmbiguousAuntUncleMarriedNN()){
            System.out.println("ANOMALY : FAMILY : US20 "+ au.getFamily().getId() + " AUNT/UNCLE SHOULD NOT BE MARRIED TO NEICE/NEWPHEW : " + au.getHusbandId()+ ", "+au.getWifeId());
        }

    }

    public void printAmbiguousBigamyLIst(IndiFamilyResponse indiFamilyResponse) {
        for(FamilyWithAnomaly bigamyList : indiFamilyResponse.getAmbiguousBigamyList()){
            System.out.println("ERROR : FAMILY : US11 "+ " Someone in these families has bigamy without divorce" + bigamyList.getFamily().getId() +" AND "+bigamyList.getAnotherFamilyOfAPerson().getId());
        }
    }
    public void printDuplicateFirstNameList(IndiFamilyResponse indiFamResp){
        for(FamilyWithAnomaly duplicateNamesList : indiFamResp.getAmbiguousDuplicateFirstNameFamilies()){
            System.out.println("ANOMALY : FAMILY : US25: "+duplicateNamesList.getFamily().getId() +" THIS FAMILY CONTAINS DUPLICATE FIRST NAMES: "+ " ".join(" ", duplicateNamesList.getDuplicateNamesInFamily()));
        }
    }
    public void printMissingCorrespondingEntries(IndiFamilyResponse indiFamResp){
        for(FamilyWithAnomaly noCorrespondingEntries : indiFamResp.getMissingCorrespondingEntries()){
            System.out.println("ERROR : FAMILY : US26: NO CORRESPONDING ENTRY WAS FOUND FOR THESE IDS : "+noCorrespondingEntries.getNocorrespondingEntry());
        }
    }
    public void printListOfDeceased( List<Individual> individualArrayList) throws ParseException, java.text.ParseException {
    		List<String> deceased= new ArrayList<>();
    		System.out.println("US29: List of all deceased individuals:" );
    		System.out.println();
            for(Individual indi : individualArrayList)
            {
                if(indi.getDeathDate().isPresent())
                {
                    deceased.add(indi.getId());
                    System.out.println(" LINE -----> " + getLineNumber(indi.getId()) + " " + indi.getId());
                }
        }
         
            System.out.println();
    }
    
    public void printListOfLivingMarried( List<Individual> individualArrayList, List<Family> familyArrayList) throws ParseException, java.text.ParseException {
		List<String> livingMarried= new ArrayList<>();
		System.out.println("US30: List of all living married individuals: ");
		System.out.println();
		  for (Family family : familyArrayList) {
	            Optional<LocalDate> marriageDate = family.getMarried();
	            for(Individual indi : individualArrayList)
	            {
                    if( marriageDate.isPresent() && !indi.getDeathDate().isPresent())
                    {
                        livingMarried.add(indi.getId());
                        System.out.println(" LINE -----> " + getLineNumber(indi.getId()) + " " + indi.getId());
                    }
	            }
	        }
		  System.out.println();
    }
    //US27 Parth
    public void printIndividualswithAge(List<Individual> individualArrayList){
        System.out.println("US:27 : Individual List with Name and Age:");
        System.out.println("+--------------------+------------+");
        System.out.println("|    NAME            |    AGE     |");
        System.out.println("+--------------------+------------+");

        for (Individual indi : individualArrayList) {
            System.out.format(" %20s   %10s", indi.getName(),indi.getAge());
            System.out.println();
        }
        System.out.println("+--------------------+------------+");

    }
    
    public void printCasesForUniqueNameAndBirthDate(List<Individual> individualArrayList)
    {
    	HashMap<String, Integer> uniqueNameAndBirthDateHashMap = new HashMap<String, Integer>();
    	ArrayList<String> DuplicatedNamesAndBDate = new ArrayList<String>();
    	
    	for(Individual indi : individualArrayList)
    	{
    		
    		if(!indi.getBdate().toString().trim().equals("Optional.empty"))
    		{
    			String key = indi.getName() + indi.getBdate().toString().trim();
    			if(!uniqueNameAndBirthDateHashMap.containsKey(key))
    			{
    				uniqueNameAndBirthDateHashMap.put(key, 1);
    			}
    			else
    			{
    				DuplicatedNamesAndBDate.add(indi.getId());
    			}
    		}
    	}
    	System.out.println();
    	System.out.print("ERROR : US23: List of all the individuals with Duplicated Names and BirthDates : ");
    	for(String dupNamesAndBDate : DuplicatedNamesAndBDate)
    	{
    		System.out.print(" LINE -----> " + getLineNumber(dupNamesAndBDate) + " " + dupNamesAndBDate + ", ");
    	}
    	System.out.println();
    }
    
    
    public void printCasesForUniqueFamilyWithSpouses(List<Family> familyArrayList)
    {
    	HashMap<String, Integer> uniqueFamily = new HashMap<String, Integer>();
    	ArrayList<String> DuplicatedFamilyWithSpouses = new ArrayList<String>();
    	
    	for(Family fam : familyArrayList)
    	{
    		if(fam.getHusbandId().trim() != "" && fam.getWifeId().trim() != "")
    		{
    			if(!uniqueFamily.containsKey(fam.getHusbandId().trim()) && !uniqueFamily.containsKey(fam.getWifeId().trim()))
    			{
    				uniqueFamily.put(fam.getHusbandId().trim(), 1);
    				uniqueFamily.put(fam.getWifeId().trim(), 1);
    			}
    			else
    			{
    				DuplicatedFamilyWithSpouses.add(fam.getId());
    			}
    		}
    		

    	}
    	System.out.println();
    	System.out.print("ERROR : US24: List of all the Families with duplicated Spouses : ");
    	for(String dupSpouses : DuplicatedFamilyWithSpouses)
    	{
    		System.out.print(" LINE -----> " + getLineNumber(dupSpouses) + " " + dupSpouses + ", ");
    	}
    	System.out.println();
    }
    public void printLargreAgeDifferences(IndiFamilyResponse indiFamilyResponse) {
        for(FamilyWithAnomaly fam : indiFamilyResponse.getAmbiguousFamiliesWithLargeAgeDifference()){
            System.out.println(" LINE -----> " + getLineNumber(fam.getFamily().getId()) + " " +"ANOMALY : FAMILY : US34 "+ " Large age difference in this couple " + fam.getFamily().getId() +". Age of husband on marriage day: " +Period.between( fam.getHusband().getBdate().get(),fam.getFamily().getMarried().get()).getYears()+" Age of wife on marriage day: "+Period.between( fam.getWife().getBdate().get(),fam.getFamily().getMarried().get()).getYears());
        }
        System.out.println();
    }
    public void printPeopleWhoDiedInLast30Days(IndiFamilyResponse indifamilyResponse){
        System.out.println("US36 : List of deaths in the last 30 days ");
        for(Individual individual : indifamilyResponse.getPeopleDiedInLast30Days()){
            System.out.print(" LINE -----> " + getLineNumber(individual.getId()) + " " + individual.getId() + "| ");
        }
        System.out.println();
    }
    public List<String> printListOfUpcBday( List<Individual> individualArrayList) throws ParseException, java.text.ParseException {
		List<String> upcoming= new ArrayList<>();
		LocalDate dateBefore;
		LocalDate dateAfter;
		System.out.println();
		System.out.println("US38: List of all individuals with upcoming birthday:");
        for(Individual indi : individualArrayList)
        {
            if(indi.getBdate().isPresent())
            {
            	dateBefore=indi.getBdate().get();
            	LocalDate currentDate = LocalDate.of(indi.getBdate().get().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth());
            	dateAfter=currentDate.plusDays(30);
            	if(30 > ChronoUnit.DAYS.between(dateBefore, dateAfter)) {
            		upcoming.add(indi.getId());
            		System.out.print(" LINE -----> " + getLineNumber(indi.getId()) + " " + indi.getId());
            		//" LINE " + getLineNumber()
            	}
            }

  
  }
        System.out.println("US38: List of all individuals with upcoming birthday:" + upcoming);
        return upcoming;
}
    public List<String> printListOfUpcAnniversary( List<Family> FamilyArrayList) throws ParseException, java.text.ParseException {
    	System.out.print("US39: List of all couples with upcoming anniversary:");
    	List<String> upcoming = new ArrayList<>();
		LocalDate dateBefore;
		LocalDate dateAfter;
        for(Family fam : FamilyArrayList)
        {
            if(fam.getMarried().isPresent())
            {
            	dateBefore=fam.getMarried().get();
            	LocalDate currentDate = LocalDate.of(fam.getMarried().get().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth());
            	dateAfter=currentDate.plusDays(30);
            	if(30 > ChronoUnit.DAYS.between(dateBefore, dateAfter)) {
                System.out.print(" Wife:"+ fam.getWifeName() + " LINE " + getLineNumber(fam.getWifeId()) + " and Husband:" + fam.getHusbandName() + " LINE " + getLineNumber(fam.getHusbandId()));


  upcoming.add(fam.getId());
            		System.out.print(" Wife:"+ fam.getWifeName() + " and Husband:" + fam.getHusbandName());
            	}
            }
    }
     return upcoming;
        
    }
    
    public void printMultipleBirthsList(List<Family> familyArrayList) {
    	System.out.println();
		
    	for(Family fam : familyArrayList)
    	{
    		String totalChildren = fam.getChildren();
    		
    		if(totalChildren.split(",").length >= 5)
    		{
    			System.out.println("US32 : List of MULTIPLE BIRTHS "+ fam.getHusbandName() + " AND " + fam.getWifeName() + " GAVE MULTIPLE BIRTHS. THE LIST OF INDIVIDUALS IS AS BELOW: ");
    			
    			for(int j=0; j<totalChildren.split(",").length; j++)
    			{
    				System.out.print("LINE " + getLineNumber(totalChildren.split(",")[j]) + " ---> " + totalChildren.split(",")[j] + ", ");
    			}
    		}
    	}
    	System.out.println();
    }

}
