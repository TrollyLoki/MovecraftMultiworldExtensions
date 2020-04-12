package net.trollyloki.movecraftmultiworldextensions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

public class MovecraftMultiworldExtensions extends JavaPlugin {
	
	private static MovecraftMultiworldExtensions instance;
	public static boolean doCircumnavigation = false, doWorldSwitching = false;
	
	@Override
	public void onEnable() {
		instance = this;
		reload();
		
		getServer().getPluginManager().registerEvents(new CircumnavigationListener(), this);
		getServer().getPluginManager().registerEvents(new WorldSwitchingListener(), this);
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
		doWorldSwitching = instance.getConfig().getBoolean("world-switching.enabled");
		
		WorldSwitchingListener.heights.clear();
		for (String keyWorld : instance.getConfig().getConfigurationSection("world-switching.heights").getKeys(false)) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			for (String world : instance.getConfig().getConfigurationSection("world-switching.heights." + keyWorld).getKeys(false)) {
				map.put(world, instance.getConfig().getInt("world-switching.heights." + keyWorld + "." + world));
			}
			WorldSwitchingListener.heights.put(keyWorld, map);
		}
	}
	
}
