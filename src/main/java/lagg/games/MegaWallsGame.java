package lagg.games;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lagg.Game;
import lagg.Main;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;

public class MegaWallsGame extends Game {
	String chosenclass;
	boolean haveWallsFallen;
	long startTime;
	int kills;
	int assists;
	int finalkills;
	int finalassists;
	
	public MegaWallsGame(String server) {
		super(server);
		
	}
	
	public void renderGUI(FontRenderer fRender) {
        fRender.drawString(EnumChatFormatting.RED + "Kills: " + EnumChatFormatting.WHITE + kills, 5, 5, 0);
        fRender.drawString(EnumChatFormatting.GREEN + "Assists " + EnumChatFormatting.WHITE + assists, 5, 15, 0);
        fRender.drawString(EnumChatFormatting.DARK_RED + "Final Kills: " + EnumChatFormatting.WHITE + finalkills, 5, 25, 0);
        fRender.drawString(EnumChatFormatting.DARK_GREEN + "Final Assists: " + EnumChatFormatting.WHITE + finalassists, 5, 35, 0);
	}
	
	public boolean haveWallsFallen() {
		ArrayList<String> board = Main.getScoreboard();
		for(int i = 0; i<board.size(); i++) {
			if(board.get(i).contains("\u00A7fGame End:")) {
				return true;
			}
		}
		//&fWalls Fall: &axx:xx
		//&fGame End: &axx:xx
		return false;
	}
	
	public void addKills(String message) {
		Matcher m = Pattern.compile("(Kills|Assists|Final Kills|Final Assists)").matcher(message); //TODO
		if(m.find()) {
			if(m.group(2).equals("Kills")) {
				this.kills++;
			} else if (m.group(2).equals("Assists")){
				this.assists++;
			} else if (m.group(2).equals("Final Kills")) {
				this.finalkills++;
			} else {
				this.finalassists++;
			}
		}
	}
	
	public int getSecondsUntilWallsFall() {
		ArrayList<String> board = Main.getScoreboard();
		for(int i = 0; i<board.size(); i++) {
			Matcher m = Pattern.compile("\u00A7fWalls Fall:.*\u00A7a(\\d+):(\\d+)").matcher(board.get(i));
			if(m.find()) {
				int seconds = Integer.parseInt(m.group(1))*60 + Integer.parseInt(m.group(2));
				return seconds;
			}
		}
		return -1;
	}
	
	public int getSecondsSinceStart() {
		ArrayList<String> board = Main.getScoreboard();
		for(int i = 0; i<board.size(); i++) {
			Matcher m = Pattern.compile("\u00A7fGame End:.*\u00A7a(\\d+):(\\d+)").matcher(board.get(i));
			if(m.find()) {
				int seconds = Integer.parseInt(m.group(1))*60 + Integer.parseInt(m.group(2));
				return 50 * 60 - seconds + 600;
			}
			
			Matcher m1 = Pattern.compile("\u00A7fWalls Fall:.*\u00A7a(\\d+):(\\d+)").matcher(board.get(i));
			if(m1.find()) {
				int seconds = Integer.parseInt(m.group(1))*60 + Integer.parseInt(m.group(2));
				return 600 - seconds;
			}
		}
		return -1;
	}
	
	
}