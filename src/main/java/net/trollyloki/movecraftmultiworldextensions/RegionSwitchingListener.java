package net.trollyloki.movecraftmultiworldextensions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.events.CraftPreTranslateEvent;
import net.countercraft.movecraft.util.hitboxes.HitBox;

public class RegionSwitchingListener implements Listener {
	
	public static Map<String, String> regions = new HashMap<String, String>();
	
	@EventHandler
	public void onPreTranslateEvent(CraftPreTranslateEvent event) {
		if (MovecraftMultiworldExtensions.doRegionSwitching) {
			
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionManager rm = container.get(BukkitAdapter.adapt(event.getWorld()));
			
			HitBox hitbox = event.getCraft().getHitBox();
			List<MovecraftLocation> locations = hitbox.asSet().stream()
					.map(location -> location.translate(event.getDx(), event.getDy(), event.getDz()))
					.toList();
			/*Vector pos1 = new com.sk89q.worldedit.Vector(hitbox.getMinX() + event.getDx(), hitbox.getMinY() + event.getDy(), hitbox.getMinZ() + event.getDz());
			Vector pos2 = new com.sk89q.worldedit.Vector(hitbox.getMaxX() + event.getDx(), hitbox.getMaxY() + event.getDy(), hitbox.getMaxZ() + event.getDz());
			CuboidRegion region = new com.sk89q.worldedit.regions.CuboidRegion(pos1, pos2);*/
			
			// Check each defined region to see if a block is in it - probably faster
			for (String regionName : regions.keySet()) {
				ProtectedRegion region = rm.getRegion(regionName);
				if (region != null) for (MovecraftLocation location : locations) {
					if (region.contains(location.getX(), location.getY(), location.getZ())) {
						event.setWorld(Bukkit.getWorld(regions.get(regionName)));
						return;
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
