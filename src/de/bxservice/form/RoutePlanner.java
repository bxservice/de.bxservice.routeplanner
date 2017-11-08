package de.bxservice.form;

import java.util.List;

import de.bxservice.model.BXSDriver;
import de.bxservice.model.BXSPlannerColumn;
import de.bxservice.model.BXSRoutePlanner;

public class RoutePlanner {

	private BXSRoutePlanner routePlanner;

	public void setBoardContent() {
		routePlanner = new BXSRoutePlanner();
	}
	
	public void refreshBoard() {
		routePlanner = null;
	}
	
	public List<BXSPlannerColumn> getColumns() {
		return routePlanner.getPlannerColumns();
	}
	
	public int getNumberOfColumns() {
		return routePlanner.getNumberOfColumns();
	}
	
	public int getNumberOfDrivers() {
		return routePlanner.getNumberOfDrivers();
	}
	
	public List<BXSDriver> getDrivers() {
		return routePlanner.getDrivers();
	}
	
	public boolean swapCard(BXSPlannerColumn startColumn, BXSPlannerColumn endColumn, BXSDriver driver) {

		boolean columnChanged = routePlanner.changeColumn(endColumn, driver);
		if (columnChanged) {
			startColumn.removeRecord(driver);
			endColumn.addRecord(driver);
			driver.setAsociatedColumn(endColumn);
		}
		return columnChanged;
	}
}
