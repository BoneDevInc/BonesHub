package team.devblook.akropolis.module.modules.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import team.devblook.akropolis.AkropolisPlugin;
import team.devblook.akropolis.Permissions;
import team.devblook.akropolis.config.ConfigType;
import team.devblook.akropolis.config.Messages;
import team.devblook.akropolis.module.Module;
import team.devblook.akropolis.module.ModuleType;
import team.devblook.akropolis.util.TextUtil;

import java.util.List;

public class AntiSwear extends Module {
    private List<String> blockedWords;

    public AntiSwear(AkropolisPlugin plugin) {
        super(plugin, ModuleType.ANTI_SWEAR);
    }

    @Override
    public void onEnable() {
        blockedWords = getConfig(ConfigType.SETTINGS).getStringList("anti_swear.blocked_words");
    }

    @Override
    public void onDisable() {
        // TODO: Refactor to follow Liskov Substitution principle.
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(Permissions.ANTI_SWEAR_BYPASS.getPermission()))
            return;

        Component message = event.originalMessage();

        for (String word : blockedWords) {
            if (TextUtil.raw(message).contains(word.toLowerCase())) {
                event.setCancelled(true);
                player.sendMessage(Messages.ANTI_SWEAR_WORD_BLOCKED.toComponent());

                Bukkit.getOnlinePlayers().stream()
                        .filter(p -> p.hasPermission(Permissions.ANTI_SWEAR_NOTIFY.getPermission())).forEach(p -> p.sendMessage(TextUtil.replace(TextUtil.replace(Messages.ANTI_SWEAR_ADMIN_NOTIFY.toComponent(), "player", player.name()), "word", message)));

                return;
            }
        }
    }
}
