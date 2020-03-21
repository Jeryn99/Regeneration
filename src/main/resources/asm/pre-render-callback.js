function initializeCoreMod() {

    /*Class/Interface*/
    Opcodes = Java.type("org.objectweb.asm.Opcodes");
    /*Class*/
    ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

    /*Class*/
    InsnList = Java.type("org.objectweb.asm.tree.InsnList");
    /*Class*/
    LabelNode = Java.type("org.objectweb.asm.tree.LabelNode");

    /*Class*/
    AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode");
    /*Class*/
    VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
    /*Class*/
    MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");


    INVOKESTATIC = Opcodes.INVOKESTATIC;
    INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL;

    ALOAD = Opcodes.ALOAD;
    FLOAD = Opcodes.FLOAD;

    LABEL = AbstractInsnNode.LABEL;

    return {
        "BipedModel#render": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.client.renderer.entity.LivingRenderer",
                "methodName": "func_188322_c",
                "methodDesc": "(Lnet/minecraft/entity/LivingEntity;F)F"
            },
            "transformer": function (methodNode) {
                patchCallback(methodNode);
                return methodNode;
            }
        }
    }
}


function patchCallback(methodNode) {
    var instructions = methodNode.instructions;

    var callback = ASMAPI.mapMethod("translatef");

    var arrayLength = instructions.size();
    for (var i = 0; i < arrayLength; ++i) {
        var instruction = instructions.get(i);

        if (callback.name === instruction.name) {
            var postInstructions = new InsnList();

            // Make list of instructions to inject
            postInstructions.add(new LabelNode());
            postInstructions.add(new VarInsnNode(ALOAD, 0)); //
            postInstructions.add(new VarInsnNode(ALOAD, 1)); // entity
            postInstructions.add(new MethodInsnNode(
                //int opcode
                INVOKESTATIC,
                //String owner
                "me/swirtzly/animateme/AMHooks",
                //String name
                "preRenderCallBack",
                //String descriptor
                "(Lnet/minecraft/client/renderer/entity/LivingRenderer;Lnet/minecraft/entity/LivingEntity;)V",
                //boolean isInterface
                false
            ));

            // Inject instructions
            instructions.insertBefore(instruction, postInstructions);

        }

    }
}
