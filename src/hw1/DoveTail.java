package hw1;

public class DoveTail 
{
	private String qString;
	private String dbString;
	private String queryId;
	private String databaseId;
	int gap,max,maxI,maxJ;
	private MatrixPoint[][] matrix;
	
	public DoveTail(int gap, String queryId, String databaseId, String queryString, String databaseString)
	{
		this.queryId = queryId;
		this.databaseId = databaseId;
		this.qString = queryString;
		this.dbString = databaseString;
		this.gap= gap;
		this.matrix = new MatrixPoint[qString.length() + 1][dbString.length() + 1];
		int i=1;
		matrix[0][0] = new MatrixPoint(Direction.DIAGONAL, 0);
		while(i < (this.qString.length() + 1))
		{
			this.matrix[i][0] = new MatrixPoint(Direction.TOP, 0);
			i++;
		}
		i=1;
		while(i < (this.dbString.length() + 1))
		
		{
			this.matrix[0][i]= new MatrixPoint(Direction.LEFT, 0);
			i++;
		}
	}


	public void calculateMatrix(ScoreMatrix scoreMatrix)
	{
			int i=1;
			while(i<this.qString.length() + 1)
			{
				char c1 = qString.charAt(i-1);
				int j = 1;
				while(j < this.dbString.length() + 1)
				{
					char c2 = dbString.charAt(j-1);
					int scr = scoreMatrix.getScore(c1, c2);
					
					int diagonal = this.matrix[i-1][j-1].Score + scr;
					int up = this.matrix[i-1][j].Score + gap;
					int side = this.matrix[i][j-1].Score + gap;

					
					if(diagonal>up)
					{
						if(side>diagonal)
						{				
								this.matrix[i][j] = new MatrixPoint(Direction.LEFT, side);							
						}
						else
						{							
								this.matrix[i][j] = new MatrixPoint(Direction.DIAGONAL,diagonal);							
						}
					}
					else{

						if(side>up)
						{							
								this.matrix[i][j] = new MatrixPoint(Direction.LEFT,side);							
						}
						else
						{
								this.matrix[i][j] = new MatrixPoint(Direction.TOP, up);
						}
					}
					j++;
			}
			i++;
		}
		findScore();
	}
	
	public void findScore(){
		max = 0;
		maxI = 0;
		maxJ = 0;
		int i = 0;
		for(; i < qString.length(); i++){
			if(matrix[i][dbString.length()].Score > max){
				max = matrix[i][dbString.length()].Score;
				maxI = i;
				maxJ = dbString.length();
			}
		}
		
		for(i = 0; i < dbString.length(); i++){
			if(matrix[qString.length()][i].Score > max){
				max = matrix[qString.length()][i].Score;
				maxI = qString.length();
				maxJ = i;
			}
		}
	}
	
	public Result traceback(){
		Result result;
		
		StringBuffer sTraceback = new StringBuffer();
		StringBuffer tTraceback = new StringBuffer();
		
		int i = maxI;
		int j = maxJ;
		
		Direction dir;
		
		while(i>0 && j>0){
			dir = matrix[i][j].dir;
			if(dir == Direction.DIAGONAL){
				sTraceback.append(qString.charAt(i-1));
				tTraceback.append(dbString.charAt(j-1));
				i--;j--;
			}
			else if(dir == Direction.TOP){
				tTraceback.append('.');
				sTraceback.append(qString.charAt(i-1));
				i--;
			}
			else if(dir == Direction.LEFT){
				sTraceback.append('.');
				tTraceback.append(dbString.charAt(j-1));
				j--;
			}
		}
		result = new Result(queryId,databaseId,i, j,max, sTraceback.reverse().toString(),tTraceback.reverse().toString());
				
		return result;
	}
}