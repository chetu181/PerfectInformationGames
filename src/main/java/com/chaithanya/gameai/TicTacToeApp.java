package com.chaithanya.gameai;

public class TicTacToeApp {

	public static void main(String[] args) {
		//boardTest();
		long startTime = System.currentTimeMillis();
		
		TicBoard board=new TicBoard(3);
		try {
			//board = new TicBoard("X......OO......X", "X", 4);
			//board = new TicBoard("X.X....OO......X", "O", 4);
			//board = new TicBoard("X.X....OO...O..X", "X", 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Player[] players = { new MiniMaxBotPlayer(),new MiniMaxBotPlayer() };
		
		System.out.println(board.display());
		for(int i=0;board.outCome()==Outcome.PLAYING;i++) {
			board = (TicBoard) players[i%2].move(board);//TODO:Gotta use generics here
			System.out.println(board.display());
		}
		
		String result = "It's a Draw :|";
		if(board.outCome()==Outcome.YOUWON)
			result = board.getWhoToPlay()+" WON";
		else if(board.outCome()==Outcome.OPPONENTWON)
			result = board.getWhoToPlay()+" LOST";
		
		System.out.println("Final Result: "+ result);
		
		long endTime = System.currentTimeMillis();
		System.out.println("\nExecution Time: "+(endTime-startTime)/1000.0 + " Seconds.");

	}

	private static void boardTest() {
		System.out.println(new TicBoard(3).getLegalMoves());
		System.out.println(new MiniMaxBotPlayer().removeTicTacToeSpuriousStates(
				new TicBoard(3).getLegalMoves()
				)
		);
	}

}
