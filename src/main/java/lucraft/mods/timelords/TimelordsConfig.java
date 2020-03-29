package lucraft.mods.timelords;

import java.util.ArrayList;
import java.util.Random;
import lucraft.mods.timelords.client.render.SkinFile;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TimelordsConfig {
  public static byte defaultRegenerations = 12;
  
  private static String[] skinsList;
  
  public static ArrayList<SkinFile> skins = new ArrayList<SkinFile>();
  
  public static void preInit(FMLPreInitializationEvent e) {
    Configuration config = new Configuration(e.getSuggestedConfigurationFile());
    config.load();
    defaultRegenerations = (byte)config.getInt("Regenerations", "General", 12, 0, 256, "Default amount of regenerations players get");
    skinsList = config.getStringList("Skins", "General", new String[] { "WEB:http://i.imgur.com/K7JkZr4.png:SMALL_ARMS", "FILE:11thdoctor:SMALL_ARMS", "FILE:12thdoctor:SMALL_ARMS", "FILE:wardoctor", "FILE:9thdoctor:SMALL_ARMS", "FILE:4thdoctor" }, "Skins players get with regenerations. Use 'FILE:<file name>' or 'WEB:<URL>' to put in skins. Put ':SMALL_ARMS' at the end to mark the skin with small arms.");
    config.save();
  }
  
  public static void postInit() {
    for (String s : skinsList) {
      boolean smallArms = s.endsWith(":SMALL_ARMS");
      if (smallArms)
        s = s.replace(":SMALL_ARMS", ""); 
      if (s.startsWith("FILE:")) {
        skins.add(new SkinFile(SkinFile.SkinFileType.FILE, s.substring(5), smallArms));
      } else if (s.startsWith("WEB:")) {
        skins.add(new SkinFile(SkinFile.SkinFileType.WEB, s.substring(4), smallArms));
      } 
    } 
  }
  
  public static SkinFile getRandomSkinFile(Random rand) {
    return skins.get(rand.nextInt(skins.size()));
  }
}
