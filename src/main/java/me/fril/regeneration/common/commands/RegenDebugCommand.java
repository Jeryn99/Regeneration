package me.fril.regeneration.common.commands;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.RegenState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class RegenDebugCommand extends CommandBase {
	
	@SuppressWarnings("deprecation")
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0 || args.length > 2)
			throw new WrongUsageException("regeneration.commands.debug.usage");
		
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
		int amount = args.length > 1 ? parseInt(args[1], 0) : -1;
		
		switch (args[0]) {
			case "glow":
				cap.getStateManager().fastForwardHandGlow();
				break;
			
			case "fastforward":
				if (cap.getState() == RegenState.ALIVE) {
					throw new CommandException("regeneration.messages.fast_forward_cmd_fail");
				}
				cap.getStateManager().fastForward();
				break;
			
			case "open":
				RegenerationMod.DEBUGGER.open();
				break;
			
			case "setregens":
				if (amount >= 0)
					cap.setRegenerationsLeft(amount);
				else
					notifyCommandListener(sender, this, "Regenerations: " + cap.getRegenerationsLeft());
				break;
		}
		
		cap.synchronise();
	}
	
	@Override
	public String getName() {
		return "regendebug";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "regeneration.commands.debug.usage";
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length < 2)
			return getListOfStringsMatchingLastWord(args, "fastforward", "setregens", "open", "glow");
		else
			return Collections.emptyList();
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
}
