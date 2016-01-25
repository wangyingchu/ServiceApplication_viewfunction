package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
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

public class ParticipantsUI extends VerticalLayout{
	private static final long serialVersionUID = 5409617177798490597L;
	private UserClientInfo userClientInfo;
	private String activitySpaceName;
	private DialogWindow addParticipantWindow;
	private IndexedContainer roleContainer;
	private ParticipantsTable participantsTable;
	
	public ParticipantsUI(Participant[] participantArray,UserClientInfo userClientInfo,ActivityObjectDetail activityObjectDetail,String activitySpaceName){
		this.userClientInfo=userClientInfo;		
		this.activitySpaceName=activitySpaceName;
		//render title bar		
		MainTitleBar mainTitleBar=new MainTitleBar(this.activitySpaceName,userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_participantsLabel"));	
		this.addComponent(mainTitleBar);		
		//render Participants list		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);					
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceParticipantTitleIcon);	
		
		HorizontalLayout participantsActionbarContainer=new HorizontalLayout();			
		participantsActionbarContainer.setWidth("30px");		
		Button addNewParticipantButton=new Button();
		addNewParticipantButton.setCaption(null);
		addNewParticipantButton.setIcon(UICommonElementDefination.ICON_addNewParticipant);
		addNewParticipantButton.setStyleName(BaseTheme.BUTTON_LINK);
		addNewParticipantButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsButton_addNewParticipantLabel"));		
		participantsActionbarContainer.addComponent(addNewParticipantButton);
		
		addNewParticipantButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -7642694392724096773L;

			public void buttonClick(ClickEvent event) {							
				renderAddParticipantFormUI();
			}});		
		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_participantsLabel"),SectionTitleBar.MIDDLEFONT,participantsActionbarContainer);		
		containerPanel.addComponent(sectionTitleBar);
		participantsTable=new ParticipantsTable(participantArray,userClientInfo,activityObjectDetail);		
		containerPanel.addComponent(participantsTable);		
		this.userClientInfo.getEventBlackboard().addListener(participantsTable);
		activityObjectDetail.participantsChangeListenerList.add(participantsTable);
	}
	
	public String getActivitySpaceName(){
		return this.activitySpaceName;
	}
	
	private void renderAddParticipantFormUI(){	
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm newParticipantDataForm=new BaseForm();			
		final TextField participantNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantNameField"));		
		participantNameField.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantNameFieldDesc"));
		participantNameField.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantNameFieldErrM"));
		participantNameField.setWidth("250px");
		participantNameField.setRequired(true);		
		newParticipantDataForm.addField("participantName", participantNameField);
		
		final TextField participantDisplayNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantDisNameField"));		
		participantDisplayNameField.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantDisNameFieldDesc"));
		participantDisplayNameField.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantDisNameFieldErrM"));
		participantDisplayNameField.setWidth("250px");
		participantDisplayNameField.setRequired(true);		
		newParticipantDataForm.addField("participantDisplayName", participantDisplayNameField);		
		
		final ComboBox participantTypeChoise = new ComboBox(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantTypeField"));	
		participantTypeChoise.setNullSelectionAllowed(false);
		participantTypeChoise.setWidth("250px");
		participantTypeChoise.setRequired(true);
		participantTypeChoise.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantTypeFieldErrM"));
		participantTypeChoise.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantTypeFieldDesc"));		
		newParticipantDataForm.addField("participantType", participantTypeChoise);		
		participantTypeChoise.addItem("USER");
		participantTypeChoise.addItem("GROUP");
		
		final ComboBox relatedRoleChoise = new ComboBox(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantRoleField"));	
		relatedRoleChoise.setNullSelectionAllowed(false);
		relatedRoleChoise.setWidth("250px");
		relatedRoleChoise.setRequired(false);	
		relatedRoleChoise.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantRoleFieldDesc"));		
		newParticipantDataForm.addField("participantRole", relatedRoleChoise);
		
		final ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		try {
			Role[] roleArray=activitySpace.getRoles();			
			roleContainer = new IndexedContainer();
			roleContainer.addContainerProperty("ROLE_DISPLAYNAME", String.class, null);
			roleContainer.addContainerProperty("ROLENAME", String.class, null);				
			Item item0 = roleContainer.addItem("EMPTYSELECTION");
			item0.getItemProperty("ROLE_DISPLAYNAME").setValue(" ");
			item0.getItemProperty("ROLENAME").setValue(" ");
			if(roleArray!=null){
				for(int i=0;i<roleArray.length;i++){
					Item item = roleContainer.addItem(""+i);						
			        item.getItemProperty("ROLE_DISPLAYNAME").setValue(roleArray[i].getDisplayName());
			        item.getItemProperty("ROLENAME").setValue(roleArray[i].getRoleName());				       						
				}	
			}			
			relatedRoleChoise.setContainerDataSource(roleContainer);
			relatedRoleChoise.setItemCaptionPropertyId("ROLE_DISPLAYNAME");
			
			List<Button> buttonList = new ArrayList<Button>();			
			Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_AddParticipant_confirmAddButtonLabel"));
			
			  okButton.addListener(new Button.ClickListener() {
				private static final long serialVersionUID = -1689764977919967117L;

				public void buttonClick(ClickEvent event) {
					newParticipantDataForm.setValidationVisible(true);
					newParticipantDataForm.setComponentError(null);
		            boolean validateResult = newParticipantDataForm.isValid();		            
		            if(validateResult){		            	
		            	String newParticipantName=participantNameField.getValue().toString();		            	
		            	try {
		            		Participant targetParticipant=activitySpace.getParticipant(newParticipantName);							
							if(targetParticipant!=null){
								newParticipantDataForm.setComponentError(new UserError(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_addNewParticipantDeupErrM")));
								return;								
							}else{
								newParticipantDataForm.setComponentError(null);
								String newParticipantDisplayName=participantDisplayNameField.getValue().toString();
								String participantType=participantTypeChoise.getValue().toString();
								String selectedRoleId=relatedRoleChoise.getValue()!=null?relatedRoleChoise.getValue().toString():null;								
								doAddNewParticipant(newParticipantName,newParticipantDisplayName,participantType,selectedRoleId);								
							}							
						} catch (ActivityEngineRuntimeException e) {							
							e.printStackTrace();
						}		            	
		            }
				}
			  });
			
		    Button cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_AddParticipant_cancelAddButtonLabel"),new Button.ClickListener() {
				private static final long serialVersionUID = -1689764977919967117L;

				public void buttonClick(ClickEvent event) {               
	            	removeAddPartipantWindow();
	            }
	        });
		    cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		    buttonList.add(okButton);
		    buttonList.add(cancelAddbutton);
		    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(280, 30, Alignment.BOTTOM_LEFT, buttonList);
		    newParticipantDataForm.getLayout().addComponent(addParticipantButtonBar);
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
		windowContent.addComponent(newParticipantDataForm);
		String windowTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_AddParticipant_windowTitle");
		String subTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_AddParticipant_windowDesc")+" <span style='color:#ce0000; font-weight:bold;'>"+this.activitySpaceName+"</span>";
		addParticipantWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(addParticipantWindow);	
	}
	
	private void removeAddPartipantWindow(){
		this.getApplication().getMainWindow().removeWindow(addParticipantWindow);
		addParticipantWindow=null;
	}	
	
	private void doAddNewParticipant(String participantName,String participantDisplayName,String participantType,String roleNameId){
		String roleName;
		if(roleNameId!=null){
			Item selectedRoleItem=roleContainer.getItem(roleNameId);		
			roleName=selectedRoleItem.getItemProperty("ROLENAME").getValue().toString();
		}else{
			roleName=null;
		}		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		
		String parType=null;
		if(participantType.equals("USER")){
			parType=Participant.PARTICIPANT_TYPE_USER;
		}if(participantType.equals("GROUP")){
			parType=Participant.PARTICIPANT_TYPE_GROUP;
		}	
		
		Participant newParticipant=ActivityComponentFactory.createParticipant(participantName,parType,this.activitySpaceName);
		newParticipant.setDisplayName(participantDisplayName);
		try {
			boolean addResult=activitySpace.addParticipant(newParticipant);			
			if(addResult){
				String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_AddParticipant_SussMsg1")+" "+participantName+"  "+userClientInfo.getI18NProperties().getProperty("ActivityManage_participantsUI_AddParticipant_SussMsg2");
				getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
				removeAddPartipantWindow();				
				this.userClientInfo.getEventBlackboard().fire(new ParticipantsChangeEvent(this.activitySpaceName));				
			}else{
				//show error message
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		if(roleName!=null&&!roleName.equals(" ")){
			Role targetRole;
			try {
				targetRole = activitySpace.getRole(roleName);
				targetRole.addParticipant(participantName);
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			}			
		}	
	}	
}