package mc.craig.software.regen.fabric.mixin;

import mc.craig.software.regen.common.item.tooltip.fob.ClientFobTooltip;
import mc.craig.software.regen.common.item.tooltip.fob.FobTooltip;
import mc.craig.software.regen.common.item.tooltip.hand.ClientHandSkinToolTip;
import mc.craig.software.regen.common.item.tooltip.hand.HandSkinToolTip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientTooltipComponent.class)
public interface ClientToolTipComponentMixin {

    @Inject(at = @At("HEAD"), cancellable = true, method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;")
    private static void create(TooltipComponent visualTooltipComponent, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        if (visualTooltipComponent instanceof HandSkinToolTip handSkinToolTip) {
            cir.setReturnValue(new ClientHandSkinToolTip(handSkinToolTip.getSkin(), handSkinToolTip.getModel()));
        }

        if (visualTooltipComponent instanceof FobTooltip fobTooltip) {
            cir.setReturnValue(new ClientFobTooltip(fobTooltip.getRegenerations()));
        }
    }

}
