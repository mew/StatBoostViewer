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

package zone.nora.skyblock.statboostviewer.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class GuiScreenTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        if (transformedName.equals("net.minecraft.client.gui.GuiScreen")) {
            ClassReader reader = new ClassReader(basicClass);
            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.EXPAND_FRAMES);

            for (MethodNode methodNode : node.methods) {
                String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(node.name, methodNode.name, methodNode.desc);
                if (methodName.equals("renderToolTip") || methodName.equals("func_146285_a")) {
                    methodNode.instructions.clear();
                    methodNode.instructions.add(insnList());
                }
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            try {
                node.accept(writer);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return writer.toByteArray();
        }

        return basicClass;
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
