package lucraft.mods.timelords.commands;

import java.util.List;
import lucraft.mods.timelords.entity.TimelordPlayerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class CommandSetRegenerations extends CommandBase {
  public String getCommandName() {
    return "setregenerations";
  }
  
  public int getRequiredPermissionLevel() {
    return 2;
  }
  
  public String getCommandUsage(ICommandSender sender) {
    return "commands.setregenerations.usage";
  }
  
  public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    if (args.length != 2)
      throw new WrongUsageException("commands.setregenerations.usage", new Object[0]); 
    EntityPlayerMP entityPlayerMP = getPlayer(sender, args[0]);
    int regens = parseInt(args[1], 0, 256);
    TimelordPlayerData.get((EntityPlayer)entityPlayerMP).setRegenerations((byte)regens);
    sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.setregenerations.success.self", new Object[] { Integer.valueOf(regens) }));
    if (entityPlayerMP != sender) {
      notifyOperators(sender, (ICommand)this, 1, "commands.setregenerations.success.other", new Object[] { entityPlayerMP.getCommandSenderName(), Integer.valueOf(regens) });
    } else {
      notifyOperators(sender, (ICommand)this, 1, "commands.setregenerations.success.self", new Object[] { Integer.valueOf(regens) });
    } 
  }
  
  protected String[] getListOfPlayerUsernames() {
    return MinecraftServer.getServer().getAllUsernames();
  }
  
  public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    return (args.length == 1) ? getListOfStringsMatchingLastWord(args, getListOfPlayerUsernames()) : null;
  }
}
