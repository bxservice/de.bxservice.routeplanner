package de.bxservice.model;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.adempiere.base.IColumnCalloutFactory;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.I_C_Order;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.Env;

public class RoutePlanner_Callouts implements IColumnCalloutFactory {
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(RoutePlanner_Callouts.class);

	@Override
	public IColumnCallout[] getColumnCallouts(String tableName, String columnName) {

		if (tableName.equalsIgnoreCase(I_C_Order.Table_Name) &&
				columnName.equalsIgnoreCase(I_C_Order.COLUMNNAME_C_BPartner_Location_ID)) {
			return new IColumnCallout[]{new CheckSORoute()};
		}

		return null;
	}
	
	private static class CheckSORoute implements IColumnCallout {

		@Override
		public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value,
				Object oldValue) {
			log.info("");

			// Called from C_BPartner_Location_I in C_Order
			// to fill automatically the route based on the location
			
			if (value == null) 
				return "";
			
			int C_BPartner_Location_ID = ((Integer) value).intValue();

			MBPartnerLocation bPartnerLocation = new MBPartnerLocation(ctx, C_BPartner_Location_ID, null);
			MLocation location = bPartnerLocation.getLocation(true);

			final String whereClause = X_BAY_RoutePlan.COLUMNNAME_BAY_PostalText + "=? AND AD_Client_ID=?";
			X_BAY_RoutePlan routePlan = new Query(ctx, X_BAY_RoutePlan.Table_Name, whereClause, null)
					.setParameters(new Object[]{location.getPostal(), Env.getAD_Client_ID(ctx)})
					.firstOnly();
			
			if (routePlan != null) {
				X_BAY_Route route = new X_BAY_Route(ctx, routePlan.getBAY_Route_ID(),null);
				mTab.setValue("Bay_route_id", route.getBAY_Route_ID());
				log.info("Route " + route.getName() + " assigned");
			} else 
				log.info("No route found for this location, assign it manually");
			
			return "";
		}
		
	}

}
