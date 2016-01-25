package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import javax.jcr.PropertyType;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.BusinessActivityDefinitionUI;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ExposedDataFieldsEditor extends VerticalLayout{	
	private static final long serialVersionUID = -3896536001035409308L;

	private UserClientInfo userClientInfo;
	private Map<String,DataFieldDefinition> dataFieldDefinitionMap;
	private Object activityObject;
	public Button saveDefinationButton;
	public HorizontalLayout saveNofityContainer;
	public BusinessActivityDefinitionUI businessActivityDefinitionUI;
	
	public ExposedDataFieldsEditor(Object activityObject,DataFieldDefinition[] dataFieldDefinations,UserClientInfo userClientInfo,boolean isPopUp,BusinessActivityDefinitionUI businessActivityDefinitionUI){
		this.userClientInfo=userClientInfo;
		this.activityObject=activityObject;
		this.dataFieldDefinitionMap=new HashMap<String,DataFieldDefinition>();		
		this.businessActivityDefinitionUI=businessActivityDefinitionUI;
		HorizontalLayout editorActionContainer=new HorizontalLayout();
		editorActionContainer.setWidth("100%");
		editorActionContainer.setHeight("20px");
		editorActionContainer.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_EditorActionContainer);	
		
		HorizontalLayout messageContainer=new HorizontalLayout();	
		if(!isPopUp){
			HorizontalLayout divSpace_0=new HorizontalLayout();
			divSpace_0.setWidth("10px");
			messageContainer.addComponent(divSpace_0);
		}		
		
		Label dataFieldsLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_sectionDescLabel"));
		messageContainer.addComponent(dataFieldsLabel);		
		saveNofityContainer=new HorizontalLayout();
		HorizontalLayout divSpace=new HorizontalLayout();
		divSpace.setWidth("10px");
		Embedded infoIcon=new Embedded(null, UICommonElementDefination.AppPanel_InfoActionbarIcon);
		Label saveDataNotification;
		if(isInBusinessActivityDefinitionUI()){
			saveDataNotification=new Label("<span style='color:#2779c7;'>"+
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_clickCommitButtonNotifyLabel")+"</span>", Label.CONTENT_XHTML);
		}else{
			saveDataNotification=new Label("<span style='color:#2779c7;'>"+
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_clickSaveButtonNotifyLabel")+"</span>", Label.CONTENT_XHTML);
		}		
		saveNofityContainer.addComponent(divSpace);
		saveNofityContainer.addComponent(infoIcon);
		saveNofityContainer.addComponent(saveDataNotification);		
		messageContainer.addComponent(saveNofityContainer);		
		saveNofityContainer.setVisible(false);
		
		editorActionContainer.addComponent(messageContainer);
		editorActionContainer.setComponentAlignment(messageContainer, Alignment.MIDDLE_LEFT);
		
		HorizontalLayout actionButtonContainer=new HorizontalLayout();
		actionButtonContainer.setWidth("60px");	
		
		Button addDefinationButton=new Button();		
		addDefinationButton.setCaption(null);
		addDefinationButton.setIcon(UICommonElementDefination.AppPanel_newActionbarIcon);
		addDefinationButton.setStyleName(BaseTheme.BUTTON_LINK);
		actionButtonContainer.addComponent(addDefinationButton);
		addDefinationButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_addNewButtonDescLabel"));
		addDefinationButton.addListener(new ClickListener(){            		
			private static final long serialVersionUID = -3567064194516804327L;

			public void buttonClick(ClickEvent event) {							
				renderAddNewDataFieldUI();
			}});				
		if(this.activityObject instanceof BusinessActivityDefinition&&this.businessActivityDefinitionUI==null){
			addDefinationButton.setEnabled(false);
		}		
		saveDefinationButton=new Button();		
		saveDefinationButton.setCaption(null);
		saveDefinationButton.setIcon(UICommonElementDefination.AppPanel_saveActionbarIcon);
		saveDefinationButton.setStyleName(BaseTheme.BUTTON_LINK);
		actionButtonContainer.addComponent(saveDefinationButton);		
		saveDefinationButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_saveChangeButtonDescLabel"));
		saveDefinationButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 4952569942448726205L;

			public void buttonClick(ClickEvent event) {							
				persistenceDataFields();
			}});
		saveDefinationButton.setEnabled(false);
		editorActionContainer.addComponent(actionButtonContainer);
		editorActionContainer.setComponentAlignment(actionButtonContainer, Alignment.MIDDLE_RIGHT);
		this.addComponent(editorActionContainer);	
		
		if(dataFieldDefinations!=null){
			for(int i=0;i<dataFieldDefinations.length;i++){
				DataFieldDefinition currentDataFieldDefinition=dataFieldDefinations[i];
				this.dataFieldDefinitionMap.put(currentDataFieldDefinition.getFieldName(), currentDataFieldDefinition);				
				this.addComponent(renderDataFieldDefinition(currentDataFieldDefinition));				
			}	
		}
	}
	
	public void renderAddNewDataFieldUI(){		
		String activityObjectName="";
		if(this.activityObject instanceof RoleQueue){
			activityObjectName=((RoleQueue)this.activityObject).getQueueName();
		}else if(this.activityObject instanceof Roster){
			activityObjectName=((Roster)this.activityObject).getRosterName();
		}else if(this.activityObject instanceof BusinessActivityDefinition){
			activityObjectName=((BusinessActivityDefinition)this.activityObject).getActivityType();
		}
		
		Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_addNewFieldWindowDescLabel")				
				+"</b> <b style='color:#ce0000;'>" +activityObjectName+ "</b>", Label.CONTENT_XHTML);	
		DataFieldDefinitionForm dataFieldDefinitionForm=new DataFieldDefinitionForm(this,dataFieldDefinitionMap,this.userClientInfo);
		LightContentWindow lightContentWindow=new LightContentWindow(dataFieldIcon,propertyNameLable,dataFieldDefinitionForm,"550px");		
		lightContentWindow.center();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	public void persistenceDataFields(){
		int index = 0;
		DataFieldDefinition[] newDataFieldDefinitions=new DataFieldDefinition[this.dataFieldDefinitionMap.size()];
		for (Map.Entry<String, DataFieldDefinition> mapEntry : this.dataFieldDefinitionMap.entrySet()) {		   
			newDataFieldDefinitions[index] = mapEntry.getValue();
		    index++;
		}
		if(this.activityObject instanceof RoleQueue){
			try {
				((RoleQueue)this.activityObject).setExposedDataFields(newDataFieldDefinitions);
				saveDefinationButton.setEnabled(false);
				saveNofityContainer.setVisible(false);
				getWindow().showNotification(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_changeSavedNotifyLabel"),Notification.TYPE_HUMANIZED_MESSAGE);
			} catch (ActivityEngineDataException e) {				
				e.printStackTrace();
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			} catch (ActivityEngineActivityException e) {				
				e.printStackTrace();
			}
		}else if(this.activityObject instanceof Roster){
			try {
				((Roster)this.activityObject).setExposedDataFields(newDataFieldDefinitions);
				saveDefinationButton.setEnabled(false);
				saveNofityContainer.setVisible(false);
				getWindow().showNotification(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_changeSavedNotifyLabel"),Notification.TYPE_HUMANIZED_MESSAGE);
			} catch (ActivityEngineDataException e) {				
				e.printStackTrace();
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			} catch (ActivityEngineActivityException e) {				
				e.printStackTrace();
			}
		}else if(this.activityObject instanceof BusinessActivityDefinition){			
			try {
				String currentBusinessActivityDefinitionType=((BusinessActivityDefinition)this.activityObject).getActivityType();
				ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(((BusinessActivityDefinition)this.activityObject).getActivitySpaceName());
				BusinessActivityDefinition businessActivityDefinition=currentActivitySpace.getBusinessActivityDefinition(currentBusinessActivityDefinitionType);
				businessActivityDefinition.resetActivityDataFields(newDataFieldDefinitions);					
				currentActivitySpace.updateBusinessActivityDefinition(businessActivityDefinition);
				saveDefinationButton.setEnabled(false);
				saveNofityContainer.setVisible(false);
				getWindow().showNotification(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_changeSavedNotifyLabel"),Notification.TYPE_HUMANIZED_MESSAGE);
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			} catch (ActivityEngineActivityException e) {				
				e.printStackTrace();
			} catch (ActivityEngineDataException e) {				
				e.printStackTrace();
			} catch (ActivityEngineProcessException e) {				
				e.printStackTrace();
			}			
		}		
	} 	
	
	public void deleteDataFieldDefinition(Component propertyItem,String dataFieldDefinitionKey){
		dataFieldDefinitionMap.remove(dataFieldDefinitionKey);		
		if(!isInBusinessActivityDefinitionUI()){
			this.saveDefinationButton.setEnabled(true);			
		}else{
			businessActivityDefinitionModifiedNotice();
			DataFieldDefinition[] newDataFieldDefinitions=getDataFieldDefinition();				
			this.businessActivityDefinitionUI.businessActivityDefinition.resetActivityDataFields(newDataFieldDefinitions);
		}		
		saveNofityContainer.setVisible(true);
		this.removeComponent(propertyItem);		
	}
	
	public void renderUpdateNewDataFieldUI(DataFieldDefinition dataFieldDefinition,PropertyItem parentPropertyItem){
		String activityObjectName="";
		if(this.activityObject instanceof RoleQueue){
			activityObjectName=((RoleQueue)this.activityObject).getQueueName();
		}		
		Embedded dataFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataField);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_updateFieldWindowDescLabel")
				+"</b> <b style='color:#ce0000;'>" +activityObjectName+ "</b>", Label.CONTENT_XHTML);	
		DataFieldDefinitionForm dataFieldDefinitionForm=new DataFieldDefinitionForm(this,dataFieldDefinitionMap,dataFieldDefinition,parentPropertyItem,this.userClientInfo);
		LightContentWindow lightContentWindow=new LightContentWindow(dataFieldIcon,propertyNameLable,dataFieldDefinitionForm,"550px");		
		lightContentWindow.center();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	public PropertyItem renderDataFieldDefinition(final DataFieldDefinition dataFieldDefinition){
		HorizontalLayout definationActionContainer=new HorizontalLayout();
		final String dataFieldDefinitionKey=dataFieldDefinition.getFieldName();
		definationActionContainer.setWidth("60px");		
		Button editDefinationButton=new Button();		
		editDefinationButton.setCaption(null);
		editDefinationButton.setIcon(UICommonElementDefination.AppPanel_editActionbarIcon);
		editDefinationButton.setStyleName(BaseTheme.BUTTON_LINK);
		editDefinationButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_editButtonDescLabel"));	
		if(this.activityObject instanceof BusinessActivityDefinition&&this.businessActivityDefinitionUI==null){
			editDefinationButton.setEnabled(false);
		}	
		definationActionContainer.addComponent(editDefinationButton);		
		editDefinationButton.addListener(new ClickListener(){				
			private static final long serialVersionUID = -4373878753133422097L;

			public void buttonClick(ClickEvent event) {
				PropertyItem parentPropertyItem =(PropertyItem)( event.getComponent().getParent().getParent());
				renderUpdateNewDataFieldUI(dataFieldDefinition,parentPropertyItem);				
			}});
		
		Button deleteDefinationButton=new Button();		
		deleteDefinationButton.setCaption(null);
		deleteDefinationButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
		deleteDefinationButton.setStyleName(BaseTheme.BUTTON_LINK);
		deleteDefinationButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_deleteButtonDescLabel"));
		if(this.activityObject instanceof BusinessActivityDefinition&&this.businessActivityDefinitionUI==null){
			deleteDefinationButton.setEnabled(false);
		}	
		definationActionContainer.addComponent(deleteDefinationButton);		
		deleteDefinationButton.addListener(new ClickListener(){		
			private static final long serialVersionUID = 4138377261600067858L;

			public void buttonClick(ClickEvent event) {
				 Component parentPropertyItem = event.getComponent().getParent().getParent();
				 deleteDataFieldDefinition(parentPropertyItem,dataFieldDefinitionKey);				
			}});
		
		HorizontalLayout definationDetailLayout=new HorizontalLayout();		
		Label displayNameLabel=new Label(dataFieldDefinition.getDisplayName());		
		definationDetailLayout.addComponent(displayNameLabel);
		
		HorizontalLayout definationProperLayout=new HorizontalLayout();
		definationProperLayout.setWidth("50px");		
	
		Embedded isMandatoryFieldIcon=null;
		if(dataFieldDefinition.isMandatoryField()){
			isMandatoryFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_MandatoryField);	
			isMandatoryFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_mandatoryFieldLabel"));
		}else{
			isMandatoryFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_NormalField);
			isMandatoryFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_normalFieldLabel"));
		}		
		Embedded isSystemFieldIcon=null;
		if(dataFieldDefinition.isSystemField()){
			isSystemFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_SystemField);
			isSystemFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_systemFieldLabel"));
		}else{
			isSystemFieldIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_BusinessField);
			isSystemFieldIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ExposedDataFieldsEditor_businessFieldLabel"));
		}		
		
		definationProperLayout.addComponent(isMandatoryFieldIcon);
		definationProperLayout.addComponent(isSystemFieldIcon);		
		definationProperLayout.setComponentAlignment(isMandatoryFieldIcon, Alignment.MIDDLE_CENTER);
		definationProperLayout.setComponentAlignment(isSystemFieldIcon, Alignment.MIDDLE_CENTER);		
		definationDetailLayout.addComponent(definationProperLayout);
		
		Embedded typeIcon=null;		
		switch(dataFieldDefinition.getFieldType()){
			case PropertyType.BINARY:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_binaryTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_binary"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_binaryArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_binaryArray"));
				}
				break;
			case PropertyType.BOOLEAN:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_booleanTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_boolean"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_booleanArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_booleanArray"));
				}
				break;
			case PropertyType.DATE:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_dateTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_date"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_dateArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_dateArray"));
				}
				break;
			case PropertyType.DECIMAL:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_decimalTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_decimale"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_decimalArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_decimalArray"));
				}
				break;
			case PropertyType.DOUBLE:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_doubleTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_double"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_doubleArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_doubleArray"));
				}
				break;
			case PropertyType.LONG:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_longTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_long"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_longArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_longArray"));	
				}
				break;
			case PropertyType.STRING:
				if(!dataFieldDefinition.isArrayField()){
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_stringTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_string"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.AppPanel_stringArrayTypeContentIcon);
					typeIcon.setDescription(userClientInfo.getI18NProperties().getProperty("contentManage_objectType_stringArray"));
				}
				break;
		}		
		PropertyItem namePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,typeIcon,dataFieldDefinition.getFieldName(),
				dataFieldDefinition.getDescription(),definationDetailLayout,definationActionContainer);			
		return namePropertyItem;
	}
	
	public DataFieldDefinition[] getDataFieldDefinition(){		
		int index = 0;
		DataFieldDefinition[] newDataFieldDefinitions=new DataFieldDefinition[this.dataFieldDefinitionMap.size()];
		for (Map.Entry<String, DataFieldDefinition> mapEntry : this.dataFieldDefinitionMap.entrySet()) {		   
			newDataFieldDefinitions[index] = mapEntry.getValue();
		    index++;
		}
		return newDataFieldDefinitions;
	}
	
	public boolean isInBusinessActivityDefinitionUI(){
		return this.activityObject instanceof BusinessActivityDefinition;		
	}
	
	public void businessActivityDefinitionModifiedNotice(){
		if(this.businessActivityDefinitionUI!=null){
			this.businessActivityDefinitionUI.activityDefinitionStepDataModified();
		}		
	}
}