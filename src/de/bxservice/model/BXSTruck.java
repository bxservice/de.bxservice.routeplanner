package de.bxservice.model;

import org.compiere.model.MResource;
import org.compiere.model.Query;
import org.compiere.util.Env;

public class BXSTruck extends ATransportationResource {

	public static final int S_ResourceType_ID = 1000001;
	
	private int  BAY_Driver_ID = 0;
	private int  BAY_CoDriver_ID = 0;

	private X_BAY_Delivery delivery;
	private BXSDriver driver;
	private BXSDriver coDriver;
	
	public BXSTruck(MResource resource) {
		super(resource);
		setDeliveryInformation();
	}
	
	private void setDeliveryInformation() {

		final String whereClause = "AD_Client_ID IN (0, ?) AND " + 
				X_BAY_Delivery.COLUMNNAME_BAY_Truck_ID + "=? ";

		delivery = new Query(Env.getCtx(), X_BAY_Delivery.Table_Name, whereClause, null)
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), resource.getS_Resource_ID()})
				.setOnlyActiveRecords(true)
				.setOrderBy(X_BAY_Delivery.COLUMNNAME_BAY_RouteDate + " DESC")
				.first();
		
		if (delivery != null) {
			BAY_Driver_ID = delivery.getBAY_Driver_ID();
			BAY_CoDriver_ID = delivery.getBAY_CoDriver_ID();
		}
	}

	public BXSDriver getDriver() {
		return driver;
	}

	public void setDriver(BXSDriver driver) {
		this.driver = driver;
	}

	public BXSDriver getCoDriver() {
		return coDriver;
	}

	public void setCoDriver(BXSDriver coDriver) {
		this.coDriver = coDriver;
	}
	
	public int getTruckID () {
		return resource.getS_Resource_ID();
	}

	public int getBAY_Driver_ID() {
		return BAY_Driver_ID;
	}

	public int getBAY_CoDriver_ID() {
		return BAY_CoDriver_ID;
	}
	
	public X_BAY_Delivery getDelivery() {
		return delivery;
	}
}
