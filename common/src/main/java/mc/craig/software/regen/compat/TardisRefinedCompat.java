package mc.craig.software.regen.compat;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.acting.Acting;
import mc.craig.software.regen.util.Platform;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;


public class TardisRefinedCompat implements Acting {

    public static void init() {
        if (Platform.isModLoaded("tardis_refined")) {
         //   ActingForwarder.register(new TardisRefinedCompat(), ActingForwarder.Side.COMMON);
        }
    }

    private static void playCloister(ServerLevel serverLevel) {
       /* if(!isTardis(serverLevel)) return;
        for (int i = 0; i < 3; i++) {
            serverLevel.playSound(null, TardisArchitectureHandler.DESKTOP_CENTER_POS, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1000f, 0.1f);
        }*/
    }

    public static boolean isTardis(ServerLevel serverLevel){
        return false;
        //return serverLevel.dimensionTypeId() == DimensionTypes.TARDIS;
    }

    @Override
    public void onEnterGrace(IRegen cap) {

    }

    @Override
    public void onHandsStartGlowing(IRegen cap) {
        if (cap.getLiving().level instanceof ServerLevel serverLevel) {
            playCloister(serverLevel);
        }
    }

    @Override
    public void onGoCritical(IRegen cap) {
        if (cap.getLiving().level instanceof ServerLevel serverLevel) {
            playCloister(serverLevel);
        }
    }

    @Override
    public void onRegenTrigger(IRegen cap) {
        if (cap.getLiving().level instanceof ServerLevel serverLevel) {
            playCloister(serverLevel);
        }
    }

    @Override
    public void onRegenFinish(IRegen cap) {

    }

    @Override
    public void onPerformingPost(IRegen cap) {

    }

    @Override
    public void onRegenTick(IRegen cap) {
        LivingEntity living = cap.getLiving();
        Level level = living.level;

       /* if (cap.regenState() == RegenStates.REGENERATING && level instanceof ServerLevel serverLevel) {
            TardisLevelOperator.get(serverLevel).ifPresent(tardisLevelOperator -> {
                TardisControlManager controlManager = tardisLevelOperator.getControlManager();
                if (controlManager.isInFlight() && controlManager.canUseControls()) {
                    controlManager.crash();
                }

                if(living.tickCount % 1200 == 0) {
                    playCloister(serverLevel);
                }

            });
        }*/
    }
}
