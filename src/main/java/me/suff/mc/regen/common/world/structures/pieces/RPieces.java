package me.suff.mc.regen.common.world.structures.pieces;

import me.suff.mc.regen.common.world.structures.TimelordSettlementHut;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

public class RPieces {

    public static StructurePieceType setPieceId(StructurePieceType p_67164_, String p_67165_) {
        return Registry.register(Registry.STRUCTURE_PIECE, p_67165_.toLowerCase(Locale.ROOT), p_67164_);
    }

    public static StructurePieceType TIMELORD_HUT = null;


    public static void init() {
        TIMELORD_HUT = setPieceId(TimelordSettlementHut.HutPiece::new, "regen:timelord_hut");
    }
}