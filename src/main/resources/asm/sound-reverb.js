var Opcodes = org.objectweb.asm.Opcodes;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var JumpInsnNode = org.objectweb.asm.tree.JumpInsnNode;
var InsnList = org.objectweb.asm.tree.InsnList;
//Credit to https://github.com/Cryptic-Mushroom/The-Midnight/blob/1.14.4/src/main/resources/midnight_core.js
function initializeCoreMod() {
    return {
        "SoundSourceTransformer": {
            "target": {
                "type": "CLASS",
                "name": "net.minecraft.client.audio.SoundSource"
            },
            "transformer": patch_sound_source
        }
    }
}

function patch_sound_source(class_node) {
    var api = Java.type('net.minecraftforge.coremod.api.ASMAPI');

    var set_pos_method = get_method(class_node, api.mapMethod("func_216420_a"));

    var instructions = set_pos_method.instructions;
    for (var i = 0; i < instructions.size(); i++) {
        var insn = instructions.get(i);
        if (insn.getOpcode() == Opcodes.RETURN) {
            instructions.insertBefore(insn, new VarInsnNode(Opcodes.ALOAD, 0));
            instructions.insertBefore(insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/audio/SoundSource", api.mapField("field_216441_b"), "I"));
            instructions.insertBefore(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, "me/swirtzly/regen/client/sound/SoundReverb", "onPlaySound", "(I)V", false));
            break;
        }
    }
    return class_node;
}

function get_method(class_node, name) {
    for (var index in class_node.methods) {
        var method = class_node.methods[index];
        if (method.name.equals(name)) {
            return method;
        }
    }
    throw "couldn't find method with name '" + name + "' in '" + class_node.name + "'"
}