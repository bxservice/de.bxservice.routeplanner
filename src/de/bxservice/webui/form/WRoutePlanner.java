package de.bxservice.webui.form;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.adempiere.webui.LayoutUtils;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.editor.WDateEditor;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.theme.ThemeManager;
import org.compiere.model.MResource;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
import de.bxservice.model.BXSTransportationResource;
import de.bxservice.model.MRoute;

public class WRoutePlanner extends RoutePlanner 
	implements IFormController, EventListener<Event>, ValueChangeListener {

	private static final String RESOURCE_TYPE = "RESOURCE_TYPE";
	private static final String TRUCK_CELL    = "TRUCK_CELL";
	private static final String DRIVER_CELL   = "DRIVER_CELL";
	private static final String CODRIVER_CELL = "CODRIVER_CELL";
	
	private CustomForm mainForm = new CustomForm();

	private Borderlayout mainLayout	= new Borderlayout();

	private Panel parameterPanel = new Panel();
	private Grid gridLayout = GridFactory.newGridLayout();
	private Label lDate = new Label();
	private WDateEditor dateField;
	private Button bRefresh = new Button();
	private Hbox northPanelHbox;

	private Map<Column, MRoute> mapColumnRoute = new HashMap<>();
	private Map<Cell, BXSTransportationResource> mapCellCards = new HashMap<>();
	private Map<Cell, MRoute> mapEmptyCellField = new HashMap<>();

	private Grid routePanel;
	private Vlayout centerVLayout;

	//parameters
	private Timestamp routeDate = Env.getContextAsDate(Env.getCtx(), "#Date");

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
		parameterPanel.appendChild(gridLayout);
		lDate.setText(Msg.translate(Env.getCtx(), "Date"));
		lDate.setStyle("padding-right: 15px; padding-left: 15px;");
		dateField = new WDateEditor("Date", false, false, true, Msg.getMsg(Env.getCtx(), "Date"));
		dateField.addValueChangeListener(this);

		dateField.setValue(routeDate);
		dateField.getComponent().setStyle("text-align: right; padding-right: 15px;");

		bRefresh.setImage(ThemeManager.getThemeResource("images/Refresh16.png"));
		bRefresh.setTooltiptext(Msg.getMsg(Env.getCtx(), "Refresh"));
		bRefresh.addEventListener(Events.ON_CLICK, this);

		Rows rows = gridLayout.newRows();
		Row row = rows.newRow();
		northPanelHbox = new Hbox();
		northPanelHbox.appendChild(lDate.rightAlign());
		northPanelHbox.appendChild(dateField.getComponent());
		northPanelHbox.appendChild(bRefresh);

		Cell cell = new Cell();
		cell.setAlign("left");
		cell.appendChild(northPanelHbox);
		row.appendChild(cell);

		North north = new North();
		north.setSize("5%");
		LayoutUtils.addSclass("tab-editor-form-north-panel", north);
		mainLayout.appendChild(north);
		north.appendChild(parameterPanel);

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
		mapColumnRoute.clear();
		mapEmptyCellField.clear();
		mapCellCards.clear();
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

			Column  column;
			for (MRoute route : getRoutes()) {
				column = new Column();
				column.setWidth(equalWidth + "%");
				column.addEventListener(Events.ON_DOUBLE_CLICK, this);
				mapColumnRoute.put(column, route);

				columns.appendChild(column);
				column.setAlign("center");
				columns.appendChild(column);
				column.setLabel(route.getName());
				column.setStyle("cursor:hand; cursor:pointer; ");
			}
			columns.setSizable(true);
			createRows();
			routePanel.appendChild(columns);
		}
	}//createRoutePlannerPanel

	public void createRows() { 
		mapCellCards.clear();
		mapEmptyCellField.clear();
		Rows rows = routePanel.newRows();
		Row row = new Row();
		int numberOfCells = getNumberOfCards();
		int rowNo = 0;
		while (numberOfCells > 0 || rowNo < 4) {
			rowNo++;
			for (MRoute route : getRoutes()) {
				if (!hasMoreCards(route)) {
					String title = null;
					if (!route.getValue().equals(MRoute.AVAILABLE_VALUE) &&
							!route.getValue().equals(MRoute.UNAVAILABLE_VALUE)) {
						switch(rowNo) {
						case 1:
							title = "Truck";
							break;
						case 2:
							title = "Driver";
							break;
						case 3:
							title = "Co Driver";
						}						
					}
					if (title != null) 
						createEmptyCardCell(row, route, title, rowNo);
					else 
						createEmptyCell(row, route);
				} else {
					row.setStyle("background: transparent;");
					createCardCell(row, getRecord(route));
					numberOfCells--;
				}		
			}
			rows.appendChild(row);
			row=new Row();
		}
	}//createRows

	private void createEmptyCell(Row row, MRoute column) {
		row.appendCellChild(new Space());

		//Do not drop into unavailable
		if (column.getValue().equals(MRoute.AVAILABLE_VALUE))
			setEmptyCellProps(row.getLastCell(), column);
	}

	private void createCardCell(Row row, BXSTransportationResource resource) {
		Vlayout l = createCell(resource);
		row.appendCellChild(l);
		setCellProps(row.getLastCell(), resource);
	}
	
	private void createEmptyCardCell(Row row, MRoute column, String title, int rowNo) {
		Label label = new Label(title);
		label.setStyle("display: inline;");
		row.appendCellChild(label);
		setEmptyCellProps(row.getLastCell(), column, rowNo);
	}

	private Vlayout createCell(BXSTransportationResource resource) {
		Vlayout div = new Vlayout();		
		StringBuilder divStyle = new StringBuilder();

		divStyle.append("padding-left: 4px; text-align: left; cursor:pointer;");
		if (!resource.isAvailable())
			divStyle.append("background-color: red;");

		div.setStyle(divStyle.toString());
		Label label = new Label(resource.getName());
		div.appendChild(label);
		label = new Label(resource.getDescription());
		div.appendChild(label);

		return div;
	}//CreateCell
	
	private void setEmptyCellProps(Cell cell, MRoute column, int rowNo) {
		cell.setDroppable("true");
		cell.addEventListener(Events.ON_DROP, this);
		cell.setStyle("text-align: center; border-style: inset;");
		mapEmptyCellField.put(cell, column);

		switch (rowNo) {
		case 1:
			cell.setAttribute(RESOURCE_TYPE, TRUCK_CELL);
			break;
		case 2:
			cell.setAttribute(RESOURCE_TYPE, DRIVER_CELL);
			break;
		case 3:
			cell.setAttribute(RESOURCE_TYPE, CODRIVER_CELL);
		}
	}

	private void setCellProps(Cell cell, BXSTransportationResource resource) {

		if (resource.isAvailable()) {
			cell.setDraggable("true");
			cell.setDroppable("true");
			cell.addEventListener(Events.ON_DROP, this);
		}

		cell.addEventListener(Events.ON_DOUBLE_CLICK, this);
		cell.setStyle("text-align: left;");
		cell.setStyle("border-style: outset; ");

		mapCellCards.put(cell, resource);
	}

	private void setEmptyCellProps(Cell lastCell, MRoute column) {
		lastCell.setDroppable("true");
		lastCell.addEventListener(Events.ON_DROP, this);
		mapEmptyCellField.put(lastCell, column);
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (Events.ON_DOUBLE_CLICK.equals(e.getName())) {
			
			int recordID = 0;
			int tableID = 0;
			if (e.getTarget() instanceof Column) {
				recordID = mapColumnRoute.get(e.getTarget()).getBAY_Route_ID();
				tableID = mapColumnRoute.get(e.getTarget()).get_Table_ID();
			} else if (e.getTarget() instanceof Cell) {
				MResource resource = mapCellCards.get(e.getTarget()).getResource();
				recordID = resource.getS_Resource_ID();
				tableID = resource.get_Table_ID();
			}
			zoom(recordID, tableID);
		} else if (Events.ON_CLICK.equals(e.getName()) && e.getTarget() instanceof Button) {
			refresh();
		} else if (e instanceof DropEvent ) {
			DropEvent me = (DropEvent) e;
			Cell startItem = null;

			if (me.getDragged() instanceof Cell) {
				startItem = (Cell) me.getDragged();
			} 

			Cell endItem = null;
			if (me.getTarget() instanceof Cell) {
				endItem = (Cell) me.getTarget();
				
				System.out.println(endItem.getAttribute(RESOURCE_TYPE));

				/*BXSDriver selectedDriver = mapCellCards.get(startItem);
				BXSPlannerColumn startColumn = selectedDriver.getAsociatedColumn();
				BXSDriver endField = mapCellCards.get(endItem);
				BXSPlannerColumn endColumn;

				if (endField == null) {
					// check empty cells
					endColumn = mapEmptyCellField.get(me.getTarget());
				} else
					endColumn = endField.getAsociatedColumn();

				if (!swapCard(startColumn, endColumn, selectedDriver));
				//Messagebox.show(Msg.getMsg(Env.getCtx(), MKanbanCard.KDB_ErrorMessage));
				else {
					refresh();
				}*/
			}
		}

	}

	@Override
	public void valueChange(ValueChangeEvent e) {
		if (e.getSource() == dateField) {
			routeDate = (Timestamp) dateField.getValue();
			System.out.println(routeDate);
		}
	}

	@Override
	public ADForm getForm() {
		return mainForm;
	}

	private void zoom(int recordId, int ad_table_id) {
		AEnv.zoom(ad_table_id, recordId);
	}

	private void refresh() {
		refreshBoard();
		repaintGrid();
	}

	private void repaintGrid(){
		centerVLayout.removeChild(routePanel);
		if (routePanel.getRows() != null)
			routePanel.removeChild(routePanel.getRows());
		createRoutePlannerPanel();
		centerVLayout.appendChild(routePanel);
	}


}
