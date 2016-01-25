package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ConfigOptionDetailItem extends VerticalLayout{
	private static final long serialVersionUID = -7393154241370190841L;
	
	public UserClientInfo userClientInfo;
	public SystemConfigurationPanel systemConfigurationPanel;
	public VerticalLayout configObjectContainer;
	
	public ConfigOptionDetailItem(UserClientInfo userClientInfo,SystemConfigurationPanel systemConfigurationPanel){
		this.userClientInfo=userClientInfo;
		this.systemConfigurationPanel=systemConfigurationPanel;		
		configObjectContainer=new VerticalLayout();		
		this.userClientInfo=userClientInfo;				
		//render title bar
		HorizontalLayout actionBarHorizontalLayout=new HorizontalLayout();
		actionBarHorizontalLayout.setWidth("100%");	
		actionBarHorizontalLayout.setHeight("12px");		
		actionBarHorizontalLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);
		this.addComponent(actionBarHorizontalLayout);		
		//configObjectContainer.addComponent(actionBarHorizontalLayout);
		this.addComponent(configObjectContainer);		
	}
	
	public void renderStepEditorConfigurationUI(){
		configObjectContainer.removeAllComponents();
		StepEditorConfigurationUI StepEditorConfigurationUI=new StepEditorConfigurationUI(this.userClientInfo);
		configObjectContainer.addComponent(StepEditorConfigurationUI);
	}
	
	public void renderMessageServiceConfigurationUI(){
		configObjectContainer.removeAllComponents();
		MessageServiceConfigurationUI messageServiceConfigurationUI=new MessageServiceConfigurationUI(this.userClientInfo);
		configObjectContainer.addComponent(messageServiceConfigurationUI);
	}
	
	public void renderContentRepositoryConfigurationUI(){
		configObjectContainer.removeAllComponents();
		ContentRepositoryConfigurationUI contentRepositoryConfigurationUI=new ContentRepositoryConfigurationUI(this.userClientInfo);
		configObjectContainer.addComponent(contentRepositoryConfigurationUI);
	}
	
	public void renderProcessRepositoryConfigurationUI(){
		configObjectContainer.removeAllComponents();
		ProcessRepositoryConfigurationUI processRepositoryConfigurationUI=new ProcessRepositoryConfigurationUI(this.userClientInfo);
		configObjectContainer.addComponent(processRepositoryConfigurationUI);
	}
}