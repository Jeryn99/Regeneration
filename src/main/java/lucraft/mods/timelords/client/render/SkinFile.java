package lucraft.mods.timelords.client.render;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class SkinFile {
  private SkinFileType type;
  
  private String path;
  
  private ResourceLocation loc;
  
  private boolean smallArms;
  
  public SkinFile(SkinFileType type, String path, boolean smallArms) {
    this.type = type;
    this.path = path;
    this.smallArms = smallArms;
    if (type == SkinFileType.FILE) {
      this.loc = new ResourceLocation("timelords", "textures/skins/" + path + ".png");
    } else if (type == SkinFileType.WEB) {
      this.loc = new ResourceLocation(path);
    } 
  }
  
  public void init() {
    TLRenderer.getDownloadImageSkin(this.loc, this.path);
  }
  
  public ResourceLocation getResourceLocation() {
    return this.loc;
  }
  
  public SkinFileType getType() {
    return this.type;
  }
  
  public boolean hasSmallArms() {
    return this.smallArms;
  }
  
  public void writeToNBT(NBTTagCompound nbt) {
    nbt.setString("Type", this.type.toString());
    nbt.setString("Path", this.path);
    nbt.setString("ResourceLocation", this.loc.toString());
    nbt.setBoolean("SmallArms", this.smallArms);
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    this.type = SkinFileType.getTypeFromName(nbt.getString("Type"));
    this.path = nbt.getString("Path");
    this.loc = new ResourceLocation(nbt.getString("ResourceLocation"));
    this.smallArms = nbt.getBoolean("SmallArms");
    if (this.type == SkinFileType.WEB)
      TLRenderer.getDownloadImageSkin(this.loc, this.path); 
  }
  
  public enum SkinFileType {
    FILE, WEB;
    
    public static SkinFileType getTypeFromName(String name) {
      for (SkinFileType sf : values()) {
        if (sf.toString().equals(name))
          return sf; 
      } 
      return null;
    }
  }
}
