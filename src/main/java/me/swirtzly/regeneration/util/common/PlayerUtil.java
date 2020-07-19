package me.swirtzly.regeneration.util.common;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.api.ZeroRoomEvent;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.block.ZeroRoomBlock;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.item.HandItem;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.messages.ThirdPersonMessage;
import me.swirtzly.regeneration.network.messages.UpdateSkinMapMessage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static me.swirtzly.regeneration.util.common.RegenUtil.NO_SKIN;

/**
 * Created by Sub on 20/09/2018.
 */
public class PlayerUtil {

	public static ArrayList<Effect> POTIONS = new ArrayList<>();

    public static void createPostList() {
        for (String potionName : RegenConfig.COMMON.postRegenEffects.get()) {
            Effect potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionName));
            if (potion != null) {
                POTIONS.add(potion);
                System.out.println("ADDED: " + potionName);
            } else {
                Regeneration.LOG.error(potionName + " is not a Valid Potion/Effect! Not adding this time around!");
            }
        }
    }

    public static void lookAt(double px, double py, double pz, PlayerEntity me) {
        double dirx = me.getPosition().getX() - px;
        double diry = me.getPosition().getY() - py;
        double dirz = me.getPosition().getZ() - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        // to degree
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;

        yaw += 90f;
        me.rotationPitch = (float) pitch;
        me.rotationYaw = (float) yaw;
    }

    public static void sendMessage(LivingEntity livingEntity, String message, boolean hotBar) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) livingEntity;
		if (!player.world.isRemote) {
			player.sendStatusMessage(new TranslationTextComponent(message), hotBar);
		}
	}

    public static void sendMessage(LivingEntity livingEntity, TranslationTextComponent translation, boolean hotBar) {
        if (!(livingEntity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) livingEntity;
		if (!player.world.isRemote) {
			player.sendStatusMessage(translation, hotBar);
		}
	}
	
	public static void sendMessageToAll(TranslationTextComponent translation) {
		List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
		players.forEach(playerMP -> sendMessage(playerMP, translation, false));
	}
	
	public static void setPerspective(ServerPlayerEntity player, boolean thirdperson, boolean resetPitch) {
		NetworkDispatcher.sendTo(new ThirdPersonMessage(thirdperson), player);
	}

    public static void updateModel(SkinManipulation.EnumChoices choice) {
		NetworkDispatcher.INSTANCE.sendToServer(new UpdateSkinMapMessage(choice.name()));
	}

    public static boolean applyPotionIfAbsent(LivingEntity player, Effect potion, int length, int amplifier, boolean ambient, boolean showParticles) {
		if (potion == null) return false;
		if (player.getActivePotionEffect(potion) == null) {
			player.addPotionEffect(new EffectInstance(potion, length, amplifier, ambient, showParticles));
			return true;
		}
		return false;
	}

    public static boolean isSharp(ItemStack stack) {
        return stack.getItem() instanceof ToolItem || stack.getItem() instanceof SwordItem;
    }

    public static void createHand(LivingEntity player) {
        RegenCap.get(player).ifPresent((data) -> {
            if (!data.getEncodedSkin().equalsIgnoreCase(NO_SKIN) && player instanceof PlayerEntity) {
                ItemStack hand = new ItemStack(RegenObjects.Items.HAND.get());
                HandItem.setTextureString(hand, data.getEncodedSkin());
                HandItem.setSkinType(hand, data.getSkinType().name());
                HandItem.setOwner(hand, player.getUniqueID());
                HandItem.setTimeCreated(hand, System.currentTimeMillis());
                HandItem.setTrait(hand, data.getDnaType().toString());
                data.setDroppedHand(true);
                data.setCutOffHand(player.getPrimaryHand() == HandSide.LEFT ? HandSide.RIGHT : HandSide.LEFT);
                data.setDroppedHand(true);
                InventoryHelper.spawnItemStack(player.world, player.posX, player.posY, player.posZ, hand);
            }
        });
    }

    public static void handleCutOffhand(LivingEntity player) {
        RegenCap.get(player).ifPresent((data) -> {
            if (data.hasDroppedHand()) {
                if (!player.getHeldItemOffhand().isEmpty()) {
                    player.entityDropItem(player.getHeldItemOffhand());
                    player.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.AIR));
                }
            }
        });
    }

    public static boolean isZeroRoom(LivingEntity livingEntity) {
        AxisAlignedBB box = livingEntity.getBoundingBox().grow(25);
        for (Iterator<BlockPos> iterator = BlockPos.getAllInBox(new BlockPos(box.maxX, box.maxY, box.maxZ), new BlockPos(box.minX, box.minY, box.minZ)).iterator(); iterator.hasNext(); ) {
            BlockPos pos = iterator.next();
            BlockState blockState = livingEntity.world.getBlockState(pos);
            if (blockState.getBlock() instanceof ZeroRoomBlock) {
                boolean isTardis = livingEntity.world.dimension.getClass().getName().contains("TardisDimension");
                if (isTardis) {
                    ZeroRoomEvent zeroRoomEvent = new ZeroRoomEvent(livingEntity);
                    MinecraftForge.EVENT_BUS.post(zeroRoomEvent);
                    if (!zeroRoomEvent.isCanceled()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean isAboveZeroGrid(LivingEntity livingEntity) {
        BlockPos livingPos = livingEntity.getPosition().down();
        AxisAlignedBB grid = new AxisAlignedBB(livingPos.north().west(), livingPos.south().east());
        for (Iterator<BlockPos> iterator = BlockPos.getAllInBox(new BlockPos(grid.maxX, grid.maxY, grid.maxZ), new BlockPos(grid.minX, grid.minY, grid.minZ)).iterator(); iterator.hasNext(); ) {
            BlockPos pos = iterator.next();
            BlockState state = livingEntity.world.getBlockState(pos);
            if (!(state.getBlock() instanceof ZeroRoomBlock)) {
                return false;
            }
        }
        return true;
    }


    public enum RegenState {

        ALIVE, GRACE, GRACE_CRIT, POST, REGENERATING;
		
		public boolean isGraceful() {
			return this == GRACE || this == GRACE_CRIT;
		}
		
		public enum Transition {
            HAND_GLOW_START, HAND_GLOW_TRIGGER, ENTER_CRITICAL, CRITICAL_DEATH, FINISH_REGENERATION, END_POST
        }
	}
}
