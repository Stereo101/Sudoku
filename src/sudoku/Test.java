package sudoku;

public class Test {
	public static void boxFill() {
		System.out.println("\n\nTEST::BOX FILL PRINT\n");
		int boxS;
		int q = 0;
		for(int i = 12; i<=108; i++) {
			
			if(q==9){
				i+=2;
				q=0;
				System.out.print("\n\n");
			}
			int col = ((i%11)-1)/3;
			int row = ((i/11)-1)/3;
			boxS = 99;
			
			switch(row){
			case 0: 
				switch(col){
				case 0: boxS = 12;break;
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
			System.out.printf("%5d",boxS);
			q++;
		}
		System.out.println("\n\nTEST::BOX FILL PRINT END\n");
	}

	public static void innerIndex() {
		System.out.println("\n\nTEST::INNER INDEX PRINT\n");
		int q = 0;
		for(int i = 12; i<=108; i++) {
			if(q==9){
				System.out.print("\n\n");
				i+=2;
				q=0;
			}
			System.out.printf("%5d",i);
			q++;
		}
		System.out.println("\n\nTEST::INNER INDEX PRINT END\n\n");
	}

	public static void testSpeeds(String[] puzzles,int repitions) {
		long time;
		long fin;
		System.out.println("Solving " + puzzles.length + " puzzles " + repitions + " times each");
		time = System.currentTimeMillis();
		for(int i = repitions; i>0; i--){
			for(String s: puzzles){
				SuBo sudoku = new SuBo();
				Grid.setupFromString(s, sudoku);
				sudoku = Solve.minConSlice(sudoku);
			}
		}
		fin = (System.currentTimeMillis() - time);
		System.out.println("minConSlice took " + (fin) + " to solve all puzzles " + repitions + " times (" + (puzzles.length * repitions) + " total). Avg time per sol: " + ((float)fin/(float)(puzzles.length*(repitions))));
		
		
		time = System.currentTimeMillis();
		for(int i = repitions; i>0; i--){
			for(String s: puzzles){
				SuBo sudoku = new SuBo();
				Grid.setupFromString(s, sudoku);
				sudoku = Solve.copySlice(sudoku);
		
			}
		}
		fin = (System.currentTimeMillis() - time);
		System.out.println("copySlice took " + (fin) + " to solve all " + repitions + " times. Avg time per sol: " + ((float)fin/(float)(puzzles.length*(repitions))));
		
		
		time = System.currentTimeMillis();
		for(int i = repitions; i>0; i--){
			for(String s: puzzles){
				SuBo sudoku = new SuBo();
				Grid.setupFromString(s, sudoku);
				sudoku = Solve.undoSlice(sudoku);
			}
		}
		fin = (System.currentTimeMillis() - time);
		System.out.println("undoSlice took " + (fin) + " to solve all " + repitions + " times. Avg time per sol: " + ((float)fin/(float)(puzzles.length*(repitions))));
		
	}

	public static SuBo genBoard(int numFilled) {
		SuBo board = new SuBo();
		boolean flag = true;
		int sqr = 0;
		int num = 0;
		board.state = -1;
		while(board.state != 0) {
			board = new SuBo();
			for(int i = numFilled; i>0; i--) {
				flag = true;
				while(flag) {
					sqr = Operation.indexToGrid((int)(Math.random()*80));
					num = ((int)(Math.random()*9) +1);
					
					while(board.grids[0][sqr] != 0) {
						sqr = Operation.indexToGrid((int)(Math.random()*80));
					}
					
					Grid.mark(sqr, num , 1, board);
					
					if(Solve.checkValid(board)) {
						flag = false;
					} else {
						//System.out.println("didnt work");
						Grid.undoMove(board);
					}
				}
				
			}
		}
		return board;
	}
}
