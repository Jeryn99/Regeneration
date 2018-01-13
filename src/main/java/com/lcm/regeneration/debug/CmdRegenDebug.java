package com.lcm.regeneration.debug;

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
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		for (String a : args) { //@formatter:off
			int i;
			try { i = Integer.parseInt(a); }
			catch (Exception e) { throw new CommandException("Failed to execute debug action " + a, e); } //@formatter:on
			EntityPlayer player = (EntityPlayer)sender.getCommandSenderEntity();
			
			switch (i) {
				default:
					server.sendMessage(new TextComponentString("No debug action defined for " + i));
			}
		}
	}
	
}
