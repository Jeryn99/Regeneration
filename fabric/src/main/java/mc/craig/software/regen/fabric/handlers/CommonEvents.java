package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.common.commands.RegenCommand;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.util.RegenUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.atomic.AtomicBoolean;

public class CommonEvents {

    private static final ResourceLocation MARS_TEMPLE_LOOT_TABLE_ID = new ResourceLocation("ad_astra:chests/temple/mars/temple");

    public static void init() {

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (MARS_TEMPLE_LOOT_TABLE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.lootPool()
                        .add(LootItem.lootTableItem(RItems.ZINC.get())
                                .setWeight(15) // Somewhat common
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .add(LootItem.lootTableItem(RItems.FOB.get())
                                .setWeight(2) // Rare
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))));

                tableBuilder.pool(poolBuilder.build());

            }
        });



        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RegenCommand.register(dispatcher));

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> RegenerationData.get(player).ifPresent(regenerationData -> regenerationData.syncToClients(null)));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            RegenUtil.setupNames();
        });

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClientSide) return InteractionResult.PASS;
            AtomicBoolean stopBreak = new AtomicBoolean(false);
            RegenerationData.get(player).ifPresent(regenerationData -> stopBreak.set(regenerationData.stateManager().onPunchBlock(pos, world.getBlockState(pos), player)));
            return stopBreak.get() ? InteractionResult.FAIL : InteractionResult.PASS;
        });

        EntityTrackingEvents.START_TRACKING.register((trackedEntity, player) -> {
            if (trackedEntity instanceof LivingEntity livingEntity) {
                RegenerationData.get(livingEntity).ifPresent(data -> data.syncToClients(null));
            }
        });

    }

}
