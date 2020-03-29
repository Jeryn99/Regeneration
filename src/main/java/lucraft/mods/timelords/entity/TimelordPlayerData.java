package lucraft.mods.timelords.entity;

import java.util.Random;
import lucraft.mods.timelords.TimelordsConfig;
import lucraft.mods.timelords.client.render.SkinFile;
import lucraft.mods.timelords.network.MessageSyncPlayerData;
import lucraft.mods.timelords.network.TLPacketDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TimelordPlayerData implements IExtendedEntityProperties {
  public static final String EXT_PROP_NAME = "Timelords";
  
  public final EntityPlayer player;
  
  private byte regenerations;
  
  private short regenTimer;
  
  public static final short maxRegenTimer = 200;
  
  private SkinFile prevSkin;
  
  private SkinFile skin;
  
  public TimelordPlayerData(EntityPlayer player) {
    this.player = player;
    this.regenerations = TimelordsConfig.defaultRegenerations;
    this.regenTimer = 0;
  }
  
  public static final void register(EntityPlayer player) {
    player.registerExtendedProperties("Timelords", new TimelordPlayerData(player));
  }
  
  public static final TimelordPlayerData get(EntityPlayer player) {
    return (TimelordPlayerData)player.getExtendedProperties("Timelords");
  }
  
  public void onUpdate() {
    if (this.player.ticksExisted < 50)
      sendDataToPlayer(); 
    if (isRegenerating()) {
      this.player.heal(0.1F);
      this.player.addPotionEffect(new PotionEffect(Potion.saturation.getId(), 10, 0));
      if (getRegenerationTimer() == 200 && !this.player.worldObj.isRemote) {
        this.prevSkin = this.skin;
        this.skin = getNewSkinFile(getSkinFile(), new Random());
        sendDataToPlayer();
      } 
      AxisAlignedBB aabb = new AxisAlignedBB(this.player.posX - 2.0D, this.player.posY - 2.0D, this.player.posZ - 2.0D, this.player.posX + 2.0D, this.player.posY + 2.0D, this.player.posZ + 2.0D);
      for (Object obj : this.player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, aabb)) {
        if (obj instanceof EntityLivingBase && obj != this.player) {
          EntityLivingBase entity = (EntityLivingBase)obj;
          entity.attackEntityFrom(DamageSource.generic, 2.0F);
        } 
      } 
      this.regenTimer = (short)(this.regenTimer - 1);
    } 
  }
  
  public byte getRegenerations() {
    return this.regenerations;
  }
  
  public void setRegenerations(byte regens) {
    this.regenerations = regens;
    sendDataToPlayer();
  }
  
  public short getRegenerationTimer() {
    return this.regenTimer;
  }
  
  public boolean isRegenerating() {
    return (getRegenerationTimer() > 0);
  }
  
  public float getRegenerationProgress() {
    return this.regenTimer / 200.0F;
  }
  
  public void startRegeneration() {
    this.regenerations = (byte)(this.regenerations - 1);
    this.regenTimer = 200;
    sendDataToAllPlayers();
  }
  
  public SkinFile getSkinFile() {
    return this.skin;
  }
  
  public SkinFile getPrevSkinFile() {
    return this.prevSkin;
  }
  
  public void saveNBTData(NBTTagCompound compound) {
    compound.setByte("Regeneratios", this.regenerations);
    compound.setShort("RegenTimer", this.regenTimer);
    if (this.prevSkin != null) {
      NBTTagCompound sf = new NBTTagCompound();
      this.prevSkin.writeToNBT(sf);
      compound.setTag("PrevSkin", (NBTBase)sf);
    } 
    if (this.skin != null) {
      NBTTagCompound sf = new NBTTagCompound();
      this.skin.writeToNBT(sf);
      compound.setTag("Skin", (NBTBase)sf);
    } 
  }
  
  public void loadNBTData(NBTTagCompound compound) {
    if (compound.hasKey("Regeneratios")) {
      this.regenerations = compound.getByte("Regeneratios");
    } else {
      this.regenerations = TimelordsConfig.defaultRegenerations;
    } 
    this.regenTimer = compound.getShort("RegenTimer");
    if (compound.hasKey("PrevSkin")) {
      this.prevSkin = new SkinFile(SkinFile.SkinFileType.FILE, "", false);
      this.prevSkin.readFromNBT(compound.getCompoundTag("PrevSkin"));
    } 
    if (compound.hasKey("Skin")) {
      this.skin = new SkinFile(SkinFile.SkinFileType.FILE, "", false);
      this.skin.readFromNBT(compound.getCompoundTag("Skin"));
    } 
  }
  
  public void init(Entity entity, World world) {}
  
  public void sendDataToPlayer() {
    sendDataToPlayer(this.player);
  }
  
  public void sendDataToPlayer(EntityPlayer toPlayer) {
    if (toPlayer instanceof EntityPlayerMP)
      TLPacketDispatcher.sendTo((IMessage)new MessageSyncPlayerData(this.player), (EntityPlayerMP)toPlayer); 
  }
  
  public void sendDataToAllPlayers() {
    TLPacketDispatcher.sendToAllAround((IMessage)new MessageSyncPlayerData(this.player), this.player, 100.0D);
  }
  
  public SkinFile getNewSkinFile(SkinFile current, Random rand) {
    if (TimelordsConfig.skins.size() > 1) {
      SkinFile newSkin = TimelordsConfig.getRandomSkinFile(rand);
      if (newSkin == current)
        return getNewSkinFile(current, rand); 
      return newSkin;
    } 
    return TimelordsConfig.skins.get(0);
  }
}
