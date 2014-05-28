package bdubz4552.bukkit.plugins.horsestats.event;

import java.util.HashMap;

import org.bukkit.entity.Horse;

import bdubz4552.bukkit.plugins.horsestats.HorseStatsMain;

/**
 * Base class for event listeners. Seems useless at the moment,
 * but if things expand this will be handy.
 */
public class HorseStatsListenerBase {
	
	public HorseStatsMain main;
	
	public HorseStatsListenerBase(HorseStatsMain horseStatsMain) {
		this.main = horseStatsMain;
	}
	
	/**
	 * A list of horses that are waiting in the teleport queue. Matches horse objects to players.
	 */
	public HashMap<String, Horse> teleportQueue = new HashMap<String, Horse>();
}
