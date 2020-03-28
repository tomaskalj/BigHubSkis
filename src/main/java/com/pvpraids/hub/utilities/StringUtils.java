package com.pvpraids.hub.utilities;

import com.pvpraids.hub.HubPlugin;
import java.util.UUID;
import org.bukkit.ChatColor;

public class StringUtils {
	public static String color(String input) {
		return ChatColor.translateAlternateColorCodes('&', input);
	}

	public static void sendMessage(UUID uniqueId, String message) {
		if (HubPlugin.getInstance().getServer().getPlayer(uniqueId) != null) {
			HubPlugin.getInstance().getServer().getPlayer(uniqueId).sendMessage(StringUtils.color(message));
		}
	}
}
