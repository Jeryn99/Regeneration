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
                "class": "net.minecraft.client.renderer.entity.model.BipedModel",
                "methodName": "func_78088_a",
                "methodDesc": "(Lnet/minecraft/entity/LivingEntity;FFFFFF)V"
            },
            "transformer": function (methodNode) {
                patchAngles(methodNode);
                return methodNode;
            }
        }
    }
}


function patchAngles(methodNode){
    var instructions = methodNode.instructions;

    var setRotationAngles_name = ASMAPI.mapMethod("func_212844_a_"); // BipedModel.setRotationAngles

    var arrayLength = instructions.size();
    for (var i = 0; i < arrayLength; ++i) {
        var instruction = instructions.get(i);

        if (instruction.name === setRotationAngles_name) {
            var postInstructions = new InsnList();

            // Make list of instructions to inject
            postInstructions.add(new LabelNode());
            postInstructions.add(new VarInsnNode(ALOAD, 0)); // this
            postInstructions.add(new VarInsnNode(ALOAD, 1)); // entity
            postInstructions.add(new VarInsnNode(FLOAD, 2)); // limbSwing
            postInstructions.add(new VarInsnNode(FLOAD, 3)); // limbSwingAmount
            postInstructions.add(new VarInsnNode(FLOAD, 4)); // ageInTicks
            postInstructions.add(new VarInsnNode(FLOAD, 5)); // netHeadYaw
            postInstructions.add(new VarInsnNode(FLOAD, 6)); // headPitch
            postInstructions.add(new VarInsnNode(FLOAD, 7)); // scale
            postInstructions.add(new MethodInsnNode(
                //int opcode
                INVOKESTATIC,
                //String owner
                "me/swirtzly/animateme/AMHooks",
                //String name
                "renderBipedPost",
                //String descriptor
                "(Lnet/minecraft/client/renderer/entity/model/BipedModel;Lnet/minecraft/entity/LivingEntity;FFFFFF)V",
                //boolean isInterface
                false
            ));

            // Inject instructions
            instructions.insert(instruction, postInstructions);

        }

    }
}


// 1) find first INVOKEVIRTUAL BipedModel.setRotationAngles
// 2) find previous label
// 3) insert right after previous label
// 4) insert right after INVOKEVIRTUAL BipedModel.setRotationAngles
function inject_setRotationAngles_Pre_Post_Hooks(methodNode) {

    var instructions = methodNode.instructions;

    var setRotationAngles_name = ASMAPI.mapMethod("func_212844_a_"); // BipedModel.setRotationAngles

    var first_INVOKEVIRTUAL_setRotationAngles;
    var arrayLength = instructions.size();
    for (var i = 0; i < arrayLength; ++i) {
        var instruction = instructions.get(i);
        if (instruction.getOpcode() == INVOKEVIRTUAL) {
            if (instruction.owner == "net/minecraft/client/renderer/entity/model/BipedModel") {
                if (instruction.name == setRotationAngles_name) {
                    print("instruction.desc: " + instruction.desc);
                    if (instruction.desc == "(Lnet/minecraft/entity/LivingEntity;FFFFFF)V") {
                        if (instruction.itf == false) {
                            first_INVOKEVIRTUAL_setRotationAngles = instruction;
                            print("Found injection point \"BipedModel.setRotationAngles\" " + instruction);
                            break;
                        }
                    }
                }
            }
        }
    }
    if (!first_INVOKEVIRTUAL_setRotationAngles) {
        throw "Error: Couldn't find injection point \"BipedModel.setRotationAngles\"!";
    }

    var firstLabelBefore_first_INVOKEVIRTUAL_setRotationAngles;
    for (i = instructions.indexOf(first_INVOKEVIRTUAL_setRotationAngles); i >= 0; --i) {
        var instruction = instructions.get(i);
        if (instruction.getType() == LABEL) {
            firstLabelBefore_first_INVOKEVIRTUAL_setRotationAngles = instruction;
            print("Found label \"firstLabelBefore_first_INVOKEVIRTUAL_setRotationAngles\" " + instruction);
            break;
        }
    }
    if (!firstLabelBefore_first_INVOKEVIRTUAL_setRotationAngles) {
        throw "Error: Couldn't find label \"firstLabelBefore_first_INVOKEVIRTUAL_setRotationAngles\"!";
    }

    {
        var preInstructions = new InsnList();

        // Labels n stuff
        var originalInstructionsLabel = new LabelNode();

        // Make list of instructions to inject
        preInstructions.add(new VarInsnNode(ALOAD, 0)); // this
        preInstructions.add(new VarInsnNode(ALOAD, 1)); // entity
        preInstructions.add(new VarInsnNode(FLOAD, 2)); // limbSwing
        preInstructions.add(new VarInsnNode(FLOAD, 3)); // limbSwingAmount
        preInstructions.add(new VarInsnNode(FLOAD, 4)); // ageInTicks
        preInstructions.add(new VarInsnNode(FLOAD, 5)); // netHeadYaw
        preInstructions.add(new VarInsnNode(FLOAD, 6)); // headPitch
        preInstructions.add(new VarInsnNode(FLOAD, 7)); // scale
        preInstructions.add(new MethodInsnNode(
            //int opcode
            INVOKESTATIC,
            //String owner
            "me/swirtzly/animateme/AMHooks",
            //String name
            "renderBipedPre",
            //String descriptor
            "(Lnet/minecraft/client/renderer/entity/model/BipedModel;Lnet/minecraft/entity/LivingEntity;FFFFFF)V",
            //boolean isInterface
            false
        ));

        preInstructions.add(originalInstructionsLabel);

        // Inject instructions
        instructions.insert(firstLabelBefore_first_INVOKEVIRTUAL_setRotationAngles, preInstructions);
    }


    {
        var postInstructions = new InsnList();

        // Make list of instructions to inject
        postInstructions.add(new LabelNode());
        postInstructions.add(new VarInsnNode(ALOAD, 0)); // this
        postInstructions.add(new VarInsnNode(ALOAD, 1)); // entity
        postInstructions.add(new VarInsnNode(FLOAD, 2)); // limbSwing
        postInstructions.add(new VarInsnNode(FLOAD, 3)); // limbSwingAmount
        postInstructions.add(new VarInsnNode(FLOAD, 4)); // ageInTicks
        postInstructions.add(new VarInsnNode(FLOAD, 5)); // netHeadYaw
        postInstructions.add(new VarInsnNode(FLOAD, 6)); // headPitch
        postInstructions.add(new VarInsnNode(FLOAD, 7)); // scale
        postInstructions.add(new MethodInsnNode(
            //int opcode
            INVOKESTATIC,
            //String owner
            "me/swirtzly/animateme/AMHooks",
            //String name
            "renderBipedPost",
            //String descriptor
            "(Lnet/minecraft/client/renderer/entity/model/BipedModel;Lnet/minecraft/entity/LivingEntity;FFFFFF)V",
            //boolean isInterface
            false
        ));

        // Inject instructions
        instructions.insert(first_INVOKEVIRTUAL_setRotationAngles, postInstructions);
    }

}