package lucraft.mods.timelords.commands;

import lucraft.mods.timelords.entity.TimelordPlayerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandTriggerRegeneration extends CommandBase {
  public String getCommandName() {
    return "triggerregeneration";
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.triggerregeneration.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    if (args.length == 0) {
      if (sender instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer)sender;
        TimelordPlayerData data = TimelordPlayerData.get(player);
        if (data.getRegenerations() > 0) {
          data.startRegeneration();
          sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.triggerregeneration.success.self", new Object[0]));
          if (player != sender) {
            notifyOperators(sender, (ICommand)this, 1, "commands.triggerregeneration.success.other", new Object[] { player.getCommandSenderName() });
          } else {
            notifyOperators(sender, (ICommand)this, 1, "commands.triggerregeneration.success.self", new Object[0]);
          } 
        } else {
          sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.triggerregeneration.noregenleft", new Object[0]));
        } 
      } else {
        sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.triggerregeneration.usage", new Object[0]));
      } 
    } else if (args.length == 1) {
      EntityPlayerMP entityPlayerMP = getPlayer(sender, args[0]);
      TimelordPlayerData data = TimelordPlayerData.get((EntityPlayer)entityPlayerMP);
      if (data.getRegenerations() > 0) {
        data.startRegeneration();
        if (entityPlayerMP != sender) {
          notifyOperators(sender, (ICommand)this, 1, "commands.triggerregeneration.success.other", new Object[0]);
        } else {
          notifyOperators(sender, (ICommand)this, 1, "commands.triggerregeneration.success.self", new Object[0]);
        } 
      } else {
        sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.triggerregeneration.noregenleft", new Object[0]));
      } 
    } else {
      throw new WrongUsageException("commands.setregenerations.usage", new Object[0]);
    } 
  }
}
