package de.bxservice.webui.form;

import java.util.HashMap;
import java.util.Map;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.compiere.model.MResource;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Space;
import org.zkoss.zul.Vlayout;

import de.bxservice.form.RoutePlanner;
import de.bxservice.model.BXSDriver;
import de.bxservice.model.BXSPlannerColumn;
import de.bxservice.model.BXSTruck;

public class WRoutePlanner extends RoutePlanner implements IFormController, EventListener<Event> {

	private CustomForm mainForm = new CustomForm();

	private Borderlayout mainLayout	= new Borderlayout();

	private Panel panel = new Panel();
	private Grid gridLayout = GridFactory.newGridLayout();
	private Label lProcess = new Label();
	private Hbox northPanelHbox;

	private Map<Column, BXSPlannerColumn> mapColumnTruck = new HashMap<Column, BXSPlannerColumn>();
	private Map<Cell, BXSDriver> mapCellDrivers = new HashMap<Cell, BXSDriver>();
	private Map<Cell, BXSPlannerColumn> mapEmptyCellField = new HashMap<Cell, BXSPlannerColumn>();

	private Grid routePanel;
	private Vlayout centerVLayout;

	public WRoutePlanner() {
		super();
		initForm();
	}

	public void initForm() {
		jbInit();
		LayoutUtils.sendDeferLayoutEvent(mainLayout, 100);
	}

	private void jbInit() {
		mainForm.setSizable(true);
		mainForm.setClosable(true);
		mainForm.setMaximizable(true);
		mainForm.setWidth("95%");
		mainForm.setHeight("95%");
		mainForm.appendChild(mainLayout);
		mainForm.setBorder("normal");

		//North Panel
		panel.appendChild(gridLayout);
		lProcess.setText(Msg.translate(Env.getCtx(), "Process"));

		Rows rows = gridLayout.newRows();
		Row row = rows.newRow();
		northPanelHbox = new Hbox();
		northPanelHbox.appendChild(lProcess.rightAlign());

		Cell cell = new Cell();
		cell.setAlign("left");
		cell.appendChild(northPanelHbox);
		row.appendChild(cell);

		North north = new North();
		north.setSize("5%");
		LayoutUtils.addSclass("tab-editor-form-north-panel", north);
		mainLayout.appendChild(north);
		north.appendChild(panel);


		//CenterPanel
		createRoutePlannerPanel();
		centerVLayout = new Vlayout();
		centerVLayout.setHeight("100%");
		centerVLayout.appendChild(routePanel);
		centerVLayout.setStyle("overflow:auto");

		South south = new South();
		LayoutUtils.addSclass("tab-editor-form-center-panel", south);
		south.setSize("95%");
		south.appendChild(centerVLayout);

		mainLayout.appendChild(south);
	}	//	jbInit

	public void createRoutePlannerPanel() {
		mapColumnTruck.clear();
		mapEmptyCellField.clear();
		mapCellDrivers.clear();
		routePanel = new Grid();
		routePanel.makeNoStrip();
		routePanel.setVflex(true);
		routePanel.setSizedByContent(true);
		routePanel.setSpan("true");

		int numCols=0;
		setBoardContent();
		numCols = getNumberOfColumns();

		if (numCols > 0) {
			// set size in percentage per column leaving a MARGIN on right
			Columns columns = new Columns();
			columns.setMenupopup("auto");

			int equalWidth = 100 ;
			Auxhead auxhead = new Auxhead();

			Auxheader auxheader;
			Column  column;
			for (BXSPlannerColumn plannerColumn : getColumns()) {
				column = new Column();
				column.setWidth(equalWidth + "%");
				column.addEventListener(Events.ON_DOUBLE_CLICK, this);
				mapColumnTruck.put(column, plannerColumn);

				columns.appendChild(column);
				column.setAlign("center");
				columns.appendChild(column);
				column.setLabel(plannerColumn.getName());

				auxheader = new Auxheader();
				BXSTruck truck = plannerColumn.getTruck();
				if (truck != null) {
					if (!truck.isAvailable()) {
						column.setStyle("background-color: red; color:white; cursor:hand; cursor:pointer;");
						auxheader.setLabel(truck.getUnavailabilityReason());
						auxheader.setAlign("center");
						auxheader.setStyle("background-color: red; color:white;");
					} else {
						auxheader.setLabel(truck.getDescription());
						auxheader.setAlign("center");
						column.setStyle("cursor:hand; cursor:pointer; ");
					}
				}
				auxhead.appendChild(auxheader);
			}
			columns.setSizable(true);
			createRows();
			routePanel.appendChild(columns);
			routePanel.appendChild(auxhead);
		}
	}//createRoutePlannerPanel

	public void createRows() { 
		mapCellDrivers.clear();
		mapEmptyCellField.clear();
		Rows rows = routePanel.newRows();
		Row row = new Row();
		int numberOfDrivers = getNumberOfDrivers();
		while (numberOfDrivers > 0) {
			for (BXSPlannerColumn column : getColumns()) {
				if (!column.hasMoreRecords()) {
					createEmptyCell(row, column);
				} else {
					row.setStyle("background: transparent;");
					createCardCell(row, column.getRecord());
					numberOfDrivers--;
				}				
			}
			rows.appendChild(row);
			row=new Row();
		}
	}//createRows

	private void createEmptyCell(Row row, BXSPlannerColumn column) {
		row.appendCellChild(new Space());

		//Do not drop into unavailable
		if (column.getTruck() != null && column.getTruck().isAvailable() || column.isAvailable())
			setEmptyCellProps(row.getLastCell(), column);
	}

	private void createCardCell(Row row, BXSDriver driver) {
		Vlayout l = createCell(driver);
		row.appendCellChild(l);
		setCellProps(row.getLastCell(), driver);
	}

	private Vlayout createCell(BXSDriver driver) {
		Vlayout div = new Vlayout();		
		StringBuilder divStyle = new StringBuilder();

		divStyle.append("text-align: left; cursor:hand; cursor:pointer;");
		div.setStyle(divStyle.toString());
		Label label = new Label(driver.getName());
		div.appendChild(label);
		label = new Label(driver.getDescription());
		div.appendChild(label);

		return div;
	}//CreateCell

	private void setCellProps(Cell cell, BXSDriver driver) {
		cell.setDraggable("true");
		cell.setDroppable("true");
		cell.addEventListener(Events.ON_DROP, this);
		cell.addEventListener(Events.ON_CLICK, this);
		cell.addEventListener(Events.ON_DOUBLE_CLICK, this);
		cell.addEventListener(Events.ON_RIGHT_CLICK, this);
		cell.setStyle("text-align: left;");
		cell.setStyle("border-style: outset; ");
		mapCellDrivers.put(cell, driver);
	}

	private void setEmptyCellProps(Cell lastCell, BXSPlannerColumn column) {
		lastCell.setDroppable("true");
		lastCell.addEventListener(Events.ON_DROP, this);
		mapEmptyCellField.put(lastCell, column);
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (Events.ON_DOUBLE_CLICK.equals(e.getName())) {
			MResource resource = null;
			if (e.getTarget() instanceof Column) {
				resource = mapColumnTruck.get(e.getTarget()).getTruck().getResource();
			} else if (e.getTarget() instanceof Cell) {
				resource = mapCellDrivers.get(e.getTarget()).getResource();
			}
			zoom(resource.getS_Resource_ID(), resource.get_Table_ID());
		} else if (e instanceof DropEvent ) {
			DropEvent me = (DropEvent) e;
			Cell startItem = null;

			if (me.getDragged() instanceof Cell) {
				startItem = (Cell) me.getDragged();
			} 

			Cell endItem = null;
			if (me.getTarget() instanceof Cell) {
				endItem = (Cell) me.getTarget();

				BXSDriver selectedDriver = mapCellDrivers.get(startItem);
				BXSPlannerColumn startColumn = selectedDriver.getAsociatedColumn();
				BXSDriver endField = mapCellDrivers.get(endItem);
				BXSPlannerColumn endColumn;

				if (endField == null) {
					// check empty cells
					endColumn = mapEmptyCellField.get(me.getTarget());
				} else
					endColumn = endField.getAsociatedColumn();

				if (!swapCard(startColumn, endColumn, selectedDriver));
					//Messagebox.show(Msg.getMsg(Env.getCtx(), MKanbanCard.KDB_ErrorMessage));
				else {
					refreshBoard();
					repaintGrid();
				}
			}
		}

	}

	@Override
	public ADForm getForm() {
		return mainForm;
	}

	private void zoom(int recordId, int ad_table_id) {
		AEnv.zoom(ad_table_id, recordId);
	}
	
	private void repaintGrid(){
		centerVLayout.removeChild(routePanel);
		if (routePanel.getRows() != null)
			routePanel.removeChild(routePanel.getRows());
		createRoutePlannerPanel();
		centerVLayout.appendChild(routePanel);
	}

}
