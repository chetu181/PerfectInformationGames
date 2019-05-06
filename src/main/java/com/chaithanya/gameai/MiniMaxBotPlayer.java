package com.chaithanya.gameai;

import java.util.ArrayList;
import java.util.List;

public class MiniMaxBotPlayer implements Player {
	private int recursionCount;
	//TODO:needs major refactoring
	@Override
	public BoardState move(BoardState s) {//TODO: add heuristic to this
		recursionCount =0;
		if(s.outCome()!=Outcome.PLAYING)
			return null;
		
		BoardState drawState = null;
		for(BoardState nextState : s.getLegalMoves()) {
			Outcome nextStateOutcome = finalOutcome(nextState);
			if(nextStateOutcome==Outcome.OPPONENTWON) {
				System.out.println("minimax: me winning inevitable");
				System.out.println("minimax recursion count: "+recursionCount);
				return nextState;//Return if any move will definitely win
			}
			else if(nextStateOutcome==Outcome.DRAW)
				drawState = nextState;
		}
		System.out.println("minimax recursion count: "+recursionCount);
		if(drawState!=null)
			return drawState;//Return move that will draw
		return s.getLegalMoves().get(0);//You'll lose it :( . return some random move.
	}

	private Outcome finalOutcome(BoardState state) {
		//TODO: optimise removeSpurious states
		recursionCount++;
		if(state.outCome()!=Outcome.PLAYING) {
			return state.outCome();
		}
		boolean canDraw = false;
		List<BoardState> nextLegalStates = state.getLegalMoves();
		//List<BoardState> reducedNextStates = removeTicTacToeSpuriousStates(nextLegalStates);
		for(BoardState nextState : nextLegalStates) {
			Outcome nextStateOutcome = finalOutcome(nextState);
			if(nextStateOutcome==Outcome.OPPONENTWON)
				return Outcome.YOUWON;
			else if(nextStateOutcome==Outcome.DRAW)
				canDraw = true;
		}
		if(canDraw)
			return Outcome.DRAW;
		return Outcome.OPPONENTWON;
	}
	/**
	 * Removes symmetric board configurations and only retains qualitatively distinct board configs.
	 * Eg:
	 * X..
	 * ...
	 * ...
	 * 
	 * and 
	 * 
	 * ...
	 * ...
	 * ..X
	 * 
	 * are symmetric.
	 * 
	 * This is where the Idea for Value Network might have come from
	 * 
	 * @param reducedNextStates
	 * @return
	 */
	public List<BoardState> removeTicTacToeSpuriousStates(List<BoardState> boardStates) {
		//TODO: this and it's submethods are horribly inefficient, needs to be optimised
		List<BoardState> reducedList = new ArrayList<>();
		for(int i=0;i<boardStates.size();i++) {
			List<String> syms = symmetricTicTacToeStates(
					((TicBoard)boardStates.get(i)).getBoardString()
					,((TicBoard)boardStates.get(i)).getSideLength()
					);
			for(int j=i+1;j<boardStates.size();j++) {
				for(String sym: syms) {
					if(sym.equals(((TicBoard)boardStates.get(j)).getBoardString())) {
						boardStates.remove(j);
						break;
					}
				}
			}
		}
		return boardStates;
	}

	private List<String> symmetricTicTacToeStates(String original,int sideLength) {
		List<String> result = new ArrayList<>();
		String temp = new String(original);
		
		result.add(temp);
		result.add(flip(temp,sideLength));
		for(int i=0;i<3;i++) {
			temp = rotate90(temp,sideLength);
			result.add(temp);
			result.add(flip(temp,sideLength));
		}
		result.add(original);
		
		String rowReverse = "";
		String columnReverse = "";
		for(int i=0;i<sideLength;i++) {
			
		}
		
		return result;
	}

	private String rotate90(String temp,int sideLength) {
		char[] rotated = new char[temp.length()];
		for(int i=0;i<rotated.length;i++){
			int x = i/sideLength;
			int y = i%sideLength;
			rotated[i]=temp.charAt(y*sideLength+(sideLength-x-1));
		}
		return new String(rotated);
	}

	private String flip(String temp,int sideLength) {
		char[] flipped = new char[temp.length()];
		for(int i=0;i<flipped.length;i++){
			int x= i/sideLength;
			int y = i%sideLength;
			flipped[i]=temp.charAt((sideLength-x-1)*sideLength+y);
		}
		return new String(flipped);
	}
}
