package com.viewfunction.vfbpm.adminCenter.UI;

import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbpm.adminCenter.UI.userClient.UserOperationPanel;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class UserClientConsoleContent  extends VerticalLayout{
	private static final long serialVersionUID = -5219053070681516113L;

	public UserClientConsoleContent(UserClientInfo userClientInfo){		
		 setHeight("100%");
		 setWidth("100%");
	     this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppContent);	     
	     UserOperationPanel UserOperationPanel=new UserOperationPanel(userClientInfo);
	     this.addComponent(UserOperationPanel);	     
	     UserOperationPanel.setSizeFull();	    
	}
}
