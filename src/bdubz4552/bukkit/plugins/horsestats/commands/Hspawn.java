package bdubz4552.bukkit.plugins.horsestats.commands;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;

import bdubz4552.bukkit.plugins.horsestats.HorseStatsCommand;
import bdubz4552.bukkit.plugins.horsestats.HorseStatsMain;
import bdubz4552.bukkit.plugins.horsestats.Message;

public class Hspawn extends HorseStatsCommand implements CommandExecutor {
	
	public Hspawn(HorseStatsMain horseStatsMain) {
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
			if (label.equalsIgnoreCase("hspawn")) {
				if (this.permCheck(p, "hspawn")) {
					this.run(p, h, args);
				}
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "[HorseStats] " + ChatColor.RED + "Commands cannot be used in console!");
		}
		return true;
	}
	
	public void run(Player p, Horse h, String[] args) {
		List<Entity> check = p.getNearbyEntities(1, 1, 1);
		if (h == null) {
			if (check.size() == 0) {					
				Variant v = null;
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("donkey")) {
						v = Variant.DONKEY;
						Message.NORMAL.send(p, "Donkey spawned successfully.");
					} else if (args[0].equalsIgnoreCase("mule")) {
						v = Variant.MULE;
						Message.NORMAL.send(p, "Mule spawned successfully.");
					} else {
						Message.NORMAL.send(p, usageList[0]);
					}
				} else {
					v = Variant.HORSE;
				}
				p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
				List<Entity> l = p.getNearbyEntities(1, 1, 1);
				h = (Horse) l.get(0);
				h.setAdult();
				h.setVariant(v);
				if (v == Variant.HORSE) {
					Random rand = new Random();
					Color[] c = {Color.BLACK, Color.BROWN, Color.CHESTNUT, Color.CREAMY, Color.DARK_BROWN, Color.GRAY, Color.WHITE};
					Style[] s = {Style.BLACK_DOTS, Style.NONE, Style.WHITE, Style.WHITE_DOTS, Style.WHITEFIELD};
					int x = rand.nextInt(7);
					int y = rand.nextInt(5);
					h.setColor(c[x]);
					h.setStyle(s[y]);
					Message.NORMAL.send(p, "Horse spawned successfully.");
				}
			} else if (check.size() >= 1) {
				Message.ERROR.send(p, "Another entity is too close by, and the horse could not be spawned. Be sure there is at least two blocks between you and nearby mobs, paintings, itemframes, and leads.");
			}
		} else {
			Message.ERROR.send(p, "You cannot be riding a horse while you use this command!");
		}
	}
}
