package lagg.games;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lagg.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import lagg.Game;

public class WarlordsGame extends Game{
	String server;
	String gamemode;
	boolean hasGameStarted;
	long firstJoinTime;
	long lastJoinTime;
	long startTime;
	ArrayList<String> players;
	public int outputdamage = 0;
	public int outputhealing = 0;
	public int damagetaken = 0;
	public int healingtaken = 0;
	public String playerclass;
	ArrayList<String> log = new ArrayList<String>();
	
	public WarlordsGame(String server) {
		super(server);
		playerclass = getPlayerClass();
	}
	
	public static String getPlayerClass() {
		ArrayList<String> scoreboard = Main.getScoreboard();
		for(int i = 0; i<scoreboard.size(); i++) {
			Matcher m = Pattern.compile("\u00A77Lv(\\d{2})\u00A78 \u00A76(.*)").matcher(scoreboard.get(i));
			if(m.find()) {
				return m.group(2);
			}
		}
		return "";
	}

	public static String getSpec() { //NOT A SPECTATOR (e.g. pyro, cryo, aqua)
		ArrayList<String> scoreboard = Main.getScoreboard();
		for(int i = 0; i<scoreboard.size(); i++) {
			Matcher m = Pattern.compile("Spec: \u00A7a(.*)").matcher(scoreboard.get(i));
			if(m.find()) {
				return m.group(1);
			}
		}
		return "";
	}
	
	public static int getLevel() {
		ArrayList<String> scoreboard = Main.getScoreboard();
		for(int i = 0; i<scoreboard.size(); i++) {
			Matcher m = Pattern.compile("\u00A77Lv(\\d{2})\u00A78 \u00A76(.*)").matcher(scoreboard.get(i));
			if(m.find()) {
				return Integer.parseInt(m.group(1));
			}
		}
		return 0;
	}
	
	public void countAttack(String message) {
		Matcher m = Pattern.compile("\u00A7r\u00A7[a-z0-9](\u00BB|\u00AB) \u00A7r\u00A77(You|Your .*?|[a-zA-Z0-9_]+'s .*?) (critically |)(hit|healed) ([a-zA-Z0-9_]+) for \u00A7r\u00A7[a-z0-9](\u00A7l|)(\\d+)(!|)\u00A7r\u00A77 (critical |)(damage|melee damage|health).\u00A7r").matcher(message);
		if(m.find()) {
			log.add(message);
			if(m.group(1).equals("\u00BB")) {
				if(m.group(4).equals("hit")) {
					this.outputdamage+=Integer.parseInt(m.group(7));
				} else if (m.group(4).equals("healed")) {
					this.outputhealing+=Integer.parseInt(m.group(7));
				}	 else {
					Minecraft.func_71410_x().field_71439_g.func_145747_a(new ChatComponentText("error with attack count, please report this"));
				}
			} else if (m.group(1).equals("\u00AB")){
				if(m.group(4).equals("hit")) {
					this.damagetaken+=Integer.parseInt(m.group(7));
				} else if (m.group(4).equals("healed")) {
					this.healingtaken+=Integer.parseInt(m.group(7));
				} else {
					Minecraft.func_71410_x().field_71439_g.func_145747_a(new ChatComponentText("error with attack count, please report this"));
				}
			} else {
				Minecraft.func_71410_x().field_71439_g.func_145747_a(new ChatComponentText("error with attack count, please report this"));
			}
		}
	}
	public boolean isCTF() {
    	ArrayList<String> scoreboard = Main.getScoreboard();
    	for(int i = 0; i<scoreboard.size(); i++) {
    		if((this.hasGameStarted && scoreboard.get(i).contains("\u00A7cRED Flag:")) || (!this.hasGameStarted && Pattern.compile("Players: �a\\d+/24").matcher(scoreboard.get(i)).find())) {
    			return true;
    		}
    	}
    	return false;    	
    }
    
    public boolean isTDM() {
    	if(isCTF()) {
    		return false;
    	}
    	ArrayList<String> scoreboard = Main.getScoreboard();
    	for(int i = 0; i<scoreboard.size(); i++) {
    		if((this.hasGameStarted && Pattern.compile("�cRED: �b\\d+�6/1000").matcher(scoreboard.get(i)).find()) || (!this.hasGameStarted && Pattern.compile("Players: �a\\d+/28").matcher(scoreboard.get(i)).find())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public boolean isDom() {
    	if(!isCTF() && !isTDM()) {
    		return true;
    	}
    	return false;
    }
    
    public String getGameType() {
    	if(isCTF()) {
    		return "CTF";
    	} else if (isTDM()) {
    		return "TDM";
    	} else {
    		return "DOM";
    	}
    }
}
