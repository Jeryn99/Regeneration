package me.swirtzly.regeneration.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.swirtzly.regeneration.client.gui.CustomizerScreen;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class RegenDebugCommand {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("regen-debug")
				.requires(s -> s.hasPermissionLevel(ServerLifecycleHooks.getCurrentServer().getOpPermissionLevel()))
				.then(Commands.literal("glow")
						.executes(ctx -> glow(ctx.getSource())))
				.then(Commands.literal("fast-forward")
						.executes(ctx -> fastForward(ctx.getSource())))
				.then(Commands.literal("set-regens")
						.then(Commands.argument("amount", IntegerArgumentType.integer(1)) //minimal regen to set is 1
								.executes(ctx -> setRegens(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "amount"))))));
	}

	private static int glow(CommandSource source) {
		try {
            RegenCap.get(source.asPlayer()).ifPresent((cap) -> {
				cap.getStateManager().fastForwardHandGlow();
			});
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}

	private static int fastForward(CommandSource source) {
		try {
            RegenCap.get(source.asPlayer()).ifPresent((cap) ->
			{
				if (cap.getState() != PlayerUtil.RegenState.ALIVE) {
					cap.getStateManager().fastForward();
				} else {
					throw new CommandException(new TranslationTextComponent("regeneration.messages.fast_forward_cmd_fail"));
				}
			});

		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}


	private static int setRegens(CommandSource source, int amount) {
		try {
			Minecraft.getInstance().displayGuiScreen(new CustomizerScreen());
            RegenCap.get(source.asPlayer()).ifPresent((cap) -> cap.setRegenerationsLeft(amount));
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}
}