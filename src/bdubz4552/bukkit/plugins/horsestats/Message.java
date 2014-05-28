package bdubz4552.bukkit.plugins.horsestats;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Preset Message enums with type imbedded.
 */
public enum Message {
	
	NORMAL(ChatColor.YELLOW + "[HorseStats] " + ChatColor.GREEN),
	ERROR(ChatColor.YELLOW + "[HorseStats] " + ChatColor.GREEN),
	STAT(ChatColor.GREEN + ""),
	
	RIDING(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED + "You must be riding the horse you want to use this on!"),
	PERMS(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED + "You do not have permissions for this command."),
	ATTACK(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED + "You cannot hurt another player's horse."),
	OWNER(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED + "You must be the owner of the horse to do this."),
	STYLE_PARAMS(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED + "Bad arguments; use '/setstyle ?' to see arguments and usage."),
	PLAYER(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED + "The specified player was not found."),
	CONSOLE(ChatColor.YELLOW + "[HorseStats] " + ChatColor.RED + "Commands cannot be used in console!");
	
	public final String message;
	
	private Message(String str) {
		message = str;
	}
	
	/**
	 * Send a message to a player, with a specific string.
	 * @param p - Player to receive the message.
	 * @param msg - The message to be received.
	 */
	public void send(Player p, String string) {
		p.sendMessage(this.message + string);
	}

	/**
	 * Sends a message to a player, using one of the predefined Message enums.
	 * @param p - Player to receive the message.
	 */
	public void send(Player p) {
		p.sendMessage(this.message);
	}
}
