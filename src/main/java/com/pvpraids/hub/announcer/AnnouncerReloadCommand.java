package com.pvpraids.hub.announcer;

import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;

public class AnnouncerReloadCommand extends BaseCommand {

	@Command(name = "announcer.reload", permission = "hub.administrator")
	public void onCommand(CommandArgs commandArgs) {
		HubPlugin.getInstance().getAnnouncerManager().load();
		commandArgs.getSender().sendMessage(StringUtils.color("&aAnnouncements have been reloaded."));
	}
}
