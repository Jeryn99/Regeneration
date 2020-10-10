package me.swirtzly.regen.common.objects;

import me.swirtzly.regen.common.tiles.JarTile;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RTiles {

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, RConstants.MODID);

    public static final RegistryObject<TileEntityType<JarTile>> HAND_JAR = TILES.register("hand_jar", () -> registerTiles(JarTile::new, RBlocks.BIO_CONTAINER.get()));


    private static <T extends TileEntity> TileEntityType<T> registerTiles(Supplier<T> tile, Block... validBlock) {
        return TileEntityType.Builder.create(tile, validBlock).build(null);
    }

}
