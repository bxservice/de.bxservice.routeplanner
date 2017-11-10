package de.bxservice.model;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;

public class RoutePlanner_ModelFactory implements IModelFactory {

	@Override
	public Class<?> getClass(String tableName) {
		if (MRoute.Table_Name.equals(tableName))
			return MRoute.class;
		if (X_BAY_RoutePlan.Table_Name.equals(tableName))
			return X_BAY_RoutePlan.class;
		if (MDelivery.Table_Name.equals(tableName))
			return MDelivery.class;
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		if (MRoute.Table_Name.equals(tableName))
			return new MRoute(Env.getCtx(), Record_ID, trxName);
		if (X_BAY_RoutePlan.Table_Name.equals(tableName))
			return new X_BAY_RoutePlan(Env.getCtx(), Record_ID, trxName);
		if (MDelivery.Table_Name.equals(tableName))
			return new MDelivery(Env.getCtx(), Record_ID, trxName);

		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
		if (MRoute.Table_Name.equals(tableName))
			return new MRoute(Env.getCtx(), rs, trxName);
		if (X_BAY_RoutePlan.Table_Name.equals(tableName))
			return new X_BAY_RoutePlan(Env.getCtx(), rs, trxName);
		if (MDelivery.Table_Name.equals(tableName))
			return new MDelivery(Env.getCtx(), rs, trxName);
		
		return null;
	}

}
