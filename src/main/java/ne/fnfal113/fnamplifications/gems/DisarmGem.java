package ne.fnfal113.fnamplifications.gems;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.fnamplifications.gems.abstracts.AbstractGem;
import ne.fnfal113.fnamplifications.gems.handlers.GemUpgrade;
import ne.fnfal113.fnamplifications.gems.handlers.OnDamageHandler;
import ne.fnfal113.fnamplifications.gems.implementation.Gem;
import ne.fnfal113.fnamplifications.utils.Utils;
import ne.fnfal113.fnamplifications.utils.WeaponArmorEnum;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class DisarmGem extends AbstractGem implements OnDamageHandler, GemUpgrade {

    public DisarmGem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 10);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onDrag(InventoryClickEvent event, Player player, SlimefunItem slimefunItem, ItemStack currentItem){
        if (WeaponArmorEnum.AXES.isTagged(currentItem.getType()) || WeaponArmorEnum.SWORDS.isTagged(currentItem.getType())) {
            if(isUpgradeGem(event.getCursor(), this.getId())) {
                upgradeGem(slimefunItem, currentItem, event, player, this.getId());
            } else {
                new Gem(slimefunItem, currentItem, player).onDrag(event, false);
            }
        } else {
            player.sendMessage(Utils.colorTranslator("&e这个物品不能绑定! 此宝石只能绑定在swords and axes only"));
        }
    }

    @Override
    public void onDamage(EntityDamageByEntityEvent event, ItemStack itemStack) {
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        if(!(event.getDamager() instanceof Player)){
            return;
        }
        if(event.isCancelled()){
            return;
        }

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        if(ThreadLocalRandom.current().nextInt(100) < getChance() / getTier(itemStack, this.getId())) {
            if(victim.getInventory().getItemInMainHand().getType() != Material.AIR){
                ItemStack itemInMainHand = victim.getInventory().getItemInMainHand();
                int slot = victim.getInventory().firstEmpty(); // get first empty slot from left to right

                victim.getInventory().setItemInMainHand(null);
                if (slot != -1) {
                    victim.getInventory().setItem(slot, itemInMainHand.clone());
                } else {
                    victim.getWorld().dropItem(victim.getLocation(), itemInMainHand.clone());
                }
                sendGemMessage(damager, this.getItemName());
            }
        }
    }

}