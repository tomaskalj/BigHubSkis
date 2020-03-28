package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class HubCreateCommand extends BaseCommand {
	@Command(name = "hub.create", permission = "hub.command.create")
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();
		CommandSender sender = command.getSender();

		if (args.length != 5) {
			sender.sendMessage(StringUtils.color("&cUsage: /hub create [name] [displayName] [slots] [icon] [guiSlot]"));
			return;
		}

		try {
			if (Material.getMaterial(args[3]) == null) {
				sender.sendMessage(StringUtils.color("&cYou must enter a valid material."));
				return;
			}

			SERVER_MANAGER.createServer(sender, args[0].replace("_", " "), args[1].replace("_", " "), Integer.parseInt(args[2]), Material.getMaterial(args[3].toUpperCase()), Integer.parseInt(args[4]));
		} catch (NumberFormatException e) {
			sender.sendMessage(StringUtils.color("&cYou must enter a valid number."));
		}
	}
}
