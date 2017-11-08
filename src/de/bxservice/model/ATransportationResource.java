package de.bxservice.model;

import java.sql.Timestamp;

import org.compiere.model.I_S_ResourceUnAvailable;
import org.compiere.model.MResource;
import org.compiere.model.MResourceUnAvailable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

public abstract class ATransportationResource implements ITransportationResource {

	protected MResource resource = null;

	public ATransportationResource(MResource resource) {
		this.resource = resource;
	}

	public MResource getResource() {
		return resource;
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
		return resource.getDescription();
	}

}
