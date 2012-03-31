package org.ilaborie.pineneedles.web.util;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The Class TimeUtil.
 */
public final class TimeUtil {

	/** The Constant DIGITS. */
	private static final int DIGITS = 4;

	/**
	 * Instantiates a new time util.
	 */
	private TimeUtil() {
		super();
	}

	/**
	 * Format time.
	 *
	 * @param timeInMillisecond the time in millisecond
	 * @return the string
	 */
	public static String formatTime(long timeInMillisecond,TimeUnit unitSource) {
		TimeUnit unit = chooseUnit(timeInMillisecond, unitSource);
		double value = (double) timeInMillisecond / unitSource.convert(1, unit);

		// Too bad this functionality is not exposed as a regular method call
		return String.format("%." + DIGITS + "g %s",
		        value, abbreviate(unit));
	}

	/**
	 * Choose unit.
	 *
	 * @param time the time
	 * @param tu the source TimeUnit
	 * @return the time unit
	 */
	private static TimeUnit chooseUnit(long time, TimeUnit tu) {
		List<TimeUnit> timeUnits = Arrays.asList(DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS, MICROSECONDS, NANOSECONDS);
		for (TimeUnit test : timeUnits) {
			if (test.convert(time, tu) > 0) {
				return test;
			}
		}
		return NANOSECONDS;
	}

	/**
	 * Abbreviate.
	 *
	 * @param unit the unit
	 * @return the string
	 */
	private static String abbreviate(TimeUnit unit) {
		switch (unit) {
			case NANOSECONDS:
				return "ns";
			case MICROSECONDS:
				return "\u03bcs"; // μs
			case MILLISECONDS:
				return "ms";
			case SECONDS:
				return "s";
			case MINUTES:
				return "min";
			case HOURS:
				return "h";
			case DAYS:
				return "d";
			default:
				throw new AssertionError();
		}
	}

}
