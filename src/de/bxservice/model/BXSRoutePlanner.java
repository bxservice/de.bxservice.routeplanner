package de.bxservice.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.compiere.model.Query;
import org.compiere.util.Env;

public class BXSRoutePlanner {

	private List<MRoute> routes;
	private List<BXSTransportationResource> resources;
	private Map<MRoute, List<BXSTransportationResource>> routeResources = new HashMap<>();
	private Timestamp routeDate;
	
	public BXSRoutePlanner(Timestamp routeDate) {
		this.routeDate = routeDate;
		getRoutes();
		getCards();
		setRouteResources();
	}

	public int getNumberOfColumns() {
		if (routes == null)
			routes = getRoutes();
		return routes.size();
	}

	public List<MRoute> getRoutes() {

		if (routes != null && !routes.isEmpty())
			return routes;

		routes = new Query(Env.getCtx(), MRoute.Table_Name, " AD_Client_ID IN (0, ?) ", null)
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx())})
				.setOnlyActiveRecords(true)
				.setOrderBy(MRoute.COLUMNNAME_Value)
				.list();

		for (MRoute route : routes)
			routeResources.put(route, new ArrayList<>());

		return routes;
	}

	public List<BXSTransportationResource> getCards() {
		if (resources == null) 
			resources = BXSTransportationResource.getTResources();

		return resources;
	}

	public int getNumberOfCards() {
		if (resources == null)
			getCards();
		return  resources.size();
	}

	private void setRouteResources() {

		for (BXSTransportationResource resource : resources) {
			MRoute route = getRoute(resource);
			resource.setRoute(route);
			routeResources.get(route).add(resource);
		}
	}

	private MRoute getRoute(BXSTransportationResource resource) {
		for (MRoute route : getRoutes()) {
			if (!resource.isAvailable(routeDate) && route.getValue().equals(MRoute.UNAVAILABLE_VALUE)) {
				return route;
			} else if (resource.isAvailable(routeDate)) {
				if (resource.getDelivery(routeDate) == null && route.getValue().equals(MRoute.AVAILABLE_VALUE))
					return route;
				else if (resource.getDelivery(routeDate) != null && 
						resource.getDelivery(routeDate).getBAY_Route_ID() == route.getBAY_Route_ID())
					return route;
			}
		}

		return null;
	}

	public boolean hasMoreCards(MRoute route) {
		return !routeResources.get(route).isEmpty();	
	}

	public BXSTransportationResource getRecord(MRoute route) {
		BXSTransportationResource record = routeResources.get(route).get(0);
		return record;
	}

	public BXSTransportationResource getTruck(MRoute route) {

		for (BXSTransportationResource record : routeResources.get(route)) {
			if (record.isTruck())
				return record;
		}

		return null;
	}

	public BXSTransportationResource getDriver(MRoute route) {
		for (BXSTransportationResource record : routeResources.get(route)) {
			if (record.isDriver())
				return record;
		}

		return null;

	}

	public BXSTransportationResource getCoDriver(MRoute route) {
		for (BXSTransportationResource record : routeResources.get(route)) {
			if (record.isCoDriver())
				return record;
		}

		return null;
	}

	public void removeRecord(BXSTransportationResource record) {
		routeResources.get(record.getRoute()).remove(record);
	}

	public boolean assignTruck(BXSTransportationResource selectedCard, MRoute endRoute) {

		MDelivery originalDelivery = selectedCard.getDelivery(routeDate);

		MDelivery routeDelivery = endRoute.getDelivery(false, routeDate);
		selectedCard.setDelivery(routeDelivery);
		selectedCard.setRoute(endRoute);
		routeDelivery.setBAY_Truck_ID(selectedCard.getResource_ID());
		routeDelivery.saveEx();

		if (originalDelivery != null) {
			originalDelivery.setBAY_Truck_ID(0);
			originalDelivery.saveEx();
		}

		return true;
	}

	public boolean assignDriver(BXSTransportationResource selectedCard, MRoute endRoute, boolean isCoDriver) {

		boolean success = false;
		MDelivery originalDelivery = selectedCard.getDelivery(routeDate);

		String columnName = null;
		if (!isCoDriver) {
			columnName = MDelivery.COLUMNNAME_BAY_Driver_ID;
		} else {
			columnName = MDelivery.COLUMNNAME_BAY_CoDriver_ID;
		}

		String originalColumnName = null;
		if (!selectedCard.isCoDriver()) {
			originalColumnName = MDelivery.COLUMNNAME_BAY_Driver_ID;
		} else {
			originalColumnName = MDelivery.COLUMNNAME_BAY_CoDriver_ID;
		}

		MDelivery routeDelivery = endRoute.getDelivery(false, routeDate);
		selectedCard.setDelivery(routeDelivery);
		selectedCard.setRoute(endRoute);
		success = routeDelivery.set_ValueOfColumnReturningBoolean(columnName, selectedCard.getResource().getS_Resource_ID());
		routeDelivery.saveEx();

		if (originalDelivery != null) {
			originalDelivery.set_ValueOfColumnReturningBoolean(originalColumnName, null);
			originalDelivery.saveEx();
		}

		return success;
	}

	public boolean makeResourceAvailable(BXSTransportationResource selectedCard, MRoute availableRoute) {

		boolean success = false;
		MDelivery resourceDelivery = selectedCard.getDelivery(routeDate);
		
		String columnName = null;
		if (selectedCard.isDriver()) {
			columnName = MDelivery.COLUMNNAME_BAY_Driver_ID;
		} else if (selectedCard.isCoDriver()) {
			columnName = MDelivery.COLUMNNAME_BAY_CoDriver_ID;
		} else {
			columnName = MDelivery.COLUMNNAME_BAY_Truck_ID;
		}

		selectedCard.setDelivery(null);
		selectedCard.setRoute(availableRoute);
		success = resourceDelivery.set_ValueOfColumnReturningBoolean(columnName, null);
		resourceDelivery.saveEx();

		return success;
	}
	
	public boolean swapCards(BXSTransportationResource startCard, BXSTransportationResource endCard) {
		if (startCard.isDriver() || startCard.isCoDriver()) 
			return swapDrivers(startCard, endCard);
		else
			return swapTrucks(startCard, endCard);
	}
	
	private boolean swapTrucks(BXSTransportationResource startCard, BXSTransportationResource endCard) {

		MDelivery startDelivery = startCard.getDelivery(routeDate);
		MDelivery endDelivery = endCard.getDelivery(routeDate);
		MRoute startRoute = startCard.getRoute();
		MRoute endRoute = endCard.getRoute();

		startCard.setDelivery(endDelivery);
		startCard.setRoute(endRoute);
		endCard.setDelivery(startDelivery);
		endCard.setRoute(startRoute);

		startDelivery.setBAY_Truck_ID(endCard.getResource_ID());
		startDelivery.saveEx();
		endDelivery.setBAY_Truck_ID(startCard.getResource_ID());
		endDelivery.saveEx();

		return true;
	}
	
	private boolean swapDrivers(BXSTransportationResource startCard, BXSTransportationResource endCard) {

		boolean success = false;

		MDelivery startDelivery = startCard.getDelivery(routeDate);
		MDelivery endDelivery = endCard.getDelivery(routeDate);
		MRoute startRoute = startCard.getRoute();
		MRoute endRoute = endCard.getRoute();
		
		String startColumnName = null;
		if (startCard.isDriver()) {
			startColumnName = MDelivery.COLUMNNAME_BAY_Driver_ID;
		} else if (startCard.isCoDriver()) {
			startColumnName = MDelivery.COLUMNNAME_BAY_CoDriver_ID;
		} else
			return false;

		String endColumnName = null;
		if (endCard.isDriver()) {
			endColumnName = MDelivery.COLUMNNAME_BAY_Driver_ID;
		} else if (endCard.isCoDriver()) {
			endColumnName = MDelivery.COLUMNNAME_BAY_CoDriver_ID;
		} else 
			return false;
		
		startCard.setDelivery(endDelivery);
		startCard.setRoute(endRoute);
		endCard.setDelivery(startDelivery);
		endCard.setRoute(startRoute);

		success = startDelivery.set_ValueOfColumnReturningBoolean(startColumnName, endCard.getResource_ID());
		startDelivery.saveEx();
		success = endDelivery.set_ValueOfColumnReturningBoolean(endColumnName, startCard.getResource_ID());
		endDelivery.saveEx();

		return success;
	}
}