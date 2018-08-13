package me.sub.regeneration.utils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;

public class DebugCommand extends CommandBase {

	@Override public String getName() {
		return "regdebug";
	}

	@Override public String getUsage(ICommandSender sender) {
		return "/regdebug <kill|set <amount>|quick-regen>";
	}

	@Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!(sender.getCommandSenderEntity() instanceof EntityPlayer))
			throw new CommandException("Can only use regeneration debug commands as player", (Object[]) args);
		if (args.length == 0)
			throw new CommandException("No arguments given", (Object[]) args);
		EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();

		switch (args[0]) {
			case "kill":
				player.attackEntityFrom(NoRegenDamageSource.INSTANCE, Float.MAX_VALUE);
				break;
			case "quick-regen":
				player.attackEntityFrom(QuickRegenDamageSource.INSTANCE, Float.MAX_VALUE);
				break;
			case "set":
				try {
//					SuperpowerHandler.getSpecificSuperpowerPlayerHandler(player, TimelordSuperpowerHandler.class).regenerationsLeft = Integer.valueOf(args[1]);
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					throw new CommandException("Invalid use of set", (Object[]) args);
				} catch (NullPointerException e) {
					throw new CommandException("Not a timelord", (Object[]) args);
				}
				break;
			default:
				throw new CommandException("Unknown option " + args[0], (Object[]) args);
		}
	}

	public static class QuickRegenDamageSource extends DamageSource {
		public static DamageSource INSTANCE = new QuickRegenDamageSource();

		public QuickRegenDamageSource() {
			super("quickregen");
		}
	}

	public static class NoRegenDamageSource extends DamageSource {
		public static DamageSource INSTANCE = new NoRegenDamageSource();

		public NoRegenDamageSource() {
			super("noregen");
		}
	}

}
