package com.edgriebel.dataCleaning;

import java.text.DateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.TimeZone;

public class Test {
	public static void main(String [] args) {
		System.out.println("Hello!");
		Date d = new Date();
		DateFormat df = DateFormat.getInstance();
		System.out.println(df.format(d));
		System.out.println(d);
		for (String s : TimeZone.getAvailableIDs()) {
			System.out.println(s);
		}
		Duration dur = Duration.ofSeconds(45);
		Duration dur1 = Duration.ofSeconds(30);
		System.out.println(dur + " " + dur.minus(dur1));

		
	}
}
