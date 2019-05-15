package com.chaithanya.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.chaithanya.gameai.BoardState;
import com.chaithanya.gameai.Outcome;

public class ChessBoard implements BoardState {
	/**
	 * string of length 64.
	 * Represents the pieces in a chess board o
	 */
	//TODO: make this a 2d array
	public char[][] boardString = new char[8][8];
	
	public boolean whitesTurn;
	private boolean whiteKingMoved;
	private boolean blackKingMoved;
	
	public final int boardSize = 8;
	private final int[] dy = {1,1,0,-1,-1,-1,0,1};
	private final int[] dx = {0,1,1,1,0,-1,-1,-1};
	private final int[] dnx = {2,2,1,-1,-2,-2,-1,1};
	private final int[] dny = {-1,1,2,2,1,-1,-2,-2};
	private final char[] promotionPiece = {'Q','B','R','N'};
	private static Map<Character, Integer> scoreMap;
	
	static {
		scoreMap= new HashMap<>();
		scoreMap.put('Q', 9);
		scoreMap.put('q', -9);
		scoreMap.put('R', 5);
		scoreMap.put('r', -5);
		scoreMap.put('B', 3);
		scoreMap.put('b', -3);
		scoreMap.put('N', 3);
		scoreMap.put('n', -3);
		scoreMap.put('P', 1);
		scoreMap.put('p', -1);
	}
	public ChessBoard() {
		String board = 
				"RNBKQBNR" +
				"PPPPPPPP" +
				"........" +
				"........" +
				"........" +
				"........" +
				"pppppppp" +
				"rnbkqbnr" ;
		for(int i=0;i<boardSize;i++) {
			boardString[i]=board.substring(8*i, 8*i+8).toCharArray();
		}						
		whitesTurn=true;
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
		if(!outCome().equals(Outcome.PLAYING))
			return nextMoves;
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
							for(int dir=0;dir<8;dir++) {
								addKnightMoves(nextMoves,i,j,dir);
							}
							break;
						}
						case 'P':{
							for(int dir = 1;dir <4;dir++)
								addPawnMoves(nextMoves, i, j,dir);
							break;
						}
						
					}
				}
			}
			//TODO: Castling Logic
			
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
							for(int dir=0;dir<8;dir++) {
								addKnightMoves(nextMoves,i,j,dir);
							}
							break;
						}
						case 'p':{
							for(int dir=5;dir<8;dir++)
								addPawnMoves(nextMoves,i,j,dir);
							break;
						}
						
					}
				}
			}
		}
		return nextMoves;
	}
	private void addPawnMoves(List<BoardState> nextMoves, int i, int j,int dir) {
		int newi = i+dx[dir];
		int newj = j+dy[dir];
		if(!inBoard(newi, newj))
			return;
		if(dir==2 || dir==6) {//Moving ahead
			if(boardString[newi][newj]=='.') {
				if(newi==0 || newi==boardSize-1) {//Promotion Logic
					for(int promoteIndex= 0;promoteIndex<4;promoteIndex++) {
						addSingleComplexMove(nextMoves,i,j,newi,newj,(char)(boardString[i][j]-('p'-'a') +(promotionPiece[promoteIndex]-'A') ));
					}
				} else {//2 moves at the beginning
					if(( (i==1 && whitesTurn) || (i==6 && !whitesTurn))
							&& boardString[newi+dx[dir]][newj+dy[dir]]=='.')
						addSingleMove(nextMoves, i, j, newi+dx[dir], newj+dy[dir]);
					addSingleMove(nextMoves, i, j, newi, newj);
				}
			}
		}
		else {//Capturing
			if(boardString[newi][newj]=='.')
				return;
			boolean capturingBlack = (boardString[newi][newj] >= 'a');
			if( (whitesTurn && !capturingBlack) || (!whitesTurn && capturingBlack) )
				return;
			if(newi==0 || newi==boardSize-1) {//Promotion Logic
				for(int promoteIndex= 0;promoteIndex<4;promoteIndex++) {
					addSingleComplexMove(nextMoves,i,j,newi,newj,(char)(boardString[i][j]-('p'-'a') +(promotionPiece[promoteIndex]-'A') ));
				}
			} else {	
				addSingleMove(nextMoves, i, j, newi, newj);
			}
		} 
	}


	private void addSingleComplexMove(List<BoardState> nextMoves, int i, int j, int newi, int newj, char promotionPiece) {
		char pieceAtOldPlace = boardString[i][j];
		char pieceAtNewPlace = boardString[newi][newj];
		boardString[i][j]='.';
		boardString[newi][newj]=promotionPiece;
		nextMoves.add(new ChessBoard(boardString, !whitesTurn, true, blackKingMoved));//TODO: write a set of parellel methods to optimise this part
		boardString[i][j]=pieceAtOldPlace;
		boardString[newi][newj]=pieceAtNewPlace;
	}


	private void addKnightMoves(List<BoardState> nextMoves, int i, int j,int dir) {
		int newi = i+dnx[dir];
		int newj = j+dny[dir];
		if(inBoard(newi, newj) && boardString[newi][newj]=='.' ) {//TODO: could be made simpler
			addSingleMove(nextMoves,i,j,newi,newj);
		} 
		else if(inBoard(newi,newj) ){//Capturing opponents piece
			boolean capturingBlack = (boardString[newi][newj] >= 'a');
			if( (whitesTurn && !capturingBlack) || (!whitesTurn && capturingBlack) )
				return;
			addSingleMove(nextMoves,i,j,newi,newj);
			return;
		}
		else {
			return;
		}
	}


	private void addMoves(List<BoardState> nextMoves, int i, int j, int dir, int maxDist) {
		
		for(int distance=1; distance<=maxDist;distance++) {
			int newi = i+dx[dir]*distance;
			int newj = j+dy[dir]*distance;
			if(inBoard(newi,newj) && boardString[newi][newj]=='.') {
				addSingleMove(nextMoves,i,j,newi,newj);
			}
			else if(inBoard(newi,newj) ){//Capturing opponents piece
				boolean capturingBlack = (boardString[newi][newj] >= 'a');
				if( (whitesTurn && !capturingBlack) || (!whitesTurn && capturingBlack) )
					break;
				addSingleMove(nextMoves,i,j,newi,newj);
				break;
			}
			else {
				break;
			}
		}
	}


	private void addSingleMove(List<BoardState> nextMoves, int i, int j, int newi, int newj) {
		char pieceAtOldPlace = boardString[i][j];
		char pieceAtNewPlace = boardString[newi][newj];
		boardString[i][j]='.';
		boardString[newi][newj]=pieceAtOldPlace;
		
		boolean hasWhiteKingMoved = whiteKingMoved;
		boolean hasBlackKingMoved = blackKingMoved;
		if(pieceAtOldPlace=='K')
			hasWhiteKingMoved=true;
		if(pieceAtOldPlace=='k')
			hasBlackKingMoved=true;
		nextMoves.add(new ChessBoard(boardString, !whitesTurn, hasWhiteKingMoved, hasBlackKingMoved));//TODO: Needs to be agnostic to white or blacks turn
		boardString[i][j]=pieceAtOldPlace;
		boardString[newi][newj]=pieceAtNewPlace;
	}


	private void swapChars(char[][] array, int i, int j, int i2, int j2) {
		char temp  = array[i][j];
		array[i][j]=array[i2][j2];
		array[i2][j2]=temp;
	}

	public static boolean inBoard(int i, int j) {
		return ( i>=0 && j>=0 && i<8 && j<8 );
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
		for(int i=boardSize-1;i>=0;i--) {
			for(int j=boardSize-1;j>=0;j--)
			toReturn = toReturn+ boardString[i][j]+" " ;
			toReturn = toReturn +"\n";
		}
		toReturn = toReturn+"_______________________"+(whitesTurn? "white" :"black") 
				+ " to play..(White score:"+stateScore()*(whitesTurn?1:-1)+")\n\n";
		return toReturn;
	}

	/**
	 * Gives the move name given two states, output will be something like RxC2, QC3 etc.
	 * @param currentState
	 * @param nextState
	 * @return
	 */
	public static String chessMoveNotation(ChessBoard currentState, ChessBoard nextState) {
		String ans=(currentState.getWhitesTurn()?"":"..");
		char pieceToMove='?';
		String action = "";
		for(int i=0;i<currentState.boardSize;i++) {
			for(int j=0;j<currentState.boardSize;j++) {
				if(currentState.boardString[i][j]!='.' && nextState.boardString[i][j]=='.') {
					pieceToMove = currentState.boardString[i][j];
				} else if( currentState.boardString[i][j]!=nextState.boardString[i][j] ) {
					if(currentState.boardString[i][j]!='.')
						action+="x";
					action+=(char)('a'+(7-j));
					action+=(char)('1'+i);
				}
			}
		}
		return ans+pieceToMove+action;
	}


	private boolean getWhitesTurn() {
		// TODO Auto-generated method stub
		return whitesTurn;
	}


	@Override
	public int stateScore() {
		if(outCome()==Outcome.YOUWON)
			return 1000;//TODO: is 1000 the right max?
		else if(outCome()==Outcome.OPPONENTWON)
			return -1000;
		
		int score = 0;
		for(int i=0;i<boardSize;i++)
			for(int j=0;j<boardSize;j++) 
				if(scoreMap.containsKey(boardString[i][j]))
					score+=scoreMap.get(boardString[i][j]);
		return score * (whitesTurn? 1 : -1);
	}


	public String getBoardString() {
		String toReturn = "";
		for(int i=0;i<8;i++)
			toReturn+=new String(boardString[i]);
		return toReturn;
	}
	
}
