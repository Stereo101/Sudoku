package sudoku;

//operations work on a single board instead of on all of them
//also work on conversions of ints
//things that effect multiple grids are under grid.java

public class Operation {
	public static int[] crossFill(int square,int value,int[] grid) {
		int ptr = square + 1;
		//Fill right
		while(grid[ptr]!=-1){
			grid[ptr]+= value;
			ptr++;
		}
		
		ptr = square-1;
		//Fill left
		while(grid[ptr]!=-1) {
			grid[ptr]+= value;
			ptr--;
		}
		
		//Fill Up
		ptr = square + 11;
		while(grid[ptr]!=-1){
			grid[ptr]+= value;
			ptr+=11;
		}
		
		//Fill Down
		ptr = square - 11;
		while(grid[ptr]!=-1){
			grid[ptr]+= value;
			ptr-=11;
		}
		
		return grid;
	}

	
	public static int[] boxFill(int square, int value,int[] grid){
		int boxS = Operation.indexToBox(square);
		
		for(int i = 0; i<=2; i++) {
			for(int q = 0; q<=2; q++){
				grid[boxS+(11*i)+q]+= value;
			}
		}
		
		
		return grid;
		
	}
	
	public static int indexToBox(int square) {
		int col = ((square%11)-1)/3;
		int row = ((square/11)-1)/3;
		int boxS = -99;
		
		switch(row){
		case 0: 
			switch(col){
			case 0: boxS = 12; break;
			case 1: boxS = 15;break;
			case 2: boxS = 18;break;
			} break;
		case 1: 
			switch(col){
			case 0: boxS = 45;break;
			case 1: boxS = 48;break;
			case 2: boxS = 51;break;
			} break;
		case 2: 
			switch(col){
			case 0: boxS = 78;break;
			case 1: boxS = 81;break;
			case 2: boxS = 84;break;
			} break;
		}
		
		return boxS;
	}
	
	public static int indexToGrid(int square) {
		return (12 + ((square/9)*11) + (square%9));
	}
	
	public static int[][] copyGrids(int[][]grids) {
		int[][] copy = new int[10][121];
		
		for(int i = 0; i<=9; i++) {
			for(int q = 0; q<=120; q++) {
				copy[i][q] = grids[i][q];
			}
		}
	
		return copy;
	}
	
	public static SuBo copySubo(SuBo oldBoard) {
		SuBo newBoard = new SuBo();
		//copy grids over
		for(int i = 0; i<=9; i++) {
			for(int q = 0; q<=120; q++) {
				newBoard.grids[i][q] = oldBoard.grids[i][q];
			}
		}
		
		//copy movelist
		for(int i = 0; i<=80; i++) {
			newBoard.moveList[i] = oldBoard.moveList[i];
		}
		
		//copy state and moves made
		newBoard.movesMade = oldBoard.movesMade;
		newBoard.state = oldBoard.state;
		newBoard.name = oldBoard.name;
		return newBoard;
	}
}
