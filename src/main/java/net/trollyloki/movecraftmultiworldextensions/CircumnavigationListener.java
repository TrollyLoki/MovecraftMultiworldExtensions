package net.trollyloki.movecraftmultiworldextensions;

import static net.countercraft.movecraft.utils.MathUtils.withinWorldBorder;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.events.CraftPreTranslateEvent;

public class CircumnavigationListener implements Listener {
	
	@EventHandler
	public void onPreTranslateEvent(CraftPreTranslateEvent event) {
		if (MovecraftMultiworldExtensions.doCircumnavigation) {
			
			int worldborderX = (int) (event.getWorld().getWorldBorder().getCenter().getX());
			int worldborderZ = (int) (event.getWorld().getWorldBorder().getCenter().getZ());
			int worldborderSize = (int) (event.getWorld().getWorldBorder().getSize());
			
			if (!withinWorldBorder(event.getWorld(), new MovecraftLocation(event.getCraft().getHitBox().getMinX() + event.getDx(), 0, worldborderZ))) {
				event.setDx(worldborderSize - event.getCraft().getHitBox().getXLength());
			}
			else if (!withinWorldBorder(event.getWorld(), new MovecraftLocation(event.getCraft().getHitBox().getMaxX() + event.getDx(), 0, worldborderZ))) {
				event.setDx(-(worldborderSize - event.getCraft().getHitBox().getXLength()));
			}
			
			if (!withinWorldBorder(event.getWorld(), new MovecraftLocation(worldborderX, 0, event.getCraft().getHitBox().getMinZ() + event.getDz()))) {
				event.setDz(worldborderSize - event.getCraft().getHitBox().getZLength());
			}
			else if (!withinWorldBorder(event.getWorld(), new MovecraftLocation(worldborderX, 0, event.getCraft().getHitBox().getMaxZ() + event.getDz()))) {
				event.setDz(-(worldborderSize - event.getCraft().getHitBox().getZLength()));
			}
			
		}
	}
	
}
