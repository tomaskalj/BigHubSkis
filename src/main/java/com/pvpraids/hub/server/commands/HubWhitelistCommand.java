package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubWhitelistCommand extends BaseCommand {
	@Command(name = "hub.whitelist", permission = "hub.command.whitelist")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 1) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub whitelist [server]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		server.setWhitelisted(!server.isWhitelisted());

		sender.sendMessage(StringUtils.color("&aYou have turned the whitelist &b" + (server.isWhitelisted() ? "on" : "off") + " &afor &5" + server.getName() + "&a."));
	}
}
