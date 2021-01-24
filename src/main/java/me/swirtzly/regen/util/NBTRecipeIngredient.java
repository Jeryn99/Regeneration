package me.swirtzly.regen.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class NBTRecipeIngredient extends Ingredient {
    private final ItemStack stack;

    public NBTRecipeIngredient(ItemStack stack) {
        super(Stream.of(new Ingredient.SingleItemList(stack)));
        this.stack = stack;
    }

    @Override
    public boolean test(@Nullable ItemStack input) {
        if (input == null)
            return false;
        return this.stack.getItem() == input.getItem() && this.stack.getDamage() == input.getDamage() && this.stack.areShareTagsEqual(input);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer< ? extends Ingredient > getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("type", CraftingHelper.getID(Serializer.INSTANCE).toString());
        json.addProperty("item", stack.getItem().getRegistryName().toString());
        json.addProperty("count", stack.getCount());
        if (stack.hasTag())
            json.addProperty("nbt", stack.getTag().toString());
        return json;
    }

    public static class Serializer implements IIngredientSerializer< NBTRecipeIngredient > {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public NBTRecipeIngredient parse(PacketBuffer buffer) {
            return new NBTRecipeIngredient(buffer.readItemStack());
        }

        @Override
        public NBTRecipeIngredient parse(JsonObject json) {
            return new NBTRecipeIngredient(CraftingHelper.getItemStack(json, true));
        }

        @Override
        public void write(PacketBuffer buffer, NBTRecipeIngredient ingredient) {
            buffer.writeItemStack(ingredient.stack);
        }
    }
}
