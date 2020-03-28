package com.pvpraids.hub.utilities;

import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.server.ServerManager;
import com.pvpraids.hub.utilities.scoreboard.BoardCooldown;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Calvin on 3/13/2017
 * <p>
 * for the Carbyne project.
 */
public class HubBoardAdapater {
	private HubPlugin main = HubPlugin.getInstance();
	private ServerManager serverManager = main.getServerManager();

	public List<String> getScoreboard(final Player player, final Set<BoardCooldown> set) {
		ArrayList<String> lines = new ArrayList<>();

		int count = 0;
        for (Server server : serverManager.getServers()) {
            if (server.getPlayerCount() > 0) {
                count = count + server.getPlayerCount();
            }
        }

		lines.add("&eOnline&7: &f" + (Bukkit.getOnlinePlayers().size() + count));

		if (serverManager.isInQueue(player.getUniqueId())) {
			Server server = serverManager.getServerFromPlayer(player.getUniqueId());

            if (server != null) {
                lines.add("&eQueue Position&7: &f#" + (serverManager.findQueuePosition(player.getUniqueId(), server.getServerQueue()) + 1) + "/" + server.getServerQueue().getEntireQueue().size());
            }
		}

		lines.add(0, "&7&m-------------------");
		lines.add("&7&m-------------------");
		lines.add("       &6PvPRaids.com");

		return lines;
	}
}
