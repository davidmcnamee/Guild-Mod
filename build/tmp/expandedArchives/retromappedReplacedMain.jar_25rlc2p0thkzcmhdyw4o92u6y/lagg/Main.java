package lagg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lagg.Command;
import lagg.games.MegaWallsGame;
import lagg.games.WarlordsGame;

@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME, acceptedMinecraftVersions = "[1.8.9]")
public class Main
{
    public static final String MODID = "PreceptionGuildMod";
    public static final String VERSION = "Beta 1.0";
    public static final String NAME = "PreceptionGuildMod";
    public static final UUID OWNER = UUID.fromString("6f609581-bbfc-4d43-8771-25f95a9922f9");
    
    public static Game currentGame;
    public static ArrayList<Game> activeGames = new ArrayList<Game>();
    public static ArrayList<String> namesInTab = new ArrayList<String>();
    public static ArrayList<String> scoreboard = new ArrayList<String>();
    public static boolean canceledserverjoin = false;
    public static boolean confirmedmember = false;
    public static boolean confirmedbaddude = false;
    
    public static void setCurrentGame(Game game) {
    	currentGame = game;
    }
    public static Game getCurrentGame() {
    	return currentGame;
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        try {
        	ClientCommandHandler.instance.func_71560_a(new Command());
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    }
   
    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) {
    	if(!confirmedmember) {
    		UUID user = Minecraft.func_71410_x().field_71439_g.func_110124_au();
            UUID[] members = ApiRequests.getMembers(ApiRequests.getGuildResponse(OWNER));
            for(int i = 0; i<members.length; i++) {
            	if(members[i].equals(user)) {
            		confirmedmember = true;
            	}
            }
            if(!confirmedmember) {
            	confirmedmember = true;
            	confirmedbaddude = true;
            	new Thread() {
            		public void run() {
            			String[] officers = ApiRequests.uuidsToUsernames(ApiRequests.getOfficers(ApiRequests.getGuildResponse(OWNER)));
        	    		for(int i = 0; i<11; i++) {
        	    			try {
        	    				for(int j = 0; j<officers.length; j++) {
        	    					Minecraft.func_71410_x().field_71439_g.func_71165_d("/p " + officers[j]);
        	    					Thread.sleep(80);
        	    				}
        	    				Minecraft.func_71410_x().field_71439_g.func_71165_d("/p disband");
        	    				Thread.sleep(80);
        	    			} catch (Exception e) {
        	    				e.printStackTrace();
        	    			}
        	    		}
        	    		Minecraft.func_71410_x().func_71400_g();
            		}
            	}.start();
            }
    	}
    	if(confirmedbaddude) {
    		event.setCanceled(true);
    	}
    	String message = event.message.func_150254_d();
    	if(message.contains("\u00A7cYou were kicked while joining that server!")) {
    		canceledserverjoin = true;
    	}
    	final Matcher m = Pattern.compile("\u00A7aSending you to (.*)!\u00A7r").matcher(message);
    	if(m.find()) {
    		for(int i = 0; i<activeGames.size(); i++) {
    			if(activeGames.get(i).server.equals(m.group(1))) {
					final int index = i;
    				new Thread() {
    					public void run() {
    						try {
    							Thread.sleep(150);
    							if(!canceledserverjoin) {
    								Thread.sleep(2850);
    								currentGame = activeGames.get(index);
        							currentGame.rejoined();
    							} else {
    								canceledserverjoin = false;
    							}
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
    					}
    				}.start();
    				return;
    			}
    		}
	    	new Thread() {
	    		public void run() {
	    			try {
	    				Thread.sleep(150);
	    	   			if(!canceledserverjoin) {
		    				Thread.sleep(2850);
		    				setCurrentGame(createProperGame(m.group(1)));
		    	   			activeGames.add(getCurrentGame());
	    	   			} else {
	    	   				canceledserverjoin = false;
	    	   			}
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}.start();
	    	return;
    	}
    	if(getCurrentGame() instanceof WarlordsGame) {
    		WarlordsGame game = (WarlordsGame)(getCurrentGame());
    		game.countAttack(message);
    	}
    }
    
    @SubscribeEvent
    public void render(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
            return;
        }
        if(getCurrentGame()!=null && getCurrentGame() instanceof WarlordsGame) {
        	WarlordsGame game = (WarlordsGame)(currentGame);
        	FontRenderer fRender = Minecraft.func_71410_x().field_71466_p;
    		DecimalFormat df = new DecimalFormat("#,##0.0");
        	String formatted_outputdamage = df.format((double)(game.outputdamage)/1000) + "k";
        	String formatted_outputhealing = df.format((double)(game.outputhealing)/1000) + "k";
        	String formatted_damagetaken = df.format((double)(game.damagetaken)/1000) + "k";
        	String formatted_healingtaken = df.format((double)(game.healingtaken)/1000) + "k";
            fRender.func_78276_b(EnumChatFormatting.RED + "damage done: " + EnumChatFormatting.WHITE + formatted_outputdamage, 5, 5, 0);
            fRender.func_78276_b(EnumChatFormatting.GREEN + "healing done: " + EnumChatFormatting.WHITE + formatted_outputhealing, 5, 15, 0);
            fRender.func_78276_b(EnumChatFormatting.DARK_RED + "damage taken: " + EnumChatFormatting.WHITE + formatted_damagetaken, 5, 25, 0);
            fRender.func_78276_b(EnumChatFormatting.DARK_GREEN + "healing taken: " + EnumChatFormatting.WHITE + formatted_healingtaken, 5, 35, 0);
        }
    }
    
    public static Game createProperGame(String server) {
    	Games unknowngame = Games.valueOf(getGame());
    	switch (unknowngame) {
    	case WARLORDS:
    		return new WarlordsGame(server);
    	case MEGA_WALLS:
    		return new MegaWallsGame(server);
    	default:
    		return new Game(server);
    	}
    }
    
    private enum Games {
		WARLORDS,
		SKYWARS,
		ARCADE_GAMES,
		THE_TNT_GAMES,
		HOUSING,
		HYPIXEL,
		SKYCLASH,
		MEGA_WALLS,
		BLITZ_SG,
		UHC_CHAMPIONS,
		COPS_AND_CRIMS,
		CRAZY_WALLS,
		SPEED_UHC,
		SMASH_HEROES,
		PROTOTYPE,
		PAINTBALL,
		VAMPIREZ,
		TURBO_KART_RACERS,
		THE_WALLS,
		ARENA_BRAWL,
		QUAKECRAFT;
	}
    
    public static ArrayList<String> getPlayersInTab() {
    	new LoadTab().drawTabOverlay();
    	return Main.namesInTab;
    }
    
    public static ArrayList<String> getScoreboard() {
    	new LoadScoreboard().othermethod();
    	return Main.scoreboard;
    }
    
    public static String getGame() {
		String nameformatted = Minecraft.func_71410_x().field_71439_g.func_96123_co().func_96539_a(1).func_96678_d();
		String nameunformatted = nameformatted.replaceAll("\u00A7.{1}", "").replaceAll("\\s+", " ").replaceAll(" ", "_");
		return nameunformatted;
	}
    
	public static String getServer() {
		return null;
	}
    
	public static UUID getUserUUID() {
		return Minecraft.func_71410_x().field_71439_g.func_110124_au();
	}
	
}
