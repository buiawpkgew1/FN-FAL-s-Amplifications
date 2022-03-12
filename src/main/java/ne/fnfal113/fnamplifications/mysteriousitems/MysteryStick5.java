package ne.fnfal113.fnamplifications.mysteriousitems;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import ne.fnfal113.fnamplifications.items.FNAmpItems;
import ne.fnfal113.fnamplifications.mysteriousitems.abstracts.AbstractStick;
import ne.fnfal113.fnamplifications.utils.Keys;
import ne.fnfal113.fnamplifications.utils.Utils;
import ne.fnfal113.fnamplifications.mysteriousitems.implementation.MainStick;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

public class MysteryStick5 extends AbstractStick {

    public final MainStick mainStick;

    @ParametersAreNonnullByDefault
    public MysteryStick5(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        this.mainStick = new MainStick(Keys.STICK_5_EXP_LEVELS, Keys.STICK_5_DAMAGE, enchantments(), weaponLore(), stickLore(), 2, 15);
    }

    @Override
    public Map<Enchantment, Integer> enchantments(){
        Map<Enchantment, Integer> enchantments = new HashMap<>();
        enchantments.put(Enchantment.DAMAGE_ARTHROPODS, 4);
        enchantments.put(Enchantment.DAMAGE_ALL, 3);
        enchantments.put(Enchantment.DAMAGE_UNDEAD, 4);

        return enchantments;
    }

    @Override
    public String weaponLore(){
        return ChatColor.GOLD + "Another stick of somewhat reckoning";
    }

    @Override
    public String stickLore(){
        return ChatColor.WHITE + "I know you are tired of this stick thing";
    }

    @Override
    public void interact(PlayerInteractEvent e) {
        mainStick.onInteract(e, Material.DIAMOND_AXE);
    }

    @Override
    public void onSwing(EntityDamageByEntityEvent event){
        Player player = (Player) event.getDamager();
        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType() != Material.DIAMOND_AXE){
            return;
        }

        if(mainStick.onSwing(item, FNAmpItems.FN_STICK_5, player, event.getDamage(), 20, 2))  {
            LivingEntity victim = (LivingEntity) event.getEntity();
            victim.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 100, 1, false, false, false));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 0, false, false, false));
            player.sendMessage(Utils.colorTranslator("&cMystery effects was applied to your enemy"));
        }

    }
}