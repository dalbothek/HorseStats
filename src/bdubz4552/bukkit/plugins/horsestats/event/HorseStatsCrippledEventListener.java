package bdubz4552.bukkit.plugins.horsestats.event;

import org.bukkit.Material;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import bdubz4552.bukkit.plugins.horsestats.HorseStatsMain;
import bdubz4552.bukkit.plugins.horsestats.Message;

/**
 * A dupe of the main listener, except this one does not import CraftBukkit.
 * Events are registered here instead of the main when CB builds don't match.
 */
public class HorseStatsCrippledEventListener extends HorseStatsListenerBase implements Listener {
	
	public HorseStatsCrippledEventListener(HorseStatsMain horseStatsMain) {
		super (horseStatsMain);
	}
	
	@EventHandler
	/**
	 * Event listener for nonOwnerHorseInteraction and saddleLock config settings.
	 * Conditionals are separated to prevent any contingencies.
	 * @param event - The PlayerInteractEntityEvent that triggered this.
	 */
	public void onHorseInventoryOpen(PlayerInteractEntityEvent event) {
		//nonOwnerHorseInteraction
		if (main.configBoolean("nonOwnerHorseInteraction") == false) {
			Player p = event.getPlayer();
			if (event.getRightClicked() instanceof Horse) {
				Horse h = (Horse) event.getRightClicked();
				if (h.getOwner() != p && h.getOwner() != null) {
					event.setCancelled(true);
				}
			}
		}		
		//saddleLock
		if (main.configBoolean("saddleLock") == true) {
			Player p = event.getPlayer();
			if (event.getRightClicked() instanceof Horse) {
				Horse h = (Horse) event.getRightClicked();
				if (h.getOwner() != p && h.getOwner() != null) {
					if (h.getInventory().getSaddle() != null) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	/**
	 * This event is called whenever an entity is hurt by another entity.
	 * The first if statement quickly drops this if it is not a player and horse.
	 * @param event - The EntityDamageByEntityEvent that triggered this.
	 */
	public void onHorseHit(EntityDamageByEntityEvent event) {
		//A horse is hit
		if (event.getEntity() instanceof Horse) {
			Horse h = (Horse) event.getEntity();
			//A player hit it
			if (event.getDamager() instanceof Player) {
				Player p = (Player) event.getDamager();
				//They held a lead
				if (p.getItemInHand().getType() == Material.LEASH) {
					event.setCancelled(true);
					displayStats(p, h);
				//They held an ender pearl
				} else if (p.getItemInHand().getType() == Material.ENDER_PEARL) {
					event.setCancelled(true);
					teleportToggle(p, h);
				//They held something else
				} else {
					if (main.configBoolean("horseGrief") == false) {
						if (p != h.getOwner() && h.getOwner() != null) {
							event.setCancelled(true);
							Message.ATTACK.send(p);
						}
					}
				}				
			}
			//An arrow hit it
			if (main.configBoolean("horseGrief") == false) {
				if (event.getDamager() instanceof Arrow) {
					Arrow a = (Arrow) event.getDamager();
					if (a.getShooter() instanceof Player) {
						Player p = (Player) a.getShooter();
						if (h.getOwner() != null) {
								event.setCancelled(true);
								Message.ATTACK.send(p);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Display the stats of a horse to a player.
	 * @param p - The player to send stats to.
	 * @param horse - The horse who's stats are to be fetched.
	 */
	public void displayStats(Player p, Horse horse) {
		//Because its still an issue, ownership correction is back.
		if (horse.getOwner() == null && horse.isTamed() == true) {
			horse.setTamed(false);
			Message.NORMAL.send(p, "This horse was found to be tamed, but the owner returned 'none'. It is now fully untamed.");
		}
		
		//RAW data (heh)
		double healthMax = horse.getMaxHealth();
		double heartMax = healthMax / 2;
		double health = horse.getHealth();
		double heart = health / 2;
		double jump = 5.5 * (Math.pow(horse.getJumpStrength(), 2));
		boolean adult = horse.isAdult();
		boolean breed = horse.canBreed();
		float age = horse.getAge();
		AnimalTamer tamer = horse.getOwner();
		
		//Horse name
		String name = horse.getCustomName();
		if (name == null) {
			name = "Horse";
		} else {
			name = name + "'s";
		}
		
		//Teleport status
		boolean tpStatus;
		if (teleportQueue.containsValue(horse)) {
			tpStatus = true;
		} else {
			tpStatus = false;
		}
		
		//Age status
		String adultMsg;
		if (adult == false) {
			adultMsg = "Is Adult:" + " " + adult + " (" + "Minutes Until Adult:" + " " + age / -20 / 60 + ")";
		} else {
			adultMsg = "Is Adult:" + " " + adult;
		}
		
		//Message output
		Message.STAT.send(p, "========================");
		Message.STAT.send(p, name + " " + "Stats");
		Message.STAT.send(p, "========================");
		Message.STAT.send(p, "Max Health:" + " " + (float) healthMax + " (" + (int) heartMax + " " + "hearts" + ")");
		Message.STAT.send(p, "Health:" + " " + (float) health + " (" + (int) heart + " " + "hearts" + ")");
		Message.STAT.send(p, "Jump Height (Blocks):" + " " + (float) jump);
		Message.STAT.send(p, "Speed could not be loaded.");
		Message.STAT.send(p, "Can Breed:" + " " + breed);
		Message.STAT.send(p, "Is Selected For Teleport:" + " " + tpStatus);
		Message.STAT.send(p, adultMsg);
		if (tamer != null) {
			Message.STAT.send(p, "Owner:" + " " + tamer.getName());
		} else {
			Message.STAT.send(p, "Owner: None");
		}
	}
	
	/**
	 * Toggles teleport selection for a horse.
	 * @param p - The player who attempted the toggle.
	 * @param horse - The horse who teleport is being toggled for.
	 */
	public void teleportToggle(Player p, Horse horse) {
		if (horse.getOwner() == p) {
			if (teleportQueue.containsKey(p.getName())) {
				teleportQueue.remove(p.getName());
				Message.NORMAL.send(p, "Horse deselected for teleport.");
			} else {
				teleportQueue.put(p.getName(), horse);
				Message.NORMAL.send(p, "Horse selected for teleport.");
			}
		} else if (horse.getOwner() == null) { 
			Message.ERROR.send(p, "You cannot select an untamed horse for teleporting!");
		} else {
			Message.ERROR.send(p, "You cannot select someone else's horse for teleporting!");
		}
	}
}


