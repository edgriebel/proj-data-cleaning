package com.edgriebel.dataCleaning;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Converters {
	public Date timeToEST_EDT(Date d) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("US/Eastern"));
		c.setTime(d);
		c.add(Calendar.HOUR, 3);
		return c.getTime();
	}

	public String convertTimestamp(String ts) {
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
	public String convertInterval(String interval) {
		if (interval == null) {
			return null;
		}
		if (interval.isEmpty()) {
			return "0";
		}
		String [] values = interval.split("[:.]");
		float seconds = Integer.valueOf(values[0]);
		seconds = seconds * 60 + Integer.valueOf(values[1]);
		seconds = seconds * 60 + Integer.valueOf(values[2]);
		float msec = Integer.valueOf(values[values.length-1]);
		seconds += msec / 1000;
		return seconds + ""; // quick conversion to string
	}
	
	public String add(String x, String y) {
		if (x == null || y == null) {
			return "0";
		}
		float f = Float.valueOf(x) + Float.valueOf(y);
		return f+""; // quick conversion to string
	}
	
	public Record fixFields(Record rec) {
		rec.setTimestamp(convertTimestamp(rec.getTimestamp()));
		rec.setBarDuration(convertInterval(rec.getBarDuration()));
		rec.setFooDuration(convertInterval(rec.getFooDuration()));
		rec.setTotalDuration(add(rec.getFooDuration(), rec.getBarDuration()));
		rec.setFullName(rec.getFullName().toUpperCase());
		rec.setZip(String.format("%5s", rec.getZip()).replaceAll(" ", "0"));
		return rec;
	}
}
