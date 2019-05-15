package com.chaithanya.chess;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

import com.chaithanya.gameai.BoardState;
import com.chaithanya.gameai.Player;

public class MinimaxChessPlayer implements Player {
	int maxDepth;
	boolean randomise=true;
	/**
	 * Depth is the number of moves you want to look ahead
	 * @param maxDepth
	 */
	public MinimaxChessPlayer(int maxDepth) {
		super();
		this.maxDepth = maxDepth;
	}

	
	@Override
	public BoardState move(BoardState s) {
		long startTime = System.currentTimeMillis();
		List<BoardState> nextMoves = s.getLegalMoves();
		int minNextStateScoreIndex = 0;
		int minNextStateScore = 2000;
		List<Integer> minValues  = new ArrayList<>();
		for(int i=0;i<nextMoves.size();i++) {
			int currentScore = heuristic(nextMoves.get(i),1) ;
			if(currentScore < minNextStateScore) {
				minNextStateScore= currentScore;
				minNextStateScoreIndex=i;
				minValues.clear();
				minValues.add(minNextStateScoreIndex);
			} else if (currentScore==minNextStateScore) {
				minValues.add(i);
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Finished Searching in "+(endTime-startTime)/1000.0+" Seconds.");
		if(randomise)
			return nextMoves.get(minValues.get(RandomUtils.nextInt(0, minValues.size())));
		else
			return nextMoves.get(minNextStateScoreIndex);
	}


	private int heuristic(BoardState boardState,int depth) {
		if(depth==maxDepth)
			return boardState.stateScore();
		List<BoardState> nextMoves = boardState.getLegalMoves();
		if(nextMoves.isEmpty())
			return -1000;
		int minNextStateScore = nextMoves.get(0).stateScore();
		for(int i=0;i<nextMoves.size();i++) {
			int currentScore = heuristic(nextMoves.get(i),depth+1) ;
			if(currentScore < minNextStateScore) { 
				minNextStateScore= currentScore;
			}
		}
		return -minNextStateScore;
	}

}
