package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.objects.RSoundSchemes;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SpawnItem< E extends TimelordEntity > extends Item {

    public SpawnItem() {
        super(new Properties().tab(RItems.MAIN));
    }

    public static void setType(ItemStack stack, Timelord type) {
        CompoundNBT tag = stack.getOrCreateTag();
        tag.putString("type", type.name());
    }

    public static Timelord getType(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        String timelordType = tag.getString("type");
        timelordType = timelordType.isEmpty() ? Timelord.FEMALE_COUNCIL.name() : timelordType;
        return Timelord.valueOf(timelordType);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList< ItemStack > items) {
        if (allowdedIn(group)) {
            for (Timelord timelordType : Timelord.values()) {
                ItemStack itemstack = new ItemStack(this);
                setType(itemstack, timelordType);
                items.add(itemstack);
            }
        }
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        Timelord name = getType(stack);
        return new TranslationTextComponent("regen.timelord_type." + name.name().toLowerCase());
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World worldIn = context.getLevel();
        BlockPos pos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();
        Hand hand = player.getUsedItemHand();

        if (!worldIn.isClientSide) {
            TimelordEntity timelord = REntities.TIMELORD.get().create(worldIn);
            timelord.setMale(getType(context.getItemInHand()).isMale());
            timelord.setPersonality(RSoundSchemes.getRandom(timelord.male()).identify().toString());
            if (getType(context.getItemInHand()) == Timelord.GUARD) {
                timelord.setTimelordType(TimelordEntity.TimelordType.GUARD);
            } else {
                timelord.setTimelordType(TimelordEntity.TimelordType.COUNCIL);
            }
            RegenCap.get(timelord).ifPresent(iRegen -> {
                timelord.initSkin(iRegen);
                timelord.genName();
                iRegen.setRegens(12);
                CompoundNBT nbt = new CompoundNBT();
                nbt.putFloat(RConstants.PRIMARY_RED, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_GREEN, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.PRIMARY_BLUE, random.nextInt(255) / 255.0F);

                nbt.putFloat(RConstants.SECONDARY_RED, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_GREEN, random.nextInt(255) / 255.0F);
                nbt.putFloat(RConstants.SECONDARY_BLUE, random.nextInt(255) / 255.0F);
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
            return this == GUARD ? random.nextBoolean() : isMale;
        }
    }

}