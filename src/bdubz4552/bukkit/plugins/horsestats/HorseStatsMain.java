package bdubz4552.bukkit.plugins.horsestats;

import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import bdubz4552.bukkit.plugins.horsestats.commands.*;
import bdubz4552.bukkit.plugins.horsestats.event.*;
import net.gravitydevelopment.updater.*;

/**
 * Had to do some bug squashing; updater noted for now.
 */
public class HorseStatsMain extends JavaPlugin {
	
	protected Logger log;
	
	/**
	 * If HorseStats is crippled due to mismatched Bukkit versions, this will be true.
	 */
	public boolean crippleMode = false;
	
	/**
	 * Set to true if the config is out of date.
	 */
	public boolean outofdateConfig = false;
		
	/**
	 * Called on plugin start.
	 */
	public void onEnable() {
		this.log = this.getLogger();
		this.saveDefaultConfig();
		registerCommands();
		
		getServer().getPluginManager().registerEvents(new AdminNotificationListener(this), this);
			
		/**
		 * Check if CraftBukkit matches. Warn if not. Register proper event handler based on outcome.
		 */
		if (checkVersion() == false) {
			crippleMode = true;			
			log.warning("The version of CraftBukkit on this server does not match that of HorseStats.");
			log.warning("The HorseStats config file is reporting that this version of HorseStats, " +
			this.getDescription().getVersion() + " is using " + this.getConfig().getConfigurationSection("information").getString("HorseStats Is Running"));
			log.warning("To avoid full plugin failure, the speed value in the stat display will be disabled.");
			log.warning("To fix this issue, simply make sure your Bukkit and HorseStats versions are the same.");
			getServer().getPluginManager().registerEvents(new HorseStatsCrippledEventListener(this), this);
		} else {
			getServer().getPluginManager().registerEvents(new HorseStatsEventListener(this), this);
		}
		
		/**
		 * Check if config is outdated. Warn if so.
		 * 
		 * Config version in config.yml and HorseStats version do not need to match exactly;
		 * if the config is updated, the config version will be updated to match HorseStats version.
		 * Otherwise it will remain the same. 
		 */
		if (this.getConfig().getString("information.configVersion") == null || this.getConfig().getDouble("information.configVersion") != 3.02) {
			outofdateConfig = true;			
			log.warning("It appears your HorseStats configuration file is out of date.");
			log.warning("Please take note of the settings you have in it, and delete it.");
			log.warning("A new configuration with new settings will generate next time you start or reload your server.");
		}
		
		if (configBoolean("allowUpdateChecks") == true) {
			@SuppressWarnings("unused")
			Updater updater = new Updater(this, 62378, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
		}		
	}
	
	/**
	 * Checks if the server version matches the plugin version.
	 * If classes are found, no problem.
	 * If not, cripple mode is activated.
	 * 
	 * When CraftBukkit updates:
	 * 1) Replace imports in Event Handlers <del>and SetStat command</del>
	 * 2) Replace class strings below
	 *
	 * @return Boolean value indicating whether or not CB builds match.
	 */
	private boolean checkVersion() {
		try {
			Class.forName("net.minecraft.server.v1_7_R3.NBTBase");
			Class.forName("org.bukkit.craftbukkit.v1_7_R3.entity.CraftHorse");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * Registers all commands.
	 */
	private void registerCommands() {
		getCommand("horsestats").setExecutor(new Horsestats(this));
		getCommand("htp").setExecutor(new Htp(this, new HorseStatsListenerBase(this)));
		getCommand("setowner").setExecutor(new Setowner(this));
		getCommand("untame").setExecutor(new Untame(this));
		getCommand("delchest").setExecutor(new Delchest(this));
		getCommand("delname").setExecutor(new Delname(this));
		getCommand("slayhorse").setExecutor(new Slayhorse(this));
		getCommand("hspawn").setExecutor(new Hspawn(this));
		getCommand("setstyle").setExecutor(new SetStyle(this));
		getCommand("setstat").setExecutor(new SetStat());
		getCommand("tame").setExecutor(new Tame(this));
	}
	
	/**
	 * Gets a ConfigurationSection object from the given path.
	 * @param sectionName - The path to get the ConfigurationSection for.
	 * @return A ConfigurationSection object matching the given path.
	 */
	public ConfigurationSection section(String sectionName) {
		ConfigurationSection section = this.getConfig().getConfigurationSection(sectionName);
		return section;
	}
	
	/**
	 * Returns the boolean value of the specified option node. This method is <b><i>exclusive</i></b>
	 * to the "options" section of the configuration.
	 * @param configBoolean - The boolean to be checked.
	 * @return The boolean value found in config.
	 */
	public boolean configBoolean(String configBoolean) {
		if (section("options").getBoolean(configBoolean) == true) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the String value of the specified translation node.  This method is <b><i>exclusive</i></b>
	 * to the "translation" section of the configuration.
	 * @param toTranslate - The string to be translated.
	 * @return The translated String.
	 */
	public String translatec(String toTranslate) {
		if (section("translate").getString(toTranslate) == null) {
			log.warning("The string at 'translate." + toTranslate + "' returned null.");
			log.warning("Please check the translate section of your config.yml and make sure that:");
			log.warning("-This node exists");
			log.warning("-This node has a string after it");
		}
		return section("translate").getString(toTranslate);
	}
}
