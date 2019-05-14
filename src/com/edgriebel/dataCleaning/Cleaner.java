package com.edgriebel.dataCleaning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class Cleaner {
	public static void main(String [] args) throws Exception {
		Cleaner cleaner = new Cleaner();

		List<Record> goodRecs = null;
		List<Record> badRecs = null;
		
		System.out.println("\n*********\nGOOD\n*********\n");
		
		try (Reader reader = new BufferedReader(new FileReader(new File("sample.csv")))) {
			/*
			for (String[] line : cleaner.readAll(reader)) {
				for (String it : line) {
					System.out.print(it + ",");
				}
				System.out.println();
			}
			*/
			goodRecs = cleaner.readAll(reader);
		}
		goodRecs.stream().forEach(System.out::println);
		
		System.out.println("\n*********\nBAD\n*********\n");
		File f = new File("sample-with-broken-utf8.csv");
		InputStream fis = new FileInputStream(f);
		
		CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();
		cs = cs.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
		try (Reader reader = new InputStreamReader(fis, cs)) {
		// try(Reader reader = new BufferedReader(new FileReader())) {
/*			for (String[] line : cleaner.readAll(reader)) {
				for (String it : line) {
					System.out.print(it + ",");
				}
				System.out.println();
			}*/
			badRecs = cleaner.readAll(reader);
		}
		List<Record> badRecsXform = badRecs.stream().peek(System.out::println).map(cleaner::fixFields).collect(Collectors.toList());
		
		badRecsXform.stream().forEach(System.out::println);
		
		System.out.println("\n*********\nWRITE\n*********\n");
		StatefulBeanToCsv<Record> csvwriter = new StatefulBeanToCsvBuilder<Record>(new PrintWriter(System.out)).withThrowExceptions(false).build();
		csvwriter.write(badRecsXform);
		
		/*
		System.out.println("\n*********\nMORE\n*********\n");

		try (Reader reader = new BufferedReader(new FileReader(new File("sample.csv")))) {
			CSVReaderHeaderAware csvreader = new CSVReaderHeaderAware(reader);
			while(true) {
				Map<String, String> values = csvreader.readMap();
				if (values == null)
					break;
				for (Map.Entry<String, String> kv : values.entrySet()) {
					System.out.println(kv);
				}
			}
			csvreader.close();
		}
				 */

	}
	
	public List<Record> readAll(Reader reader) throws IOException {
		CsvToBean<Record> csvreader = new CsvToBeanBuilder<Record>(reader)
				.withType(Record.class)
				.build();
		return csvreader.parse();
	}

	public Date timeToEST_EDT(Date d) {
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("US/Eastern"));
		c.setTime(d);
		c.add(Calendar.HOUR, 3);
		return c.getTime();
	}
	
	public Record fixFields(Record rec) {
		rec.setTimestamp(timeToEST_EDT(rec.getTimestamp()));
		return rec;
	}
}
