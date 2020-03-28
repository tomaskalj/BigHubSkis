package com.pvpraids.hub.listeners;

import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.server.ServerManager;
import com.pvpraids.hub.utilities.Cooldowns;
import com.pvpraids.hub.utilities.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

	private HubPlugin main = HubPlugin.getInstance();
	private ServerManager serverManager = main.getServerManager();

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		event.setJoinMessage(null);

        if (serverManager.getSpawnLocation() != null) {
            player.teleport(serverManager.getSpawnLocation());
        }

		serverManager.giveGadgets(player);

        if (!main.getConfig().getString("motd").isEmpty()) {
            player.sendMessage(StringUtils.color(main.getConfig().getString("motd")));
        }

		for (Player online : Bukkit.getOnlinePlayers()) {
			online.hidePlayer(player);
			player.hidePlayer(online);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		event.setQuitMessage(null);
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("hub.build")) {
            event.setCancelled(true);
        }
	}

	@EventHandler
	public void onSheepDye(SheepDyeWoolEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {

        if (!event.getPlayer().hasPermission("hub.build")) {
            event.setCancelled(true);
        }
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

        if (player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }

        if (event.getCurrentItem() == null) {
            return;
        }

        if (!event.getCurrentItem().hasItemMeta()) {
            return;
        }

		if (event.getInventory().getTitle().contains("Server Selection")) {
			event.setCancelled(true);

			if (Cooldowns.tryCooldown(player.getUniqueId(), "gui-cooldown", 3000)) {
				Server server = serverManager.getServerFromItem(event.getCurrentItem());

				player.closeInventory();

                if (server != null) {
                    if (server.isWhitelisted()) {
                        player.sendMessage(StringUtils.color("&cThis server is currently whitelisted."));
                        return;
                    }

                    if (server.getServerQueue().isEnabled()) {
                        serverManager.addToServerQueue(player, server);
                        return;
                    }

                    serverManager.connectToServer(player, server.getName());
                } else {
                    player.closeInventory();
                }
			} else {
				player.sendMessage(StringUtils.color("&cYou can use this again in &c&l" + (Cooldowns.getCooldown(player.getUniqueId(), "gui-cooldown") / 1000) + " &cseconds."));
				player.closeInventory();
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
        if (event.getTo().getBlockY() <= 0) {
            event.getPlayer().teleport(serverManager.getSpawnLocation());
        }
	}

	@EventHandler
	public void onFoodLoss(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().remove();
		ItemStack droppedItem = event.getItemDrop().getItemStack().clone();

        if (droppedItem.getType() == Material.WRITTEN_BOOK) {
            event.getPlayer().getInventory().setItem(1, droppedItem);
        } else if (droppedItem.getType() == Material.COMPASS) {
            event.getPlayer().getInventory().setItem(4, droppedItem);
        } else if (droppedItem.getType() == Material.FEATHER) {
            event.getPlayer().getInventory().setItem(7, droppedItem);
        } else {
            event.setCancelled(true);
        }
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
	}

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
        if (!event.getPlayer().hasPermission("hub.build")) {
            event.setCancelled(true);
        }
	}

	@EventHandler
	public void onStomp(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
			event.setCancelled(true);
	}
}
