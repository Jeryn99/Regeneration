package me.swirtzly.regeneration.compat;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.tileentities.ConsoleTile;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 12:13
 */
public class ArchSubSystem extends Subsystem {

    public ArchSubSystem(ConsoleTile console, Item item) {
        super(console, item);
    }

    @Override
    public void onTakeoff() {

    }

    @Override
    public void onLand() {

    }

    @Override
    public void onFlightSecond() {

    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Override
    public boolean stopsFlight() {
        return false;
    }
}
