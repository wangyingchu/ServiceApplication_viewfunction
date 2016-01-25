package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants;

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
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsChangeEvent.ParticipantsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesTable;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ParticipantsTable extends Table implements ParticipantsChangeListener{	
	private static final long serialVersionUID = 3623141016485259524L;
	private UserClientInfo userClientInfo;
	private Participant[] participantArray;
	private ActivityObjectDetail activityObjectDetail;
	private String columnName_ParticipanName="columnName_ParticipanName";
	private String columnName_ParticipanDisplayName="columnName_ParticipanDisplayName";
	private String columnName_ParticipanType="columnName_ParticipanType";
	private String columnName_ParticipanOperations="columnName_ParticipanOperations";

	public ParticipantsTable(Participant[] participantArray,UserClientInfo userClientInfo,ActivityObjectDetail activityObjectDetail){
		this.participantArray=participantArray;
		this.userClientInfo=userClientInfo;		
		this.activityObjectDetail=activityObjectDetail;
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getParticipantsData(participantArray));	
		
		if(this.participantArray!=null){
			if(this.participantArray.length<=20){
				setPageLength(this.participantArray.length);
			}else{
				setPageLength(20);
			}
		}else{
			setPageLength(0);
		}
		
		setColumnReorderingAllowed(false);
		setColumnHeaders(new String[] {
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_nameLabel"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_displayNameLabel"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_typeLabel"),
				"",
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_OperationLabel")
				});		
		
		setColumnAlignment(columnName_ParticipanName,Table.ALIGN_LEFT);	
		setColumnAlignment(columnName_ParticipanDisplayName,Table.ALIGN_LEFT);
		setColumnWidth(columnName_ParticipanDisplayName, 350);
		setColumnAlignment(columnName_ParticipanType,Table.ALIGN_CENTER);
		setColumnWidth(columnName_ParticipanType, 60);
		setColumnWidth("DIV", 0);
		setColumnAlignment(columnName_ParticipanOperations,Table.ALIGN_CENTER);
		setColumnWidth(columnName_ParticipanOperations, 160);		
		addGeneratedColumns();
	}
	
	private void renderParticipantUI(String participantIndex){		
		int index=Integer.parseInt(participantIndex);
		Participant participant=participantArray[index];
		this.activityObjectDetail.renderParticipantUI(participant);		
	}
	
	private void renderRelatedRolesUI(String participantIndex){
		int index=Integer.parseInt(participantIndex);
		Participant participant=participantArray[index];
		Role[] relatedRoles=null;
		RolesTable rolesTable=null;
		try {
			relatedRoles=participant.getRoles();
			rolesTable=new RolesTable(relatedRoles,userClientInfo,activityObjectDetail);
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}	
		Embedded relatedRolesIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_relatedRolesWindow_taskListLabel")
				+"</b> <b style='color:#ce0000;'>" + participant.getParticipantName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(relatedRolesIcon,propertyNameLable,rolesTable,"950px");		
		lightContentWindow.center();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	private void renderParticipantTasksUI(String participantIndex){
		int index=Integer.parseInt(participantIndex);
		Participant participant=participantArray[index];		
		ParticipantTasksTable participantTasksTable=new ParticipantTasksTable(participant,this.userClientInfo);				
		Embedded participantTasksIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_fetchTasks);	
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_participantTasksWindow_taskListLabel")
				+"</b> <b style='color:#ce0000;'>" + participant.getParticipantName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(participantTasksIcon,propertyNameLable,participantTasksTable,"900px");		
		lightContentWindow.center();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	private IndexedContainer getParticipantsData(Participant[] participantArray){		
		IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(columnName_ParticipanName, String.class,null);
        container.addContainerProperty(columnName_ParticipanDisplayName, String.class,null);
        container.addContainerProperty(columnName_ParticipanType, String.class,null);
        container.addContainerProperty("DIV", String.class,null);
        container.addContainerProperty(columnName_ParticipanOperations, String.class,null);	
        if(participantArray==null){
        	return container;
        }
		for(int i=0;i<  participantArray.length;i++){
			Participant participant=participantArray[i];
			String id = ""+i;
			Item item = container.addItem(id);			
			item.getItemProperty(columnName_ParticipanName).setValue(participant.getParticipantName());  
			item.getItemProperty(columnName_ParticipanDisplayName).setValue(participant.getDisplayName());
			item.getItemProperty("DIV").setValue("");
			item.getItemProperty(columnName_ParticipanOperations).setValue("");
			if(participant.isGroup()){
				item.getItemProperty(columnName_ParticipanType).setValue("GROUP");				
			}else{
				item.getItemProperty(columnName_ParticipanType).setValue("PEOPLE");
			}			
		}       	
		return container;
	}

	private void reloadContent(String activitySpaceName) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();		
		this.removeGeneratedColumn(columnName_ParticipanName);
		this.removeGeneratedColumn(columnName_ParticipanDisplayName);
		this.removeGeneratedColumn(columnName_ParticipanType);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_ParticipanOperations);			
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			this.participantArray=activitySpace.getParticipants();			
			if(this.participantArray!=null){
				if(this.participantArray.length<=20){
					setPageLength(this.participantArray.length);
				}else{
					setPageLength(20);
				}
			}else{
				setPageLength(0);
			}
			for(int i=0;i<participantArray.length;i++){
				Participant participant=participantArray[i];
				String id = ""+i;
				Item item = container.addItem(id);			
				item.getItemProperty(columnName_ParticipanName).setValue(participant.getParticipantName());  
				item.getItemProperty(columnName_ParticipanDisplayName).setValue(participant.getDisplayName());
				item.getItemProperty("DIV").setValue("");
				item.getItemProperty(columnName_ParticipanOperations).setValue("");
				if(participant.isGroup()){
					item.getItemProperty(columnName_ParticipanType).setValue("GROUP");				
				}else{
					item.getItemProperty(columnName_ParticipanType).setValue("PEOPLE");
				}			
			}
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		addGeneratedColumns();		
	}		
	
	public void reloadContent(Participant[] containsParticipant) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();		
		this.removeGeneratedColumn(columnName_ParticipanName);
		this.removeGeneratedColumn(columnName_ParticipanDisplayName);
		this.removeGeneratedColumn(columnName_ParticipanType);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_ParticipanOperations);		
		this.participantArray=containsParticipant;			
		if(this.participantArray!=null){
			if(this.participantArray.length<=20){
				setPageLength(this.participantArray.length);
			}else{
				setPageLength(20);
			}
		}else{
			setPageLength(0);
		}
		if(participantArray!=null){		
			for(int i=0;i<participantArray.length;i++){
				Participant participant=participantArray[i];
				String id = ""+i;
				Item item = container.addItem(id);			
				item.getItemProperty(columnName_ParticipanName).setValue(participant.getParticipantName());  
				item.getItemProperty(columnName_ParticipanDisplayName).setValue(participant.getDisplayName());
				item.getItemProperty("DIV").setValue("");
				item.getItemProperty(columnName_ParticipanOperations).setValue("");
				if(participant.isGroup()){
					item.getItemProperty(columnName_ParticipanType).setValue("GROUP");				
				}else{
					item.getItemProperty(columnName_ParticipanType).setValue("PEOPLE");
				}			
			}
		}
		addGeneratedColumns();		
	}	
	
	private void addGeneratedColumns(){
		addGeneratedColumn(columnName_ParticipanName, new Table.ColumnGenerator() {
			private static final long serialVersionUID = 477578880360864077L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	final String objectNameString=(String)item.getItemProperty(columnName_ParticipanName).getValue();		            	
            	Button objNameButton = new Button(objectNameString);
            	objNameButton.setStyleName(BaseTheme.BUTTON_LINK);
            	objNameButton.addListener(new ClickListener(){            		
					private static final long serialVersionUID = -3567064194516804327L;

					public void buttonClick(ClickEvent event) {	
						renderParticipantUI(itemId.toString());						
					}}); 
            	return objNameButton;
            }

		 });		 
		
		addGeneratedColumn(columnName_ParticipanDisplayName, new Table.ColumnGenerator() {  
			private static final long serialVersionUID = -4826516237564732562L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString=(String)item.getItemProperty(columnName_ParticipanDisplayName).getValue();
	            	Label objectDisplayNameLabel= new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }

	    });	
		 
		final String groupLabel= userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_groupTypeLabel");
		final String peopleLabel= userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsTable_peopleTypeLabel");
		addGeneratedColumn(columnName_ParticipanType, new Table.ColumnGenerator() {
			private static final long serialVersionUID = 2576048663634554543L;
			
				public Component generateCell(Table source, Object itemId, Object columnId) {         	
	            	Item item = getItem(itemId);
	            	String objectTypeString=(String)item.getItemProperty(columnName_ParticipanType).getValue();	
	            	Embedded objectTypeIcon;
	            	if(objectTypeString.equals("GROUP")){
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_groupParticipant);
	            		objectTypeIcon.setDescription(groupLabel);	            		
	            	}else{
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_peopleParticipant);	            	
	            		objectTypeIcon.setDescription(peopleLabel);
	            	}	            		            	
	            	return objectTypeIcon;
	            }
	        });
		
		addGeneratedColumn("DIV", new Table.ColumnGenerator() { 			
			private static final long serialVersionUID = 267641482070041174L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Embedded objectTypeIcon=new Embedded(null, UICommonElementDefination.AppTable_emptySpaceDiv);	            	
	            	return objectTypeIcon;
	            }
	        });			
		 
		 final String getRolesLabel= userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsButton_getRolesLabel");
		 final String fetchTasksLabel= userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsButton_fetchTasksLabel");
		 addGeneratedColumn(columnName_ParticipanOperations, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -6791644717382641912L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	 
	            	HorizontalLayout actionsContainerHorizontalLayout=new HorizontalLayout();
	            	actionsContainerHorizontalLayout.setWidth("90px");	        		
	            	Button getRolesButton = new Button("getRoles");  
	            	getRolesButton.setCaption(null);
	            	getRolesButton.setIcon(UICommonElementDefination.ICON_Button_participant_getRoles);
	            	getRolesButton.setStyleName(BaseTheme.BUTTON_LINK); 
	            	getRolesButton.setDescription(getRolesLabel);
	            	
	            	Button getTasksButton = new Button("fetchParticipantTasks");
	            	getTasksButton.setCaption(null);
	            	getTasksButton.setIcon(UICommonElementDefination.ICON_Button_participant_fetchTasks);
	            	getTasksButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	getTasksButton.setDescription(fetchTasksLabel);
	            	
	            	getRolesButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderRelatedRolesUI(itemId.toString());
						}});	            	
	            	
	            	getTasksButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderParticipantTasksUI(itemId.toString());
						}});
	            	
	            	actionsContainerHorizontalLayout.addComponent(getRolesButton);	            	
	            	actionsContainerHorizontalLayout.setComponentAlignment(getRolesButton, Alignment.MIDDLE_RIGHT);
	            	actionsContainerHorizontalLayout.addComponent(getTasksButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(getTasksButton, Alignment.MIDDLE_RIGHT);
	            	return actionsContainerHorizontalLayout;
	            }
	        });		
	}

	public void receiveParticipantsChange(ParticipantsChangeEvent event) {			
		reloadContent(event.getActivitySpaceName());
	}	
}