package com.pvpraids.hub.announcer;

import com.pvpraids.hub.HubPlugin;
import com.pvpraids.hub.utilities.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class AnnouncerManager {

	private HubPlugin main = HubPlugin.getInstance();
	private int interval = 600, taskID;
	private final List<AnnouncementMessage> messages = new ArrayList<>();

	public AnnouncerManager() {
		load();
	}

	public void load() {
		Bukkit.getScheduler().cancelTask(taskID);

        if (main.getConfig().contains("announcer-interval")) {
            this.interval = main.getConfig().getInt("announcer-interval");
        }

		messages.clear();

		ConfigurationSection section = main.getConfig().getConfigurationSection("announcer-messages");
		if (section == null) {
			main.getConfig().createSection("announcer-messages");
			main.saveConfig();
			return;
		}

		for (String key : section.getKeys(false)) {
			List<String> message = new ArrayList<>();

            if (section.contains(key + ".message")) {
                message = section.getStringList(key + ".message");
            }

			AnnouncementMessage newMessage = new AnnouncementMessage(message);
			this.messages.add(newMessage);
		}

		run(0);
	}

	public void run(int index) {
		if (!messages.isEmpty()) {
			AnnouncementMessage announcementMessage = messages.get(index);
            if (announcementMessage != null) {
                if (announcementMessage.getMessage() != null && !announcementMessage.getMessage().isEmpty()) {
                    for (String message : announcementMessage.getMessage()) {
                        for (Player all : Bukkit.getOnlinePlayers()) {
                            all.sendMessage(StringUtils.color(message));
                        }
                    }
                }
            }
		}

		index++;
        if (index >= messages.size()) {
            index = 0;
        }

		final int i = index;

		this.taskID = Bukkit.getScheduler().scheduleAsyncDelayedTask(main, () -> AnnouncerManager.this.run(i), interval * 20);
	}
}
