package ne.fnfal113.fnamplifications.utils;

import ne.fnfal113.fnamplifications.config.ReturnConfValue;
import ne.fnfal113.fnamplifications.FNAmplifications;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerJoinLister implements Listener {

    private final List<UUID> players = new ArrayList<>();

    private static final ReturnConfValue value = new ReturnConfValue();

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(!value.changelogEnable()){
            return;
        }

        if(players.contains(player.getUniqueId())){
           return;
        }

        players.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater(FNAmplifications.getInstance(), () -> {
            for(String string : getChangelog()) {
                player.sendMessage(string);
            }
        }, 25L);

    }

    public String[] getChangelog(){
        return Utils.stringSequence(
                "||---------------------------------------------------||",
                Utils.colorTranslator("&e&lFN &c&lAmpli&b&lfications &r&e" + FNAmplifications.getInstance().getDescription().getVersion()),
                Utils.colorTranslator("&fChangelog"),
                "",
                Utils.colorTranslator("&d&l• &r&d&lAdded Disarmor gem"),
                Utils.colorTranslator("  &ba chance to disamor your enemy armor!"),
                Utils.colorTranslator("&d&l• &r&d&lFixed &r&dimportant bugs"),
                Utils.colorTranslator("&d&l• &r&dAdded gem messages to know it"),
                Utils.colorTranslator("  &bhas taken effect when triggered"),
                Utils.colorTranslator("&d&l• &r&dAdded throw and return weapon sounds"),
                Utils.colorTranslator("  &bfor axe throwie, damnation and tri-sword gem"),
                Utils.colorTranslator(""),
                Utils.colorTranslator("&ehttps://github.com/FN-FAL113"),
                "||---------------------------------------------------||"
        );
    }

}