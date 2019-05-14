package com.edgriebel.dataCleaning;

import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

public class Record {

	@CsvBindByName
	@CsvDate("MM/dd/yy hh:mm:ss aa")
	private Date Timestamp;
	
	@CsvBindByName(column="Address")
	protected String Address;
	
	@CsvBindByName
	private String ZIP;
	
	@CsvBindByName
	// @CsvCustomBindByName(converter = MyClass.class)
	private String FullName;
	
	@CsvBindByName
	private String FooDuration;
	
	@CsvBindByName
	private String BarDuration;
	
	@CsvBindByName
	private String TotalDuration;
	
	@CsvBindByName
	private String Notes;

	public Date getTimestamp() {
		return Timestamp;
	}

	public void setTimestamp(Date timestamp) {
		Timestamp = timestamp;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getZIP() {
		return ZIP;
	}

	public void setZIP(String zIP) {
		ZIP = zIP;
	}

	public String getFullName() {
		return FullName;
	}

	public void setFullName(String fullName) {
		FullName = fullName;
	}

	public String getFooDuration() {
		return FooDuration;
	}

	public void setFooDuration(String fooDuration) {
		FooDuration = fooDuration;
	}

	public String getBarDuration() {
		return BarDuration;
	}

	public void setBarDuration(String barDuration) {
		BarDuration = barDuration;
	}

	public String getTotalDuration() {
		return TotalDuration;
	}

	public void setTotalDuration(String totalDuration) {
		TotalDuration = totalDuration;
	}

	public String getNotes() {
		return Notes;
	}

	public void setNotes(String notes) {
		Notes = notes;
	}
	
	public String toString() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
				Timestamp,Address,ZIP,FullName,FooDuration,BarDuration,TotalDuration,Notes);
	}
}
