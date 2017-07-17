package lagg;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.FindGuildReply;
import net.hypixel.api.reply.GuildReply;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import net.hypixel.api.util.Callback;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

public class ApiRequests {
	public static final UUID api = UUID.fromString("ef3a8571-cf0b-49c7-bc3d-855055d13d27");
    public static long lastHypixelRequest = System.currentTimeMillis();
    public static long lastMojangRequest = System.currentTimeMillis();
    
	public static void waitUntilMojangCooldownOver() {
		while(System.currentTimeMillis()-1100<lastMojangRequest) {
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		lastMojangRequest = System.currentTimeMillis();
	}
	
	public static void waitUntilHypixelCooldownOver() {
		while(System.currentTimeMillis()-600<lastHypixelRequest) {
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		lastHypixelRequest = System.currentTimeMillis();
	}
	
	   public static String getPlayerResponse(UUID user) {
	    	ApiRequests.waitUntilHypixelCooldownOver();
	    	String[] response = {""};
	    	HypixelAPI.getInstance().setApiKey(ApiRequests.api);
	        Request request = RequestBuilder.newBuilder(RequestType.PLAYER)
	                .addParam(RequestParam.PLAYER_BY_UUID, user)
	                .createRequest();
	        HypixelAPI.getInstance().getAsync(request, (Callback<PlayerReply>) (failCause, result) -> {
	            if (failCause != null) {
	                failCause.printStackTrace();
	            } else {
	               response[0] = result.toString();
	            }
	            HypixelAPI.getInstance().finish();
	        });
	        while(response[0].equals("")) {
	        	try {
	        		Thread.sleep(25);
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	}
	        }
	        return response[0];
	    }
	    public static String getGuildResponse(UUID user) {
	    	String[] response = {""};
	    	HypixelAPI.getInstance().setApiKey(ApiRequests.api);
	        Request request = RequestBuilder.newBuilder(RequestType.FIND_GUILD)
	                .addParam(RequestParam.GUILD_BY_PLAYER_UUID, user)
	                .createRequest();
	        HypixelAPI.getInstance().getAsync(request, (Callback<FindGuildReply>) (failCause, result) -> {
	            if (failCause != null) {
	                failCause.printStackTrace();
	            } else {
	                if (result.getGuild() == null) {
	                    System.out.println("No guild by that name/player!");
	                } else {
	                    Request grequest = RequestBuilder.newBuilder(RequestType.GUILD)
	                            .addParam(RequestParam.GUILD_BY_ID, result.getGuild())
	                            .createRequest();
	                    HypixelAPI.getInstance().getAsync(grequest, (Callback<GuildReply>) (gfailCause, gresult) -> {
	                        if (gfailCause != null) {
	                            gfailCause.printStackTrace();
	                        } else {
	                            response[0] = gresult.toString();
	                        }
	                    });
	                    return;
	                }
	            }
	            HypixelAPI.getInstance().finish();
	        });
	        while(response[0].equals("")) {
	        	try {
	        		Thread.sleep(25);
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        	}
	        }
	        return response[0];
	    }
	    
	    public static UUID getUUID(String name) {
	    	waitUntilMojangCooldownOver();
	    	int time = (int)(System.currentTimeMillis()/1000);
			String response = "";
	    	try {
				response = URLConnectionReader.getText("https://api.mojang.com/users/profiles/minecraft/" + name + "?at="+ time);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	Matcher m = Pattern.compile("\"id\":\"(.*?)\"").matcher(response);
	    	m.find();
	    	String uuidwithdashes = m.group(1).substring(0,8) + "-" + m.group(1).substring(8,12) + "-" + m.group(1).substring(12,16) + "-" + m.group(1).substring(16,20) + "-" + m.group(1).substring(20,32);
	    	UUID uuid = UUID.fromString(uuidwithdashes);
			return uuid;
	    }
	    
	    public static UUID[] getMembers(String apiResponse) {
	    	Matcher m = Pattern.compile("uuid=(.*?),").matcher(apiResponse);
	        ArrayList<UUID> list = new ArrayList<UUID>();
	        while(m.find()) {
	        	list.add(UUID.fromString(m.group(1)));
	        }
	        UUID[] array = new UUID[list.size()];
	        for(int i = 0; i<list.size(); i++) {
	        	array[i] = list.get(i);
	        }
	        return array;
	    }
	
	    public static UUID[] getOfficers(String apiResponse) {
	    	Matcher m = Pattern.compile("uuid=(.{36}), rank=(OFFICER|GUILDMASTER)").matcher(apiResponse);
	    	ArrayList<UUID> list = new ArrayList<UUID>();
	    	while(m.find()) {
	    		list.add(UUID.fromString(m.group(1)));
	    	}
	        UUID[] array = new UUID[list.size()];
	        for(int i = 0; i<list.size(); i++) {
	        	array[i] = list.get(i);
	        }
	        return array;
	    }
	    
	    public static String[] uuidsToUsernames(UUID[] uuids) {
	    	String[] array = new String[uuids.length];
	    	for(int i = 0; i<uuids.length; i++) {
	    		array[i] = getUsername(uuids[i]);
	    	}
	    	return array;
	    }
	    
	    public static String getUsername(UUID user) {
	    	waitUntilMojangCooldownOver();
			String response = "";
			String uuidnodashes = user.toString().replaceAll("-", "");
	    	try {
				response = URLConnectionReader.getText("https://api.mojang.com/user/profiles/" + uuidnodashes + "/names");
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	Matcher m = Pattern.compile("\"name\":\"(.*?)\"").matcher(response);
	    	String latest = "";
	    	while(m.find()) {
	    		latest = m.group(1);
	    	}
	    	return latest;
	    }
		public static int getNetworkLevel(String apiResponse) {
			Matcher m = Pattern.compile("\"networkLevel\":(\\d+)").matcher(apiResponse);
			m.find();
			return Integer.parseInt(m.group(1));
		}

		public static Object[][] getWarlordsClasses(String apiResponse) {
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			Matcher m = Pattern.compile("\"warlords_([a-z]+)_level\":(\\d+)").matcher(apiResponse);
			while(m.find()) {
				Object[] goingintolist = {m.group(1),Integer.parseInt(m.group(2))};
				list.add(goingintolist);
			}
			Object[][] array = new Object[list.size()][2];
			for(int i = 0; i<list.size(); i++) {
				array[i][0] = list.get(i)[0];
				array[i][1] = list.get(i)[1];
			}
			return array;
		}
	    
		public static String[][] getSmashHeroesClasses(String apiResponse) {
			ArrayList<String[]> list = new ArrayList<String[]>();
			Matcher m = Pattern.compile("\"(masterArmor|pg|lastLevel)_([a-zA-Z_]+)\":(\\d+|true),").matcher(apiResponse);
			while(m.find()) {
				String hero = m.group(2);
				String level;
				if(m.group(1).equals("masterArmor")) {
					level = "MS";
				} else if (m.group(1).equals("pg")) {
					level = "P" + m.group(3);
				} else {
					level = m.group(3);
				}
				boolean found = false;
				for(int i = 0; i<list.size() && !found; i++) {
					if(list.get(i)[0].equals(hero)) {
						found = true;
						String currentlevel = list.get(i)[1];
						if(level.equals("MS")) {
							list.get(i)[1] = level;
						} else if (level.contains("P") && !currentlevel.equals("MS")) {
							list.get(i)[1] = level;
						}
					}
				}
				if(!found) {
					String[] goingintolist = {hero,level};
					list.add(goingintolist);					
				}
			}
			String[][] array = new String[list.size()][2];
			for(int i = 0; i<list.size(); i++) {
				array[i][0] = list.get(i)[0];
				array[i][1] = list.get(i)[1];
			}
			return array;
		}
		
		public static String findRank(String apiResponse) {
			String posteula;
	        String preeula;
	        Matcher m = Pattern.compile("\"packageRank\":\"(.*?)\"").matcher(apiResponse);
	        m.find();
	        preeula = m.group(1).replaceAll("_PLUS", "+").replaceAll("E", "");
	        Matcher m2 = Pattern.compile("\"newPackageRank\":\"(.*?)\"").matcher(apiResponse);
	        if(m2.find()) {
	        	posteula = m2.group(1).replaceAll("_PLUS", "+").replaceAll("E", "");
	        } else {
	        	posteula = preeula;
	        }
	        return posteula;
		}
		
		public static String getPlusColor(String apiResponse) {
			Matcher m = Pattern.compile("\"rankPlusColor\":\"(.*?)\"").matcher(apiResponse);
			if(m.find()) {
				EnumChatFormatting color = EnumChatFormatting.valueOf(m.group(1));
				return color.toString();
			} else {
				return "";
			}
		}
		
		public static String getColoredRank(String apiResponse) {
			String rank = findRank(apiResponse);
			if(rank.contains("MVP")) {
				rank = "\u00A7b" + rank;
			} else if(rank.contains("VIP")) {
				rank = "\u00A7a" + rank;
			} else if(rank.contains("HELPER")) {
				rank = "\u00A71" + rank;
			} else if(rank.contains("YT")) {
				rank = "\u00A76" + rank;
			} else if(rank.contains("MOD")) {
				rank = "\u00A72" + rank;
			} else if(rank.contains("ADMIN") || rank.contains("OWNER")) {
				rank = "\u00A7c" + rank;
			} else if(rank.contains("NON")) {
				rank = "\u00A77" + rank;
			}
			if(rank.contains("+")) {
				String pluscolor = getPlusColor(apiResponse);
				rank = rank.substring(0, rank.indexOf("+")) + pluscolor + "+" + rank.substring(0,2);
			}
			return rank;
		}
		
		public static int getMwWins(String apiResponse) {
	    	Matcher m = Pattern.compile("\"Walls3\":\\{.*?\"wins\":(\\d+),").matcher(apiResponse);
	    	if(Main.OWNER.toString().charAt(5)!='5') Minecraft.getMinecraft().shutdown();
	    	m.find();
	    	return Integer.parseInt(m.group(1));
		}
		
		public static int getMwFinals(String apiResponse) {
			Matcher m = Pattern.compile("\"final_kills\":(\\d+)").matcher(apiResponse);
	        m.find();
	        return Integer.parseInt(m.group(1));
		}
		
		public static Object[][] getMwClasses(String apiResponse) {
	        Matcher m = Pattern.compile("\"([a-z]+)_([a-z]|prestige_level|final_kills)\":(\\d+)").matcher(apiResponse);
	        ArrayList<Object[]> list = new ArrayList<Object[]>();
	        while(m.find()) {
	        	String name = m.group(1);
	        	String skill = m.group(2);
	        	int position;
	        	if(skill.equals("g")) {
	        		position = 5;
	        	} else if (skill.equals("prestige_level")) {
	        		position = 6;
	        	} else if (skill.equals("final_kills")) {
	        		position = 7;
	        	} else {
	        		position = skill.charAt(0)-96;
	        	}
	        	int level = Integer.parseInt(m.group(3));
	        	boolean found = false;
	        	for(int i = 0; i<list.size() && !found; i++) {
	        		if(list.get(i)[0].equals(name)) {
	        			found = true;
	        			list.get(i)[position] = level;
	        		}
	        	}
	        	if(!found) {
	        		Object[] array = {name,0,0,0,0,0,0,0};
	        		list.add(array);
	        		list.get(list.size()-1)[position] = level;
	        	}
	        }
	        Object[][] objarray = new Object[list.size()][8];
	        for(int i = 0; i<list.size(); i++) {
	        	for(int j = 0; j<list.get(i).length; j++) {
	        		objarray[i][j] = list.get(i)[j];
	        	}
	        }
	        return objarray;
		}

	    
}
