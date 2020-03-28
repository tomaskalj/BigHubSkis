package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class HubSetSpawnCommand extends BaseCommand {
	@Command(name = "hub.setspawn", permission = "hub.command.setspawn", inGameOnly = true)
	public void onCommand(CommandArgs command) {
		Player player = command.getPlayer();

		SERVER_MANAGER.setSpawnLocation(player.getLocation());

		player.sendMessage(StringUtils.color("&aSpawn location set at &BX: &e" + player.getLocation().getBlockX() + " &bY: &e" + player.getLocation().getBlockY() + " &bZ: &e" + player.getLocation().getBlockZ()));
	}
}
