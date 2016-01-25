package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Binary;
import javax.jcr.PropertyType;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class BusinessActivitiesTable extends Table{
	private static final long serialVersionUID = 6230703624730300333L;	
	
	private Roster roster;
	private UserClientInfo userClientInfo;
	private String columnName_ActivityId="columnName_ActivityId";
	private String columnName_ActivityType="columnName_ActivityType";
	
	public BusinessActivitiesTable(Roster roster,UserClientInfo userClientInfo){
		this.roster=roster;
		this.userClientInfo=userClientInfo;		
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getBusinessActivitiesData(this.roster));				
		setColumnWidth(columnName_ActivityId, 80);
		setColumnWidth("DIV", 0);
		addGeneratedColumn("DIV", new Table.ColumnGenerator() { 		
			private static final long serialVersionUID = 6025139741103010735L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Embedded objectTypeIcon=new Embedded(null, UICommonElementDefination.AppTable_emptySpaceDiv);	            	
	            	return objectTypeIcon;
	            }
	        });
		addGeneratedColumn(columnName_ActivityId, new Table.ColumnGenerator() { 			
			private static final long serialVersionUID = -6832415666950861844L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String activityIdString="";
	            	if(item.getItemProperty(columnName_ActivityId).getValue()!=null){
	            		activityIdString=(String)item.getItemProperty(columnName_ActivityId).getValue();
	            	}	            	
	            	Label objectDisplayNameLabel= new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+activityIdString+"</span>",Label.CONTENT_XHTML);
	            	return objectDisplayNameLabel;
	            }
	        });	
	}
	
	private IndexedContainer getBusinessActivitiesData(Roster roster){
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_ActivityId, String.class,null);
		container.addContainerProperty(columnName_ActivityType, String.class,null);
		this.setColumnHeader(columnName_ActivityId, userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivitiesTable_activityIDField"));	
		this.setColumnHeader(columnName_ActivityType,userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivitiesTable_activityTypeField"));
		this.setColumnHeader("DIV", " ");
		
		container.addContainerProperty("DIV", String.class,null);		
		try {		
			DataFieldDefinition[] dataFieldDefinition=roster.getExposedDataFields();
			if(dataFieldDefinition!=null){
				for(int i=0;i<dataFieldDefinition.length;i++){
					DataFieldDefinition currentDataFieldDefinition=dataFieldDefinition[i];					
					String fieldName=currentDataFieldDefinition.getFieldName();	
					String fieldDisplayName=currentDataFieldDefinition.getDisplayName()!=null?currentDataFieldDefinition.getDisplayName():currentDataFieldDefinition.getFieldName();
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
					this.setColumnHeader(fieldName, fieldDisplayName);	
					
				}
			}					
			List<BusinessActivity> businessActivityList=roster.fetchBusinessActivitys();
			if(businessActivityList==null){
				return container;
			}
			if(businessActivityList.size()<=20){
				setPageLength(businessActivityList.size());
			}else{
				setPageLength(20);
			}			
			for(int i=0;i<businessActivityList.size();i++){
				BusinessActivity currentActivity=businessActivityList.get(i);				
				ActivityData[] activityData=currentActivity.getActivityData();				
				String activityId=currentActivity.getActivityId();				
				String activityType=currentActivity.getActivityDefinition().getActivityType();				
				String id = ""+i;
				Item item = container.addItem(id);
				item.getItemProperty(columnName_ActivityId).setValue(activityId); 				
							
				item.getItemProperty(columnName_ActivityType).setValue(activityType); 
				
				item.getItemProperty("DIV").setValue(""); 				
				
				if(dataFieldDefinition!=null){
					for(DataFieldDefinition currentDefinition:dataFieldDefinition){								
						String fieldName=currentDefinition.getFieldName();
						Object fieldValue=getActivityDataValue(fieldName,activityData);
						if(fieldValue!=null){
							item.getItemProperty(fieldName).setValue(fieldValue);	
						}					
					}
				}				
			}			
		} catch (ActivityEngineRuntimeException e) {
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {
			e.printStackTrace();
		} catch (ActivityEngineProcessException e) {
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		return container;		
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
}