package bdubz4552.bukkit.plugins.horsestats;

import org.bukkit.entity.Player;

public class HorseStatsCommand {
	
	public HorseStatsMain main;
	public String[] usageList = {"/hspawn <donkey|mule>. To spawn a horse, do not add an argument.", "/setowner <player>", "/setstat <jump|health> <value>. Health is measured in hearts. Jump is measured in blocks."};
	
	public boolean permCheck(Player player, String permission) {
		if (player.hasPermission("HorseStats." + permission)) {
			return true;
		} else {
			Message.PERMS.send(player);
			return false;
		}
	}
}
