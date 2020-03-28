package com.pvpraids.hub.server;

import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.utilities.Cooldowns;
import com.pvpraids.hub.utilities.StringUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class ServerQueue {
	private final HubPlugin main = HubPlugin.getInstance();

	private Server server;
	private LinkedList<UUID> proQueue = new LinkedList<>();
	private LinkedList<UUID> vipQueue = new LinkedList<>();
	private LinkedList<UUID> defaultQueue = new LinkedList<>();
	private boolean enabled = true;
	private boolean paused = false;

	public ServerQueue(Server server) {
		this.server = server;

		new BukkitRunnable() {
			@Override
			public void run() {
                if (isPaused()) {
                    getEntireQueue().stream().filter(id -> Cooldowns.tryCooldown(id, "paused-cooldown", 15000)).forEach(id -> {
                        StringUtils.sendMessage(id, "&4&m-----------------------------------------------------");
                        StringUtils.sendMessage(id, "&r");
                        StringUtils.sendMessage(id, "&r &r&cThe queue is currently paused.");
                        StringUtils.sendMessage(id, "&r &r&cQueuing will resume shortly!");
                        StringUtils.sendMessage(id, "&r");
                        StringUtils.sendMessage(id, "&4&m-----------------------------------------------------");
                    });
                } else if (enabled) {
                    ArrayList<UUID> joinList = new ArrayList<>();
                    LinkedList<UUID> queue = getEntireQueue();

                    Bukkit.broadcastMessage("EntireQueue: " + queue.toString());

                    if (queue.size() > 0) {
                        for (int count = 0; count < 3; count++) {
                            if (peek() != null) {
                                if (defaultQueue.contains(peek()) && server.getPlayerCount() >= server.getMaxPlayerCount()) {
                                    continue;
                                }

                                System.out.println("Polling: " + peek() + ", Count: " + count);

                                joinList.add(poll());
                            }
                        }

                        if (joinList.size() > 0) {
                            System.out.println(joinList);

                            for (UUID id : joinList) {
                                if (Bukkit.getPlayer(id) == null) {
                                    return;
                                }

                                main.getServerManager().removeFromServerQueue(Bukkit.getPlayer(id), server);
                                main.getServerManager().getPlayerCount(Bukkit.getPlayer(id), server.getName());
                                main.getServerManager().connectToServer(Bukkit.getPlayer(id), server.getName());
                                StringUtils.sendMessage(id, "&6Connecting you to " + server.getName() + "...");
                            }
                        }
                    }
                }
			}
		}.runTaskTimerAsynchronously(HubPlugin.getInstance(), 0L, 2 * 20L);
	}

	public LinkedList<UUID> getEntireQueue() {
		LinkedList<UUID> queue = new LinkedList<>();
		queue.addAll(proQueue);
		queue.addAll(vipQueue);
		queue.addAll(defaultQueue);

		return queue;
	}

	public UUID poll() {
        if (proQueue.size() > 0) {
            return proQueue.poll();
        } else if (vipQueue.size() > 0) {
            return vipQueue.poll();
        } else {
            return defaultQueue.poll();
        }
	}

	public UUID peek() {
        if (proQueue.size() > 0) {
            return proQueue.peek();
        } else if (vipQueue.size() > 0)
            return vipQueue.peek();
        else
            return defaultQueue.peek();
	}
}
