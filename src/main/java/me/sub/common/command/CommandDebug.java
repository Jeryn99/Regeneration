package me.sub.common.command;

import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sub
 * on 19/09/2018.
 */
public class CommandDebug extends CommandTreeBase {
    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return "regen-debug";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getUsage(ICommandSender sender) {
        return "regen-debug [grace]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            IRegeneration regenInfo = CapabilityRegeneration.get(player);

            if (!regenInfo.isCapable()) {
                throw new CommandException("You must have regenerations to use.");
            }

            //GRACE TESTING
            if (args[0].equalsIgnoreCase("grace")) {
                regenInfo.setRegenerating(true);
                regenInfo.setInGracePeriod(true);
                regenInfo.setSolaceTicks(16800);
            }


        } else {
            throw new CommandException("You are not a player.");
        }
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "grace") : Collections.emptyList();
    }
}
