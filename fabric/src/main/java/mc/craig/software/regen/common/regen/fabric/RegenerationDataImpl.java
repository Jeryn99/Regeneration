package mc.craig.software.regen.common.regen.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import mc.craig.software.regen.common.regen.RegenerationData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.Objects;
import java.util.Optional;

public class RegenerationDataImpl extends RegenerationData implements ComponentV3 {

    public RegenerationDataImpl(LivingEntity livingEntity) {
        super(livingEntity);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.deserializeNBT(tag);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        CompoundTag nbt = this.serializeNBT();
        for (String key : nbt.getAllKeys()) {
            tag.put(key, Objects.requireNonNull(nbt.get(key)));
        }
    }

    public static Optional<RegenerationData> get(LivingEntity livingEntity) {
        try {
            return Optional.of(RegenerationComponents.REGENERATION_DATA.get(livingEntity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
