package me.swirtzly.regeneration.common.tiles;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityHandInJar extends TileEntity implements ITickable {

    public boolean isInUse = false;
    public int lindosAmont = 0;
    private AxisAlignedBB AABB = new AxisAlignedBB(0.2, 0, 0, 0.8, 2, 0.1);

    public int getLindosAmont() {
        return lindosAmont;
    }

    public void setLindosAmont(int lindosAmont) {
        this.lindosAmont = lindosAmont;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean inUse) {
        isInUse = inUse;
    }

    @Override
    public void update() {

        if (world != null) {

            if (world.getWorldTime() % 35 == 0 && getLindosAmont() > 0) {
                world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), RegenObjects.Sounds.JAR_BUBBLES, SoundCategory.PLAYERS, 1.0F, 0.3F);
            }

            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, AABB.grow(12, 12, 12));
            for (EntityPlayer player : players) {

                IRegeneration data = CapabilityRegeneration.getForPlayer(player);

                if (!isInUse && data.getState() == PlayerUtil.RegenState.REGENERATING && player.world.getWorldTime() % 30 == 0) {
                    lindosAmont = lindosAmont + 1;
                    isInUse = true;
                }

            }

            isInUse = false;
        }
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("inUse", isInUse);
        compound.setFloat("lindos", lindosAmont);
        return super.writeToNBT(compound);
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        isInUse = compound.getBoolean("inUse");
        lindosAmont = compound.getInteger("lindos");
        super.readFromNBT(compound);
    }


}
