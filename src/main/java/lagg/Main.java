package lagg;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import lagg.Command;
import lagg.games.*;


@Mod(modid = Main.MODID, version = Main.VERSION, name = Main.NAME, acceptedMinecraftVersions = "[1.8.9]", useMetadata = true)
public class Main
{
    public static final String MODID = "SynexGuildMod";
    public static final String VERSION = "Beta 1.1";
    public static final String NAME = "SynexGuildMod";
    public static final UUID OWNER = UUID.fromString("6f609581-bbfc-4d43-8771-25f95a9922f9");
    
    public static Game currentGame;
    public static ArrayList<Game> activeGames = new ArrayList<Game>();
    public static ArrayList<String> namesInTab = new ArrayList<String>();
    public static ArrayList<String> scoreboard = new ArrayList<String>();
    public static boolean confirmedmember = true;
    public static boolean confirmedbaddude = false;
    public static boolean extraWarlordsInfo = false;
    
    public static void setCurrentGame(Game game) {
    	currentGame = game;
    }
    public static Game getCurrentGame() {
    	return currentGame;
    }
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) throws Exception {
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception
    {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new Command());
        
    }

    @SubscribeEvent
    public void login(WorldEvent.Load event) {
    	new Thread() {
    		public void run() {
    			try {
    				Thread.sleep(2850);
    				String server = Main.getServer();
    		    	for(int i = 0; i<activeGames.size(); i++) {
    		    		if(activeGames.get(i).server.equals(server)) {
    		    			setCurrentGame(activeGames.get(i));
    		    			return;
    		    		}
    		    	}
    		    	setCurrentGame(createProperGame(server));
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}.start();
    }
    
    @SubscribeEvent
    public void chat(ClientChatReceivedEvent event) throws Exception {
    	String message = event.message.getFormattedText();
    	if(!AutoTip.beforeandaftertipping && System.currentTimeMillis()-AutoTip.lastWave>3600000) {
			AutoTip.wave();
			AutoTip.lastWave = System.currentTimeMillis();
		}
    	if(!confirmedmember) {
    		UUID user = Minecraft.getMinecraft().thePlayer.getUniqueID();
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
        	    					Minecraft.getMinecraft().thePlayer.sendChatMessage("/p " + officers[j]);
        	    					Thread.sleep(80);
        	    				}
        	    				Minecraft.getMinecraft().thePlayer.sendChatMessage("/p disband");
        	    				Thread.sleep(80);
        	    			} catch (Exception e) {
        	    				e.printStackTrace();
        	    			}
        	    		}
        	    		Minecraft.getMinecraft().shutdown();
            		}
            	}.start();
            }
    	}
    	if(confirmedbaddude) {
    		event.setCanceled(true);
    	}
    	if(getCurrentGame()!=null) {
	    	Main.currentGame.addCoins(message);
	    	if(getCurrentGame() instanceof WarlordsGame) {
	    		WarlordsGame game = (WarlordsGame)(getCurrentGame());
	    		game.countAttack(message);
	    	}
	    	if(getCurrentGame() instanceof MegaWallsGame) {
	    		MegaWallsGame game = (MegaWallsGame)(getCurrentGame());
	    		game.addKills(message);
	    	}
    	}
    }
    
    @SubscribeEvent
    public void logout(WorldEvent.Unload event) {
    	try {
    		BufferedWriter out = new BufferedWriter(new FileWriter("gmod settings.txt"));
    		out.write("under development");
    		out.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @SubscribeEvent
    public void render(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.type != ElementType.EXPERIENCE) {
            return;
        }
    	FontRenderer fRender = Minecraft.getMinecraft().fontRendererObj;
    	int width = (new ScaledResolution(Minecraft.getMinecraft())).getScaledWidth();
    	int height = (new ScaledResolution(Minecraft.getMinecraft())).getScaledHeight();
    	fRender.drawString("test", width-fRender.getStringWidth("test"), 5,0);
    	ResourceLocation logo;
    	logo = new ResourceLocation("SynexGuildMod","textures/gui/synexlogo.png");
        Minecraft.getMinecraft().renderEngine.bindTexture(logo);
    	Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(5, 5, 5, 5, 200, 200);
    	GL11.glScalef(2.0f, 2.0f, 2.0f);
    	fRender.drawString(EnumChatFormatting.BOLD.toString() + EnumChatFormatting.func_175744_a(15732752) + "SynexGMod by Lagg", (width-fRender.getStringWidth("SynexGMod by Lagg")*2)/2, (height-fRender.FONT_HEIGHT*2)/2, 0);
    	GL11.glScalef(0.5f, 0.5f, 0.5f);
    	Game game = getCurrentGame();
    	if(game!=null) {
    		String coins = Integer.toString(game.coins) + " coins";
    		int coinswidth = fRender.getStringWidth(coins);
    		fRender.drawString(EnumChatFormatting.GOLD + coins, width-coinswidth, 5, 0);
    	}
        if(game instanceof WarlordsGame) {
        	WarlordsGame wlgame = (WarlordsGame)(game);
    		wlgame.renderGUI(fRender);
        }
        if(game instanceof MegaWallsGame) {
        	MegaWallsGame mwgame = (MegaWallsGame)(game);
        	mwgame.renderGUI(fRender);
        }

    }
    
    public static Game createProperGame(String server) {
    	Games unknowngame = Games.valueOf(getGame());
    	switch (unknowngame) {
    	case WARLORDS:
    		return new WarlordsGame(server);
    	case MEGA_WALLS:
    		return new MegaWallsGame(server);
    	case SKYWARS:
    		return new SkywarsGame(server);
    	case ARCADE_GAMES:
    		return new ArcadeGame(server);
    	case THE_TNT_GAMES:
    		return new TNTGame(server);
    	case SKYCLASH:
    		return new SkyclashGame(server);
    	case BLITZ_SG:
    		return new BlitzGame(server);
    	case UHC_CHAMPIONS:
    		return new UHCGame(server);
    	case COPS_AND_CRIMS:
    		return new CopsAndCrimsGame(server);
    	case CRAZY_WALLS:
    		return new CrazyWallsGame(server);
    	case SPEED_UHC:
    		return new SpeedUHCGame(server);
    	case SMASH_HEROES:
    		return new SmashHeroesGame(server);
    	case PROTOTYPE:
    		return new PrototypeGame(server);
    	case PAINTBALL:
    		return new PaintballGame(server);
    	case VAMPIREZ:
    		return new VampireZGame(server);
    	case TURBO_KART_RACERS:
    		return new TKRGame(server);
    	case THE_WALLS:
    		return new Walls2Game(server);
    	case ARENA_BRAWL:
    		return new ArenaBrawlGame(server);
    	case QUAKECRAFT:
    		return new QuakeCraftGame(server);
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
		String nameformatted = Minecraft.getMinecraft().thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
		String nameunformatted = nameformatted.replaceAll("\u00A7.{1}", "").replaceAll("\\s+", " ").replaceAll(" ", "_");
		return nameunformatted;
	}
    
	public static String getServer() {
		return null;
	}
    
	public static UUID getUserUUID() {
		return Minecraft.getMinecraft().thePlayer.getUniqueID();
	}
	
}
