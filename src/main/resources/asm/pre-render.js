function initializeCoreMod() {

    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");

    ALOAD = Opcodes.ALOAD;
    DLOAD = Opcodes.DLOAD;
    FLOAD = Opcodes.FLOAD;
    GETFIELD = Opcodes.GETFIELD;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;
    INVOKESTATIC = Opcodes.INVOKESTATIC;

    return {
        'EntityRendererManager#renderEntityStatic': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.EntityRendererManager',
                'methodName': 'func_229084_a_',
                'methodDesc': '(Lnet/minecraft/entity/Entity;DDDFFLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V'
            },
            'transformer': function (methodNode) {
                var instructions = methodNode.instructions;
                var injectionPoint1 = null;
                var injectionPoint2 = null;
                var matrixStackTranslate_name = ASMAPI.mapField('func_227861_a_');
                var renderShadow_name = ASMAPI.mapField('func_229096_a_');

                for (var i = 0; i < instructions.size(); i++) {
                    var instruction = instructions.get(i);

                    if (instruction.getOpcode() == INVOKEVIRTUAL && (instruction.name == matrixStackTranslate_name || instruction.name == "translate") && !injectionPoint1) {
                        injectionPoint1 = instructions.get(i);
                    }

                    if (instruction.getOpcode() == INVOKESTATIC && (instruction.name == renderShadow_name || instruction.name == "renderShadow") && !injectionPoint2) {
                        injectionPoint2 = instructions.get(i + 1);
                    }
                }

                if (!injectionPoint1 || !injectionPoint2) {
                    print("Was not able to patch EntityRendererManager#renderEntityStatic()!");
                    return methodNode;
                }

                // Hook 1
                {
                    var insertingInstructions = new InsnList();

                    insertingInstructions.add(new VarInsnNode(ALOAD, 1));
                    insertingInstructions.add(new VarInsnNode(ALOAD, 10));
                    insertingInstructions.add(new VarInsnNode(FLOAD, 9));
                    insertingInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "me/swirtzly/regen/client/RenderingASM",
                        //String name
                        "preRenderCallback",
                        //String descriptor
                        "(Lnet/minecraft/entity/Entity;Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
                        //boolean isInterface
                        false
                    ));

                    instructions.insert(injectionPoint1, insertingInstructions);
                }

                // Hook 2
                {
                    var insertingInstructions = new InsnList();

                    insertingInstructions.add(new VarInsnNode(ALOAD, 1));
                    insertingInstructions.add(new VarInsnNode(ALOAD, 10));
                    insertingInstructions.add(new VarInsnNode(FLOAD, 9));
                    insertingInstructions.add(new MethodInsnNode(
                        //int opcode
                        INVOKESTATIC,
                        //String owner
                        "me/swirtzly/regen/client/RenderingASM",
                        //String name
                        "postRenderCallback",
                        //String descriptor
                        "(Lnet/minecraft/entity/Entity;Lcom/mojang/blaze3d/matrix/MatrixStack;F)V",
                        //boolean isInterface
                        false
                    ));

                    instructions.insert(injectionPoint2, insertingInstructions);
                }

                return methodNode;
            }
        }
    }
}
