package com.edgriebel.dataCleaning;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ConvertersTest {
	Converters impl;
	
	@Before
	public void setUp() {
		impl = new Converters();
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testTimeToEST_EDT() {
//		Matcher<T>
		assertNull("NULL in should return NULL", 
				impl.timeToEST_EDT(null));
		Date d = new Date();
		d.setHours(0); // hack but it's a test
		Date ret = impl.timeToEST_EDT(d);
		assertEquals("Midnight PST should be 3am EST", 3, ret.getHours());
		assertEquals("Date should be the same for Midnight PST -> EST", d.getDate(), ret.getDate());
	}

	@Test
	public void testConvertTimestamp() {
		assertNull("Null in should return null", impl.convertTimestamp(null));
		assertNull("Garbage string should return null", impl.convertTimestamp("foodbarbaz"));
		System.err.println("******** Stack trace here ^^^ is OK ********");
		// Because the target method uses SimpleDateFormat we'll hardcode it here
		// SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm:ss aa");
		String datestr = impl.convertTimestamp("09/11/2001 09:00:00 am");
		assertNotNull("real date should not be null", datestr);
		assertEquals("YYYY-MM-DD should be found", "2001-09-11", datestr.substring(0, 10));
		assertEquals("T should come after YYYY-MM-DD", "2001-09-11T", datestr.substring(0, 11));
		assertEquals("EST Timezone offset should appear at the end", "00", datestr.substring(datestr.length()-2));
	}

	@Test
	public void testConvertInterval() {
		assertNull("Null in should return null", impl.convertInterval(null));
		assertEquals("Empty string should return zero", "0", impl.convertInterval(""));
		assertEquals("Incorrect inverval should return 0", "0", impl.convertInterval("xxxxx"));
		System.err.println("******** warning message here ^^^ is OK ********");
		assertEquals("100 msec should return 0.1", (Double)0.1, Double.valueOf(impl.convertInterval("0:0:0.100")));
		assertEquals("1 minute should return 60", (Double)60d, Double.valueOf(impl.convertInterval("0:1:0.0")));
		assertEquals("1 hour should return 3600", (Double)3600d, Double.valueOf(impl.convertInterval("1:0:0.0")));
		assertEquals("111 hour should return 111*3600", (Double)(111*3600d), Double.valueOf(impl.convertInterval("111:0:0.0")));
		assertEquals("1 hour, 1 minute, 1 second, 1 msec should return 3661.001", (Double)3661.001d, Double.valueOf(impl.convertInterval("1:1:1.001")));
	}

	@Test
	public void testAdd() {
		assertEquals("both null should return 0", "0", impl.add(null, null));
		// test integers
		assertEquals("1+1=2", "2.0", impl.add("1", "1"));
		// test floats
		assertEquals("3.14 + 2.76 = pi+e", ((float)Math.PI + (float)Math.E)+"", impl.add(Math.PI+"", Math.E+""));
	}

	@Test
	public void testZeroPadZip() {
		assertNull("Null in should return null", impl.zeroPadZip(null));
		assertEquals("Empty string should return zeros", "00000", impl.zeroPadZip(""));
		assertEquals("'1' should be zero padded", "00001", impl.zeroPadZip("1"));
		assertEquals("5-digit zip should not be changed", "90120", impl.zeroPadZip("90120"));
	}
	
	@Test
	public void testFixFields() {
		/*
		 * We're not going to do many tests because we've already tested the methods.
		 */
		assertNull("Null record in should return null", impl.fixFields(null));
		Record r = new Record();
		r.setFooDuration("0:0:1.0");
		r.setBarDuration("0:0:1.0");
		r.setFullName("Hello World");
		r.setNotes("abcdef");
		r.setAddress("not a real address");
		r.setZip("1");
		Record ret = impl.fixFields(r);
		System.out.print(r+"\n"+ret);
		assertNotNull("Real Record in should return a record", ret);
		assertNotEquals("FooDuration should not be the same", r.getFooDuration(), ret.getFooDuration());
		assertNotEquals("BarDuration should not be the same", r.getBarDuration(), ret.getBarDuration());
		assertNotEquals("TotalDuration should not be the same", r.getTotalDuration(), ret.getTotalDuration());
		assertNotEquals("FullName should not be the same", r.getFullName(), ret.getFullName());
		// We CANNOT test this because it's very dependent on the value in being 5 chars or less than 5
		// assertNotEquals("Zip should not be the same", r.getZip(), ret.getZip());
		assertNotNull("Zip should not be null", ret.getZip());
	
		assertEquals("Address should not be different", r.getAddress(), ret.getAddress());
		assertEquals("Notes should not change", r.getNotes(), ret.getNotes());
	}

}
