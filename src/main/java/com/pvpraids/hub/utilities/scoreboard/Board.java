package com.pvpraids.hub.utilities.scoreboard;

import com.pvpraids.hub.utilities.HubBoardAdapater;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

@Getter
@Setter
public class Board {

	private static Set<Board> boards;

	static {
		Board.boards = new HashSet<>();
	}

	private final HubBoardAdapater hubBoardAdapater;
	private String title;
	private Scoreboard scoreboard;
	private UUID uuid;
	private Objective objective;
	private Set<String> keys;
	private List<BoardEntry> entries;
	private Set<BoardCooldown> cooldowns;

	public Board(final Player player, final String title, final HubBoardAdapater hubBoardAdapater) {
		this.uuid = player.getUniqueId();
		this.title = title;
		this.hubBoardAdapater = hubBoardAdapater;
		this.keys = new HashSet<>();
		this.cooldowns = new HashSet<>();
		this.entries = new ArrayList<>();
		this.setup(player);
	}

	public static Board getByPlayer(final UUID uuid) {
        for (final Board board : Board.boards) {
            if (board.getUuid().equals(uuid)) {
                return board;
            }
        }
		return null;
	}

	private void setup(Player player) {
        if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            this.scoreboard = player.getScoreboard();
        } else {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
		(this.objective = this.scoreboard.registerNewObjective("alex_is_shit", "dummy")).setDisplaySlot(DisplaySlot.SIDEBAR);
        if (this.hubBoardAdapater != null) {
            this.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.title));
        }
//            this.objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', this.carbyne.getTitle(player)));
        else {
            this.objective.setDisplayName("Default Title");
        }
		Board.boards.add(this);
	}

	public String getNewKey(final BoardEntry entry) {
		for (final ChatColor color : ChatColor.values()) {
			String colorText = color + "" + ChatColor.WHITE;
			if (entry.getText().length() > 16) {
				final String sub = entry.getText().substring(0, 16);
				colorText += ChatColor.getLastColors(sub);
			}
			if (!this.keys.contains(colorText)) {
				this.keys.add(colorText);
				return colorText;
			}
		}
		throw new IndexOutOfBoundsException("No more keys available!");
	}

	public List<String> getBoardEntriesFormatted() {
		final List<String> toReturn = new ArrayList<>();
        for (final BoardEntry entry : new ArrayList<>(this.entries)) {
            toReturn.add(entry.getText());
        }
		return toReturn;
	}

	public BoardEntry getByPosition(final int position) {
		int i = 0;
		for (final BoardEntry board : this.entries) {
            if (i == position) {
                return board;
            }
			++i;
		}
		return null;
	}

	public BoardCooldown getCooldown(final String id) {
        for (final BoardCooldown cooldown : this.getCooldowns()) {
            if (cooldown.getId().equals(id)) {
                return cooldown;
            }
        }
		return null;
	}

	public Set<BoardCooldown> getCooldowns() {
		this.cooldowns.removeIf(cooldown -> System.currentTimeMillis() >= cooldown.getEnd());
		return this.cooldowns;
	}

	public static Set<Board> getBoards() {
		return boards;
	}
}
