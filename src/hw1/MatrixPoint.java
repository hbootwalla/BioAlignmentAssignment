package hw1;

enum Direction{
	ZERO,
	LEFT,
	TOP,
	DIAGONAL
};

public class MatrixPoint {
	Direction dir;
	int Score;
	
	public MatrixPoint(Direction d, int Score){
		this.dir = d;
		this.Score = Score;
	}
}
