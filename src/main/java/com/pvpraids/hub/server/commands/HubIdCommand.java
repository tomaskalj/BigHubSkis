package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubIdCommand extends BaseCommand {
	@Command(name = "hub.id", permission = "hub.command.id")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 2) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub id [server] [id]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		if (SERVER_MANAGER.getServer(args[1]) != null) {
			sender.sendMessage(StringUtils.color("&cThere is already an existing server using that ID."));
			return;
		}

		server.setName(args[1]);

		sender.sendMessage(StringUtils.color("&aServer &b" + args[0] + " &aID has been set to &e" + server.getName() + "&a."));
	}
}
