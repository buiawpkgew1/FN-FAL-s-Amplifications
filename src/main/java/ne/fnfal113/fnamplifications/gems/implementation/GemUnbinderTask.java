package ne.fnfal113.fnamplifications.gems.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.Getter;
import ne.fnfal113.fnamplifications.FNAmplifications;
import ne.fnfal113.fnamplifications.gems.abstracts.AbstractGem;
import ne.fnfal113.fnamplifications.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GemUnbinderTask {

    @Getter
    private final Player player;
    @Getter
    private final ItemStack itemInOffhand;

    public GemUnbinderTask(Player player, ItemStack itemInOffhand) {
        this.player = player;
        this.itemInOffhand = itemInOffhand;
    }

    /**
     * Retrieve the gems from the item in the offhand and display it in a inventory gui
     */
    @SuppressWarnings("ConstantConditions")
    public void getGemsFromItem(){
        Inventory inventory = Bukkit.createInventory(null, 9, Utils.colorTranslator("&cSelect a gem to unbind"));
        PersistentDataContainer pdc = getItemInOffhand().getItemMeta().getPersistentDataContainer();
        if(pdc.isEmpty()){
            return;
        }

        List<ItemStack> gemArray = new ArrayList<>();
        for(NamespacedKey gemKeys : pdc.getKeys()){
            if(pdc.has(gemKeys, PersistentDataType.STRING)) {
                SlimefunItem gem = SlimefunItem.getById(pdc.get(gemKeys, PersistentDataType.STRING));
                if(gem instanceof AbstractGem) {
                    gemArray.add(gem.getItem().clone());
                }
            }
        }

        if(gemArray.isEmpty()){
            getPlayer().sendMessage(Utils.colorTranslator("&eItem doesn't have any gems bound to it"));
            return;
        }

        for (ItemStack gems: gemArray) {
            inventory.addItem(gems);
        }

        getPlayer().openInventory(inventory);
        getPlayer().playSound(player.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1.0F, 1.0F);
    }

    /**
     * this is where the selected gem is unbound from the item
     * @param slimefunItem the selected gem that will be removed
     * @param chance the chance to remove the gem from the item
     */
    @SuppressWarnings("ConstantConditions")
    public void unBindGem(SlimefunItem slimefunItem, int chance){
        getPlayer().getInventory().getItemInMainHand().setAmount(0);
        if(ThreadLocalRandom.current().nextInt(100) <= chance) {
            ItemMeta meta = getItemInOffhand().getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();

            List<String> lore = meta.getLore();
            List<String> gemLore = new ArrayList<>();

            NamespacedKey socketAmount = new NamespacedKey(FNAmplifications.getInstance(), getItemInOffhand().getType().toString().toLowerCase() + "_socket_amount");
            NamespacedKey gem = new NamespacedKey(FNAmplifications.getInstance(), slimefunItem.getId().toLowerCase());

            pdc.remove(gem);
            pdc.set(socketAmount, PersistentDataType.INTEGER, pdc.get(socketAmount, PersistentDataType.INTEGER) - 1);

            if (pdc.get(socketAmount, PersistentDataType.INTEGER) == 0) {
                for (int i = 0; i < lore.indexOf(Utils.colorTranslator("&6◤◤◤◤◤◤| &d&lGems &c|◥◥◥◥◥◥")) + 1; i++) {
                    if (lore.get(i).contains(Utils.colorTranslator("&6◤◤◤◤◤◤| &d&lGems &c|◥◥◥◥◥◥"))) {
                        lore.remove(i - 1);
                    }
                }
                gemLore.add(Utils.colorTranslator("&6◤◤◤◤◤◤| &d&lGems &c|◥◥◥◥◥◥"));
                gemLore.add(ChatColor.RED + "◬ " + slimefunItem.getItemName());
                gemLore.add(Utils.colorTranslator("&6◤◤◤◤◤◤◤◤◤◤◤&c◥◥◥◥◥◥◥◥◥◥◥"));
                lore.removeAll(gemLore);
                pdc.remove(socketAmount);
            } else {
                lore.removeIf(line -> ChatColor.stripColor(line).contains(ChatColor.stripColor(slimefunItem.getItemName())));
            }

            meta.setLore(lore);
            getItemInOffhand().setItemMeta(meta);
            getPlayer().sendMessage(Utils.colorTranslator("&aSuccessfully removed selected gem!"));
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_VILLAGER_WORK_WEAPONSMITH, 1.0F, 1.0F);
        } else {
            getPlayer().sendMessage(Utils.colorTranslator("&cFailed to unbind the gem from the item!"));
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_INFECT, 1.0F, 1.0F);
        }
    }

}