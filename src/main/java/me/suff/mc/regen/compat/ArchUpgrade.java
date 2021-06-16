package me.suff.mc.regen.compat;

import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.tileentities.ConsoleTile;
import net.tardis.mod.upgrades.Upgrade;
import net.tardis.mod.upgrades.UpgradeEntry;

/**
 * Created by Craig
 * on 22/04/2020 @ 12:13
 */
public class ArchUpgrade extends Upgrade {

    protected ArchUpgrade(UpgradeEntry<?> entry, ConsoleTile tile, Class<? extends Subsystem> clazz) {
        super(entry, tile, clazz);
    }

    public ArchUpgrade(UpgradeEntry<?> upgradeEntry, ConsoleTile consoleTile) {
        this(upgradeEntry, consoleTile, null);
    }

    @Override
    public void onLand() {

    }

    @Override
    public void onTakeoff() {

    }

    @Override
    public void onFlightSecond() {

    }

    @Override
    public boolean isUsable() {
        return super.isUsable() && getStack().getDamageValue() < this.getStack().getMaxDamage();
    }
}
