package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

public class LightContentWindow extends Window{	
	private static final long serialVersionUID = 253255436886852699L;	
	
	public LightContentWindow(Embedded windowTitleIcon,Label windowTitleLibel,Component contentComponent,String width){
		this.setStyleName(Reindeer.WINDOW_LIGHT);
		if(width!=null){
			this.setWidth(width);
		}
		HorizontalLayout titleLayout=new HorizontalLayout();
		titleLayout.setWidth("100%");
		titleLayout.setStyleName(UICommonElementDefination.LIGHT_PROPERTYPANEL_TITLE_STYLE);
		if(windowTitleIcon!=null){
			titleLayout.addComponent(windowTitleIcon);	
		}		
		if(windowTitleLibel!=null){
			titleLayout.addComponent(windowTitleLibel);		
			titleLayout.setExpandRatio(windowTitleLibel, 1.0F);
		}				
		this.addComponent(titleLayout);		
		HorizontalLayout divHorizontalLayout=new HorizontalLayout();
		divHorizontalLayout.setHeight("20px");
		this.addComponent(divHorizontalLayout);		
		this.addComponent(contentComponent);	
	}
}