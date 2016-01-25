package com.viewfunction.vfbpm.adminCenter.UI.processManagement;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ProcessManagementPanel  extends VerticalLayout{	
	private static final long serialVersionUID = -8936834847151761406L;

	public ProcessObjectBrowser processObjectBrowser;
	public ProcessObjectDetail processObjectDetail;
	
	public ProcessManagementPanel(UserClientInfo userClientInfo){
		HorizontalSplitPanel horiz = new HorizontalSplitPanel();
        horiz.setSplitPosition(300,Sizeable.UNITS_PIXELS);         
        horiz.setStyleName(Reindeer.SPLITPANEL_SMALL);        
        this.setSizeFull();        
        addComponent(horiz);
        
        this.processObjectBrowser=new ProcessObjectBrowser(userClientInfo);   
        this.processObjectDetail=new ProcessObjectDetail(userClientInfo);
        // left component:
        horiz.addComponent(processObjectBrowser);       
        // right component:
        horiz.addComponent(processObjectDetail);		
        this.processObjectBrowser.setProcessManagementPanel(this);
        this.processObjectDetail.setProcessManagementPanel(this);
	}
}