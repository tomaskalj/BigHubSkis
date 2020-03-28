package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubAddLoreCommand extends BaseCommand {
	@Command(name = "hub.addlore", permission = "hub.command.addlore")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length < 2 || args.length > 3) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub addLore [server] [text] (index)"));
			return;
		}

		Server server = SERVER_MANAGER.getServer(args[0]);

		if (server == null) {
			sender.sendMessage(StringUtils.color("&cCould not find that server."));
			return;
		}

		if (args.length == 2) {
			server.getDisplayLore().add(args[1]);

			sender.sendMessage(StringUtils.color("&aThe line &4" + args[1] + " &ahas been added to the lore of &b" + server.getName() + "&a."));
		} else {
			try {
				int index = Integer.parseInt(args[1]);

				if (index < 0) {
					sender.sendMessage(StringUtils.color("&cYou need to enter a positive value."));
					return;
				}

				if ((server.getDisplayLore().size() > 0 || server.getDisplayLore().isEmpty()) || index > server.getDisplayLore().size()) {
					sender.sendMessage(StringUtils.color("&cYou cannot replace a none existent lore."));
					return;
				}

				server.getDisplayLore().set(index, args[2]);

				sender.sendMessage(StringUtils.color("&aThe line &4" + args[2] + " &ahas been added to the lore of &b" + server.getName() + "&a."));
			} catch (NumberFormatException ignored) {
				sender.sendMessage(StringUtils.color("&cYou need to enter a positive value as the index."));
			}
		}
	}
}
