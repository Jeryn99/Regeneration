package mc.craig.software.regen.common.regen.transitions;

public class DrinkTransition extends SadFieryTransition {

    public static DrinkTransition INSTANCE = new DrinkTransition();


    @Override
    public int getAnimationLength() {
        return 340;
    }
}
