package lagg.games;

import java.util.ArrayList;

import lagg.Game;

public class QuakeCraftGame extends Game {
	int shots;
	int kills;
	int deaths;
	long lastJump;
	
	public QuakeCraftGame(String server) {
		super(server);
	}
}
