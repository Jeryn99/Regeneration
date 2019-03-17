package me.suff.regeneration.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.CapabilityRegeneration;
import me.suff.regeneration.util.RegenState;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class CommandRegen {
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("regendebug")
				.requires(s -> s.hasPermissionLevel(ServerLifecycleHooks.getCurrentServer().getOpPermissionLevel()))
				.then(Commands.literal("glow")
						.executes(ctx -> glow(ctx.getSource())))
				.then(Commands.literal("fastforward")
						.executes(ctx -> fastForward(ctx.getSource())))
				.then(Commands.literal("open")
						.executes(ctx -> open(ctx.getSource())))
				.then(Commands.literal("setregens")
						.then(Commands.argument("amount", IntegerArgumentType.integer(1)) //minimal regen to set is 1
								.executes(ctx -> setRegens(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "amount"))))));
	}
	
	private static int glow(CommandSource source) {
		try {
			CapabilityRegeneration.getForPlayer(source.asPlayer()).ifPresent((cap) -> {
				cap.getStateManager().fastForwardHandGlow();
			});
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}
	
	private static int fastForward(CommandSource source) {
		try {
			CapabilityRegeneration.getForPlayer(source.asPlayer()).ifPresent((cap) ->
			{
				if(cap.getState() != RegenState.ALIVE) {
					cap.getStateManager().fastForward();
				} else {
					throw new CommandException(new TextComponentTranslation("regeneration.messages.fast_forward_cmd_fail"));
				}
			});
			
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}
	
	private static int open(CommandSource source) {
		RegenerationMod.DEBUGGER.open();
		return Command.SINGLE_SUCCESS;
	}
	
	private static int setRegens(CommandSource source, int amount) {
		try {
			CapabilityRegeneration.getForPlayer(source.asPlayer()).ifPresent((cap) -> cap.setRegenerationsLeft(amount));
		} catch (CommandSyntaxException e) {
			e.printStackTrace();
		}
		return Command.SINGLE_SUCCESS;
	}
}