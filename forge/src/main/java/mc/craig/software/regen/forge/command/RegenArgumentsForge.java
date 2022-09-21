package mc.craig.software.regen.forge.command;

import mc.craig.software.regen.common.commands.arguments.TraitArgumentType;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegenArgumentsForge {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, RConstants.MODID);


    public static final RegistryObject<SingletonArgumentInfo<TraitArgumentType>> TRAIT_COMMAND = COMMAND_ARGUMENT_TYPES.register("trait", () ->
            ArgumentTypeInfos.registerByClass(TraitArgumentType.class, SingletonArgumentInfo.contextFree(TraitArgumentType::traitArgumentType)));
}