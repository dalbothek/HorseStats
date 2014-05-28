package bdubz4552.bukkit.plugins.horsestats.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import bdubz4552.bukkit.plugins.horsestats.HorseStatsMain;
import bdubz4552.bukkit.plugins.horsestats.Message;

public class AdminNotificationListener extends HorseStatsListenerBase implements Listener {

	public AdminNotificationListener(HorseStatsMain horseStatsMain) {
		super (horseStatsMain);
	}

	@EventHandler
	public void onAdminJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		//Login message regarding horse ownership bug.
		Message.NORMAL.send(p, "Please use /horsestats to view an important message regarding horse ownership.");
		if (p.hasPermission("HorseStats.pluginalerts")) {
			if (main.outofdateConfig == true) {
				Message.NORMAL.send(p, "The HorseStats config is out of date. Details can be found in the console when the plugin started.");
			}
			
			if (main.crippleMode == true) {
				Message.NORMAL.send(p, "HorseStats is running in cripple mode. Details can be found in the console when the plugin started.");
			}
		}
	}
}
