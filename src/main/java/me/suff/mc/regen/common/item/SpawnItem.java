package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSoundSchemes;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SpawnItem extends Item {

    public SpawnItem() {
        super(new Properties().tab(RItems.MAIN));
    }

    public static void setType(ItemStack stack, Timelord type) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("type", type.name());
    }

    public static Timelord getType(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        String timelordType = tag.getString("type");
        timelordType = timelordType.isEmpty() ? SpawnItem.Timelord.FEMALE_COUNCIL.name() : timelordType;
        return SpawnItem.Timelord.valueOf(timelordType);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowedIn(group)) {
            for (Timelord timelordType : SpawnItem.Timelord.values()) {
                ItemStack itemstack = new ItemStack(this);
                setType(itemstack, timelordType);
                items.add(itemstack);
            }
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        Timelord name = getType(stack);
        return Component.translatable("regen.timelord_type." + name.name().toLowerCase());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = player.getUsedItemHand();

        if (!worldIn.isClientSide) {
            me.suff.mc.regen.common.entities.Timelord timelord = REntities.TIMELORD.get().create(worldIn);
            timelord.setMale(getType(context.getItemInHand()).isMale());
            timelord.setPersonality(RSoundSchemes.getRandom(timelord.male()).identify().toString());
            if (getType(context.getItemInHand()) == SpawnItem.Timelord.GUARD) {
                timelord.setTimelordType(me.suff.mc.regen.common.entities.Timelord.TimelordType.GUARD);
            } else {
                timelord.setTimelordType(me.suff.mc.regen.common.entities.Timelord.TimelordType.COUNCIL);
            }
            RegenCap.get(timelord).ifPresent(iRegen -> {
                timelord.initSkin(iRegen);
                timelord.genName();
                iRegen.setRegens(12);
                CompoundTag nbt = new CompoundTag();
                nbt.putFloat(RConstants.PRIMARY_RED, RegenUtil.RAND.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_GREEN, RegenUtil.RAND.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_BLUE, RegenUtil.RAND.nextInt(255) / 255.0F);

                nbt.putFloat(RConstants.SECONDARY_RED, RegenUtil.RAND.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_GREEN, RegenUtil.RAND.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_BLUE, RegenUtil.RAND.nextInt(255) / 255.0F);
                iRegen.readStyle(nbt);
            });
            timelord.setup();
            timelord.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            timelord.lookAt(player, 90.0F, 90.0F);
            player.getItemInHand(hand).shrink(1);
            worldIn.addFreshEntity(timelord);

            if (!player.isCreative()) {
                context.getItemInHand().shrink(1);
            }
        }
        return super.useOn(context);
    }

    public enum Timelord {
        FEMALE_COUNCIL(false), MALE_COUNCIL(true), GUARD(false);

        private final boolean isMale;

        Timelord(boolean b) {
            this.isMale = b;
        }

        public boolean isMale() {
            return this == GUARD ? RegenUtil.RAND.nextBoolean() : isMale;
        }
    }

}