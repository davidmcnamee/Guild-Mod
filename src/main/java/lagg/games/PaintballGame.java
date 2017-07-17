package lagg.games;

import java.util.ArrayList;

import lagg.Game;

public class PaintballGame extends Game {
	ArrayList<ArrayList<Object>> killstreaks = new ArrayList<ArrayList<Object>>(); 
	
	public PaintballGame(String server) {
		super(server);
	}
	public void processKillstreakMessage(String message) {
		
	}
}
