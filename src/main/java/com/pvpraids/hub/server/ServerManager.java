package com.pvpraids.hub.server;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.pvpraids.core.utils.item.ItemBuilder;
import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.utilities.HiddenStringUtils;
import com.pvpraids.hub.utilities.LocationSerialization;
import com.pvpraids.hub.utilities.StringUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerManager {
	private HubPlugin main = HubPlugin.getInstance();
	private MongoCollection<Document> serverCollection = HubPlugin.getInstance().getMongoDatabase().getCollection("servers");
	@Getter
	private ArrayList<Server> servers = new ArrayList<>();
	@Getter
	@Setter
	private Location spawnLocation;

	public ServerManager() {
		load();

		new BukkitRunnable() {
			@Override
			public void run() {
				save();
			}
		}.runTaskTimer(main, 0L, 300 * 20L);
	}

	public void load() {
        if (main.getConfig().getString("spawn-location") != null && !main.getConfig().getString("spawn-location").isEmpty()) {
            spawnLocation = LocationSerialization.deserializeLocation(main.getConfig().getString("spawn-location"));
        }

		main.getLogger().log(Level.INFO, "Loading " + serverCollection.countDocuments() + " servers...");
		long startTime = System.currentTimeMillis();

		for (Document document : serverCollection.find()) {
            if (document.getString("name") != null) {
                String name = document.getString("name");
                String displayName = document.getString("displayName");
                List<String> displayLore = new ArrayList<>();
                int maxPlayerCount = 0, serverSlot = 0;
                Material serverIcon = Material.STONE;
                boolean whitelisted = false, queueEnabled = true, queuePaused = true;

                if (document.get("displayLore") != null) {
                    displayLore = (List) document.get("displayLore");
                }
                if (document.getInteger("maxPlayerCount") != null) {
                    maxPlayerCount = document.getInteger("maxPlayerCount");
                }
                if (Material.getMaterial(document.getString("serverIcon")) != null) {
                    serverIcon = Material.valueOf(document.getString("serverIcon"));
                }
                if (document.getInteger("serverSlot") != null) {
                    serverSlot = document.getInteger("serverSlot");
                }
                if (document.getBoolean("whitelisted") != null) {
                    whitelisted = document.getBoolean("whitelisted");
                }
                if (document.getBoolean("queueEnabled") != null) {
                    queueEnabled = document.getBoolean("queueEnabled");
                }

                Server server = new Server(name);
                server.setDisplayName(displayName);
                server.setDisplayLore(displayLore);
                server.setMaxPlayerCount(maxPlayerCount);
                server.setServerIcon(serverIcon);
                server.setServerSlot(serverSlot);
                server.setWhitelisted(whitelisted);
                server.getServerQueue().setEnabled(queueEnabled);
                server.getServerQueue().setPaused(queuePaused);

                servers.add(server);

                System.out.println("Server: " + server.getName() + " | DisplayName: " + displayName);
                System.out.println("  Max Player Count: " + server.getMaxPlayerCount());
                System.out.println("  Server Icon: " + server.getServerIcon());
                System.out.println("  Server Slot: " + server.getServerSlot());
                System.out.println("  Whitelisted: " + server.isWhitelisted());
                System.out.println("  Queue Enabled: " + server.getServerQueue().isEnabled());
                System.out.println("  Queue Paused: " + server.getServerQueue().isPaused());
            } else {
                serverCollection.deleteOne(document);
            }
		}

		main.getLogger().log(Level.INFO, "Successfully loaded " + servers.size() + " servers. (Time Spent: " + (System.currentTimeMillis() - startTime) + "ms)");
	}

	public void save() {
        if (spawnLocation != null) {
            main.getConfig().set("spawn-location", LocationSerialization.serializeLocation(spawnLocation));
        }
		main.saveConfig();

		main.getLogger().log(Level.INFO, "Saving " + servers.size() + " servers...");
		long startTime = System.currentTimeMillis();

        for (Server server : servers) {
            if (server.getName() != null) {
                Document document = new Document("name", server.getName());
                document.append("displayName", server.getDisplayName());
                document.append("displayLore", server.getDisplayLore());
                document.append("MaxPlayerCount", server.getMaxPlayerCount());
                document.append("serverIcon", server.getServerIcon().toString());
                document.append("serverSlot", server.getServerSlot());
                document.append("whitelisted", server.isWhitelisted());
                document.append("queueEnabled", server.getServerQueue().isEnabled());
                document.append("queuePaused", server.getServerQueue().isPaused());

                serverCollection.replaceOne(Filters.eq("name", server.getName()), document, new ReplaceOptions().upsert(true));
            }
        }


		main.getLogger().log(Level.INFO, "Successfully saved " + servers.size() + " servers. (Time Spent: " + (System.currentTimeMillis() - startTime) + "ms)");
	}

	public void showServerSelectionInventory(Player player) {
		Inventory serverSelectionInventory = Bukkit.createInventory(player, 9, ChatColor.translateAlternateColorCodes('&', "&d&lServer Selection"));

		new BukkitRunnable() {
			@Override
			public void run() {
				for (Server server : servers) {
                    if (server.getServerIcon() == null || (server.getServerSlot() <= -1)) {
                        return;
                    }

					ItemBuilder itemBuilder = new ItemBuilder(server.getServerIcon()).name(server.getDisplayName());
					itemBuilder.clearLore();
					itemBuilder.addLore(HiddenStringUtils.encodeString(server.getName()));

                    for (String line : server.getDisplayLore()) {
                        itemBuilder.addLore(ChatColor.translateAlternateColorCodes('&', line
                                .replace("%players%", server.getPlayerCount() + "")
                                .replace("%online%", server.getPlayerCount() + "")
                                .replace("%totalQueue%", (server.getServerQueue().getEntireQueue().size() > 0 ? server.getServerQueue().getEntireQueue().size() + "" : "&7None"))
                                .replace("%proQueue%", (server.getServerQueue().getProQueue().size() > 0 ? server.getServerQueue().getProQueue().size() + "" : "&7None"))
                                .replace("%vipQueue%", (server.getServerQueue().getVipQueue().size() > 0 ? server.getServerQueue().getVipQueue().size() + "" : "&7None"))
                                .replace("%defaultQueue%", (server.getServerQueue().getDefaultQueue().size() > 0 ? server.getServerQueue().getDefaultQueue().size() + "" : "None"))
                                .replace("%slots%", server.getMaxPlayerCount() + "")
                                .replace("%max%", server.getMaxPlayerCount() + "")
                                .replace("%newline%", " "))
                                .replace("_", " "));
                    }

					itemBuilder.addLore(" ");

                    if (server.isWhitelisted()) {
                        itemBuilder.addLore("&c&lCurrently Whitelisted");
                    } else if (server.getServerQueue().isEnabled()) {
                        if (server.getServerQueue().isPaused()) {
                            itemBuilder.addLore("&c&lThe queue is currently paused.");
                        }

                        if (server.getServerQueue().getEntireQueue().contains(player.getUniqueId())) {
                            itemBuilder.addLore("&7You are currently in position &b#" + (findQueuePosition(player.getUniqueId(), server.getServerQueue()) + 1) + " &7in the queue.");
                        } else {
                            itemBuilder.addLore("&7&nClick&r &7to join the queue.");
                        }
                    } else {
                        itemBuilder.addLore("&7&nClick&r &7to join the server.");
                    }

					serverSelectionInventory.setItem(server.getServerSlot(), itemBuilder.build());
				}
			}
		}.runTaskTimerAsynchronously(main, 0L, 10L);

		player.openInventory(serverSelectionInventory);
	}

	public void giveGadgets(Player player) {
		player.getInventory().clear();

//        ItemBuilder networkInformationBuilder = new ItemBuilder(Material.WRITTEN_BOOK)
//                .title("&d&lNetwork Information")
//                .author("PvPRaids");
//
//        for (String page : main.getConfig().getStringList("network-information-pages"))
//            networkInformationBuilder.addPage(page);

		ItemBuilder serverSelectorBuilder = new ItemBuilder(Material.COMPASS)
				.name("&5&lServer Selector");

		ItemBuilder flightFeatherBuilder = new ItemBuilder(Material.FEATHER)
				.name("&d&lFlight Feather");

//        player.getInventory().setItem(1, networkInformationBuilder.build());
		player.getInventory().setItem(2, serverSelectorBuilder.build());
		player.getInventory().setItem(6, flightFeatherBuilder.build());
		player.getInventory().setHeldItemSlot(2);
	}

	public void createServer(CommandSender sender, String name, String displayName, int maxPlayerCount, Material serverIcon, int serverSlot) {
		if (getServer(name) != null) {
			sender.sendMessage(StringUtils.color("&cThat name is already being used by another server."));
			return;
		}

		Server server = new Server(name);
		server.setDisplayName(displayName);
		server.setMaxPlayerCount(maxPlayerCount);
		server.setServerIcon(serverIcon);
		server.setServerSlot(serverSlot);
		server.setWhitelisted(false);
		servers.add(server);

		save();

		sender.sendMessage(StringUtils.color("&aServer &b" + name + " &ahas been created."));
	}

	public void removeServer(CommandSender sender, Server server) {
		serverCollection.findOneAndDelete(new Document("name", server.getName()));

		servers.remove(server);

		sender.sendMessage(StringUtils.color("&aServer &b" + server.getName() + " &ahas been removed."));
	}

	public void addToServerQueue(Player player, Server server) {
		if (player.hasPermission("hub.queue.bypass")) {
			connectToServer(player, server.getName());
			player.sendMessage(StringUtils.color("&6Connecting you to " + server.getName() + "..."));
			return;
		}

		if (server.getServerQueue().getEntireQueue().contains(player.getUniqueId())) {
			player.sendMessage(StringUtils.color("&cYou are already queued for this server."));
			return;
		}

		String groupName = "Default";

        if (player.hasPermission("hub.pro")) {
            groupName = "Pro";
        } else if (player.hasPermission("hub.vip")) {
            groupName = "Vip";
        }

		switch (groupName) {
			case "Pro":
                if (!server.getServerQueue().getProQueue().contains(player.getUniqueId())) {
                    server.getServerQueue().getProQueue().offer(player.getUniqueId());
                }

				server.getServerQueue().getVipQueue().remove(player.getUniqueId());
				server.getServerQueue().getDefaultQueue().remove(player.getUniqueId());
				break;
			case "Vip":
                if (!server.getServerQueue().getVipQueue().contains(player.getUniqueId())) {
                    server.getServerQueue().getVipQueue().offer(player.getUniqueId());
                }

				server.getServerQueue().getProQueue().remove(player.getUniqueId());
				server.getServerQueue().getDefaultQueue().remove(player.getUniqueId());
				break;
			case "Default":
                if (!server.getServerQueue().getDefaultQueue().contains(player.getUniqueId())) {
                    server.getServerQueue().getDefaultQueue().offer(player.getUniqueId());
                }

				server.getServerQueue().getProQueue().remove(player.getUniqueId());
				server.getServerQueue().getVipQueue().remove(player.getUniqueId());
				break;
		}

		player.sendMessage(StringUtils.color("&3You are currently position &b#" + (findQueuePosition(player.getUniqueId(), server.getServerQueue()) + 1) + " &3in the &b" + server.getName() + " &3queue."));

        if (groupName.equalsIgnoreCase("vip") || groupName.equalsIgnoreCase("pro")) {
            player.sendMessage(StringUtils.color("&bYour " + groupName + " &brank has put you in front of other players in the queue!"));
        }

		player.playNote(player.getLocation(), Instrument.PIANO, Note.sharp(1, Note.Tone.A));
	}

	public void removeFromServerQueue(Player player, Server server) {
		server.getServerQueue().getProQueue().remove(player.getUniqueId());
		server.getServerQueue().getVipQueue().remove(player.getUniqueId());
		server.getServerQueue().getDefaultQueue().remove(player.getUniqueId());
	}

	public int findQueuePosition(UUID uniqueId, ServerQueue serverQueue) {
		int pos = 0;

		final Queue<UUID> totalQueue = serverQueue.getEntireQueue();

        for (UUID uuid : totalQueue) {
            if (uuid != uniqueId) {
                pos++;
            } else {
                return pos;
            }
        }

		return pos;
	}

	public void connectToServer(Player player, String channel) {
		final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		final DataOutputStream out = new DataOutputStream(byteArray);

		try {
			out.writeUTF("Connect");
			out.writeUTF(channel);
		} catch (IOException e) {
			e.printStackTrace();
		}

		player.sendPluginMessage(main, "BungeeCord", byteArray.toByteArray());
	}

	public void getPlayerCount(Player player, String server) {
        if (server == null) {
            server = "ALL";
        }

		final ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("PlayerCount");
		out.writeUTF(server);

		player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
	}

	public boolean isInQueue(UUID uniqueId) {
        for (Server server : servers) {
            if (server.getServerQueue().getEntireQueue().contains(uniqueId)) {
                return true;
            }
        }
		return false;
	}

	public Server getServerFromPlayer(UUID uniqueId) {
        for (Server server : servers) {
            if (server.getServerQueue().getEntireQueue().contains(uniqueId)) {
                return server;
            }
        }
		return null;
	}

	public Server getServer(String name) {
        for (Server server : servers) {
            if (server.getName().equalsIgnoreCase(name.replace("_", " "))) {
                return server;
            }
        }

		return null;
	}

	public Server getServerFromItem(ItemStack item) {
		List<String> lore = item.getItemMeta().getLore();

		for (Server server : getServers())
			if (HiddenStringUtils.extractHiddenString(lore.get(0)) != null && !HiddenStringUtils.extractHiddenString(lore.get(0)).isEmpty())
				if (server.getName().equalsIgnoreCase(HiddenStringUtils.extractHiddenString(lore.get(0))))
					return server;

		return null;
	}
}
