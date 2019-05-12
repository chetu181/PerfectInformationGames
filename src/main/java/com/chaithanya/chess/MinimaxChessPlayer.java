package com.chaithanya.chess;

import java.util.List;

import com.chaithanya.gameai.BoardState;
import com.chaithanya.gameai.Player;

public class MinimaxChessPlayer implements Player {
	int maxDepth;
	
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
		List<BoardState> nextMoves = s.getLegalMoves();
		int minNextStateScoreIndex = 0;
		int minNextStateScore = nextMoves.get(0).stateScore();
		for(int i=0;i<nextMoves.size();i++) {
			int currentScore = nextMoves.get(i).stateScore() ;
			if(currentScore < minNextStateScore) {
				minNextStateScore= currentScore;
				minNextStateScoreIndex=i;
			}
		}
		return nextMoves.get(minNextStateScoreIndex);
	}

}
