package de.bxservice.process;

import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.util.IServerPushCallback;
import org.adempiere.webui.util.ServerPushTemplate;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.kanbanboard.model.MKanbanBoard;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Window.Mode;

import de.bxservice.georeferencing.model.MBXSGeoreferencing;
import de.bxservice.georeferencing.webui.form.WGeoreferencingMap;

/**
 * Class made to create a map showing the delivery points in all the routes 
 * @author Diego Ruiz - BX Service GmbH
 */
public class KanbanBayenTourBoardMap extends SvrProcess implements IServerPushCallback {

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

		StringBuilder whereClause = new StringBuilder();
		if (!Util.isEmpty(kanbanBoard.getWhereClause()))
			whereClause.append(kanbanBoard.getWhereClause());
		if (!Util.isEmpty(Env.getContext(Env.getCtx(), "#KDB_Params"))) {
			if (whereClause.length() != 0)
				whereClause.append(" AND ");
			whereClause.append(Env.getContext(Env.getCtx(), "#KDB_Params"));
		}
		
		int BXS_GeoReferencing_ID = DB.getSQLValue(get_TrxName(), "SELECT BXS_Georeferencing_ID FROM BXS_Georeferencing WHERE BXS_Georeferencing_UU = " + "'d62fbf6b-a36a-4f64-9a83-2f5e27317bb4'");
		if (BXS_GeoReferencing_ID > 0) {
			MBXSGeoreferencing geoConfig = new MBXSGeoreferencing(getCtx(), BXS_GeoReferencing_ID, get_TrxName());
			geoConfig.setAdditionalWhereClause(whereClause.toString());
			openForm(geoConfig);
		}

	}
	
	protected void openForm(MBXSGeoreferencing geoConfig) {
		WGeoreferencingMap form  = new WGeoreferencingMap();
		form.getForm().setMaximizable(true);
		form.getForm().setMaximized(true);
		form.getForm().setTitle(geoConfig.get_Translation(MBXSGeoreferencing.COLUMNNAME_Name));
		form.getForm().setAttribute(Window.MODE_KEY, Mode.EMBEDDED);
		AEnv.showWindow(form.getForm());
		form.setContent(geoConfig);
	}

}