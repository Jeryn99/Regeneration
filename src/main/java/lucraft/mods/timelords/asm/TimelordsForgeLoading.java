package lucraft.mods.timelords.asm;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion("1.8")
@TransformerExclusions({"lucraft.mods.timelords.asm"})
@SortingIndex(2147483647)
public class TimelordsForgeLoading implements IFMLLoadingPlugin {
  public static boolean runtimeObfuscationEnabled;
  
  public String[] getASMTransformerClass() {
    return new String[] { TimelordsClassTransformer.class.getCanonicalName() };
  }
  
  public String getModContainerClass() {
    return null;
  }
  
  public String getSetupClass() {
    return null;
  }
  
  public void injectData(Map<String, Object> data) {
    runtimeObfuscationEnabled = !((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
  }
  
  public String getAccessTransformerClass() {
    return null;
  }
}
