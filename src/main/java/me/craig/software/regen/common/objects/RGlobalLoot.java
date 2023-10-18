package me.craig.software.regen.common.objects;

import com.google.gson.JsonObject;
import me.craig.software.regen.config.RegenConfig;
import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.common.item.FobWatchItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

/* Created by Craig on 10/03/2021 */
public class RGlobalLoot {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, RConstants.MODID);

    public static final RegistryObject<RegenerationLoot.Serializer> REGEN_LOOT = GLM.register("loot", RegenerationLoot.Serializer::new);

    public static ItemStack createBrokenFob(Random random) {
        ItemStack itemStack = new ItemStack(RItems.FOB.get());
        FobWatchItem.setEngrave(itemStack, random.nextBoolean());
        itemStack.setDamageValue(random.nextInt(RegenConfig.COMMON.regenCapacity.get()));
        return itemStack;
    }

    public static class RegenerationLoot extends LootModifier {

        private final int chance;

        public RegenerationLoot(final ILootCondition[] conditionsIn, int chance) {
            super(conditionsIn);
            this.chance = chance;
        }

        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {

            if (context.getRandom().nextInt(100) <= chance && RegenConfig.COMMON.genFobLoot.get()) {
                generatedLoot.add(createBrokenFob(context.getRandom()));
            }

            return generatedLoot;
        }

        private static class Serializer extends GlobalLootModifierSerializer<RegenerationLoot> {
            @Override
            public RegenerationLoot read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
                final int multiplicationFactor = JSONUtils.getAsInt(object, "chance", 2);
                return new RegenerationLoot(conditions, multiplicationFactor);
            }

            @Override
            public JsonObject write(RegenerationLoot instance) {
                final JsonObject obj = this.makeConditions(instance.conditions);
                obj.addProperty("chance", instance.chance);
                return obj;
            }
        }
    }

}
