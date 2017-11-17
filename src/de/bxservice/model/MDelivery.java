package de.bxservice.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class MDelivery extends X_BAY_Delivery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2908061064495847636L;

	public MDelivery(Properties ctx, int BAY_Delivery_ID, String trxName) {
		super(ctx, BAY_Delivery_ID, trxName);
	}

	public MDelivery(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	/**
	 * Returns a copy of the original delivery with no date
	 * @param originalDelivery
	 * @param trxName
	 * @return
	 */
	public static MDelivery copyDelivery(MDelivery originalDelivery, String trxName) {

		MDelivery deliveryCopy = new MDelivery(Env.getCtx(), 0, trxName);
		deliveryCopy.setBAY_Driver_ID(originalDelivery.getBAY_Driver_ID());
		deliveryCopy.setBAY_CoDriver_ID(originalDelivery.getBAY_CoDriver_ID());
		deliveryCopy.setBAY_Truck_ID(originalDelivery.getBAY_Truck_ID());
		deliveryCopy.setBAY_Route_ID(originalDelivery.getBAY_Route_ID());
		
		return deliveryCopy;
	}
	
	public static Timestamp getLastDeliveryDate(String trxName) {
		return DB.getSQLValueTSEx(trxName, "SELECT MAX(" + MDelivery.COLUMNNAME_BAY_RouteDate + ") FROM " 
				+ MDelivery.Table_Name + " WHERE AD_Client_ID IN (0, ?)", new Object[]{Env.getAD_Client_ID(Env.getCtx())});
	}

	public static List<MDelivery> getLastDateDeliveries() {

		String whereClause = "AD_Client_ID IN (0, ?) AND " + MDelivery.COLUMNNAME_BAY_RouteDate + " = (SELECT MAX(" + 
				MDelivery.COLUMNNAME_BAY_RouteDate + ") FROM " + MDelivery.Table_Name + " WHERE AD_Client_ID IN (0, ?))"; 

		List<MDelivery> deliveries = new Query(Env.getCtx(), MDelivery.Table_Name, whereClause, null)
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), Env.getAD_Client_ID(Env.getCtx())})
				.setOrderBy(MDelivery.COLUMNNAME_BAY_RouteDate + " DESC")
				.setOnlyActiveRecords(true)
				.list();

		return deliveries;
	}
}
