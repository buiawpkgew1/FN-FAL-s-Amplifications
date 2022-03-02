package ne.fnfal113.fnamplifications.Staffs;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import ne.fnfal113.fnamplifications.ConfigValues.ReturnConfValue;
import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.Items.FNAmpItems;
import ne.fnfal113.fnamplifications.Multiblock.FnAssemblyStation;
import ne.fnfal113.fnamplifications.Staffs.Interface.StaffImpl;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaffOfHealing extends SlimefunItem implements StaffImpl {

    private static final SlimefunAddon plugin = FNAmplifications.getInstance();

    private static final ReturnConfValue value = new ReturnConfValue();

    private final NamespacedKey defaultUsageKey;
    private final NamespacedKey defaultUsageKey2;

    private final MainStaff mainStaff;

    public StaffOfHealing(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        this.defaultUsageKey = new NamespacedKey(FNAmplifications.getInstance(), "healingstaff");
        this.defaultUsageKey2 = new NamespacedKey(FNAmplifications.getInstance(), "cloudfn");
        this.mainStaff = new MainStaff(lore(), value.staffOfHealing(), getStorageKey());
    }

    protected @Nonnull
    NamespacedKey getStorageKey() {
        return defaultUsageKey;
    }

    protected @Nonnull
    NamespacedKey getStorageKey2() {
        return defaultUsageKey2;
    }

    @Override
    public List<String> lore(){
        List<String> lore = new ArrayList<>();
        lore.add(0, "");
        lore.add(1, ChatColor.LIGHT_PURPLE + "Right click to spawn a cloud of effect");
        lore.add(2, ChatColor.LIGHT_PURPLE + "that heals the caster only if inside the");
        lore.add(3, ChatColor.LIGHT_PURPLE + "radius for 8 seconds");

        return lore;
    }

    @Override
    public void onRightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getPlayer().getTargetBlockExact(50);

        if(block == null || item.getType() == Material.AIR){
            return;
        }

        if (!Slimefun.getProtectionManager().hasPermission(
                Bukkit.getOfflinePlayer(player.getUniqueId()),
                block,
                Interaction.BREAK_BLOCK)
        ) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have permission to cast healing there!");
            return;
        }

        ItemMeta meta = item.getItemMeta();

        mainStaff.updateMeta(item, meta, player);

        AreaEffectCloud effectCloud = (AreaEffectCloud) player.getWorld().spawnEntity(block.getLocation().add(0.5, 1, 0.5) , EntityType.AREA_EFFECT_CLOUD);
        effectCloud.setParticle(Particle.HEART);
        effectCloud.setDuration(160);
        effectCloud.setRadius(2.85F);
        effectCloud.setCustomName("FN_HEALING");
        effectCloud.setCustomNameVisible(false);
        effectCloud.setReapplicationDelay(0);
        effectCloud.getPersistentDataContainer().set(getStorageKey2(), PersistentDataType.STRING, player.getName());
        effectCloud.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 0 , 0, false, false, false), true);

        Objects.requireNonNull(player.getLocation().getWorld()).playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);

    }

    public static void setup(){
        new StaffOfHealing(FNAmpItems.FN_STAFFS, FNAmpItems.FN_STAFF_HEALING, FnAssemblyStation.RECIPE_TYPE, new ItemStack[]{
                new SlimefunItemStack(SlimefunItems.ENDER_LUMP_3, 8), new ItemStack(Material.SPLASH_POTION), new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_3, 8),
                new SlimefunItemStack(SlimefunItems.BANDAGE, 8), new ItemStack(Material.BLAZE_ROD), new SlimefunItemStack(SlimefunItems.BANDAGE, 8),
                new SlimefunItemStack(SlimefunItems.ESSENCE_OF_AFTERLIFE, 1), SlimefunItems.MAGIC_SUGAR, new SlimefunItemStack(SlimefunItems.ESSENCE_OF_AFTERLIFE, 1)})
                .register(plugin);
    }
}
