package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubListCommand extends BaseCommand {
	@Command(name = "bighud", permission = "hub.command.bighud.")
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();

	}
}
