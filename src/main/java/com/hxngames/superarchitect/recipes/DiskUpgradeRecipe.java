package com.hxngames.superarchitect.recipes;

import com.hxngames.superarchitect.items.SuperArchitectItems;
import com.hxngames.superarchitect.items.components.DiskComponent;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class DiskUpgradeRecipe implements CraftingRecipe {
    private final ShapedRecipe wrapped;

    public DiskUpgradeRecipe(ShapedRecipe wrapped) {
        this.wrapped = wrapped;
    }

    public ShapedRecipe getWrapped() {
        return wrapped;
    }

    @Override
    public boolean matches(CraftingInput input, net.minecraft.world.level.Level level) {
        return wrapped.matches(input, level);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        ItemStack result = wrapped.assemble(input, provider);

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.has(SuperArchitectItems.DISK_COMPONENT)) {
                DiskComponent component = stack.get(SuperArchitectItems.DISK_COMPONENT);
                if (component != null) {
                    result.set(SuperArchitectItems.DISK_COMPONENT, component);
                }
                break; // Only transfer from the first disk found
            }
        }

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return wrapped.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return wrapped.getResultItem(provider);
    }

    @Override
    public CraftingBookCategory category() {
        return wrapped.category();
    }

    @Override
    public net.minecraft.core.NonNullList<Ingredient> getIngredients() {
        return wrapped.getIngredients();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String getGroup() {
        return wrapped.getGroup();
    }

    @Override
    public ItemStack getToastSymbol() {
        return wrapped.getToastSymbol();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SuperArchitectRecipes.DISK_UPGRADE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
