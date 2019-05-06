package com.chaithanya.gameai;

import java.util.List;

public interface BoardState {
	public List<BoardState> getLegalMoves();
	public Outcome outCome();
}
