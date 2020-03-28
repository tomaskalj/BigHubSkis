package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.Bukkit;

public class TestMessageCommand extends BaseCommand {
	@Command(name = "testmessage", permission = "hub.command.testmessage", aliases = {"tm"})
	public void onCommand(CommandArgs command) {
		String[] args = command.getArgs();

		Bukkit.broadcastMessage(StringUtils.color(org.apache.commons.lang.StringUtils.join(args, " ", 0, args.length)));
	}
}