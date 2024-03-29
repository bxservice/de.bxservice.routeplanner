package de.bxservice.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.compiere.model.MResourceUnAvailable;
import org.compiere.model.Query;
import org.compiere.model.X_C_NonBusinessDay;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

import de.bxservice.model.MDelivery;

public class RoutePlanner_CopyDelivery extends SvrProcess {

	private Timestamp targetDay;
	
	private Timestamp p_TargetDate;
	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("BAY_RouteDate"))
				p_TargetDate = para[i].getParameterAsTimestamp();
		}
	}

	@Override
	protected String doIt() throws Exception {
		log.warning("Copying last date deliveries");
		
		//Check if working day - not weekends nor holidays
		if (p_TargetDate == null)
			p_TargetDate = Env.getContextAsDate(getCtx(), "#Date");

		targetDay = TimeUtil.trunc(p_TargetDate, TimeUtil.TRUNC_DAY);
			
		X_C_NonBusinessDay nonWorkingDays = new Query(Env.getCtx(), X_C_NonBusinessDay.Table_Name, 
				"AD_Client_ID IN (0, ?) AND TRUNC(" + 
						X_C_NonBusinessDay.COLUMNNAME_Date1 + ")=? ", get_TrxName())
				.setParameters(new Object[]{Env.getAD_Client_ID(Env.getCtx()), targetDay})
				.setOnlyActiveRecords(true)
				.first();
		
		if (nonWorkingDays != null)
			return "No deliveries for non-working days: " + targetDay;
		
		if (targetDay.equals(TimeUtil.trunc(MDelivery.getLastDeliveryDate(get_TrxName()), TimeUtil.TRUNC_DAY)))
			return "Delivery records already created for " + targetDay;

		List<MDelivery> lastDeliveries = MDelivery.getLastDateDeliveries();
		
		if (lastDeliveries == null || lastDeliveries.isEmpty())
			return "Non existing deliveries to copy";
		
		int counter = 0;
		for (MDelivery delivery : lastDeliveries) {
			if (delivery.getBAY_Route().isBAY_isExtraordinary())
				continue;
			counter ++;
			StringBuilder message = new StringBuilder("Delivery created");
			MDelivery newDelivery = MDelivery.copyDelivery(delivery, get_TrxName());
			newDelivery.setBAY_RouteDate(targetDay);
			if (!checkResourceAvailability(newDelivery))
				message.append(" with unavailable resources. Please check");
			newDelivery.saveEx(get_TrxName());
			addLog(getProcessInfo().getAD_Process_ID(), 
					targetDay, new BigDecimal(getProcessInfo().getAD_PInstance_ID()), 
					message.toString(), MDelivery.Table_ID, 
					newDelivery.getBAY_Delivery_ID());
		}
		
		return counter + " delivery records created";
	}
	
	private boolean checkResourceAvailability(MDelivery delivery) {
		
		boolean allResourcesAvailable = true;
		
		if (delivery.getBAY_Driver() != null && MResourceUnAvailable.isUnAvailable(delivery.getBAY_Driver(), targetDay)) {
			delivery.setBAY_Driver_ID(0);
			allResourcesAvailable = false;
		}
		
		if (delivery.getBAY_CoDriver() != null && MResourceUnAvailable.isUnAvailable(delivery.getBAY_CoDriver(), targetDay)) {
			delivery.setBAY_CoDriver_ID(0);
			allResourcesAvailable = false;
		}
		
		if (delivery.getBAY_Truck() != null && MResourceUnAvailable.isUnAvailable(delivery.getBAY_Truck(), targetDay)) {
			delivery.setBAY_Truck_ID(0);
			allResourcesAvailable = false;
		}
		
		return allResourcesAvailable;
	}
	
}
