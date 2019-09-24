import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;


public class App {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		FileReader fr = new FileReader("SampleFile.txt");
		BufferedReader br = new BufferedReader(fr);
		
		FileOutputStream fos = new FileOutputStream("Output.txt");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		String arr[];
		String valid = "|Y|";
		String inValid = "|N|";
		
		HashSet<String> hs = new HashSet<String>();
		
		hs.add("INDI");
		hs.add("NAME");
		hs.add("SEX");
		hs.add("BIRT");
		hs.add("DEAT");
		hs.add("FAMC");
		hs.add("FAMS");
		hs.add("FAM");
		hs.add("MARR");
		hs.add("HUSB");
		hs.add("WIFE");
		hs.add("CHIL");
		hs.add("DIV");
		hs.add("DATE");
		hs.add("HEAD");
		hs.add("TRLR");
		hs.add("NOTE");
		
		String line = br.readLine();
		
		while(line != null)
		{
			String input = "";
			String output = "";
			
			arr = line.split(" ");
			
			for(int i=0; i<arr.length; i++)
			{
				if(i==0)
				{
					input = input + "--> " + arr[i] + " ";
				}
				else
				{
					if(i < arr.length-1)
					{
						input = input + arr[i] + " ";
					}
					else
					{
						input = input + arr[i];
					}
				}
			}
			System.out.println(input);
			
			bw.write(input);
			bw.newLine();
			
			for(int i=0; i<arr.length; i++)
			{
				if(i==0)
				{
					if(Integer.parseInt(arr[i]) == 0)
					{
						if(!hs.contains(arr[i+1]))
						{
							String temp = arr[i+1];
							arr[i+1] = arr[i+2];
							arr[i+2] = temp;
						}
					}
						
						
					output = output + "<-- " + arr[i];
				}
				else
				{
					
					if(i == 1)
					{
						output = output + "|" + arr[i];
						
						if(hs.contains(arr[i]))
						{
							output = output + valid;
						}
						else
						{
							output = output + inValid;
						}
					}
					else
					{
						if(i < arr.length-1)
						{
							output = output + arr[i] + " ";
						}
						else
						{
							output = output + arr[i];
						}
					}
				}
			}
			System.out.println(output);
			
			bw.write(output);
			bw.newLine();
			
			line = br.readLine();
		}
		
		bw.close();
	}

}
