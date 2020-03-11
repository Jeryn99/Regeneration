package me.swirtzly.regeneration.asm;

import me.swirtzly.regeneration.RegenerationMod;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

import javax.annotation.Nullable;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

@IFMLLoadingPlugin.Name("Regeneration")
@IFMLLoadingPlugin.TransformerExclusions("me.swirtzly.regeneration.asm")
@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class RegenerationPlugin implements IFMLLoadingPlugin {

    // ASM UTILS BELOW
    public static void insertBefore(InsnList instructions, Predicate<AbstractInsnNode> predicate, Supplier<InsnList> insert) {
        AbstractInsnNode node = selectNode(instructions, predicate);
        if (node != null) {
            instructions.insertBefore(node, insert.get());
        } else {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            RegenerationMod.LOG.warn("Failed to find location to insert for {}", stackTrace[1].getMethodName());
        }
    }

    @Nullable
    private static AbstractInsnNode selectNode(InsnList instructions, Predicate<AbstractInsnNode> predicate) {
        ListIterator<AbstractInsnNode> iterator = instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode node = iterator.next();
            if (predicate.test(node)) {
                return node;
            }
        }
        return null;
    }

    public static Predicate<AbstractInsnNode> invoke(String owner, Predicate<String> name) {
        return n -> n instanceof MethodInsnNode && ((MethodInsnNode) n).owner.equals(owner) && name.test(((MethodInsnNode) n).name);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{RegenClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
	
}
