package me.swirtzly.regen.data;

import me.swirtzly.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class RSoundsGen extends BaseSoundsProvider {

    public RSoundsGen(DataGenerator gen) {
        super(gen, RConstants.MODID);
    }

    @Override
    protected void addSounds() {
        for (SoundEvent soundEvent : ForgeRegistries.SOUND_EVENTS.getValues()) {
            if(soundEvent.getName().getNamespace().contains(RConstants.MODID)){

                ResourceLocation rl = soundEvent.getName();

                if(rl.getPath().contains("regeneration")){
                    String newPath = rl.getPath().replace("regeneration", "regen");
                    rl = new ResourceLocation(RConstants.MODID, "regen/"+newPath);
                }

                addSoundEvent(new SoundEventBuilder(soundEvent).subtitle("regen.sound."+soundEvent.getName().getPath()).addSounds(new SoundBuilder(rl)));
            }
        }
    }
}
