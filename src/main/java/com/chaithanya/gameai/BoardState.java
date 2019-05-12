package com.chaithanya.gameai;

import java.util.List;

public interface BoardState {
	public List<BoardState> getLegalMoves();
	public Outcome outCome();
	/**
	 * @return returns the approximate value of state. 0 if draw. >0 if winning <0 if losing
	 */
	public int stateScore();
}
