package craig.software.mc.regen.common.commands;

import craig.software.mc.regen.common.commands.arguments.TraitsArgumentType;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegenCommands {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, RConstants.MODID);


    public static final RegistryObject<SingletonArgumentInfo<TraitsArgumentType>> TRAIT_COMMAND = COMMAND_ARGUMENT_TYPES.register("trait", () ->
            ArgumentTypeInfos.registerByClass(TraitsArgumentType.class,
                    SingletonArgumentInfo.contextFree(TraitsArgumentType::createArgument)));
}
