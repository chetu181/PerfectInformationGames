package com.chaithanya.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.chaithanya.gameai.BoardState;
import com.chaithanya.gameai.Outcome;

public class ChessBoard implements BoardState {
	/**
	 * string of length 64.
	 * Represents the pieces in a chess board o
	 */
	//TODO: make this a 2d array
	private char[][] boardString = new char[8][8];
	
	private boolean whitesTurn;
	private boolean whiteKingMoved;
	private boolean blackKingMoved;
	
	private final int boardSize = 8;
	private final int[] dx = {1,1,0,-1,-1,-1,0,1};
	private final int[] dy = {0,1,1,1,0,-1,-1,-1};
	
	public ChessBoard() {
		String board = 
				"RNBQKBNR" +
				"PPP...P." +
				"........" +
				"........" +
				"........" +
				"........" +
				".ppp.ppp" +
				"rnbqkbnr" ;
		for(int i=0;i<boardSize;i++) {
			boardString[i]=board.substring(8*i, 8*i+8).toCharArray();
		}						
		whitesTurn=false;
		whiteKingMoved=false;// to use this in case of castling
		blackKingMoved=false;
	}
	

	public ChessBoard(char[][] boardString, boolean whitesTurn, boolean whiteKingMoved, boolean blackKingMoved) {
		super();
		for(int i=0;i<boardSize;i++)for(int j=0;j<boardSize;j++)
			this.boardString[i][j]=boardString[i][j];
		this.whitesTurn = whitesTurn;
		this.whiteKingMoved = whiteKingMoved;
		this.blackKingMoved = blackKingMoved;
	}
	
	@Override
	public List<BoardState> getLegalMoves() {
		// TODO Auto-generated method stub
		List<BoardState> nextMoves = new ArrayList<>();
		if(whitesTurn) {
			for(int i=0;i<boardSize;i++)for(int j=0;j<boardSize;j++) {
				if(boardString[i][j]>='A' && boardString[i][j]<='Z'){//if white piece , TODO: this if condition can be removed later
					switch(boardString[i][j]) {
						case 'K':{
							for(int dir=0;dir<8;dir++) {
								addMoves(nextMoves,i,j,dir,1);
							}
							break;
						}
						case 'Q':{
							for(int dir=0;dir<8;dir++) {
								addMoves(nextMoves,i,j,dir,8);								
							}
							break;
						}
						case 'R':{
							for(int dir=0;dir<8;dir+=2) {
								addMoves(nextMoves,i,j,dir,8);								
							}
							break;
						}
						case 'B':{
							for(int dir=1;dir<8;dir+=2) {
								addMoves(nextMoves,i,j,dir,8);								
							}
							break;
						}
						case 'N':{
							addKnightMoves(nextMoves,i,j);
							break;
						}
						case 'P':{
							addPawnMoves(nextMoves,i,j);
							break;
						}
						
					}
				}
			}
		} else  {
			for(int i=0;i<boardSize;i++)for(int j=0;j<boardSize;j++) {
				if(boardString[i][j]>='a' && boardString[i][j]<='z'){//if white piece , TODO: this if condition can be removed later
					switch(boardString[i][j]) {
						case 'k':{
							for(int dir=0;dir<8;dir++) {
								addMoves(nextMoves,i,j,dir,1);
							}
							break;
						}
						case 'q':{
							for(int dir=0;dir<8;dir++) {
								addMoves(nextMoves,i,j,dir,8);								
							}
							break;
						}
						case 'r':{
							for(int dir=0;dir<8;dir+=2) {
								addMoves(nextMoves,i,j,dir,8);								
							}
							break;
						}
						case 'b':{
							for(int dir=1;dir<8;dir+=2) {
								addMoves(nextMoves,i,j,dir,8);								
							}
							break;
						}
						case 'n':{
							addKnightMoves(nextMoves,i,j);
							break;
						}
						case 'p':{
							addPawnMoves(nextMoves,i,j);
							break;
						}
						
					}
				}
			}
		}
		return nextMoves;
	}
	private void addPawnMoves(List<BoardState> nextMoves, int i, int j) {
		// TODO Auto-generated method stub
		
	}


	private void addKnightMoves(List<BoardState> nextMoves, int i, int j) {
		// TODO Auto-generated method stub
		
	}


	private void addMoves(List<BoardState> nextMoves, int i, int j, int dir, int maxDist) {
		
		for(int distance=1; distance<=maxDist;distance++) {
			int newi = i+dx[dir]*distance;
			int newj = j+dy[dir]*distance;
			if(inBoard(newi,newj) && boardString[newi][newj]=='.') {
				char pieceAtOldPlace = boardString[i][j];
				char pieceAtNewPlace = boardString[newi][newj];
				boardString[i][j]='.';
				boardString[newi][newj]=pieceAtOldPlace;
				nextMoves.add(new ChessBoard(boardString, !whitesTurn, true, blackKingMoved));//TODO: Needs to be agnostic to white or blacks turn
				boardString[i][j]=pieceAtOldPlace;
				boardString[newi][newj]=pieceAtNewPlace;
			}
			else if(inBoard(newi,newj) ){//Capturing opponents piece
				char pieceAtOldPlace = boardString[i][j];
				char pieceAtNewPlace = boardString[newi][newj];
				boolean capturingBlack = (pieceAtNewPlace >= 'a');
				if( (whitesTurn && !capturingBlack) || (!whitesTurn && capturingBlack) )
					break;
				boardString[i][j]='.';
				boardString[newi][newj]=pieceAtOldPlace;
				nextMoves.add(new ChessBoard(boardString, !whitesTurn, true, blackKingMoved));//TODO: Needs to be agnostic to white or blacks turn
				boardString[i][j]=pieceAtOldPlace;
				boardString[newi][newj]=pieceAtNewPlace;
				break;
			}
			else {
				break;
			}
		}
	}


	private void swapChars(char[][] array, int i, int j, int i2, int j2) {
		char temp  = array[i][j];
		array[i][j]=array[i2][j2];
		array[i2][j2]=temp;
	}

	private boolean inBoard(int i, int j) {
		return ( i>=0 && j>=0 && i<boardSize && j<boardSize );
	}

	/**
	 * If checkmate return WHO won(white or black), else return PLAYING
	 */
	@Override
	public Outcome outCome() {
		boolean whiteKingFound = false;
		boolean blackKingFound = false;
		for(int i=0;i<boardSize; i++) for(int j=0;j<boardSize; j++){
			if(boardString[i][j]=='K')
				whiteKingFound = true;
			if(boardString[i][j]=='k')
				blackKingFound = true;
		}
		if(whiteKingFound && blackKingFound)
			return Outcome.PLAYING;
		if(!whiteKingFound) {
			return whitesTurn? Outcome.OPPONENTWON : Outcome.YOUWON;
		}
		if(!blackKingFound) {
			return whitesTurn? Outcome.YOUWON : Outcome.OPPONENTWON;
		}
		return null;
	}

	@Override
	public String toString() {
		String toReturn = "\n";
		for(int i=0;i<boardSize;i++) {
			for(int j=0;j<boardSize;j++)
			toReturn = toReturn+ boardString[i][j]+" " ;
			toReturn = toReturn +"\n";
		}
		toReturn = toReturn +"\n" + (whitesTurn? "white" :"black") + " to play..\n\n";
		return toReturn;
	}
	
}
