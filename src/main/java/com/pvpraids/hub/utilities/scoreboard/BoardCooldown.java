package com.pvpraids.hub.utilities.scoreboard;

import java.text.DecimalFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.time.DurationFormatUtils;

@Getter
@Setter
public class BoardCooldown {
	private static final DecimalFormat SECONDS_FORMATTER;

	static {
		SECONDS_FORMATTER = new DecimalFormat("#0.0");
	}

	private final Board board;
	private final String id;
	private final double startDuration;
	private final double duration;
	private final long end;

	public BoardCooldown(final Board board, final String id, final double startDuration) {
		this.board = board;
		this.id = id;
		this.startDuration = startDuration;
		this.duration = startDuration;
		this.end = (long) (System.currentTimeMillis() + duration * 1000.0);
		board.getCooldowns().add(this);
	}

	public String getFormattedString(final BoardFormat format) {
        if (format == null) {
            throw new NullPointerException();
        }
        if (format == BoardFormat.SECONDS) {
            return BoardCooldown.SECONDS_FORMATTER.format((this.end - System.currentTimeMillis()) / 1000.0f);
        }
		return DurationFormatUtils.formatDuration(this.end - System.currentTimeMillis(), "mm:ss");
	}

	public void cancel() {
		this.board.getCooldowns().remove(this);
	}
}
