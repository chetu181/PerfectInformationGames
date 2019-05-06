package com.chaithanya.gameai;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class SimplePerfectInformationGame {
	
	public static void main(String[] args) {
		List<SimplePlayer> players = new ArrayList<>();
		players.add(new SimplePlayer(PlayerType.COMPUTER,"bleep"));
		players.add(new SimplePlayer(PlayerType.HUMAN,"chetu"));
		
		GameState currentGameState = new GameState(18, players,0);//First player to play
		while(currentGameState.getToPlay()!=-1) {
			try {
				currentGameState.advanceByOneStep();
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentGameState.show();
		}
		SimplePlayer.scanner.close();
	}

}
class GameState{
	int gameNum;
	List<SimplePlayer> players;
	int toPlay = 0;
	
	public GameState(int gameNum,List<SimplePlayer> players,int toPlay) {
		this.gameNum=gameNum;
		this.players= players;
		this.toPlay= toPlay;
	}

	public void show() {
		System.out.println("GameNum is now "+gameNum);
	}

	public void advanceByOneStep() throws Exception{
		int move = players.get(toPlay).getMove(this);
		
		//validation
		if(gameNum-move<0)
			throw new ExecutionException("Not a valid move:"+move, null);
		
		System.out.println(players.get(toPlay).getPlayerName()+" played "+move);
		gameNum -= move;
		if(gameNum==0) {
			System.out.println("\nGAME OVER!! "+players.get(toPlay).getPlayerName() +" Wins!");
			toPlay=-1;
		}
			
		else {
			toPlay = 1-toPlay;
		}
	}

	public int getGameNum() {
		return gameNum;
	}

	public void setGameNum(int gameNum) {
		this.gameNum = gameNum;
	}

	public int getToPlay() {
		return toPlay;
	}

	public void setToPlay(int toPlay) {
		this.toPlay = toPlay;
	}
}
class SimplePlayer {
	PlayerType playertype;
	String playerName;
	public static Scanner scanner = new Scanner(System.in);
	SimplePlayer(PlayerType playertype,String playerName) {
		this.playertype = playertype;
		this.playerName = playerName;
	}
	public String getPlayerName() {
		return this.playerName;
	}
	int getMove(GameState g) {//TODO: use the gameState to validate the move.(should it be done here?)
		if(playertype == PlayerType.HUMAN) {
			System.out.println("Enter any number from 1 to 4");
			boolean validInput = false;
			int gameMove=1;
			gameMove = Integer.parseInt(scanner.next());
			
			return gameMove;
		}
		//Here is all the AI.
		else if(playertype == PlayerType.COMPUTER){
			int num = g.getGameNum();
			if(num%4==0)
				return 1;
			else {
				return num%4;
			}
		}
		else return 1;
	}
}
enum PlayerType{
	COMPUTER,HUMAN
}
