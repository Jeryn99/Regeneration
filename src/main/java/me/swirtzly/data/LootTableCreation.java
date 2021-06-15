package me.swirtzly.data;

import com.google.gson.*;
import me.swirtzly.regeneration.Regeneration;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

public class LootTableCreation implements IDataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;


    public LootTableCreation(DataGenerator gen) {
        this.generator = gen;
    }

    public static Path getPath(Path path, ResourceLocation rl) {
        return path.resolve("data/" + rl.getNamespace() + "/loot_tables/blocks/" + rl.getPath() + ".json");
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();

        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            if (block.getRegistryName().getNamespace().equalsIgnoreCase(Regeneration.MODID)) {
                this.generateSelfTable(block, cache, path);
            }
        }
    }

    @Override
    public String getName() {
        return "Loot Tables";
    }

    public void generateSelfTable(Block block, DirectoryCache cache, Path base) throws IOException {
        this.generateTable(cache, getPath(base, block.getRegistryName()), () -> this.createBlockDropSelf(block));
    }

    public void generateTable(DirectoryCache cache, Path path, Supplier<JsonElement> element) throws IOException {
        IDataProvider.save(GSON, cache, element.get(), path);
    }

    public JsonElement createBlockDropGuarenteed(Block block, ResourceLocation drop) {
        JsonObject root = new JsonObject();
        root.add("type", new JsonPrimitive("minecraft:block"));

        JsonArray pool = new JsonArray();

        JsonObject first = new JsonObject();

        first.add("name", new JsonPrimitive(block.getRegistryName().toString()));
        first.add("rolls", new JsonPrimitive(1));

        JsonArray entries = new JsonArray();

        JsonObject entry = new JsonObject();

        entry.add("type", new JsonPrimitive("minecraft:item"));
        entry.add("name", new JsonPrimitive(drop.toString()));

        entries.add(entry);

        first.add("entries", entries);

        pool.add(first);

        root.add("pools", pool);

        return root;
    }

    public JsonElement createBlockDropSelf(Block block) {
        return this.createBlockDropGuarenteed(block, block.getRegistryName());
    }

}
