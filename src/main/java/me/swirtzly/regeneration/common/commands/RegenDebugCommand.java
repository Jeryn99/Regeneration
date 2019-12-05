package me.swirtzly.regeneration.common.commands;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

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

        switch (args[0]) {
            case "glow":
                if (cap.getState().isGraceful()) {
                    cap.getStateManager().fastForwardHandGlow();
                } else throw new CommandException("regeneration.messages.cant_glow");
                break;

            case "fast-forward":
                if (cap.getState() == PlayerUtil.RegenState.ALIVE) {
                    throw new CommandException("regeneration.messages.fast_forward_cmd_fail");
                }
                cap.getStateManager().fastForward();
                break;
            case "set-regens":
                int amount = args.length > 1 ? parseInt(args[1], 0) : -1;
                if (amount >= 0)
                    cap.setRegenerationsLeft(amount);
                else
                    notifyCommandListener(sender, this, "Regenerations: " + cap.getRegenerationsLeft());
                break;
            case "set-trait":
                if (DnaHandler.DNA_ENTRIES.containsKey(new ResourceLocation(args[1]))) {
                    DnaHandler.IDna trait = DnaHandler.DNA_ENTRIES.get(new ResourceLocation(args[1]));
                    DnaHandler.DNA_ENTRIES.get(cap.getDnaType()).onRemoved(cap);
                    trait.onAdded(cap);
                    cap.setDnaType(new ResourceLocation(args[1]));
                    PlayerUtil.sendMessage(player, new TextComponentTranslation(trait.getLangKey()), true);
                } else {
                    throw new CommandException(args[1] + " is not a valid Trait!");
                }

        }

        cap.synchronise();
    }

    @Override
    public String getName() {
        return "regen";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "regeneration.commands.debug.usage";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length < 2)
            return getListOfStringsMatchingLastWord(args, "fast-forward", "set-regens", "glow", "set-trait");
        if (args[0].equals("set-trait") && args.length < 3) {
            return getListOfStringsMatchingLastWord(args, DnaHandler.DNA_ENTRIES.keySet());
        } else
            return Collections.emptyList();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }


}
