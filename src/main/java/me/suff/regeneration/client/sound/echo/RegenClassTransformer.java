package me.suff.regeneration.client.sound.echo;

import me.suff.regeneration.RegenerationMod;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

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
		
		if (transformedName.equals("net.minecraft.client.model.ModelBiped"))
			return patchModelBiped(name, data, false);
		
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
					instructions.add(new MethodInsnNode(INVOKESTATIC, "me/suff/regeneration/client/sound/echo/SoundReverbHandler", "onPlaySound", "(I)V", false));
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
	
	public static byte[] patchModelBiped(String name, byte[] bytes, boolean obf) {
		String renderMethod = RegenerationMod.isDevEnv() ? "render" : "func_78088_a";
		String renderDesc = "(Lnet/minecraft/entity/Entity;FFFFFF)V";
		
		String setRotationAnglesMethod = RegenerationMod.isDevEnv() ? "setRotationAngles" : "func_78087_a";
		String setRotationAnglesDesc = "(FFFFFFLnet/minecraft/entity/Entity;)V";
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		InsnList list = new InsnList();
		
		for (int j = 0; j < classNode.methods.size(); j++) {
			MethodNode method = classNode.methods.get(j);
			if (renderMethod.equals(method.name) && renderDesc.equals(method.desc)) {
				for (int i = 0; i < method.instructions.size(); ++i) {
					AbstractInsnNode node = method.instructions.get(i);
					if (node instanceof MethodInsnNode) {
						MethodInsnNode methodNode = (MethodInsnNode) node;
						
						if (methodNode.name.equals(setRotationAnglesMethod) && methodNode.desc.equals(setRotationAnglesDesc)) {
							list.add(node);
							list.add(new VarInsnNode(ALOAD, 0));
							list.add(new VarInsnNode(ALOAD, 1));
							list.add(new VarInsnNode(FLOAD, 2));
							list.add(new VarInsnNode(FLOAD, 3));
							list.add(new VarInsnNode(FLOAD, 4));
							list.add(new VarInsnNode(FLOAD, 5));
							list.add(new VarInsnNode(FLOAD, 6));
							list.add(new VarInsnNode(FLOAD, 7));
							list.add(new MethodInsnNode(INVOKESTATIC, "me/suff/regeneration/client/sound/echo/RegenClassTransformer", "renderBipedPre", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;FFFFFF)V", false));
							continue;
						}
					}
					
					if (node.getOpcode() == RETURN) {
						list.add(new VarInsnNode(ALOAD, 0));
						list.add(new VarInsnNode(ALOAD, 1));
						list.add(new VarInsnNode(FLOAD, 2));
						list.add(new VarInsnNode(FLOAD, 3));
						list.add(new VarInsnNode(FLOAD, 4));
						list.add(new VarInsnNode(FLOAD, 5));
						list.add(new VarInsnNode(FLOAD, 6));
						list.add(new VarInsnNode(FLOAD, 7));
						list.add(new MethodInsnNode(INVOKESTATIC, "me/suff/regeneration/client/sound/echo/RegenClassTransformer", "renderBipedPost", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;FFFFFF)V", false));
					}
					
					list.add(node);
				}
				
				method.instructions.clear();
				method.instructions.add(list);
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	public static void renderBipedPre(ModelBiped model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entity == null)
			return;
		AnimationEvent.SetRotationAngels ev = new AnimationEvent.SetRotationAngels(entity, model, f, f1, f2, f3, f4, f5, AnimationEvent.ModelSetRotationAnglesEventType.PRE);
		MinecraftForge.EVENT_BUS.post(ev);
		
		if (!ev.isCanceled()) {
			model.setRotationAngles(ev.limbSwing, ev.limbSwingAmount, ev.partialTicks, ev.ageInTicks, ev.netHeadYaw, ev.headPitch, ev.getEntity());
		}
	}
	
	public static void renderBipedPost(ModelBiped model, Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entity == null)
			return;
		AnimationEvent.SetRotationAngels ev = new AnimationEvent.SetRotationAngels(entity, model, f, f1, f2, f3, f4, f5, AnimationEvent.ModelSetRotationAnglesEventType.POST);
		MinecraftForge.EVENT_BUS.post(ev);
	}
	
}