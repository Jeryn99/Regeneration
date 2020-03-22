package me.swirtzly.regeneration.asm;

import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class RegenClassTransformer implements IClassTransformer, Opcodes {

    private static final String SOURCE_LWJGL_NAME = "paulscode/sound/libraries/SourceLWJGLOpenAL";
    private static final String CHANNEL_LWJGL_NAME = "paulscode/sound/libraries/ChannelLWJGLOpenAL";

    private static final String REGEN_HOOKS_CLASS = "me/swirtzly/regeneration/asm/RegenClientHooks";

    public static byte[] transformSoundSource(byte[] data) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(data);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (method.name.equals("play")) {
                RegenerationPlugin.insertBefore(method.instructions, RegenerationPlugin.invoke(SOURCE_LWJGL_NAME, "checkPitch"::equals), () -> {
                    InsnList instructions = new InsnList();
                    instructions.add(new VarInsnNode(ALOAD, 0));
                    instructions.add(new FieldInsnNode(GETFIELD, SOURCE_LWJGL_NAME, "channelOpenAL", "L" + CHANNEL_LWJGL_NAME + ";"));
                    instructions.add(new FieldInsnNode(GETFIELD, CHANNEL_LWJGL_NAME, "ALSource", "Ljava/nio/IntBuffer;"));
                    instructions.add(new InsnNode(ICONST_0));
                    instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "java/nio/IntBuffer", "get", "(I)I", false));
                    instructions.add(new MethodInsnNode(INVOKESTATIC, "me/swirtzly/regeneration/asm/SoundReverbHandler", "onPlaySound", "(I)V", false));
                    return instructions;
                });
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static byte[] patchShader(byte[] bytes) {
        String shaderMethod = RegenerationMod.isDevEnv() ? "loadEntityShader" : "func_175066_a";
        String shaderMethodDesc = "(Lnet/minecraft/entity/Entity;)V";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        for (int j = 0; j < classNode.methods.size(); j++) {
            MethodNode method = classNode.methods.get(j);
            if (shaderMethod.equals(method.name) && shaderMethodDesc.equals(method.desc)) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode anode = iterator.next();
                    if (anode.getOpcode() == Opcodes.RETURN) {
                        InsnList newInstructions = new InsnList();
                        newInstructions.add(new MethodInsnNode(INVOKESTATIC, REGEN_HOOKS_CLASS, "handleShader", "()V", false));
                        method.instructions.insertBefore(anode, newInstructions);
                    }
                }
            }
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static byte[] patchModelBiped(byte[] bytes) {
        String renderMethod = RegenerationMod.isDevEnv() ? "setRotationAngles" : "func_78087_a";
        String renderDesc = "(FFFFFFLnet/minecraft/entity/Entity;)V";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        // POST
        for (int j = 0; j < classNode.methods.size(); j++) {
            MethodNode method = classNode.methods.get(j);

            if (renderMethod.equals(method.name) && renderDesc.equals(method.desc)) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode anode = iterator.next();
                    if (anode.getOpcode() == Opcodes.RETURN) {
                        InsnList newInstructions = new InsnList();
                        newInstructions.add(new VarInsnNode(ALOAD, 0));
                        newInstructions.add(new VarInsnNode(FLOAD, 1));
                        newInstructions.add(new VarInsnNode(FLOAD, 2));
                        newInstructions.add(new VarInsnNode(FLOAD, 3));
                        newInstructions.add(new VarInsnNode(FLOAD, 4));
                        newInstructions.add(new VarInsnNode(FLOAD, 5));
                        newInstructions.add(new VarInsnNode(FLOAD, 6));
                        newInstructions.add(new VarInsnNode(ALOAD, 7));
                        newInstructions.add(new MethodInsnNode(INVOKESTATIC, REGEN_HOOKS_CLASS, "handleRotations", "(Lnet/minecraft/client/model/ModelBiped;FFFFFFLnet/minecraft/entity/Entity;)V", false));
                        method.instructions.insertBefore(anode, newInstructions);
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    public static byte[] patchRenderLivingBase(byte[] bytes) {
        String targetMethodName = RegenerationMod.isDevEnv() ? "prepareScale" : "func_188322_c";
        String targetScaleMethodname = RegenerationMod.isDevEnv() ? "scale" : "func_179152_a";
        String targetIsnName = "(Lnet/minecraft/entity/EntityLivingBase;F)F";

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        for (int i = 0; i < classNode.methods.size(); i++) {
            MethodNode method = classNode.methods.get(i);

            if (targetMethodName.equals(method.name) && targetIsnName.equals(method.desc)) {
                InsnList insnList = method.instructions;

                for (int j = 0; j < insnList.size(); j++) {
                    AbstractInsnNode insnNote = method.instructions.get(j);

                    if (insnNote.getOpcode() == Opcodes.INVOKESTATIC) {
                        MethodInsnNode method_0 = (MethodInsnNode) insnNote;

                        if (targetScaleMethodname.contains(method_0.name)) {
                            InsnList insnList_0 = new InsnList();
                            insnList_0.add(new VarInsnNode(ALOAD, 0));
                            insnList_0.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            String parameter = "(Lnet/minecraft/client/renderer/entity/RenderLivingBase;Lnet/minecraft/entity/EntityLivingBase;)V";
                            MethodInsnNode method_1 = new MethodInsnNode(Opcodes.INVOKESTATIC, REGEN_HOOKS_CLASS, "preRenderCallBack", parameter, false);
                            insnList_0.add(method_1);
                            insnList.insert(method_0, insnList_0);
                            break;
                        }
                    }
                }
                break;
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchLighting(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        String methodName = RegenerationMod.isDevEnv() ? "updateLightMap" : "func_78472_g";

        MethodNode updateLightmap = null;

        for (MethodNode mn : classNode.methods) {
            if (mn.name.toLowerCase().equals(methodName.toLowerCase())) {
                updateLightmap = mn;
            }
        }

        Float m0 = new Float("0.95");
        Float m3 = new Float("0.96");

        Float a0 = new Float("0.05");
        Float a3 = new Float("0.03");

        if (updateLightmap != null) {
            for (int i = 0; i < updateLightmap.instructions.size(); i++) {
                AbstractInsnNode an = updateLightmap.instructions.get(i);
                if (an instanceof LdcInsnNode) {
                    LdcInsnNode lin = (LdcInsnNode) an;

                    if (lin.cst.equals(m0) || lin.cst.equals(m3)) {
                        updateLightmap.instructions.insert(lin, new MethodInsnNode(Opcodes.INVOKESTATIC, REGEN_HOOKS_CLASS, "up", "(F)F", false));
                    } else if (lin.cst.equals(a0) || lin.cst.equals(a3)) {
                        updateLightmap.instructions.insert(lin, new MethodInsnNode(Opcodes.INVOKESTATIC, REGEN_HOOKS_CLASS, "down", "(F)F", false));
                    }
                } else if (an instanceof FieldInsnNode) {
                    FieldInsnNode fin = (FieldInsnNode) an;

                    if (fin.name.equals(RegenerationMod.isDevEnv() ? "gammaSetting" : "field_74333_Y")) {
                        updateLightmap.instructions.insert(fin, new MethodInsnNode(Opcodes.INVOKESTATIC, REGEN_HOOKS_CLASS, "overrideGamma", "(F)F", false));
                    }
                } else if (an instanceof MethodInsnNode) {
                    MethodInsnNode min = (MethodInsnNode) an;
                    if (min.name.equals(RegenerationMod.isDevEnv() ? "updateDynamicTexture" : "func_110564_a")) {
                        InsnList toInsert = new InsnList();
                        toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        String lightmapColors = RegenerationMod.isDevEnv() ? "lightmapColors" : "field_78504_Q";
                        toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/EntityRenderer", lightmapColors, "[I"));
                        toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, REGEN_HOOKS_CLASS, "modifyLightmap", "([I)[I", false));
                        toInsert.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/EntityRenderer", lightmapColors, "[I"));
                        updateLightmap.instructions.insertBefore(min, toInsert);
                        i += 5;
                    }
                }
            }

        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);

        return patchShader(writer.toByteArray());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] data) {
        if (data == null) {
            return null;
        }

        if (transformedName.equals("paulscode.sound.libraries.SourceLWJGLOpenAL")) {
            return transformSoundSource(data);
        } else if (transformedName.equals("net.minecraft.client.model.ModelBiped")) {
            return patchModelBiped(data);
        } else if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer")) {
            return patchLighting(data);
        }
        if (transformedName.equals("net.minecraft.client.renderer.entity.RenderLivingBase"))
            return patchRenderLivingBase(data);

        return data;
    }
	
}
