package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsTable;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesTable;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesChangeEvent.RolesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RolesTable extends Table  implements RolesChangeListener{
	private static final long serialVersionUID = 7091703380568876261L;
	
	private Role[] roleArray;
	private UserClientInfo userClientInfo;
	private ActivityObjectDetail activityObjectDetail;
	
	private String columnName_RoleName="columnName_RoleName";
	private String columnName_RoleDisplayName="columnName_RoleDisplayName";
	private String columnName_RoleDescription="columnName_RoleDescription";
	private String columnName_RoleOperations="columnName_RoleOperations";
	 
	public RolesTable(Role[] roleArray, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail){
		this.roleArray=roleArray;
		this.userClientInfo=userClientInfo;
		this.activityObjectDetail=activityObjectDetail;
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getParticipantsData(this.roleArray));	
		if(this.roleArray!=null){
			if(this.roleArray.length<=20){
				setPageLength(this.roleArray.length);
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}
		
		setColumnReorderingAllowed(false);
		setColumnHeaders(new String[] {
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesTable_nameLabel"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesTable_displayNameLabel"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesTable_descriptionLabel"),
				"",
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesTable_operationLabel")
		});	
		setColumnAlignment(columnName_RoleName,Table.ALIGN_LEFT);
		setColumnWidth(columnName_RoleName, 250);
		setColumnAlignment(columnName_RoleDisplayName,Table.ALIGN_LEFT);
		setColumnWidth(columnName_RoleDisplayName, 250);
		setColumnAlignment(columnName_RoleDescription,Table.ALIGN_LEFT);
		setColumnWidth("DIV", 0);
		setColumnAlignment(columnName_RoleOperations,Table.ALIGN_CENTER);
		setColumnWidth(columnName_RoleOperations, 160);
		addGeneratedColumns();		
	}
	
	private void renderRoleUI(String participantIndex){		
		int index=Integer.parseInt(participantIndex);
		Role role=roleArray[index];
		this.activityObjectDetail.renderRoleUI(role);		
	}
	
	private void renderParticipantsUI(String roleIndex){
		int index=Integer.parseInt(roleIndex);
		Role role=roleArray[index];
		Participant[] participantArray=null;
		try {
			participantArray=role.getParticipants();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
		ParticipantsTable participantsTable=new ParticipantsTable(participantArray,userClientInfo,activityObjectDetail);		
		Embedded containsParticipantsIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceParticipant_blackIcon);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_containsParticipantsWindow_participantListLabel")
				+"</b> <b style='color:#ce0000;'>" + role.getRoleName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(containsParticipantsIcon,propertyNameLable,participantsTable,"900px");		
		lightContentWindow.center();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderRelatedRoleQueuesUI(String roleIndex){
		int index=Integer.parseInt(roleIndex);
		Role role=roleArray[index];
		RoleQueue[] roleQueueArray=null;
		try {
			roleQueueArray=role.getRelatedRoleQueues();
		} catch (ActivityEngineRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		Embedded relatedeRoleQueuesIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleQueue_blackIcon);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_relatedRoleQueuesWindow_roleQueueListLabel")
				+"</b> <b style='color:#ce0000;'>" +role.getRoleName()+ "</b>", Label.CONTENT_XHTML);		
		RoleQueuesTable roleQueuesTable=new RoleQueuesTable(roleQueueArray,userClientInfo,activityObjectDetail);		
		LightContentWindow lightContentWindow=new LightContentWindow(relatedeRoleQueuesIcon,propertyNameLable,roleQueuesTable,"950px");		
		lightContentWindow.center();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);	
	}
	
	private IndexedContainer getParticipantsData(Role[] roleArray){
		IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(columnName_RoleName, String.class,null);
        container.addContainerProperty(columnName_RoleDisplayName, String.class,null);
        container.addContainerProperty(columnName_RoleDescription, String.class,null);
        container.addContainerProperty("DIV", String.class,null);
        container.addContainerProperty(columnName_RoleOperations, String.class,null);	
        if(roleArray==null){
			return container;
		}
		for(int i=0;i<roleArray.length;i++){
			Role role=roleArray[i];
			String id = ""+i;
			Item item = container.addItem(id);			
			item.getItemProperty(columnName_RoleName).setValue(role.getRoleName());  
			item.getItemProperty(columnName_RoleDisplayName).setValue(role.getDisplayName());
			item.getItemProperty(columnName_RoleDescription).setValue(role.getDescription());
			item.getItemProperty("DIV").setValue("");
			item.getItemProperty(columnName_RoleOperations).setValue("");			
		}		
		return container;
	}

	public void receiveRolesChange(RolesChangeEvent event) {
		reloadContent(event.getActivitySpaceName());
	}
	
	private void reloadContent(String activitySpaceName) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();		
		this.removeGeneratedColumn(columnName_RoleName);
		this.removeGeneratedColumn(columnName_RoleDisplayName);
		this.removeGeneratedColumn(columnName_RoleDescription);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_RoleOperations);			
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
		try {
			this.roleArray=activitySpace.getRoles();			
			if(this.roleArray!=null){
				if(this.roleArray.length<=20){
					setPageLength(this.roleArray.length);
				}else{
					setPageLength(20);
				}			
			}else{
				setPageLength(0);
			}
			for(int i=0;i<roleArray.length;i++){
				Role role=roleArray[i];
				String id = ""+i;
				Item item = container.addItem(id);			
				item.getItemProperty(columnName_RoleName).setValue(role.getRoleName());  
				item.getItemProperty(columnName_RoleDisplayName).setValue(role.getDisplayName());
				item.getItemProperty(columnName_RoleDescription).setValue(role.getDescription());
				item.getItemProperty("DIV").setValue("");
				item.getItemProperty(columnName_RoleOperations).setValue("");			
			}
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		addGeneratedColumns();		
	}
	
	private void addGeneratedColumns(){
		addGeneratedColumn(columnName_RoleName, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = 201664762821285040L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	final String objectNameString=(String)item.getItemProperty(columnName_RoleName).getValue();		            	
            	Button objNameButton = new Button(objectNameString);
            	objNameButton.setStyleName(BaseTheme.BUTTON_LINK);
            	objNameButton.addListener(new ClickListener(){            		
					private static final long serialVersionUID = -3567064194516804327L;

					public void buttonClick(ClickEvent event) {	
						renderRoleUI(itemId.toString());						
					}}); 
            	return objNameButton;
            }
		 });		 
		
		addGeneratedColumn(columnName_RoleDisplayName, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -8605245512717829979L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString="";
	            	if(item.getItemProperty(columnName_RoleDisplayName).getValue()!=null){
	            		objectDisplayNameString=(String)item.getItemProperty(columnName_RoleDisplayName).getValue();
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }

	        });	
		
		addGeneratedColumn(columnName_RoleDescription, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -8605245512717829979L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString="";
	            	if(item.getItemProperty(columnName_RoleDescription).getValue()!=null){
	            		objectDisplayNameString=(String)item.getItemProperty(columnName_RoleDescription).getValue();
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }

	        });	
		
		addGeneratedColumn("DIV", new Table.ColumnGenerator() { 			
			private static final long serialVersionUID = 267641482070041174L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Embedded objectTypeIcon=new Embedded(null, UICommonElementDefination.AppTable_emptySpaceDiv);	            	
	            	return objectTypeIcon;
	            }
	        });	
		
		 final String getParticipantsLabel= userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesTableButton_getParticipantsLabel");
		 final String getRoleQueuessLabel= userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesTableButton_getRoleQueuesLabel");
		 addGeneratedColumn(columnName_RoleOperations, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -6791644717382641912L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	 
	            	HorizontalLayout actionsContainerHorizontalLayout=new HorizontalLayout();
	            	actionsContainerHorizontalLayout.setWidth("90px");	        		
	            	Button getParticipantsButton = new Button("getParticipants");  
	            	getParticipantsButton.setCaption(null);
	            	getParticipantsButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceParticipant_blackIcon);
	            	getParticipantsButton.setStyleName(BaseTheme.BUTTON_LINK); 
	            	getParticipantsButton.setDescription(getParticipantsLabel);
	            	
	            	Button getRelatedRoleQueuesButton = new Button("getRelatedRoleQueuesButton");
	            	getRelatedRoleQueuesButton.setCaption(null);
	            	getRelatedRoleQueuesButton.setIcon(UICommonElementDefination.AppPanel_activitySpaceRoleQueue_blackIcon);
	            	getRelatedRoleQueuesButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	getRelatedRoleQueuesButton.setDescription(getRoleQueuessLabel);
	            	
	            	getParticipantsButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderParticipantsUI(itemId.toString());
						}});	            	
	            	
	            	getRelatedRoleQueuesButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderRelatedRoleQueuesUI(itemId.toString());
						}});
	            	
	            	actionsContainerHorizontalLayout.addComponent(getParticipantsButton);	            	
	            	actionsContainerHorizontalLayout.setComponentAlignment(getParticipantsButton, Alignment.MIDDLE_RIGHT);
	            	actionsContainerHorizontalLayout.addComponent(getRelatedRoleQueuesButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(getRelatedRoleQueuesButton, Alignment.MIDDLE_RIGHT);
	            	return actionsContainerHorizontalLayout;
	            }
	        });	
	}
	
	public void reloadContent(Role[] roleArray) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();	
		this.removeGeneratedColumn(columnName_RoleName);
		this.removeGeneratedColumn(columnName_RoleDisplayName);
		this.removeGeneratedColumn(columnName_RoleDescription);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_RoleOperations);		
		this.roleArray=roleArray;
		if(this.roleArray!=null){
			if(this.roleArray.length<=20){
				setPageLength(this.roleArray.length);
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}
		if(roleArray==null){
			return;
		}	
		for(int i=0;i<roleArray.length;i++){
			Role role=roleArray[i];
			String id = ""+i;
			Item item = container.addItem(id);			
			item.getItemProperty(columnName_RoleName).setValue(role.getRoleName());  
			item.getItemProperty(columnName_RoleDisplayName).setValue(role.getDisplayName());
			item.getItemProperty(columnName_RoleDescription).setValue(role.getDescription());
			item.getItemProperty("DIV").setValue("");
			item.getItemProperty(columnName_RoleOperations).setValue("");			
		}
		addGeneratedColumns();
	}
}