package com.viewfunction.vfbpm.adminCenter.UI.activityManagement;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityManagementPanel  extends VerticalLayout{
	
	private static final long serialVersionUID = -475733343795856019L;
	public ActivityObjectBrowser activityObjectBrowser;
	public ActivityObjectDetail activityObjectDetail;
	public ActivityManagementPanel(UserClientInfo userClientInfo){
		HorizontalSplitPanel horiz = new HorizontalSplitPanel();
        horiz.setSplitPosition(300,Sizeable.UNITS_PIXELS);         
        horiz.setStyleName(Reindeer.SPLITPANEL_SMALL);        
        this.setSizeFull();        
        addComponent(horiz);
        
        // left component:
        this.activityObjectBrowser=new ActivityObjectBrowser(userClientInfo);        
        horiz.addComponent(this.activityObjectBrowser);
        this.activityObjectBrowser.setActivityManagementPanel(this);
        // right component:        
        this.activityObjectDetail=new ActivityObjectDetail(userClientInfo);
        horiz.addComponent(this.activityObjectDetail);
        this.activityObjectDetail.setActivityManagementPanel(this);
	}
}