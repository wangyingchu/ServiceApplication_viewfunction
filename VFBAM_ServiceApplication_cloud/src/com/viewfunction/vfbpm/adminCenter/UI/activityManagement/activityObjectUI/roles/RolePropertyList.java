package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles;

import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RolePropertyList extends VerticalLayout{
	private static final long serialVersionUID = -365714967313379606L;
	Panel containerPanel;	
	UserClientInfo userClientInfo;
	Role role;	
	
	public RolePropertyList(Role role,UserClientInfo userClientInfo){
		this.role=role;
		this.userClientInfo=userClientInfo;
		
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
			
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleProperty_nameLabel"),role.getRoleName(),null);		
		containerPanel.addComponent(namePropertyItem);		
		
		String roleDispalyName=role.getDisplayName()!=null?role.getDisplayName():"";
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleProperty_displayNameLabel"),roleDispalyName,null);		
		containerPanel.addComponent(displayNamePropertyItem);		

		String roleDescription=role.getDescription()!=null?role.getDescription():"";
		PropertyItem roleDescriptionPropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleProperty_descriptionLabel"),roleDescription,null);		
		containerPanel.addComponent(roleDescriptionPropertyItem);
		
		this.addComponent(containerPanel);	
	}
	
	public void reloadContent(Role role){
		containerPanel.removeAllComponents();
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleProperty_nameLabel"),role.getRoleName(),null);		
		containerPanel.addComponent(namePropertyItem);		
		
		String roleDispalyName=role.getDisplayName()!=null?role.getDisplayName():"";
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleProperty_displayNameLabel"),roleDispalyName,null);		
		containerPanel.addComponent(displayNamePropertyItem);		

		String roleDescription=role.getDescription()!=null?role.getDescription():"";
		PropertyItem roleDescriptionPropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleProperty_descriptionLabel"),roleDescription,null);		
		containerPanel.addComponent(roleDescriptionPropertyItem);		
	}
}