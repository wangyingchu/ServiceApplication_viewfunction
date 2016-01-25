package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.MainTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ParticipantUI extends VerticalLayout{	
	private static final long serialVersionUID = 5878963289087963569L;

	private Participant participant;
	private UserClientInfo userClientInfo;
	private VerticalLayout participantTasksUIContainer;
	private DialogWindow editParticipantWindow;
	private ParticipantPropertyList participantPropertyList;
	public ParticipantUI(final Participant participant,UserClientInfo userClientInfo,ActivityObjectDetail activityObjectDetail){
		this.participant=participant;
		this.userClientInfo=userClientInfo;
		//render title bar		
		Button parentObjectLinkButton=new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_participantsLabel"));
		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);		
		parentObjectLinkButton.addListener(new ClickListener(){ 			
			private static final long serialVersionUID = -3605756437918727779L;

			public void buttonClick(ClickEvent event) {				
				renderParticipantsUI(participant.getActivitySpaceName());
			}}); 		
		
		MainTitleBar mainTitleBar=new MainTitleBar(participant.getActivitySpaceName(),parentObjectLinkButton,participant.getParticipantName());
		this.addComponent(mainTitleBar);		
		
		//render Participant data		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);				
				
		// header part		
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceParticipantIcon);
		iconEmbedded.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_object_participantLabel"));
		
		HorizontalLayout participantActionbarContainer=new HorizontalLayout();			
		participantActionbarContainer.setWidth("30px");		
		Button editParticipantButton=new Button();
		editParticipantButton.setCaption(null);
		editParticipantButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		editParticipantButton.setStyleName(BaseTheme.BUTTON_LINK);
		editParticipantButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_editParticipantDataLabel"));
		editParticipantButton.addListener(new ClickListener(){		
			private static final long serialVersionUID = -863201212192177575L;

			public void buttonClick(ClickEvent event) {							
				renderEditParticipantFormUI();
			}});		
		participantActionbarContainer.addComponent(editParticipantButton);		
		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,participant.getParticipantName(),SectionTitleBar.BIGFONT,participantActionbarContainer);
		containerPanel.addComponent(sectionTitleBar);
		
		//participant detail		
		participantPropertyList=new ParticipantPropertyList(participant,userClientInfo);
		containerPanel.addComponent(participantPropertyList);
		
		HorizontalLayout divHorizontalLayout=new HorizontalLayout();
		divHorizontalLayout.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout);		
		
		Embedded tasksIconEmbedded=new Embedded(null, UICommonElementDefination.ICON_Button_participant_fetchTasks_light);			
		HorizontalLayout participantTasksActionbarContainer=new HorizontalLayout();			
		participantTasksActionbarContainer.setWidth("30px");		
		Button fetchParticipantTasksButton=new Button();
		fetchParticipantTasksButton.setCaption(null);
		fetchParticipantTasksButton.setIcon(UICommonElementDefination.Appaction_fetchDataIcon);
		fetchParticipantTasksButton.setStyleName(BaseTheme.BUTTON_LINK);
		fetchParticipantTasksButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsButton_fetchTasksLabel"));		
		participantTasksActionbarContainer.addComponent(fetchParticipantTasksButton);
		
		fetchParticipantTasksButton.addListener(new ClickListener(){            		
			private static final long serialVersionUID = -3567064194516804327L;

			public void buttonClick(ClickEvent event) {							
				renderParticipantTasksUI();
			}});		
		
		SectionTitleBar tasksSectionTitleBar=new SectionTitleBar(tasksIconEmbedded,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_participantTasksLabel"),SectionTitleBar.MIDDLEFONT,participantTasksActionbarContainer);		
		containerPanel.addComponent(tasksSectionTitleBar);
		
		participantTasksUIContainer=new VerticalLayout();
		containerPanel.addComponent(participantTasksUIContainer);		
	}
	
	public void renderParticipantsUI(String activitySpaceName){
		ActivityObjectDetail activityObjectDetail =(ActivityObjectDetail)(this.getParent().getParent());
		activityObjectDetail.renderParticipantsUI(activitySpaceName);
	}
	
	public void renderParticipantTasksUI(){		
		participantTasksUIContainer.removeAllComponents();
		ParticipantTasksTable participantTasksTable=new ParticipantTasksTable(participant,userClientInfo);
		participantTasksUIContainer.addComponent(participantTasksTable);		
	}
	
	private void renderEditParticipantFormUI(){	
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm editParticipantDataForm=new BaseForm();			
		final TextField participantNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantNameField"));		
		participantNameField.setValue(this.participant.getParticipantName());
		participantNameField.setEnabled(false);
		participantNameField.setWidth("250px");
		participantNameField.setRequired(true);	
		editParticipantDataForm.addField("participantName", participantNameField);
		
		final TextField participantDisplayNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantDisNameField"));		
		participantDisplayNameField.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantDisNameFieldDesc"));
		participantDisplayNameField.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantDisNameFieldErrM"));
		participantDisplayNameField.setValue(this.participant.getDisplayName());
		participantDisplayNameField.setWidth("250px");
		participantDisplayNameField.setRequired(true);		
		editParticipantDataForm.addField("participantDisplayName", participantDisplayNameField);		
		
		final ComboBox participantTypeChoise = new ComboBox(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantTypeField"));	
		participantTypeChoise.setNullSelectionAllowed(false);
		participantTypeChoise.setWidth("250px");
		participantTypeChoise.setRequired(true);		
		editParticipantDataForm.addField("participantType", participantTypeChoise);		
		participantTypeChoise.addItem("USER");
		participantTypeChoise.addItem("GROUP");		
		if(this.participant.isGroup()){
			participantTypeChoise.select("GROUP");			
		}else{
			participantTypeChoise.select("USER");	
		}
		
		final OptionGroup roleSelect = new OptionGroup(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_EditParticipantRoleField"));		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.participant.getActivitySpaceName());
		try {
			Role[] activitySpaceRoleArray=activitySpace.getRoles();				
			List<String> spaceRoleList=new ArrayList<String>();
			if(spaceRoleList!=null){
				if(activitySpaceRoleArray!=null){
					for(Role role:activitySpaceRoleArray){				
						spaceRoleList.add(role.getRoleName());
						roleSelect.addItem(role.getRoleName());
					}
				}				
			}								
			roleSelect.setMultiSelect(true);
			roleSelect.setNullSelectionAllowed(true);			
			Role[] currentRolesArray=this.participant.getRoles();	
			if(currentRolesArray!=null){
				for(Role currentRole:currentRolesArray){				
					roleSelect.select(currentRole.getRoleName());				
				}				
			}			
			editParticipantDataForm.addField("participantRoles", roleSelect);			
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		}
	
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_EditParticipant_confirmEditButtonLabel"));
			
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -7220414433731041237L;

			public void buttonClick(ClickEvent event) {					
				editParticipantDataForm.setValidationVisible(true);
				editParticipantDataForm.setComponentError(null);
	            boolean validateResult = editParticipantDataForm.isValid();		            
	            if(validateResult){
	            	editParticipantDataForm.setComponentError(null);
					String newParticipantDisplayName=participantDisplayNameField.getValue().toString();
					String newParticipantType=participantTypeChoise.getValue().toString();
					Set newRoleS=(Set)roleSelect.getValue();					
					doEditParticipant(newParticipantDisplayName,newParticipantType,newRoleS);
	            }	            
			}				
		});
			
		Button cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_EditParticipant_cancelEditButtonLabel"),new Button.ClickListener() {
			private static final long serialVersionUID = 5074697503899035203L;

			public void buttonClick(ClickEvent event) {               
	            	removeEditPartipantWindow();
	            }
	        });
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		buttonList.add(okButton);
		buttonList.add(cancelAddbutton);
		BaseButtonBar editParticipantButtonBar = new BaseButtonBar(280, 30, Alignment.BOTTOM_LEFT, buttonList);
		editParticipantDataForm.getLayout().addComponent(editParticipantButtonBar);

		windowContent.addComponent(editParticipantDataForm);
		String windowTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_EditParticipant_windowTitle");
		String subTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_EditParticipant_windowDesc")+" <span style='color:#ce0000; font-weight:bold;'>"+this.participant.getParticipantName()+"</span>";
		editParticipantWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(editParticipantWindow);
	}
	
	private void removeEditPartipantWindow(){
		this.getApplication().getMainWindow().removeWindow(editParticipantWindow);
		editParticipantWindow=null;
	}
	
	private void doEditParticipant(String newDisplayName,String newType,Set newRoles){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.participant.getActivitySpaceName());		
		String[] newRoleArray=new String[newRoles.size()];
		newRoles.toArray(newRoleArray);
		String newPartipicantType=null;
		if(newType.equals("GROUP")){
			newPartipicantType=Participant.PARTICIPANT_TYPE_GROUP;			
		}else{
			newPartipicantType=Participant.PARTICIPANT_TYPE_USER;
		}		
		try {
			Participant newPartipicant=activitySpace.updateParticipant(this.participant.getParticipantName(), newDisplayName, newPartipicantType, newRoleArray);		
			this.participant=newPartipicant;
			participantPropertyList.reloadContent(this.participant);
			this.userClientInfo.getEventBlackboard().fire(new ParticipantsChangeEvent(this.participant.getActivitySpaceName()));
			String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_EditParticipant_SussMsg1")+" "+this.participant.getParticipantName()+"  "+userClientInfo.getI18NProperties().getProperty("ActivityManage_participantUI_EditParticipant_SussMsg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
			removeEditPartipantWindow();			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
}