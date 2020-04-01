package me.swirtzly.regeneration.common.tiles;

import me.swirtzly.regeneration.client.gui.BioContainerContainer;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class TileEntityHandInJar extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	public int lindosAmont = 0;
	public LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

	public TileEntityHandInJar() {
		super(RegenObjects.Tiles.HAND_JAR);
	}

	public int getLindosAmont() {
		return lindosAmont;
	}

	public void setLindosAmont(int lindosAmont) {
		this.lindosAmont = lindosAmont;
	}

	@Override
	public void tick() {

		if (world.getGameTime() % 35 == 0 && hasHand()) {
			world.playSound(null, getPos().getX(), getPos().getY(), getPos().getZ(), RegenObjects.Sounds.JAR_BUBBLES, SoundCategory.PLAYERS, 0.4F, 0.3F);
		}

		PlayerEntity player = world.getClosestPlayer(getPos().getX(), getPos().getY(), getPos().getZ(), 56, false);
		if (player != null) {
			RegenCap.get(player).ifPresent((data) -> {
				if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
					if (world.rand.nextInt(90) < 10) {
						lindosAmont = lindosAmont + 1;
					}
				}
			});
		}
	}

	public boolean hasHand() {
		return getCapability(ITEM_HANDLER_CAPABILITY).map(data -> data.getStackInSlot(0).getItem() == RegenObjects.Items.HAND).orElse(false);
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 3, getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	public void sendUpdates() {
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		markDirty();
	}

	@Override
	public void read(CompoundNBT tag) {
		CompoundNBT invTag = tag.getCompound("inv");
		handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(invTag));
		lindosAmont = tag.getInt("lindos");
		super.read(tag);
	}

	@Override
	public CompoundNBT write(CompoundNBT tag) {
		handler.ifPresent(h -> {
			CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
			tag.put("inv", compound);
		});
		tag.putInt("lindos", lindosAmont);
		tag.putBoolean("hasHand", hasHand());
		return super.write(tag);
	}

	private IItemHandler createHandler() {
		return new ItemStackHandler(1) {
			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return stack.getItem() == RegenObjects.Items.HAND;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if (stack.getItem() != RegenObjects.Items.HAND) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
		};
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

	public LazyOptional<IItemHandler> getInventory() {
		return getCapability(ITEM_HANDLER_CAPABILITY);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent(RegenObjects.Blocks.HAND_JAR.getTranslationKey());
	}

	@Nullable
	@Override
	public Container createMenu(int slot, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return new BioContainerContainer(slot, playerInventory, playerEntity, this);
	}

	public ItemStack getHand() {
		return getCapability(ITEM_HANDLER_CAPABILITY).map(data -> data.getStackInSlot(0)).orElse(ItemStack.EMPTY);
	}

	public void destroyHand() {
		getCapability(ITEM_HANDLER_CAPABILITY).ifPresent(data -> data.getStackInSlot(0).setCount(0));
	}

}
