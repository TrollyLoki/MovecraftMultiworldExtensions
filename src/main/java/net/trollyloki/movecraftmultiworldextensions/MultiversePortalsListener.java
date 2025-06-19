package net.trollyloki.movecraftmultiworldextensions;

import net.countercraft.movecraft.craft.PlayerCraft;
import net.countercraft.movecraft.events.CraftPreTranslateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.WorldManager;
import org.mvplugins.multiverse.portals.MVPortal;
import org.mvplugins.multiverse.portals.MultiversePortalsApi;
import org.mvplugins.multiverse.portals.destination.PortalDestinationInstance;
import org.mvplugins.multiverse.portals.utils.PortalManager;

import java.util.List;

public class MultiversePortalsListener implements Listener {

    @EventHandler
    public void onPreTranslate(CraftPreTranslateEvent event) {
        if (!MovecraftMultiworldExtensions.doMultiversePortals) return;

        WorldManager worldManager = MultiverseCoreApi.get().getWorldManager();
        PortalManager portalManager = MultiversePortalsApi.get().getPortalManager();

        MultiverseWorld mvWorld = worldManager.getWorld(event.getWorld()).getOrNull();
        if (mvWorld == null) return;

        Player player = null;
        if (event.getCraft() instanceof PlayerCraft playerCraft) {
            player = playerCraft.getPilot();
        }

        for (MVPortal portal : portalManager.getPortals(player, mvWorld)) {
            if (!(portal.getDestination() instanceof PortalDestinationInstance destination)) continue;

            //TODO: Figure out if portal applies and where the craft should be moved to
        }

    }

}
