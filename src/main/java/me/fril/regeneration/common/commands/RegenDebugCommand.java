package me.fril.regeneration.common.commands;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class RegenDebugCommand extends CommandBase {
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0 || args.length > 2)
			throw new WrongUsageException("regeneration.commands.debug.usage");
		
		EntityPlayer player = getCommandSenderAsPlayer(sender);
		IRegeneration cap = CapabilityRegeneration.getForPlayer(player);
		
		int amount = args.length > 1 ? parseInt(args[1], 0) : -1;
		
		switch (args[0]) {
			case "fastforward":
				cap.getStateManager().fastForward();
				break;
				
			case "open":
				RegenerationMod.DEBUGGER.open();
				break;
				
			case "setregens":
				if (amount > 0) {
					int difference = amount - cap.getRegenerationsLeft();
					if (difference > 0)
						cap.receiveRegenerations(difference);
					else
						cap.extractRegeneration(difference);
				} else notifyCommandListener(sender, this, "Regenerations: "+cap.getRegenerationsLeft());
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
			return getListOfStringsMatchingLastWord(args, "fastforward", "setregens", "open");
		else
			return Collections.emptyList();
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
}
