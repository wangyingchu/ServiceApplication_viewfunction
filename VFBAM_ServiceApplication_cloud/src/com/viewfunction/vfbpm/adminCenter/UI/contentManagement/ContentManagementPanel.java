package com.viewfunction.vfbpm.adminCenter.UI.contentManagement;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ContentManagementPanel extends VerticalLayout{
	private static final long serialVersionUID = 1806943829074816729L;
	
	public ContentObjectBrowser contentObjectBrowser;
	public ContentObjectDetail contentObjectDetail;
	
	public ContentManagementPanel(UserClientInfo userClientInfo){		
		HorizontalSplitPanel horiz = new HorizontalSplitPanel();
        horiz.setSplitPosition(300,Sizeable.UNITS_PIXELS);         
        horiz.setStyleName(Reindeer.SPLITPANEL_SMALL);        
        this.setSizeFull();        
        addComponent(horiz);
        
        this.contentObjectBrowser=new ContentObjectBrowser(userClientInfo);   
        this.contentObjectDetail=new ContentObjectDetail(userClientInfo);
        // left component:
        horiz.addComponent(contentObjectBrowser);       
        // right component:
        horiz.addComponent(contentObjectDetail);		
        this.contentObjectBrowser.setContentManagementPanel(this);
        this.contentObjectDetail.setContentManagementPanel(this);
	}
}