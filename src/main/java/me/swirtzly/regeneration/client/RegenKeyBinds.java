package me.swirtzly.regeneration.client;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.network.MessageTriggerForcedRegen;
import me.swirtzly.regeneration.network.MessageTriggerRegeneration;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.ClientUtil;
import me.swirtzly.regeneration.util.EnumCompatModids;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

import static me.swirtzly.regeneration.util.PlayerUtil.RegenState.REGENERATING;

/**
 * Created by Sub
 * on 17/09/2018.
 */
@EventBusSubscriber(Side.CLIENT)
public class RegenKeyBinds {
    public static KeyBinding REGEN_NOW;
    public static KeyBinding REGEN_FORCEFULLY;

    public static void init() {

        REGEN_NOW = new KeyBinding("regeneration.keybinds.regenerate", Keyboard.KEY_R, RegenerationMod.NAME);
        ClientRegistry.registerKeyBinding(REGEN_NOW);

        REGEN_FORCEFULLY = new KeyBinding("regeneration.keybinds.regenerate_forced", Keyboard.KEY_Y, RegenerationMod.NAME);
        ClientRegistry.registerKeyBinding(REGEN_FORCEFULLY);
    }


    @SubscribeEvent
    public static void keyInput(InputUpdateEvent e) {
        handleGeneralInputs(e);
    }


    public static void handleGeneralInputs(InputUpdateEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (player == null) return;

        //If Lucraft isn't installed, we get our stuff
        if (Minecraft.getMinecraft().currentScreen == null && !EnumCompatModids.LCCORE.isLoaded()) {
            ClientUtil.keyBind = RegenKeyBinds.getRegenerateNowDisplayName();
        }

        //Regenerate if in Grace & Keybind is pressed
        if (REGEN_NOW.isPressed() && CapabilityRegeneration.getForPlayer(player).getState().isGraceful()) {
            NetworkHandler.INSTANCE.sendToServer(new MessageTriggerRegeneration(player));
        }

        //
        if (RegenKeyBinds.REGEN_FORCEFULLY.isPressed()) {
            NetworkHandler.INSTANCE.sendToServer(new MessageTriggerForcedRegen());
        }


        IRegeneration cap = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player);
        if (cap.getState() == REGENERATING || cap.isSyncingToJar()) { // locking user
            MovementInput moveType = e.getMovementInput();
            moveType.rightKeyDown = false;
            moveType.leftKeyDown = false;
            moveType.backKeyDown = false;
            moveType.jump = false;
            moveType.moveForward = 0.0F;
            moveType.sneak = false;
            moveType.moveStrafe = 0.0F;
        }

    }


    /**
     * Handles LCCore compatibility
     */
    public static String getRegenerateNowDisplayName() {
        return REGEN_NOW.getDisplayName();
    }

}
