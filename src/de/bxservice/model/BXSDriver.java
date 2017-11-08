package de.bxservice.model;

import org.compiere.model.MResource;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class BXSDriver extends ATransportationResource {

	public static final int S_ResourceType_ID = 1000000;

	private boolean isCoDriver = false;
	private int  BAY_Truck_ID = 0;
	
	private X_BAY_Delivery delivery;
	private BXSTruck assignedTruck = null;
	private BXSPlannerColumn asociatedColumn = null;

	public BXSDriver(MResource resource) {
		super(resource);
		setDeliveryInformation();
	}

	private void setDeliveryInformation() {

		final String whereClause = "AD_Client_ID IN (0, ?) AND (" + 
				X_BAY_Delivery.COLUMNNAME_BAY_Driver_ID + "=? OR " +
				X_BAY_Delivery.COLUMNNAME_BAY_CoDriver_ID + "=?) ";

		delivery = new Query(Env.getCtx(), X_BAY_Delivery.Table_Name, whereClause, null)
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), resource.getS_Resource_ID(), resource.getS_Resource_ID()})
				.setOnlyActiveRecords(true)
				.setOrderBy(X_BAY_Delivery.COLUMNNAME_BAY_RouteDate + " DESC")
				.first();
		
		if (delivery != null) {
			BAY_Truck_ID = delivery.getBAY_Truck_ID();
			if (delivery.getBAY_Driver_ID() != resource.getS_Resource_ID())
				isCoDriver = true;
		}
	}

	public boolean isCoDriver() {
		return isCoDriver;
	}

	public void setCoDriver(boolean isCoDriver) {
		this.isCoDriver = isCoDriver;
	}

	public BXSTruck getAssignedTruck() {
		return assignedTruck;
	}

	public void setAssignedTruck(BXSTruck assignedTruck) {
		this.assignedTruck = assignedTruck;
	}

	public BXSPlannerColumn getAsociatedColumn() {
		return asociatedColumn;
	}

	public void setAsociatedColumn(BXSPlannerColumn asociatedColumn) {
		this.asociatedColumn = asociatedColumn;
	}

	public int getBAY_Truck_ID() {
		return BAY_Truck_ID;
	}

	public X_BAY_Delivery getDelivery() {
		return delivery;
	}

}
