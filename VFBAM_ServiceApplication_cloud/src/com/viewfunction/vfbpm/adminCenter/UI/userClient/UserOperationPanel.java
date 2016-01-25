package com.viewfunction.vfbpm.adminCenter.UI.userClient;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.Button.ClickEvent;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityBureauImpl.CCRActivityEngineConstant;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.BasicBusinessActivityQueryEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.CommonDocumentsEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.FinishedActivityStepTable;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.StartNewActivityEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueueElementsTable;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class UserOperationPanel extends VerticalLayout{
	private static final long serialVersionUID = 1087658358519577213L;
	private String activitySpaceName;
	private Participant userParticipant;
	private UserClientInfo userClientInfo;
	public UserOperationPanel(UserClientInfo userClientInfo){
		this.activitySpaceName=userClientInfo.getUserActivitySpace();
		this.userParticipant=userClientInfo.getUserParticipant();
		this.userClientInfo=userClientInfo;
		ActivitySpace userActivitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		MenuBar contentOperationMenubar = new MenuBar();
		contentOperationMenubar.setWidth("100%");
		MenuBar.MenuItem activityOperationItems = contentOperationMenubar.addItem(
				this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_startNewActivityMenu"),UICommonElementDefination.AppMenu_operationMenuIcon, null);
		try {
			BusinessActivityDefinition[] businessActivityDefinitions=userActivitySpace.getBusinessActivityDefinitions();
			for(final BusinessActivityDefinition businessActivityDefinition:businessActivityDefinitions){
				MenuItem startActivityItem=activityOperationItems.addItem(businessActivityDefinition.getActivityType()+"...",new Command() {		           
					private static final long serialVersionUID = 6670739033428321090L;

					public void menuSelected(MenuItem selectedItem) {
						renderStartNewActivityUI(businessActivityDefinition);
		            }
		        });					
				startActivityItem.setIcon(UICommonElementDefination.AppMenu_createSpaceMenuIcon);					
			}			
		} catch (ActivityEngineRuntimeException e1) {		
			e1.printStackTrace();
		} catch (ActivityEngineActivityException e1) {			
			e1.printStackTrace();
		} catch (ActivityEngineDataException e1) {			
			e1.printStackTrace();
		}
		
		MenuBar.MenuItem processOperationItems = contentOperationMenubar.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_activityActionMenu"),UICommonElementDefination.AppMenu_activityActionsMenuIcon, null);
		MenuItem queryIstartedActivityItem=processOperationItems.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_queryICreatedActivityMenuItem"),null);
		queryIstartedActivityItem.setIcon(UICommonElementDefination.ICON_userClient_Activities);
		MenuItem queryInvolvedActivityItem=processOperationItems.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_queryIFinishedTaskMenuItem"),null);
		queryInvolvedActivityItem.setIcon(UICommonElementDefination.ICON_userClient_Activities);
		MenuItem advancedSearchActivityItem=processOperationItems.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_activitySearchMenu"),null);	
		advancedSearchActivityItem.setIcon(UICommonElementDefination.ICON_userClient_Search);		
		
		queryIstartedActivityItem.setCommand(new Command(){
			private static final long serialVersionUID = 409165263460586554L;

			public void menuSelected(MenuItem selectedItem) {
				renderActivityQueryEditorUI();				
			}});
		
		queryInvolvedActivityItem.setCommand(new Command(){
			private static final long serialVersionUID = 1941853004309116723L;

			public void menuSelected(MenuItem selectedItem) {
				renderActivityStepQueryEditorUI();				
			}});		
		
		MenuBar.MenuItem contentOperationItems = contentOperationMenubar.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_contentActionMenu"),UICommonElementDefination.AppMenu_contentActionsMenuIcon, null);	
		MenuItem myDocumentsItem=contentOperationItems.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_myDocumentMenuItem"),null);
		myDocumentsItem.setCommand(new Command(){			
			private static final long serialVersionUID = 409165263460586554L;

			public void menuSelected(MenuItem selectedItem) {
				renderParticipantDocmentEditorUI();				
			}});	
		
		myDocumentsItem.setIcon(UICommonElementDefination.ICON_userClient_Documents);
		MenuItem teamDocumentsItem=contentOperationItems.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_teamDocumentMenuItem"),null);		
		try {
			Role[] invoRole=this.userParticipant.getRoles();	
			if(invoRole!=null){
				for(final Role role:invoRole){				
					if(role.getDisplayName()!=null){
						teamDocumentsItem.addItem(role.getDisplayName(), new Command() { 
							private static final long serialVersionUID = 2418335410585689321L;

							public void menuSelected(MenuItem selectedItem) {
								renderRoleDocmentEditorUI(role.getRoleName(),role.getDisplayName());
				            }
				        });
					}else{
						teamDocumentsItem.addItem(role.getRoleName(), new Command() { 				
							private static final long serialVersionUID = 8327986777752402802L;

							public void menuSelected(MenuItem selectedItem) {
								renderRoleDocmentEditorUI(role.getRoleName(),role.getDisplayName());
				            }
				        });
					}			
				}			
			}			
		} catch (ActivityEngineRuntimeException e1) {			
			e1.printStackTrace();
		}		
		
		teamDocumentsItem.setIcon(UICommonElementDefination.ICON_userClient_Documents);
		MenuItem spaceDocumentsItem=contentOperationItems.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_spaceDocumentMenuItem"),null);
		spaceDocumentsItem.setIcon(UICommonElementDefination.ICON_userClient_Documents);		
		spaceDocumentsItem.setCommand(new Command(){
			private static final long serialVersionUID = 409165263460586554L;

			public void menuSelected(MenuItem selectedItem) {
				renderSpaceDocmentEditorUI();				
			}});		
		
		MenuItem searchDocumentsItem=contentOperationItems.addItem(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_searchDocumentMenuItem"),null);
		searchDocumentsItem.setIcon(UICommonElementDefination.ICON_userClient_Search);
		
		this.addComponent(contentOperationMenubar);	
		
		Panel contentPanel=new Panel();	
		contentPanel.setSizeFull();		
		this.addComponent(contentPanel);		
		this.setExpandRatio(contentPanel, 1.0F);
		try {				
			HorizontalLayout userComponentsContainerLayout=new HorizontalLayout();	
			userComponentsContainerLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_UserLogin);		
			userComponentsContainerLayout.setWidth("100%");
			contentPanel.addComponent(userComponentsContainerLayout);				
			VerticalLayout userTaskListLayout=new VerticalLayout();
			
			userTaskListLayout.addStyleName("ui_userClient_middleDiv");
			
			userTaskListLayout.setWidth("450px");
			userTaskListLayout.setHeight("100%");			
			userComponentsContainerLayout.addComponent(userTaskListLayout);	
			
			HorizontalLayout userTaskLabelLayout=new HorizontalLayout();			
			userTaskLabelLayout.setWidth("450px");			
			userTaskLabelLayout.setStyleName(UICommonElementDefination.LIGHT_PROPERTYPANEL_TITLE_STYLE);
			
			HorizontalLayout userTaskLabelContainerLayout=new HorizontalLayout();			
			Embedded participantTasksIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_fetchTasks);
			userTaskLabelContainerLayout.addComponent(participantTasksIcon);			
			Label userTaskLabel=new Label("<span style='color:#222222;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+
					this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_myWorkingTaskListLable")+"</span>",Label.CONTENT_XHTML);
			userTaskLabelContainerLayout.addComponent(userTaskLabel);
			userTaskLabelLayout.addComponent(userTaskLabelContainerLayout);		
			
			final UserTaskList userTaskList=new UserTaskList(userParticipant,this.userClientInfo);			
			HorizontalLayout taskActionButtonLayout=new HorizontalLayout();
			taskActionButtonLayout.setWidth("20px");					
			Button refreshTaskContentButton=new Button();		
			refreshTaskContentButton.setCaption(null);
			refreshTaskContentButton.setIcon(UICommonElementDefination.ICON_userClient_ReloadContent);
			refreshTaskContentButton.setStyleName(BaseTheme.BUTTON_LINK);
			refreshTaskContentButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_reloadTaskListButton"));			
			refreshTaskContentButton.addListener(new Button.ClickListener() {			
				private static final long serialVersionUID = -1444469487408546353L;

				public void buttonClick(ClickEvent event) {	 
					userTaskList.reloadContent();
	            }
	        });		
			taskActionButtonLayout.addComponent(refreshTaskContentButton);
			userTaskLabelLayout.addComponent(taskActionButtonLayout);			
			userTaskLabelLayout.setComponentAlignment(taskActionButtonLayout, Alignment.MIDDLE_RIGHT);				
			userTaskListLayout.addComponent(userTaskLabelLayout);	
			
			userTaskListLayout.addComponent(userTaskList);				
			
			VerticalLayout groupTasksListLayout=new VerticalLayout();			
			groupTasksListLayout.addStyleName("ui_userClient_right");			
			
			groupTasksListLayout.setHeight("100%");		
			userComponentsContainerLayout.addComponent(groupTasksListLayout);	
			userComponentsContainerLayout.setExpandRatio(groupTasksListLayout, 1.0F);
			HorizontalLayout groupTaskLabelLayout=new HorizontalLayout();			
			groupTaskLabelLayout.setWidth("100%");			
			groupTaskLabelLayout.setStyleName(UICommonElementDefination.LIGHT_PROPERTYPANEL_TITLE_STYLE);
			
			HorizontalLayout groupTaskLabelTxtLayout=new HorizontalLayout();
			Embedded groupTasksIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_fetchTasks);
			groupTaskLabelTxtLayout.addComponent(groupTasksIcon);			
			Label groupTaskLabel=new Label("<span style='color:#222222;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+
					this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_teamsTaskListLable")+"</span>",Label.CONTENT_XHTML);
			groupTaskLabelTxtLayout.addComponent(groupTaskLabel);
			groupTaskLabelLayout.addComponent(groupTaskLabelTxtLayout);	
			groupTasksListLayout.addComponent(groupTaskLabelLayout);				
			
			Role[] currentUserRoleArray=userParticipant.getRoles();	
			if(currentUserRoleArray==null){
				return;
			}
			
			List<ReloadableUIElement> refreshUIElementsList=new ArrayList<ReloadableUIElement>();
			refreshUIElementsList.add(userTaskList);
			
			for(Role role:currentUserRoleArray){
				RoleQueue[] roleQueueArray=role.getRelatedRoleQueues();
				if(roleQueueArray!=null){
					for(RoleQueue roleQueue:roleQueueArray){
						final RoleQueueElementsTable roleQueueElementsTable=new RoleQueueElementsTable(roleQueue,userClientInfo,new ReloadableUIElement[]{userTaskList});
						refreshUIElementsList.add(roleQueueElementsTable);
						HorizontalLayout roleQueueNameLayout=new HorizontalLayout();					
						roleQueueNameLayout.setHeight("30px");	
						roleQueueNameLayout.setWidth("100%");
						HorizontalLayout roleQueueNameLabelLayout=new HorizontalLayout();					
						Embedded roleQueueIcon=new Embedded(null, UICommonElementDefination.ICON_userClient_RoleQueue);
						roleQueueNameLabelLayout.addComponent(roleQueueIcon);
						Label queueName=new Label("<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;'>"+roleQueue.getDisplayName()+" : "+"</span>",Label.CONTENT_XHTML);
						roleQueueNameLabelLayout.addComponent(queueName);
						roleQueueNameLayout.addComponent(roleQueueNameLabelLayout);
						roleQueueNameLayout.setComponentAlignment(roleQueueNameLabelLayout, Alignment.MIDDLE_LEFT);						
						HorizontalLayout actionButtonLayout=new HorizontalLayout();
						actionButtonLayout.setWidth("50px");					
						Button refreshContentButton=new Button();		
						refreshContentButton.setCaption(null);
						refreshContentButton.setIcon(UICommonElementDefination.ICON_userClient_ReloadContent);
						refreshContentButton.setStyleName(BaseTheme.BUTTON_LINK);
						refreshContentButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_reloadRoleQueueButton"));	
						refreshContentButton.addListener(new Button.ClickListener() {					
							private static final long serialVersionUID = -1090048585299120019L;
	
							public void buttonClick(ClickEvent event) {	 
								roleQueueElementsTable.reloadContent();
				            }
				        });							
						actionButtonLayout.addComponent(refreshContentButton);
						roleQueueNameLayout.addComponent(actionButtonLayout);
						roleQueueNameLayout.setComponentAlignment(actionButtonLayout, Alignment.MIDDLE_RIGHT);					
						groupTasksListLayout.addComponent(roleQueueNameLayout);					
						groupTasksListLayout.addComponent(roleQueueElementsTable);						
						HorizontalLayout spaceDivLayout=new HorizontalLayout();	
						spaceDivLayout.setHeight("10px");
						groupTasksListLayout.addComponent(spaceDivLayout);						
					}	
				}
			}
			
			this.userClientInfo.setRefreshUIElementsList(refreshUIElementsList);			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
	
	private void renderStartNewActivityUI(BusinessActivityDefinition businessActivityDefinition){		
		StartNewActivityEditor startNewActivityForm=new StartNewActivityEditor(businessActivityDefinition,userClientInfo);	
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_startNewActivityWindowTitle")+" "				
				+"</b> <b style='color:#ce0000;'>" + businessActivityDefinition.getActivityType()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,startNewActivityForm,"1000px");		
		lightContentWindow.center();		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderSpaceDocmentEditorUI(){		
		String documentFolderParentABSPath="/"+CCRActivityEngineConstant.ACTIVITYSPACE_ContentStore;		
		CommonDocumentsEditor commonDocumentsEditor=new CommonDocumentsEditor(this.activitySpaceName,documentFolderParentABSPath,
				CCRActivityEngineConstant.ACTIVITYSPACE_SpaceContentStore,this.userClientInfo);
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_spaceDocumentWindowTitle")+" "				
				+"</b> <b style='color:#ce0000;'>" + this.activitySpaceName+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,commonDocumentsEditor,null);		
		lightContentWindow.center();	
		lightContentWindow.setSizeFull();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderParticipantDocmentEditorUI(){
		String documentFolderABSPath="/"+CCRActivityEngineConstant.ACTIVITYSPACE_ContentStore+"/"+CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantContentStore;				
		CommonDocumentsEditor commonDocumentsEditor=new CommonDocumentsEditor(this.activitySpaceName,documentFolderABSPath,
				this.userParticipant.getParticipantName(),this.userClientInfo);
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_userDocumentWindowTitle")+" "				
				+"</b> <b style='color:#ce0000;'>" + this.userParticipant.getDisplayName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,commonDocumentsEditor,null);		
		lightContentWindow.center();	
		lightContentWindow.setSizeFull();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderRoleDocmentEditorUI(String roleName,String roleDisplayName){
		String documentFolderABSPath="/"+CCRActivityEngineConstant.ACTIVITYSPACE_ContentStore+"/"+CCRActivityEngineConstant.ACTIVITYSPACE_RoleContentStore;		
		CommonDocumentsEditor commonDocumentsEditor=new CommonDocumentsEditor(this.activitySpaceName,documentFolderABSPath,roleName,this.userClientInfo);
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_roleDocumentWindowTitle")+" "				
				+"</b> <b style='color:#ce0000;'>" + roleDisplayName+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,commonDocumentsEditor,null);		
		lightContentWindow.center();	
		lightContentWindow.setSizeFull();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderActivityQueryEditorUI(){		
		BasicBusinessActivityQueryEditor basicBusinessActivityQueryEditor=new BasicBusinessActivityQueryEditor(this.userClientInfo);
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_activityQuerytWindowTitle")+
				" "				
				+"</b> <b style='color:#ce0000;'>" + this.userClientInfo.getUserParticipant().getDisplayName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,basicBusinessActivityQueryEditor,null);		
		lightContentWindow.center();	
		lightContentWindow.setSizeFull();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
	
	private void renderActivityStepQueryEditorUI(){
		FinishedActivityStepTable finishedActivityStepTable=new FinishedActivityStepTable(this.userClientInfo);
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("UserClient_UserOperationPanel_stepQuerytWindowTitle")+
				" "				
				+"</b> <b style='color:#ce0000;'>" + this.userClientInfo.getUserParticipant().getDisplayName()+ "</b>", Label.CONTENT_XHTML);		
		LightContentWindow lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,finishedActivityStepTable,null);		
		lightContentWindow.center();	
		lightContentWindow.setSizeFull();
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
}