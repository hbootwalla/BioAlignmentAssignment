package hw1;

public class NWAlignment {
	public String dbString;
	public String qString;
	public MatrixPoint[][] matrix;
	public int gap;
	
	public NWAlignment(int gap, String qString, String dbString){
		this.gap = gap;
		this.qString = qString;
		this.dbString = dbString;
		this.matrix = new MatrixPoint[this.qString.length() + 1][this.dbString.length() + 1];
		int score = 0;
		this.matrix[0][0] = new MatrixPoint(Direction.DIAGONAL, score);
		for(int i=1; i<(this.qString.length()+1); i++){
			score+=gap;
			this.matrix[i][0] = new MatrixPoint(Direction.LEFT,score);
			
		}
		score = 0;
		for(int i=1; i< (this.dbString.length()+1); i++){
			score+=gap;
			this.matrix[0][i]= new MatrixPoint(Direction.TOP,score);
			
		}
	}
	
	public int calculateMatrix(ScoreMatrix scoreMatrix){
		for(int i = 1; i < qString.length() + 1; i++){
			char q = qString.charAt(i - 1);
			for(int j = 1; j < dbString.length() + 1 ; j++){
				char d = dbString.charAt(j - 1);
				int sc = scoreMatrix.getScore(q, d);
				int diagScore = matrix[i-1][j-1].Score + sc;
				int topScore = matrix[i-1][j].Score + gap;
				int leftScore = matrix[i][j-1].Score + gap;
				
				if(diagScore > topScore)
				{
					if(leftScore > diagScore){
						matrix[i][j] = new MatrixPoint(Direction.LEFT, leftScore);
					}
					else{
						matrix[i][j] = new MatrixPoint(Direction.DIAGONAL, diagScore);
					}
				}
				else{
					if(leftScore > topScore){
						matrix[i][j] = new MatrixPoint(Direction.LEFT, leftScore);
					}else{
						matrix[i][j] = new MatrixPoint(Direction.TOP, topScore);
					}
				}
				
			}
		}
		return matrix[qString.length()][dbString.length()].Score;
	}
	
	public String[] traceback(){
		StringBuffer sTraceBack = new StringBuffer();
		StringBuffer tTraceBack = new StringBuffer();
		int i = qString.length();
		int j = dbString.length();
		Direction dir;
		
		while(i>0 && j>0){
			dir = matrix[i][j].dir;
			if(dir == Direction.DIAGONAL){
				sTraceBack.append(qString.charAt(i-1));
				tTraceBack.append(dbString.charAt(j-1));
				i--;j--;
			}
			else if(dir == Direction.LEFT){
				tTraceBack.append('.');
				sTraceBack.append(qString.charAt(i-1));
				i--;
			}
			else if(dir == Direction.TOP){
				sTraceBack.append('.');
				tTraceBack.append(dbString.charAt(j-1));
				j--;
			}
		}
		while(i > 1){
			tTraceBack.append('.');
			sTraceBack.append(qString.charAt(i-1));
			i--;
		}
		
		while(j > 1){
			sTraceBack.append('.');
			tTraceBack.append(dbString.charAt(j-1));
			j--;
		}
		return new String[]{sTraceBack.reverse().toString(), tTraceBack.reverse().toString()};
	}
	
}

