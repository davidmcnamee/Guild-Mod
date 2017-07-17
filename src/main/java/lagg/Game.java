package lagg;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;

public class Game {
	public String server;
	public String gamemode;
	public boolean hasGameStarted;
	public long firstJoinTime;
	public long lastJoinTime;
	public ArrayList<String> players;
	public int coins = 0;
	public ArrayList<String> coinlog = new ArrayList<String>();
	
	public Game(String server) {
		this.server = server;
		this.gamemode = getGame();
		this.hasGameStarted = hasGameStarted();
		this.firstJoinTime = System.currentTimeMillis();
		this.lastJoinTime = System.currentTimeMillis();
		this.players = Main.getPlayersInTab();
	}
	
	public void addCoins(String message) {
		Matcher m = Pattern.compile("+(\\d+) coins").matcher(message);
		if(m.find() && !message.contains("for being generous")) {
			coins += Integer.parseInt(m.group(1));
			this.coinlog.add(message);
		}
	}
	
	public void rejoined() {
		this.lastJoinTime = System.currentTimeMillis();
		this.players = Main.getPlayersInTab();
		this.hasGameStarted = hasGameStarted();
	}
	
    public static String getGame() {
		String nameformatted = Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
		String nameunformatted = nameformatted.replaceAll("\u00A7.{1}", "").replaceAll("\\s+", " ").replaceAll(" ", "_");
		return nameunformatted;
	}
    
    public static boolean hasGameStarted() {
    	ArrayList<String> scoreboard = Main.getScoreboard();
    	for(int i = 0; i<scoreboard.size(); i++) {
    		if(scoreboard.get(i).contains("Waiting...")) {
    			return false;
    		}
    	}
    	return true;
    }

}



