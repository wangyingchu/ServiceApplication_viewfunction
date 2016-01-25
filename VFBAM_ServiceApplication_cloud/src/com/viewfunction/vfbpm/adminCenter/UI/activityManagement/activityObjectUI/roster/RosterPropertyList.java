package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RosterPropertyList extends VerticalLayout{
	private static final long serialVersionUID = 5397877184593450215L;
	
	private Panel containerPanel;	
	private UserClientInfo userClientInfo;
	private Roster roster;	
	
	public RosterPropertyList(Roster roster,UserClientInfo userClientInfo){
		this.roster=roster;
		this.userClientInfo=userClientInfo;
		
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_RosterPropertyList_rosterNameLabel"),this.roster.getRosterName(),null);		
		containerPanel.addComponent(namePropertyItem);	
		String rosterDispalyName=this.roster.getDisplayName()!=null?this.roster.getDisplayName():"";
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_RosterPropertyList_rosterDisplayNameLabel"),rosterDispalyName,null);		
		containerPanel.addComponent(displayNamePropertyItem);
		String rosterDescription=this.roster.getDescription()!=null?this.roster.getDescription():"";
		PropertyItem descriptionPropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_RosterPropertyList_rosterDescLabel"),rosterDescription,null);		
		containerPanel.addComponent(descriptionPropertyItem);		
		this.addComponent(containerPanel);		
	}
	
	public void reloadContent(Roster roster){
		this.roster=roster;
		containerPanel.removeAllComponents();
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_RosterPropertyList_rosterNameLabel"),this.roster.getRosterName(),null);		
		containerPanel.addComponent(namePropertyItem);	
		String rosterDispalyName=this.roster.getDisplayName()!=null?this.roster.getDisplayName():"";
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_RosterPropertyList_rosterDisplayNameLabel"),rosterDispalyName,null);		
		containerPanel.addComponent(displayNamePropertyItem);
		String rosterDescription=this.roster.getDescription()!=null?this.roster.getDescription():"";
		PropertyItem descriptionPropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_RosterPropertyList_rosterDescLabel"),rosterDescription,null);		
		containerPanel.addComponent(descriptionPropertyItem);
	}
}