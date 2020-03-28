package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubMaxSlotsCommand extends BaseCommand {
	@Command(name = "hub.maxslots", permission = "hub.command.maxslots")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 2) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub maxSlots [server] [slots]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		try {
			int maxSlots = Integer.parseInt(args[1]);

			server.setMaxPlayerCount(maxSlots);
			sender.sendMessage(StringUtils.color("&aServer &b" + server.getName() + " &amax player count has been set to &e" + server.getMaxPlayerCount() + "&a."));
		} catch (NumberFormatException e) {
			sender.sendMessage(StringUtils.color("&cYou must enter a valid number."));
		}
	}
}
