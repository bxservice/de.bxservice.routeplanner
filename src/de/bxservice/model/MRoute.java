package de.bxservice.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MRoute extends X_BAY_Route {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8249075332965223214L;
	
	public static final String UNAVAILABLE_VALUE = "0000";
	public static final String AVAILABLE_VALUE   = "0001";

	public MRoute(Properties ctx, int BAY_Route_ID, String trxName) {
		super(ctx, BAY_Route_ID, trxName);
	}
	
	public MRoute(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}
	
}
