package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants;

import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ParticipantPropertyList extends VerticalLayout{
	private static final long serialVersionUID = -1270098943761739635L;
	
	Panel containerPanel;	
	UserClientInfo userClientInfo;
	Participant participant;
	
	public ParticipantPropertyList(Participant participant,UserClientInfo userClientInfo){
		this.participant=participant;
		this.userClientInfo=userClientInfo;
		
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);

		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
	
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_nameLabel"),participant.getParticipantName(),null);		
		containerPanel.addComponent(namePropertyItem);			
		
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_displayNameLabel"),participant.getDisplayName(),null);		
		containerPanel.addComponent(displayNamePropertyItem);		
	
		HorizontalLayout typeIconLayout=new HorizontalLayout();
		Embedded objectTypeIcon;
		Label typeLabel;
		if(participant.isGroup()){
			objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_groupParticipant);	
			typeLabel=new Label("<span style='color:#666666;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;margin-left:5px;'>("+
					userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_groupTypeLabel")+")</span>" ,Label.CONTENT_XHTML);
		}else{
			objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_peopleParticipant);
			typeLabel=new Label("<span style='color:#666666;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;margin-left:5px;'>("+
					userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_peopleTypeLabel")+")</span>" ,Label.CONTENT_XHTML);
		}
		typeIconLayout.addComponent(objectTypeIcon);
		typeIconLayout.addComponent(typeLabel);		
		PropertyItem typePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_typeLabel"),null,typeIconLayout,null);		
		containerPanel.addComponent(typePropertyItem);			
		String rolesStr="";
		try {
			Role[] roleArray=participant.getRoles();
			if(roleArray!=null){
				for(Role role:roleArray){
					rolesStr=rolesStr+role.getRoleName()+";";				
				}
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		PropertyItem rolesItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_inRoleLabel")
				,rolesStr,null);		
		containerPanel.addComponent(rolesItem);
		this.addComponent(containerPanel);	
	}
	
	public void reloadContent(Participant participant){
		containerPanel.removeAllComponents();
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_nameLabel"),participant.getParticipantName(),null);		
		containerPanel.addComponent(namePropertyItem);			
		
		PropertyItem displayNamePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_displayNameLabel"),participant.getDisplayName(),null);		
		containerPanel.addComponent(displayNamePropertyItem);		
	
		HorizontalLayout typeIconLayout=new HorizontalLayout();
		Embedded objectTypeIcon;
		Label typeLabel;
		if(participant.isGroup()){
			objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_groupParticipant);	
			typeLabel=new Label("<span style='color:#666666;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;margin-left:5px;'>("+
					userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_groupTypeLabel")+")</span>" ,Label.CONTENT_XHTML);
		}else{
			objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_peopleParticipant);
			typeLabel=new Label("<span style='color:#666666;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;margin-left:5px;'>("+
					userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_peopleTypeLabel")+")</span>" ,Label.CONTENT_XHTML);
		}
		typeIconLayout.addComponent(objectTypeIcon);
		typeIconLayout.addComponent(typeLabel);		
		PropertyItem typePropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_typeLabel"),null,typeIconLayout,null);		
		containerPanel.addComponent(typePropertyItem);			
		String rolesStr="";
		try {
			Role[] roleArray=participant.getRoles();
			if(roleArray!=null){
				for(Role role:roleArray){
					rolesStr=rolesStr+role.getRoleName()+";";				
				}
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		PropertyItem rolesItem=new PropertyItem(PropertyItem.POSTION_ODD,null,userClientInfo.getI18NProperties().getProperty("ActivityManage_participantProperty_inRoleLabel")
				,rolesStr,null);		
		containerPanel.addComponent(rolesItem);
	}
}