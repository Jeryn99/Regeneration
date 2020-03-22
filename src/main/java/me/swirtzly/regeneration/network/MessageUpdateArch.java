package me.swirtzly.regeneration.network;

import io.netty.buffer.ByteBuf;
import me.swirtzly.regeneration.common.item.arch.capability.CapabilityArch;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Swirtzly on 01/03/2020 @ 12:46
 */
public class MessageUpdateArch implements IMessage {

    private NBTTagCompound data;
    private ItemStack stack;

    public MessageUpdateArch() {
    }

    public MessageUpdateArch(ItemStack stack, NBTTagCompound data) {
        this.stack = stack;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stack = ByteBufUtils.readItemStack(buf);
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
        ByteBufUtils.writeTag(buf, data);
    }

    public static class Handler implements IMessageHandler<MessageUpdateArch, IMessage> {

        @Override
        public IMessage onMessage(MessageUpdateArch message, MessageContext ctx) {
            System.out.println(message.data);
            Minecraft.getMinecraft().addScheduledTask(() -> CapabilityArch.getForPlayer(message.stack).deserializeNBT(message.data));
            return null;
        }
    }
}
