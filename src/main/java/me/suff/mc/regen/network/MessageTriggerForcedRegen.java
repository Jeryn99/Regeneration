package me.suff.mc.regen.network;

import io.netty.buffer.ByteBuf;
import me.suff.mc.regen.common.advancements.RegenTriggers;
import me.suff.mc.regen.common.capability.CapabilityRegeneration;
import me.suff.mc.regen.common.capability.IRegeneration;
import me.suff.mc.regen.common.item.ItemHand;
import me.suff.mc.regen.common.tiles.TileEntityHandInJar;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static me.suff.mc.regen.util.PlayerUtil.RegenState.REGENERATING;

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

                    if (RegenUtil.getContainer(RegenUtil.getPosLookingAt(player), player.world) != null) {
                        TileEntityHandInJar detected = RegenUtil.getContainer(RegenUtil.getPosLookingAt(player), player.world);
                        if (detected.hasHand()) {
                            boolean isPlayers = ItemHand.getOwner(detected.getHand()).toString().equals(player.getUniqueID().toString());
                            if (isPlayers) {
                                data.getStateManager().fastForward();
                                data.setEncodedSkin(ItemHand.getTextureString(detected.getHand()));
                                data.setSkinType(ItemHand.getSkinType(detected.getHand()));
                                data.setDnaType(new ResourceLocation(ItemHand.getTrait(detected.getHand())));
                                data.synchronise();
                                data.setSyncingFromJar(true);
                                NetworkHandler.INSTANCE.sendToAll(new MessageRemovePlayer(player.getUniqueID()));
                                detected.clear();
                                detected.setLindosAmont(detected.getLindosAmont() + player.world.rand.nextInt(15));
                                RegenTriggers.HAND_JAR_FIRST.trigger(player);
                                data.getStateManager().fastForward();
                                detected.markDirty();
                                detected.sendUpdates();
                                return;
                            }
                        }
                    } else {
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
                }
            });

            return null;
        }

    }
}
