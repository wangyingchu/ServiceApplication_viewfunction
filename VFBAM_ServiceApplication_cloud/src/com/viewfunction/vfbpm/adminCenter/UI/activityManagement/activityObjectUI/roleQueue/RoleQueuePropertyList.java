package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RoleQueuePropertyList extends VerticalLayout{
	private static final long serialVersionUID = 3327235628823265119L;
	
	private Panel containerPanel;	
	private UserClientInfo userClientInfo;
	private RoleQueue roleQueue;	
	
	public RoleQueuePropertyList(RoleQueue roleQueue,UserClientInfo userClientInfo){
		this.roleQueue=roleQueue;
		this.userClientInfo=userClientInfo;
		
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();			
	
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueuePropertyList_roleNameLabel"),this.roleQueue.getQueueName(),null);		
		containerPanel.addComponent(namePropertyItem);	
		String roleDispalyName=this.roleQueue.getDisplayName()!=null?this.roleQueue.getDisplayName():"";
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueuePropertyList_roleDisplayNameLabel"),roleDispalyName,null);		
		containerPanel.addComponent(displayNamePropertyItem);
		String roleDescription=this.roleQueue.getDescription()!=null?this.roleQueue.getDescription():"";
		PropertyItem roleDescriptionPropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueuePropertyList_roleDescLabel"),roleDescription,null);		
		containerPanel.addComponent(roleDescriptionPropertyItem);		
		this.addComponent(containerPanel);			
	}
	
	public void reloadContent(RoleQueue newRoleQueue){
		this.roleQueue=newRoleQueue;
		containerPanel.removeAllComponents();
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueuePropertyList_roleNameLabel"),this.roleQueue.getQueueName(),null);		
		containerPanel.addComponent(namePropertyItem);	
		String roleDispalyName=this.roleQueue.getDisplayName()!=null?this.roleQueue.getDisplayName():"";
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueuePropertyList_roleDescLabel"),roleDispalyName,null);		
		containerPanel.addComponent(displayNamePropertyItem);
		String roleDescription=this.roleQueue.getDescription()!=null?this.roleQueue.getDescription():"";
		PropertyItem roleDescriptionPropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"RoleQueue desc",roleDescription,null);		
		containerPanel.addComponent(roleDescriptionPropertyItem);
	}
}
