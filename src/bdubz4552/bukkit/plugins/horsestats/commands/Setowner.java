package bdubz4552.bukkit.plugins.horsestats.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import bdubz4552.bukkit.plugins.horsestats.HorseStatsCommand;
import bdubz4552.bukkit.plugins.horsestats.HorseStatsMain;
import bdubz4552.bukkit.plugins.horsestats.Message;

public class Setowner extends HorseStatsCommand implements CommandExecutor {
	
	public Setowner(HorseStatsMain horseStatsMain) {
		this.main = horseStatsMain;
	}

	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Horse h = null;
			if (p.isInsideVehicle()) {
				if (p.getVehicle() instanceof Horse) {
					h = (Horse) p.getVehicle();
				}
			}
			if (label.equalsIgnoreCase("setowner")) {
				if (this.permCheck(p, "setowner")) {
					this.run(p, h, args);
				}
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "[HorseStats] " + ChatColor.RED + "Commands cannot be used in console!");
		}
		return true;
	}
	
	/**
	 * As long as Player objects remain consistent, we shouldn't have name issues.
	 */
	@SuppressWarnings("deprecation")
	public void run(Player p, Horse h, String[] args) {
		if (h != null) {
			if (h.getOwner() == p) {
				if (args.length == 1) {					
					if (Bukkit.getServer().getPlayerExact(args[0]) != null) {
						h.eject();
						Message.NORMAL.send(p, "Owner changed successfully.");
						h.setOwner(p.getServer().getPlayerExact(args[0]));
					} else {
						Message.PLAYER.send(p);
					}
				} else {
					Message.ERROR.send(p, usageList[1]);
				}
			} else {
				Message.OWNER.send(p);
			}
		} else {
			Message.RIDING.send(p);
		}
	}
}
