package de.bxservice.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.compiere.model.I_S_ResourceUnAvailable;
import org.compiere.model.MResource;
import org.compiere.model.MResourceUnAvailable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

public class BXSTransportationResource implements ITransportationResource {

	private static final int S_ResourceType_Driver_ID = 1000000;
	private static final int S_ResourceType_Truck_ID  = 1000001;

	protected MResource resource = null;
	private   MDelivery delivery;
	private   int resourceTypeID = 0;

	public BXSTransportationResource(MResource resource) {
		this.resource = Objects.requireNonNull(resource, "Resource must not be null");;
	}

	public MResource getResource() {
		return resource;
	}

	public boolean isTruck() {
		return resourceTypeID == S_ResourceType_Truck_ID;
	}

	public boolean isAvailable() {
		return !MResourceUnAvailable.isUnAvailable(resource, Env.getContextAsDate(Env.getCtx(), "#Date"));
	}

	public String getUnavailabilityReason() {
		Timestamp date = TimeUtil.trunc(Env.getContextAsDate(Env.getCtx(), "#Date"), TimeUtil.TRUNC_DAY);

		final String whereClause = MResource.COLUMNNAME_S_Resource_ID+"=? AND AD_Client_ID=?"
				+" AND TRUNC("+I_S_ResourceUnAvailable.COLUMNNAME_DateFrom+") <= ?"
				+" AND TRUNC("+I_S_ResourceUnAvailable.COLUMNNAME_DateTo+") >= ?";

		MResourceUnAvailable ru = new Query(Env.getCtx(), I_S_ResourceUnAvailable.Table_Name, whereClause, null)
				.setParameters(resource.getS_Resource_ID(), resource.getAD_Client_ID(), date, date)
				.first();

		return ru.getDescription();
	}

	public String getName() {
		return resource.getName();
	}

	public String getDescription() {
		if (isAvailable())
			return resource.getDescription();
		else
			return getUnavailabilityReason();
	}

	public MDelivery getDelivery() {

		if (delivery == null && resource.isAvailable()) {
			//Timestamp date = TimeUtil.trunc(Env.getContextAsDate(Env.getCtx(), "#Date"), TimeUtil.TRUNC_DAY);

			StringBuilder whereClause = new StringBuilder("AD_Client_ID IN (0, ?) AND ");
			Object[] parameters;
			
			if (isTruck()) {
				whereClause.append(MDelivery.COLUMNNAME_BAY_Truck_ID + " = ?");
				parameters = new Object[]{Env.getAD_Client_ID(Env.getCtx()), resource.getS_Resource_ID()};
			} else {
				whereClause.append("(" + X_BAY_Delivery.COLUMNNAME_BAY_Driver_ID + " = ? OR " +
						X_BAY_Delivery.COLUMNNAME_BAY_CoDriver_ID + " = ?) ");
				parameters = new Object[]{Env.getAD_Client_ID(Env.getCtx()), resource.getS_Resource_ID(), resource.getS_Resource_ID()};
			}

			delivery = new Query(Env.getCtx(), MDelivery.Table_Name, whereClause.toString(), null)
					.setParameters(parameters)
					.setOrderBy(X_BAY_Delivery.COLUMNNAME_BAY_RouteDate + " DESC")
					.setOnlyActiveRecords(true)
					.first();
		}
		
		return delivery;
	}

	public static List<BXSTransportationResource> getTResources() {

		List<BXSTransportationResource> transportResources = new ArrayList<>();

		List<MResource> resources = new Query(Env.getCtx(), MResource.Table_Name, " AD_Client_ID IN (0, ?) "
									+ "AND S_ResourceType_ID IN (?,?)", null)
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), S_ResourceType_Driver_ID, S_ResourceType_Truck_ID})
				.setOnlyActiveRecords(true)
				.setOrderBy(MResource.COLUMNNAME_S_ResourceType_ID)
				.list();

		for (MResource res : resources)
			transportResources.add(new BXSTransportationResource(res));

		return transportResources;
	}

}
