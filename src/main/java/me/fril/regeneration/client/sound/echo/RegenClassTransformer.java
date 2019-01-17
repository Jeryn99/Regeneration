package me.fril.regeneration.client.sound.echo;

import me.fril.regeneration.RegenerationMod;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import javax.annotation.Nullable;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.function.Supplier;

//CREDIT https://raw.githubusercontent.com/Cryptic-Mushroom/The-Midnight/13ffab9ab0d78b0d030c2b3b55252e0e8fd2864e/src/main/java/com/mushroom/midnight/core/transformer/MidnightClassTransformer.java
public class RegenClassTransformer implements IClassTransformer, Opcodes {
    private static final String SOURCE_LWJGL_NAME = "paulscode/sound/libraries/SourceLWJGLOpenAL";
    private static final String CHANNEL_LWJGL_NAME = "paulscode/sound/libraries/ChannelLWJGLOpenAL";

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        if (data == null) {
            return null;
        }
        if (transformedName.equals("paulscode.sound.libraries.SourceLWJGLOpenAL")) {
            return this.applyTransform(transformedName, data, this::transformSoundSource);
        }
        return data;
    }

    private boolean transformSoundSource(ClassNode node) {
        for (MethodNode method : node.methods) {
            if (method.name.equals("play")) {
                this.insertBefore(method.instructions, this.invoke(SOURCE_LWJGL_NAME, "checkPitch"::equals), () -> {
                    InsnList instructions = new InsnList();
                    instructions.add(new VarInsnNode(ALOAD, 0));
                    instructions.add(new FieldInsnNode(GETFIELD, SOURCE_LWJGL_NAME, "channelOpenAL", "L" + CHANNEL_LWJGL_NAME + ";"));
                    instructions.add(new FieldInsnNode(GETFIELD, CHANNEL_LWJGL_NAME, "ALSource", "Ljava/nio/IntBuffer;"));
                    instructions.add(new InsnNode(ICONST_0));
                    instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "java/nio/IntBuffer", "get", "(I)I", false));
                    instructions.add(new MethodInsnNode(INVOKESTATIC, "me/fril/regeneration/client/sound/echo/SoundReverbHandler", "onPlaySound", "(I)V", false));
                    return instructions;
                });
                return true;
            }
        }
        return false;
    }

    private byte[] applyTransform(String name, byte[] data, Predicate<ClassNode> transformer) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(data);
        reader.accept(node, 0);

        if (transformer.test(node)) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(writer);

            return writer.toByteArray();
        } else {
            RegenerationMod.LOG.warn("Unable to patch class {}", name);
            return data;
        }
    }

    private void insertBefore(InsnList instructions, Predicate<AbstractInsnNode> predicate, Supplier<InsnList> insert) {
        AbstractInsnNode node = this.selectNode(instructions, predicate);
        if (node != null) {
            instructions.insertBefore(node, insert.get());
        } else {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            RegenerationMod.LOG.warn("Failed to find location to insert for {}", stackTrace[1].getMethodName());
        }
    }

    @Nullable
    private AbstractInsnNode selectNode(InsnList instructions, Predicate<AbstractInsnNode> predicate) {
        ListIterator<AbstractInsnNode> iterator = instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode node = iterator.next();
            if (predicate.test(node)) {
                return node;
            }
        }
        return null;
    }

    private Predicate<AbstractInsnNode> invoke(String owner, Predicate<String> name) {
        return n -> n instanceof MethodInsnNode && ((MethodInsnNode) n).owner.equals(owner) && name.test(((MethodInsnNode) n).name);
    }
}