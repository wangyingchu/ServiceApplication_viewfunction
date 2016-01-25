package com.viewfunction.vfbpm.adminCenter.UI.userClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import javax.jcr.Binary;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;

import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.peter.contextmenu.ContextMenu.ClickEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItem;

import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.activityView.common.ParticipantTask;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ActivityCommentEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.StepDataEditorFactory;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class UserTaskList extends VerticalLayout implements ReloadableUIElement{
	private static final long serialVersionUID = -1610454614310358899L;
	
	private VerticalLayout userTasksContainer;
	private UserClientInfo userClientInfo;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	private Participant userParticipant;
	
	private DialogWindow returnTaskComfirmWindow;
	private DialogWindow deletePrivateTaskComfirmWindow;
	private DialogWindow reassignTaskOperationWindow;
	private ActivitySpace activitySpace;	

	public UserTaskList(Participant userParticipant,UserClientInfo userClientInfo){				
		this.userParticipant=userParticipant;		
		this.userClientInfo=userClientInfo;		
		this.activitySpace=ActivityComponentFactory.getActivitySpace(this.userClientInfo.getUserActivitySpace());
		this.userTasksContainer=new VerticalLayout();
		this.userTasksContainer.setWidth("100%");	
		this.addComponent(this.userTasksContainer);		
		renderUserTaskListUI(userParticipant);		
	}
	
	public void renderUserTaskListUI(Participant userParticipant){
		try {
			this.userTasksContainer.removeAllComponents();
			List<ParticipantTask> userTaskList=userParticipant.fetchParticipantTasks();
			for(ParticipantTask participantTask:userTaskList){
				this.userTasksContainer.addComponent(renderuserTaskItem(participantTask));				
			}			
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}
	}
	
	public PropertyItem renderuserTaskItem(final ParticipantTask participantTask){
		String stepName=participantTask.getActivityStepName();
		String activityType=participantTask.getActivityType();	
		String activityId=participantTask.getActivityStep().getActivityId();
		Date createTime=participantTask.getCreateTime();		
		//Date dueDate=participantTask.getDueDate();
		String taskRole="";
		try {
			taskRole=participantTask.getRoleName();				
			if(participantTask.getRoleName()!=null){
				Role currentTaskRole=activitySpace.getRole(taskRole);
				taskRole=currentTaskRole.getDisplayName()!=null?currentTaskRole.getDisplayName():currentTaskRole.getRoleName();			
			}else{
				taskRole=null;
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}
		//String stepAssignee=participantTask.getStepAssignee();
		String stepDesc=participantTask.getStepDescription();
		//String stepOwner=participantTask.getStepOwner();
		
		HorizontalLayout taskValueContainerLayout=new HorizontalLayout();	
		taskValueContainerLayout.setWidth("100%");		
		taskValueContainerLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_PropertyItem);
		
		VerticalLayout userTaskInfoLayout=new VerticalLayout();
		userTaskInfoLayout.setWidth("100%");
		taskValueContainerLayout.addComponent(userTaskInfoLayout);			
		
		taskValueContainerLayout.addListener(new LayoutClickListener(){			
			private static final long serialVersionUID = 6591702165883652459L;

			public void layoutClick(LayoutClickEvent event) {
				if (event.isDoubleClick()) {					
					renderActivityStepDetailEditor(participantTask.getActivityStep());
				}				
			}});			
		
		HorizontalLayout spaceDivLayout_0=new HorizontalLayout();
		spaceDivLayout_0.setHeight("5px");
		spaceDivLayout_0.setWidth("450px");
		userTaskInfoLayout.addComponent(spaceDivLayout_0);
		
		HorizontalLayout userTaskLabelLayout=new HorizontalLayout();
		userTaskLabelLayout.setWidth("450px");	
		
		HorizontalLayout leftPartLayout=new HorizontalLayout();
		
		Embedded participantTasksIcon=new Embedded(null, UICommonElementDefination.ICON_userClient_Step);			
		leftPartLayout.addComponent(participantTasksIcon);		
		Label activityStepName=new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold; font-size: 10pt;'>"+stepName+"</span> "
					+"<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;font-size:smaller;-webkit-text-size-adjust:none;'>("+activityId+")</span>",Label.CONTENT_XHTML);
		activityStepName.setDescription(stepDesc);
		leftPartLayout.addComponent(activityStepName);	
		Label activityTypee=new Label("- <span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;font-size:smaller;-webkit-text-size-adjust:none;'>"+activityType+"</span>",Label.CONTENT_XHTML);
		leftPartLayout.addComponent(activityTypee); 
		userTaskLabelLayout.addComponent(leftPartLayout);		
		
		final String openTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_openTaskMenuItem");
		final String reassignTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_reassignTaskMenuItem");
		final String returnTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_returnTaskMenuItem");
		final String deleteTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_deleteTaskMenuItem");
		final String showCommentMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_showCommentsMenuItem");
		
		final Button taskActionButton=new Button();
		taskActionButton.setCaption(null);
		taskActionButton.setIcon(UICommonElementDefination.ICON_userClient_DropdownMenu);
		taskActionButton.setStyleName(BaseTheme.BUTTON_LINK);		
		taskActionButton.setDebugId(participantTask.getActivityStep().getActivityId());	
		
		taskActionButton.addListener(new Button.ClickListener() {			 
			private static final long serialVersionUID = 3678003731989823911L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				ContextMenu menu = new ContextMenu();
				// Generate main level items
				ContextMenuItem openTaskItem = menu.addItem(openTaskMenuItem);
				openTaskItem.setIcon(UICommonElementDefination.ICON_userClient_OpenTask);
				String roleStr=null;
				try {
					roleStr=participantTask.getRoleName();
				} catch (ActivityEngineRuntimeException e1) {					
					e1.printStackTrace();
				} catch (ActivityEngineActivityException e1) {					
					e1.printStackTrace();
				} catch (ActivityEngineDataException e1) {					
					e1.printStackTrace();
				} catch (ActivityEngineProcessException e1) {					
					e1.printStackTrace();
				}
				if(roleStr!=null){
					ContextMenuItem reassignTaskItem = menu.addItem(reassignTaskMenuItem);	
					reassignTaskItem.setIcon(UICommonElementDefination.ICON_userClient_ReassignTask);				   
					ContextMenuItem returnTaskItem = menu.addItem(returnTaskMenuItem);
					returnTaskItem.setIcon(UICommonElementDefination.ICON_userClient_ReturnTask);	
				}else{
					ContextMenuItem removeTaskItem = menu.addItem(deleteTaskMenuItem);
					removeTaskItem.setIcon(UICommonElementDefination.ICON_userClient_DeleteTask);					
				}
				ContextMenuItem showCommentItem = menu.addItem(showCommentMenuItem);
				showCommentItem.setIcon(UICommonElementDefination.ICON_userClient_ShowComment);
				
				menu.addListener(new ContextMenu.ClickListener() {				
					private static final long serialVersionUID = 126908269754142518L;

					public void contextItemClick(ClickEvent event) {
					      // Get reference to clicked item
					      ContextMenuItem clickedItem = event.getClickedItem();					      
					      executeTaskAction(clickedItem.getName(),participantTask);
					   }					
					});				   
				   menu.show(taskActionButton);				   
				   event.getComponent().getApplication().getMainWindow().addComponent(menu);  			     
			   }
			});		
		
		HorizontalLayout rightpartLayout=new HorizontalLayout();		
		rightpartLayout.addComponent(taskActionButton); 
		userTaskLabelLayout.addComponent(rightpartLayout); 
		userTaskLabelLayout.setComponentAlignment(rightpartLayout, Alignment.MIDDLE_RIGHT);	
		userTaskInfoLayout.addComponent(userTaskLabelLayout);		
		
		HorizontalLayout spaceDivLayout_1=new HorizontalLayout();
		spaceDivLayout_1.setHeight("5px");
		userTaskInfoLayout.addComponent(spaceDivLayout_1);
		
		HorizontalLayout userTaskDataLayout=new HorizontalLayout();	
		userTaskDataLayout.setWidth("450px");
		try {
			ActivityData[] stepData=participantTask.getActivityStep().getActivityStepData();
			if(stepData!=null){
				StringBuffer stepDataFieldStr=new StringBuffer("<span style='word-wrap: break-word;word-break: normal;width:445px;'>");
				String first5FieldDisplay="";
				int maxDisplayFieldNum=5;
				if(stepData.length<=maxDisplayFieldNum){
					for(ActivityData activityData:stepData){
						String displauName=activityData.getDataFieldDefinition().getDisplayName();
						int fieldType=activityData.getDataFieldDefinition().getFieldType();
						boolean isArray=activityData.getDataFieldDefinition().isArrayField();				
						Object dataValue=activityData.getDatFieldValue();
						if(dataValue!=null){							
							String displayString=getStepDataDisplayString(fieldType,isArray,dataValue);
							stepDataFieldStr.append("<span style='color:#ce0000;font-size:smaller;-webkit-text-size-adjust:none;white-space: nowrap;'>"+displauName+"</span>-&nbsp;<span style='color:#444444;font-size:smaller;-webkit-text-size-adjust:none;white-space: nowrap;'>"+displayString+"&nbsp;&nbsp;</span>");
						}
					}				
				}else{
					int displayedFieldNumber=0;
					for(int i=0;i<stepData.length;i++ ){
						ActivityData activityData=stepData[i];					
						String displauName=activityData.getDataFieldDefinition().getDisplayName();
						int fieldType=activityData.getDataFieldDefinition().getFieldType();
						boolean isArray=activityData.getDataFieldDefinition().isArrayField();				
						Object dataValue=activityData.getDatFieldValue();
						if(dataValue!=null){
							String displayString=getStepDataDisplayString(fieldType,isArray,dataValue);
							stepDataFieldStr.append("<span style='color:#ce0000;font-size:smaller;-webkit-text-size-adjust:none;white-space: nowrap;'>"+displauName+"</span>-&nbsp;<span style='color:#444444;font-size:smaller;-webkit-text-size-adjust:none;white-space: nowrap;'>"+displayString+"&nbsp;&nbsp;</span>");
							displayedFieldNumber++;						
							if(displayedFieldNumber==maxDisplayFieldNum){
								first5FieldDisplay=stepDataFieldStr.toString()+"&nbsp;...</span>";
							}						
						}
					}				
				}			
				stepDataFieldStr.append("</span>");
				
				if(stepData.length<=maxDisplayFieldNum){
					Label dataValueLabel=new Label(stepDataFieldStr.toString(),Label.CONTENT_XHTML);
					//fix content in chrom right side is trimed problem,but not fixed perfectly yet
					if(this.userClientInfo.getWebBrowser().isChrome()){				
						dataValueLabel.setWidth("350px"); 
					}			
					userTaskDataLayout.addComponent(dataValueLabel);				
				}else{
					Label dataValueLabel=new Label(first5FieldDisplay,Label.CONTENT_XHTML);
					//fix content in chrom right side is trimed problem,but not fixed perfectly yet
					if(this.userClientInfo.getWebBrowser().isChrome()){				
						dataValueLabel.setWidth("350px"); 
					}			
					userTaskDataLayout.addComponent(dataValueLabel);
					dataValueLabel.setDescription(stepDataFieldStr.toString());		       
				}
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		} catch (RepositoryException e) {			
			e.printStackTrace();
		}
		userTaskInfoLayout.addComponent(userTaskDataLayout);
		
		HorizontalLayout spaceDivLayout_2=new HorizontalLayout();
		spaceDivLayout_2.setHeight("5px");
		userTaskInfoLayout.addComponent(spaceDivLayout_2);		
		
		HorizontalLayout userTaskPropertyLayout=new HorizontalLayout();	
		userTaskPropertyLayout.setWidth("100%");		
		HorizontalLayout startDateInfoLayout=new HorizontalLayout();		
		Embedded startDateiconEmbedded=new Embedded(null, UICommonElementDefination.ICON_stepDetail_clock);
		startDateiconEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_activityCreateTimeLabel"));
		Label activityStartDateLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;font-size:smaller;-webkit-text-size-adjust:none;'>"+formatter.format(createTime)+"</span>",Label.CONTENT_XHTML);			
		startDateInfoLayout.addComponent(startDateiconEmbedded);
		startDateInfoLayout.setComponentAlignment(startDateiconEmbedded, Alignment.MIDDLE_LEFT);		
		startDateInfoLayout.addComponent(activityStartDateLabel);
		startDateInfoLayout.setComponentAlignment(activityStartDateLabel, Alignment.MIDDLE_LEFT);		
		
		userTaskPropertyLayout.addComponent(startDateInfoLayout);			
		HorizontalLayout spaceDivLayout_01=new HorizontalLayout();
		spaceDivLayout_01.setWidth("10px");		
		userTaskPropertyLayout.addComponent(spaceDivLayout_01);	
		
		HorizontalLayout roleInfoLayout=new HorizontalLayout();			
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);
		iconEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_taskRoleLabel"));
		Label roleNameLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;font-size:smaller;-webkit-text-size-adjust:none;'>"+taskRole+"</span>",Label.CONTENT_XHTML);			
		roleInfoLayout.addComponent(iconEmbedded);		
		roleInfoLayout.addComponent(roleNameLabel);		
		roleInfoLayout.setComponentAlignment(iconEmbedded, Alignment.MIDDLE_LEFT);				
		roleInfoLayout.setComponentAlignment(roleNameLabel, Alignment.MIDDLE_LEFT);			
		if(taskRole!=null){
			userTaskPropertyLayout.addComponent(roleInfoLayout);
		}
		userTaskInfoLayout.addComponent(userTaskPropertyLayout);		
		userTaskInfoLayout.setComponentAlignment(userTaskPropertyLayout, Alignment.MIDDLE_RIGHT);		
		
		HorizontalLayout spaceDivLayout_3=new HorizontalLayout();
		spaceDivLayout_3.setHeight("5px");
		userTaskInfoLayout.addComponent(spaceDivLayout_3);
		
		PropertyItem userTaskItem=new PropertyItem("BLANKBG",taskValueContainerLayout);
		return userTaskItem;
	}
	
	private void renderActivityStepDetailEditor(ActivityStep activityStep){		
		Embedded relatedRolesIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_taskDetailWindowTitle")+ "</b>", Label.CONTENT_XHTML);		
		VerticalLayout activityStepDetailEditor=StepDataEditorFactory.buildStepDataEditor(activityStep,this.userClientInfo,new ReloadableUIElement[]{this});	
		LightContentWindow lightContentWindow=new LightContentWindow(relatedRolesIcon,propertyNameLable,activityStepDetailEditor,"100%");		
		lightContentWindow.center();
		lightContentWindow.setSizeFull();
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}

	public void reloadContent() {
		renderUserTaskListUI(this.userParticipant);		
	}
	
	public void executeTaskAction(String actionType,ParticipantTask participantTask){		
		String openTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_openTaskMenuItem");
		String reassignTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_reassignTaskMenuItem");
		String returnTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_returnTaskMenuItem");
		String deleteTaskMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_deleteTaskMenuItem");
		String showCommentMenuItem=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_showCommentsMenuItem");
		
		if(actionType.equals(openTaskMenuItem)){
			renderActivityStepDetailEditor(participantTask.getActivityStep());
		}else if(actionType.equals(reassignTaskMenuItem)){			
			reassignTask(participantTask);			
		}else if(actionType.equals(returnTaskMenuItem)){			
			returnTask(participantTask);				
		}else if(actionType.equals(deleteTaskMenuItem)){
			deleteTask(participantTask);
		}else if(actionType.equals(showCommentMenuItem)){
			showComments(participantTask);
		}
	}
	
	private void showComments(ParticipantTask participantTask){				
		Role relatedRole=null;
		try {
			if(participantTask.getRoleName()!=null){
				relatedRole = activitySpace.getRole(participantTask.getRoleName());
			}			
			Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_stepDetail_comment);
			Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_commentWindowTitle")				
					+"</b> <b style='color:#ce0000;'>" + participantTask.getActivityStepName()+ "</b>", Label.CONTENT_XHTML);			
			ActivityCommentEditor activityCommentEditor=new ActivityCommentEditor(this.userClientInfo,participantTask.getActivityStep(),relatedRole);
			LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,activityCommentEditor,"810px");		
			lightContentWindow.center();
			lightContentWindow.setModal(true);
			lightContentWindow.setHeight("500px");
			this.getApplication().getMainWindow().addWindow(lightContentWindow);			
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
	
	private void doReturnTask(ParticipantTask participantTask){
		try {
			participantTask.getActivityStep().returnActivityStep();
			this.reloadContent();
			closeReturnTaskComfirmWindow();
		} catch (ActivityEngineProcessException e) {				
			e.printStackTrace();
		}		
	}
	
	private void closeReturnTaskComfirmWindow(){
		this.getApplication().getMainWindow().removeWindow(returnTaskComfirmWindow);
		returnTaskComfirmWindow=null;
	}
	
	private void returnTask(final ParticipantTask participantTask){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_returnTaskWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_returnTaskWindowDesc");        		
        Button confirmDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_returnTask_confirmButton"));
        confirmDeleteButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = 6714316137466441960L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				doReturnTask(participantTask);
				
			}
        });                
        Button cancelDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_returnTask_cancelButton"));        
        cancelDeleteButton.addListener(new Button.ClickListener() {	       
			private static final long serialVersionUID = 7637409303563626282L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				closeReturnTaskComfirmWindow();
            }
        });	                
        cancelDeleteButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmDeleteButton);
        buttonList.add(cancelDeleteButton);         
        returnTaskComfirmWindow=UIComponentCreator.createDialogWindow_DeleteData_Confirm(windowTitle, windowDesc, new BaseButtonBar(370, 45, Alignment.MIDDLE_RIGHT, buttonList));		
        returnTaskComfirmWindow.center();
        returnTaskComfirmWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(returnTaskComfirmWindow);		
	}
	
	private void doDeleteTask(ParticipantTask participantTask){		 
		 try {
			activitySpace.deleteBusinessActivityByActivityId(participantTask.getActivityStep().getActivityId(), "deletePrivateOwnedActivity");
			this.reloadContent();
			closeDeletePrivateTaskComfirmWindow();
		} catch (ActivityEngineProcessException e) {				
			e.printStackTrace();
		}
	}
	
	private void closeDeletePrivateTaskComfirmWindow(){
		this.getApplication().getMainWindow().removeWindow(deletePrivateTaskComfirmWindow);
		deletePrivateTaskComfirmWindow=null;
	}
	
	private void deleteTask(final ParticipantTask participantTask){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_deleteTaskWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_deleteTaskWindowDesc");       		
        Button confirmDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_deleteTask_confirmButton"));
        confirmDeleteButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -7125649017680763894L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				doDeleteTask(participantTask);
				
			}
        });                
        Button cancelDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_deleteTask_cancelButton"));        
        cancelDeleteButton.addListener(new Button.ClickListener() {	   		
			private static final long serialVersionUID = -7435949531443796860L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				closeDeletePrivateTaskComfirmWindow();
            }
        });	                
        cancelDeleteButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmDeleteButton);
        buttonList.add(cancelDeleteButton);         
        deletePrivateTaskComfirmWindow=UIComponentCreator.createDialogWindow_DeleteData_Confirm(windowTitle, windowDesc, new BaseButtonBar(370, 45, Alignment.MIDDLE_RIGHT, buttonList));		
        deletePrivateTaskComfirmWindow.center();
        deletePrivateTaskComfirmWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(deletePrivateTaskComfirmWindow);		
	}	
	
	private void doReassignTask(ParticipantTask participantTask,String newAssigner){
		try {
			participantTask.getActivityStep().reassignActivityStep(newAssigner);
			this.reloadContent();
			closeReassignTaskOperationWindow();
		} catch (ActivityEngineProcessException e) {				
			e.printStackTrace();
		}		
	}
	
	private void closeReassignTaskOperationWindow(){
		this.getApplication().getMainWindow().removeWindow(reassignTaskOperationWindow);
		reassignTaskOperationWindow=null;		
	}
	
	private void reassignTask(final ParticipantTask participantTask){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_reassignTaskWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_reassignTaskWindowDesc");
		try {
			String roleName=participantTask.getRoleName();					
			Participant[] participantArray=activitySpace.getRole(roleName).getParticipants();			
			VerticalLayout reassignOperationLayout = new VerticalLayout();			
			HorizontalLayout dropdownListLayout=new HorizontalLayout();	
			HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
			leftSpaceLayout.setWidth("20px");
			dropdownListLayout.addComponent(leftSpaceLayout);
			final ComboBox participantChoise = new ComboBox();
			participantChoise.setNullSelectionAllowed(false);			
			participantChoise.setWidth("250px");
			participantChoise.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_reassignTaskSelectuserPrompt"));			
			participantChoise.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);			
			dropdownListLayout.addComponent(participantChoise);
			IndexedContainer participantContainer = new IndexedContainer();
			participantContainer.addContainerProperty("PARTICIPANT_DISPLAYNAME", String.class, null);
			participantContainer.addContainerProperty("PARTICIPANT", Participant.class, null);						
			for(int i=0;i<participantArray.length;i++){				
				if(!participantArray[i].getParticipantName().equals(this.userClientInfo.getUserParticipant().getParticipantName())){
					Item item = participantContainer.addItem(""+i);	
					item.getItemProperty("PARTICIPANT_DISPLAYNAME").setValue(participantArray[i].getDisplayName());
			        item.getItemProperty("PARTICIPANT").setValue(participantArray[i]);
				}					
			}
			participantChoise.setContainerDataSource(participantContainer);
	        participantChoise.setItemCaptionPropertyId("PARTICIPANT_DISPLAYNAME");
			participantChoise.setEnabled(true);			
			reassignOperationLayout.addComponent(dropdownListLayout);
			
			Button confirmDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_reassignTask_confirmButton"));
		    confirmDeleteButton.addListener(new Button.ClickListener() {			    	
				private static final long serialVersionUID = 7329508329732094604L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
					if(participantChoise.getValue()==null){
						return;
					}
					IndexedContainer participantContainer =(IndexedContainer) participantChoise.getContainerDataSource();
					Item selectedItem=participantContainer.getItem(participantChoise.getValue().toString());				
					Participant selectedParticipant=(Participant)selectedItem.getItemProperty("PARTICIPANT").getValue();
					doReassignTask(participantTask,selectedParticipant.getParticipantName());						
				}
		     });  
		    
		    Button cancelDeleteButton = new Button(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskList_reassignTask_cancelButton"));        
		    cancelDeleteButton.addListener(new Button.ClickListener() {	
				private static final long serialVersionUID = 5467388553064866404L;

				public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
					closeReassignTaskOperationWindow();
		           }
		     });	
		    cancelDeleteButton.setStyleName(BaseTheme.BUTTON_LINK);
	        List<Button> buttonList = new ArrayList<Button>();
	        buttonList.add(confirmDeleteButton);
	        buttonList.add(cancelDeleteButton); 
	        reassignOperationLayout.addComponent(new BaseButtonBar(370, 45, Alignment.MIDDLE_RIGHT, buttonList));
	        reassignTaskOperationWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, reassignOperationLayout);
	        reassignTaskOperationWindow.center();
	        reassignTaskOperationWindow.setModal(true);
			this.getApplication().getMainWindow().addWindow(reassignTaskOperationWindow);		     
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
	
	private String getStepDataDisplayString(int fieldType,boolean isArray,Object dataValue) throws RepositoryException{		
		String valueDisplay=null;
		switch(fieldType){
			case PropertyType.BINARY:
				if(isArray){
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_Binary")+"["+((Object[])dataValue).length+"]";
				}else{
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_BinarySize")+"("+((Binary)dataValue).getSize()+")";
				}										
				break;
			case PropertyType.BOOLEAN:
				if(isArray){
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_Boolean")+"["+((boolean[])dataValue).length+"]";
				}else{
					valueDisplay=""+((Boolean)dataValue).booleanValue();
				}						
				break;
			case PropertyType.DATE:	
				if(isArray){
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_Date")+"["+((Object[])dataValue).length+"]";
				}else{
					valueDisplay=formatter.format(((Calendar)dataValue).getTime());
				}						
				break;
			case PropertyType.DECIMAL:
				if(isArray){
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_Decimal")+"["+((Object[])dataValue).length+"]";
				}else{
					valueDisplay=""+((BigDecimal)dataValue).doubleValue();
				}						
				break;
			case PropertyType.DOUBLE:				
				if(isArray){
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_Double")+"["+((double[])dataValue).length+"]";
				}else{
					valueDisplay=""+((Double)dataValue).doubleValue();
				}	
				break;
			case PropertyType.LONG:
				if(isArray){
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_Long")+"["+((long[])dataValue).length+"]";
				}else{
					valueDisplay=""+((Long)dataValue).longValue();
				}						
				break;
			case PropertyType.STRING:
				if(isArray){
					valueDisplay=this.userClientInfo.getI18NProperties().getProperty("UserClient_UserTaskListDataType_String")+"["+((Object[])dataValue).length+"]";
				}else{
					valueDisplay=dataValue.toString();
				}						
				break;
		}
		return valueDisplay;
	}
}