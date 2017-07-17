package lagg;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

public class LoadScoreboard {
	
	public LoadScoreboard() {
		
	}
	
	public void othermethod() {
		Main.scoreboard = new ArrayList<String>();
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(Minecraft.getMinecraft().thePlayer.getName());

        if (scoreplayerteam != null)
        {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0)
            {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

        if (scoreobjective1 != null)
        {
            this.mainscoreboardmethod(scoreobjective1, scaledresolution);
        }

	}
	
    public void mainscoreboardmethod(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_)
    {
        Scoreboard scoreboard = p_180475_1_.getScoreboard();
        Collection collection = scoreboard.getSortedScores(p_180475_1_);
        ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate()
        {
            private static final String __OBFID = "CL_00001958";
            public boolean func_178903_a(Score p_178903_1_)
            {
                return p_178903_1_.getPlayerName() != null && !p_178903_1_.getPlayerName().startsWith("#");
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.func_178903_a((Score)p_apply_1_);
            }
        }));
        ArrayList arraylist1;

        if (arraylist.size() > 15)
        {
            arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
        }
        else
        {
            arraylist1 = arraylist;
        }

        int i = this.func_175179_f().getStringWidth(p_180475_1_.getDisplayName());
        String s;

        for (Iterator iterator = arraylist1.iterator(); iterator.hasNext(); i = Math.max(i, this.func_175179_f().getStringWidth(s)))
        {
            Score score = (Score)iterator.next();
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
        }

        int i1 = arraylist1.size() * this.func_175179_f().FONT_HEIGHT;
        int j1 = p_180475_2_.getScaledHeight() / 2 + i1 / 3;
        byte b0 = 3;
        int k1 = p_180475_2_.getScaledWidth() - i - b0;
        int j = 0;
        Iterator iterator1 = arraylist1.iterator();

        while (iterator1.hasNext())
        {
            Score score1 = (Score)iterator1.next();
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int k = j1 - j * this.func_175179_f().FONT_HEIGHT;
            int l = p_180475_2_.getScaledWidth() - b0 + 2;
            //drawRect(k1 - 2, k, l, k + this.func_175179_f().FONT_HEIGHT, 1342177280);
            Main.scoreboard.add(s1);
            //this.func_175179_f().drawString(s1, k1, k, 553648127);
            //Main.scoreboard.add(s2);
            //this.func_175179_f().drawString(s2, l - this.func_175179_f().getStringWidth(s2), k, 553648127);

            if (j == arraylist1.size())
            {
                String s3 = p_180475_1_.getDisplayName();
                //drawRect(k1 - 2, k - this.func_175179_f().FONT_HEIGHT - 1, l, k - 1, 1610612736);
                //drawRect(k1 - 2, k - 1, l, k, 1342177280);
                //Main.scoreboard.add(s3);
                //this.func_175179_f().drawString(s3, k1 + i / 2 - this.func_175179_f().getStringWidth(s3) / 2, k - this.func_175179_f().FONT_HEIGHT, 553648127);
            }
        }
    }
    public FontRenderer func_175179_f()
    {
        return Minecraft.getMinecraft().fontRendererObj;
    }

}
