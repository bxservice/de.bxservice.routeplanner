package de.bxservice.model;

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

	public BXSRoutePlanner() {
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
			routeResources.get(route).add(resource);
		}
	}
	
	private MRoute getRoute(BXSTransportationResource resource) {
		for (MRoute route : getRoutes()) {
			if (!resource.isAvailable() && route.getValue().equals(MRoute.UNAVAILABLE_VALUE)) {
				return route;
			} else if (resource.isAvailable()) {
				if (resource.getDelivery() == null && route.getValue().equals(MRoute.AVAILABLE_VALUE))
					return route;
				else if (resource.getDelivery() != null && 
						resource.getDelivery().getBAY_Route_ID() == route.getBAY_Route_ID())
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
		routeResources.get(route).remove(record);
		return record;
	}
	
	/*	public boolean changeColumn(BXSPlannerColumn endColumn, BXSDriver driver) {

		boolean success = true;

		// First case -> End column is a truck
		if (endColumn.getTruck() != null) {
			X_BAY_Delivery driverDelivery = driver.getDelivery();
			X_BAY_Delivery truckDelivery = endColumn.getTruck().getDelivery();

			if (truckDelivery == null) {
				truckDelivery = new X_BAY_Delivery(Env.getCtx(), 0, null);
				truckDelivery.setBAY_Truck_ID(endColumn.getTruck().getTruckID());
			}

			String columnName = null;
			if (truckDelivery.getBAY_Driver_ID() == 0) {
				columnName = X_BAY_Delivery.COLUMNNAME_BAY_Driver_ID;
			} else {
				columnName = X_BAY_Delivery.COLUMNNAME_BAY_CoDriver_ID;
			}
			success = truckDelivery.set_ValueOfColumnReturningBoolean(columnName, driver.getResource().getS_Resource_ID());
			truckDelivery.saveEx();

			if (driverDelivery != null) {
				columnName = null;
				if (!driver.isCoDriver()) {
					columnName = X_BAY_Delivery.COLUMNNAME_BAY_Driver_ID;
				} else {
					columnName = X_BAY_Delivery.COLUMNNAME_BAY_CoDriver_ID;
				}
				driverDelivery.set_ValueOfColumnReturningBoolean(columnName, null);
				driverDelivery.saveEx();
			}
		} else {
			X_BAY_Delivery driverDelivery = driver.getDelivery();
			if (driverDelivery != null) {

			}

			driver.setAssignedTruck(null);
		}

		return success;
	}*/
}