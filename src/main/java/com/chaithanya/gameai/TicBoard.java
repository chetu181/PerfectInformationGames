package com.chaithanya.gameai;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class TicBoard implements BoardState{
	private String boardString = "";
	private String whoToPlay = "";
	private int sideLength ;
	
	private List<List<Integer> > wins;
	
	private void initialiseWins() {
		wins = new ArrayList<List<Integer>>();
		for(int i=0;i<sideLength;i++) {//Rows
			List<Integer> win = new ArrayList<>();
			for(int j=0;j<sideLength;j++)
				win.add(i*sideLength+j);
			wins.add(win);
		}
		for(int i=0;i<sideLength;i++) {//Columns
			List<Integer> win = new ArrayList<>();
			for(int j=0;j<sideLength;j++)
				win.add(i+j*sideLength);
			wins.add(win);
		}
		
		List<Integer> win1 = new ArrayList<>();//Diagonal1
		for(int i=0;i<sideLength;i++) {
			win1.add(i*sideLength+i);
		}
		wins.add(win1);
		
		List<Integer> win2 = new ArrayList<>();//Diagonal2
		for(int i=0;i<sideLength;i++) {
			win2.add((i+1)*(sideLength-1));
		}
		wins.add(win2);
		
	}
	public TicBoard(int sideLength) {
		boardString= StringUtils.repeat('.', sideLength*sideLength);
		whoToPlay = "X";
		this.sideLength = sideLength;
		initialiseWins();
	}
	
	public TicBoard(String boardString, String whoToPlay,int sideLength) throws Exception{
		super();
		if(boardString.length()!=sideLength*sideLength)
			throw new Exception("BoardString should be a square of side "+sideLength);
		
		this.boardString = boardString;
		this.whoToPlay = whoToPlay;
		this.sideLength = sideLength;	
		initialiseWins();
	}

	@Override
	public List<BoardState> getLegalMoves() {
		List<BoardState> nextLegalStates = new ArrayList<>();
		if(this.outCome()!=Outcome.PLAYING)
			return nextLegalStates;
		for(int i=0;i<sideLength*sideLength;i++) {
			if(boardString.charAt(i)=='.') {
				String nextBoardString = boardString.substring(0, i)+whoToPlay+boardString.substring(i+1);
				String nextPlayer = whoToPlay.equals("X") ? "O" : "X";
				try {
					nextLegalStates.add(new TicBoard(nextBoardString,nextPlayer,sideLength));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return nextLegalStates;
	}
	
	@Override
	public Outcome outCome() {//optimise this by getting wins out of this
		
		boolean atLeastOnePlayerCanWin=false;
		for(List<Integer> win : wins) {
			boolean xCanWin = true;
			boolean oCanWin = true;
			boolean xHasWon = true;
			boolean oHasWon = true;
			
			for(Integer i : win) {
				char current = boardString.charAt(i);
				if(current=='.') {
					oHasWon=false;
					xHasWon=false;
				}
				else if(current=='X') {
					oHasWon=false;
					oCanWin=false;
				}
					
				else if(current=='O') {
					xCanWin=false;
					xHasWon=false;
				}
			}
			if(xHasWon) {//there must be some elegant way of doing this(instead of variables xCanWin,oCanWin)
				if(whoToPlay.charAt(0)=='X')
					return Outcome.YOUWON;
				else 
					return Outcome.OPPONENTWON;
			} else if(oHasWon) {
				if(whoToPlay.charAt(0)=='O')
					return Outcome.YOUWON;
				else 
					return Outcome.OPPONENTWON;
			}
			else if(xCanWin || oCanWin)
				atLeastOnePlayerCanWin=true;
		}
		if(atLeastOnePlayerCanWin) 
			return Outcome.PLAYING;
		else
			return Outcome.DRAW;
	}
	
	@Override
	public String toString() {
		return "TicBoard [boardString=" + boardString + ", whoToPlay=" + whoToPlay + "]";
	}

	public String display() {
		String finalString = "";
		for(int i=0;i<sideLength; i++)
			finalString = finalString+ boardString.substring(sideLength*i,sideLength*(i+1))+"\n";
		
		return finalString;
	}

	public String getWhoToPlay() {
		return whoToPlay;
	}

	public void setWhoToPlay(String whoToPlay) {
		this.whoToPlay = whoToPlay;
	}

	public int getSideLength() {
		return sideLength;
	}

	public void setSideLength(int sideLength) {
		this.sideLength = sideLength;
	}
	
	public String getBoardString() {
		return boardString;
	}
	public void setBoardString(String boardString) {
		this.boardString = boardString;
	}

}
