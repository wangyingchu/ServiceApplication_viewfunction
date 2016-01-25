package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.PropertyType;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import com.vaadin.event.ItemClickEvent;

import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.util.StepDataEditorFactory;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RoleQueueElementsTable extends Table implements ReloadableUIElement{
	private static final long serialVersionUID = -8705865962800381557L;
	private DataFieldDefinition[] dataFieldDefinition;
	private String columnName_ActivityStepName="columnName_ActivityStepName";
	private String columnName_ActivityStepCreateDate="columnName_ActivityStepCreateDate";
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	private List<ActivityStep> activityStepList;
	private UserClientInfo userClientInfo;
	private RoleQueue roleQueue;
	private ReloadableUIElement[] syncReloadableUIElements;
	public RoleQueueElementsTable(RoleQueue roleQueue,UserClientInfo userClientInfo,ReloadableUIElement[] syncReloadableUIElements){
		this.userClientInfo=userClientInfo;
		this.roleQueue=roleQueue;
		this.syncReloadableUIElements=syncReloadableUIElements;
		try {
			dataFieldDefinition= roleQueue.getExposedDataFields();
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		}
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getRoleQueueElementsData(roleQueue));		
		setColumnWidth(columnName_ActivityStepName, 250);		
		
		addGeneratedColumn(columnName_ActivityStepName, new Table.ColumnGenerator() {		
			private static final long serialVersionUID = 2491195131784911956L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String objectNameString=(String)item.getItemProperty(columnName_ActivityStepName).getValue();	
            	int divStr=objectNameString.indexOf(":");            	
            	String activityName=objectNameString.substring(0,divStr);
            	String stepName=objectNameString.substring(divStr+1,objectNameString.length());
            	Label stepNameLabel=new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+stepName+"</span>",Label.CONTENT_XHTML);
            	stepNameLabel.setDescription(activityName);
            	return stepNameLabel;
            }
		 });
		
		addGeneratedColumn(columnName_ActivityStepCreateDate, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = -1471668366763271204L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	Date objectNameString=(Date)item.getItemProperty(columnName_ActivityStepCreateDate).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+formatter.format(objectNameString)+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });		
		setColumnWidth(columnName_ActivityStepCreateDate, 110);
		
		addListener(new ItemClickEvent.ItemClickListener() {          
			private static final long serialVersionUID = 6782203145148677844L;

			public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {                    
                	renderActivityStepDetailEditor(event.getItemId().toString());                	
                }
            }
        });
	}
	
	private IndexedContainer getRoleQueueElementsData(RoleQueue roleQueue){		
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_ActivityStepName, String.class,null);
		container.addContainerProperty(columnName_ActivityStepCreateDate, Date.class,null);		
		this.setColumnHeader(columnName_ActivityStepName, this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueueElementsTable_stepNameField"));			 
		this.setColumnHeader(columnName_ActivityStepCreateDate, this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueueElementsTable_stepCreateDateField"));
		if(roleQueue==null){
			return container;
		}
		if(dataFieldDefinition!=null){
			for(int i=0;i<dataFieldDefinition.length;i++){
				DataFieldDefinition currentDataFieldDefinition=dataFieldDefinition[i];					
				String fieldName=currentDataFieldDefinition.getDisplayName()!=null?currentDataFieldDefinition.getDisplayName():currentDataFieldDefinition.getFieldName();						
				int fieldType=currentDataFieldDefinition.getFieldType();			
				switch(fieldType){
					case PropertyType.STRING:
						container.addContainerProperty(fieldName, String.class,null);					
						break;
					case PropertyType.BOOLEAN:
						container.addContainerProperty(fieldName, Boolean.class,null);					
						break;
					case PropertyType.DATE:
						container.addContainerProperty(fieldName, Calendar.class,null);					
						break;
					case PropertyType.DECIMAL:
						container.addContainerProperty(fieldName, BigDecimal.class,null);					
						break;
					case PropertyType.DOUBLE:
						container.addContainerProperty(fieldName, Double.class,null);					
						break;
					case PropertyType.LONG:
						container.addContainerProperty(fieldName, Long.class,null);					
						break;
					case PropertyType.BINARY:
						container.addContainerProperty(fieldName, Binary.class,null);					
						break;
				}
			}
		}		
		try {
			this.activityStepList=roleQueue.fetchActivitySteps();		
			if(activityStepList!=null){				
				if(activityStepList.size()<=20){
					setPageLength(activityStepList.size());
				}else{
					setPageLength(20);
				}
			}else{
				setPageLength(0);
			}
			for(int i=0;i<activityStepList.size();i++){
				ActivityStep activityStep=activityStepList.get(i);
				String activityType=activityStep.getActivityType();
				String stepName=activityStep.getActivityStepName();				
				ActivityData[] activityDataArry=activityStep.getActivityStepData();				
				String id = ""+i;
				Item item = container.addItem(id);
				item.getItemProperty(columnName_ActivityStepName).setValue(activityType+":"+stepName); 								
				item.getItemProperty(columnName_ActivityStepCreateDate).setValue(activityStep.getCreateTime()); 		
				if(dataFieldDefinition!=null){
					for(DataFieldDefinition currentDefinition:dataFieldDefinition){						
						String fieldName=currentDefinition.getFieldName();						
						Object fieldValue=getActivityDataValue(fieldName,activityDataArry);
						if(fieldValue!=null){
							item.getItemProperty(fieldName).setValue(fieldValue);	
						}					
					}
				}				
			}			
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		return container;
	}
	
	private void refreshTableDataSource(){
		IndexedContainer container=(IndexedContainer)this.getContainerDataSource();
		container.removeAllItems();		
		this.removeGeneratedColumn(columnName_ActivityStepName);
		this.removeGeneratedColumn(columnName_ActivityStepCreateDate);		
		try {
			this.activityStepList=roleQueue.fetchActivitySteps();		
			if(activityStepList!=null){				
				if(activityStepList.size()<=20){
					setPageLength(activityStepList.size());
				}else{
					setPageLength(20);
				}
			}else{
				setPageLength(0);
			}
			for(int i=0;i<activityStepList.size();i++){
				ActivityStep activityStep=activityStepList.get(i);
				String activityType=activityStep.getActivityType();
				String stepName=activityStep.getActivityStepName();				
				ActivityData[] activityDataArry=activityStep.getActivityStepData();				
				String id = ""+i;
				Item item = container.addItem(id);
				item.getItemProperty(columnName_ActivityStepName).setValue(activityType+":"+stepName); 									
				item.getItemProperty(columnName_ActivityStepCreateDate).setValue(activityStep.getCreateTime()); 				
				if(dataFieldDefinition!=null){					
					for(DataFieldDefinition currentDefinition:dataFieldDefinition){						
						String fieldName=currentDefinition.getFieldName();
						this.setColumnHeader(fieldName, fieldName);						
						Object fieldValue=getActivityDataValue(fieldName,activityDataArry);
						if(fieldValue!=null){
							item.getItemProperty(fieldName).setValue(fieldValue);	
						}					
					}				
				}				
			}			
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}	
		
		addGeneratedColumn(columnName_ActivityStepName, new Table.ColumnGenerator() {
			private static final long serialVersionUID = -5241249183207210044L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String objectNameString=(String)item.getItemProperty(columnName_ActivityStepName).getValue();	
            	int divStr=objectNameString.indexOf(":");            	
            	String activityName=objectNameString.substring(0,divStr);
            	String stepName=objectNameString.substring(divStr+1,objectNameString.length());
            	Label stepNameLabel=new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+stepName+"</span>",Label.CONTENT_XHTML);
            	stepNameLabel.setDescription(activityName);
            	return stepNameLabel;
            }
		 });
		
		addGeneratedColumn(columnName_ActivityStepCreateDate, new Table.ColumnGenerator() {				
			private static final long serialVersionUID = -8456650904278959390L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	Date objectNameString=(Date)item.getItemProperty(columnName_ActivityStepCreateDate).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+formatter.format(objectNameString)+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });
	}
	
	private void renderActivityStepDetailEditor(String activityStepIdx){
		int index=Integer.parseInt(activityStepIdx);		
		ActivityStep activityStep=this.activityStepList.get(index);		
		Embedded relatedRolesIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_RoleQueueElementsTable_stepDetailEditorWindowTitle")+ "</b>", Label.CONTENT_XHTML);	
		
		ReloadableUIElement[] syncReloadableUIElement;
		if(this.syncReloadableUIElements!=null){
			syncReloadableUIElement=new ReloadableUIElement[this.syncReloadableUIElements.length+1];
			for(int i=0;i<this.syncReloadableUIElements.length;i++){
				syncReloadableUIElement[i]=this.syncReloadableUIElements[i];				
			}
			syncReloadableUIElement[this.syncReloadableUIElements.length]=this;			
		}else{
			syncReloadableUIElement=new ReloadableUIElement[]{this};
		}
		
		VerticalLayout activityStepDetailEditor=StepDataEditorFactory.buildStepDataEditor(activityStep,this.userClientInfo,syncReloadableUIElement);
		LightContentWindow lightContentWindow=new LightContentWindow(relatedRolesIcon,propertyNameLable,activityStepDetailEditor,"100%");	
		lightContentWindow.center();
		lightContentWindow.setSizeFull();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private Object getActivityDataValue(String targetFieldName,ActivityData[] activityDataArry){
		if(activityDataArry==null){
			return null;
		}		
		for(ActivityData activityData:activityDataArry){
			String fieldName=activityData.getDataFieldDefinition().getFieldName();			
			if(fieldName.equals(targetFieldName)){				
				Object fieldValue=activityData.getDatFieldValue();
				return fieldValue;				
			}			
		}		
		return null;
	}

	public void reloadContent() {
		refreshTableDataSource();		
	}
}