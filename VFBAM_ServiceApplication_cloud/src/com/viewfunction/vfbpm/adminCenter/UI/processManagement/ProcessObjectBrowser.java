package com.viewfunction.vfbpm.adminCenter.UI.processManagement;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ProcessObjectBrowser extends VerticalLayout{
	private static final long serialVersionUID = -69835902400940232L;
	
	private UserClientInfo userClientInfo;
	private ProcessManagementPanel processManagementPanel;
	
	public ProcessObjectBrowser(UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		this.setStyleName(Reindeer.LAYOUT_BLACK);			
		this.setSizeFull();		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		
		Label treeTitleLabel=new Label("-&nbsp;&nbsp;"+
				this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectBrowser_processObjectTree"),Label.CONTENT_XHTML);				
		treeTitleLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_TreeTitleText);		
		containerPanel.addComponent(treeTitleLabel);
		
		try {
			ActivitySpace[] activitySpaceArray=ActivityComponentFactory.getActivitySpaces();
			for(ActivitySpace activitySpace:activitySpaceArray){				
				ProcessSpaceDetailTree processSpaceDetailTree=new ProcessSpaceDetailTree(activitySpace,this.userClientInfo);
				containerPanel.addComponent(processSpaceDetailTree);
			}			
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
		}		
		this.addComponent(containerPanel);
		setExpandRatio(containerPanel, 1.0F);
	}

	public ProcessManagementPanel getProcessManagementPanel() {
		return processManagementPanel;
	}

	public void setProcessManagementPanel(ProcessManagementPanel processManagementPanel) {
		this.processManagementPanel = processManagementPanel;
	}
}