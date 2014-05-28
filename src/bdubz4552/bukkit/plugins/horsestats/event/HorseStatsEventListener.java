package bdubz4552.bukkit.plugins.horsestats.event;

import net.minecraft.server.v1_7_R3.NBTBase;
import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftHorse;
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
 * The main listener. Imports CraftBukkit code and can break with version changes.
 * Events are registered here if server CB precisely matches HorseStats CB.
 */
public class HorseStatsEventListener extends HorseStatsListenerBase implements Listener {
		
	public HorseStatsEventListener(HorseStatsMain horseStatsMain) {
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
					Message.OWNER.send(p);
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
						Message.OWNER.send(p);
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
			else if (main.configBoolean("horseGrief") == false) {
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
			adultMsg = "Is Adult: " + adult + " (Minutes Until Adult: " + age / -20 / 60 + ")";
		} else {
			adultMsg = "Is Adult: " + adult;
		}
		
		//Message output
		Message.STAT.send(p, "========================");
		Message.STAT.send(p, name + " Stats");
		Message.STAT.send(p, "========================");
		Message.STAT.send(p, "Max Health: " + (float) healthMax + " (" + (int) heartMax + " " + "hearts" + ")");
		Message.STAT.send(p, "Health: " + (float) health + " (" + (int) heart + " " + "hearts" + ")");
		Message.STAT.send(p, "Jump Height (Blocks): " + (float) jump);
		Message.STAT.send(p, "Speed (Blocks per Second):" + " " + (float) getSpeed(horse) * 43);		
		Message.STAT.send(p, "Can Breed: " + breed);
		Message.STAT.send(p, "Is Selected For Teleport: " + tpStatus);
		Message.STAT.send(p, adultMsg);
		if (tamer != null) {
			Message.STAT.send(p, "Owner: " + tamer.getName());
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
	
	/**
	 * The 'fragile' code used to retrieve horse speed. NBT stuff.
	 * Needs re-imported when NBT code changes, or build number changes (e.g. CB 1.7.2-R0.1 to CB 1.7.2-R0.2)
	 * @param horse - The horse that was hit with a lead.
	 * @return Double that represents horse speed.
	 */
	public double getSpeed(Horse horse) {
		CraftHorse cHorse = (CraftHorse) horse;
		NBTTagCompound compound = new NBTTagCompound();
		cHorse.getHandle().b(compound);
		double speed = -1;
		NBTTagList list = (NBTTagList) compound.get("Attributes");
		for(int i = 0; i < list.size() ; i++) {
			NBTBase base = list.get(i);
			if (base.getTypeId() == 10) {
				NBTTagCompound attrCompound = (NBTTagCompound)base;
				if (base.toString().contains("generic.movementSpeed")) {
					speed = attrCompound.getDouble("Base");
				}
			}
		}
		return speed;
	}		
}


