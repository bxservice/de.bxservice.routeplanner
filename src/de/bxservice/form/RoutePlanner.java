package de.bxservice.form;

import java.sql.Timestamp;
import java.util.List;

import de.bxservice.model.BXSRoutePlanner;
import de.bxservice.model.BXSTransportationResource;
import de.bxservice.model.MRoute;
import de.bxservice.webui.form.WRoutePlanner;

public class RoutePlanner {

	private BXSRoutePlanner routePlanner;
	private String errorMessage;

	public void setBoardContent(Timestamp routeDate) {
		routePlanner = new BXSRoutePlanner(routeDate);
		errorMessage = null;
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
	
	public BXSTransportationResource getTruck(MRoute route) {
		return routePlanner.getTruck(route);
	}
	
	public BXSTransportationResource getDriver(MRoute route) {
		return routePlanner.getDriver(route);
	}

	public BXSTransportationResource getCoDriver(MRoute route) {
		return routePlanner.getCoDriver(route);
	}
	
	public void removeRecord(BXSTransportationResource record) {
		routePlanner.removeRecord(record);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * Assign the selected card to the empty cell
	 * @param selectedCard  Selected card
	 * @param endColumn Column where the card is dropped
	 * @param cellType  Type of cell where the card is dropped
	 * @return boolean if the swap was possible -> if not the errorMessage string is assigned
	 */
	public boolean assignCard(BXSTransportationResource selectedCard, MRoute endColumn, String cellType) {

		setErrorMessage(null);
		MRoute startColumn = selectedCard.getRoute();
		
		if (WRoutePlanner.TRUCK_CELL.equals(cellType)) { //Move to an empty truck cell
			if (!selectedCard.isTruck()) {
				setErrorMessage("Only trucks can be assigned to this cell");
				return false;
			}
			//Assign a truck to an empty truck cell
			return routePlanner.assignTruck(selectedCard, endColumn);
		} else if (WRoutePlanner.DRIVER_CELL.equals(cellType) || WRoutePlanner.CODRIVER_CELL.equals(cellType)) { //Move to an empty driver cell
			if (selectedCard.isTruck()) {
				setErrorMessage("Only drivers can be assigned to this cell");
				return false;
			}
			
			return routePlanner.assignDriver(selectedCard, endColumn, WRoutePlanner.CODRIVER_CELL.equals(cellType));
		} else if (WRoutePlanner.CARD_CELL.equals(cellType)) { //Move to a cell with a card 
			//First case: Move to a cell in available
			if (endColumn.getValue().equals(MRoute.AVAILABLE_VALUE)) {
				//If from available to available -> Do nothing
				if (startColumn.getValue().equals(MRoute.AVAILABLE_VALUE))
					return false;
				
				return routePlanner.makeResourceAvailable(selectedCard, endColumn);
			}
		}

		return false;
	}
	
	public boolean swapCard(BXSTransportationResource selectedCard, BXSTransportationResource endCard) {
		setErrorMessage(null);
		
		if (selectedCard.getResourceType_ID() != endCard.getResourceType_ID()) { 
			setErrorMessage("Can only swap card of the same type");
			return false;
		} 
		
		return routePlanner.swapCards(selectedCard, endCard);
	}
}
