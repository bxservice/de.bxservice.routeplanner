package de.bxservice.model;

import java.util.ArrayList;
import java.util.List;

public class BXSPlannerColumn {
	
	private BXSTruck truck;
	private List<BXSDriver> records = new ArrayList<>();
	private int recordNumber = 0;
	private boolean available = false;
	private String name = "";
	private String description = "";
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public BXSTruck getTruck() {
		return truck;
	}
	
	public void setTruck(BXSTruck resource) {
		this.truck = resource;
		setName(resource.getName());
		setDescription(resource.getDescription());
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	public void addRecord(BXSDriver driver) {
		records.add(driver);
	}

	public List<BXSDriver> getRecords() {
		return records;
	}
	
	public void removeRecord(BXSDriver driver) {
		for (BXSDriver c : records){
			if (c.equals(driver)) {
				records.remove(driver);
				break;
			}
		}
	}
	
	public boolean hasMoreRecords() {
		return recordNumber < records.size();
	}
	
	public BXSDriver getRecord() {
		BXSDriver card = records.get(recordNumber);
		recordNumber++;
		return card;
	}

}
