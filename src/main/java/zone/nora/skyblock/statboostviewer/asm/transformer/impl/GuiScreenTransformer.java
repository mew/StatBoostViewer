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
            if (methodName.equals("renderToolTip")) {
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
