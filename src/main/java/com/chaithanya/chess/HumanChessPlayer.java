package com.chaithanya.chess;

import java.util.List;
import java.util.Scanner;

import com.chaithanya.gameai.BoardState;
import com.chaithanya.gameai.Player;

public class HumanChessPlayer implements Player {
	private static Scanner scanner;
	static {
		 scanner = new Scanner(System.in);
	}
	@Override
	public BoardState move(BoardState s) {
		ChessBoard chessBoard = (ChessBoard)s;
		//TODO: fix the scanner
		System.out.print("Your Move("+(chessBoard.whitesTurn?"white":"black")+") :");
		String input = scanner.nextLine();
		BoardState humanMove = getNextBoardState(input,chessBoard);
		while(humanMove==null) {
			System.err.println("Invalid Input.");
			input = scanner.nextLine();
			humanMove = getNextBoardState(input,chessBoard);
		}
		return humanMove;
		
	}

	private BoardState getNextBoardState(String input, ChessBoard chessBoard) {
		if(input.length()!=4) {
			System.err.println("Invalid input format:Enter the move in the form, say b3d5 if you want to move from b3 to d5");
			return null;
		}
		int y1=7-(int)(input.charAt(0)-'a');
		int x1=input.charAt(1)-'1';
		int y2=7-(int)(input.charAt(2)-'a');
		int x2=input.charAt(3)-'1';
		if(!ChessBoard.inBoard(x1, y1) || !ChessBoard.inBoard(x2, y2)) {
			System.err.println("Your moves are out of board.");
			return null;
		}
		char pieceAtSource = 		chessBoard.boardString[x1][y1];
		char pieceAtDestination = 	chessBoard.boardString[x2][y2];
		List<BoardState> legalMoves = chessBoard.getLegalMoves();
		for(int i=0;i<legalMoves.size();i++) {
			ChessBoard nextState = (ChessBoard)(legalMoves.get(i));
			if( nextState.boardString[x1][y1]=='.') {
				if (nextState.boardString[x2][y2]==pieceAtSource ) {
					return nextState;
				}
				else if( (pieceAtSource=='p' || pieceAtSource=='P') //Promotion Logic, (gets the first legal Move(Queen I think),TODO: need to accommodate for other pieces
						&& (x2==7 || x2==0) 
						&& nextState.boardString[x2][y2]!=chessBoard.boardString[x2][y2]) {
					return nextState;
				}
			} 
		}
		System.err.println("Your Move is not Legal");//TODO: take care of promotions
		return null;
	}

}
