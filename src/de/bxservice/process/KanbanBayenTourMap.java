package de.bxservice.process;

import java.util.List;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.util.IServerPushCallback;
import org.adempiere.webui.util.ServerPushTemplate;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocation;
import org.compiere.model.MOrder;
import org.compiere.model.MOrgInfo;
import org.compiere.model.Query;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.kanbanboard.model.MKanbanBoard;
import org.zkoss.zk.ui.Desktop;

import de.bxservice.webui.form.WTourMap;

public class KanbanBayenTourMap extends SvrProcess implements IServerPushCallback {

	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() throws Exception {
		return null;
	}
	
	@Override
	protected void postProcess(boolean success) {
		if (success) {
			Desktop desktop = AEnv.getDesktop();
			ServerPushTemplate template = new ServerPushTemplate(desktop);
			template.executeAsync(this);
		}
	}
	
	@Override
	public void updateUI() {
		int KDB_KanbanBoard_ID = Integer.parseInt(Env.getContext(getCtx(), "#KDB_KanbanBoard_ID"));
		MKanbanBoard kanbanBoard = new MKanbanBoard(getCtx(), KDB_KanbanBoard_ID, get_TrxName());

		String whereClause = " EXISTS (SELECT viewID FROM T_Selection WHERE T_Selection.AD_PInstance_ID=? "
				+ "AND (cast(T_Selection.viewID as int)) = C_Order.BAY_Route_ID) AND "
				+ kanbanBoard.getWhereClause()
				+ " AND " + Env.getContext(Env.getCtx(), "#KDB_Params");

		List<MOrder> statusOrders = new Query(Env.getCtx(), MOrder.Table_Name, whereClause.toString(), get_TrxName())
				.setParameters(getAD_PInstance_ID())
				.setOnlyActiveRecords(true)
				.list();
		
		if (statusOrders != null && !statusOrders.isEmpty()) {
			String points_str="";
			points_str+="https://graphhopper.com/maps/?";

			MOrgInfo org = MOrgInfo.get(1000000);
			MLocation orgloc = MLocation.get(org.getC_Location_ID());
			points_str+="&point="+orgloc.getMapsLocation();

			for (MOrder order : statusOrders) {
				MBPartnerLocation cloc = new MBPartnerLocation(getCtx(), order.getC_BPartner_Location_ID(), get_TrxName());
				MLocation m_loc = MLocation.get(cloc.getC_Location_ID());
				points_str+="&point="+m_loc.getMapsLocation();
			}

			points_str+="&point="+orgloc.getMapsLocation();
			points_str+= "&locale="+Env.getContext(Env.getCtx(), "#Locale");
			points_str+= "&vehicle=truck&weighting=fastest&elevation=false&use_miles=false&layer=Omniscale'";

			openForm(points_str);
		}
	}
	
	protected void openForm(String url) {
		int formID = DB.getSQLValue(get_TrxName(), "SELECT AD_Form_ID FROM AD_Form WHERE AD_Form_UU = " + WTourMap.TOURMAP_FORM_UU);
		WTourMap form  = (WTourMap) ADForm.openForm(formID);
		form.setMaximizable(true);
		form.setMaximized(true);
		AEnv.showWindow(form);
		form.setRoute(url);
	}

}