package com.edgriebel.dataCleaning;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class Cleaner {
	public static void main(String [] args) throws Exception {
		Converters converters = new Converters();

		
		final InputStream is;
		if (args.length > 0) {
			File f = new File(args[0]);
			is = new FileInputStream(f);
		}
		else {
			is = System.in;
		}
		
		// FIXME this should replace all invalid UTF-8 characters with the "Unicode Replacement Character" (e.g. <?>) but it doesn't  
		CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();
		cs = cs.onMalformedInput(CodingErrorAction.REPLACE)
			   .onUnmappableCharacter(CodingErrorAction.REPLACE);
		
		System.err.printf("Reading records...\n");
		final List<Record> records;
		try (Reader reader = new InputStreamReader(is, cs)) {
			CsvToBean<Record> csvreader = new CsvToBeanBuilder<Record>(reader)
					.withType(Record.class)
					.build();
			records = csvreader.parse();
		}
		
		// Normalize records
		final List<Record> transformedRecs = records.stream()
				// .peek(System.out::println) // debug print raw values
				.map(converters::fixFields)
				.collect(Collectors.toList());
		
		// transformedRecs.stream().forEach(System.err::println); // debug
		
		System.err.printf("Writing %d records...\n", transformedRecs.size());
		ColumnPositionMappingStrategy<Record> mappingStrategy = new CustomMappingStrategy<>();
		mappingStrategy.setType(Record.class);
		
		try (Writer w = new PrintWriter(System.out)) { 
		StatefulBeanToCsv<Record> csvwriter = new StatefulBeanToCsvBuilder<Record>(w)
				.withMappingStrategy(mappingStrategy)
				.withApplyQuotesToAll(false)
				.build();
		csvwriter.write(records);
		if (!csvwriter.getCapturedExceptions().isEmpty())
			System.err.println("*********\nEXCEPTIONS\n*********\n");
			csvwriter.getCapturedExceptions().stream().peek(System.err::println);
		}
		System.err.println("Done...");
	}
	
	static class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
		private final String[] columns = {"timestamp","address","zip","fullName","fooDuration","barDuration","totalDuration","notes"};
		
		{
			super.setColumnMapping(columns);
		} 
		
		@Override
		public String[] generateHeader(T bean) {
			return columns;
		}
	}
}
