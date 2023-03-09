package mc.craig.software.regen.network;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.network.messages.*;
import net.minecraft.resources.ResourceLocation;

public class RegenNetwork {

    public static final NetworkManager NETWORK = NetworkManager.create(new ResourceLocation(Regeneration.MOD_ID, "channel"));
    public static MessageType TOGGLE_TRAIT, TRANSITION_TYPE, UPLOAD_SKIN, SET_NEXT_SKIN, CHANGE_MODEL, UPDATE_POV, REMOVE_LOCAL_SKIN, PLAY_SFX, UPDATE_LOCAL_STATE, REMOVE_LOCAL_TIMELORD_SKIN, SYNC_CAP, CHANGE_SOUNDSCHEME, COLOR_CHANGE, FORCE_REGENERATION;

    public static void init() {
        UPDATE_POV = NETWORK.registerS2C("update_pov", POVMessage::new);
        REMOVE_LOCAL_SKIN = NETWORK.registerS2C("remove_local_skin", RemoveSkinPlayerMessage::new);
        PLAY_SFX = NETWORK.registerS2C("play_sfx", SFXMessage::new);
        UPDATE_LOCAL_STATE = NETWORK.registerS2C("update_local_state", StateMessage::new);
        REMOVE_LOCAL_TIMELORD_SKIN = NETWORK.registerS2C("remove_local_timelord_skin", RemoveTimelordSkinMessage::new);
        SYNC_CAP = NETWORK.registerS2C("sync_cap", SyncMessage::new);
        CHANGE_SOUNDSCHEME = NETWORK.registerC2S("change_soundscheme", ChangeSoundScheme::new);
        COLOR_CHANGE = NETWORK.registerC2S("color_change", ColorChangeMessage::new);
        FORCE_REGENERATION = NETWORK.registerC2S("force_regeneration", ForceRegenMessage::new);
        CHANGE_MODEL = NETWORK.registerC2S("change_model", ModelMessage::new);
        SET_NEXT_SKIN = NETWORK.registerC2S("set_next_skin", NextSkinMessage::new);
        UPLOAD_SKIN = NETWORK.registerC2S("upload_skin", SkinMessage::new);
        TRANSITION_TYPE = NETWORK.registerC2S("transition_type", TypeMessage::new);
        TOGGLE_TRAIT = NETWORK.registerC2S("toggle_trait", ToggleTraitMessage::new);
    }

}
