package mc.craig.software.regen.common.regen.acting;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.client.skin.SkinRetriever;
import mc.craig.software.regen.client.skin.VisualManipulator;
import mc.craig.software.regen.common.item.ChaliceItem;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.objects.RSounds;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.network.messages.SkinMessage;
import mc.craig.software.regen.util.ClientUtil;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.io.File;

public class ClientActing implements Acting {

    public static final Acting INSTANCE = new ClientActing();

    @Override
    public void onRegenTick(IRegen cap) {
        // never forwarded as per the documentation
    }

    @Override
    public void onEnterGrace(IRegen cap) {
        if (!Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) return;
            SoundEvent ambientSound = cap.getTimelordSound().getSound();
            ClientUtil.playSound(cap.getLiving(), Registry.SOUND_EVENT.getKey(RSounds.HEART_BEAT.get()), SoundSource.PLAYERS, true, () -> !cap.regenState().isGraceful(), 0.2F, cap.getLiving().getRandom());
            ClientUtil.playSound(cap.getLiving(), Registry.SOUND_EVENT.getKey(ambientSound), SoundSource.AMBIENT, true, () -> cap.regenState() != RegenStates.GRACE, 1.5F, cap.getLiving().getRandom());
    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        if (cap.getLiving().getType() == EntityType.PLAYER) {
            ClientUtil.playSound(cap.getLiving(), Registry.SOUND_EVENT.getKey(RSounds.HAND_GLOW.get()), SoundSource.PLAYERS, true, () -> !cap.glowing(), 1.0F, cap.getLiving().getRandom());
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {
        if (!Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) return;
        TraitBase trait = cap.getCurrentTrait();
        ItemStack chaliceStack = new ItemStack(RItems.GAUNTLET.get());
        ChaliceItem.setTrait(chaliceStack, trait);
        Minecraft.getInstance().gameRenderer.displayItemActivation(chaliceStack);
    }

    @Override
    public void onPerformingPost(IRegen cap) {

    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        if (!Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) return;

            // Don't change skin if player has it disabled locally
            if (!RegenConfig.CLIENT.changeMySkin.get()) {
                VisualManipulator.sendResetMessage();
                return;
            }

            // Set players stored "next skin" if one is stored
            if (cap.isNextSkinValid()) {
                new SkinMessage(cap.nextSkin(), cap.isNextSkinTypeAlex()).send();
                Regeneration.LOGGER.info("Skin chosen from saved data");
                return;
            }

            // Find and send random skin
            Minecraft.getInstance().submit(() -> {
                if (!cap.isNextSkinValid()) {
                    File file = SkinRetriever.chooseRandomSkin(cap.getLiving().getRandom(), cap.getLiving().getType() == REntities.TIMELORD.get(), cap.preferredModel().isAlex());
                    boolean isAlex = file.getAbsolutePath().contains("\\skins\\slim");
                    Regeneration.LOGGER.info("Chosen Skin: " + file);
                    new SkinMessage(RegenUtil.fileToBytes(file), isAlex).send();
                }
            });
    }

    @Override
    public void onGoCritical(IRegen cap) {
        if (Minecraft.getInstance().player.getUUID().equals(cap.getLiving().getUUID())) {
            if (cap.getLiving().getType() == EntityType.PLAYER) {
                ClientUtil.createToast(Component.translatable("regen.toast.enter_critical"), Component.translatable("regen.toast.enter_critical.sub", RegenConfig.COMMON.criticalPhaseLength.get() / 60));
                ClientUtil.playSound(cap.getLiving(), Registry.SOUND_EVENT.getKey(RSounds.CRITICAL_STAGE.get()), SoundSource.PLAYERS, true, () -> cap.regenState() != RegenStates.GRACE_CRIT, 1.0F, RandomSource.create());
            }
        }
    }

}
