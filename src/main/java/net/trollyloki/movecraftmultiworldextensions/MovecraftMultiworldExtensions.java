package net.trollyloki.movecraftmultiworldextensions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class MovecraftMultiworldExtensions extends JavaPlugin {
	
	private static MovecraftMultiworldExtensions instance;
	public static boolean doCircumnavigation = false, doHeightSwitching = false, doRegionSwitching = false, doMultiversePortals = false;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		reload();
		
		getServer().getPluginManager().registerEvents(new CircumnavigationListener(), this);
		getServer().getPluginManager().registerEvents(new HeightSwitchingListener(), this);
		
		if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
			getServer().getPluginManager().registerEvents(new RegionSwitchingListener(), this);
		}
		else {
			getLogger().info("WorldGuard not found. Region based switching will not be available.");
		}

		if (getServer().getPluginManager().getPlugin("Multiverse-Portals") != null) {
			getServer().getPluginManager().registerEvents(new MultiversePortalsListener(), this);
			getLogger().info("Multiverse-Portals support enabled.");
		}
		
	}
	
	@Override
	public void onDisable() {
		instance = null;
	}
	
	public static MovecraftMultiworldExtensions getInstance() {
		return instance;
	}
	
	public static void reload() {
		instance.reloadConfig();
		doCircumnavigation = instance.getConfig().getBoolean("circumnavigation.enabled");
		doHeightSwitching = instance.getConfig().getBoolean("height-switching.enabled");
		doRegionSwitching = instance.getConfig().getBoolean("region-switching.enabled");
		doMultiversePortals = instance.getConfig().getBoolean("multiverse-portals");
		
		if (doHeightSwitching) {
			HeightSwitchingListener.heights.clear();
			for (String keyWorld : instance.getConfig().getConfigurationSection("height-switching.heights").getKeys(false)) {
				Map<String, Integer> map = new HashMap<String, Integer>();
				for (String world : instance.getConfig().getConfigurationSection("height-switching.heights." + keyWorld).getKeys(false)) {
					map.put(world, instance.getConfig().getInt("height-switching.heights." + keyWorld + "." + world));
				}
				HeightSwitchingListener.heights.put(keyWorld, map);
			}
		}
		
		if (doRegionSwitching) {
			RegionSwitchingListener.regions.clear();
			for (String region : instance.getConfig().getConfigurationSection("region-switching.regions").getKeys(false)) {
				RegionSwitchingListener.regions.put(region, instance.getConfig().getString("region-switching.regions." + region));
			}
		}
	}
	
}
