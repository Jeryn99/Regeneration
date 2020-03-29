package lucraft.mods.timelords.asm;

import java.util.logging.Logger;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TimelordsClassTransformer implements IClassTransformer, Opcodes {
  private static Logger logger = Logger.getLogger("Lucraft");
  
  public byte[] transform(String name, String transformedName, byte[] classBytes) {
    if (transformedName.equals("net.minecraft.client.renderer.entity.RendererLivingEntity"))
      return patchClassASMRendererEntityLiving(name, classBytes, false); 
    if (transformedName.equals("net.minecraft.client.model.ModelBiped"))
      return patchClassASMModelBiped(name, classBytes, false); 
    return classBytes;
  }
  
  private byte[] patchClassASMModelBiped(String name, byte[] bytes, boolean obf) {
    String renderMethod = TimelordsForgeLoading.runtimeObfuscationEnabled ? "render" : "render";
    String renderDesc = TimelordsForgeLoading.runtimeObfuscationEnabled ? "(Lnet/minecraft/entity/Entity;FFFFFF)V" : "(Lnet/minecraft/entity/Entity;FFFFFF)V";
    String setRotationAnglesMethod = TimelordsForgeLoading.runtimeObfuscationEnabled ? "setRotationAngles" : "setRotationAngles";
    String setRotationAnglesDesc = TimelordsForgeLoading.runtimeObfuscationEnabled ? "(FFFFFFLnet/minecraft/entity/Entity;)V" : "(FFFFFFLnet/minecraft/entity/Entity;)V";
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept((ClassVisitor)classNode, 0);
    InsnList list = new InsnList();
    for (int j = 0; j < classNode.methods.size(); j++) {
      MethodNode method = classNode.methods.get(j);
      if (renderMethod.equals(method.name) && renderDesc.equals(method.desc)) {
        for (int i = 0; i < method.instructions.size(); i++) {
          AbstractInsnNode node = method.instructions.get(i);
          if (node instanceof MethodInsnNode) {
            MethodInsnNode methodNode = (MethodInsnNode)node;
            if (methodNode.name.equals(setRotationAnglesMethod) && methodNode.desc.equals(setRotationAnglesDesc)) {
              list.add(node);
              list.add((AbstractInsnNode)new VarInsnNode(25, 0));
              list.add((AbstractInsnNode)new VarInsnNode(25, 1));
              list.add((AbstractInsnNode)new VarInsnNode(23, 2));
              list.add((AbstractInsnNode)new VarInsnNode(23, 3));
              list.add((AbstractInsnNode)new VarInsnNode(23, 4));
              list.add((AbstractInsnNode)new VarInsnNode(23, 5));
              list.add((AbstractInsnNode)new VarInsnNode(23, 6));
              list.add((AbstractInsnNode)new VarInsnNode(23, 7));
              list.add((AbstractInsnNode)new MethodInsnNode(184, "lucraft/mods/timelords/asm/TimelordsClientHooks", "renderBipedPre", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;FFFFFF)V", false));
              continue;
            } 
          } 
          if (node.getOpcode() == 177) {
            list.add((AbstractInsnNode)new VarInsnNode(25, 0));
            list.add((AbstractInsnNode)new VarInsnNode(25, 1));
            list.add((AbstractInsnNode)new VarInsnNode(23, 2));
            list.add((AbstractInsnNode)new VarInsnNode(23, 3));
            list.add((AbstractInsnNode)new VarInsnNode(23, 4));
            list.add((AbstractInsnNode)new VarInsnNode(23, 5));
            list.add((AbstractInsnNode)new VarInsnNode(23, 6));
            list.add((AbstractInsnNode)new VarInsnNode(23, 7));
            list.add((AbstractInsnNode)new MethodInsnNode(184, "lucraft/mods/timelords/asm/TimelordsClientHooks", "renderBipedPost", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;FFFFFF)V", false));
          } 
          list.add(node);
          continue;
        } 
        method.instructions.clear();
        method.instructions.add(list);
      } 
    } 
    ClassWriter writer = new ClassWriter(3);
    classNode.accept(writer);
    return writer.toByteArray();
  }
  
  public static byte[] patchClassASMRendererEntityLiving(String name, byte[] bytes, boolean obf) {
    String targetMethodName = TimelordsForgeLoading.runtimeObfuscationEnabled ? "doRender" : "doRender";
    String targetScaleMethodname = TimelordsForgeLoading.runtimeObfuscationEnabled ? "scale" : "scale";
    String targetIsnName = TimelordsForgeLoading.runtimeObfuscationEnabled ? "(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V" : "(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V";
    ClassNode classNode = new ClassNode();
    ClassReader classReader = new ClassReader(bytes);
    classReader.accept((ClassVisitor)classNode, 0);
    for (int i = 0; i < classNode.methods.size(); i++) {
      MethodNode method = classNode.methods.get(i);
      if (targetMethodName.equals(method.name) && targetIsnName.equals(method.desc)) {
        InsnList insnList = method.instructions;
        for (int j = 0; j < insnList.size(); j++) {
          AbstractInsnNode insnNote = method.instructions.get(j);
          if (insnNote.getOpcode() == 184) {
            MethodInsnNode method_0 = (MethodInsnNode)insnNote;
            if (targetScaleMethodname.contains(method_0.name)) {
              InsnList insnList_0 = new InsnList();
              insnList_0.add((AbstractInsnNode)new VarInsnNode(25, 1));
              String parameter = "(Lnet/minecraft/entity/EntityLivingBase;)V";
              MethodInsnNode method_1 = new MethodInsnNode(184, "lucraft/mods/timelords/asm/TimelordsClientHooks", "preRenderCallBack", parameter, false);
              insnList_0.add((AbstractInsnNode)method_1);
              insnList.insert((AbstractInsnNode)method_0, insnList_0);
              break;
            } 
          } 
        } 
        break;
      } 
    } 
    ClassWriter writer = new ClassWriter(3);
    classNode.accept(writer);
    return writer.toByteArray();
  }
}
