package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue;

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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ExposedDataFieldsEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesChangeEvent.RoleQueuesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesTable;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RoleQueuesTable extends Table implements RoleQueuesChangeListener{
	private static final long serialVersionUID = -1757339541259395016L;
	
	private RoleQueue[] roleQueueArray;
	private UserClientInfo userClientInfo;
	private ActivityObjectDetail activityObjectDetail;
	
	private String columnName_RoleQueueName="columnName_RoleQueueName";
	private String columnName_RoleQueueDisplayName="columnName_RoleQueueDisplayName";
	private String columnName_RoleQueueDescription="columnName_RoleQueueDescription";
	private String columnName_RoleQueueOperations="columnName_RoleQueueOperations";
	
	public RoleQueuesTable(RoleQueue[] roleQueueArray,UserClientInfo userClientInfo,ActivityObjectDetail activityObjectDetail){
		this.roleQueueArray=roleQueueArray;
		this.userClientInfo=userClientInfo;
		this.activityObjectDetail=activityObjectDetail;
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getRoleQueuesData(roleQueueArray));
		if(this.roleQueueArray!=null){
			if(this.roleQueueArray.length<=20){
				setPageLength(this.roleQueueArray.length);
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}			
		setColumnReorderingAllowed(false);
		setColumnHeaders(new String[] {
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTable_nameLabel"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTable_displayNameLabel"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTable_descriptionLabel"),
				"",
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTable_operationLabel")
		});	
		setColumnAlignment(columnName_RoleQueueName,Table.ALIGN_LEFT);	
		setColumnAlignment(columnName_RoleQueueDisplayName,Table.ALIGN_LEFT);
		setColumnWidth(columnName_RoleQueueDisplayName, 250);
		setColumnAlignment(columnName_RoleQueueDescription,Table.ALIGN_LEFT);
		setColumnWidth(columnName_RoleQueueDescription, 250);
		setColumnWidth("DIV", 0);
		setColumnAlignment(columnName_RoleQueueOperations,Table.ALIGN_CENTER);
		setColumnWidth(columnName_RoleQueueOperations, 160);		
		addGeneratedColumns();		
	}
	
	private void renderRoleQueueUI(String roleQueueIndex){		
		int index=Integer.parseInt(roleQueueIndex);
		RoleQueue roleQueue=roleQueueArray[index];
		this.activityObjectDetail.renderRoleQueueUI(roleQueue);		
	}
	
	private void renderRelatedRolesUI(String roleQueueIndex){
		int index=Integer.parseInt(roleQueueIndex);		
		RoleQueue roleQueue=roleQueueArray[index];
		RolesTable rolesTable=null;
		Role[] relatedRoles=null;
		try {
			relatedRoles=roleQueue.getRelatedRoles();
			rolesTable=new RolesTable(relatedRoles,userClientInfo,activityObjectDetail);
		} catch (ActivityEngineRuntimeException e) {
			e.printStackTrace();
		}	
		Embedded relatedRolesIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_relatedRolesWindow_taskListLabel")				
				+"</b> <b style='color:#ce0000;'>" + roleQueue.getQueueName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(relatedRolesIcon,propertyNameLable,rolesTable,"900px");		
		lightContentWindow.center();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	private void renderExposedDataFieldsUI(String roleQueueIndex){
		int index=Integer.parseInt(roleQueueIndex);		
		RoleQueue roleQueue=roleQueueArray[index];	
		ExposedDataFieldsEditor exposedDataFieldsEditor=null;		
		DataFieldDefinition[] dataFieldDefinations=null;
		try {
			dataFieldDefinations=roleQueue.getExposedDataFields();			
			exposedDataFieldsEditor=new ExposedDataFieldsEditor(roleQueue,dataFieldDefinations,userClientInfo,true,null);
		} catch (ActivityEngineRuntimeException e) {
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {
			e.printStackTrace();
		}	
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_exposedDataFieldsWindow_dataFieldsEditorLabel")				
				+"</b> <b style='color:#ce0000;'>" + roleQueue.getQueueName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,exposedDataFieldsEditor,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	private void renderActivityStepsUI(String roleQueueIndex){
		int index=Integer.parseInt(roleQueueIndex);		
		RoleQueue roleQueue=roleQueueArray[index];	
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_activityStep);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTable_activityStepsWindowTitle")				
				+"</b> <b style='color:#ce0000;'>" + roleQueue.getQueueName()+ "</b>", Label.CONTENT_XHTML);			
		RoleQueueElementsTable roleQueueElementsTable=new RoleQueueElementsTable(roleQueue,userClientInfo,null);		
		Label operationPropmt=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTable_activityStepsWindowDesc"));		
		VerticalLayout roleQueueElementsLayout=new VerticalLayout();
		roleQueueElementsLayout.addComponent(operationPropmt);
		roleQueueElementsLayout.addComponent(roleQueueElementsTable);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,roleQueueElementsLayout,"95%");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}

	private IndexedContainer getRoleQueuesData(RoleQueue[] roleQueueArray){		
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_RoleQueueName, String.class,null);
        container.addContainerProperty(columnName_RoleQueueDisplayName, String.class,null);
        container.addContainerProperty(columnName_RoleQueueDescription, String.class,null);	
        container.addContainerProperty("DIV", String.class,null);	
        container.addContainerProperty(columnName_RoleQueueOperations, String.class,null);	
        if(roleQueueArray==null){
			return container;
		}		
		for(int i=0;i<roleQueueArray.length;i++){
			RoleQueue roleQueue=roleQueueArray[i];
			String id = ""+i;
			Item item = container.addItem(id);
			item.getItemProperty(columnName_RoleQueueName).setValue(roleQueue.getQueueName());  
			item.getItemProperty(columnName_RoleQueueDisplayName).setValue(roleQueue.getDisplayName());
			item.getItemProperty(columnName_RoleQueueDescription).setValue(roleQueue.getDescription());
			item.getItemProperty("DIV").setValue("");
			item.getItemProperty(columnName_RoleQueueOperations).setValue("");				
		}       	
		return container;
	}
	
	public void reloadContent(RoleQueue[] roleQueueArray) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();		
		this.removeGeneratedColumn(columnName_RoleQueueName);
		this.removeGeneratedColumn(columnName_RoleQueueDisplayName);
		this.removeGeneratedColumn(columnName_RoleQueueDescription);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_RoleQueueOperations);			
		
		this.roleQueueArray=roleQueueArray;
		if(this.roleQueueArray!=null){
			if(this.roleQueueArray.length<=20){
				setPageLength(this.roleQueueArray.length);
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}			
		
		if(roleQueueArray==null){
				return;
		}		
		for(int i=0;i<this.roleQueueArray.length;i++){
			RoleQueue roleQueue=roleQueueArray[i];
			String id = ""+i;
			Item item = container.addItem(id);
			item.getItemProperty(columnName_RoleQueueName).setValue(roleQueue.getQueueName());  
			item.getItemProperty(columnName_RoleQueueDisplayName).setValue(roleQueue.getDisplayName());
			item.getItemProperty(columnName_RoleQueueDescription).setValue(roleQueue.getDescription());
			item.getItemProperty("DIV").setValue("");
			item.getItemProperty(columnName_RoleQueueOperations).setValue("");				
		}		
		addGeneratedColumns();
	}
	
	private void addGeneratedColumns(){
		addGeneratedColumn(columnName_RoleQueueName, new Table.ColumnGenerator() {				
			private static final long serialVersionUID = 3812714728966411447L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	final String objectNameString=(String)item.getItemProperty(columnName_RoleQueueName).getValue();		            	
            	Button objNameButton = new Button(objectNameString);
            	objNameButton.setStyleName(BaseTheme.BUTTON_LINK);
            	objNameButton.addListener(new ClickListener(){            		
					private static final long serialVersionUID = -3567064194516804327L;

					public void buttonClick(ClickEvent event) {	
						renderRoleQueueUI(itemId.toString());						
					}}); 
            	return objNameButton;
            }			
		 });		
		
		addGeneratedColumn(columnName_RoleQueueDisplayName, new Table.ColumnGenerator() { 			
			private static final long serialVersionUID = 2428636580728274817L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString="";
	            	if(item.getItemProperty(columnName_RoleQueueDisplayName).getValue()!=null){
	            		objectDisplayNameString=(String)item.getItemProperty(columnName_RoleQueueDisplayName).getValue();
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }
	        });	
		
		addGeneratedColumn(columnName_RoleQueueDescription, new Table.ColumnGenerator() { 			
			private static final long serialVersionUID = 267641482070041174L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString="";
	            	if(item.getItemProperty(columnName_RoleQueueDescription).getValue()!=null){
	            		objectDisplayNameString=(String)item.getItemProperty(columnName_RoleQueueDescription).getValue();
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
		
		 final String getRolesLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTableButton_getRolessLabel");
		 final String getRoleQueuessLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTableButton_getDataFieldsLabel");
		 final String fetchActivityStepsLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesTableButton_fetchActivityStepsLabel");
		 addGeneratedColumn(columnName_RoleQueueOperations, new Table.ColumnGenerator() { 	
			private static final long serialVersionUID = -2819781131706074650L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	 
	            	HorizontalLayout actionsContainerHorizontalLayout=new HorizontalLayout();
	            	actionsContainerHorizontalLayout.setWidth("120px");	  	            	
	            	Button getRolesButton = new Button("");  
	            	getRolesButton.setCaption(null);
	            	getRolesButton.setIcon(UICommonElementDefination.ICON_Button_participant_getRoles);
	            	getRolesButton.setStyleName(BaseTheme.BUTTON_LINK); 
	            	getRolesButton.setDescription(getRolesLabel);
	            	
	            	Button getExposedDataFieldsButton = new Button("");
	            	getExposedDataFieldsButton.setCaption(null);
	            	getExposedDataFieldsButton.setIcon(UICommonElementDefination.ICON_dataFieldDefine_DataFields);
	            	getExposedDataFieldsButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	getExposedDataFieldsButton.setDescription(getRoleQueuessLabel);
	            	
	            	Button fetchActivityStepsButton = new Button("");
	            	fetchActivityStepsButton.setCaption(null);
	            	fetchActivityStepsButton.setIcon(UICommonElementDefination.ICON_activityStep);
	            	fetchActivityStepsButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	fetchActivityStepsButton.setDescription(fetchActivityStepsLabel);
	            	
	            	getRolesButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderRelatedRolesUI(itemId.toString());
						}});	            	
	            	
	            	getExposedDataFieldsButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderExposedDataFieldsUI(itemId.toString());
						}});
	            	
	            	fetchActivityStepsButton.addListener(new ClickListener(){
						private static final long serialVersionUID = -7835019069286302323L;

						public void buttonClick(ClickEvent event) {							
							renderActivityStepsUI(itemId.toString());
						}});
	            	
	            	actionsContainerHorizontalLayout.addComponent(getRolesButton);	            	
	            	actionsContainerHorizontalLayout.setComponentAlignment(getRolesButton, Alignment.MIDDLE_RIGHT);
	            	actionsContainerHorizontalLayout.addComponent(getExposedDataFieldsButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(getExposedDataFieldsButton, Alignment.MIDDLE_RIGHT);	 
	            	actionsContainerHorizontalLayout.addComponent(fetchActivityStepsButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(fetchActivityStepsButton, Alignment.MIDDLE_RIGHT);	
	            	return actionsContainerHorizontalLayout;
	            }
	        });		
	}

	public void receiveRoleQueuesChange(RoleQueuesChangeEvent event) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();
		this.removeGeneratedColumn(columnName_RoleQueueName);
		this.removeGeneratedColumn(columnName_RoleQueueDisplayName);
		this.removeGeneratedColumn(columnName_RoleQueueDescription);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_RoleQueueOperations);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(event.getActivitySpaceName());		
		try {
			this.roleQueueArray=activitySpace.getRoleQueues();
			if(this.roleQueueArray!=null){
				if(this.roleQueueArray.length<=20){
					setPageLength(this.roleQueueArray.length);
				}else{
					setPageLength(20);
				}			
			}else{
				setPageLength(0);
			}			
			for(int i=0;i<roleQueueArray.length;i++){
				RoleQueue roleQueue=roleQueueArray[i];
				String id = ""+i;
				Item item = container.addItem(id);
				item.getItemProperty(columnName_RoleQueueName).setValue(roleQueue.getQueueName());  
				item.getItemProperty(columnName_RoleQueueDisplayName).setValue(roleQueue.getDisplayName());
				item.getItemProperty(columnName_RoleQueueDescription).setValue(roleQueue.getDescription());
				item.getItemProperty("DIV").setValue("");
				item.getItemProperty(columnName_RoleQueueOperations).setValue("");				
			}       			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
		addGeneratedColumns();			
	}	
}