package org.ilaborie.pineneedles.web.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The Class DateUtil.
 */
public final class DateUtil {
	
	/** The Constant MIDDAY. */
	private static final TimeZone MIDDAY = TimeZone.getTimeZone("GMT-12:00");
	
	/** The Constant UTC. */
	private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

	/** The Constant formatters. */
	private static final ThreadLocal<List<DateFormat>> formatters = new ThreadLocal<List<DateFormat>>();

	/**
	 * Instantiates a new date util.
	 */
	private DateUtil() {
		super();
	}

	/**
	 * Parses the.
	 *
	 * @param string the string
	 * @return the date
	 */
	public static Date parse(String string) {
		Date result = null;
		if (string != null) {
			List<DateFormat> formatters = getDateFormatter();
			for (DateFormat formatter : formatters) {
				try {
					result = formatter.parse(string);
					break;
				} catch (ParseException e) {
					// Skip this error
				}
			}
			if (result == null) {
				throw new RuntimeException("Could not find a valid formatter for date: " + string);
			}
		}
		return result;
	}

	/**
	 * Gets the date formatter.
	 *
	 * @return the date formatter
	 */
	private static List<DateFormat> getDateFormatter() {
		List<DateFormat> result = formatters.get();
		if (result == null) {
			result = new ArrayList<DateFormat>();
			formatters.set(result);

			// yyyy-mm-ddThh...
			result.add(createDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", UTC)); // UTC/Zulu
			result.add(createDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", null)); // With timezone
			result.add(createDateFormat("yyyy-MM-dd'T'HH:mm:ss", null)); // Without timezone

			// yyyy-mm-dd hh...
			result.add(createDateFormat("yyyy-MM-dd' 'HH:mm:ss'Z'", UTC)); // UTC/Zulu
			result.add(createDateFormat("yyyy-MM-dd' 'HH:mm:ssZ", null)); // With timezone
			result.add(createDateFormat("yyyy-MM-dd' 'HH:mm:ss", null)); // Without timezone

			// Date without time, set to Midday UTC
			result.add(createDateFormat("yyyy-MM-dd", MIDDAY)); // Normal date format
			result.add(createDateFormat("yyyy:MM:dd", MIDDAY)); // Image (IPTC/EXIF) format
		}
		return formatters.get();
	}

	/**
	 * Creates the date format.
	 *
	 * @param format the format
	 * @param timezone the timezone
	 * @return the date format
	 */
	private static DateFormat createDateFormat(String format, TimeZone timezone) {
		SimpleDateFormat sdf =
		        new SimpleDateFormat(format, new DateFormatSymbols(Locale.US));
		if (timezone != null) {
			sdf.setTimeZone(timezone);
		}
		return sdf;
	}
}
