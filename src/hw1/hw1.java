package hw1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class hw1 {
	enum Direction {TOP, BOTTOM, DIAGONAL};
	
	public static LinkedHashMap<String, String> queries = new LinkedHashMap<>();
	public static LinkedHashMap<String, String> database = new LinkedHashMap<>();
	public static int[][] scoringMatrix;
	public static HashSet<String> alphabets = null;
	public static Direction traceBackMatrix[][] = null;
	
	static void parseQueries(String fileName, int qOrD) throws IOException{
		Scanner sc = new Scanner(new File(fileName));
		boolean first = true;
		String header = null;
		StringBuffer query = new StringBuffer();
		while(sc.hasNextLine()){
			String line = sc.nextLine().trim();
			if(line.charAt(0) == '>'){
				if(first == false){
					if(qOrD == 1)
						queries.put(header, query.toString());
					else if(qOrD == 2)
						database.put(header, query.toString());
				}else{
					first = false;
				}
				header = line;
				query = new StringBuffer();
			}
			else{
				query.append(line);
			}
		}
		if(qOrD == 1)
			queries.put(header, query.toString());
		else if(qOrD == 2)
			database.put(header, query.toString());
		sc.close();
	}
	
	public static void parseScoringMatrix(String scoringFileName) throws IOException{
		Scanner sc = new Scanner(new File(scoringFileName));
		scoringMatrix = new int[alphabets.size()][alphabets.size()];
		int i = 0;
		while(sc.hasNextLine()){
			String[] scoresStr = sc.nextLine().split(" ");
			int j = 0;
			
			for(String s : scoresStr){
				if(s != null && !s.equals(" ") && !s.equals("")){
					scoringMatrix[i][j++] = Integer.parseInt(s);
				}
			}
			i++;
		}
		sc.close();
//		for(i = 0; i<scoringMatrix.length; i++){
//			for(int j = 0; j<scoringMatrix[0].length; j++){
//				System.out.print(" " + scoringMatrix[i][j]);
//			}
//			System.out.println();
//		}
	}
	
	public static void parseAlphabets(String alphabetFileName) throws IOException{
		Scanner sc = new Scanner(new File(alphabetFileName));
		
		StringBuffer alpha = new StringBuffer();
		while(sc.hasNextLine()){
			alpha.append(sc.nextLine());
		}
		alphabets = new HashSet<String>(Arrays.asList(alpha.toString().split("")));
		System.out.println(alphabets);
	}
	
	public static void calculateGlobalAlignment(){
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			Integer align_method = Integer.parseInt(args[0]);
			String queryFileName = args[1];
			String databaseFileName = args[2];
			String alphabetFileName = args[3];
			String scoringFileName = args[4];
			Integer k = Integer.parseInt(args[5]);
			Integer m = Integer.parseInt(args[6]);
			
			parseQueries(queryFileName,1);
			parseQueries(databaseFileName,2);
			parseAlphabets(alphabetFileName);
			parseScoringMatrix(scoringFileName);
			
			switch(align_method){
				case 1 : 
					calculateGlobalAlignment();
					break;
			}
		}
		catch(NumberFormatException numFormatException){
			System.out.println(numFormatException.toString());
		}
		catch(IOException io){
			System.out.println(io.toString());
		}
	}

}
