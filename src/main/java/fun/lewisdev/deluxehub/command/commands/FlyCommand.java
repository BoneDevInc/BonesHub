package fun.lewisdev.deluxehub.command.commands;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.command.InjectableCommand;
import fun.lewisdev.deluxehub.config.ConfigManager;
import fun.lewisdev.deluxehub.config.ConfigType;
import fun.lewisdev.deluxehub.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class FlyCommand extends InjectableCommand {
    private final DeluxeHubPlugin plugin;
    private final FileConfiguration dataConfig;
    private final boolean saveState;

    public FlyCommand(DeluxeHubPlugin plugin, List<String> aliases) {
        super(plugin, "fly", "Toggle flight mode", "/fly [player]", aliases);
        this.plugin = plugin;

        ConfigManager configManager = plugin.getConfigManager();

        this.dataConfig = configManager.getFile(ConfigType.DATA).get();
        this.saveState = configManager.getFile(ConfigType.SETTINGS).get().getBoolean("fly.save_state", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Console cannot clear inventory");
                return true;
            }

            if (!(sender.hasPermission(Permissions.COMMAND_FLIGHT.getPermission()))) {
                sender.sendMessage(Messages.NO_PERMISSION.toString());
                return true;
            }

            Player player = (Player) sender;

            if (player.getAllowFlight()) {
                player.sendMessage(Messages.FLIGHT_DISABLE.toString());
                toggleFlight(player, false);
            } else {
                player.sendMessage(Messages.FLIGHT_ENABLE.toString());
                toggleFlight(player, true);
            }
        } else if (args.length == 1) {
            if (!(sender.hasPermission(Permissions.COMMAND_FLIGHT_OTHERS.getPermission()))) {
                sender.sendMessage(Messages.NO_PERMISSION.toString());
                return true;
            }

            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(Messages.INVALID_PLAYER.toString().replace("%player%", args[0]));
                return true;
            }

            if (player.getAllowFlight()) {
                player.sendMessage(Messages.FLIGHT_DISABLE.toString());
                sender.sendMessage(Messages.FLIGHT_DISABLE_OTHER.toString().replace("%player%", player.getName()));
                toggleFlight(player, false);
            } else {
                player.sendMessage(Messages.FLIGHT_ENABLE.toString());
                sender.sendMessage(Messages.FLIGHT_ENABLE_OTHER.toString().replace("%player%", player.getName()));
                toggleFlight(player, true);
            }
        }

        return true;
    }

    private void toggleFlight(Player player, boolean value) {
        player.setAllowFlight(value);
        player.setFlying(value);

        if (!saveState) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                dataConfig.set("players." + player.getUniqueId() + ".fly", value));
    }
}
