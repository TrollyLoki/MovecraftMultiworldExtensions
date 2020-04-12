package net.trollyloki.movecraftmultiworldextensions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.countercraft.movecraft.events.CraftPreTranslateEvent;

public class WorldSwitchingListener implements Listener {
	
	public static Map<String, Map<String, Integer>> heights = new HashMap<String, Map<String, Integer>>();
	
	@EventHandler
	public void onPreTranslateEvent(CraftPreTranslateEvent event) {
		if (MovecraftMultiworldExtensions.doWorldSwitching) {
			
			String craftWorld = event.getWorld().getName();
			Map<String, Integer> switchHeights = heights.get(craftWorld);
			int maxY = event.getCraft().getHitBox().getMaxY() + event.getDy();
			int minY = event.getCraft().getHitBox().getMinY() + event.getDy();
			
			for (String world : switchHeights.keySet()) {
				int switchHeight = switchHeights.get(world);
				if (maxY >= switchHeight || minY <= switchHeights.get(world)) {
					
					event.setWorld(Bukkit.getWorld(world));
					int destHeight = heights.get(world).get(craftWorld);
					if (destHeight > switchHeight) event.setDy(destHeight - event.getCraft().getHitBox().getMaxY());
					else event.setDy(destHeight - event.getCraft().getHitBox().getMinY());
					
				}
			}
			
		}
	}
	
}
