package com.pvpraids.hub.server;

import com.pvpraids.hub.HubPlugin;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@Setter
public class Server {

	private String name, displayName;
	private List<String> displayLore;
	private int playerCount = 0, maxPlayerCount, serverSlot;
	private boolean isWhitelisted = false;
	private Material serverIcon;
	private ServerQueue serverQueue;

	public Server(String name) {
		this.name = name;
		this.displayLore = new ArrayList<>();
		this.serverQueue = new ServerQueue(this);

		new BukkitRunnable() {
			@Override
			public void run() {
				Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[]{});

                if (players.length > 0) {
                    HubPlugin.getInstance().getServerManager().getPlayerCount(players[0], name);
                }
			}
		}.runTaskTimerAsynchronously(HubPlugin.getInstance(), 0L, 10L);
	}
}
