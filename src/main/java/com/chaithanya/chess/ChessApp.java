package com.chaithanya.chess;

import com.chaithanya.gameai.BotPlayer;
import com.chaithanya.gameai.MiniMaxBotPlayer;
import com.chaithanya.gameai.Outcome;
import com.chaithanya.gameai.Player;

public class ChessApp {

	public static void main(String[] args) {
		//testMinMax();System.exit(0);
		ChessBoard chessBoard = new ChessBoard(); 
		
		//System.out.println(chessBoard.getLegalMoves());
		//System.exit(0);
		
		Player[] players = {new HumanChessPlayer(),new MinimaxChessPlayer(4)};
		System.out.println(chessBoard);
		int move;
		for(move=0; move<1000 && chessBoard.outCome()==Outcome.PLAYING;move++) {
			System.out.println("Move: "+(move+1));
			ChessBoard nextState= (ChessBoard)players[move%2].move(chessBoard);
			System.out.println(ChessBoard.chessMoveNotation(chessBoard,nextState));
			chessBoard=nextState;
			System.out.println(chessBoard.getBoardString());
			System.out.println(chessBoard);
			
		}
		if(chessBoard.outCome()==Outcome.PLAYING) {
			System.out.println("Still playing");
			return;
		}
		if( 	(move%2==0 && chessBoard.outCome().equals(Outcome.YOUWON)) 
			||	(move%2!=0 && chessBoard.outCome().equals(Outcome.OPPONENTWON))	)
			System.out.println("White wins");
		else
			System.out.println("Black wins");
		System.out.println("after "+move+" Moves");
	}

	private static void testMinMax() {
		char[][] boardString = new char[8][8];
		String board = 
				".NBKQB..RPPPPPRPP....NP..............b..p.pp.pp..p..pn.prn.kqb.r\n" + 
				"" ;
		for(int i=0;i<8;i++) {
			boardString[i]=board.substring(8*i, 8*i+8).toCharArray();
		}							
		ChessBoard chessBoard = new ChessBoard(boardString, true, false, false);
		System.out.println(chessBoard);
		long startTime = System.currentTimeMillis();
		System.out.println(new MinimaxChessPlayer(6).move(chessBoard));
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken: "+(endTime-startTime)/1000.0+" seconds");
		
	}


}
