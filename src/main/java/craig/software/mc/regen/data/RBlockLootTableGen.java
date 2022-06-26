package craig.software.mc.regen.data;

import com.google.gson.*;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Created by 50ap5ud5 23/3/2021
 */
public class RBlockLootTableGen extends LootTableProvider {


    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;


    public RBlockLootTableGen(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    public static Path getPath(Path base, Block block) {
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
        return base.resolve("data/" + key.getNamespace() + "/loot_tables/blocks/" + key.getPath() + ".json");
    }

    public static Path getPath(Path path, ResourceLocation rl) {
        return path.resolve("data/" + rl.getNamespace() + "/loot_tables/blocks/" + rl.getPath() + ".json");
    }

    @Override
    public void run(@NotNull CachedOutput cache) {

        Path path = this.generator.getOutputFolder();

        for (Block block : ForgeRegistries.BLOCKS) {
            if (ForgeRegistries.BLOCKS.getKey(block).getNamespace().contentEquals(RConstants.MODID)) {
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

    public void generateSelfTable(Block block, CachedOutput cache, Path base) {
        this.generateTable(cache, getPath(base, ForgeRegistries.BLOCKS.getKey(block)), () -> this.createSingleDropTable(ForgeRegistries.BLOCKS.getKey(block).toString()));
    }

    public void generateSelfSlabTable(Block block, CachedOutput cache, Path base) {
        this.generateTable(cache, getPath(base, ForgeRegistries.BLOCKS.getKey(block)), () -> this.createSlabDropTable(ForgeRegistries.BLOCKS.getKey(block).toString()));
    }

    public void generateTable(CachedOutput cache, Path path, Supplier<JsonElement> element) {
        try {
            DataProvider.saveStable(cache, element.get(), path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull String getName() {
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
