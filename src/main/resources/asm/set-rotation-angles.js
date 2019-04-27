function initializeCoreMod() {
	return {
		'coremodone': {
			'target': {
				'type': 'CLASS',
				'name': 'net.minecraft.client.renderer.entity.model.ModelBiped'
			},
			'transformer': function(classNode) {
			    print("Patching ModelBiped...");
                var methods = classNode.methods;
                var length = methods.length;
                for(var i = 0; i < length; i++) {
                    var method = methods[i];
                    print(method.name);
                    if("render".equals(method.name) || "func_78088_a".equals(method.name)) {
                        edit(method);
                        break;
                    }
                }
				return classNode;
			}
		}
	}
}

var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');

// The starting target instruction to find in the method 'renderItemModelIntoGUI'
var startInstruction = {
    obfName: "func_78088_a",
    name: "render",
    matches: function(s) {
        return s.equals(this.obfName) || s.equals(this.name);
    }
};

// The ending target instruction to find in the method 'renderItemModelIntoGUI'
var endInstruction = {
    obfName: "func_78087_a",
    name: "setRotationAngles",
    matches: function(s) {
        return s.equals(this.obfName) || s.equals(this.name);
    }
};

function edit(method){

     var startTarget;
     var endTarget;

        var instructionsArray = method.instructions.toArray();
         var length = instructionsArray.length;

     // Finds the starting target node
        for (var i = 0; i < length; i++) {
            var instruction = instructionsArray[i];
            if(instruction instanceof MethodInsnNode && startInstruction.matches(instruction.name)) {
                startTarget = instruction;
                print("Found start target " + instruction);
                break;
            }
        }

        // Finds the ending target node
        for (var j = 0; j < length; j++) {
            var instruction = instructionsArray[j];
            if(instruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                if(endInstruction.matches(instruction.name) && instruction.getPrevious().getOpcode() == Opcodes.ALOAD) {
                    endTarget = instruction;
                    print("Found end target " + instruction);
                    break;
                }
            }
        }

    var rotationName = "func_78087_a";
    var rotationNameDev = "setRotationAngles";
    var rotationDesc = "(FFFFFFLnet/minecraft/entity/Entity;)V";

    var arrayLength = method.instructions.size();
    for (var i = 0; i < arrayLength; ++i) {
	var instruction = method.instructions.get(i);
	if(instruction instanceof MethodInsnNode) {
	if(instruction.name == rotationName || instruction.name == rotationNameDev)
	    print("Found Rotation entry!");

    var toInject = ASMAPI.getMethodNode().instructions;
    var originalInstructionsLabel = new LabelNode();

    toInject.add(new VarInsnNode(ALOAD, 0));
    toInject.add(new VarInsnNode(ALOAD, 1));
    toInject.add(new VarInsnNode(FLOAD, 2));
    toInject.add(new VarInsnNode(FLOAD, 3));
    toInject.add(new VarInsnNode(FLOAD, 4));
    toInject.add(new VarInsnNode(FLOAD, 5));
    toInject.add(new VarInsnNode(FLOAD, 6));
    toInject.add(new VarInsnNode(FLOAD, 7));
    toInject.add(new MethodInsnNode(INVOKESTATIC, "me/suff/regeneration/client/RegenClientHooks", "renderBipedPre", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;FFFFFF)V", false));

    toInject.add(originalInstructionsLabel);

    if(instruction.getOpCode() == RETURN){
        toInject.add(new VarInsnNode(ALOAD, 0));
        toInject.add(new VarInsnNode(ALOAD, 1));
        toInject.add(new VarInsnNode(FLOAD, 2));
        toInject.add(new VarInsnNode(FLOAD, 3));
        toInject.add(new VarInsnNode(FLOAD, 4));
        toInject.add(new VarInsnNode(FLOAD, 5));
        toInject.add(new VarInsnNode(FLOAD, 6));
        toInject.add(new VarInsnNode(FLOAD, 7));
        toInject.add(new MethodInsnNode(INVOKESTATIC, "me/suff/regeneration/client/RegenClientHooks", "renderBipedPost", "(Lnet/minecraft/client/model/ModelBiped;Lnet/minecraft/entity/Entity;FFFFFF)V", false));
    }

    insertInstructions(method, startInstruction, toInject);
    insertInstructions(method, endInstruction, toInject);

	}

	}
}


/* At the time of writing this core mod InsnList class access has not been added. Instead a simple
 * array that inserts the instructions in reverse will solve the problem for now. */
function insertInstructions(method, target, instructions) {
    var length = instructions.length;
    for(var i = length - 1; i >= 0; i--) {
        method.instructions.insert(target, instructions[i]);
    }
}


// Access flags values, defined in
// - https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.1-200-E.1
// - https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.5-200-A.1
// - https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.6-200-A.1
// - https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.25
var ACC_PUBLIC = Opcodes.ACC_PUBLIC; // class, field, method
var ACC_PRIVATE = Opcodes.ACC_PRIVATE; // class, field, method
var ACC_PROTECTED = Opcodes.ACC_PROTECTED; // class, field, method
var ACC_STATIC = Opcodes.ACC_STATIC; // field, method
var ACC_FINAL = Opcodes.ACC_FINAL; // class, field, method, parameter
var ACC_SUPER = Opcodes.ACC_SUPER; // class
var ACC_SYNCHRONIZED = Opcodes.ACC_SYNCHRONIZED; // method
var ACC_OPEN = Opcodes.ACC_OPEN; // module
var ACC_TRANSITIVE = Opcodes.ACC_TRANSITIVE; // module requires
var ACC_VOLATILE = Opcodes.ACC_VOLATILE; // field
var ACC_BRIDGE = Opcodes.ACC_BRIDGE; // method
var ACC_STATIC_PHASE = Opcodes.ACC_STATIC_PHASE; // module requires
var ACC_VARARGS = Opcodes.ACC_VARARGS; // method
var ACC_TRANSIENT = Opcodes.ACC_TRANSIENT; // field
var ACC_NATIVE = Opcodes.ACC_NATIVE; // method
var ACC_INTERFACE = Opcodes.ACC_INTERFACE; // class
var ACC_ABSTRACT = Opcodes.ACC_ABSTRACT; // class, method
var ACC_STRICT = Opcodes.ACC_STRICT; // method
var ACC_SYNTHETIC = Opcodes.ACC_SYNTHETIC; // class, field, method, parameter, module *
var ACC_ANNOTATION = Opcodes.ACC_ANNOTATION; // class
var ACC_ENUM = Opcodes.ACC_ENUM; // class(?) field inner
var ACC_MANDATED = Opcodes.ACC_MANDATED; // parameter, module, module *
var ACC_MODULE = Opcodes.ACC_MODULE; // class

// ASM specific access flags.
// WARNING: the 16 least significant bits must NOT be used, to avoid conflicts with standard
// access flags, and also to make sure that these flags are automatically filtered out when
// written in class files (because access flags are stored using 16 bits only).

var ACC_DEPRECATED = Opcodes.ACC_DEPRECATED; // class, field, method

// Possible values for the type operand of the NEWARRAY instruction.
// See https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-6.html#jvms-6.5.newarray.

var T_BOOLEAN = Opcodes.T_BOOLEAN;
var T_CHAR = Opcodes.T_CHAR;
var T_FLOAT = Opcodes.T_FLOAT;
var T_DOUBLE = Opcodes.T_DOUBLE;
var T_BYTE = Opcodes.T_BYTE;
var T_SHORT = Opcodes.T_SHORT;
var T_INT = Opcodes.T_INT;
var T_LONG = Opcodes.T_LONG;

// Possible values for the reference_kind field of CONSTANT_MethodHandle_info structures.
// See https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.4.8.

var H_GETFIELD = Opcodes.H_GETFIELD;
var H_GETSTATIC = Opcodes.H_GETSTATIC;
var H_PUTFIELD = Opcodes.H_PUTFIELD;
var H_PUTSTATIC = Opcodes.H_PUTSTATIC;
var H_INVOKEVIRTUAL = Opcodes.H_INVOKEVIRTUAL;
var H_INVOKESTATIC = Opcodes.H_INVOKESTATIC;
var H_INVOKESPECIAL = Opcodes.H_INVOKESPECIAL;
var H_NEWINVOKESPECIAL = Opcodes.H_NEWINVOKESPECIAL;
var H_INVOKEINTERFACE = Opcodes.H_INVOKEINTERFACE;

// ASM specific stack map frame types, used in {@link ClassVisitor#visitFrame}.

/** An expanded frame. See {@link ClassReader#EXPAND_FRAMES}. */
var F_NEW = Opcodes.F_NEW;

/** A compressed frame with complete frame data. */
var F_FULL = Opcodes.F_FULL;

/**
 * A compressed frame where locals are the same as the locals in the previous frame, except that
 * additional 1-3 locals are defined, and with an empty stack.
 */
var F_APPEND = Opcodes.F_APPEND;

/**
 * A compressed frame where locals are the same as the locals in the previous frame, except that
 * the last 1-3 locals are absent and with an empty stack.
 */
var F_CHOP = Opcodes.F_CHOP;

/**
 * A compressed frame with exactly the same locals as the previous frame and with an empty stack.
 */
var F_SAME = Opcodes.F_SAME;

/**
 * A compressed frame with exactly the same locals as the previous frame and with a single value
 * on the stack.
 */
var F_SAME1 = Opcodes.F_SAME1;

// The JVM opcode values (with the MethodVisitor method name used to visit them in comment, and
// where '-' means 'same method name as on the previous line').
// See https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-6.html.

var NOP = Opcodes.NOP; // visitInsn
var ACONST_NULL = Opcodes.ACONST_NULL; // -
var ICONST_M1 = Opcodes.ICONST_M1; // -
var ICONST_0 = Opcodes.ICONST_0; // -
var ICONST_1 = Opcodes.ICONST_1; // -
var ICONST_2 = Opcodes.ICONST_2; // -
var ICONST_3 = Opcodes.ICONST_3; // -
var ICONST_4 = Opcodes.ICONST_4; // -
var ICONST_5 = Opcodes.ICONST_5; // -
var LCONST_0 = Opcodes.LCONST_0; // -
var LCONST_1 = Opcodes.LCONST_1; // -
var FCONST_0 = Opcodes.FCONST_0; // -
var FCONST_1 = Opcodes.FCONST_1; // -
var FCONST_2 = Opcodes.FCONST_2; // -
var DCONST_0 = Opcodes.DCONST_0; // -
var DCONST_1 = Opcodes.DCONST_1; // -
var BIPUSH = Opcodes.BIPUSH; // visitIntInsn
var SIPUSH = Opcodes.SIPUSH; // -
var LDC = Opcodes.LDC; // visitLdcInsn
var ILOAD = Opcodes.ILOAD; // visitVarInsn
var LLOAD = Opcodes.LLOAD; // -
var FLOAD = Opcodes.FLOAD; // -
var DLOAD = Opcodes.DLOAD; // -
var ALOAD = Opcodes.ALOAD; // -
var IALOAD = Opcodes.IALOAD; // visitInsn
var LALOAD = Opcodes.LALOAD; // -
var FALOAD = Opcodes.FALOAD; // -
var DALOAD = Opcodes.DALOAD; // -
var AALOAD = Opcodes.AALOAD; // -
var BALOAD = Opcodes.BALOAD; // -
var CALOAD = Opcodes.CALOAD; // -
var SALOAD = Opcodes.SALOAD; // -
var ISTORE = Opcodes.ISTORE; // visitVarInsn
var LSTORE = Opcodes.LSTORE; // -
var FSTORE = Opcodes.FSTORE; // -
var DSTORE = Opcodes.DSTORE; // -
var ASTORE = Opcodes.ASTORE; // -
var IASTORE = Opcodes.IASTORE; // visitInsn
var LASTORE = Opcodes.LASTORE; // -
var FASTORE = Opcodes.FASTORE; // -
var DASTORE = Opcodes.DASTORE; // -
var AASTORE = Opcodes.AASTORE; // -
var BASTORE = Opcodes.BASTORE; // -
var CASTORE = Opcodes.CASTORE; // -
var SASTORE = Opcodes.SASTORE; // -
var POP = Opcodes.POP; // -
var POP2 = Opcodes.POP2; // -
var DUP = Opcodes.DUP; // -
var DUP_X1 = Opcodes.DUP_X1; // -
var DUP_X2 = Opcodes.DUP_X2; // -
var DUP2 = Opcodes.DUP2; // -
var DUP2_X1 = Opcodes.DUP2_X1; // -
var DUP2_X2 = Opcodes.DUP2_X2; // -
var SWAP = Opcodes.SWAP; // -
var IADD = Opcodes.IADD; // -
var LADD = Opcodes.LADD; // -
var FADD = Opcodes.FADD; // -
var DADD = Opcodes.DADD; // -
var ISUB = Opcodes.ISUB; // -
var LSUB = Opcodes.LSUB; // -
var FSUB = Opcodes.FSUB; // -
var DSUB = Opcodes.DSUB; // -
var IMUL = Opcodes.IMUL; // -
var LMUL = Opcodes.LMUL; // -
var FMUL = Opcodes.FMUL; // -
var DMUL = Opcodes.DMUL; // -
var IDIV = Opcodes.IDIV; // -
var LDIV = Opcodes.LDIV; // -
var FDIV = Opcodes.FDIV; // -
var DDIV = Opcodes.DDIV; // -
var IREM = Opcodes.IREM; // -
var LREM = Opcodes.LREM; // -
var FREM = Opcodes.FREM; // -
var DREM = Opcodes.DREM; // -
var INEG = Opcodes.INEG; // -
var LNEG = Opcodes.LNEG; // -
var FNEG = Opcodes.FNEG; // -
var DNEG = Opcodes.DNEG; // -
var ISHL = Opcodes.ISHL; // -
var LSHL = Opcodes.LSHL; // -
var ISHR = Opcodes.ISHR; // -
var LSHR = Opcodes.LSHR; // -
var IUSHR = Opcodes.IUSHR; // -
var LUSHR = Opcodes.LUSHR; // -
var IAND = Opcodes.IAND; // -
var LAND = Opcodes.LAND; // -
var IOR = Opcodes.IOR; // -
var LOR = Opcodes.LOR; // -
var IXOR = Opcodes.IXOR; // -
var LXOR = Opcodes.LXOR; // -
var IINC = Opcodes.IINC; // visitIincInsn
var I2L = Opcodes.I2L; // visitInsn
var I2F = Opcodes.I2F; // -
var I2D = Opcodes.I2D; // -
var L2I = Opcodes.L2I; // -
var L2F = Opcodes.L2F; // -
var L2D = Opcodes.L2D; // -
var F2I = Opcodes.F2I; // -
var F2L = Opcodes.F2L; // -
var F2D = Opcodes.F2D; // -
var D2I = Opcodes.D2I; // -
var D2L = Opcodes.D2L; // -
var D2F = Opcodes.D2F; // -
var I2B = Opcodes.I2B; // -
var I2C = Opcodes.I2C; // -
var I2S = Opcodes.I2S; // -
var LCMP = Opcodes.LCMP; // -
var FCMPL = Opcodes.FCMPL; // -
var FCMPG = Opcodes.FCMPG; // -
var DCMPL = Opcodes.DCMPL; // -
var DCMPG = Opcodes.DCMPG; // -
var IFEQ = Opcodes.IFEQ; // visitJumpInsn
var IFNE = Opcodes.IFNE; // -
var IFLT = Opcodes.IFLT; // -
var IFGE = Opcodes.IFGE; // -
var IFGT = Opcodes.IFGT; // -
var IFLE = Opcodes.IFLE; // -
var IF_ICMPEQ = Opcodes.IF_ICMPEQ; // -
var IF_ICMPNE = Opcodes.IF_ICMPNE; // -
var IF_ICMPLT = Opcodes.IF_ICMPLT; // -
var IF_ICMPGE = Opcodes.IF_ICMPGE; // -
var IF_ICMPGT = Opcodes.IF_ICMPGT; // -
var IF_ICMPLE = Opcodes.IF_ICMPLE; // -
var IF_ACMPEQ = Opcodes.IF_ACMPEQ; // -
var IF_ACMPNE = Opcodes.IF_ACMPNE; // -
var GOTO = Opcodes.GOTO; // -
var JSR = Opcodes.JSR; // -
var RET = Opcodes.RET; // visitVarInsn
var TABLESWITCH = Opcodes.TABLESWITCH; // visiTableSwitchInsn
var LOOKUPSWITCH = Opcodes.LOOKUPSWITCH; // visitLookupSwitch
var IRETURN = Opcodes.IRETURN; // visitInsn
var LRETURN = Opcodes.LRETURN; // -
var FRETURN = Opcodes.FRETURN; // -
var DRETURN = Opcodes.DRETURN; // -
var ARETURN = Opcodes.ARETURN; // -
var RETURN = Opcodes.RETURN; // -
var GETSTATIC = Opcodes.GETSTATIC; // visitFieldInsn
var PUTSTATIC = Opcodes.PUTSTATIC; // -
var GETFIELD = Opcodes.GETFIELD; // -
var PUTFIELD = Opcodes.PUTFIELD; // -
var INVOKEVIRTUAL = Opcodes.INVOKEVIRTUAL; // visitMethodInsn
var INVOKESPECIAL = Opcodes.INVOKESPECIAL; // -
var INVOKESTATIC = Opcodes.INVOKESTATIC; // -
var INVOKEINTERFACE = Opcodes.INVOKEINTERFACE; // -
var INVOKEDYNAMIC = Opcodes.INVOKEDYNAMIC; // visitInvokeDynamicInsn
var NEW = Opcodes.NEW; // visitTypeInsn
var NEWARRAY = Opcodes.NEWARRAY; // visitIntInsn
var ANEWARRAY = Opcodes.ANEWARRAY; // visitTypeInsn
var ARRAYLENGTH = Opcodes.ARRAYLENGTH; // visitInsn
var ATHROW = Opcodes.ATHROW; // -
var CHECKCAST = Opcodes.CHECKCAST; // visitTypeInsn
var INSTANCEOF = Opcodes.INSTANCEOF; // -
var MONITORENTER = Opcodes.MONITORENTER; // visitInsn
var MONITOREXIT = Opcodes.MONITOREXIT; // -
var MULTIANEWARRAY = Opcodes.MULTIANEWARRAY; // visitMultiANewArrayInsn
var IFNULL = Opcodes.IFNULL; // visitJumpInsn
var IFNONNULL = Opcodes.IFNONNULL; // -

