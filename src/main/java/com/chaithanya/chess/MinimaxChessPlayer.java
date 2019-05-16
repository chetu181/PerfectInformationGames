package com.chaithanya.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;

import com.chaithanya.gameai.BoardState;
import com.chaithanya.gameai.Player;

public class MinimaxChessPlayer implements Player {
	int maxDepth;
	boolean randomise=false;
	boolean bruteForce =false;
	boolean orderHeuristic = true;
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
			int currentScore=2000;
			int currentScorePruned;
			currentScorePruned = heuristicWithPruning(nextMoves.get(i),1,minNextStateScore) ;
			if(bruteForce)
				currentScore = heuristic(nextMoves.get(i),1) ;
			if(bruteForce && currentScorePruned!=currentScore && currentScore<minNextStateScore) {
				System.err.println("Alpha-beta pruning is wrong for state:");
				System.err.println(nextMoves.get(i));
				System.err.println(((ChessBoard)(nextMoves.get(i))).getBoardString());
			}
			currentScore=currentScorePruned;
			if(currentScore < minNextStateScore) {
				minNextStateScore= currentScore;
				minNextStateScoreIndex=i;
				minValues.clear();
				minValues.add(minNextStateScoreIndex);
			} else if (currentScore==minNextStateScore) {
				minValues.add(i);
				minNextStateScoreIndex=i;
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Finished Searching in "+(endTime-startTime)/1000.0+" Seconds.");
		if(randomise)
			return nextMoves.get(minValues.get(RandomUtils.nextInt(0, minValues.size())));
		else
			return nextMoves.get(minNextStateScoreIndex);
	}

	private int heuristicWithPruning(BoardState boardState,int depth,int minOfParent) {
		if(depth==maxDepth)
			return boardState.stateScore();
		List<BoardState> nextMoves = boardState.getLegalMoves();
		if(orderHeuristic) {
			Collections.sort(nextMoves);
		}
		
		if(nextMoves.isEmpty())
			return -1000;
		int minNextStateScore = 2000;
		for(int i=0;i<nextMoves.size();i++) {
			int currentScore = heuristicWithPruning(nextMoves.get(i),depth+1,minNextStateScore) ;
			if((-currentScore) > minOfParent )//TODO: can this be done >=?
				return -currentScore;//Pruning is happening here
			if(currentScore < minNextStateScore) { 
				minNextStateScore= currentScore;
			}
		}
		return -minNextStateScore;
	}
	private int heuristic(BoardState boardState,int depth) {
		if(depth==maxDepth)
			return boardState.stateScore();
		List<BoardState> nextMoves = boardState.getLegalMoves();
		if(nextMoves.isEmpty())
			return -1000;
		int minNextStateScore = 2000;
		for(int i=0;i<nextMoves.size();i++) {
			int currentScore = heuristic(nextMoves.get(i),depth+1) ;
			if(currentScore < minNextStateScore) { 
				minNextStateScore= currentScore;
			}
		}
		return -minNextStateScore;
	}
	public static void main(String args[]) {
		System.out.println("Testing alpha - beta");
		char[][] boardString = new char[8][8];
		String board = 
				"R..KQ.NRPPP.PPBP..NP..P................np.p...pB.p.ppp.prnbkqb.r\n" + 
				"" ;
		for(int i=0;i<8;i++) {
			boardString[i]=board.substring(8*i, 8*i+8).toCharArray();
		}							
		ChessBoard chessBoard = new ChessBoard(boardString, true, false, false);
		System.out.println(chessBoard);
		long startTime = System.currentTimeMillis();
		System.out.println(new MinimaxChessPlayer(4).move(chessBoard));
		long endTime = System.currentTimeMillis();
		System.out.println("Total time taken: "+(endTime-startTime)/1000.0+" seconds");
		
	}
}
