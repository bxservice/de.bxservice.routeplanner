package de.bxservice.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

public class MRoute extends X_BAY_Route {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8249075332965223214L;
	
	public static final String UNAVAILABLE_VALUE = "0000";
	public static final String AVAILABLE_VALUE   = "0001";
	private MDelivery delivery;

	public MRoute(Properties ctx, int BAY_Route_ID, String trxName) {
		super(ctx, BAY_Route_ID, trxName);
	}
	
	public MRoute(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
	public MDelivery getDelivery(boolean reQuery, Timestamp routeDate) {
		if (delivery != null && !reQuery)
			return delivery;
		Timestamp date = TimeUtil.trunc(routeDate, TimeUtil.TRUNC_DAY);

		delivery = new Query(Env.getCtx(), MDelivery.Table_Name, "AD_Client_ID IN (0, ?) AND TRUNC(" + 
								MDelivery.COLUMNNAME_BAY_RouteDate + ")=? AND " + 
								COLUMNNAME_BAY_Route_ID + "=?", null)
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), date, getBAY_Route_ID()})
				.setOrderBy(MDelivery.COLUMNNAME_BAY_RouteDate + " DESC")
				.setOnlyActiveRecords(true)
				.first();
		
		if (delivery == null) {
			delivery = new MDelivery(Env.getCtx(), 0, null);
			delivery.setBAY_Route_ID(getBAY_Route_ID());
			delivery.setBAY_RouteDate(date);
			delivery.saveEx();
		}
		
		return delivery;
	}
	
}
