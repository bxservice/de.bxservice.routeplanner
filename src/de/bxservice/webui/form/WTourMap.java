package de.bxservice.webui.form;

import org.adempiere.webui.panel.ADForm;
import org.zkoss.zul.Iframe;

public class WTourMap extends ADForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 185733698789895886L;
	
	public static final String TOURMAP_FORM_UU = "'3bb139cd-968b-40a9-b25e-5f549733e065'";
	
	private Iframe gmaps;

	public WTourMap() {
		super();
	}
	
	public void initForm() {
		jbInit();
	}
	
	private void jbInit() {
		gmaps = new Iframe();
		gmaps.setWidth("100%");
		gmaps.setHeight("100%");
		
		this.setClosable(true);
		this.setMaximizable(true);
		this.setWidth("100%");
		this.setHeight("100%");
		this.setBorder("normal");
	}	//	jbInit
	
	public void setRoute(String route) {
		gmaps.setSrc(route);
		this.appendChild(gmaps);
	}
}
