package hw1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

class Result implements Comparable<Result>{
	public String queryId;
	public String databaseId;
	public int startQuery;
	public int startDatabase;
	public int score;
	public String qTraceback;
	public String dbTraceback;

	public Result(String queryId, String databaseId, int startQuery, int startDatabase, int score, String qt, String dt) {
		this.queryId = queryId;
		this.databaseId = databaseId;
		this.startQuery = startQuery;
		this.startDatabase = startDatabase;
		this.score = score;
		this.qTraceback = qt;
		this.dbTraceback = dt;
	}

	public int compareTo(Result o1) {
		return o1.score - this.score;
	}
}

public class hw1 {
	
	public static LinkedHashMap<String, String> queries = new LinkedHashMap<>();
	public static LinkedHashMap<String, String> database = new LinkedHashMap<>();
	
	public static String alphabetString = null;
	public static ScoreMatrix scoreMatrix = null;
	public static int k,gap;
	
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
				header = header.substring(header.indexOf("hsa:")+4, header.indexOf(" "));
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
	
	
	
	public static void parseAlphabets(String alphabetFileName) throws IOException{
		Scanner sc = new Scanner(new File(alphabetFileName));
		
		StringBuffer alpha = new StringBuffer();
		while(sc.hasNextLine()){
			alpha.append(sc.nextLine());
		}
		alphabetString = alpha.toString();
		sc.close();
	}
	
	public static void calculateGlobalAlignment(){
		ArrayList<Result> results = new ArrayList<Result>();
		Set<Map.Entry<String, String>> inputSet = queries.entrySet();
		for(Entry<String, String> entry : inputSet){
			String input = entry.getValue();
			for(Map.Entry<String, String> databaseQuery : database.entrySet()){
				   NWAlignment nW = new NWAlignment(gap, input, databaseQuery.getValue());
	         	   int score = nW.calculateMatrix(scoreMatrix);
	         	   String[] traceBackArray = nW.traceback();
				   Result temp_output = new Result(entry.getKey(), databaseQuery.getKey(), 0, 0, score, traceBackArray[0], traceBackArray[1]);
	         	   results.add(temp_output);
			}
		}
		Collections.sort(results);
		for(int i = 0; i<Math.min(results.size(),k); i++){
			Result res = results.get(i);
			System.out.println(res.score);
			System.out.println(res.queryId + " " + res.startQuery + " " + res.qTraceback);
			System.out.println(res.databaseId + " " + res.startDatabase + " " + res.dbTraceback);
		}
	}
	
	public static void calculateLocalAlignment(){
		ArrayList<Result> results = new ArrayList<Result>();
		Set<Map.Entry<String, String>> inputSet = queries.entrySet();
		for(Entry<String, String> entry : inputSet){
			String input = entry.getValue();
			for(Map.Entry<String, String> databaseQuery : database.entrySet()){
				   SWAlignment sW = new SWAlignment(gap, input, databaseQuery.getValue());
	         	   int score = sW.calculateMatrix(scoreMatrix);
	         	   String[] traceBackArray = sW.traceback();
				   Result temp_output = new Result(entry.getKey(), databaseQuery.getKey(), Integer.parseInt(traceBackArray[0]), Integer.parseInt(traceBackArray[1]), score, traceBackArray[2], traceBackArray[3]);
	         	   results.add(temp_output);
			}
		}
		Collections.sort(results);
		for(int i = 0; i<Math.min(results.size(),k); i++){
			Result res = results.get(i);
			System.out.println(res.score);
			System.out.println(res.queryId + " " + res.startQuery + " " + res.qTraceback);
			System.out.println(res.databaseId + " " + res.startDatabase + " " + res.dbTraceback);
		}
	}
	
	public static void calculateDovetailAlignment(){
		ArrayList<Result> results = new ArrayList<Result>();
		Set<Map.Entry<String, String>> inputSet = queries.entrySet();
		for(Entry<String, String> query : inputSet){
			for(Map.Entry<String, String> databaseQuery : database.entrySet()){
				   DoveTail dT = new DoveTail(gap, query.getKey(), databaseQuery.getKey(), query.getValue(), databaseQuery.getValue());
	         	   dT.calculateMatrix(scoreMatrix);
	         	   Result result = dT.traceback();
				   results.add(result);
			}
		}
		Collections.sort(results);
		for(int i = 0; i<Math.min(results.size(),k); i++){
			Result res = results.get(i);
			System.out.println(res.score);
			System.out.println(res.queryId + " " + res.startQuery + " " + res.qTraceback);
			System.out.println(res.databaseId + " " + res.startDatabase + " " + res.dbTraceback);
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			Integer align_method = Integer.parseInt(args[0]);
			String queryFileName = args[1];
			String databaseFileName = args[2];
			String alphabetFileName = args[3];
			String scoringFileName = args[4];
			k = Integer.parseInt(args[5]);
			gap = Integer.parseInt(args[6]);
			
			parseQueries(queryFileName,1);
			parseQueries(databaseFileName,2);
			parseAlphabets(alphabetFileName);
			
			scoreMatrix = new ScoreMatrix(alphabetString);
			scoreMatrix.parseScoringMatrix(scoringFileName);
			
			
			switch(align_method){
				case 1 : {
						long time = System.currentTimeMillis();
						calculateGlobalAlignment();
						System.out.println("\nFINAL TIME: " + (System.currentTimeMillis() - time));
					}
					break;
					
				case 2 :{
						long time = System.currentTimeMillis();
						calculateLocalAlignment();
						System.out.println("\nFINAL TIME: " + (System.currentTimeMillis() - time));
					}
					break;
				case 3 :{
						long time = System.currentTimeMillis();
						calculateDovetailAlignment();
						System.out.println("\nFINAL TIME: " + (System.currentTimeMillis() - time));
					}
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
