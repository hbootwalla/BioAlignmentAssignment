package hw1;

public class SWAlignment {
	public String dbString;
	public String qString;
	public MatrixPoint[][] matrix;
	public int gap;
	public int max;
	public int maxI, maxJ;
	
	public SWAlignment(int gap, String qString, String dbString){
		this.gap = gap;
		this.qString = qString;
		this.dbString = dbString;
		this.matrix = new MatrixPoint[this.qString.length() + 1][this.dbString.length() + 1];
		int score = 0;
		this.matrix[0][0]= new MatrixPoint(Direction.DIAGONAL,score);
		for(int i=1; i<(this.qString.length()+1); i++){
			this.matrix[i][0] = new MatrixPoint(Direction.LEFT,score);
		}
		for(int i=1; i< (this.dbString.length()+1); i++){
			this.matrix[0][i]= new MatrixPoint(Direction.TOP,score);
		}
	}
	
	public int calculateMatrix(ScoreMatrix scoreMatrix){
		maxI = 0;
		maxJ = 0;
		max = 0;
		for(int i = 1; i < qString.length() + 1; i++){
			char q = qString.charAt(i - 1);
			for(int j = 1; j < dbString.length() + 1 ; j++){
				char d = dbString.charAt(j - 1);
				int sc = scoreMatrix.getScore(q, d);
				int diagScore = matrix[i-1][j-1].Score + sc;
				int topScore = matrix[i-1][j].Score + gap;
				int leftScore = matrix[i][j-1].Score + gap;
				int zero = 0;
				
				if(diagScore > topScore)
				{
					if(leftScore > diagScore){
						if(leftScore > 0)
							matrix[i][j] = new MatrixPoint(Direction.LEFT, leftScore);
						else
							matrix[i][j] = new MatrixPoint(Direction.ZERO, 0);
					}
					else{
						if(diagScore > 0)
							matrix[i][j] = new MatrixPoint(Direction.DIAGONAL, diagScore);
						else 
							matrix[i][j] = new MatrixPoint(Direction.ZERO, 0);
					}
				}
				else{
					if(leftScore > topScore){
						if(leftScore > 0)
							matrix[i][j] = new MatrixPoint(Direction.LEFT, leftScore);
						else
							matrix[i][j] = new MatrixPoint(Direction.ZERO, 0);
					}else{
						if(topScore > 0)
							matrix[i][j] = new MatrixPoint(Direction.TOP, topScore);
						else
							matrix[i][j] = new MatrixPoint(Direction.ZERO, 0);
					}
				}
				if(matrix[i][j].Score > max){
					max = matrix[i][j].Score;
					maxI = i;
					maxJ = j;
				}
			}
		}
		return max;
	}
	
	public String[] traceback(){
		StringBuffer sTraceBack = new StringBuffer();
		StringBuffer tTraceBack = new StringBuffer();
		int i = maxI;
		int j = maxJ;
		Direction dir;
		
		while(i>0 && j>0 && matrix[i][j].Score != 0){
			dir = matrix[i][j].dir;
			if(dir == Direction.DIAGONAL){
				sTraceBack.append(qString.charAt(i-1));
				tTraceBack.append(dbString.charAt(j-1));
				i--;j--;
			}
			else if(dir == Direction.TOP){
				tTraceBack.append('.');
				sTraceBack.append(qString.charAt(i-1));
				i--;
			}
			else if(dir == Direction.LEFT){
				sTraceBack.append('.');
				tTraceBack.append(dbString.charAt(j-1));
				j--;
			}
		}
		return new String[]{i+"",j+"",sTraceBack.reverse().toString(), tTraceBack.reverse().toString()};
	}
	
}
