package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubDelLoreCommand extends BaseCommand {
	@Command(name = "hub.dellore", permission = "hub.command.dellore")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 2) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub delLore [server] [index]"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		try {
			int index = Integer.parseInt(args[1]);

			if (index < 0) {
				sender.sendMessage(StringUtils.color("&cYou need to enter a positive value."));
				return;
			}

			if (index >= server.getDisplayLore().size()) {
				sender.sendMessage(StringUtils.color("&cThere is no lore for the index provided."));
				return;
			}

			server.getDisplayLore().remove(index);

			sender.sendMessage(StringUtils.color("&aLine &4" + args[1] + " &ahas been removed from the lore of &b" + server.getName() + "&a."));
		} catch (NumberFormatException ignored) {
			sender.sendMessage(StringUtils.color("&cYou must enter a valid integer ID."));
		}
	}
}
