package lagg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Command extends CommandBase {
	
	@Override
	public String getCommandUsage(ICommandSender arg0) {
		return "g";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "g";
	}
	
	@Override
	public void processCommand(ICommandSender sender, final String[] arg) throws CommandException {
		if(arg.length>=1) {
			if(arg[0].equals("list")) {
				ArrayList<String> playersintab = Main.getPlayersInTab();
				String biglongstring = "";
				for(int i = 0; i<playersintab.size(); i++) {
					biglongstring += playersintab.get(i);
					biglongstring += i==playersintab.size()-1 ? "" : ",";
				}
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(biglongstring));
				System.out.println(biglongstring);
			} else if (arg[0].equals("score")) { 
				ArrayList<String> scoreboard = Main.getScoreboard();
				String biglongstring = "";
				for(int i = 0; i<scoreboard.size(); i++) {
					biglongstring += scoreboard.get(i);
					biglongstring += i==scoreboard.size()-1 ? "" : ",";
				}
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(biglongstring));
				System.out.println(biglongstring);
			} else if (arg[0].equals("lookup") && arg.length==2) {
				new Thread() {
					public void run() {
						try {
							String[] names = ApiRequests.getUsernames(ApiRequests.getUUID(arg[1]));
							for(int i = 0; i<names.length; i++) {
								Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + names[i]));
							}
						} catch (Exception e) {
							e.printStackTrace();
							Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("could not find user by that name"));
						}
					}
				}.start();
			} else {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("/lagg [list]"));
			}
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("/lagg [list]"));
		}
	}
	public static double distanceFromMe(EntityPlayer player) {
		double x = Minecraft.getMinecraft().thePlayer.posX-player.posX;
		double y = Minecraft.getMinecraft().thePlayer.posY-player.posY;
		double z = Minecraft.getMinecraft().thePlayer.posZ-player.posZ;
		double xy = Math.sqrt(x*x + y*y);
		double xyz = Math.sqrt(xy * xy + z * z);
		return xyz;
	}
}
