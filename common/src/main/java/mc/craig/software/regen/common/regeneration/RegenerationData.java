package mc.craig.software.regen.common.regeneration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mc.craig.software.regen.util.RegenConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class RegenerationData {

    private final Player player;
    private int remainingRegens = 12;

    public RegenerationData(Player player) {
        this.player = player;
    }


    public void tick(Player player){

    }

    public int getRemainingRegens() {
        return this.remainingRegens;
    }

    public void setRemainingRegens(int remainingRegens) {
        this.remainingRegens = remainingRegens;
    }

    public void fromNBT(CompoundTag nbt) {
        this.remainingRegens = nbt.getInt(RegenConstants.REMAINING_REGENS);
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(RegenConstants.REMAINING_REGENS, remainingRegens);
        return nbt;
    }

    @ExpectPlatform
    public static Optional<RegenerationData> get(Player player) {
        throw new AssertionError();
    }

}
