package com.lcm.regeneration.util;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CmdRegenDebug extends CommandBase {
	
	@Override
	public String getName() {
		return "regdebug";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "/regdebug <actionindex>";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException { //@formatter:off
		for (int i = 0; i < args.length; i++) {
			int c;
			try { c = Integer.parseInt(args[i]); }
			catch (Exception e) { throw new CommandException("Failed to execute debug action " + args[i], e); } //@formatter:on
			@SuppressWarnings("unused")
			EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
			
			switch (c) {
				default:
					server.sendMessage(new TextComponentString("No debug action defined for " + c));
			}
		}
	}
	
}
