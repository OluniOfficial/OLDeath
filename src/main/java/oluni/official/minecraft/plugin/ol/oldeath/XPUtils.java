package oluni.official.minecraft.plugin.ol.oldeath;
import org.bukkit.entity.Player;

public class XPUtils {

    public static int getTotalExperience(Player player) {
        int experience = 0;
        int level = player.getLevel();
        if (level >= 0 && level <= 15) {
            experience = (int) (Math.pow(level, 2) + 6 * level);
        } else if (level >= 16 && level <= 30) {
            experience = (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360);
        } else if (level > 30) {
            experience = (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220);
        }
        int progress = Math.round(player.getExp() * player.getExpToLevel());
        experience += progress;
        return experience;
    }

    public static void setTotalExperience(Player player, int exp) {
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.giveExp(exp);
    }
}