package de.bxservice.webui.form;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

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
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.event.ValueChangeEvent;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.grid.WQuickEntry;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.theme.ThemeManager;
import org.compiere.model.MImage;
import org.compiere.model.MResource;
import org.compiere.model.MUser;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
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
	public static final String TRUCK_CELL    = "TRUCK_CELL";
	public static final String DRIVER_CELL   = "DRIVER_CELL";
	public static final String CODRIVER_CELL = "CODRIVER_CELL";
	public static final String CARD_CELL     = "CARD_CELL";
	private int windowNo = 0;

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
		windowNo = SessionManager.getAppDesktop().registerWindow(this);
		initForm();
	}

	public void initForm() {
		jbInit();
		LayoutUtils.sendDeferLayoutEvent(mainLayout, 100);
	}

	private void jbInit() {
		mainForm.setClosable(true);
		mainForm.setMaximizable(true);
		mainForm.setWidth("100%");
		mainForm.setHeight("100%");
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
		routePanel.setSpan("true");

		int numCols=0;
		setBoardContent(routeDate);
		numCols = getNumberOfColumns();

		if (numCols > 0) {
			// set size in percentage per column leaving a MARGIN on right
			Columns columns = new Columns();
			columns.setMenupopup("auto");

			Column  column;
			for (MRoute route : getRoutes()) {
				column = new Column();
				column.setHflex("1");
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
					createEmptyCell(row, route, rowNo);
				} else {
					row.setStyle("background: transparent;");
					BXSTransportationResource record = null;
					if (route.getValue().equals(MRoute.AVAILABLE_VALUE) ||
							route.getValue().equals(MRoute.UNAVAILABLE_VALUE))
						record = getRecord(route);
					else {
						switch(rowNo) {
						case 1:
							record = getTruck(route);
							break;
						case 2:
							record = getDriver(route);
							break;
						case 3:
							record = getCoDriver(route);
						}
					}
					if (record != null) {
						createCardCell(row, record);
						removeRecord(record);
						numberOfCells--;
					} else
						createEmptyCell(row, route, rowNo);
				}
			}
			rows.appendChild(row);
			row=new Row();
			row.setHeight("100px");
		}
	}//createRows

	private void createEmptyCell(Row row, MRoute route, int rowNo) {
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
			createEmptyBlankCell(row, route);
	}

	private void createEmptyBlankCell(Row row, MRoute column) {
		row.appendCellChild(new Space());

		//Do not drop into a route if not the designated cells
		if (column.getValue().equals(MRoute.AVAILABLE_VALUE) ||
				column.getValue().equals(MRoute.UNAVAILABLE_VALUE))
			setEmptyCellProps(row.getLastCell(), column);
	}

	private void createCardCell(Row row, BXSTransportationResource resource) {
		Hlayout l = createCell(resource);
		row.appendCellChild(l);
		setCellProps(row.getLastCell(), resource);
	}

	private void createEmptyCardCell(Row row, MRoute column, String title, int rowNo) {
		Label label = new Label(title);
		label.setStyle("display: inline;");
		row.appendCellChild(label);
		setEmptyCellProps(row.getLastCell(), column, rowNo);
	}

	private Hlayout createCell(BXSTransportationResource resource) {
		Hlayout div = new Hlayout();
		Vlayout vlayout = new Vlayout();		
		StringBuilder divStyle = new StringBuilder();

		divStyle.append("padding-left: 4px; text-align: left; cursor:pointer;");
		if (!resource.isAvailable())
			divStyle.append("background-color: red;");

		div.setStyle(divStyle.toString());
		
		//Left side - labels
		Label label = new Label(resource.getName());
		vlayout.appendChild(label);
		label.setVflex("1");
		label = new Label(resource.getDescription());
		label.setVflex("1");
		vlayout.appendChild(label);
		vlayout.setHflex("1");
		vlayout.setVflex("1");

		div.appendChild(vlayout);
		
		// Right panel - image
		Image imageDiv = new Image();
		String imageName = null;
		
		if (resource.isTruck()) {
			imageName = "delivery-truck.png";
		} else if (resource.getResource().getAD_User() != null) {
			MUser user = (MUser) resource.getResource().getAD_User();
			MImage userImage = MImage.get(Env.getCtx(), user.getAD_Image_ID());
			if (userImage != null && userImage.getData() != null) {
				AImage aImage;
				try {
					aImage = new AImage(userImage.getName(), userImage.getData());
					imageDiv.setContent(aImage);
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		}
		
		if (imageDiv.getContent() == null) {
			try {
				if (imageName == null)
					imageName = "delivery-man.png";

				URL imageURL = getClass().getClassLoader().getResource("images/" + imageName);
				BufferedImage originalImage = ImageIO.read(imageURL);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(originalImage, "png", baos);
				AImage aImage = new AImage(imageName, baos.toByteArray());
				imageDiv.setContent(aImage);
			} catch (IOException e) {
				e.printStackTrace();
			}			
			//imageDiv.setSrc(getClass().getClassLoader().getResource("images/delivery-man.png").toString());
		}
		imageDiv.setWidth("75px");
		imageDiv.setHeight("75px");

		div.appendChild(imageDiv);
		
		return div;
	} //CreateCell

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
		cell.setAttribute(RESOURCE_TYPE, CARD_CELL);

		mapCellCards.put(cell, resource);
	}

	private void setEmptyCellProps(Cell lastCell, MRoute column) {
		lastCell.setDroppable("true");
		lastCell.setAttribute(RESOURCE_TYPE, CARD_CELL);
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

				BXSTransportationResource startCard = mapCellCards.get(startItem);
				BXSTransportationResource endCard = mapCellCards.get(endItem);
				boolean success = false;
				
				MRoute endColumn = null;
				if (endCard == null) 
					endColumn = mapEmptyCellField.get(endItem);
				else if (endCard.getDelivery(routeDate) == null) //Dropped in available
					endColumn = endCard.getRoute();

				if (endColumn != null) {
					if (endColumn.getValue().equals(MRoute.UNAVAILABLE_VALUE))
						actionQuickEntry(startCard.getResource());
					else
						success = assignCard(startCard, endColumn, (String) endItem.getAttribute(RESOURCE_TYPE));
				}
				else
					success = swapCard(startCard, endCard);

				if (success)
					refresh();
				else if (getErrorMessage() != null && !getErrorMessage().isEmpty())
					Messagebox.show(getErrorMessage());
			}
		}
	}
	
	private void actionQuickEntry(MResource resource) {

		int Record_ID = resource.getS_Resource_ID();
		int windowId = Env.getZoomWindowID(resource.get_Table_ID(), Record_ID);
		final WQuickEntry vqe = new WQuickEntry(windowNo, windowId);
		if (vqe.getQuickFields()<=0)
			return;

		vqe.loadRecord(Record_ID);
		vqe.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				refresh();
			}
		});

		vqe.setSizable(true);
		AEnv.showWindow(vqe);
	}	//	actionQuickEntry

	@Override
	public void valueChange(ValueChangeEvent e) {
		if (e.getSource() == dateField) {
			routeDate = (Timestamp) dateField.getValue();
			refresh();
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
