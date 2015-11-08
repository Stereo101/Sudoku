package sudoku;

//States
	// 0 == not solved, but valid board
	// 1 == solved
	//-1 == invalid board (an error was made somewhere)

public class SuBo {
	int[][] grids;
	int movesMade;
	Move[] moveList;
	int state;
	String name;
	
	public SuBo() {
		grids = Grid.makeGrids();
		movesMade = 0;
		state = 0;
		moveList = new Move[81];
		name = "DEFAULT NAME";
	}
	
	public void report() {
		System.out.println("\n\n#######\nBoard: "  + name + "\nState : " + state + "\nMoves : " + movesMade);
		Grid.rateDif(this);
		System.out.print("#######\n\n");
	}
}
