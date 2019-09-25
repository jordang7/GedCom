import java.io.File;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Family
{
	public String id;
	public String married;
	public String husbandId;
	public String husbandName;
	public String wifeId;
	public String wifeName;
	public String children;
	public String divorced;
	
	Family()
	{
		this.id="";
		this.married="";
		this.husbandId="";
		this.husbandName="";
		this.wifeId="";
		this.wifeName="";
		this.children="";
		this.divorced = "";
	}
}

class Individual
{
	public String id;
	public String name;
	public String gender;
	public String birthDay;
	public String age;
	public String alive;
	public String death;
	public String Child;
	public String spouse;
	
	Individual()
	{
		this.id="";
		this.name="";
		this.gender="";
		this.birthDay="";
		this.age="";
		this.alive="";
		this.death="";
		this.Child="";
		this.spouse="";
	}
	
}

public class readGedCom {

	public static void main(String[] args) throws ParseException {
		  // Create an instance of File for data.txt file.
        File file = new File("./PROJECT_1.ged");
        String[] tags = new String[]{ "INDI","NAME","SEX","BIRT","DEAT","FAMC","FAMS","FAM","MARR","HUSB","WIFE","CHIL","DIV","DATE","HEAD","TRLR","NOTE" };
        List<String> list = Arrays.asList(tags);
        String valid;
        boolean indiStarted = false;
        boolean familyStarted = false;
        ArrayList<String> IndiDetails = new ArrayList<String>();
        ArrayList<Individual> IndiArrayList = new ArrayList<Individual>();
        ArrayList<Family> familyArrayList = new ArrayList<Family>();
        Individual indi = null;
        Family family = null;
        boolean isBirthDay = false;
        boolean isDeathDay = false;
        
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String arg="";
                String tag="";
//                System.out.println("-->"+line);
                String[] splitted = line.split(" ");
                if(list.contains(splitted[1])) {
                	valid="Y";
                	tag=splitted[1];
                	for(int i =2;i<splitted.length;i++) {
                		arg+=" "+ splitted[i];
                	}
                }
                else if(splitted[2].contentEquals("INDI") || splitted[2].contentEquals("FAM")){
                		valid="Y";
                		arg=splitted[1];
                		tag=splitted[2];
                	}
                else {
                	valid="N";
                	tag=splitted[1];
                	for(int i =2;i<splitted.length;i++) {
                		arg+=" "+ splitted[i];
                	}
                }
                
                if(splitted.length > 2)
                {
                if(splitted[2].contentEquals("INDI"))
                {
                	indi = new Individual();
                	indi.id = splitted[1];
                	indiStarted = true;
                }
                
                else
                {
                	if(indiStarted == true)
                	{
                		if(splitted[1].contentEquals("NAME"))
                		{
                			
                			for(int j=2; j<splitted.length; j++)
                			{
                				indi.name = indi.name + " " + splitted[j];
                			}
                			 
                		}
                		
                		if(splitted[1].contentEquals("SEX"))
                		{
                			indi.gender = splitted[2];
                		}
                		
                		if(splitted[1].contentEquals("DATE"))
                		{
                			if(isBirthDay == true)
                			{
                				for(int j=2; j<splitted.length; j++)
                    			{
                    				indi.birthDay = indi.birthDay + " " + splitted[j];
                    			}
                				isBirthDay = false;
                				
                				Date date = new Date();
//                				- new SimpleDateFormat("dd/MM/yyyy").parse(indi.birthDay));
                				
//                				indi.age =  
                			}
                			
                			if(isDeathDay == true)
                			{
                				for(int j=2; j<splitted.length; j++)
                    			{
                    				indi.death = indi.death + " " + splitted[j];
                    			}
                				isDeathDay = false;
                			}
                		}
                		
                		if(splitted[1].contentEquals("CHIL"))
                		{
                			indi.Child = splitted[2];
                		}
                		
                		if(splitted[1].contentEquals("CHIL"))
                		{
                			indi.Child = splitted[2];
                		}
                		
                		if((splitted[1].contentEquals("FAMC") || splitted[1].contentEquals("FAMS")) && indiStarted == true)
                		{
                			if(splitted[1].contentEquals("FAMS"))
                			{
                				indi.spouse = splitted[2];
                			}
                			IndiArrayList.add(indi);
                			indiStarted = false;
                		}
                	}
                	
                	
                }
                
                if(splitted[2].contentEquals("FAM"))
                {
                	family = new Family();
                	
                	family.id = splitted[1];
                	familyStarted = true;
                }
                else
                {
                	if(familyStarted == true)
                	{
                		if(splitted[1].contentEquals("HUSB"))
                		{
                			family.husbandId = splitted[2];
                		}
                		
                		if(splitted[1].contentEquals("WIFE"))
                		{
                			family.wifeId = splitted[2];
                		}
                		
                		if(splitted[1].contentEquals("CHIL"))
                		{
                			family.children = (family.children=="" ? family.children : family.children + "," )+ splitted[2];
                		}
                		
                		if(splitted[1].contentEquals("_CURRENT"))
                		{
                			if(splitted[2] == "N")
                			{
                				family.divorced = "YES";
                			}
                			
                			if(splitted[2] == "Y")
                			{
                				family.divorced = "NA";
                			}
                			
                			familyArrayList.add(family);
                			familyStarted = false;
                			
                		}
                	}
                }
                }
                else
                {
                	if(splitted[1].contentEquals("BIRT"))
                	{
                		isBirthDay = true;
                	}
                	
                	if(splitted[1].contentEquals("DEAT") && splitted[2].contentEquals("Y"))
                	{
                		isDeathDay = true;
                	}
                }
                               
            }
            
            
            
            for(int i=0; i<familyArrayList.size(); i++)
            {
            	for(int j=0; j<IndiArrayList.size(); j++)
                {
                	if(familyArrayList.get(i).husbandId.equals(IndiArrayList.get(j).id))
                	{
                		familyArrayList.get(i).husbandName = IndiArrayList.get(j).name;
                	}
                	
                	if(familyArrayList.get(i).wifeId.equals(IndiArrayList.get(j).id))
                	{
                		familyArrayList.get(i).wifeName = IndiArrayList.get(j).name;
                	}
                }
            	
            	System.out.println(familyArrayList.get(i).wifeName);
            	
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}

}
