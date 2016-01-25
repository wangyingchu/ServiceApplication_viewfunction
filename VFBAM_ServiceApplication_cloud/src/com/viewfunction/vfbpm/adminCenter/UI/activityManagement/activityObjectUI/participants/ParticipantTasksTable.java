package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.activityView.common.ParticipantTask;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ActivityStepDetailEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.StepDataEditorFactory;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
public class ParticipantTasksTable extends Table{
	private static final long serialVersionUID = 2223399117425288556L;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd'-'HH:mm");	
	private String columnName_ActivityType="columnName_ActivityType";
	private String columnName_ActivityStepName="columnName_ActivityStepName";
	private String columnName_RoleName="columnName_RoleName";
	private String columnName_CreateTime="columnName_CreateTime";
	private String columnName_DueDate="columnName_DueDate";
	private UserClientInfo userClientInfo;
	private List<ParticipantTask> participantTaskList;
	public ParticipantTasksTable(Participant participant,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		setStyleName(Reindeer.TABLE_STRONG);
		setWidth("100%");
		setContainerDataSource(getParticipantsData(participant));		
		setColumnReorderingAllowed(false);
		setColumnHeaders(new String[] {
				userClientInfo.getI18NProperties().getProperty("ActivityManage_ParticipantTasksTable_stepLabel"),
				userClientInfo.getI18NProperties().getProperty("ActivityManage_ParticipantTasksTable_typeLabel"),
				userClientInfo.getI18NProperties().getProperty("ActivityManage_ParticipantTasksTable_roleLabel"),
				userClientInfo.getI18NProperties().getProperty("ActivityManage_ParticipantTasksTable_createTimeLabel"),
				userClientInfo.getI18NProperties().getProperty("ActivityManage_ParticipantTasksTable_dueDateLabel")
				});
		
		setColumnWidth(columnName_RoleName, 120);
		setColumnWidth(columnName_CreateTime, 100);
		setColumnWidth(columnName_DueDate, 100);
		addGeneratedColumn(columnName_ActivityStepName, new Table.ColumnGenerator() {  
			private static final long serialVersionUID = -4826516237564732562L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		           	Item item = getItem(itemId);
		           	String objectDisplayNameString=(String)item.getItemProperty(columnName_ActivityStepName).getValue();
		           	Label objectDisplayNameLabel= new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
		           	return objectDisplayNameLabel;
		           }

		});
		
		addGeneratedColumn(columnName_RoleName, new Table.ColumnGenerator() {  
			private static final long serialVersionUID = -4826516237564732562L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		           	Item item = getItem(itemId);
		           	String objectDisplayNameString=(String)item.getItemProperty(columnName_RoleName).getValue();
		           	Label objectDisplayNameLabel= new Label("<span style='color:#444444;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
		           	return objectDisplayNameLabel;
		           }

		});
		
		addGeneratedColumn(columnName_CreateTime, new Table.ColumnGenerator() {  
			private static final long serialVersionUID = -4826516237564732562L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		           	Item item = getItem(itemId);
		           	Date createdDate=(Date)item.getItemProperty(columnName_CreateTime).getValue();
		           	String objectDisplayNameString=formatter.format(createdDate.getTime());
		           	Label objectDisplayNameLabel= new Label("<span style='color:#444444;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
		           	return objectDisplayNameLabel;
		           }

		});
		
		addGeneratedColumn(columnName_DueDate, new Table.ColumnGenerator() {  
			private static final long serialVersionUID = -4826516237564732562L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		           	Item item = getItem(itemId);
		        	Date dueDate=(Date)item.getItemProperty(columnName_DueDate).getValue();
		        	String objectDisplayNameString="";
		        	if(dueDate!=null){
		        		objectDisplayNameString=formatter.format(dueDate.getTime());
		        	}		        			           	
		           	Label objectDisplayNameLabel= new Label("<span style='color:#1360a8;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
		           	return objectDisplayNameLabel;
		           }

		});
		
		addGeneratedColumn(columnName_ActivityType, new Table.ColumnGenerator() {  
			private static final long serialVersionUID = -4826516237564732562L;

				public Component generateCell(Table source, Object itemId, Object columnId) {	            	
		           	Item item = getItem(itemId);
		           	String objectDisplayNameString=(String)item.getItemProperty(columnName_ActivityType).getValue();
		           	Label objectDisplayNameLabel= new Label("<span style='color:#444444;text-shadow: 1px 1px 1px #eeeeee;'>"+objectDisplayNameString+"</span>",Label.CONTENT_XHTML);
		           	return objectDisplayNameLabel;
		           }
		});
		/* does not have Participant info when login as admin,should not execute business logic
		addListener(new ItemClickEvent.ItemClickListener() {
			private static final long serialVersionUID = -3043422804491182189L;

			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {                    
                	renderActivityStepDetailEditor(event.getItemId().toString());                	
                }
            }
        });
        */
	}
	
	private IndexedContainer getParticipantsData(Participant participant){
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(columnName_ActivityStepName, String.class,null);
		container.addContainerProperty(columnName_ActivityType, String.class,null);        
        container.addContainerProperty(columnName_RoleName, String.class,null);	
        container.addContainerProperty(columnName_CreateTime, Date.class,null);	
        container.addContainerProperty(columnName_DueDate, Date.class,null);   
        if(participant==null){
        	setPageLength(0);
        	return container;
        }
		try {
			this.participantTaskList=participant.fetchParticipantTasks();			
			if(participantTaskList.size()<=20){
				setPageLength(participantTaskList.size());
			}else{
				setPageLength(20);
			}
			for(int i=0;i<participantTaskList.size();i++){
				ParticipantTask currentParticipantTask=participantTaskList.get(i);				
				String id = ""+i;
				Item item = container.addItem(id);			
				item.getItemProperty(columnName_ActivityType).setValue(currentParticipantTask.getActivityType());  
				item.getItemProperty(columnName_ActivityStepName).setValue(currentParticipantTask.getActivityStep().getActivityStepName());
				item.getItemProperty(columnName_RoleName).setValue(currentParticipantTask.getRoleName());
				item.getItemProperty(columnName_CreateTime).setValue(currentParticipantTask.getCreateTime());
				item.getItemProperty(columnName_DueDate).setValue(currentParticipantTask.getDueDate());
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
	
	private void renderActivityStepDetailEditor(String activityStepIdx){
		int index=Integer.parseInt(activityStepIdx);		
		ActivityStep activityStep=this.participantTaskList.get(index).getActivityStep();		
		Embedded relatedRolesIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+"Activity Step detail"+ "</b>", Label.CONTENT_XHTML);		
		VerticalLayout activityStepDetailEditor=StepDataEditorFactory.buildStepDataEditor(activityStep,this.userClientInfo,null);					                                
		LightContentWindow lightContentWindow=new LightContentWindow(relatedRolesIcon,propertyNameLable,activityStepDetailEditor,"100%");		
		lightContentWindow.center();
		lightContentWindow.setSizeFull();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
}