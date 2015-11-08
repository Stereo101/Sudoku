package sudoku;

public class Grid {

	
	public static int[][] makeGrids(){
		int[][] grids = new int[10][121];
		int q = 0;
		while(q<=9) {
		
			for(int i = 1; i<=9; i++) {
				grids[q][i] = -1;
				grids[q][i+110] = -1;
			}
			
			for(int i = 0; i<=120; i+=11) {
				if(i%11==0) {
					grids[q][i] = -1;
					grids[q][i+10] =-1;
				}
			}
			q++;
		}
		
		return grids;
	}
	
	public static SuBo setupFromString(String setup,SuBo board) {
		String name = "";
		int offset = 0;
		while(setup.charAt(offset)!='~'){
			name += setup.charAt(offset);
			offset++;
		}
		offset++;
		
		int q = 12;
		for(int i = 0; (i)<=88; i++) {
			if(setup.charAt(i+offset)=='/'){
				q+=1;
			} else if(setup.charAt(i+offset)=='-' || setup.charAt(i+offset)== '.') {
				board.grids[0][i+q]=0;
			} else {
				Grid.mark(i+q,(int)setup.charAt(i+offset)-'0', 1, board);
			}
		}
		board.name = name;
		return board;
	}
	
	public static void printAll(SuBo board) {
			for(int q = 0; q<=9; q++){
				System.out.println("\n\n\n				BOARD " + q + "\n___________________________________________________");
				for(int i = 0; i<=120; i++) {
					
					if(i%9==0){
						System.out.print("\n\n");
					}
					
					System.out.printf("%4d",board.grids[q][Operation.indexToGrid(i)]);
				}
			}
			System.out.println();
	}
	
	public static void print(SuBo board) {
			
		System.out.print("\n\n\n BOARD :: " + board.name + "\n ______________________________________\n|");
			//fuck the variable name police
			//you need an alphabet PHD to understand this mess 
			int ww = 0;
			int q = 0;
			int k = 0;
			int l = 0;
			int w = 0;
			for(int i = 12; i<=108; i++) {
				if(q==9){
					k++;
					q=0;
					if(k==3) {
						System.out.print(" |\n|____________|____________|____________|\n|");
						w=0;
						k=0;
						l=0;
					} else {
						System.out.print(" |\n|            |            |            |\n|");
						w=0;
						l=0;
					}
					
				}
				if(board.grids[0][i]!=-1){
					ww = board.grids[0][i];
					if(ww!=0){
						System.out.printf("%3d",board.grids[0][i]);
					} else {
						System.out.printf("%3s",".");
					}
					
					l++;
					if(l==3){
						if(w!=2){
							System.out.print(" |");
						} else {
							w=0;
						}
						l=0;
						w++;
					} else {
						System.out.print(" ");
					}
					q++;
				}
			}
			System.out.print(" |\n|____________|____________|____________|\n");
	}


	public static SuBo mark(int square, int numType, int moveType, SuBo board) {
		//System.out.println("Marking square " + square + " as a " + numType + ". Move Type: " + moveType);
		
		//Add to move list
		board.moveList[board.movesMade] = new Move(numType,square,moveType,0);
		board.movesMade++;
		
		//mark spot on main board
		board.grids[0][square] = numType;
		
		//mark spot on all subboards
		for(int i = 1; i<=9; i++){
			board.grids[i][square]++;
		}
		
		//box/crossfill specific subboard
		Operation.crossFill(square, 1, board.grids[numType]);
		Operation.boxFill(square, 1, board.grids[numType]);
		return(board);
	}
	
	public static SuBo undoMove(SuBo board) {
		//System.out.println("Marking square " + square + " as a " + numType + ". Move Type: " + moveType);
		Move temp = board.moveList[board.movesMade-1];
		board.movesMade--;
		
		
		
		//mark spot on main board as 0
		board.grids[0][temp.square] = 0;
		
		//unmark spot on all subboards
		for(int i = 1; i<=9; i++){
			board.grids[i][temp.square]--;
		}
		
		//unbox/uncrossfill specific subboard
		Operation.crossFill(temp.square, -1, board.grids[temp.num]);
		Operation.boxFill(temp.square, -1, board.grids[temp.num]);
		return(board);
	}
	
	

	//Function not used anymore
	//It redundantly marks
	//faster to just undo moves
	public static SuBo iniSubGrid(SuBo board) {
		for(int q = 1; q<=9; q++){
			for(int i = 12; i<=120; i++){
				
				if(board.grids[0][i]==-1){
					i+=1;
				} else if(board.grids[0][i]==0) {
					
				} else {
					Operation.crossFill(i, 1, board.grids[board.grids[0][i]]);
					Operation.boxFill(i, 1, board.grids[board.grids[0][i]]);
				}
			}
		}
		
		return board;
	}
	
	public static void moveList(SuBo board) {
		String status = "ERROR";
		int[] moveMap = new int[121];
		
		for(int i = 0; i<=board.movesMade-1; i++){
			switch(board.moveList[i].moveType) {
			case -1: status = "ERROR"; break;
			case 1: status = "setup"; break;
			case 2: status = "logical search"; break;
			case 3: status = "guessing"; break;
			}
			moveMap[board.moveList[i].square]++;
			System.out.println("#" + (i+1) + ": " + board.moveList[i].num + " placed on " + board.moveList[i].square + " during " + status + " overwriting " + board.moveList[i].overWrite);
			
		}
		
		for(int i = 0; i<=120; i++) {
			if(i%11==0) {
				System.out.println();
			}
			System.out.print(moveMap[i] + " ");
		}
		return;
	}
	
	public static void rateDif(SuBo board) {
		int depth = 0;
		if(board.state==1) {
			for(int i = 0; i<=80; i++) {
				if(board.moveList[i].moveType==3) {
					depth++;
				}
			}
			System.out.println(board.name + " took " + depth + " blind guesses to solve");
		} else {
			System.out.println("Cannot rate difficulty, board not solved.");
		}
	}
	
	public static void walkSol(SuBo board, long pauseInMili) {
		SuBo disBoard = new SuBo();
		disBoard.name = board.name;
		for(int i = 0; i<=80; i++) {
			Grid.mark(board.moveList[i].square, board.moveList[i].num, board.moveList[i].moveType, disBoard);
			if(board.moveList[i].moveType!=1) {
				Grid.print(disBoard);
				try {
					Thread.sleep(pauseInMili);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}

