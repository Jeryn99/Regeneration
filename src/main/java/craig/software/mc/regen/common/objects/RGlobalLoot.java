package craig.software.mc.regen.common.objects;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import craig.software.mc.regen.common.item.FobWatchItem;
import craig.software.mc.regen.config.RegenConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static craig.software.mc.regen.util.RConstants.MODID;

/* Created by Craig on 10/03/2021 */
public class RGlobalLoot {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

    public static final RegistryObject<Codec<RegenerationLoot>> REGEN_LOOT = GLM.register("loot", RegenerationLoot.CODEC);

    public static ItemStack createBrokenFob(RandomSource random) {
        ItemStack itemStack = new ItemStack(RItems.FOB.get());
        FobWatchItem.setEngrave(itemStack, random.nextBoolean());
        itemStack.setDamageValue(random.nextInt(8));
        return itemStack;
    }

    public static class RegenerationLoot extends LootModifier {
        public static final Supplier<Codec<RegenerationLoot>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst)
                .and(ExtraCodecs.POSITIVE_INT.optionalFieldOf("chance", 2).forGetter(m -> m.chance))
                .apply(inst, RegenerationLoot::new)
        ));
        ;

        public final int chance;

        public RegenerationLoot(LootItemCondition[] conditionsIn, int chance) {
            super(conditionsIn);
            this.chance = chance;
        }

        @NotNull
        @Override
        public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
            if (context.getRandom().nextInt(100) <= chance && RegenConfig.COMMON.genFobLoot.get()) {
                generatedLoot.add(createBrokenFob(context.getRandom()));
            }

            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec() {
            return CODEC.get();
        }
    }


}
