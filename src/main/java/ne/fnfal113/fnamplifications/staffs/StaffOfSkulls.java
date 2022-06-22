package ne.fnfal113.fnamplifications.staffs;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.fnamplifications.staffs.abstracts.AbstractStaff;
import ne.fnfal113.fnamplifications.utils.Keys;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StaffOfSkulls extends AbstractStaff {

    public StaffOfSkulls(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 10, Keys.createKey("skullsstaff"));
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block block = event.getPlayer().getTargetBlock(null, 50);

        if (!hasPermissionToCast(item.getItemMeta().getDisplayName(), player, block.getLocation())) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        getStaffTask().updateMeta(item, meta, player);

        for(int i = 0; i < 7; i++) {
            WitherSkull skull = (WitherSkull) player.getWorld().spawnEntity
                    (player.getEyeLocation().clone().add(player.getLocation().getDirection().clone().multiply(i).setY(-0.5)),
                            EntityType.WITHER_SKULL);
            skull.setDirection(player.getLocation().getDirection().clone());
            skull.setGlowing(true);
            skull.setIsIncendiary(true);
            skull.setBounce(false);
        }

    }

}