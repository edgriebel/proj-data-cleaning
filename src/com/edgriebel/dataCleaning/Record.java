package com.edgriebel.dataCleaning;

import com.opencsv.bean.CsvBindByName;

public class Record {

	/**
	 * Even though we know this is a timestamp and we can 
	 * convert it to a java.util.Date, we can't control
	 * the output format for the date so we'll keep
	 * this as a string and to String->Date->String in 
	 * another method.
	 */ 
	@CsvBindByName(column="Timestamp", locale="UTF-8")
	// @CsvDate("MM/dd/yy hh:mm:ss aa")
	private String timestamp;
	
	@CsvBindByName(column="Address", locale="UTF-8")
	protected String address;
	
	@CsvBindByName(column="ZIP", locale="UTF-8")
	private String zip;
	
	@CsvBindByName(column="FullName", locale="UTF-8")
	// @CsvCustomBindByName(converter = MyClass.class)
	private String fullName;
	
	@CsvBindByName(column="FooDuration", locale="UTF-8")
	private String fooDuration;
	
	@CsvBindByName(column="BarDuration", locale="UTF-8")
	private String barDuration;
	
	@CsvBindByName(column="TotalDuration", locale="UTF-8")
	private String totalDuration;
	
	@CsvBindByName(column="Notes", locale="UTF-8")
	private String notes;

	public String toString() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
				timestamp, address, zip, fullName, fooDuration, barDuration, totalDuration, notes);
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFooDuration() {
		return fooDuration;
	}

	public void setFooDuration(String fooDuration) {
		this.fooDuration = fooDuration;
	}

	public String getBarDuration() {
		return barDuration;
	}

	public void setBarDuration(String barDuration) {
		this.barDuration = barDuration;
	}

	public String getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(String totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
