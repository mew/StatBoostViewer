package zone.nora.skyblock.statboostviewer.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.nora.skyblock.statboostviewer.asm.transformer.ClassTransformer;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class FMLLoadingPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { ClassTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
