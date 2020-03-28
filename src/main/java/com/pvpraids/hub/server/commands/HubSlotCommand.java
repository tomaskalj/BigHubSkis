package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubSlotCommand extends BaseCommand {
	@Command(name = "hub.slot", permission = "hub.command.slot")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 2) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub slot [server] [slot]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		try {
			server.setServerSlot(Integer.parseInt(args[1]));

			sender.sendMessage(StringUtils.color("&aServer &b" + server.getName() + " &aserver slot has been set to &e" + server.getServerSlot() + "&a."));
		} catch (NumberFormatException e) {
			sender.sendMessage(StringUtils.color("&cYou must enter a valid number."));
		}
	}
}
