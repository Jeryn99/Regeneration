package me.swirtzly.regeneration.common.misc;

import com.google.common.collect.ImmutableSet;
import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.block.Block;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.tardis.mod.Tardis;

import java.lang.reflect.Method;

/**
 * Created by Swirtzly
 * on 19/04/2020 @ 15:46
 */
//@Mod.EventBusSubscriber(modid = RegenerationMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Villager {

    public static VillagerProfession TIMELORD;

    public static PointOfInterestType BIO;

    @SubscribeEvent
    public static void registerTrades(RegistryEvent.Register<VillagerProfession> event) {
        event.getRegistry().registerAll(
                TIMELORD = registerProfesion("timelord", BIO)
        );
    }

    @SubscribeEvent
    public static void registerPOI(RegistryEvent.Register<PointOfInterestType> event) {
        event.getRegistry().registerAll(
                BIO = registerPOI("timelord", RegenObjects.Blocks.HAND_JAR.get())
        );

        try {
            Method m = ObfuscationReflectionHelper.findMethod(PointOfInterestType.class, "func_221052_a", PointOfInterestType.class);
            m.invoke(null, BIO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PointOfInterestType registerPOI(String name, Block block) {
        return new PointOfInterestType("regeneration:" + name, ImmutableSet.copyOf(block.getStateContainer().getValidStates()), 1, null, 1).setRegistryName(Tardis.MODID, name);
    }

    public static VillagerProfession registerProfesion(String name, PointOfInterestType poi) {
        return new VillagerProfession("regeneration:" + name, poi, ImmutableSet.of(), ImmutableSet.of()).setRegistryName(new ResourceLocation(RegenerationMod.MODID, name));
    }

}
