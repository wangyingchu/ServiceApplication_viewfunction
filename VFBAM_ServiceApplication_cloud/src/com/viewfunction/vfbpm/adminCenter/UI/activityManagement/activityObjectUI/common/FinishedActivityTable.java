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
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.processRepository.processBureau.ProcessObject;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class FinishedActivityTable extends Table{
	private static final long serialVersionUID = -6451600186922664330L;
	
	private final UserClientInfo userClientInfo;
	private String columnName_ActivityType="columnName_ActivityType";
	private String columnName_ActivityId="columnName_ActivityId";
	private String columnName_StartTime="columnName_StartTime";
	private String columnName_EndTime="columnName_EndTime";
	private String columnName_StartUser="columnName_StartUser";
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public FinishedActivityTable(UserClientInfo userClientInfo,List<BusinessActivity> businessActivityList){
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
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityTable_activvityTypeColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityTable_activvityIdColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityTable_activvityCreateDateColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityTable_activvityFinishDateColumn"),
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_FinishedActivityTable_activvityCreateByColumn")
				});		
		setColumnWidth(columnName_ActivityId, 70);
		setColumnWidth(columnName_StartTime, 100);
		setColumnWidth(columnName_EndTime, 100);
		
		addGeneratedColumn(columnName_ActivityType, new Table.ColumnGenerator() {		
			private static final long serialVersionUID = -7936949437787628195L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String activityType=(String)item.getItemProperty(columnName_ActivityType).getValue();
            	Label stepNameLabel=new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+activityType+"</span>",Label.CONTENT_XHTML);
            	return stepNameLabel;
            }
		 });
		
		addGeneratedColumn(columnName_ActivityId, new Table.ColumnGenerator() {				
			private static final long serialVersionUID = -344741809229507116L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String activityIdString=(String)item.getItemProperty(columnName_ActivityId).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+activityIdString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });
		
		addGeneratedColumn(columnName_StartTime, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = -2036060033184577797L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String startTimeString=(String)item.getItemProperty(columnName_StartTime).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+startTimeString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });
		
		addGeneratedColumn(columnName_EndTime, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = -2929829017564048434L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String startTimeString=(String)item.getItemProperty(columnName_EndTime).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+startTimeString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });
		
		addGeneratedColumn(columnName_StartUser, new Table.ColumnGenerator() {			
			private static final long serialVersionUID = 2555494608987262193L;

			public Component generateCell(Table source, final Object itemId, Object columnId) {	            	
            	Item item = getItem(itemId);
            	String startUserString=(String)item.getItemProperty(columnName_StartUser).getValue();	            	
            	Label stepCDateLabel=new Label("<span style='color:#898989;'>"+startUserString+"</span>",Label.CONTENT_XHTML);            	
            	return stepCDateLabel;
            }
		 });		
	}
	
	private IndexedContainer getActivitiesData(List<BusinessActivity> businessActivityList){		
		IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(columnName_ActivityType, String.class,null);		
        container.addContainerProperty(columnName_ActivityId, String.class,null);
        container.addContainerProperty(columnName_StartTime, String.class,null);
        container.addContainerProperty(columnName_EndTime, String.class,null);
        container.addContainerProperty(columnName_StartUser, String.class,null);        
        ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.userClientInfo.getUserActivitySpace());
        try {
	        for(int i=0;i<businessActivityList.size();i++){
	        	BusinessActivity businessActivity=businessActivityList.get(i);
	        	String id = ""+i;
				Item item = container.addItem(id);	
				String activityType=businessActivity.getActivityDefinition().getActivityType();					
				ProcessObject currentProcessObject=businessActivity.getActivityProcessObject();									
				String startUserid=currentProcessObject.getProcessStartUserId();					
				String startUserName=activitySpace.getParticipant(startUserid).getDisplayName();			
				item.getItemProperty(columnName_ActivityType).setValue(activityType);  
				item.getItemProperty(columnName_ActivityId).setValue(currentProcessObject.getProcessObjectId());
				item.getItemProperty(columnName_StartTime).setValue(formatter.format(currentProcessObject.getProcessStartTime()));
				item.getItemProperty(columnName_EndTime).setValue(formatter.format(currentProcessObject.getProcessEndTime()));
				item.getItemProperty(columnName_StartUser).setValue(startUserName);
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