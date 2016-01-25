package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;

public class ConfigOptionSelectionBrowser extends VerticalLayout{
	private static final long serialVersionUID = 14233528394137684L;
	
	public UserClientInfo userClientInfo;
	public SystemConfigurationPanel systemConfigurationPanel;
	
	public ConfigOptionSelectionBrowser(UserClientInfo userClientInfo,SystemConfigurationPanel systemConfigurationPanel){
		this.setStyleName(Reindeer.LAYOUT_BLACK);			
		this.setSizeFull();
		this.userClientInfo=userClientInfo;
		this.systemConfigurationPanel=systemConfigurationPanel;
		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		
		Label treeTitleLabel=new Label("-&nbsp;&nbsp;"+this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_configutationTitle")
				,Label.CONTENT_XHTML);
		treeTitleLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_TreeTitleText);		
		containerPanel.addComponent(treeTitleLabel);		
		this.addComponent(containerPanel);
		setExpandRatio(containerPanel, 1.0F);		
		
		VerticalLayout optionSelectionContainer=new VerticalLayout();
		containerPanel.addComponent(optionSelectionContainer);
		
		HorizontalLayout stepEditorConfigLayout=new HorizontalLayout();			
		Embedded stepEditorIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_stepEditor);		
		stepEditorConfigLayout.addComponent(stepEditorIcon);		
		Label stepEditorLink=new Label("<span style='text-shadow: 1px 1px 1px #000000;font-weight: bold;cursor:pointer;color:#dfdfdf;'>"+"&nbsp;"+
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_stepEditorconfig")+"</span>",Label.CONTENT_XHTML);		
		stepEditorConfigLayout.addComponent(stepEditorLink);
		stepEditorConfigLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = -5165872373182642612L;

			public void layoutClick(LayoutClickEvent event) {				
				renderStepEditorConfigUI();
			}});				
		containerPanel.addComponent(stepEditorConfigLayout);
		
		HorizontalLayout contentRepositoryConfigLayout=new HorizontalLayout();			
		Embedded contentRepositoryConfigIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_contentRepository);		
		contentRepositoryConfigLayout.addComponent(contentRepositoryConfigIcon);		
		Label contentRepositoryConfigLink=new Label("<span style='text-shadow: 1px 1px 1px #000000;font-weight: bold;cursor:pointer;color:#dfdfdf;'>"+"&nbsp;"+
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_contentRepositoryConfig")+"</span>",Label.CONTENT_XHTML);		
		contentRepositoryConfigLayout.addComponent(contentRepositoryConfigLink);
		contentRepositoryConfigLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = 184124050590172897L;

			public void layoutClick(LayoutClickEvent event) {				
				renderContentRepositoryConfigurationUI();
			}});				
		containerPanel.addComponent(contentRepositoryConfigLayout);
		
		HorizontalLayout processRepositoryConfigLayout=new HorizontalLayout();			
		Embedded processRepositoryConfigIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_processRepository);		
		processRepositoryConfigLayout.addComponent(processRepositoryConfigIcon);		
		Label processRepositoryConfigLink=new Label("<span style='text-shadow: 1px 1px 1px #000000;font-weight: bold;cursor:pointer;color:#dfdfdf;'>"+"&nbsp;"+
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_processRepositoryConfig")+"</span>",Label.CONTENT_XHTML);		
		processRepositoryConfigLayout.addComponent(processRepositoryConfigLink);
		processRepositoryConfigLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = 7399290299208927769L;

			public void layoutClick(LayoutClickEvent event) {				
				renderProcessRepositoryConfigurationUI();
			}});				
		containerPanel.addComponent(processRepositoryConfigLayout);
		
		HorizontalLayout messageServiceConfigLayout=new HorizontalLayout();			
		Embedded messageConfigIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messageService);		
		messageServiceConfigLayout.addComponent(messageConfigIcon);		
		Label messageConfigLink=new Label("<span style='text-shadow: 1px 1px 1px #000000;font-weight: bold;cursor:pointer;color:#dfdfdf;'>"+"&nbsp;"+
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_messageServiceConfig")+"</span>",Label.CONTENT_XHTML);		
		messageServiceConfigLayout.addComponent(messageConfigLink);
		messageServiceConfigLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = 184124050590172897L;

			public void layoutClick(LayoutClickEvent event) {				
				renderMessageServiceConfigUI();
			}});				
		containerPanel.addComponent(messageServiceConfigLayout);		
	}
	
	private void renderStepEditorConfigUI(){			
		this.systemConfigurationPanel.getConfigOptionDetailItem().renderStepEditorConfigurationUI();
	}
	
	private void renderMessageServiceConfigUI(){			
		this.systemConfigurationPanel.getConfigOptionDetailItem().renderMessageServiceConfigurationUI();
	}
	
	private void renderContentRepositoryConfigurationUI(){			
		this.systemConfigurationPanel.getConfigOptionDetailItem().renderContentRepositoryConfigurationUI();
	}
	
	private void renderProcessRepositoryConfigurationUI(){			
		this.systemConfigurationPanel.getConfigOptionDetailItem().renderProcessRepositoryConfigurationUI();
	}
}