package bdubz4552.bukkit.plugins.horsestats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import bdubz4552.bukkit.plugins.horsestats.*;
import bdubz4552.bukkit.plugins.horsestats.event.HorseStatsListenerBase;

public class Htp extends HorseStatsCommand implements CommandExecutor {
	
	private HorseStatsListenerBase base;
	
	public Htp(HorseStatsMain horseStatsMain, HorseStatsListenerBase horseStatsListenerBase) {
		this.main = horseStatsMain;
		this.base = horseStatsListenerBase;
	}
	
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (label.equalsIgnoreCase("htp")) {
				if (this.permCheck(p, "htp")) {
					this.run(p);
				}
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "[HorseStats] " + ChatColor.RED + "Commands cannot be used in console!");
		}
		return true;
	}
	
	/**
	 * Does not work if chunks are not loaded. Attempts at loading chunks were made,
	 * but no success.
	 * @param p - The player who initiated the teleport.
	 */
	public void run(Player p) {
		if (base.teleportQueue.get(p.getName()) == null) {
			Message.ERROR.send(p, "No horse has been selected!");
		} else {
			Horse h = base.teleportQueue.get(p.getName());			
			if (main.configBoolean("interWorldTeleport") == false) {
				if (p.getWorld() != h.getWorld()) {
					Message.ERROR.send(p, "Teleporting between worlds is disabled.");
				} else {
					if (h.teleport(p) == true) {
						Message.NORMAL.send(p, "Teleporting...");
					} else {
						Message.ERROR.send(p, "Teleport failed.");
					}
					base.teleportQueue.remove(p.getName());
				}
			} else {
				if (h.teleport(p) == true) {
					Message.NORMAL.send(p, "Teleporting...");
				} else {
					Message.ERROR.send(p, "Teleport failed.");
				}
				base.teleportQueue.remove(p.getName());
			}
		}
	}
}
