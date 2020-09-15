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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

@Mod(modid = "StatBoostViewer", name = "StatBoostViewer", version = "1.2")
public class StatBoostViewer {
    @EventHandler
    public void onPostInit(FMLPostInitializationEvent e) {
        String c = null;
        if (!ForgeVersion.getVersion().contains("2318")) {
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
                if (s.contains(Minecraft.getMinecraft().getSession().getPlayerID().replace("-", ""))) {
                    c = "You are blacklisted from using StatBoostViewer.";
                }
            } catch (Exception ignored) { }
        }
        if (c != null) {
            throw new RuntimeException(c);
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onToolTip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;

        NBTTagCompound nbt = stack.serializeNBT();
        if (nbt.hasKey("tag")) {
            NBTTagCompound tag = nbt.getCompoundTag("tag");
            if (tag.hasKey("ExtraAttributes")) {
                NBTTagCompound extraAttributes = tag.getCompoundTag("ExtraAttributes");
                boolean flag = extraAttributes.hasKey("baseStatBoostPercentage", 99);
                boolean flag2 = extraAttributes.hasKey("item_tier", 99);

                if (flag || flag2) {
                    event.toolTip.add("");
                    if (flag) {
                        int i = extraAttributes.getInteger("baseStatBoostPercentage");
                        event.toolTip.add("\u00a76Stat Boost Percentage: " + i + "/50");
                    }
                    if (flag2) {
                        int i = extraAttributes.getInteger("item_tier");
                        event.toolTip.add("\u00a76Found on Floor " + i);
                    }
                }
            }
        }
    }
}
