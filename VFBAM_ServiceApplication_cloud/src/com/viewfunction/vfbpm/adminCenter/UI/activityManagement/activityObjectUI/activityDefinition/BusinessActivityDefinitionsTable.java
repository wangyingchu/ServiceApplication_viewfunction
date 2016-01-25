package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

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
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.ActivityDefinitionsChangeEvent.ActivityDefinitionsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ExposedDataFieldsEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.StartNewActivityEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class BusinessActivityDefinitionsTable extends Table implements ActivityDefinitionsChangeListener{
	private static final long serialVersionUID = 6953352043326440373L;
	
	private BusinessActivityDefinition[] businessActivityDefinitionArray;
	private UserClientInfo userClientInfo;
	private ActivityObjectDetail activityObjectDetail;
	
	private String columnName_businessActivityDefinitionType="columnName_businessActivityDefinitionType";
	private String columnName_RosterName="columnName_RosterName";
	private String columnName_isEnabled="columnName_isEnabled";
	private String columnName_businessActivityDefinitionOperations="columnName_businessActivityDefinitionOperations";
	
	public BusinessActivityDefinitionsTable(BusinessActivityDefinition[] businessActivityDefinitionArray,UserClientInfo userClientInfo,ActivityObjectDetail activityObjectDetail){
		this.businessActivityDefinitionArray=businessActivityDefinitionArray;
		this.userClientInfo=userClientInfo;
		this.activityObjectDetail=activityObjectDetail;
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getBusinessActivityDefinitionsData(businessActivityDefinitionArray));
		if(this.businessActivityDefinitionArray!=null){
			if(this.businessActivityDefinitionArray.length<=20){
				setPageLength(this.businessActivityDefinitionArray.length);
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}			
		setColumnReorderingAllowed(false);		
		setColumnHeaders(new String[] {
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_activityTypeLabel"),
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_rosterNameLabel"),
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_statusLabel"),
				"",
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_activityActionsLabel")
		});
		setColumnAlignment(columnName_businessActivityDefinitionType,Table.ALIGN_LEFT);	
		setColumnAlignment(columnName_RosterName,Table.ALIGN_LEFT);
		setColumnWidth(columnName_RosterName, 250);
		setColumnAlignment(columnName_isEnabled,Table.ALIGN_CENTER);
		setColumnWidth(columnName_isEnabled, 60);
		setColumnWidth("DIV", 0);
		setColumnAlignment(columnName_businessActivityDefinitionOperations,Table.ALIGN_CENTER);
		setColumnWidth(columnName_businessActivityDefinitionOperations, 160);		
		addGeneratedColumns();
	} 
	
	private void renderActivityTypeUI(String typeIndex){		
		int index=Integer.parseInt(typeIndex);
		BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[index];
		this.activityObjectDetail.renderBusinessActivityDefinitionUI(businessActivityDefinition);		
	}
	
	private void renderExposedDataFieldsUI(String typeIndex){
		int index=Integer.parseInt(typeIndex);		
		BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[index];
		ExposedDataFieldsEditor exposedDataFieldsEditor=null;		
		DataFieldDefinition[] dataFieldDefinations=null;
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(businessActivityDefinition.getActivitySpaceName());
		try {
			BusinessActivityDefinition realtimeBusinessActivityDefinition=currentActivitySpace.getBusinessActivityDefinition(businessActivityDefinition.getActivityType());
			dataFieldDefinations=realtimeBusinessActivityDefinition.getActivityDataFields();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		exposedDataFieldsEditor=new ExposedDataFieldsEditor(businessActivityDefinition,dataFieldDefinations,userClientInfo,true,null);	
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_dataFieldLabel")			
				+"</b> <b style='color:#ce0000;'>" + businessActivityDefinition.getActivityType()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,exposedDataFieldsEditor,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	private void renderContainsStepsUI(String typeIndex){
		int index=Integer.parseInt(typeIndex);		
		BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[index];
		ActivityStepsEditor activityStepsEditor=new ActivityStepsEditor(businessActivityDefinition.getActivitySpaceName(),
				businessActivityDefinition,this.userClientInfo,null);	
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_activityStep);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_businessStepLabel")				
				+" </b> <b style='color:#ce0000;'>" + businessActivityDefinition.getActivityType()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,activityStepsEditor,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderStartNewActivityUI(String typeIndex){
		int index=Integer.parseInt(typeIndex);		
		BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[index];
		StartNewActivityEditor startNewActivityForm=new StartNewActivityEditor(businessActivityDefinition,userClientInfo);	
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_userClient_Activities);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_startNewActivityLabel")					
				+" </b> <b style='color:#ce0000;'>" + businessActivityDefinition.getActivityType()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,startNewActivityForm,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}	

	private  IndexedContainer getBusinessActivityDefinitionsData(BusinessActivityDefinition[] businessActivityDefinitionArray){
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_businessActivityDefinitionType, String.class,null);
        container.addContainerProperty(columnName_RosterName, String.class,null);
        container.addContainerProperty(columnName_isEnabled, Boolean.class,null);        
        container.addContainerProperty("DIV", String.class,null);	
        container.addContainerProperty(columnName_businessActivityDefinitionOperations, String.class,null);	
		
		if(businessActivityDefinitionArray==null){
			return container;
		}	
		for(int i=0;i<businessActivityDefinitionArray.length;i++){
			BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[i];
			String id = ""+i;
			Item item = container.addItem(id);			
			item.getItemProperty(columnName_businessActivityDefinitionType).setValue(businessActivityDefinition.getActivityType());  	
			item.getItemProperty(columnName_RosterName).setValue(businessActivityDefinition.getRosterName());
			item.getItemProperty(columnName_isEnabled).setValue(businessActivityDefinition.isEnabled());
			item.getItemProperty("DIV").setValue("");
			item.getItemProperty(columnName_businessActivityDefinitionOperations).setValue("");
		}
		return container;
	}	
	
	private void addGeneratedColumns(){
		addGeneratedColumn(columnName_businessActivityDefinitionType, new Table.ColumnGenerator() {				
			private static final long serialVersionUID = -19576501732795795L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	final String objectNameString=(String)item.getItemProperty(columnName_businessActivityDefinitionType).getValue();		            	
            	Button objNameButton = new Button(objectNameString);
            	objNameButton.setStyleName(BaseTheme.BUTTON_LINK);
            	objNameButton.addListener(new ClickListener(){ 
					private static final long serialVersionUID = 1631179748086849831L;

					public void buttonClick(ClickEvent event) {	
						renderActivityTypeUI(itemId.toString());						
					}}); 
            	return objNameButton;
            }			
		 });		
		
		addGeneratedColumn(columnName_RosterName, new Table.ColumnGenerator() { 		
			private static final long serialVersionUID = -3210043411234857682L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String objectDisplayNameString="";
	            	if(item.getItemProperty(columnName_RosterName).getValue()!=null){
	            		objectDisplayNameString=(String)item.getItemProperty(columnName_RosterName).getValue();
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }
	        });	
		
		addGeneratedColumn(columnName_isEnabled, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = 911755711057221205L;

				public Component generateCell(Table source, Object itemId, Object columnId) {            	
	            	
	            	Item item = getItem(itemId);
	            	boolean objectType=((Boolean)item.getItemProperty(columnName_isEnabled).getValue()).booleanValue();	
	            	Embedded objectTypeIcon;
	            	if(objectType){
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_Button_activityType_enable);
	            		objectTypeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_businessActivityEnabledLabel"));	            		
	            	}else{
	            		objectTypeIcon=new Embedded(null, UICommonElementDefination.ICON_Button_activityType_disable);	            	
	            		objectTypeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_businessActivityDisabledLabel"));
	            	}	            		            	
	            	return objectTypeIcon;      	
	            }
	        });	
		
		addGeneratedColumn("DIV", new Table.ColumnGenerator() { 
			private static final long serialVersionUID = 911755711057221205L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Embedded objectTypeIcon=new Embedded(null, UICommonElementDefination.AppTable_emptySpaceDiv);	            	
	            	return objectTypeIcon;
	            }
	    });		
		
		final String getDataFieldsLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_getDataFieldsLabel");
		final String getStepsLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_getStepsLabel");
		final String startNewActivityLabel=userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsTable_startNewActivityLabel");
		addGeneratedColumn(columnName_businessActivityDefinitionOperations, new Table.ColumnGenerator() { 
			private static final long serialVersionUID = -5149546455515092871L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	 
	            	HorizontalLayout actionsContainerHorizontalLayout=new HorizontalLayout();
	            	actionsContainerHorizontalLayout.setWidth("120px"); 	            	
	            	Button getExposedDataFieldsButton = new Button("");
	            	getExposedDataFieldsButton.setCaption(null);
	            	getExposedDataFieldsButton.setIcon(UICommonElementDefination.ICON_dataFieldDefine_DataFields);
	            	getExposedDataFieldsButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	getExposedDataFieldsButton.setDescription(getDataFieldsLabel);
	            	
	            	Button getStepsButton = new Button("");
	            	getStepsButton.setCaption(null);
	            	getStepsButton.setIcon(UICommonElementDefination.ICON_activityStep);
	            	getStepsButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	getStepsButton.setDescription(getStepsLabel);
	            	
	            	Button startNewActivityButton = new Button("");
	            	startNewActivityButton.setCaption(null);
	            	startNewActivityButton.setIcon(UICommonElementDefination.ICON_userClient_Activities);
	            	startNewActivityButton.setStyleName(BaseTheme.BUTTON_LINK);	            	
	            	startNewActivityButton.setDescription(startNewActivityLabel);	            	
	            	
	            	getExposedDataFieldsButton.addListener(new ClickListener(){            		
						private static final long serialVersionUID = -3567064194516804327L;

						public void buttonClick(ClickEvent event) {							
							renderExposedDataFieldsUI(itemId.toString());
						}});
	            	
	            	getStepsButton.addListener(new ClickListener(){   
						private static final long serialVersionUID = -4128628066125743446L;

						public void buttonClick(ClickEvent event) {							
							renderContainsStepsUI(itemId.toString());
						}});
	            	
	            	startNewActivityButton.addListener(new ClickListener(){
						private static final long serialVersionUID = -4605436716425314695L;

						public void buttonClick(ClickEvent event) {							
							renderStartNewActivityUI(itemId.toString());
						}});
	            	
	            	actionsContainerHorizontalLayout.addComponent(getExposedDataFieldsButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(getExposedDataFieldsButton, Alignment.MIDDLE_RIGHT);	
	            	actionsContainerHorizontalLayout.addComponent(getStepsButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(getStepsButton, Alignment.MIDDLE_RIGHT);
	            	actionsContainerHorizontalLayout.addComponent(startNewActivityButton);
	            	actionsContainerHorizontalLayout.setComponentAlignment(startNewActivityButton, Alignment.MIDDLE_RIGHT);	            	
	            	return actionsContainerHorizontalLayout;
	            }
	        });
	}
	
	public void reloadContent(BusinessActivityDefinition[] businessActivityDefinitionArray) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();	
		this.removeGeneratedColumn(columnName_businessActivityDefinitionType);		
		this.removeGeneratedColumn(columnName_RosterName);
		this.removeGeneratedColumn(columnName_isEnabled);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_businessActivityDefinitionOperations);
		this.businessActivityDefinitionArray=businessActivityDefinitionArray;		
		if(this.businessActivityDefinitionArray!=null){
			if(this.businessActivityDefinitionArray.length<=20){
				setPageLength(this.businessActivityDefinitionArray.length);
			}else{
				setPageLength(20);
			}			
		}else{
			setPageLength(0);
		}			
		if(businessActivityDefinitionArray!=null){
			for(int i=0;i<businessActivityDefinitionArray.length;i++){
				BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[i];
				String id = ""+i;
				Item item = container.addItem(id);			
				item.getItemProperty(columnName_businessActivityDefinitionType).setValue(businessActivityDefinition.getActivityType());  
				item.getItemProperty(columnName_RosterName).setValue(businessActivityDefinition.getRosterName());
				item.getItemProperty(columnName_isEnabled).setValue(businessActivityDefinition.isEnabled());
				item.getItemProperty("DIV").setValue("");
				item.getItemProperty(columnName_businessActivityDefinitionOperations).setValue("");
			}			
		}	
		addGeneratedColumns();		
	}

	public void receiveActivityDefinitionsChange(ActivityDefinitionsChangeEvent event) {
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();
		this.removeGeneratedColumn(columnName_businessActivityDefinitionType);		
		this.removeGeneratedColumn(columnName_RosterName);
		this.removeGeneratedColumn(columnName_isEnabled);
		this.removeGeneratedColumn("DIV");
		this.removeGeneratedColumn(columnName_businessActivityDefinitionOperations);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(event.getActivitySpaceName());		
		try {			
			this.businessActivityDefinitionArray=activitySpace.getBusinessActivityDefinitions();
			if(this.businessActivityDefinitionArray!=null){
				if(this.businessActivityDefinitionArray.length<=20){
					setPageLength(this.businessActivityDefinitionArray.length);
				}else{
					setPageLength(20);
				}			
			}else{
				setPageLength(0);
			}
			if(businessActivityDefinitionArray!=null){
				for(int i=0;i<businessActivityDefinitionArray.length;i++){
					BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[i];
					String id = ""+i;
					Item item = container.addItem(id);			
					item.getItemProperty(columnName_businessActivityDefinitionType).setValue(businessActivityDefinition.getActivityType());  
					item.getItemProperty(columnName_RosterName).setValue(businessActivityDefinition.getRosterName());
					item.getItemProperty(columnName_isEnabled).setValue(businessActivityDefinition.isEnabled());
					item.getItemProperty("DIV").setValue("");
					item.getItemProperty(columnName_businessActivityDefinitionOperations).setValue("");
				}		
			}				
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}	
		addGeneratedColumns();		
	}
}