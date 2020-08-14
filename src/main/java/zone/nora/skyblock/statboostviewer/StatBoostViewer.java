/*
 * Stat Boost Viewer
 * Copyright (C) 2020 Nora Cos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package zone.nora.skyblock.statboostviewer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.List;

@Mod(modid = "StatBoostViewer", name = "StatBoostViewer", version = "1.0.1")
public class StatBoostViewer {
    private static final Minecraft mc = Minecraft.getMinecraft();

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent e) {
        String c = null;
        if (ForgeVersion.buildVersion != 2318) {
            JOptionPane.showMessageDialog(new JFrame(), "StatBoostViewer will not work on your Forge version.\nPlease download the LATEST 1.8.9 Forge version.", "StatBoostViewer", JOptionPane.ERROR_MESSAGE);
            try {
                if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("http://files.minecraftforge.net/maven/net/minecraftforge/forge/index_1.8.9.html"));
                }
            } catch (Exception ignored) { }
            c = "Unsupported MinecraftForge Version. Update to the Latest 1.8.9 Forge Version!";
        } else {
            try {
                // this is obviously super easy to bypass. it is just to deter, not to prevent.
                // if i *really* cared, i would obfuscate the mod and definitely not open source it.
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpUriRequest req = new HttpGet(new URI("https://gist.githubusercontent.com/mew/6686a939151c8fb3be34a54392646189/raw"));
                req.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
                String s = EntityUtils.toString(httpClient.execute(req).getEntity());
                if (s.contains(mc.getSession().getPlayerID().replace("-", ""))) {
                    c = "You are blacklisted from using StatBoostViewer.";
                }
            } catch (Exception ignored) { }
        }
        if (c != null) {
            throw new RuntimeException(c);
        }
    }

    public static void renderToolTip(ItemStack stack, int x, int y) {
        List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + list.get(i));
            } else {
                list.set(i, EnumChatFormatting.GRAY + list.get(i));
            }
        }

        NBTTagCompound nbt = stack.serializeNBT();
        if (nbt.hasKey("tag")) {
            NBTTagCompound tag = nbt.getCompoundTag("tag");
            if (tag.hasKey("ExtraAttributes")) {
                NBTTagCompound extraAttributes = tag.getCompoundTag("ExtraAttributes");
                boolean flag = extraAttributes.hasKey("baseStatBoostPercentage", 99);
                boolean flag2 = extraAttributes.hasKey("item_tier", 99);

                if (flag || flag2) {
                    list.add("");
                    if (flag) {
                        int i = extraAttributes.getInteger("baseStatBoostPercentage");
                        list.add("\u00a76Stat Boost Percentage: " + i + "/50");
                    }
                    if (flag2) {
                        int i = extraAttributes.getInteger("item_tier");
                        list.add("\u00a76Found on Floor " + i);
                    }
                }
            }
        }

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GuiUtils.drawHoveringText(list, x, y, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), -1, (font == null ? mc.fontRendererObj : font));
    }
}
