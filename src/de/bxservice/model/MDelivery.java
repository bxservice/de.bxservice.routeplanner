package de.bxservice.model;

import java.sql.ResultSet;
import java.util.Properties;

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

	
}
