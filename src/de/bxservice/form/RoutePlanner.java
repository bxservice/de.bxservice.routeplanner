package de.bxservice.form;

import java.util.List;

import de.bxservice.model.BXSRoutePlanner;
import de.bxservice.model.BXSTransportationResource;
import de.bxservice.model.MRoute;

public class RoutePlanner {

	private BXSRoutePlanner routePlanner;

	public void setBoardContent() {
		routePlanner = new BXSRoutePlanner();
	}
	
	public void refreshBoard() {
		routePlanner = null;
	}
	
	public List<MRoute> getRoutes() {
		return routePlanner.getRoutes();
	}
	
	public int getNumberOfColumns() {
		return routePlanner.getNumberOfColumns();
	}
	
	public int getNumberOfCards() {
		return routePlanner.getNumberOfCards();
	}
	
	public List<BXSTransportationResource> getCards() {
		return routePlanner.getCards();
	}
	
	public boolean hasMoreCards(MRoute route) {
		return routePlanner.hasMoreCards(route);
	}
	
	public BXSTransportationResource getRecord(MRoute route) {
		return routePlanner.getRecord(route);
	}
	
	/*public boolean swapCard(BXSPlannerColumn startColumn, BXSPlannerColumn endColumn, BXSDriver driver) {

		boolean columnChanged = routePlanner.changeColumn(endColumn, driver);
		if (columnChanged) {
			startColumn.removeRecord(driver);
			endColumn.addRecord(driver);
			driver.setAsociatedColumn(endColumn);
		}
		return columnChanged;
	}*/
}
