package me.swirtzly.regeneration.compat;

import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.tardis.mod.protocols.Protocol;
import net.tardis.mod.tileentities.ConsoleTile;

/**
 * Created by Swirtzly
 * on 22/04/2020 @ 11:07
 */
public class ArchProtocol extends Protocol {

    private static final TranslationTextComponent ARCH = new TranslationTextComponent("tardis.protocol.arch");
    private static AxisAlignedBB BOX = new AxisAlignedBB(-20, -20, -20, 20, 20, 20);

    public static BlockPos getGoodArchPlacement(World world, BlockPos consolePos) {
        BlockPos northPos = consolePos.north(2).up(2);
        BlockPos southPos = consolePos.south(2).up(2);
        BlockPos eastPos = consolePos.east(2).up(2);
        BlockPos westPos = consolePos.west(2).up(2);

        if (world.isAirBlock(northPos)) {
            return northPos;
        }

        if (world.isAirBlock(southPos)) {
            return southPos;
        }

        if (world.isAirBlock(eastPos)) {
            return eastPos;
        }

        if (world.isAirBlock(westPos)) {
            return westPos;
        }
        return BlockPos.ZERO;
    }

    @Override
    public void call(World world, PlayerEntity player, ConsoleTile consoleTile) {
    	if (!world.isRemote()) {
            consoleTile.getUpgrade(ArchUpgrade.class).ifPresent((archSubSystem -> {
                if (archSubSystem.isUsable()) {
                    BlockPos pos = consoleTile.getPos();
                    BlockPos placePos = pos.north(2).up(2);
                    if (world.isAirBlock(placePos)) {
                        world.setBlockState(placePos, RegenObjects.Blocks.ARCH.get().getDefaultState());
                        if (consoleTile.getArtron() > 10) {
                            consoleTile.setArtron(consoleTile.getArtron() - 10);
                        }
                        for (PlayerEntity playerEntity : world.getEntitiesWithinAABB(PlayerEntity.class, BOX.offset(consoleTile.getPos()))) {
                             PlayerUtil.sendMessage(playerEntity, "message.regeneration.arch_placed", true);
                        }
                    } 
                    else {
                        world.setBlockState(placePos, Blocks.AIR.getDefaultState());
                        TardisCompat.damageSubsystem(world); //Only damage after you retract the arch
                        for (PlayerEntity playerEntity : world.getEntitiesWithinAABB(PlayerEntity.class, BOX.offset(consoleTile.getPos()))) {
                        	PlayerUtil.sendMessage(playerEntity, "message.regeneration.arch_removed", true);
                        }
                   }
                } 
                else {
                    for (PlayerEntity playerEntity : world.getEntitiesWithinAABB(PlayerEntity.class, BOX.offset(consoleTile.getPos()))) {
                        PlayerUtil.sendMessage(playerEntity, new TranslationTextComponent("message.regeneration.arch_system_dead"), true);
                    }
                }
          }));
    	}
    	else Regeneration.proxy.closeGui();
    }

    @Override
    public String getSubmenu() {
        return "interior";
    }


    @Override
    public String getDisplayName() {
        return ARCH.getUnformattedComponentText();
    }
}
