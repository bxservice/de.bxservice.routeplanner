package de.bxservice.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.compiere.model.I_S_ResourceUnAvailable;
import org.compiere.model.MResource;
import org.compiere.model.MResourceType;
import org.compiere.model.MResourceUnAvailable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

public class BXSTransportationResource implements ITransportationResource {

	private static final String S_ResourceType_Driver_VALUE = "Fahrer";
	private static final String S_ResourceType_Truck_VALUE  = "LKW";

	protected MRoute    route = null;
	protected MResource resource = null;
	private   MDelivery delivery;

	public BXSTransportationResource(MResource resource) {
		this.resource = Objects.requireNonNull(resource, "Resource must not be null");;
	}

	public MResource getResource() {
		return resource;
	}
	
	public int getResource_ID() {
		return resource.getS_Resource_ID();
	}
	
	public int getResourceType_ID() {
		return resource.getS_ResourceType_ID();
	}

	public boolean isTruck() {
		return MResourceType.get(Env.getCtx(), resource.getS_ResourceType_ID()).getValue().equals(S_ResourceType_Truck_VALUE);
	}
	
	public boolean isCoDriver() {
		if (delivery != null) {
			return delivery.getBAY_CoDriver_ID() == getResource_ID();
		}
			
		return false;
	}
	
	public boolean isCoDriver2() {
		if (delivery != null) {
			return delivery.getBAY_CoDriver2_ID() == getResource_ID();
		}
			
		return false;
	}
	
	public boolean isDriver() {
		if (delivery != null) {
			return delivery.getBAY_Driver_ID() == getResource_ID();
		}
			
		return false;
	}

	public boolean isAvailable(Timestamp date) {
		if (date == null)
			date = Env.getContextAsDate(Env.getCtx(), "#Date");
		return !MResourceUnAvailable.isUnAvailable(resource, date);
	}

	public String getUnavailabilityReason(Timestamp date) {
		if (date == null)
			date = TimeUtil.trunc(Env.getContextAsDate(Env.getCtx(), "#Date"), TimeUtil.TRUNC_DAY);

		final String whereClause = MResource.COLUMNNAME_S_Resource_ID+"=? AND AD_Client_ID=?"
				+" AND TRUNC("+I_S_ResourceUnAvailable.COLUMNNAME_DateFrom+") <= ?"
				+" AND TRUNC("+I_S_ResourceUnAvailable.COLUMNNAME_DateTo+") >= ?";

		MResourceUnAvailable ru = new Query(Env.getCtx(), I_S_ResourceUnAvailable.Table_Name, whereClause, null)
				.setParameters(resource.getS_Resource_ID(), resource.getAD_Client_ID(), date, date)
				.first();

		return ru != null ? ru.getDescription() : "";
	}

	public String getName() {
		return resource.getName();
	}

	public String getDescription() {
		return resource.getDescription();
	}

	public MRoute getRoute() {
		return route;
	}

	public void setRoute(MRoute route) {
		this.route = route;
	}

	public MDelivery getDelivery(Timestamp routeDate, boolean isExtraordinary) {

		if (delivery == null && resource.isAvailable()) {

			Timestamp date = TimeUtil.trunc(routeDate, TimeUtil.TRUNC_DAY);
			
			StringBuilder whereClause = new StringBuilder("AD_Client_ID IN (0, ?) AND TRUNC("+ MDelivery.COLUMNNAME_BAY_RouteDate + ")=? AND ");
			whereClause.append(MDelivery.COLUMNNAME_BAY_Route_ID);
			whereClause.append(" IN (SELECT ");
			whereClause.append(MRoute.COLUMNNAME_BAY_Route_ID);
			whereClause.append(" FROM ");
			whereClause.append(MRoute.Table_Name);
			whereClause.append(" WHERE ");
			whereClause.append(MRoute.COLUMNNAME_BAY_isExtraordinary);
			whereClause.append(" =?) AND ");
			
			Object[] parameters;
			
			if (isTruck()) {
				whereClause.append(MDelivery.COLUMNNAME_BAY_Truck_ID + " = ?");
				parameters = new Object[]{Env.getAD_Client_ID(Env.getCtx()), date, isExtraordinary, resource.getS_Resource_ID()};
			} else {
				whereClause.append("(" + X_BAY_Delivery.COLUMNNAME_BAY_Driver_ID + " = ? OR " +
						X_BAY_Delivery.COLUMNNAME_BAY_CoDriver_ID + " = ? OR " +  
						X_BAY_Delivery.COLUMNNAME_BAY_CoDriver2_ID + " = ?) ");
				parameters = new Object[]{Env.getAD_Client_ID(Env.getCtx()), date, isExtraordinary, resource.getS_Resource_ID(), resource.getS_Resource_ID(), resource.getS_Resource_ID()};
			}

			delivery = new Query(Env.getCtx(), MDelivery.Table_Name, whereClause.toString(), null)
					.setParameters(parameters)
					.setOrderBy(X_BAY_Delivery.COLUMNNAME_BAY_RouteDate + " DESC")
					.setOnlyActiveRecords(true)
					.first();
		}
		
		return delivery;
	}

	public void setDelivery(MDelivery delivery) {
		this.delivery = delivery;
	}

	public static List<BXSTransportationResource> getTResources() {

		List<BXSTransportationResource> transportResources = new ArrayList<>();
		
		StringBuilder whereClause = new StringBuilder(" AD_Client_ID IN (0, ?) AND ");
		whereClause.append(MResource.COLUMNNAME_S_ResourceType_ID);
		whereClause.append(" IN (SELECT t.");
		whereClause.append(MResourceType.COLUMNNAME_S_ResourceType_ID);
		whereClause.append(" FROM ");
		whereClause.append(MResourceType.Table_Name);
		whereClause.append(" t WHERE t.");
		whereClause.append(MResourceType.COLUMNNAME_Value);
		whereClause.append(" IN (?,?))");
		
		List<MResource> resources = new Query(Env.getCtx(), MResource.Table_Name, whereClause.toString(), null)
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), S_ResourceType_Driver_VALUE, S_ResourceType_Truck_VALUE})
				.setOnlyActiveRecords(true)
				.setOrderBy(MResource.COLUMNNAME_S_ResourceType_ID)
				.list();

		for (MResource res : resources)
			transportResources.add(new BXSTransportationResource(res));

		return transportResources;
	}

}
