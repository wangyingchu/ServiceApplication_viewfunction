package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
/*
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;
*/
import com.viewfunction.activityEngine.activityView.ProcessQueue;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
public class ProcessQueueUI  extends VerticalLayout{
	private static final long serialVersionUID = -247451652464506567L;

	public ProcessQueueUI(ProcessQueue currentProcessQueue, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail){
		//render title bar
		HorizontalLayout actionBarHorizontalLayout=new HorizontalLayout();
		actionBarHorizontalLayout.setWidth("100%");	
		actionBarHorizontalLayout.setHeight("23px");		
		actionBarHorizontalLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);				
		this.addComponent(actionBarHorizontalLayout);
		
		Label label=new Label("ProcessQueueUI");
		this.addComponent(label);
	}

}
