package ne.fnfal113.fnamplifications.Machines;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerHead;
import io.github.thebusybiscuit.slimefun4.libraries.dough.skins.PlayerSkin;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;

import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.Items.FNAmpItems;

public class ElectricCompressor extends AContainer implements RecipeDisplayItem {

    private static final SlimefunAddon plugin = FNAmplifications.getInstance();

    public ElectricCompressor(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Nonnull
    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new ArrayList<>(recipes.size() * 4);

        for (MachineRecipe recipe : recipes) {
            displayRecipes.add(recipe.getInput()[0]);
            displayRecipes.add(new ItemStack(Material.AIR));
            displayRecipes.add(PlayerHead.getItemStack(PlayerSkin.fromHashCode(
                    "682ad1b9cb4dd21259c0d75aa315ff389c3cef752be3949338164bac84a96e")));
            displayRecipes.add(new ItemStack(Material.AIR));
            displayRecipes.add(recipe.getOutput()[0]);
            displayRecipes.add(new ItemStack(Material.AIR));
            displayRecipes.add(new ItemStack(Material.AIR));
            displayRecipes.add(new ItemStack(Material.AIR));
            displayRecipes.add(new ItemStack(Material.AIR));
            displayRecipes.add(new ItemStack(Material.AIR));
        }

        return displayRecipes;
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(16, new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.REINFORCED_ALLOY_INGOT, 2)},
                new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.REINFORCED_PLATE, 2)});
        registerRecipe(16, new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.STEEL_INGOT, 2)},
                new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.STEEL_PLATE, 3)});
        registerRecipe(16, new ItemStack[]{new ItemStack(Material.COAL_BLOCK, 4)},
                new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.CARBON, 12)});
        registerRecipe(16, new ItemStack[]{new ItemStack(Material.COAL, 4)},
                new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.CARBON, 1)});
        registerRecipe(16, new ItemStack[]{new ItemStack(Material.CHARCOAL, 2)},
                new ItemStack[]{new ItemStack(Material.COAL, 1)});
        registerRecipe(16, new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.SMALL_URANIUM, 2)},
                new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.URANIUM, 1)});
        registerRecipe(16, new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.URANIUM, 64)},
                new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.NEPTUNIUM, 1)});
        registerRecipe(16, new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.MAGNESIUM_INGOT, 4)},
                new SlimefunItemStack[]{new SlimefunItemStack(SlimefunItems.SALT, 6)});
    }

    public static void setup() {
        new ElectricCompressor(FNAmpItems.MACHINES, FNAmpItems.FN_FAL_COMPRESSOR_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                FNAmpItems.COMPRESSOR_PART, FNAmpItems.THREAD_PART, FNAmpItems.MOTOR_SWITCH,
                FNAmpItems.GEAR_PART, FNAmpItems.BASIC_MACHINE_BLOCK, FNAmpItems.GEAR_PART,
                FNAmpItems.CONDENSER_PART, FNAmpItems.ALUMINUM_PLATING, FNAmpItems.POWER_COMPONENT})
        .setCapacity(1536).setEnergyConsumption(64).setProcessingSpeed(1).register(plugin);

        new ElectricCompressor(FNAmpItems.MACHINES, FNAmpItems.FN_FAL_COMPRESSOR_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                FNAmpItems.COMPRESSOR_PART, FNAmpItems.THREAD_PART, FNAmpItems.MOTOR_SWITCH,
                FNAmpItems.GEAR_PART, FNAmpItems.BASIC_MACHINE_BLOCK, FNAmpItems.FN_FAL_COMPRESSOR_1,
                FNAmpItems.CONDENSER_PART, FNAmpItems.DIAMOND_PLATING, FNAmpItems.POWER_COMPONENT})
        .setCapacity(1536).setEnergyConsumption(128).setProcessingSpeed(2).register(plugin);

        new ElectricCompressor(FNAmpItems.MACHINES, FNAmpItems.FN_FAL_COMPRESSOR_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                FNAmpItems.COMPRESSOR_PART, FNAmpItems.COMPONENT_PART, FNAmpItems.MOTOR_SWITCH,
                FNAmpItems.FN_FAL_COMPRESSOR_2, FNAmpItems.HIGHTECH_MACHINE_BLOCK, FNAmpItems.FN_FAL_COMPRESSOR_2,
                FNAmpItems.CONDENSER_PART, FNAmpItems.REINFORCED_CASING, FNAmpItems.POWER_COMPONENT})
        .setCapacity(1536).setEnergyConsumption(256).setProcessingSpeed(4).register(plugin);
    }

    @Nonnull
    @Override
    public String getMachineIdentifier() {
        return "ELECTRIC_COMPRESSOR";
    }

    @Override
    public ItemStack getProgressBar() {
        return new SlimefunItemStack(FNAmpItems.COMPRESSOR_PART, 1);
    }
}