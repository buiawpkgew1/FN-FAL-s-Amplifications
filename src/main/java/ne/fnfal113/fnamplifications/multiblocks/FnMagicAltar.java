package ne.fnfal113.fnamplifications.multiblocks;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.libraries.paperlib.PaperLib;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.items.FNAmpItems;
import ne.fnfal113.fnamplifications.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class FnMagicAltar extends MultiBlockMachine {

    public static final RecipeType RECIPE_TYPE = new RecipeType(
            new NamespacedKey(FNAmplifications.getInstance(), "fn_magic_altar"),
            FNAmpItems.FN_MAGIC_ALTAR,
            "",
            "&f合成魔法物品的祭坛!"
    );


    public FnMagicAltar() {
        super(FNAmpItems.MULTIBLOCK, FNAmpItems.FN_MAGIC_ALTAR, new ItemStack[] {
                null , null , null,
                null, new ItemStack(Material.ACACIA_FENCE), null,
                new ItemStack(Material.ENCHANTING_TABLE), new ItemStack(Material.DISPENSER), new ItemStack(Material.SMITHING_TABLE)
        }, BlockFace.SELF);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block dispBlock = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(dispBlock, false).getState();

        if (state instanceof Dispenser) {
            Dispenser disp = (Dispenser) state;
            Inventory inv = disp.getInventory();
            List<ItemStack[]> inputs = RecipeType.getRecipeInputList(this);

            for (int i = 0; i < inputs.size(); i++) {
                if (canCraft(inv, inputs.get(i))) {
                    ItemStack output = RecipeType.getRecipeOutputList(this, inputs.get(i)).clone();

                    if (SlimefunUtils.canPlayerUseItem(p, output, true)) {
                        craft(dispBlock, p, b, inv, inputs.get(i), output);
                    }

                    return;
                }
            }

            if (inv.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
            } else {
                Slimefun.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
            }
        }
    }

    private boolean canCraft(Inventory inv, ItemStack[] recipe) {
        for (int j = 0; j < inv.getContents().length; j++) {
            if (!SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true)) {
                return false;
            }
        }

        return true;
    }

    protected void craft(Block dispenser, Player p, Block b, Inventory inv, ItemStack[] recipe, ItemStack output) {
        Inventory fakeInv = createVirtualInventory(inv);
        Inventory outputInv = findOutputInventory(output, dispenser, inv, fakeInv);

        if (outputInv != null) {
            craftItem(inv, recipe, b);

            outputInv.addItem(output);
        } else {
            craftItem(inv, recipe, b);

            dispenser.getWorld().dropItem(b.getLocation(), output);
            Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
            p.sendMessage(Utils.colorTranslator("&d合成出的物品将会被丢至附近"));
        }

        if(output.getItemMeta().hasDisplayName()){
            p.sendMessage(Utils.colorTranslator("&d成功合成 " + output.getItemMeta().getDisplayName() + "!"));
        } else{
            p.sendMessage(Utils.colorTranslator("&d成功合成物品!"));
        }
    }

    public void craftItem(Inventory inv, ItemStack[] recipe, Block b){
        for (int j = 0; j < 9; j++) {
            ItemStack item = inv.getContents()[j];

            if (item != null && item.getType() != Material.AIR && SlimefunUtils.isItemSimilar(inv.getContents()[j], recipe[j], true)) {
                ItemUtils.consumeItem(item, recipe[j].getAmount(),true);
            }
        }
        Bukkit.getScheduler().runTaskLater(FNAmplifications.getInstance(), () -> {
            b.getWorld().spawnParticle(Particle.CLOUD, b.getLocation().add(0.5, 0.7, 0.5), 5, 0.1, 0.1, 0.1);
            b.getWorld().spawnParticle(Particle.FLASH, b.getLocation().add(0.3, 0.4, 0.45), 2, 0.1, 0.1, 0.1, 0.1);
            b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1, 1);

            Bukkit.getScheduler().runTaskLater(FNAmplifications.getInstance(), () -> {
                b.getWorld().playEffect(b.getLocation().add(0.5, 0.7, 0.5), Effect.MOBSPAWNER_FLAMES, 1);
                b.getWorld().spawnParticle(Particle.FLASH, b.getLocation().add(0.4, 0.45, 0.5), 2, 0.1, 0.1, 0.1, 0.1);
                b.getWorld().spawnParticle(Particle.CLOUD, b.getLocation().add(0.4, 0.45, 0.5), 2, 0.1, 0.1, 0.1, 0.1);
                b.getWorld().playSound(b.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1, 1);

                Bukkit.getScheduler().runTaskLater(FNAmplifications.getInstance(), () -> {
                    b.getWorld().playEffect(b.getLocation().add(0.35, 0.75, 0.35), Effect.MOBSPAWNER_FLAMES, 1);
                    b.getWorld().spawnParticle(Particle.CLOUD, b.getLocation().add(0.2, 0.3, 0.2), 2, 0.1, 0.1, 0.1, 0.1);
                    b.getWorld().playSound(b.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);

                    Bukkit.getScheduler().runTaskLater(FNAmplifications.getInstance(), () -> {
                        b.getWorld().playEffect(b.getLocation().add(0.5, 0.7, 0.5), Effect.SMOKE, 1);
                        b.getWorld().spawnParticle(Particle.CLOUD, b.getLocation().add(0.35, 0.4, 0.4), 2, 0.1, 0.1, 0.1, 0.1);
                        b.getWorld().spawnParticle(Particle.FLASH, b.getLocation().add(0.35, 0.4, 0.4), 2, 0.1, 0.1, 0.1, 0.1);
                        b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);

                    }, 30);
                }, 30);
            }, 30);
        }, 30);
    }

    protected @Nonnull
    Inventory createVirtualInventory(@Nonnull Inventory inv) {
        Inventory fakeInv = Bukkit.createInventory(null, 9, "Fake Inventory");

        for (int j = 0; j < inv.getContents().length; j++) {
            ItemStack stack = inv.getContents()[j];

            if (stack != null) {
                stack = stack.clone();
                ItemUtils.consumeItem(stack, true);
            }

            fakeInv.setItem(j, stack);
        }

        return fakeInv;
    }


}