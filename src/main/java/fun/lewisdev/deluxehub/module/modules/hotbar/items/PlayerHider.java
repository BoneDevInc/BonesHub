package fun.lewisdev.deluxehub.module.modules.hotbar.items;

import de.tr7zw.changeme.nbtapi.NBTItem;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.cooldown.CooldownType;
import fun.lewisdev.deluxehub.module.modules.hotbar.HotbarItem;
import fun.lewisdev.deluxehub.module.modules.hotbar.HotbarManager;
import fun.lewisdev.deluxehub.utility.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerHider extends HotbarItem {
    private final int cooldown;
    private final ItemStack hiddenItem;
    private final List<UUID> hidden;

    public PlayerHider(HotbarManager hotbarManager, ItemStack item, int slot, String key) {
        super(hotbarManager, item, slot, key);
        hidden = new ArrayList<>();
        FileConfiguration config = getHotbarManager().getConfig(ConfigType.SETTINGS);
        NBTItem nbtItem = new NBTItem(
                ItemStackBuilder.getItemStack(config.getConfigurationSection("player_hider.hidden")).build());
        nbtItem.setString("hotbarItem", key);
        hiddenItem = nbtItem.getItem();
        cooldown = config.getInt("player_hider.cooldown");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onInteract(Player player) {
        if (!getHotbarManager().tryCooldown(player.getUniqueId(), CooldownType.PLAYER_HIDER, cooldown)) {
            player.sendMessage(Messages.COOLDOWN_ACTIVE.toString().replace("%time%",
                    getHotbarManager().getCooldown(player.getUniqueId(), CooldownType.PLAYER_HIDER)));
            return;
        }

        if (!hidden.contains(player.getUniqueId())) {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                player.hidePlayer(pl);
            }

            hidden.add(player.getUniqueId());
            player.sendMessage(Messages.PLAYER_HIDER_HIDDEN.toString());

            player.getInventory().setItem(getSlot(), hiddenItem);
        } else {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                player.showPlayer(pl);
            }

            hidden.remove(player.getUniqueId());
            player.sendMessage(Messages.PLAYER_HIDER_SHOWN.toString());

            player.getInventory().setItem(getSlot(), getItem());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (hidden.contains(player.getUniqueId())) {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                player.showPlayer(pl);
            }
        }

        hidden.remove(player.getUniqueId());
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player playerToHide = event.getPlayer();

        hidden.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) return;

            player.hidePlayer(playerToHide);
        });
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (getHotbarManager().inDisabledWorld(player.getLocation()) && hidden.contains(player.getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers())
                player.showPlayer(p);
            hidden.remove(player.getUniqueId());
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (hidden.contains(player.getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                player.showPlayer(p);
            }
            hidden.remove(player.getUniqueId());
        }
    }
}