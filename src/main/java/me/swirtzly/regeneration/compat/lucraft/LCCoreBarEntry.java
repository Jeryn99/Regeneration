package me.swirtzly.regeneration.compat.lucraft;

import lucraft.mods.lucraftcore.util.abilitybar.IAbilityBarEntry;
import lucraft.mods.lucraftcore.util.abilitybar.IAbilityBarProvider;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.network.MessageTriggerRegeneration;
import me.swirtzly.regeneration.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class LCCoreBarEntry implements IAbilityBarProvider, IAbilityBarEntry {

    public static final ResourceLocation ICON_TEX = new ResourceLocation(RegenerationMod.MODID, "textures/gui/icons.png");

    @Override
    public boolean isActive() {
        return CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getState().isGraceful();
    }

    @Override
    public void onButtonPress() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (CapabilityRegeneration.getForPlayer(player).getState().isGraceful()) {
            NetworkHandler.INSTANCE.sendToServer(new MessageTriggerRegeneration(player));
        }
    }

    @Override
    public void onButtonRelease() {

    }

    @Override
    public void drawIcon(Minecraft mc, Gui gui, int x, int y) {
        mc.renderEngine.bindTexture(ICON_TEX);
        gui.drawTexturedModalRect(x, y, 9 * 16, 16, 16, 16);
    }

    @Override
    public String getDescription() {
        return "Regenerate!";
    }

    @Override
    public boolean renderCooldown() {
        return false;
    }

    @Override
    public float getCooldownPercentage() {
        return 0;
    }

    @Override
    public Vec3d getCooldownColor() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        return CapabilityRegeneration.getForPlayer(player).getPrimaryColor();
    }

    @Override
    public boolean showKey() {
        return true;
    }

    @Override
    public List<IAbilityBarEntry> getEntries() {
        ArrayList<IAbilityBarEntry> list = new ArrayList<>();
        list.add(new LCCoreBarEntry());
        return list;
    }


}
