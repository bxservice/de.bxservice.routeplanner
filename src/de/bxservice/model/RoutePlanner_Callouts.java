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
import org.compiere.util.DB;
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
			//Don't override the defined default in Tab
			if (value == null || (mTab.getField("BAY_Route_ID") == null ||
					mTab.getField("BAY_Route_ID").getDefault() != null)) 
				return "";
			
			int C_BPartner_Location_ID = ((Integer) value).intValue();

			MBPartnerLocation bPartnerLocation = new MBPartnerLocation(ctx, C_BPartner_Location_ID, null);
			int bpartnerRouteId = 0;
			
			if (bPartnerLocation.get_Value("BAY_Route_ID") != null) {
				log.info("Default route for BPartner assigned");
				bpartnerRouteId = (int) bPartnerLocation.get_Value("BAY_Route_ID");
			} else {
				MLocation location = bPartnerLocation.getLocation(true);

				//Return the closest date in the same week that a route will be delivered to the postal code
				String whereClause = X_BAY_RoutePlan.COLUMNNAME_BAY_PostalText + "=? AND AD_Client_ID=? AND weekday::integer >= EXTRACT(ISODOW from ?::date)";
				X_BAY_RoutePlan routePlan = new Query(ctx, X_BAY_RoutePlan.Table_Name, whereClause, null)
						.setParameters(new Object[]{location.getPostal(), Env.getAD_Client_ID(ctx), DB.TO_STRING(mTab.getField("DateOrdered").getValue().toString())})
						.setOrderBy(X_BAY_RoutePlan.COLUMNNAME_WeekDay)
						.first();
				
				if (routePlan != null) {
					X_BAY_Route route = new X_BAY_Route(ctx, routePlan.getBAY_Route_ID(),null);
					bpartnerRouteId = route.getBAY_Route_ID();
				} else {
					log.info("No route is going to this address in the rest of the week");

					whereClause = X_BAY_RoutePlan.COLUMNNAME_BAY_PostalText + "=? AND AD_Client_ID=? ";
					routePlan = new Query(ctx, X_BAY_RoutePlan.Table_Name, whereClause, null)
							.setParameters(new Object[]{location.getPostal(), Env.getAD_Client_ID(ctx)})
							.setOrderBy(X_BAY_RoutePlan.COLUMNNAME_WeekDay)
							.first();
					if (routePlan != null) {
						X_BAY_Route route = new X_BAY_Route(ctx, routePlan.getBAY_Route_ID(),null);
						bpartnerRouteId = route.getBAY_Route_ID();
					}
				}
			}
			
			if (bpartnerRouteId != 0) {
				mTab.setValue("Bay_route_id", bpartnerRouteId);
				log.info("Route " + bpartnerRouteId + " assigned");
			} else 
				log.info("No route found for this location, assign it manually");				
			
			return "";
		}
		
	}

}
