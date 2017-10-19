package de.bxservice.model;

import java.sql.ResultSet;

import org.adempiere.base.IModelFactory;
import org.compiere.model.PO;
import org.compiere.util.Env;

public class RoutePlanner_ModelFactory implements IModelFactory {

	@Override
	public Class<?> getClass(String tableName) {
		if (X_BAY_Route.Table_Name.equals(tableName))
			return X_BAY_Route.class;
		if (X_BAY_RoutePlan.Table_Name.equals(tableName))
			return X_BAY_RoutePlan.class;
		if (X_BAY_Delivery.Table_Name.equals(tableName))
			return X_BAY_Delivery.class;
		return null;
	}

	@Override
	public PO getPO(String tableName, int Record_ID, String trxName) {
		if (X_BAY_Route.Table_Name.equals(tableName))
			return new X_BAY_Route(Env.getCtx(), Record_ID, trxName);
		if (X_BAY_RoutePlan.Table_Name.equals(tableName))
			return new X_BAY_RoutePlan(Env.getCtx(), Record_ID, trxName);
		if (X_BAY_Delivery.Table_Name.equals(tableName))
			return new X_BAY_Delivery(Env.getCtx(), Record_ID, trxName);

		return null;
	}

	@Override
	public PO getPO(String tableName, ResultSet rs, String trxName) {
		if (X_BAY_Route.Table_Name.equals(tableName))
			return new X_BAY_Route(Env.getCtx(), rs, trxName);
		if (X_BAY_RoutePlan.Table_Name.equals(tableName))
			return new X_BAY_RoutePlan(Env.getCtx(), rs, trxName);
		if (X_BAY_Delivery.Table_Name.equals(tableName))
			return new X_BAY_Delivery(Env.getCtx(), rs, trxName);
		
		return null;
	}

}
