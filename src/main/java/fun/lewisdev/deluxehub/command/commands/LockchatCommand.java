package fun.lewisdev.deluxehub.command.commands;

import cl.bgmp.minecraft.util.commands.CommandContext;
import cl.bgmp.minecraft.util.commands.annotations.Command;
import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.Permissions;
import fun.lewisdev.deluxehub.config.Messages;
import fun.lewisdev.deluxehub.module.ModuleType;
import fun.lewisdev.deluxehub.module.modules.chat.ChatLock;
import org.bukkit.command.CommandSender;

public class LockchatCommand {
    private final DeluxeHubPlugin plugin;

    public LockchatCommand(DeluxeHubPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(aliases = {"lockchat"}, desc = "Locks global chat")
    public void lockchat(final CommandContext args, final CommandSender sender) {
        if (!sender.hasPermission(Permissions.COMMAND_LOCKCHAT.getPermission())) {
            sender.sendMessage(Messages.NO_PERMISSION.toString());
            return;
        }

        ChatLock chatLockModule = (ChatLock) plugin.getModuleManager().getModule(ModuleType.CHAT_LOCK);

        if (chatLockModule.isChatLocked()) {
            plugin.getServer().broadcastMessage(
                    Messages.CHAT_UNLOCKED_BROADCAST.toString().replace("%player%", sender.getName()));
            chatLockModule.setChatLocked(false);
        } else {
            plugin.getServer()
                    .broadcastMessage(Messages.CHAT_LOCKED_BROADCAST.toString().replace("%player%", sender.getName()));
            chatLockModule.setChatLocked(true);
        }
    }
}
