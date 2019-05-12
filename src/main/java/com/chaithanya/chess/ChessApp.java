package com.chaithanya.chess;

import com.chaithanya.gameai.BotPlayer;
import com.chaithanya.gameai.MiniMaxBotPlayer;
import com.chaithanya.gameai.Outcome;
import com.chaithanya.gameai.Player;

public class ChessApp {

	public static void main(String[] args) {
		ChessBoard chessBoard = new ChessBoard();
		//System.out.println(chessBoard.getLegalMoves());
		//System.exit(0);
		Player[] players = {new MinimaxChessPlayer(3),new MinimaxChessPlayer(3)};
		System.out.println(chessBoard);
		int move;
		for(move=0; move<400 && chessBoard.outCome()==Outcome.PLAYING;move++) {
			System.out.println("Move: "+(move+1));
			ChessBoard nextState= (ChessBoard)players[move%2].move(chessBoard);
			System.out.println(ChessBoard.chessMoveNotation(chessBoard,nextState));
			chessBoard=nextState;
			System.out.println(chessBoard);
		}
		if( 	(move%2==0 && chessBoard.outCome().equals(Outcome.YOUWON)) 
			||	(move%2!=0 && chessBoard.outCome().equals(Outcome.OPPONENTWON))	)
			System.out.println("White wins");
		else
			System.out.println("Black wins");
	}


}
