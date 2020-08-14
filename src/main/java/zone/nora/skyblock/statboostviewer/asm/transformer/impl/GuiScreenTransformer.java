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

package zone.nora.skyblock.statboostviewer.asm.transformer.impl;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import zone.nora.skyblock.statboostviewer.asm.transformer.ClassTransformer;
import zone.nora.skyblock.statboostviewer.asm.transformer.ITransformer;

public class GuiScreenTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[] { "net.minecraft.client.gui.GuiScreen" };
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, methodNode.name, methodNode.desc);
            if (methodName.equals("renderToolTip") || methodName.equals("func_146285_a")) {
                methodNode.instructions.clear();
                methodNode.instructions.add(insnList());
                ClassTransformer.LOGGER.info("Overwrote renderToolTip.");
            }
        }
    }

    private InsnList insnList() {
        InsnList insnList = new InsnList();
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 2));
        insnList.add(new VarInsnNode(Opcodes.ILOAD, 3));
        // Lzone/nora/skyblock/statboostviewer/StatBoostViewer;renderToolTip(Lnet/minecraft/item/ItemStack;II)V
        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zone/nora/skyblock/statboostviewer/StatBoostViewer", "renderToolTip", "(Lnet/minecraft/item/ItemStack;II)V", false));
        insnList.add(new InsnNode(Opcodes.RETURN));
        return insnList;
    }
}
