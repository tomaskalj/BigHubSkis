package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubPauseCommand extends BaseCommand {
	@Command(name = "hub.pause", permission = "hub.command.pause")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 1) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub pause [server]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		server.getServerQueue().setPaused(!server.getServerQueue().isPaused());

		sender.sendMessage(StringUtils.color("&aYou have " + (server.getServerQueue().isPaused() ? "paused" : "unpaused") + " the queue for &5" + server.getName() + "&a."));
	}
}
