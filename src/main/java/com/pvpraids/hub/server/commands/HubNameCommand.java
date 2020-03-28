package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubNameCommand extends BaseCommand {
	@Command(name = "hub.name", permission = "hub.command.name")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 2) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub name [server] [displayName]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		server.setDisplayName(args[1]);

		sender.sendMessage(StringUtils.color("&aServer &b" + server.getName() + " &adisplay name has been set to &e" + server.getDisplayName() + "&a."));
	}
}
