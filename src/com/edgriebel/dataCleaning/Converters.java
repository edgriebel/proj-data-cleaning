package com.edgriebel.dataCleaning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Converters {
	/**
	 * Convert a {@link Date} from Pacific to Eastern timezone by adding 3 hours
	 * @param d Date in Pacific timezone (with no TZ specified)
	 * @return Date in Eastern Timezone
	 */
	protected Date timeToEST_EDT(Date d) {
		if (d == null) {
			return null;
		}
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("US/Eastern"));
		c.setTime(d);
		c.add(Calendar.HOUR, 3);
		return c.getTime();
	}

	/**
	 * Convert a string from "MM/dd/yy hh:mm:ss aa" to ISO-8601
	 * @param ts Timestamp in MM/dd/yy hh:mm:ss aa
	 * @return date in ISO-8601 format (yyyy-MM-ddTHH:mm:ss-9999)
	 */
	protected String convertTimestamp(String ts) {
		if (ts == null) {
			return null;
		}
		SimpleDateFormat sdfFrom = new SimpleDateFormat("MM/dd/yy hh:mm:ss aa");
		SimpleDateFormat sdfIso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		try {
			Date d = sdfFrom.parse(ts);
			d = timeToEST_EDT(d);
			return sdfIso8601.format(d);
		} catch (ParseException e) {
			System.err.printf("Exception when parsing timestamp %s: %s",
					ts, e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Convert a string in format HH:MM:SS.MS to a string in SSSSS.MS format
	 * Note that all fields MUST be given 
	 * @param interval
	 * @return String formatted in fractional seconds
	 */
	protected String convertInterval(String interval) {
		if (interval == null) {
			return null;
		}
		if (interval.isEmpty()) {
			return "0";
		}
		String [] values = interval.split("[:.]");
		if (values.length < 4) {
			System.err.printf("Incorrect format for '%s', should be '%s'\n",
					interval, "HH:MM:SS.MSEC");
			return "0";
		}
		float seconds = Integer.valueOf(values[0]);
		seconds = seconds * 60 + Integer.valueOf(values[1]);
		seconds = seconds * 60 + Integer.valueOf(values[2]);
		float msec = Integer.valueOf(values[values.length-1]);
		seconds += msec / 1000;
		return seconds + ""; // quick conversion to string
	}
	
	/**
	 * Add 2 numbers in {@link String} format, returning as a string
	 * <p>Note that they are converted to floats so may show some rounding errors
	 * @param x First number
	 * @param y Second Number
	 * @return numbers added together as a String
	 */
	protected String add(String x, String y) {
		if (x == null || y == null) {
			return "0";
		}
		float f = Float.valueOf(x) + Float.valueOf(y);
		return f+""; // quick conversion to string
	}
	
	/**
	 * Return a zip code string with leading zeros added to get it to 5 characters long.
	 * @param zip Partial ZIP code
	 * @return Zip code with zeros added to left hand side (if required)
	 */
	protected String zeroPadZip(String zip) {
		if (zip == null) {
			return null;
		}
		return String.format("%5s", zip).replaceAll(" ", "0");

	}
	
	/**
	 * Apply the normalizations required.
	 * <p>A new {@link Record} object is returned, the original is not altered
	 * 
	 * @param rec record in
	 * @return the record sent in with appropriate modifications needed
	 */
	public Record fixFields(final Record rec) {
		if (rec == null) {
			return null;
		}
		final Record out = new Record();
		out.setTimestamp(convertTimestamp(rec.getTimestamp()));
		out.setBarDuration(convertInterval(rec.getBarDuration()));
		out.setFooDuration(convertInterval(rec.getFooDuration()));
		out.setTotalDuration(add(out.getFooDuration(), out.getBarDuration()));
		if (rec.getFullName() != null)
			out.setFullName(rec.getFullName().toUpperCase());
		out.setZip(zeroPadZip(rec.getZip()));
		// no change to Address nor Notes
		out.setAddress(rec.getAddress());
		out.setNotes(rec.getNotes());
		return out;
	}
}
