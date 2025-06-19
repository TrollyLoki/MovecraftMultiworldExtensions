package net.trollyloki.movecraftmultiworldextensions;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.events.CraftPreTranslateEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;
import org.mvplugins.multiverse.portals.MVPortal;
import org.mvplugins.multiverse.portals.MultiversePortals;
import org.mvplugins.multiverse.portals.MultiversePortalsApi;
import org.mvplugins.multiverse.portals.destination.PortalDestinationInstance;
import org.mvplugins.multiverse.portals.utils.MultiverseRegion;
import org.mvplugins.multiverse.portals.utils.PortalManager;

public class MultiversePortalsListener implements Listener {

    @EventHandler
    public void onPreTranslate(CraftPreTranslateEvent event) {
        if (!MovecraftMultiworldExtensions.doMultiversePortals) return;

        PortalManager portalManager = MultiversePortalsApi.get().getPortalManager();

        for (MovecraftLocation location : event.getCraft().getHitBox()) {
            Location newLocation = location.translate(event.getDx(), event.getDy(), event.getDz()).toBukkit(event.getWorld());

            MVPortal portal = portalManager.getPortal(newLocation);
            if (portal == null) continue;

            if (event.getCraft() instanceof PlayerCraft playerCraft) {
                //TODO: Check costs
                if (MultiversePortals.EnforcePortalAccess && !portal.playerCanEnterPortal(playerCraft.getPilot())) {
                    //TODO: Send message?
                    event.setCancelled(true);
                    return;
                }
            }

            Location destinationLocation = null;
            if (event.getCraft() instanceof PlayerCraft playerCraft) {
                destinationLocation = portal.getDestination().getLocation(playerCraft.getPilot()).getOrNull();
            } else {
                try {
                    destinationLocation = portal.getDestination().getLocation(null).getOrNull();
                } catch (NullPointerException e) {
                    MovecraftMultiworldExtensions.getInstance().getLogger().warning("Unable to get destination location of portal " + portal.getName() + " without a player");
                    event.setCancelled(true);
                }
            }
            if (destinationLocation == null) return;

            if (destinationLocation.getWorld() != null) {
                event.setWorld(destinationLocation.getWorld());
            }
            MovecraftLocation midpoint = event.getCraft().getHitBox().getMidPoint();
            event.setDx(destinationLocation.getBlockX() - midpoint.getX());
            event.setDy(destinationLocation.getBlockY() - event.getCraft().getHitBox().getMinY());
            event.setDz(destinationLocation.getBlockZ() - midpoint.getZ());

            if (portal.getDestination() instanceof PortalDestinationInstance) {
                MVPortal destinationPortal = portalManager.getPortal(destinationLocation);
                if (destinationPortal == null) return;

                Vector shift = new Vector(0, 0, 1).rotateAroundY(-Math.toRadians(destinationLocation.getYaw()));
                shift.setX(normalize(shift.getX()));
                shift.setZ(normalize(shift.getZ()));

                MultiverseRegion region = destinationPortal.getLocation().getRegion();
                if (shift.getX() > 0) {
                    event.setDx(region.getMaximumPoint().getBlockX() - event.getCraft().getHitBox().getMinX() + 1);
                } else if (shift.getX() < 0) {
                    event.setDx(region.getMinimumPoint().getBlockX() - event.getCraft().getHitBox().getMaxX() - 1);
                }
                if (shift.getZ() > 0) {
                    event.setDz(region.getMaximumPoint().getBlockZ() - event.getCraft().getHitBox().getMinZ() + 1);
                } else if (shift.getZ() < 0) {
                    event.setDz(region.getMinimumPoint().getBlockZ() - event.getCraft().getHitBox().getMaxZ() - 1);
                }
            }
            return;

        }

    }

    private static double normalize(double x) {
        if (x > Vector.getEpsilon()) return 1;
        if (x < -Vector.getEpsilon()) return -1;
        return 0;
    }

}
