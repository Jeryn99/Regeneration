package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.common.commands.RegenCommand;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.util.RegenUtil;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.atomic.AtomicBoolean;

public class CommonEvents {

    public static void init() {
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

        UseItemCallback.EVENT.register((player, world, hand) -> {
            RegenUtil.spawnHandIfPossible(player, player.getItemInHand(InteractionHand.MAIN_HAND));
            return InteractionResultHolder.pass(player.getItemInHand(InteractionHand.MAIN_HAND));
        });
    }

}
