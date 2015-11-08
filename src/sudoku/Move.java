package sudoku;

//1 is a setup move
//2 is a logic move
//3 is a guess move

public class Move {
	int moveType = -1;
	int square = -1;
	int num = -1;
	int overWrite = -1;
		
	public Move(int Num, int Square, int MoveType, int OverWrite) {
		square = Square;
		num = Num;
		moveType = MoveType;
		overWrite = OverWrite;
	}
}
