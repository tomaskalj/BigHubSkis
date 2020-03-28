package com.pvpraids.hub.server.commands;

import com.pvpraids.hub.utilities.StringUtils;
import com.pvpraids.hub.utilities.command.BaseCommand;
import com.pvpraids.hub.utilities.command.Command;
import com.pvpraids.hub.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;

public class HubCommand extends BaseCommand {
	private static final String[] HUB_HELP = {
			"&7&m----------&r&7[ &bHub Command&7]&m----------",
			"&r &r&5/hub &7- Displays this page.",
			"&r &r&5/hub list &7- Lists servers available.",
			"&r &r&5/hub setspawn &7- Sets the server spawn.",
			"&r &r&5/hub create &7(&bname&7) (&bdisplayName&7) (&bslots&7) (&bmaterial&7) (&bslot&7) - Creates a server.",
			"&r &r&5/hub remove &7(&bserver&7) - Removes a server.",
			"&r &r&5/hub whitelist &7(&bserver&7) - Toggle the whitelist for this server.",
			"&r &r&5/hub pause &7(&bserver&7) - Pause the queue for this server.",
			"&r &r&5/hub enable &7(&bserver&7) - Toggle the queue for this server.",
			"&r &r&5/hub id &7(&bserver&7) (&bname&7) - Sets a server's server ID.",
			"&r &r&5/hub name &7(&bserver&7) (&bname&7) - Set a servers GUI Display Name.",
			"&r &r&5/hub addlore &7(&bserver&7) (&btext&7) - Add a line to the lore.",
			"&r &r&5/hub dellore &7(&bserver&7) [&bline&7] - Delete a line from the lore.",
			"&r &r&5/hub icon &7(&bserver&7) (&bmaterial&7) - Set a servers GUI icon.",
			"&r &r&5/hub slot &7(&bserver&7) (&bslot&7) - Set a servers GUI slot.",
			"&r &r&5/hub maxslots &7(&bserver&7) (&bcount&7) - Set a servers max slots.",
			"&r &r&5/hub wipe &7- Wipes the collections used by dHub in the MongoDB."
	};

	@Command(name = "hub", permission = "hub.command.hub")
	public void onCommand(CommandArgs command) {
		CommandSender sender = command.getSender();

		for (String line : HUB_HELP) {
			sender.sendMessage(StringUtils.color(line));
		}
	}
}
