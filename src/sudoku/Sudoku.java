package sudoku;

public class Sudoku {
	public static void main(String[] args) {
		//PROJECT LAYOUT
		//---------------------------------------------------
		//SUDOKU: is where the main function is, you are here
		//		Its where you choose what functions you want to use.
		//		Also where all the unsolved puzzles lay dormant as strings
		
		//SUBO: The class which defines what data is included in the (Su)_doku (Bo)_ard
		
		//SOLVE: Class for higher level methods which are used to either solve, or assist a solve
		//		Notable methods:: Solve.simple will only make moves that are 100% logically certain and is always exhausted before guesses are made
		//							Solve.minConSlice will call solve.simple, and then recursively searches for and make guesses based on constraints.
		//								This is currently the fastest solver here
		//							Solve.copySlice and Solve.undoSlive scan rows, columns, and boxes for possible moves, then guess on 50/50's
		//								copySlice creates a new board each time it makes a guess while undoSlice undoes moves when a bad branch is found
		//
		//
		//			Basic logic of the solve :::
		//			solve.simple -> test_if_complete(return solution) -> test_if_invalid(revert to last good boardstate)-> save board state -> make guess (goto start).
		
		//TEST: Class holding methods used mostly for debugging
		
		//OPERATION: Class for holding more trivial operations that come up often
		
		//GRID: Class for holding operations that act on a SuBo, but don't solve it
		//		Contains the Grid.mark() function, which does all the heavy lifting
		
		//MOVE: Class for defining what a move is.
		//		Used for keeping track of past actions which in turn allows to you undo moves, and playback a solution
		//---------------------------------------------------
		
		//There are 10 grids, 0 through 9
		//the 0 grid is the main board
		//grids 1-9 are for each individual number
			//individual number's grids keep track of where they CANT be placed
			//0 for possible, 1 and higher for not possible
			//Letting the spot increment upwards past one allows you to undo moves much easier
			//Its faster to undo 1 move than to rebuild the board from scratch.
			//useful for process of elimination simple sudoku solve
		//. on grid space means not yet filled.
		//-1 means off board space (but wont be printed)
		//otherwise a number represents what occupies the space
		
		//Unsolved boards are saved as strings, loading into the board character by character
		//The name of the board is everything up till ~
		
		///////////////////
		//SIMPLE SOLVABLE// (NO GUESSING)
		///////////////////
		
		String easySudoku = "easySudoku~8--2-6-4-/4--8--2-1/-91--7-6-/6--78----/--83-4--7/-27---41-/----65-3-/-3-4-85--/5-6---7-9";
		String sudoku2 = "sudoku2~--734--1-/15--7---3/69----8--/3192-7--6/7-------1/4--9-1752/--6----47/9---2--68/-7--641--";
		String sudokumedium = "sudokumedium~-29-4-53-/-47------/1---2---9/8--4---2-/-5--1--4-/-7---9--3/9---8---6/------29-/-63-5-78-";
		
		
		///////////////////////
		//NOT SIMPLE SOLVABLE// (SOME GUESSING)
		///////////////////////
		
		String evilsudoku1 = "evilsudoku1~--1--8---/2--------/-97--4-26/---84---9/--3---6--/4---56---/58-7--36-/--------1/---2--5--";
		String worldHardest = "worldHardest~8--------/--36-----/-7--9-2--/-5---7---/----457--/---1---3-/--1----68/--85---1-/-9----4--";
		String tokyo = "tokyo~-61--7--3/-92--3---/---------/--853----/------5-4/5----8---/-4------1/---16-8--/6--------";
		String extreme = "extreme~2-5---7-3/-8--3--6-/---4-----/--9-----7/-1--2--8-/3-----4--/-----1---/-4--6--2-/9-7---8-4";
		String evilsudoku2 = "evilsudoku2~1--9----4/-7--5--2-/4-8-2-6--/5----9---/-31-7-24-/---4----6/--9-4-8-7/-1--3--6-/2----6--1";
		String norvigH1 = "norvigH1~85...24../72......9/..4....../...1.7..2/3.5...9../.4......./....8..7./.17....../....36.4.";
		String norvigH2 = "norvigH2~..53...../8......2./.7..1.5../4....53../.1..7...6/..32...8./.6.5....9/..4....3./.....97..";
		String norvigH3 = "norvigH3~12..4..../..5.69.1./..9...5../.......7./7...52.9./.3......2/.9.6...5./4..9..8.1/..3...9.4";
		String norvigH4 = "norvigH4~...57..3./1......2./7...234../....8...4/..7..4.../49....6.5/.42...3../...7..9../..18.....";
		String norvigH5 = "norvigH5~7..1523../......92./...3...../1....47.8/.......6./........./..9...5.6/.4.9.7.../8....6.1.";
		String norvigH6 = "norvigH6~1....7.9./.3..2...8/..96..5../..53..9../.1..8...2/6....4.../3......1./.4......7/..7...3..";
		String norvigH7 = "norvigH7~1...34.8./...8..5../..4.6..21/.18....../3..1.2..6/......81./52..7.9../..6..9.../.9.64...2";
		String norvigH8 = "norvigH8~...92..../..68.3.../19..7...6/23..4.1../..1...7../..8.3..29/7...8..91/...5.72../....64...";
		String norvigH9 = "norvigH9~.6.5.4.3./1...9...8/........./9...5...6/.4.6.2.7./7...4...5/........./4...8...1/.5.2.3.4.";
		String norvigH10 = "norvigH10~7.....4../.2..7..8./..3..8.79/9..5..3../.6..2..9./..1.97..6/...3..9../.3..4..6./..9..1.35";
		String norvigH11 = "norvigH11~....7..2./8.......6/.1.2.5.../9.54....8/........./3....85.1/...3.2.8./4.......9/.7..6....";
		
		
		///////////////////////
		//PROBABLY UNSOLVABLE// (NO SOLUTION) Use for testing bruteforce exhaustion speed
		///////////////////////
		
		String norvig = "norvig~-----5-8-/---6-1-43/---------/-1-5-----/---1-6---/3-------5/53-----61/--------4/---------";
		
		///////////////////////
		
		
		String[] all = {easySudoku, sudoku2, sudokumedium, evilsudoku1, worldHardest, tokyo, extreme, evilsudoku2, norvigH1,
							norvigH2, norvigH3,norvigH4,norvigH5,norvigH6,norvigH7,norvigH8,norvigH9,norvigH10,norvigH11};
		
		String[] unSol = {norvig};
		
		//This solves each board in the string array all, walking through each solution with a 200 ms delay before starting on the next
		
		
		
		for(String s : all) {
			SuBo board = new SuBo();
			Grid.setupFromString(s, board);
			Solve.minConSlice(board);
			Grid.walkSol(board, 200);
			board.report();
			
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		
		Test.testSpeeds(all, 400);
	}
}
