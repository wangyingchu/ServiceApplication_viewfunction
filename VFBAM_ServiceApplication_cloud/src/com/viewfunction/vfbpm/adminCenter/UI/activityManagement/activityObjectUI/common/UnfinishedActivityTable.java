package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.processRepository.processBureau.ProcessObject;
import com.viewfunction.processRepository.processBureau.ProcessStep;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class UnfinishedActivityTable extends Table{
	private static final long serialVersionUID = -8485452666598593639L;
	
	private final UserClientInfo userClientInfo;
	private String columnName_ActivityType="columnName_ActivityType";
	private String columnName_ActivityId="columnName_ActivityId";
	private String columnName_StartTime="columnName_StartTime";
	private String columnName_StartUser="columnName_StartUser";
	private String columnName_CurrentSteps="columnName_CurrentSteps";
	private String columnName_NextSteps="columnName_NextSteps";
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public UnfinishedActivityTable(UserClientInfo userClientInfo,List<BusinessActivity> businessActivityList){
		this.userClientInfo=userClientInfo;		
		setColumnReorderingAllowed(false);
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		
		if(businessActivityList.size()>20){
			setPageLength(20);
		}else{
			setPageLength(businessActivityList.size());
		}		
		setContainerDataSource(getActivitiesData(businessActivityList));		
		setColumnHeaders(new String[] {
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_UnfinishedActivityTable_activityTypeColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_UnfinishedActivityTable_currentTaskColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_UnfinishedActivityTable_nextTaskColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_UnfinishedActivityTable_activityIdColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_UnfinishedActivityTable_activityCreateDateColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_UnfinishedActivityTable_activityCreateByColumn")
				});		
		setColumnWidth(columnName_ActivityId, 70);
		setColumnWidth(columnName_StartTime, 100);	
		
		addGeneratedColumn(columnName_ActivityType, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = 6854215064208274219L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String activityType=(String)item.getItemProperty(columnName_ActivityType).getValue();
            	Label stepNameLabel=new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+activityType+"</span>",Label.CONTENT_XHTML);
            	return stepNameLabel;
            }
		 });
		
		addGeneratedColumn(columnName_ActivityId, new Table.ColumnGenerator() {	
			private static final long serialVersionUID = -213363033249167673L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String activityIdString=(String)item.getItemProperty(columnName_ActivityId).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+activityIdString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });
		
		addGeneratedColumn(columnName_StartTime, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = -1471668366763271204L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String startTimeString=(String)item.getItemProperty(columnName_StartTime).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+startTimeString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });
		
		addGeneratedColumn(columnName_StartUser, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = 913271456769342175L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String startUserString=(String)item.getItemProperty(columnName_StartUser).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+startUserString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });
		
		addGeneratedColumn(columnName_CurrentSteps, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = -135432015063224040L;
			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);            	
            	List<ProcessStep> currentProcessSteps=(List<ProcessStep>)item.getItemProperty(columnName_CurrentSteps).getValue();	 
            	return getCurentStepsLayout(currentProcessSteps);
            }
		 });
		
		addGeneratedColumn(columnName_NextSteps, new Table.ColumnGenerator() {					
			private static final long serialVersionUID = -135432015063224040L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String nextStepsString=(String)item.getItemProperty(columnName_NextSteps).getValue();	            	
            	Label stepCDateLabel=new Label("<span>"+nextStepsString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });	
	}
	
	private IndexedContainer getActivitiesData(List<BusinessActivity> businessActivityList){		
		IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(columnName_ActivityType, String.class,null);
		container.addContainerProperty(columnName_CurrentSteps, List.class,null);
		container.addContainerProperty(columnName_NextSteps, String.class,null);		
        container.addContainerProperty(columnName_ActivityId, String.class,null);
        container.addContainerProperty(columnName_StartTime, String.class,null);
        container.addContainerProperty(columnName_StartUser, String.class,null);
        ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.userClientInfo.getUserActivitySpace());
        try {
	        for(int i=0;i<businessActivityList.size();i++){
	        	BusinessActivity businessActivity=businessActivityList.get(i);
	        	String id = ""+i;
				Item item = container.addItem(id);	
				String activityType=businessActivity.getActivityDefinition().getActivityType();				
				ProcessObject currentProcessObject=businessActivity.getActivityProcessObject();				
				String activityId=currentProcessObject.getProcessObjectId();
				Date startTime=currentProcessObject.getProcessStartTime();				
				String startUserid=currentProcessObject.getProcessStartUserId();				
				String startUserName=activitySpace.getParticipant(startUserid).getDisplayName();
				List<ProcessStep> currentProcessSteps=currentProcessObject.getCurrentProcessSteps();
				List<String> nextSteps=currentProcessObject.getNextProcessSteps();	
				StringBuffer nextStepsSb=new StringBuffer();				
				for(String nextStepName:nextSteps){					
					nextStepsSb.append("<span style='color:#0099ff;'>"+nextStepName+"</span>");
					nextStepsSb.append("<span style='color:#898989;'>;</span>");				
				}				
				item.getItemProperty(columnName_ActivityType).setValue(activityType);  
				item.getItemProperty(columnName_ActivityId).setValue(activityId);
				item.getItemProperty(columnName_StartTime).setValue(formatter.format(startTime));				
				item.getItemProperty(columnName_StartUser).setValue(startUserName);
				item.getItemProperty(columnName_CurrentSteps).setValue(currentProcessSteps);
				item.getItemProperty(columnName_NextSteps).setValue(nextStepsSb.toString());
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
	
	private HorizontalLayout getCurentStepsLayout(List<ProcessStep> currentProcessSteps){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(userClientInfo.getUserActivitySpace());
        HorizontalLayout currentStepLayout=new HorizontalLayout();	
        for(ProcessStep processStep:currentProcessSteps){            		
        	String processStepName=processStep.getStepName();
        	String processAssigne=processStep.getStepAssignee();
        	String startUserName=null;
			try {
				if(processAssigne!=null){
					startUserName = activitySpace.getParticipant(processAssigne).getDisplayName();
				}				
			} catch (ActivityEngineRuntimeException e) {					
				e.printStackTrace();
			}
        	Label processStepNameLabel=new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+processStepName+"</span>",Label.CONTENT_XHTML); 
        	if(startUserName!=null){
        		processStepNameLabel.setDescription("Handling by "+startUserName);
        	}
        	currentStepLayout.addComponent(processStepNameLabel);            		
        	currentStepLayout.addComponent(new Label("<span style='color:#898989;'>;"+"</span>",Label.CONTENT_XHTML));
        	}  	
        	return currentStepLayout;        
	}	
}