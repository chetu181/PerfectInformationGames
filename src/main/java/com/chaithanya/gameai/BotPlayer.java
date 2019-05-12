package com.chaithanya.gameai;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

public class BotPlayer implements Player {

	@Override
	public BoardState move(BoardState s) {
		List<BoardState> legalMoves =  s.getLegalMoves();
		int index = RandomUtils.nextInt(0, legalMoves.size());
		return legalMoves.get(index);//Dumb, returns a random Move
	}

}
