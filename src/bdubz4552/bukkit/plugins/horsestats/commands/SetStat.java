package bdubz4552.bukkit.plugins.horsestats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import bdubz4552.bukkit.plugins.horsestats.HorseStatsCommand;
import bdubz4552.bukkit.plugins.horsestats.Message;

public class SetStat extends HorseStatsCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Horse h = null;
			if (p.isInsideVehicle()) {
				if (p.getVehicle() instanceof Horse) {
					h = (Horse) p.getVehicle();
				}
			}
			if (label.equalsIgnoreCase("setstat")) {
				if (this.permCheck(p, "setstat")) {
					this.run(p, h, args);
				}
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "[HorseStats] " + ChatColor.RED + "Commands cannot be used in console!");
		}
		return true;
	}
	
	public void run(Player p, Horse h, String args[]) {
		if (h != null) {
			if (h.getOwner() == p) {
				if (args.length == 2) {			
					if (args[0].equalsIgnoreCase("health")) {
						double health = Double.parseDouble(args[1]);
						h.setMaxHealth(2 * health);
						Message.NORMAL.send(p, "Horse's health has been set to" + " " + health + " " + "hearts.");
					} else if (args[0].equalsIgnoreCase("jump")) {
						double jump = Double.parseDouble(args[1]);
						if (jump > 22) {
							jump = 22;
							Message.NORMAL.send(p, "Horses cannot jump higher than 22 blocks.");
						}
						h.setJumpStrength(Math.sqrt(jump / 5.5));
						Message.NORMAL.send(p, "Horse's jump height has been set to" + " " + jump + " " + "blocks.");
					} else {
						Message.ERROR.send(p, usageList[2]);
					}
				} else {
					Message.ERROR.send(p, usageList[2]);
				}
			} else {
				Message.OWNER.send(p);
			}
		} else {
			Message.RIDING.send(p);
		}
	}
}
