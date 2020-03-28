package com.pvpraids.hub.listeners;

import com.pvpraids.hub.HubPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class GadgetListeners implements Listener {

	private HubPlugin main = HubPlugin.getInstance();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (player.getInventory().getItemInHand() == null) {
			return;
		}

		if (!player.getInventory().getItemInHand().hasItemMeta()) {
			return;
		}

		if (player.getInventory().getItemInHand().getType() == Material.FEATHER) {
			if (event.getAction().toString().contains("LEFT") || event.getAction().toString().contains("RIGHT")) {
				event.setCancelled(true);

				if ((!player.hasMetadata("hopper") || (player.getMetadata("hopper").get(0)).asInt() == 1) && !player.isFlying()) {
					player.setVelocity(new Vector(player.getLocation().getDirection().getX() * 2.0D, 1D, player.getLocation().getDirection().getZ() * 2.0D));

					if (!player.hasMetadata("hopper")) {
						player.setMetadata("hopper", new FixedMetadataValue(main, 1));
					} else {
						player.setMetadata("hopper", new FixedMetadataValue(main, 2));
					}
				}
			}
		}

		if (player.getInventory().getItemInHand().getType() == Material.COMPASS && player.getInventory().getItemInHand().hasItemMeta()) {
			if (event.getAction().toString().contains("RIGHT")) {
				main.getServerManager().showServerSelectionInventory(player);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (player.hasMetadata("hopper") && player.isOnGround()) {
			player.removeMetadata("hopper", main);
		}
	}
}
