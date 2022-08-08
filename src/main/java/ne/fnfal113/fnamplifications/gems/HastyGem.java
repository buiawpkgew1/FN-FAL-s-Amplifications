package ne.fnfal113.fnamplifications.gems;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import ne.fnfal113.fnamplifications.gems.abstracts.AbstractGem;
import ne.fnfal113.fnamplifications.utils.WeaponArmorEnum;
import ne.fnfal113.fnamplifications.gems.handlers.OnBlockBreakHandler;
import ne.fnfal113.fnamplifications.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class HastyGem extends AbstractGem implements OnBlockBreakHandler {

    public HastyGem(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe, 16);
    }

    @Override
    public void onDrag(Player player, SlimefunItem gem, ItemStack gemItem, ItemStack currentItem){
        if (WeaponArmorEnum.AXES.isTagged(currentItem.getType()) || WeaponArmorEnum.SHOVELS.isTagged(currentItem.getType())
                || WeaponArmorEnum.PICKAXE.isTagged(currentItem.getType())) {
            bindGem(gem, currentItem, player, false);
        } else {
            player.sendMessage(Utils.colorTranslator("&e这个物品不能绑定! 此宝石只能绑定在铲子, 镐子和斧头上"));
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event, Player player){
        if(event.isCancelled()){
            return;
        }
        Block block = event.getBlock();

        if(SlimefunTag.ORES.isTagged(block.getType()) || SlimefunTag.STONE_VARIANTS.isTagged(block.getType())) {
            if (ThreadLocalRandom.current().nextInt(100) < getChance()) {
                PotionEffect potionEffect = new PotionEffect(PotionEffectType.FAST_DIGGING, 80, 2, true, false, false);
                player.addPotionEffect(potionEffect);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Utils.colorTranslator("&e你的动作变快了!")));
            }
        }
    }
}