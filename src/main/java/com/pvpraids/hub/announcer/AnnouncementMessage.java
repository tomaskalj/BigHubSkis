package com.pvpraids.hub.announcer;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;

@Getter
@Setter
public class AnnouncementMessage {

	private final List<String> message = new ArrayList<>();

	public AnnouncementMessage(List<String> messagel) {
        for (String s : message) {
            this.message.add(ChatColor.translateAlternateColorCodes('&', StringEscapeUtils.unescapeJava(s)));
        }
	}
}
