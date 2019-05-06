package com.chaithanya.gameai;

public class BotPlayer implements Player {

	@Override
	public BoardState move(BoardState s) {
		return s.getLegalMoves().get(0);//Dumb, returns the first legalmove
	}

}
