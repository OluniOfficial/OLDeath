package oluni.official.minecraft.plugin.ol.oldeath;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createConfigFile() {
        File pluginFolder = plugin.getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdir();
        }

        configFile = new File(pluginFolder, "config.yml");
        if (!configFile.exists()) {
            config = YamlConfiguration.loadConfiguration(configFile);
            setDefaultConfigValues();
            saveConfig();
        }
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        config.addDefault("onPlayerDeath.SaveInventory", true);
        config.addDefault("onPlayerDeath.KeepLevel", true);
        config.addDefault("onPlayerDeath.DroppedExp", "0");
        config.addDefault("onPlayerDeath.RespawnOnMoment", true);
        config.addDefault("onPlayerDeath.DisableMinecraftDeathMessage", true);
        config.addDefault("onPlayerRespawn.Title", "&aВоскресье!");
        config.addDefault("onPlayerRespawn.Subtitle", "");
        config.addDefault("onPlayerRespawn.TitleFadeIn", 10);
        config.addDefault("onPlayerRespawn.TitleStay", 70);
        config.addDefault("onPlayerRespawn.TitleFadeOut", 20);
        config.addDefault("onPlayerRespawn.gamemodeAfterRespawn", "Survival");
        config.addDefault("onPlayerRespawn.ActionBar", "");
        config.addDefault("onPlayerRespawn.Chat", "");
        config.addDefault("onPlayerRespawn.sound", true);
        config.addDefault("onPlayerRespawn.soundOnRespawn", "BLOCK_NOTE_BLOCK_BASS");
        config.addDefault("messages.configReloaded", "&aКонфигурация OLDeath перезагружена.");
        config.addDefault("messages.noPermission", "&cУ вас нет прав для выполнения этой команды.");
        config.addDefault("messages.onlyPlayer", "&cЭту команду может использовать только игрок.");
        config.options().copyDefaults(true);
        saveConfig();
    }

    private void setDefaultConfigValues() {
        config.set("onPlayerDeath.SaveInventory", true);
        config.set("onPlayerDeath.KeepLevel", true);
        config.set("onPlayerDeath.DroppedExp", "0");
        config.set("onPlayerDeath.RespawnOnMoment", true);
        config.set("onPlayerDeath.DisableMinecraftDeathMessage", true);
        config.set("onPlayerRespawn.Title", "&aВоскресье!");
        config.set("onPlayerRespawn.Subtitle", "");
        config.set("onPlayerRespawn.TitleFadeIn", 10);
        config.set("onPlayerRespawn.TitleStay", 70);
        config.set("onPlayerRespawn.TitleFadeOut", 20);
        config.set("onPlayerRespawn.gamemodeAfterRespawn", "Survival");
        config.set("onPlayerRespawn.ActionBar", "");
        config.set("onPlayerRespawn.Chat", "");
        config.set("onPlayerRespawn.sound", true);
        config.set("onPlayerRespawn.soundOnRespawn", "BLOCK_NOTE_BLOCK_BASS");
        config.set("messages.configReloaded", "&aКонфигурация OLDeath перезагружена.");
        config.set("messages.noPermission", "&cУ вас нет прав для выполнения этой команды.");
        config.set("messages.onlyPlayer", "&cЭту команду может использовать только игрок.");
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String translateHexColorCodes(String message) {
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + group).toString());
        }

        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
