package de.bxservice.model;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MResource;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class BXSRoutePlanner {

	private List<BXSPlannerColumn> routePlannerColumns = null;
	private List<BXSTruck> trucks = null;
	private List<BXSDriver> drivers = null;
	private int numberOfDrivers = 0;

	public BXSRoutePlanner() {
		getPlannerColumns();
		getDrivers();
		setTruckDrivers();
		setDriversColumns();
	}

	public int getNumberOfColumns() {
		if (routePlannerColumns == null)
			routePlannerColumns = getPlannerColumns();
		return routePlannerColumns.size();
	}

	public List<BXSPlannerColumn> getPlannerColumns() {
		
		if (routePlannerColumns != null && !routePlannerColumns.isEmpty())
			return routePlannerColumns;

		routePlannerColumns = new ArrayList<>();
		//Create first two special columns - Available and Unavailable
		BXSPlannerColumn plannerColumn = new BXSPlannerColumn();
		plannerColumn.setName("Unavailable");
		routePlannerColumns.add(plannerColumn);

		plannerColumn = new BXSPlannerColumn();
		plannerColumn.setAvailable(true);
		plannerColumn.setName("Available");
		routePlannerColumns.add(plannerColumn);

		for (BXSTruck truck : getTrucks()) {
			plannerColumn = new BXSPlannerColumn();
			plannerColumn.setTruck(truck);
			routePlannerColumns.add(plannerColumn);
		}

		return routePlannerColumns;
	}

	public List<BXSTruck> getTrucks() {

		if (trucks == null) {
			trucks = new ArrayList<>();
			List<MResource> resources = new Query(Env.getCtx(), MResource.Table_Name, " AD_Client_ID IN (0, ?) AND S_ResourceType_ID=?", null)
					.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), BXSTruck.S_ResourceType_ID})
					.setOnlyActiveRecords(true)
					.setOrderBy(MResource.COLUMNNAME_Name)
					.list();

			for (MResource res : resources)
				trucks.add(new BXSTruck(res));
		}

		return trucks;
	}

	public int getNumberOfDrivers() {
		if (numberOfDrivers <= 0)
			getDrivers();
		numberOfDrivers = drivers.size();
		return numberOfDrivers;
	}

	public List<BXSDriver> getDrivers() {
		if (drivers == null) {
			drivers = new ArrayList<>();

			List<MResource> resources = new Query(Env.getCtx(), MResource.Table_Name, " AD_Client_ID IN (0, ?) AND S_ResourceType_ID=?", null)
					.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), BXSDriver.S_ResourceType_ID})
					.setOnlyActiveRecords(true)
					.setOrderBy(MResource.COLUMNNAME_Name)
					.list();

			for (MResource res : resources) {
				drivers.add(new BXSDriver(res));
			}
		}

		return drivers;
	}

	private void setDriversColumns() {

		for (BXSPlannerColumn column : getPlannerColumns()) {
			for (BXSDriver driver : getDrivers()) {
				if (driver.getAssignedTruck() == null) {
					if (column.getTruck() == null && 
							driver.isAvailable() == column.isAvailable()) {
						driver.setAsociatedColumn(column);
						column.addRecord(driver);
					}
				} else if (column.getTruck() != null && driver.getAssignedTruck().getTruckID() == 
						column.getTruck().getTruckID()) {
					driver.setAsociatedColumn(column);
					column.addRecord(driver);
				}
			}
		}
	}

	private void setTruckDrivers() {

		for (BXSTruck truck : getTrucks()) {
			for (BXSDriver driver : getDrivers()) {
				if (truck.getTruckID() == driver.getBAY_Truck_ID()) {
					driver.setAssignedTruck(truck);
					if (driver.isCoDriver())
						truck.setCoDriver(driver);
					else
						truck.setDriver(driver);
				}
			}
		}
	}

	public boolean changeColumn(BXSPlannerColumn endColumn, BXSDriver driver) {

		boolean success = true;

		// First case -> End column is a truck
		if (endColumn.getTruck() != null) {
			X_BAY_Delivery driverDelivery = driver.getDelivery();
			X_BAY_Delivery truckDelivery = endColumn.getTruck().getDelivery();

			if (truckDelivery == null) {
				truckDelivery = new X_BAY_Delivery(Env.getCtx(), 0, null);
				truckDelivery.setBAY_Truck_ID(endColumn.getTruck().getTruckID());
			}

			success = truckDelivery.set_ValueOfColumnReturningBoolean(X_BAY_Delivery.COLUMNNAME_BAY_Driver_ID, driver.getResource().getS_Resource_ID());
			truckDelivery.saveEx();

			if (driverDelivery != null) {
				driverDelivery.set_ValueOfColumnReturningBoolean(X_BAY_Delivery.COLUMNNAME_BAY_Driver_ID, null);
				driverDelivery.saveEx();
			}
		} else {
			X_BAY_Delivery driverDelivery = driver.getDelivery();
			if (driverDelivery != null) {

			}

			driver.setAssignedTruck(null);
		}

		return success;
	}
}