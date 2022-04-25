package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.common.tiles.JarTile;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RTiles {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RConstants.MODID);

    private static <T extends TileEntity> TileEntityType<T> registerTiles(Supplier<T> tile, Block... validBlock) {
        return TileEntityType.Builder.of(tile, validBlock).build(null);
    }    public static final RegistryObject<TileEntityType<JarTile>> HAND_JAR = TILES.register("hand_jar", () -> registerTiles(JarTile::new, RBlocks.BIO_CONTAINER.get()));




}
