package ne.fnfal113.fnamplifications.gems;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.fnamplifications.gems.abstracts.AbstractGem;
import ne.fnfal113.fnamplifications.utils.Keys;
import ne.fnfal113.fnamplifications.utils.WeaponArmorEnum;
import ne.fnfal113.fnamplifications.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RetaliateGem extends AbstractGem {

    public RetaliateGem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public void onDrag(Player player, SlimefunItem gem, ItemStack gemItem, ItemStack currentItem){
        if (WeaponArmorEnum.SWORDS.isTagged(currentItem.getType()) || WeaponArmorEnum.AXES.isTagged(currentItem.getType())) {
            if (hasNeededGem(currentItem.getItemMeta().getPersistentDataContainer())) {
                bindGem(gem, currentItem, player, true);
            } else {
                player.sendMessage(Utils.colorTranslator("&e武器中缺少所需的宝石, 请先阅读宝石的介绍!"));
            }
        } else {
            player.sendMessage(Utils.colorTranslator("&e这个物品不能绑定! 此宝石只能绑定在剑和斧上"));
        }
    }

    public boolean hasNeededGem(PersistentDataContainer pdc){
        return pdc.has(Keys.RETURN_DAMNATION_KEY, PersistentDataType.STRING) ||
                pdc.has(Keys.RETURN_TRISWORD_KEY, PersistentDataType.STRING) ||
                pdc.has(Keys.RETURN_AXE_KEY, PersistentDataType.STRING);
    }
}