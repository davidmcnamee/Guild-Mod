package lagg;

import java.util.UUID;

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
	boolean teammate;
	
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
		this.bsgKits = ApiRequests.getBlitzKits(apiResponse);
	}
	
	public void refresh() {
		this.username = ApiRequests.getUsername(this.uuid);
		String apiResponse = ApiRequests.getPlayerResponse(this.uuid);
		this.networkLevel = ApiRequests.getNetworkLevel(apiResponse);
		this.megaWallsFinals = ApiRequests.getMwFinals(apiResponse);
		this.megaWallsWins = ApiRequests.getMwWins(apiResponse);
		this.mwClasses = ApiRequests.getMwClasses(apiResponse);
		this.rank = ApiRequests.getColoredRank(apiResponse);
		this.warlordsClasses = ApiRequests.getWarlordsClasses(apiResponse);
		this.bsgKits = ApiRequests.getBlitzKits(apiResponse);
	}
	
	public String getUsername() {
		return this.username;
	}
	public UUID getUUID() {
		return this.uuid;
	}
	public int getNetworkLevel() {
		return this.networkLevel;
	}
	public String getRank() {
		return this.rank;
	}
	public int getMegaWallsWins() {
		return this.megaWallsWins;
	}
	public int getMegaWallsFinals() {
		return this.megaWallsFinals;
	}
	public Object[][] getMegaWallsClasses() {
		return this.mwClasses;
	}
	public Object[][] getWarlordsClasses() {
		return this.warlordsClasses;
	}
	public Object[][] getBsgKits() {
		return this.bsgKits;
	}
	public Object[][] getSmashHeroesClasses() {
		return this.smashHeroes;
	}
}
