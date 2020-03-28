package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubWipeCommand extends BaseCommand {
	@Command(name = "hub.wipe", permission = "hub.command.wipe")
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();

		SERVER_MANAGER.getServers().clear();
		HubPlugin.getInstance().getMongoDatabase().getCollection("servers").drop();

		sender.sendMessage(StringUtils.color("&cThe database has been purged."));
	}
}
