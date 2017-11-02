package hw1;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class ScoreMatrix {
	
	int[][] scoreMatrix;
	HashMap<Character, Integer> alphabetIndex = new HashMap<>();
	
	public ScoreMatrix(String alphabetString){
		int len = alphabetString.length();
		for(int i = 0; i<len; i++){
			alphabetIndex.put(Character.toLowerCase(alphabetString.charAt(i)), i);
		}
		scoreMatrix = new int[len][len];
	}
	
	public void parseScoringMatrix(String scoringFileName) throws IOException{
		Scanner sc = new Scanner(new File(scoringFileName));
		int i = 0;
		while(sc.hasNextLine()){
			String[] scoresStr = sc.nextLine().split(" ");
			int j = 0;
			
			for(String s : scoresStr){
				if(s != null && !s.equals(" ") && !s.equals("")){
					scoreMatrix[i][j++] = Integer.parseInt(s);
				}
			}
			i++;
		}
		sc.close();
		for(i = 0; i<scoreMatrix.length; i++){
			for(int j = 0; j<scoreMatrix[0].length; j++){
				System.out.print(" " + scoreMatrix[i][j]);
			}
			System.out.println();
		}
	}
	
	public int getScore(Character alpha, Character beta){
		int i1 = alphabetIndex.get(Character.toLowerCase(alpha));
		int i2 = alphabetIndex.get(Character.toLowerCase(beta));
		return scoreMatrix[i1][i2];
	}
	
}
