package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class HubIconCommand extends BaseCommand {
	@Command(name = "hub.icon", permission = "hub.command.hub")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 2) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub icon [server] [material]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		if (Material.getMaterial(args[1]) == null) {
			sender.sendMessage(StringUtils.color("&cThat is not a valid material."));
			return;
		}

		server.setServerIcon(Material.getMaterial(args[1]));

		sender.sendMessage(StringUtils.color("&aServer &b" + server.getName() + " &aserver icon has been set to &e" + server.getServerIcon() + "&a."));
	}
}
