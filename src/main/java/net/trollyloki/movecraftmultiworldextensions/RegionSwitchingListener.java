package net.trollyloki.movecraftmultiworldextensions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.events.CraftPreTranslateEvent;
import net.countercraft.movecraft.utils.BitmapHitBox;

public class RegionSwitchingListener implements Listener {
	
	public static Map<String, String> regions = new HashMap<String, String>();
	
	@EventHandler
	public void onPreTranslateEvent(CraftPreTranslateEvent event) {
		if (MovecraftMultiworldExtensions.doRegionSwitching) {
			
			RegionManager rm = WGBukkit.getRegionManager(event.getCraft().getW());
			
			BitmapHitBox hitbox = event.getCraft().getHitBox();
			/*Vector pos1 = new com.sk89q.worldedit.Vector(hitbox.getMinX() + event.getDx(), hitbox.getMinY() + event.getDy(), hitbox.getMinZ() + event.getDz());
			Vector pos2 = new com.sk89q.worldedit.Vector(hitbox.getMaxX() + event.getDx(), hitbox.getMaxY() + event.getDy(), hitbox.getMaxZ() + event.getDz());
			CuboidRegion region = new com.sk89q.worldedit.regions.CuboidRegion(pos1, pos2);*/
			
			// Check each defined region to see if a block is in it - probably faster
			for (String regionName : regions.keySet()) {
				ProtectedRegion region = rm.getRegion(regionName);
				for (MovecraftLocation location : hitbox) {
					if (region.contains(location.getX(), location.getY(), location.getZ())) {
						event.setWorld(Bukkit.getWorld(regions.get(regionName)));
					}
				}
			}
			
			// Check regions for every block method - probably very slow
			/*for (MovecraftLocation location : hitbox) {
				ApplicableRegionSet applicableRegions = rm.getApplicableRegions(new Location(event.getCraft().getW(), location.getX(), location.getY(), location.getZ()));
				for (ProtectedRegion region : applicableRegions) {
					if (regions.containsKey(region.getId())) {
						event.setWorld(Bukkit.getWorld(regions.get(region.getId())));
						return;
					}
				}
			}*/
			
		}
	}
	
}
