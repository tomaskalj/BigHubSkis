package com.pvpraids.hub.utilities.scoreboard;

import com.pvpraids.hub.utilities.HubBoardAdapater;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class HubScoreboard implements Listener {

	private HubBoardAdapater adapter;
	private JavaPlugin plugin;

	public HubScoreboard(final JavaPlugin plugin, final HubBoardAdapater hubBoardAdapater) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.setAdapter(hubBoardAdapater);
		this.run();
	}

	private void run() {
		new BukkitRunnable() {
			public void run() {
                if (HubScoreboard.this.adapter == null) {
                    return;
                }

				for (Player player : Bukkit.getOnlinePlayers()) {
					final Board board = Board.getByPlayer(player.getUniqueId());
					if (board != null) {
						final List<String> scores = HubScoreboard.this.adapter.getScoreboard(player, board.getCooldowns());
						final List<String> translatedScores = new ArrayList<>();

						if (scores == null) {
                            if (board.getEntries().isEmpty()) {
                                continue;
                            }

                            for (final BoardEntry boardEntry : board.getEntries()) {
                                boardEntry.remove();
                            }

							board.getEntries().clear();
						} else {
                            for (final String line : scores) {
                                translatedScores.add(ChatColor.translateAlternateColorCodes('&', line));
                            }

							Collections.reverse(scores);
							final Scoreboard scoreboard = board.getScoreboard();
							final Objective objective = board.getObjective();

//                            if (!objective.getDisplayName().equals(HubScoreboard.this.adapter.getTitle(player)))
                            if (!objective.getDisplayName().equals(board.getTitle())) {
                                objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', board.getTitle()));
                            }

							int i = 0;
							Label_0280:
							while (i < scores.size()) {
								final String text = scores.get(i);
								int position = i + 1;
								while (true) {
									for (final BoardEntry boardEntry2 : board.getEntries()) {
										final Score score = objective.getScore(boardEntry2.getKey());
										if (score != null && boardEntry2.getText().equals(ChatColor.translateAlternateColorCodes('&', text)) && score.getScore() == position) {
											++i;
											continue Label_0280;
										}
									}
									final int positionToSearch = position - 1;
									final BoardEntry entry = board.getByPosition(positionToSearch);
                                    if (entry == null) {
                                        new BoardEntry(board, text).send(position);
                                    } else {
                                        entry.setText(text).setup().send(position);
                                    }
									if (board.getEntries().size() > scores.size()) {
										final Iterator<BoardEntry> iterator = board.getEntries().iterator();
										while (iterator.hasNext()) {
											final BoardEntry boardEntry3 = iterator.next();
											if (!translatedScores.contains(boardEntry3.getText()) || Collections.frequency(board.getBoardEntriesFormatted(), boardEntry3.getText()) > 1) {
												iterator.remove();
												boardEntry3.remove();
											}
										}
									}
								}
							}

							player.setScoreboard(scoreboard);
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(this.plugin, 20L, 2L);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinEvent(final PlayerJoinEvent event) {
        if (Board.getByPlayer(event.getPlayer().getUniqueId()) == null) {
            new Board(event.getPlayer(), "&6PvPRaids", adapter);
        }
	}

	public JavaPlugin getPlugin() {
		return this.plugin;
	}

	public HubBoardAdapater getAdapter() {
		return this.adapter;
	}

	public void setAdapter(final HubBoardAdapater adapter) {
		this.adapter = adapter;
		for (final Player player : Bukkit.getOnlinePlayers()) {
			final Board board = Board.getByPlayer(player.getUniqueId());

			if (board != null)
				Board.getBoards().remove(board);

			new Board(player, "&6PvPRaids", adapter);
		}
	}
}
