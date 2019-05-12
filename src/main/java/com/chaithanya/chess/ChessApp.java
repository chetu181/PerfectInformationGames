package com.chaithanya.chess;

public class ChessApp {

	public static void main(String[] args) {
		ChessBoard chessBoard = new ChessBoard();
		System.out.println(chessBoard.toString());
		System.out.println(chessBoard.getLegalMoves());
	}

}
