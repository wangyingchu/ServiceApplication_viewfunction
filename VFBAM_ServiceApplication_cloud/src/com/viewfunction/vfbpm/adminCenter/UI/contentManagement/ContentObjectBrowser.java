package com.viewfunction.vfbpm.adminCenter.UI.contentManagement;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.contentBrowserTree.ContentSpaceBrowserTree;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentSpaceDataProvider;

public class ContentObjectBrowser extends VerticalLayout{
	private static final long serialVersionUID = 4264941224458272651L;
	
	private ContentManagementPanel contentManagementPanel;
	
	public ContentSpaceBrowserTree contentSpaceBrowserTree;
	
	public HierarchicalContainer contentSpace_DataContainer;
	
	public UserClientInfo userClientInfo;

	public ContentObjectBrowser(UserClientInfo userClientInfo){		
		this.setStyleName(Reindeer.LAYOUT_BLACK);			
		this.setSizeFull();
		this.userClientInfo=userClientInfo;
		/*
		MenuBar contentOperationMenubar = new MenuBar();
		contentOperationMenubar.setWidth("100%");
		
		MenuBar.MenuItem operationItems = contentOperationMenubar.addItem(
				userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuBar"),UICommonElementDefination.AppMenu_operationMenuIcon, null);
		
		MenuItem createSpaceItem=operationItems.addItem(userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_createContentSpace"),null);
		createSpaceItem.setIcon(UICommonElementDefination.AppMenu_createSpaceMenuIcon);
		
		MenuItem deleteSpaceItem=operationItems.addItem(userClientInfo.getI18NProperties().getProperty("contentManage_operationMenuItem_deleteContentSpace"),null);		
		deleteSpaceItem.setIcon(UICommonElementDefination.AppMenu_deleteSpaceMenuIcon);
		
		this.addComponent(contentOperationMenubar);		
		*/
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		
		Label treeTitleLabel=new Label("-&nbsp;&nbsp;"+
				userClientInfo.getI18NProperties().getProperty("contentManage_contentObjectTree"),Label.CONTENT_XHTML);
		treeTitleLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_TreeTitleText);
		
		containerPanel.addComponent(treeTitleLabel);				
			
		this.contentSpace_DataContainer=ContentSpaceDataProvider.getRegisteredContentSpace_DataContainer();		
		this.contentSpaceBrowserTree=new ContentSpaceBrowserTree(this.contentSpace_DataContainer,this);
		containerPanel.addComponent(this.contentSpaceBrowserTree);				
		
		this.addComponent(containerPanel);
		setExpandRatio(containerPanel, 1.0F);		
	}	

	public ContentManagementPanel getContentManagementPanel() {
		return contentManagementPanel;
	}

	public void setContentManagementPanel(ContentManagementPanel contentManagementPanel) {
		this.contentManagementPanel = contentManagementPanel;
	}
	
}
