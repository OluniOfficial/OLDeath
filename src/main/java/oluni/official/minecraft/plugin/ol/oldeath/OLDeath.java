package oluni.official.minecraft.plugin.ol.oldeath;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class OLDeath extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.createConfigFile();
        configManager.loadConfig();

        getServer().getPluginManager().registerEvents(new DeathListener(this, configManager), this);

        Objects.requireNonNull(getCommand("oldeath")).setExecutor(new OLDeathCommand(configManager));
        Objects.requireNonNull(getCommand("oldeath")).setTabCompleter(new OLDeathTabCompleter());
    }

    @Override
    public void onDisable() {
        configManager.saveConfig();
    }
}