package me.suff.mc.regen.common.objects;

import com.google.gson.JsonObject;
import me.suff.mc.regen.common.item.FobWatchItem;
import me.suff.mc.regen.config.RegenConfig;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fmllegacy.RegistryObject;
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
        itemStack.setDamageValue(random.nextInt(8));
        return itemStack;
    }

    public static class RegenerationLoot extends LootModifier {

        private final int chance;

        public RegenerationLoot(final LootItemCondition[] conditionsIn, int chance) {
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
            public RegenerationLoot read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions) {
                final int multiplicationFactor = GsonHelper.getAsInt(object, "chance", 2);
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
