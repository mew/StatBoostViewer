package zone.nora.skyblock.statboostviewer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod(modid = "StatBoostViewer", name = "StatBoostViewer", version = "1.0")
public class StatBoostViewer {
    public static Minecraft mc = Minecraft.getMinecraft();

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
