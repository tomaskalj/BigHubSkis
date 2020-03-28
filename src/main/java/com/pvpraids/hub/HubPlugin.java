package com.pvpraids.hub;

import com.pvpraids.hub.announcer.AnnouncerManager;
import com.pvpraids.hub.announcer.AnnouncerReloadCommand;
import com.pvpraids.hub.listeners.GadgetListeners;
import com.pvpraids.hub.listeners.PlayerListeners;
import com.pvpraids.hub.listeners.ServerListeners;
import com.pvpraids.hub.server.Server;
import com.pvpraids.hub.server.ServerManager;
import com.pvpraids.hub.server.commands.HubAddLoreCommand;
import com.pvpraids.hub.server.commands.HubCommand;
import com.pvpraids.hub.server.commands.HubCreateCommand;
import com.pvpraids.hub.server.commands.HubDelLoreCommand;
import com.pvpraids.hub.server.commands.HubEnableCommand;
import com.pvpraids.hub.server.commands.HubIconCommand;
import com.pvpraids.hub.server.commands.HubIdCommand;
import com.pvpraids.hub.server.commands.HubListCommand;
import com.pvpraids.hub.server.commands.HubMaxSlotsCommand;
import com.pvpraids.hub.server.commands.HubNameCommand;
import com.pvpraids.hub.server.commands.HubPauseCommand;
import com.pvpraids.hub.server.commands.HubRemoveCommand;
import com.pvpraids.hub.server.commands.HubSetSpawnCommand;
import com.pvpraids.hub.server.commands.HubSlotCommand;
import com.pvpraids.hub.server.commands.HubWhitelistCommand;
import com.pvpraids.hub.server.commands.HubWipeCommand;
import com.pvpraids.hub.server.commands.TestMessageCommand;
import com.pvpraids.hub.utilities.HubBoardAdapater;
import com.pvpraids.hub.utilities.command.CommandFramework;
import com.pvpraids.hub.utilities.scoreboard.HubScoreboard;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

@Getter
public class HubPlugin extends JavaPlugin implements PluginMessageListener {

	@Getter
	private static HubPlugin instance;
	private AnnouncerManager announcerManager;
	private ServerManager serverManager;
	private CommandFramework commandFramework;
	private HubBoardAdapater hubBoardAdapater;
	private HubScoreboard hubScoreboard;

	@Override
	public void onEnable() {
		instance = this;

		getConfig().options().copyDefaults(true);
		saveConfig();

		announcerManager = new AnnouncerManager();
		serverManager = new ServerManager();
		commandFramework = new CommandFramework(this);
		hubBoardAdapater = new HubBoardAdapater();
		hubScoreboard = new HubScoreboard(this, hubBoardAdapater);

		registerCommands();

		registerListeners();
	}

	@Override
	public void onDisable() {
		serverManager.save();
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}

		DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(message));

		try {
			String subChannel = dataInput.readUTF();

			if (subChannel.equals("PlayerCount")) {
				String serverId = dataInput.readUTF();

				for (Server server : serverManager.getServers()) {
					if (server.getName().equalsIgnoreCase(serverId)) {
						server.setPlayerCount(dataInput.readInt());
					}
				}
			}
		} catch (IOException ignored) {
		}
	}

	public void registerCommands() {
		new HubCommand();
		new HubAddLoreCommand();
		new HubCreateCommand();
		new HubDelLoreCommand();
		new HubEnableCommand();
		new HubIconCommand();
		new HubIdCommand();
		new HubListCommand();
		new HubMaxSlotsCommand();
		new HubNameCommand();
		new HubPauseCommand();
		new HubRemoveCommand();
		new HubSetSpawnCommand();
		new HubSlotCommand();
		new HubWhitelistCommand();
		new HubWipeCommand();
		new AnnouncerReloadCommand();
		new TestMessageCommand();
	}

	public void registerListeners() {
		getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
		getServer().getPluginManager().registerEvents(new ServerListeners(), this);
		getServer().getPluginManager().registerEvents(new GadgetListeners(), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
	}
}
