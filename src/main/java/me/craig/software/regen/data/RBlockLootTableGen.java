package me.craig.software.regen.data;

import com.google.gson.*;
import me.craig.software.regen.util.RConstants;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Created by 50ap5ud5 23/3/2021
 */
public class RBlockLootTableGen extends LootTableProvider {


    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private DataGenerator generator;


    public RBlockLootTableGen(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    public static Path getPath(Path base, Block block) {
        ResourceLocation key = block.getRegistryName();
        return base.resolve("data/" + key.getNamespace() + "/loot_tables/blocks/" + key.getPath() + ".json");
    }

    public static Path getPath(Path path, ResourceLocation rl) {
        return path.resolve("data/" + rl.getNamespace() + "/loot_tables/blocks/" + rl.getPath() + ".json");
    }

    @Override
    public void run(DirectoryCache cache) {

        Path path = this.generator.getOutputFolder();

        for (Block block : ForgeRegistries.BLOCKS) {
            if (block.getRegistryName().getNamespace().contentEquals(RConstants.MODID)) {
                if (block.asItem() != null && block.asItem() != Items.AIR) {
                    if (block instanceof SlabBlock) {
                        this.generateSelfSlabTable(block, cache, path);
                    } else {
                        this.generateSelfTable(block, cache, path);
                    }
                }
            }
        }

    }

    public void generateSelfTable(Block block, DirectoryCache cache, Path base) {
        this.generateTable(cache, getPath(base, block.getRegistryName()), () -> this.createSingleDropTable(block.getRegistryName().toString()));
    }

    public void generateSelfSlabTable(Block block, DirectoryCache cache, Path base) {
        this.generateTable(cache, getPath(base, block.getRegistryName()), () -> this.createSlabDropTable(block.getRegistryName().toString()));
    }

    public void generateTable(DirectoryCache cache, Path path, Supplier<JsonElement> element) {
        try {
            IDataProvider.save(GSON, cache, element.get(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Regeneration Block Loot Gen";
    }

    public JsonElement createSingleDropTable(String name) {
        JsonObject root = new JsonObject();

        root.add("type", new JsonPrimitive("minecraft:block"));

        JsonArray pools = new JsonArray();

        JsonObject first = new JsonObject();
        first.add("name", new JsonPrimitive(name));
        first.add("rolls", new JsonPrimitive(1));

        JsonArray entry = new JsonArray();
        JsonObject type = new JsonObject();
        type.add("type", new JsonPrimitive("minecraft:item"));
        type.add("name", new JsonPrimitive(name));
        entry.add(type);
        first.add("entries", entry);

        pools.add(first);

        root.add("pools", pools);

        return root;
    }

    public JsonElement createSlabDropTable(String name) {
        JsonObject root = new JsonObject();

        root.add("type", new JsonPrimitive("minecraft:block"));

        JsonArray pools = new JsonArray();

        JsonObject first = new JsonObject();
        first.add("name", new JsonPrimitive(name));
        first.add("rolls", new JsonPrimitive(1));
        //Entries Start
        JsonArray entry = new JsonArray();

        JsonObject type = new JsonObject();
        type.add("type", new JsonPrimitive("minecraft:item"));

        //Functions Start
        JsonArray functions1 = new JsonArray();

        JsonObject rootFunction = new JsonObject();
        rootFunction.add("function", new JsonPrimitive("minecraft:set_count"));

        //Conditions Start
        JsonArray conditions = new JsonArray();
        JsonObject rootCondition = new JsonObject();
        rootCondition.add("condition", new JsonPrimitive("minecraft:block_state_property"));
        rootCondition.add("block", new JsonPrimitive(name));

        //Properties
        JsonObject rootProperties = new JsonObject();
        rootProperties.add("type", new JsonPrimitive("double"));
        rootCondition.add("properties", rootProperties);
        //Properties End


        conditions.add(rootCondition);
        rootFunction.add("conditions", conditions);
        //Conditions End


        functions1.add(rootFunction);

        rootFunction.add("count", new JsonPrimitive(2));

        //Extra Function Start
        JsonObject extraFunction = new JsonObject();
        extraFunction.add("function", new JsonPrimitive("minecraft:explosion_decay"));
        functions1.add(extraFunction);
        //Extra Function End

        type.add("functions", functions1);
        //Functions End

        type.add("name", new JsonPrimitive(name));

        entry.add(type);


        first.add("entries", entry);
        //Entries end
        pools.add(first);

        root.add("pools", pools);

        return root;
    }


}
