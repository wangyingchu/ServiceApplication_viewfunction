package com.viewfunction.vfbpm.adminCenter.UI;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityManagementPanel;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.ContentManagementPanel;
import com.viewfunction.vfbpm.adminCenter.UI.processManagement.ProcessManagementPanel;
import com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration.SystemConfigurationPanel;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ConsoleContent extends VerticalLayout{
	private static final long serialVersionUID = 6662693823292760908L;

	public ConsoleContent(UserClientInfo userClientInfo){		
		 setHeight("100%");
		 setWidth("100%");
	     this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppContent);	     
	     TabSheet contentTabsheet=new TabSheet();	     
	     this.addComponent(contentTabsheet);	     
	     contentTabsheet.setSizeFull();	   	     	     
	     ActivityManagementPanel activityManagementPanel=new ActivityManagementPanel(userClientInfo);	     
	     contentTabsheet.addTab(activityManagementPanel, userClientInfo.getI18NProperties().getProperty("contentTab_activityManage"),UICommonElementDefination.AppBanner_activityTabIcon);	     
	     ProcessManagementPanel processManagementPanel=new ProcessManagementPanel(userClientInfo);
	     contentTabsheet.addTab(processManagementPanel, userClientInfo.getI18NProperties().getProperty("contentTab_processManage"),UICommonElementDefination.AppBanner_processTabIcon);	 	     
	     ContentManagementPanel contentManagementPanel=new ContentManagementPanel(userClientInfo);	     
	     contentTabsheet.addTab(contentManagementPanel, userClientInfo.getI18NProperties().getProperty("contentTab_contentManage"),UICommonElementDefination.AppBanner_contentTabIcon);
	     SystemConfigurationPanel systemConfigurationPanel=new SystemConfigurationPanel(userClientInfo);
	     contentTabsheet.addTab(systemConfigurationPanel, userClientInfo.getI18NProperties().getProperty("contentTab_systemConfig"),UICommonElementDefination.AppBanner_configTabIcon);  		
	}
}