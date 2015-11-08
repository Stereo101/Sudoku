package sudoku;


public class Solve {
	
	public static SuBo copySlice(SuBo board) {
		//int[][] grids = board.grids;
		simple(board);
		if(board.state==1){
			return board;
		} else if(board.state==-1){
			//System.out.println("invalid board state, ending leaf of slice");
			return board;
		}
			////FIND INSTANCE OF 2 OPTIONS
			////SPLIT AND SEARCH EACH OPTION FOR A SOLUTION
			////GIVE UP IF NO MORE BISECTIONS OCCURE
			////Steve Jobs: "IT JUST WERKS"
			int sqr;
			int countFound;
			int option1;
			int option2;
			int numType = 0;
			
			for(int i=1; i<=9; i++) {
				//act on each col
				for(int q = 12; q<=20; q++) {
					numType = i;
					countFound = 0;
					option1 = 0;
					option2 = 0;
					sqr = q;
					
					while((sqr<=120&&countFound<3)){
						if(board.grids[i][sqr]==0){
							if(option1 != 0){
								option2 = sqr;
							} else {
								option1 = sqr;
							}
							countFound++;
						}
						sqr+=11;
					}
					if(countFound==2) {
						SuBo test1 = Operation.copySubo(board);
						Grid.mark(option1, numType, 3, test1);
						test1 = copySlice(test1);
						if(test1.state==1){
							return test1;
						} else {
							SuBo test2 = Operation.copySubo(board);
							Grid.mark(option2, numType, 3, test2);
							test2 = copySlice(test2);
							return test2;
						}
					}
				}
			}
			board.state = -1;
			return board;
				/*
				//act on each col
				for(int q = 12; q<=100; q+=11) {
					countFound = 0;
					sqr = q;
					while((grids[i][sqr]!=-1)&&countFound!=2){
						if(grids[i][sqr]==0){
							countFound++;
							squareFound = sqr;
						}
						sqr++;
					}
					if(countFound==1) {
						check.square = squareFound;
						check.numType = i;
						return(check);
					}
				}
				*/
			
		}
	
	public static SuBo simple(SuBo board) {
		Move check;
		boolean exitFlag = false;
		while(!exitFlag) {
			check = poeSearch(board);
			if(check.square == -1){
				check = findBoxFill(board);
				if(check.square == -1) {
					check = findMinConof1(board);
					if(check.square == -1) {
						exitFlag = true;
					} else {
						Grid.mark(check.square, check.num, 2, board);
					}
					
				} else {
					//System.out.print("\nBOX::Marking square " + check.square + " as an " + check.num + "\n");
					Grid.mark(check.square, check.num, 2, board);
				}
				
			} else {
				//System.out.print("\nPOE::Marking square " + check.square + " as an " + check.num + "\n");
				Grid.mark(check.square, check.num, 2, board);
			}
		}
		Solve.checkComplete(board);
		return(board);
	}
	
	public static Move findMinConof1(SuBo board) {
		Move check = new Move(-1,-1,2,-1);
		int min = 99;
		int holdcount = 0;
		int holdref = 0;
		int holdval = 0;
		int ref = 0;
		
		for(int i = 0; i<=80&&min>1; i++) {
			ref = Operation.indexToGrid(i);
			holdcount = 0;
			for(int q = 1; q<=9; q++) {
				if(board.grids[q][ref]==0){
					holdcount++;
					holdval = q;
				}
			}
			if(holdcount<min&&holdcount!=0) {
				min = holdcount;
				holdref = ref;
			}
			if(min==1){
				check.square = holdref;
				check.num = holdval;
				check.overWrite = 0;
				return check;
			}
		}
		
		return check;
		
	}
	
	public static SuBo hypSolve(SuBo board) {
		//HypSolve should be a standard recursive brute force search
		//Hypothetically, we should be able to brute force, but then switch to quicker methods
		//Probably guaranteed to solve given infinite time so thats nice
		
		SuBo hypgrid = new SuBo();
		board = Solve.simple(board);
		if(board.state==1){
			return board;
		} else if(board.state==-1){
			System.out.println("invalid board state, ending leaf of hyp");
			return board;
		} else {
			for(int i = 1; i<=9; i++){
				for(int q = 12; q<=120; q++) {
					if(board.grids[i][q]==0){
						System.out.println("Making a guess " + i + " " + q);
						hypgrid = Operation.copySubo(board);
						Grid.mark(q, i, 3, hypgrid);
						Solve.checkValid(hypgrid);
						if(hypgrid.state!=-1){
							hypgrid = Solve.hypSolve(hypgrid);
							if(hypgrid.state==1){
								System.out.println("Solution found");
								return hypgrid;
							}
						}
					}
				}
			}
		}
		System.out.println("hypsolve failed");
		return hypgrid;
	}
	
	public static Move poeSearch(SuBo board){
		Move check = new Move(-1,-1,2,-1);
		int countFound = -1;
		int squareFound = -1;
		int sqr;
		//act on each board
		for(int i=1; i<=9; i++) {
			//act on each row
			for(int q = 12; q<=20; q++) {
				countFound = 0;
				sqr = q;
				while((board.grids[i][sqr]!=-1)&&countFound!=2){
					if(board.grids[i][sqr]==0){
						countFound++;
						squareFound = sqr;
					}
					sqr+=11;
				}
				if(countFound==1) {
					check.square = squareFound;
					check.num = i;
					check.overWrite = board.grids[0][sqr];
					return(check);
				}
			}
			
			//act on each col
			for(int q = 12; q<=100; q+=11) {
				countFound = 0;
				sqr = q;
				while((board.grids[i][sqr]!=-1)&&countFound!=2){
					if(board.grids[i][sqr]==0){
						countFound++;
						squareFound = sqr;
					}
					sqr++;
				}
				if(countFound==1) {
					check.square = squareFound;
					check.num = i;
					check.overWrite = board.grids[0][sqr];
					return(check);
				}
			}
		}
		
		return check;
	}

	public static Move findBoxFill(SuBo board) {
		Move check = new Move(-1,-1,2,-1);
		int[] boxIndex = {12,15,18,45,48,51,78,81,84};
		int countFound = 0;
		int squareFound = 0;
		
		for(int i = 0; i<=8; i++) {
			countFound = 0;
			squareFound = 0;
			for(int k = 0; k<=2 && (countFound!=2); k++) {
				for(int q = 0; (q<=2) && (countFound!=2); q++){
					if(board.grids[0][boxIndex[i]+(11*k)+q]==0){
						countFound++;
						squareFound = (boxIndex[i]+(11*k)+q);
					}
				}
			}
			if(countFound == 1) {
				int searchBox = Operation.indexToBox(squareFound);
				int[] poeArray = new int[10];
				
				for(int m = 0; m<=2; m++){
					for(int q = 0; q<=2; q++){
						poeArray[board.grids[0][searchBox+(m*11)+q]]=1;
					}
				}
				for(int q = 1; q<=9;q++){
					if(poeArray[q]==0){
						check.square = squareFound;
						check.num = q;
						check.overWrite = board.grids[0][squareFound];
						return(check);
					}
				}
			}
		}
		
		return check;
	}

	public static boolean checkValid(SuBo board) {
		//System.out.println("START VALID STATE CHECK");
		int[] checkArray = new int[10];
		int sqr;
		
		//act on each col
		
		for(int q = 12; q<=20; q++) {
			//System.out.println("_____Clearing array____");
			for(int m = 0; m<=9; m++){
				checkArray[m] = 0;
			}
			sqr = q;
			
			while((board.grids[0][sqr]!=-1)){
				if(board.grids[0][sqr]!=0){
					//System.out.println("COL:::add " + board.grids[0][sqr] + " to array");
					checkArray[board.grids[0][sqr]]++;;
					if(checkArray[board.grids[0][sqr]]==2) {
						//System.out.println("In number duplicate found, board not \nTwo instances of " + board.grids[0][sqr] + " found on col.");
						//System.out.println("END VALID STATE CHECK::Failed");
						board.state = -1;
						return false;
					}
				} else {
					//System.out.println("0 found");
				}
				sqr+=11;
			}
		}
		
		//act on each row
		for(int q = 12; q<=100; q+=11) {
			//System.out.println("_____Clearing array____");
			for(int m = 0; m<=9; m++){
				checkArray[m] = 0;
			}
			sqr = q;
			
			while((board.grids[0][sqr]!=-1)){
				if(board.grids[0][sqr]!=0){
					//System.out.println("ROW:::add " + board.grids[0][sqr] + " to array");
					checkArray[board.grids[0][sqr]]++;;
					if(checkArray[board.grids[0][sqr]]==2) {
						//System.out.println("In number duplicate found, board not \nTwo instances of " + board.grids[0][sqr] + " found on row.");
						//System.out.println("END VALID STATE CHECK::Failed");
						board.state = -1;
						return false;
					}
				} else {
					//System.out.println("0 found");
				}
				sqr+=1;
			}
		}
		//act on each box
		int[] boxIndex = {12,15,18,45,48,51,78,81,84};
		
		for(int i = 0; i<=8; i++) {
			//System.out.println("_____Clearing array____");
			for(int m = 0; m<=9; m++){
				checkArray[m] = 0;
			}
			
			for(int k = 0; k<=2; k++) {
				for(int q = 0; (q<=2); q++){
					if(board.grids[0][boxIndex[i]+(11*k)+q]!=0){
						//System.out.println("BOX:::add " + board.grids[0][boxIndex[i]+(11*k)+q] + " to array");
						checkArray[board.grids[0][boxIndex[i]+(11*k)+q]]++;;
						if(checkArray[board.grids[0][boxIndex[i]+(11*k)+q]]==2){
							//System.out.println("In number duplicate found, board not \nTwo instances of " + board.grids[0][boxIndex[i]+(11*k)+q] + " found in box.");
							//System.out.println("END VALID STATE CHECK::Failed");
							board.state = -1;
							return false;
						}
					} else {
						//System.out.println("0 found");
					}
				}
			}
		}
			
		
		//System.out.println("END VALID STATE CHECK::Passed");
		if(board.state != 1){
			board.state = 0;
		}
		return true;
	}

	public static boolean checkComplete(SuBo board) {
		//System.out.println("START SOLVED STATE CHECK::");
			int[] checkArray = new int[10];
			int sqr;
			int[] grid = board.grids[0];
			
			//act on each col
			
			for(int q = 12; q<=20; q++) {
				//System.out.println("_____Clearing array____");
				for(int m = 0; m<=9; m++){
					checkArray[m] = 0;
				}
				sqr = q;
				
				while((grid[sqr]!=-1)){
					if(grid[sqr]!=0){
						//System.out.println("COL:::add " + grid[sqr] + " to array");
						checkArray[grid[sqr]]++;;
						if(checkArray[grid[sqr]]==2) {
							//System.out.println("In number duplicate found, board not \nTwo instances of " + grid[sqr] + " found on col.");
							//System.out.println("END SOLVED STATE CHECK::Failed");
							board.state = -1;
							return false;
						}
					} else {
						//System.out.println("Found a 0, not complete");
						//System.out.println("END SOLVED STATE CHECK::Failed");
						return false;
					}
					sqr+=11;
				}
			}
			
			//act on each row
			for(int q = 12; q<=100; q+=11) {
				//System.out.println("_____Clearing array____");
				for(int m = 0; m<=9; m++){
					checkArray[m] = 0;
				}
				sqr = q;
				
				while((grid[sqr]!=-1)){
					if(grid[sqr]!=0){
						//System.out.println("ROW:::add " + grid[sqr] + " to array");
						checkArray[grid[sqr]]++;;
						if(checkArray[grid[sqr]]==2) {
							//System.out.println("In number duplicate found, board not \nTwo instances of " + grid[sqr] + " found on row.");
							//System.out.println("END SOLVED STATE CHECK::Failed");
							board.state = -1;
							return false;
						}
					} else {
						//System.out.println("Found a 0, not complete");
						//System.out.println("END SOLVED STATE CHECK::Failed");
						return false;
					}
					sqr+=1;
				}
			}
			//act on each box
			int[] boxIndex = {12,15,18,45,48,51,78,81,84};
			
			for(int i = 0; i<=8; i++) {
				//System.out.println("_____Clearing array____");
				for(int m = 0; m<=9; m++){
					checkArray[m] = 0;
				}
				
				for(int k = 0; k<=2; k++) {
					for(int q = 0; (q<=2); q++){
						if(grid[boxIndex[i]+(11*k)+q]!=0){
							//System.out.println("BOX:::add " + grid[boxIndex[i]+(11*k)+q] + " to array");
							checkArray[grid[boxIndex[i]+(11*k)+q]]++;;
							if(checkArray[grid[boxIndex[i]+(11*k)+q]]==2){
								//System.out.println("In number duplicate found, board not \nTwo instances of " + grid[boxIndex[i]+(11*k)+q] + " found in box.");
								//System.out.println("END SOLVED STATE CHECK::Failed");
								board.state = -1;
								return false;
							}
						} else {
							//System.out.println("Found a 0, not complete");
							//System.out.println("END SOLVED STATE CHECK::Failed");
							return false;
						}
					}
				}
			}
			
			//System.out.println("END SOLVED STATE CHECK::Passed");
			board.state = 1;
			return true;
		}
	
	public static SuBo minConSlice(SuBo board) {
		
		if(board.state!=0){
			return board;
		}
		
		//Solve.checkComplete(board);
		board = simple(board);
		
		if(board.state!=0){
			return board;
		}
		
		
		int min = 99;
		int holdcount = 0;
		int hold = 0;
		int ref = 0;
		
		for(int i = 0; i<=80&&min>2; i++) {
			ref = Operation.indexToGrid(i);
			holdcount = 0;
			for(int q = 1; q<=9; q++) {
				if(board.grids[q][ref]==0){
					holdcount++;
				}
			}
			if(holdcount<min&&holdcount!=0) {
				min = holdcount;
				hold = ref;
			}
		}
		
		//If no open constraints, board is broken
		if(min==99) {
			return board;
		}
		
		int[] op = new int[min];
		int place = 0;
		for(int i = 0; i<op.length;i++){
			op[i] = -1;
		}
		
		for(int i = 1; i<=9; i++) {
			if(board.grids[i][hold]==0) {
				//System.out.println("op" + (place+1) + " is " + i);
				op[place] = i;
				place++;
			}
		}
		//System.out.println("\n Trying out " + op1 + " " + op2 + " on sqaure " + hold);
		int holdMove = board.movesMade;
		int holdState = board.state;
		
		if(min == 1) {
			System.out.println("FUCK minCon found a 1 after sol didnt");
			Grid.mark(hold, op[0], 2, board);
			return minConSlice(board);
		} else {
			for(int i = 0; i<op.length;i++) {
				
				Grid.mark(hold, op[i], 3, board);
				board = minConSlice(board);
				if(board.state==1){
					return board;
				} else {
					while(board.movesMade>holdMove){
						Grid.undoMove(board);
					}
					board.state = holdState;
				}
			}
		}
		board.state = -1;
		return board;
		
	}
	
	
	public static SuBo undoSlice(SuBo board) {
		//int[][] grids = board.grids;
		board = simple(board);
		if(board.state==1){
			return board;
		} else if(board.state==-1){
			//System.out.println("invalid board state, ending leaf of slice");
			return board;
		}
			////FIND INSTANCE OF 2 OPTIONS
			////SPLIT AND SEARCH EACH OPTION FOR A SOLUTION
			////GIVE UP IF NO MORE BISECTIONS OCCURE
			////Steve Jobs: "IT JUST WERKS"
			int sqr;
			int countFound;
			int option1;
			int option2;
			int numType = 0;
			
			for(int i=1; i<=9; i++) {
				//act on each col
				for(int q = 12; q<=20; q++) {
					numType = i;
					countFound = 0;
					option1 = 0;
					option2 = 0;
					sqr = q;
					
					while((sqr<=120&&countFound<3)){
						if(board.grids[i][sqr]==0){
							if(option1 != 0){
								option2 = sqr;
							} else {
								option1 = sqr;
							}
							countFound++;
						}
						sqr+=11;
					}
					if(countFound==2) {
						//SuBo test1 = Operation.copySubo(board);
						int holdMove = board.movesMade;
						int holdState = board.state;
						Grid.mark(option1, numType, 3, board);
						board = undoSlice(board);
						if(board.state==1){
							return board;
						} else {
							while(board.movesMade>holdMove){
								Grid.undoMove(board);
							}
							board.state = holdState;
							Grid.mark(option2, numType, 3, board);
							board = undoSlice(board);
							return board;
						}
					}
				}
			}
			board.state = -1;
			return board;
				/*
				//act on each col
				for(int q = 12; q<=100; q+=11) {
					countFound = 0;
					sqr = q;
					while((grids[i][sqr]!=-1)&&countFound!=2){
						if(grids[i][sqr]==0){
							countFound++;
							squareFound = sqr;
						}
						sqr++;
					}
					if(countFound==1) {
						check.square = squareFound;
						check.numType = i;
						return(check);
					}
				}
				*/
			
		}
	
}