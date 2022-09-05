package mc.craig.software.regen.forge;

import mc.craig.software.regen.Regeneration;
import mc.craig.software.regen.common.entities.Timelord;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RSoundSchemes;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.ClientUtil;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Regeneration.MOD_ID)
public class RegenerationForge {
    public RegenerationForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        Regeneration.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RegenConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RegenConfig.CLIENT_SPEC);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::commoNSetup);
        modBus.addListener(this::onAttributeAssign);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ClientUtil.doClientStuff();
    }

    private void commoNSetup(final FMLCommonSetupEvent event) {
        RSoundSchemes.init();
    }


    public void onAttributeAssign(EntityAttributeCreationEvent event) {
        event.put(REntities.TIMELORD.get(), Timelord.createAttributes().build());
    }

}
