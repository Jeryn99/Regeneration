package mc.craig.software.regen.fabric.handlers;

import mc.craig.software.regen.common.commands.RegenCommand;
import mc.craig.software.regen.common.objects.RSoundSchemes;
import mc.craig.software.regen.common.regen.RegenerationData;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionResult;

import java.util.concurrent.atomic.AtomicBoolean;

public class CommonEvents {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RegenCommand.register(dispatcher));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> RSoundSchemes.init());

        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClientSide) return InteractionResult.PASS;
            AtomicBoolean stopBreak = new AtomicBoolean(false);
            RegenerationData.get(player).ifPresent(regenerationData -> {
                stopBreak.set(regenerationData.stateManager().onPunchBlock(pos, world.getBlockState(pos), player));
            });
            return stopBreak.get() ? InteractionResult.FAIL : InteractionResult.PASS;
        });

    }

}
