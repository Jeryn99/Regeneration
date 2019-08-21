package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.common.advancements.RegenTriggers;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.item.ItemHand;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static me.swirtzly.regeneration.util.PlayerUtil.RegenState.REGENERATING;

public class MessageTriggerForcedRegen implements IMessage {

    public MessageTriggerForcedRegen() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<MessageTriggerForcedRegen, IMessage> {

        @Override
        public IMessage onMessage(MessageTriggerForcedRegen message, MessageContext ctx) {

            ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {

                EntityPlayerMP player = ctx.getServerHandler().player;

                IRegeneration data = CapabilityRegeneration.getForPlayer(ctx.getServerHandler().player);
                if (data.canRegenerate() && data.getState() == PlayerUtil.RegenState.ALIVE) {
                    data.getPlayer().attackEntityFrom(RegenObjects.REGEN_DMG_LINDOS, Integer.MAX_VALUE);
                }

                if (data.getState() == REGENERATING) {
                    for (BlockPos pos : BlockPos.getAllInBox(player.getPosition().add(15, 15, 15), player.getPosition().subtract(new Vec3i(15, 15, 15)))) {
                        IBlockState blockState = player.world.getBlockState(pos);
                        if (blockState.getBlock().getRegistryName() == RegenObjects.Blocks.HAND_JAR.getRegistryName()) {
                            TileEntityHandInJar handInJar = (TileEntityHandInJar) player.world.getTileEntity(pos);
                            if (handInJar.hasHand()) {
                                boolean isPlayers = ItemHand.getOwner(handInJar.getHand()).toString().equals(player.getUniqueID().toString());
                                if (isPlayers) {
                                    PlayerUtil.lookAt(pos.getX(), pos.getY(), pos.getZ(), player);
                                    data.getStateManager().fastForward();
                                    data.setEncodedSkin(ItemHand.getTextureString(handInJar.getHand()));
                                    data.setSkinType(ItemHand.getSkinType(handInJar.getHand()));
                                    data.setDnaType(new ResourceLocation(ItemHand.getTrait(handInJar.getHand())));
                                    data.synchronise();
                                    data.setSyncingFromJar(true);
                                    NetworkHandler.INSTANCE.sendToAll(new MessageRemovePlayer(player.getUniqueID()));
                                    handInJar.clear();
                                    handInJar.setLindosAmont(handInJar.getLindosAmont() + player.world.rand.nextInt(15));
                                    RegenTriggers.HAND_JAR_FIRST.trigger(player);
                                    data.getStateManager().fastForward();
                                    handInJar.markDirty();
                                    handInJar.sendUpdates();
                                    return;
                                }
                            }
                        }
                    }
                }

            });

            return null;
        }

    }
}
