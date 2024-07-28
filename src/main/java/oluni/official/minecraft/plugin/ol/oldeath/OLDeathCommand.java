package oluni.official.minecraft.plugin.ol.oldeath;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class OLDeathCommand implements CommandExecutor {

    private final ConfigManager configManager;

    public OLDeathCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("oldeath")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("oldeath.reload")) {
                    configManager.loadConfig();
                    sender.sendMessage(configManager.translateHexColorCodes(configManager.getConfig().getString("messages.configReloaded")));
                } else {
                    sender.sendMessage(configManager.translateHexColorCodes(configManager.getConfig().getString("messages.noPermission")));
                }
                return true;
            }
        }
        return false;
    }
}
