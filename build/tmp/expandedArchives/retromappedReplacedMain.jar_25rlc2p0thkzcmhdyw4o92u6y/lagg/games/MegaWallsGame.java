package lagg.games;

import java.util.ArrayList;

import lagg.Game;
import lagg.Main;

public class MegaWallsGame extends Game {
	String server;
	String gamemode;
	String chosenclass;
	boolean haveWallsFallen;
	boolean hasGameStarted;
	long firstjoinTime;
	long lastJoinTime;
	long startTime;
	ArrayList<String> players;
	
	public MegaWallsGame(String server) {
		super(server);
		
	}
	
	public boolean haveWallsFallen() {
		return true;
	}
}