package com.pvpraids.hub.utilities.command;

import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.server.ServerManager;
import lombok.Getter;

@Getter
public class BaseCommand {

	public static final ServerManager SERVER_MANAGER = HubPlugin.getInstance().getServerManager();

	public BaseCommand() {
		HubPlugin.getInstance().getCommandFramework().registerCommands(this);
	}
}
