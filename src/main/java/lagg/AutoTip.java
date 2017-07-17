package lagg;

import net.minecraft.client.Minecraft;
import lagg.ApiRequests;

public class AutoTip {
	public static long lastWave = 0;
	public static int index = 0;
	public static final String[] gamesList = {"warlords","skywars","arcade","tnt","skyclash","mw","bsg","uhc","cops","crazywalls","speeduhc","smash","paintball","vampirez","tkr","walls","arena","quake"};
	public static String[] members;
	public static boolean tipping = false;
	public static boolean beforeandaftertipping = false;
	
	public static void tip(String username, String game) {
		Minecraft.getMinecraft().thePlayer.sendChatMessage("/tip " + username + " " + game);
	}
	
	public static void wave() throws Exception {
		beforeandaftertipping = true;
		if(members==null) {
			members = ApiRequests.uuidsToUsernames(ApiRequests.getMembers(ApiRequests.getPlayerResponse(Main.OWNER)));
		}
		while(members==null) {
			Thread.sleep(25);
		}
		tipping = true;
		for(int i = 0; i<gamesList.length; i++,index=(index+1)%(members.length)) {
			tip(members[index],gamesList[i]);
			Thread.sleep(150);
		}
		tipping = false;
		beforeandaftertipping = false;
	}
}
