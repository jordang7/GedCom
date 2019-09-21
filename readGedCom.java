import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class readGedCom {

	public static void main(String[] args) {
		  // Create an instance of File for data.txt file.
        File file = new File("/home/jordan/Documents/PROJECT_1.ged");
        String[] tags = new String[]{ "INDI","NAME","SEX","BIRT","DEAT","FAMC","FAMS","FAM","MARR","HUSB","WIFE","CHIL","DIV","DATE","HEAD","TRLR","NOTE" };
        List<String> list = Arrays.asList(tags);
        String valid;
        
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String arg="";
                String tag="";
                System.out.println("-->"+line);
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
                
                System.out.println("<--"+splitted[0]+"|"+tag+"|"+valid+"|"+arg);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}

}
