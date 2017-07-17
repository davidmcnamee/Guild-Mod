package lagg;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LoadTab {
	private static final Ordering field_175252_a = Ordering.from(new LoadTab.PlayerComparator(null));
    private final Minecraft field_175250_f = Minecraft.func_71410_x();
    private final GuiIngame field_175251_g = Minecraft.func_71410_x().field_71456_v;
    private IChatComponent footer;
    private IChatComponent header;
    private long field_175253_j;
	public static ArrayList<String> tabplayerlist;
	public final GuiPlayerTabOverlay overlayPlayerList = new GuiPlayerTabOverlay(Minecraft.func_71410_x(),Minecraft.func_71410_x().field_71456_v);
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected float zLevel = Float.MAX_VALUE;
    
    public LoadTab() {}
    
    public String func_175243_a(NetworkPlayerInfo p_175243_1_)
    {
        return p_175243_1_.func_178854_k() != null ? p_175243_1_.func_178854_k().func_150254_d() : ScorePlayerTeam.func_96667_a(p_175243_1_.func_178850_i(), p_175243_1_.func_178845_a().getName());
    }
    
    public static void drawRect(int left, int top, int right, int bottom, int color)
    {}

    public void drawTexturedModalRect(float xCoord, float yCoord, int minU, int minV, int maxU, int maxV)
    {}

    public void drawTexturedModalRect(int xCoord, int yCoord, TextureAtlasSprite textureSprite, int p_175175_4_, int p_175175_5_)
    {}
    
    public void drawTabOverlay() {
        this.tabplayerlist = new ArrayList<String>();
        func_175249_a((new ScaledResolution(Minecraft.func_71410_x())).func_78326_a(), Minecraft.func_71410_x().field_71441_e.func_96441_U(), Minecraft.func_71410_x().field_71441_e.func_96441_U().func_96539_a(0));
        Main.namesInTab = this.tabplayerlist;
    }
    
    public void func_175249_a(int p_175249_1_, Scoreboard p_175249_2_, ScoreObjective p_175249_3_) {
    	this.tabplayerlist = new ArrayList<String>();
        NetHandlerPlayClient nethandlerplayclient = this.field_175250_f.field_71439_g.field_71174_a;
        List<?> list = field_175252_a.sortedCopy(nethandlerplayclient.func_175106_d());
        int j = 0;
        int k = 0;
        Iterator<?> iterator = list.iterator();
        int l;

        while (iterator.hasNext())
        {
            NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)iterator.next();
            l = this.field_175250_f.field_71466_p.func_78256_a(this.func_175243_a(networkplayerinfo));
            j = Math.max(j, l);

            if (p_175249_3_ != null && p_175249_3_.func_178766_e() != IScoreObjectiveCriteria.EnumRenderType.HEARTS)
            {
                l = this.field_175250_f.field_71466_p.func_78256_a(" " + p_175249_2_.func_96529_a(networkplayerinfo.func_178845_a().getName(), p_175249_3_).func_96652_c());
                k = Math.max(k, l);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));
        int j3 = list.size();
        int k3 = j3;

        for (l = 1; k3 > 20; k3 = (j3 + l - 1) / l)
        {
            ++l;
        }

        boolean flag = this.field_175250_f.func_71387_A() || this.field_175250_f.func_147114_u().func_147298_b().func_179292_f();
        int i1;

        if (p_175249_3_ != null)
        {
            if (p_175249_3_.func_178766_e() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
            {
                i1 = 90;
            }
            else
            {
                i1 = k;
            }
        }
        else
        {
            i1 = 0;
        }

        int j1 = Math.min(l * ((flag ? 9 : 0) + j + i1 + 13), p_175249_1_ - 50) / l;
        int k1 = p_175249_1_ / 2 - (j1 * l + (l - 1) * 5) / 2;
        int l1 = 10;
        int i2 = j1 * l + (l - 1) * 5;
        List<?> list1 = null;
        List<?> list2 = null;
        Iterator<?> iterator1;
        String s;

        if (this.header != null)
        {
            list1 = this.field_175250_f.field_71466_p.func_78271_c(this.header.func_150254_d(), p_175249_1_ - 50);

            for (iterator1 = list1.iterator(); iterator1.hasNext(); i2 = Math.max(i2, this.field_175250_f.field_71466_p.func_78256_a(s)))
            {
                s = (String)iterator1.next();
            }
        }

        if (this.footer != null)
        {
            list2 = this.field_175250_f.field_71466_p.func_78271_c(this.footer.func_150254_d(), p_175249_1_ - 50);

            for (iterator1 = list2.iterator(); iterator1.hasNext(); i2 = Math.max(i2, this.field_175250_f.field_71466_p.func_78256_a(s)))
            {
                s = (String)iterator1.next();
            }
        }

        int j2;

        if (list1 != null)
        {
            //drawRect(p_175249_1_ / 2 - i2 / 2 - 1, l1 - 1, p_175249_1_ / 2 + i2 / 2 + 1, l1 + list1.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (iterator1 = list1.iterator(); iterator1.hasNext(); l1 += this.field_175250_f.field_71466_p.field_78288_b)
            {
                s = (String)iterator1.next();
                j2 = this.field_175250_f.field_71466_p.func_78256_a(s);
                //this.field_175250_f.fontRendererObj.drawStringWithShadow(s, (float)(p_175249_1_ / 2 - j2 / 2), (float)l1, -1);
            }

            ++l1;
        }

        //drawRect(p_175249_1_ / 2 - i2 / 2 - 1, l1 - 1, p_175249_1_ / 2 + i2 / 2 + 1, l1 + k3 * 9, Integer.MIN_VALUE);

        for (int l3 = 0; l3 < j3; ++l3)
        {
            int i4 = l3 / k3;
            j2 = l3 % k3;
            int k2 = k1 + i4 * j1 + i4 * 5;
            int l2 = l1 + j2 * 9;
            //drawRect(k2, l2, k2 + j1, l2 + 8, 553648127);
            //GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            //GlStateManager.enableAlpha();
            //GlStateManager.enableBlend();
            //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            if (l3 < list.size())
            {
                NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo)list.get(l3);
                String s1 = this.func_175243_a(networkplayerinfo1);

                if (flag)
                {
                    //this.field_175250_f.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                   //Gui.drawScaledCustomSizeModalRect(k2, l2, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    EntityPlayer entityplayer = this.field_175250_f.field_71441_e.func_152378_a(networkplayerinfo1.func_178845_a().getId());

                    if (entityplayer != null && entityplayer.func_175148_a(EnumPlayerModelParts.HAT))
                    {
                        //Gui.drawScaledCustomSizeModalRect(k2, l2, 40.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    }

                    k2 += 9;
                }

                if (networkplayerinfo1.func_178848_b() == WorldSettings.GameType.SPECTATOR)
                {
                    s1 = EnumChatFormatting.ITALIC + s1;
                    //this.field_175250_f.fontRendererObj.drawStringWithShadow(s1, (float)k2, (float)l2, -1862270977);
                    this.tabplayerlist.add(s1);
                }
                else
                {
                    //this.field_175250_f.fontRendererObj.drawStringWithShadow(s1, (float)k2, (float)l2, -1);
                    this.tabplayerlist.add(s1); //this is normally the only useful one
                }

                if (p_175249_3_ != null && networkplayerinfo1.func_178848_b() != WorldSettings.GameType.SPECTATOR)
                {
                    int j4 = k2 + j + 1;
                    int i3 = j4 + i1;

                    if (i3 - j4 > 5)
                    {
                    	String networkplayerinfo1s = networkplayerinfo1.func_178845_a().toString();
                        this.func_175247_a(p_175249_3_, l2, networkplayerinfo1.func_178845_a().getName(), j4, i3, networkplayerinfo1);
                    	this.tabplayerlist.add(networkplayerinfo1.func_178845_a().getName());
                    }
                }

                this.func_175245_a(j1, k2 - (flag ? 9 : 0), l2, networkplayerinfo1);
            }
        }

        if (list2 != null)
        {
            l1 += k3 * 9 + 1;
            //drawRect(p_175249_1_ / 2 - i2 / 2 - 1, l1 - 1, p_175249_1_ / 2 + i2 / 2 + 1, l1 + list2.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (iterator1 = list2.iterator(); iterator1.hasNext(); l1 += this.field_175250_f.field_71466_p.field_78288_b)
            {
                s = (String)iterator1.next();
                j2 = this.field_175250_f.field_71466_p.func_78256_a(s);
                //this.field_175250_f.fontRendererObj.drawStringWithShadow(s, (float)(p_175249_1_ / 2 - j2 / 2), (float)l1, -1);
                this.tabplayerlist.add(s);
            }
        }
    }

    protected void func_175245_a(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo p_175245_4_)
    {
        //GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        //this.field_175250_f.getTextureManager().bindTexture(icons);
        byte b0 = 0;
        boolean flag = false;
        byte b1;

        if (p_175245_4_.func_178853_c() < 0)
        {
            b1 = 5;
        }
        else if (p_175245_4_.func_178853_c() < 150)
        {
            b1 = 0;
        }
        else if (p_175245_4_.func_178853_c() < 300)
        {
            b1 = 1;
        }
        else if (p_175245_4_.func_178853_c() < 600)
        {
            b1 = 2;
        }
        else if (p_175245_4_.func_178853_c() < 1000)
        {
            b1 = 3;
        }
        else
        {
            b1 = 4;
        }

        this.zLevel += 100.0F;
        //this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + b0 * 10, 176 + b1 * 8, 10, 8);
        this.zLevel -= 100.0F;
    }

    private void func_175247_a(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_)
    {
        int l = p_175247_1_.func_96682_a().func_96529_a(p_175247_3_, p_175247_1_).func_96652_c();

        if (p_175247_1_.func_178766_e() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
        {
            //this.field_175250_f.getTextureManager().bindTexture(icons);

            if (this.field_175253_j == p_175247_6_.func_178855_p())
            {
                if (l < p_175247_6_.func_178835_l())
                {
                    p_175247_6_.func_178846_a(Minecraft.func_71386_F());
                    p_175247_6_.func_178844_b((long)(this.field_175251_g.func_73834_c() + 20));
                }
                else if (l > p_175247_6_.func_178835_l())
                {
                    p_175247_6_.func_178846_a(Minecraft.func_71386_F());
                    p_175247_6_.func_178844_b((long)(this.field_175251_g.func_73834_c() + 10));
                }
            }

            if (Minecraft.func_71386_F() - p_175247_6_.func_178847_n() > 1000L || this.field_175253_j != p_175247_6_.func_178855_p())
            {
                p_175247_6_.func_178836_b(l);
                p_175247_6_.func_178857_c(l);
                p_175247_6_.func_178846_a(Minecraft.func_71386_F());
            }

            p_175247_6_.func_178843_c(this.field_175253_j);
            p_175247_6_.func_178836_b(l);
            int i1 = MathHelper.func_76123_f((float)Math.max(l, p_175247_6_.func_178860_m()) / 2.0F);
            int j1 = Math.max(MathHelper.func_76123_f((float)(l / 2)), Math.max(MathHelper.func_76123_f((float)(p_175247_6_.func_178860_m() / 2)), 10));
            boolean flag = p_175247_6_.func_178858_o() > (long)this.field_175251_g.func_73834_c() && (p_175247_6_.func_178858_o() - (long)this.field_175251_g.func_73834_c()) / 3L % 2L == 1L;

            if (i1 > 0)
            {
                float f = Math.min((float)(p_175247_5_ - p_175247_4_ - 4) / (float)j1, 9.0F);

                if (f > 3.0F)
                {
                    int k1;

                    for (k1 = i1; k1 < j1; ++k1)
                    {
                        this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }

                    for (k1 = 0; k1 < i1; ++k1)
                    {
                        this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);

                        if (flag)
                        {
                            if (k1 * 2 + 1 < p_175247_6_.func_178860_m())
                            {
                                this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }

                            if (k1 * 2 + 1 == p_175247_6_.func_178860_m())
                            {
                                this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }

                        if (k1 * 2 + 1 < l)
                        {
                            this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, k1 >= 10 ? 160 : 52, 0, 9, 9);
                        }

                        if (k1 * 2 + 1 == l)
                        {
                            this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, k1 >= 10 ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else
                {
                    float f1 = MathHelper.func_76131_a((float)l / 20.0F, 0.0F, 1.0F);
                    int l1 = (int)((1.0F - f1) * 255.0F) << 16 | (int)(f1 * 255.0F) << 8;
                    String s1 = "" + (float)l / 2.0F;

                    if (p_175247_5_ - this.field_175250_f.field_71466_p.func_78256_a(s1 + "hp") >= p_175247_4_)
                    {
                        s1 = s1 + "hp";
                    }

                    //this.field_175250_f.fontRendererObj.drawStringWithShadow(s1, (float)((p_175247_5_ + p_175247_4_) / 2 - this.field_175250_f.fontRendererObj.getStringWidth(s1) / 2), (float)p_175247_2_, l1);
                }
            }
        }
        else
        {
            String s2 = EnumChatFormatting.YELLOW + "" + l;
            //this.field_175250_f.fontRendererObj.drawStringWithShadow(s2, (float)(p_175247_5_ - this.field_175250_f.fontRendererObj.getStringWidth(s2)), (float)p_175247_2_, 16777215);
        }
    }

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator
        {
            private static final String __OBFID = "CL_00001941";

            private PlayerComparator() {}

            public int func_178952_a(NetworkPlayerInfo p_178952_1_, NetworkPlayerInfo p_178952_2_)
            {
                ScorePlayerTeam scoreplayerteam = p_178952_1_.func_178850_i();
                ScorePlayerTeam scoreplayerteam1 = p_178952_2_.func_178850_i();
                return ComparisonChain.start().compareTrueFirst(p_178952_1_.func_178848_b() != WorldSettings.GameType.SPECTATOR, p_178952_2_.func_178848_b() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.func_96661_b() : "", scoreplayerteam1 != null ? scoreplayerteam1.func_96661_b() : "").compare(p_178952_1_.func_178845_a().getName(), p_178952_2_.func_178845_a().getName()).result();
            }

            public int compare(Object p_compare_1_, Object p_compare_2_)
            {
                return this.func_178952_a((NetworkPlayerInfo)p_compare_1_, (NetworkPlayerInfo)p_compare_2_);
            }

            PlayerComparator(Object p_i45528_1_)
            {
                this();
            }
        }


}
