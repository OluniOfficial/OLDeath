package oluni.official.minecraft.plugin.ol.oldeath;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;

import java.util.Objects;

public class DeathListener implements Listener {

    private final OLDeath plugin;
    private final ConfigManager configManager;

    public DeathListener(OLDeath plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        boolean saveInventory = configManager.getConfig().getBoolean("onPlayerDeath.SaveInventory");
        boolean keepLevel = configManager.getConfig().getBoolean("onPlayerDeath.KeepLevel");
        int droppedExp = Integer.parseInt(Objects.requireNonNull(configManager.getConfig().getString("onPlayerDeath.DroppedExp")));
        boolean respawnOnMoment = configManager.getConfig().getBoolean("onPlayerDeath.RespawnOnMoment");

        event.setKeepInventory(saveInventory);

        if (keepLevel) {
            event.setKeepLevel(true);
            if (droppedExp > 0) {
                int playerTotalExp = XPUtils.getTotalExperience(player);
                int newExp = playerTotalExp - droppedExp;
                XPUtils.setTotalExperience(player, Math.max(newExp, 0));
                event.setDroppedExp(droppedExp);
            } else {
                event.setDroppedExp(0);
            }
        } else {
            event.setKeepLevel(false);
            event.setDroppedExp(droppedExp);
        }

        if (saveInventory) {
            event.getDrops().clear();
        }

        if (respawnOnMoment) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.spigot().respawn(), 1L);
        }

        if (configManager.getConfig().getBoolean("onPlayerDeath.DisableMinecraftDeathMessage")) {
            event.setDeathMessage(null);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.valueOf(Objects.requireNonNull(configManager.getConfig().getString("onPlayerRespawn.gamemodeAfterRespawn")).toUpperCase()));
        sendRespawnMessages(player);
    }

    private void sendRespawnMessages(Player player) {
        String title = configManager.translateHexColorCodes(configManager.getConfig().getString("onPlayerRespawn.Title"));
        String subtitle = configManager.translateHexColorCodes(configManager.getConfig().getString("onPlayerRespawn.Subtitle"));
        int fadeIn = configManager.getConfig().getInt("onPlayerRespawn.TitleFadeIn");
        int stay = configManager.getConfig().getInt("onPlayerRespawn.TitleStay");
        int fadeOut = configManager.getConfig().getInt("onPlayerRespawn.TitleFadeOut");

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);

        String actionBar = configManager.translateHexColorCodes(configManager.getConfig().getString("onPlayerRespawn.ActionBar"));
        if (!actionBar.isEmpty()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionBar));
        }

        String chatMessage = configManager.translateHexColorCodes(configManager.getConfig().getString("onPlayerRespawn.Chat"));
        if (!chatMessage.isEmpty()) {
            player.sendMessage(chatMessage);
        }

        boolean playSound = configManager.getConfig().getBoolean("onPlayerRespawn.sound");
        if (playSound) {
            String soundName = configManager.getConfig().getString("onPlayerRespawn.soundOnRespawn");
            try {
                Sound sound = Sound.valueOf(soundName);
                player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid sound name in config: " + soundName);
            }
        }
    }
}
