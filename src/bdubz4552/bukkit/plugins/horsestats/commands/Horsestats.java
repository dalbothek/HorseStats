package bdubz4552.bukkit.plugins.horsestats.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bdubz4552.bukkit.plugins.horsestats.HorseStatsCommand;
import bdubz4552.bukkit.plugins.horsestats.HorseStatsMain;
import bdubz4552.bukkit.plugins.horsestats.Message;

public class Horsestats extends HorseStatsCommand implements CommandExecutor {
	static ChatColor ccg = ChatColor.GREEN;
	static ChatColor ccy = ChatColor.YELLOW;
	
	static String[] help = {ccg + "========================", ccy + "HorseStats by 'bdubz4552'", ccg + "========================", ccy + "Stat Display", ccg + "Grab a lead and punch a horse to return a list of statistics:", ccg + "-MaxHealth -Health -Jump Height -Speed (Blocks per Second)", ccg + "-Can Breed -Is Adult -Owner", ccy + "Noteworthy Things:", ccg + "1) Horses will NOT take damage from the punch", ccg + "2) Speed and jump values are not infinitely precise.", ccy + "Horse Teleporting", ccg + "Grab an ender pearl and punch a horse to select it. The damage will be canceled, and the horse will be selected for teleporting. To teleport the horse, use '/htp' at the desired destination and the horse will teleport to you.", ccy + "To see HorseStats commands, use '/help horsestats'. If this does not work, contact an administrator."};
	
	public Horsestats(HorseStatsMain horseStatsMain) {
		this.main = horseStatsMain;
	}

	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (label.equalsIgnoreCase("horsestats")) {
				p.sendMessage(help);
				Message.NORMAL.send(p, "An old issue with tame but un-owned horses has been brought back by 1.7.9. If horses on this server are tamed, but not owned, hit them with leads and they will be untamed. Unfortunately, you will have to re-tame any affected horses.");
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "[HorseStats] " + ChatColor.RED + "Commands cannot be used in console!");
		}
		return true;
	}
}
