package lagg;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class Player {
	public String username;
	public UUID uuid;
	public int networkLevel;
	public String rank;
	public int megaWallsWins;
	public int megaWallsFinals;
	public Object[][] mwClasses;
	public Object[][] warlordsClasses;
	public Object[][] bsgKits;
	public String[][] smashHeroes;
	
	public Player(String username) {
		this.username = username;
		this.uuid = ApiRequests.getUUID(username);
		String apiResponse = ApiRequests.getPlayerResponse(uuid);
		this.networkLevel = ApiRequests.getNetworkLevel(apiResponse);
		this.megaWallsFinals = ApiRequests.getMwFinals(apiResponse);
		this.megaWallsWins = ApiRequests.getMwWins(apiResponse);
		this.mwClasses = ApiRequests.getMwClasses(apiResponse);
		this.rank = ApiRequests.getColoredRank(apiResponse);
		this.warlordsClasses = ApiRequests.getWarlordsClasses(apiResponse);
		
	}
	
}
