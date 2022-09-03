package mc.craig.software.regen.common.regeneration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class RegenerationData {

    private final Player player;
    private int example = 0;

    public RegenerationData(Player player) {
        this.player = player;
    }

    public int getExample() {
        return this.example;
    }

    public void fromNBT(CompoundTag nbt) {
        this.example = nbt.getInt("Example");
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("Example", this.example);
        return nbt;
    }

    @ExpectPlatform
    public static Optional<RegenerationData> get(Player player) {
        throw new AssertionError();
    }

}
