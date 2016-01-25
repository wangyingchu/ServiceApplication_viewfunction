package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.processRepository.exception.ProcessRepositoryRuntimeException;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class FinishedActivityStepTable extends Table{
	private static final long serialVersionUID = -726903293981950199L;
	
	private UserClientInfo userClientInfo;
	
	private String columnName_ActivityType="columnName_ActivityType";
	private String columnName_ActivityStepName="columnName_ActivityStepName";
	private String columnName_ActivityStepCreateTime="columnName_ActivityStepCreateTime";
	private String columnName_ActivityIsFinished="columnName_ActivityIsFinished";
	private String columnName_ActivityStepRelatedRole="columnName_ActivityStepRelatedRole";
	private String columnName_ActivityStepAssignee="columnName_ActivityStepAssignee";
	private String columnName_ActivityId="columnName_ActivityId";
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public FinishedActivityStepTable(UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;	
		setColumnReorderingAllowed(false);
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");		
		ActivitySpace userActivitySpace=ActivityComponentFactory.getActivitySpace(this.userClientInfo.getUserActivitySpace());		
		try {
			List<ActivityStep> finishedStepList=userActivitySpace.getActivityStepsByInvolvedUserId(this.userClientInfo.getUserParticipant().getParticipantName(), ActivitySpace.ACTIVITY_STATUS_FINISHED);
			if(finishedStepList.size()>20){
				setPageLength(20);
			}else{
				setPageLength(finishedStepList.size());
			}
			setContainerDataSource(getActivitiesData(finishedStepList));	
			setColumnHeaders(new String[] {
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_taskColume"),
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_taskRoleColume"),
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_taskExecutedByColume"),
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_activityTypeColume"),
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_isActivityTypeFinishedColume"),
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_activityCreateDateColume"),
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_activityIdColume")
					});
			
			setColumnWidth(columnName_ActivityId, 70);
			setColumnWidth(columnName_ActivityStepCreateTime, 100);	
			setColumnWidth(columnName_ActivityIsFinished, 100);			
			
			addGeneratedColumn(columnName_ActivityStepName, new Table.ColumnGenerator() {
				private static final long serialVersionUID = -5414722120985211099L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String stepName=(String)item.getItemProperty(columnName_ActivityStepName).getValue();
	            	Label stepNameLabel=new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+stepName+"</span>",Label.CONTENT_XHTML);
	            	return stepNameLabel;
	            }
			 });			
			
			addGeneratedColumn(columnName_ActivityStepRelatedRole, new Table.ColumnGenerator() {						
				private static final long serialVersionUID = 5464079291204914789L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String roleString=(String)item.getItemProperty(columnName_ActivityStepRelatedRole).getValue();
	            	Label roleLabel=null;
	            	if(roleString!=null){
	            		roleLabel=new Label("<span style='color:#898989;'>"+roleString+"</span>",Label.CONTENT_XHTML);  
	            	}else{
	            		roleLabel=new Label("<span style='color:#898989;'>"+"</span>",Label.CONTENT_XHTML); 
	            	}	            	          	
	            	return roleLabel;
	            }
			 });
			
			addGeneratedColumn(columnName_ActivityStepAssignee, new Table.ColumnGenerator() {				
				private static final long serialVersionUID = -160437611242387203L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String assigneeString=(String)item.getItemProperty(columnName_ActivityStepAssignee).getValue();	            	
	            	Label assigneeLabel=new Label("<span style='color:#898989;'>"+assigneeString+"</span>",Label.CONTENT_XHTML);            	
	            	return assigneeLabel;
	            }
			 });
			
			addGeneratedColumn(columnName_ActivityType, new Table.ColumnGenerator() {				
				private static final long serialVersionUID = -5215511052521325795L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String activityTypeString=(String)item.getItemProperty(columnName_ActivityType).getValue();	            	
	            	Label activityTypeLabel=new Label("<span style='color:#898989;'>"+activityTypeString+"</span>",Label.CONTENT_XHTML);            	
	            	return activityTypeLabel;
	            }
			 });
			final String finishedLabel=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_activityFinishedLabel");
			final String unFinishedLabel=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityStepTable_activityUnfinishedLabel");
			addGeneratedColumn(columnName_ActivityIsFinished, new Table.ColumnGenerator() {	
				private static final long serialVersionUID = 669617859288380048L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	Boolean activityFinishString=(Boolean)item.getItemProperty(columnName_ActivityIsFinished).getValue();	            	
	            	Label activityFinishLabel=null;	            			
	            	if(activityFinishString.booleanValue()){
	            		activityFinishLabel=new Label("<span style='color:#898989;'>"+finishedLabel+"</span>",Label.CONTENT_XHTML);
	            	}else{
	            		activityFinishLabel=new Label("<span style='color:#ce0000;'>"+unFinishedLabel+"</span>",Label.CONTENT_XHTML);
	            	}		            	
	            	return activityFinishLabel;
	            }
			 });
			
			addGeneratedColumn(columnName_ActivityStepCreateTime, new Table.ColumnGenerator() {					
				private static final long serialVersionUID = 8218156453911026347L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String stepCreateTimeString=(String)item.getItemProperty(columnName_ActivityStepCreateTime).getValue();	            	
	            	Label stepCreateTimeLabel=new Label("<span style='color:#898989;'>"+stepCreateTimeString+"</span>",Label.CONTENT_XHTML);            	
	            	return stepCreateTimeLabel;
	            }
			 });
			
			addGeneratedColumn(columnName_ActivityId, new Table.ColumnGenerator() {					
				private static final long serialVersionUID = -4981682934161594547L;

				public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
	            	Item item = getItem(itemId);
	            	String activityIdString=(String)item.getItemProperty(columnName_ActivityId).getValue();	            	
	            	Label activityIdLabel=new Label("<span style='color:#898989;'>"+activityIdString+"</span>",Label.CONTENT_XHTML);            	
	            	return activityIdLabel;
	            }
			 });		
			
		} catch (ProcessRepositoryRuntimeException e) {			
			e.printStackTrace();
		}			
	}
	
	private IndexedContainer getActivitiesData(List<ActivityStep> finishedStepList){		
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_ActivityStepName, String.class,null);
		container.addContainerProperty(columnName_ActivityStepRelatedRole, String.class,null);
		 container.addContainerProperty(columnName_ActivityStepAssignee, String.class,null);		 
		container.addContainerProperty(columnName_ActivityType, String.class,null);		
		 container.addContainerProperty(columnName_ActivityIsFinished, Boolean.class,null);  		 
        container.addContainerProperty(columnName_ActivityStepCreateTime, String.class,null);         
        container.addContainerProperty(columnName_ActivityId, String.class,null);
        
        ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.userClientInfo.getUserActivitySpace());
        try {
	        for(int i=0;i<finishedStepList.size();i++){
	        	ActivityStep activityStep=finishedStepList.get(i);	        	
	        	String id = ""+i;
				Item item = container.addItem(id);	
				item.getItemProperty(columnName_ActivityType).setValue(activityStep.getActivityType());  
				item.getItemProperty(columnName_ActivityStepName).setValue(activityStep.getActivityStepName());
				item.getItemProperty(columnName_ActivityStepCreateTime).setValue(formatter.format(activityStep.getCreateTime()));
				item.getItemProperty(columnName_ActivityIsFinished).setValue(activityStep.getBusinessActivity().isFinished());						
				if(activityStep.getRelatedRole()!=null){
					item.getItemProperty(columnName_ActivityStepRelatedRole).setValue(activityStep.getRelatedRole().getDisplayName());
				}
				String startUserName=activitySpace.getParticipant(activityStep.getStepAssignee()).getDisplayName();				
				item.getItemProperty(columnName_ActivityStepAssignee).setValue(startUserName);
				item.getItemProperty(columnName_ActivityId).setValue(activityStep.getActivityId());
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
}