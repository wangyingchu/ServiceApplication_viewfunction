package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class SystemConfigurationPanel extends VerticalLayout{
	private static final long serialVersionUID = 3712891270153384450L;
	
	private ConfigOptionSelectionBrowser configOptionSelectionBrowser;
	private ConfigOptionDetailItem configOptionDetailItem;
	
	public SystemConfigurationPanel(UserClientInfo userClientInfo){
		HorizontalSplitPanel horiz = new HorizontalSplitPanel();
        horiz.setSplitPosition(300,Sizeable.UNITS_PIXELS);         
        horiz.setStyleName(Reindeer.SPLITPANEL_SMALL);        
        this.setSizeFull();        
        addComponent(horiz);        
        // left component:
        this.configOptionSelectionBrowser=new ConfigOptionSelectionBrowser(userClientInfo,this);        
        horiz.addComponent(this.configOptionSelectionBrowser);        
        // right component:        
        this.configOptionDetailItem=new ConfigOptionDetailItem(userClientInfo,this);
        horiz.addComponent(this.configOptionDetailItem);        
	}
	
	public ConfigOptionDetailItem getConfigOptionDetailItem(){
		return this.configOptionDetailItem;
	}
}