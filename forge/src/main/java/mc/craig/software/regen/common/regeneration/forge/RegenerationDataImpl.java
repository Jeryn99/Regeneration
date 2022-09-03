package mc.craig.software.regen.common.regeneration.forge;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.regeneration.RegenerationData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Regeneration.MOD_ID)
public class RegenerationDataImpl {

    public static Capability<RegenerationData> REGENERATION_DATA = CapabilityManager.get(new CapabilityToken<>() {
    });

    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent e) {
        e.register(RegenerationData.class);
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (e.getObject() instanceof Player player) {
            e.addCapability(new ResourceLocation(Regeneration.MOD_ID, "regeneration_data"), new RegenerationDataProvider(new RegenerationData(player)));
        }
    }

    public static Optional<RegenerationData> get(Player player) {
        return player.getCapability(REGENERATION_DATA).resolve();
    }

    public static class RegenerationDataProvider implements ICapabilitySerializable<CompoundTag> {

        public final RegenerationData capability;
        public final LazyOptional<RegenerationData> lazyOptional;

        public RegenerationDataProvider(RegenerationData capability) {
            this.capability = capability;
            this.lazyOptional = LazyOptional.of(() -> capability);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            return capability == REGENERATION_DATA ? this.lazyOptional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.capability.toNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag arg) {
            this.capability.fromNBT(arg);
        }
    }

}
