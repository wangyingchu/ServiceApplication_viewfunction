package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
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
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.MainTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RolesUI  extends VerticalLayout{
	private static final long serialVersionUID = -8672904122849688515L;
	
	private UserClientInfo userClientInfo;
	private DialogWindow addRoleWindow;
	private String activitySpaceName;
	private LightContentWindow participantSelectWindow;
	private Label containsParticipantNum;
	private Set newRoleContainsParticipantSet;
	
	public RolesUI(Role[] roleArray, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail,String activitySpaceName){
		this.userClientInfo=userClientInfo;	
		this.activitySpaceName=activitySpaceName;
		//render title bar		
		MainTitleBar mainTitleBar=new MainTitleBar(this.activitySpaceName,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_rolesLabel"));
		this.addComponent(mainTitleBar);
		
		//render Roles list				
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);					
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleTitleIcon);
		
		HorizontalLayout rolesActionbarContainer=new HorizontalLayout();			
		rolesActionbarContainer.setWidth("30px");		
		Button addNewRoleButton=new Button();
		addNewRoleButton.setCaption(null);
		addNewRoleButton.setIcon(UICommonElementDefination.ICON_addNewRole);
		addNewRoleButton.setStyleName(BaseTheme.BUTTON_LINK);
		addNewRoleButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_buttonLabel"));		
		rolesActionbarContainer.addComponent(addNewRoleButton);
		
		addNewRoleButton.addListener(new ClickListener(){		
			private static final long serialVersionUID = 1825488330695913846L;

			public void buttonClick(ClickEvent event) {							
				renderAddRoleFormUI();
			}});		
		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_rolesLabel"),SectionTitleBar.MIDDLEFONT,rolesActionbarContainer);		
		containerPanel.addComponent(sectionTitleBar);			
		RolesTable rolesTable=new RolesTable(roleArray,userClientInfo,activityObjectDetail);		
		containerPanel.addComponent(rolesTable);
		this.userClientInfo.getEventBlackboard().addListener(rolesTable);
		activityObjectDetail.rolesChangeListenerList.add(rolesTable);
	}
	
	private void renderAddRoleFormUI(){
		newRoleContainsParticipantSet=null;
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm newRoleDataForm=new BaseForm();
		final TextField roleNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleNameField"));		
		roleNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleNameFieldDesc"));
		roleNameField.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleNameFieldErrM"));
		roleNameField.setWidth("249px");
		roleNameField.setRequired(true);		
		newRoleDataForm.addField("participantName", roleNameField);
		
		final TextField roleDisplayNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDisNameField"));		
		roleDisplayNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDisNameFieldDesc"));
		roleDisplayNameField.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDisNameFieldErrM"));
		roleDisplayNameField.setWidth("249px");
		roleDisplayNameField.setRequired(true);		
		newRoleDataForm.addField("roleDisplayName", roleDisplayNameField);
		
		final TextArea roleDescTextArea=new TextArea(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDescField"));
		roleDescTextArea.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDescFieldDesc"));
		roleDescTextArea.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDescFieldErrM"));
		roleDescTextArea.setRows(2);
		roleDescTextArea.setColumns(20);
		roleDescTextArea.setRequired(true);
		newRoleDataForm.addField("roleDescription", roleDescTextArea);	
		
		HorizontalLayout addParticipantsContainerLayout=new HorizontalLayout();		
		addParticipantsContainerLayout.setWidth("240px");		
		HorizontalLayout addParticipantsButtonContainerLayout=new HorizontalLayout();		
		Button addParticipantsButton=new Button();
		addParticipantsButton.setCaption(null);
		addParticipantsButton.setIcon(UICommonElementDefination.ICON_addRoleParticipant);
		addParticipantsButton.setStyleName(BaseTheme.BUTTON_LINK);
		addParticipantsButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_buttonLabel"));		
		addParticipantsButtonContainerLayout.addComponent(addParticipantsButton);
		addParticipantsButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -7336833691783656839L;

			public void buttonClick(ClickEvent event) {
				renderParticipantsSelectUI();
			}
		  });
		
		containsParticipantNum=new Label("(0)");
		addParticipantsButtonContainerLayout.addComponent(containsParticipantNum);		
		addParticipantsContainerLayout.addComponent(addParticipantsButtonContainerLayout);		
		addParticipantsContainerLayout.setComponentAlignment(addParticipantsButtonContainerLayout, Alignment.MIDDLE_RIGHT);		
		newRoleDataForm.getLayout().addComponent(addParticipantsContainerLayout);		
		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_confirmButtonLabel"));
		final ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 8288901532888943645L;

			public void buttonClick(ClickEvent event) {				
				newRoleDataForm.setValidationVisible(true);
				newRoleDataForm.setComponentError(null);
	            boolean validateResult = newRoleDataForm.isValid();		            
	            if(validateResult){  
	            	String newRoleName=roleNameField.getValue().toString();
	            	try {
	            		Role targetRole=activitySpace.getRole(newRoleName);							
						if(targetRole!=null){
							newRoleDataForm.setComponentError(new UserError(userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleExistErrM")));
							return;								
						}else{
							newRoleDataForm.setComponentError(null);
							String newRoleDisplayName=roleDisplayNameField.getValue().toString();
							String newRoleDesc=roleDescTextArea.getValue().toString();													
							doAddNewRole(newRoleName,newRoleDisplayName,newRoleDesc,newRoleContainsParticipantSet);								
						}							
					} catch (ActivityEngineRuntimeException e) {							
						e.printStackTrace();
					}		            	
	            }	           
			}
		  });
		
	    Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_cancelButtonLabel"),new Button.ClickListener() {
			private static final long serialVersionUID = -1689764977919967117L;

			public void buttonClick(ClickEvent event) {               
            	removeAddRoleWindow();
            }
        });
	    cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	    
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(280, 30, Alignment.BOTTOM_LEFT, buttonList);
	    newRoleDataForm.getLayout().addComponent(addParticipantButtonBar);
		
	    windowContent.addComponent(newRoleDataForm);
		String windowTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_windowTitle");
		String subTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_windowDesc")+" <span style='color:#ce0000; font-weight:bold;'>"+this.activitySpaceName+"</span>";
		addRoleWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(addRoleWindow);		
	}
	
	private void removeAddRoleWindow(){
		this.getApplication().getMainWindow().removeWindow(addRoleWindow);
		addRoleWindow=null;
		newRoleContainsParticipantSet=null;
	}
	
	private void renderParticipantsSelectUI(){
		Embedded participantOfActivitySpaceIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceParticipant_blackIcon);		
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_windowTitle")+" </b> ", Label.CONTENT_XHTML);
		final OptionGroup participantsSelect = new OptionGroup("");
		participantsSelect.setMultiSelect(true);
		participantsSelect.setNullSelectionAllowed(true);
		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);		
		try {
			Participant[] participantsInSpace=activitySpace.getParticipants();
			if(participantsInSpace!=null){
				for(Participant participant:participantsInSpace){				
					participantsSelect.addItem(participant.getParticipantName());
				}	
			}					
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		}		
		
		VerticalLayout participantsSelectContainerLayout=new VerticalLayout();		
		participantsSelectContainerLayout.addComponent(participantsSelect);		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_confirmButtonLabel"));		
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 3618788002266737211L;

			public void buttonClick(ClickEvent event) {
				newRoleContainsParticipantSet=(Set)participantsSelect.getValue();				
				containsParticipantNum.setValue("("+newRoleContainsParticipantSet.size()+")");
				removeParticipantSelectWindow();	
			}
		  });		
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_cancelButtonLabel"),new Button.ClickListener() {		
			private static final long serialVersionUID = 3618788002266737211L;

			public void buttonClick(ClickEvent event) {               
				removeParticipantSelectWindow();			
	        }
	    });
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		buttonList.add(okButton);
		buttonList.add(cancelAddbutton);
		BaseButtonBar selectParticipantsButtonBar = new BaseButtonBar(100, 30, Alignment.BOTTOM_RIGHT, buttonList);		
		participantsSelectContainerLayout.addComponent(selectParticipantsButtonBar);		
		participantSelectWindow=new LightContentWindow(participantOfActivitySpaceIcon,propertyNameLable,participantsSelectContainerLayout,"300px");		
		participantSelectWindow.center();
		this.getApplication().getMainWindow().addWindow(participantSelectWindow);		
	}
	
	private void removeParticipantSelectWindow(){
		this.getApplication().getMainWindow().removeWindow(participantSelectWindow);
		participantSelectWindow=null;
	}
	
	private void doAddNewRole(String roleName,String roleDisplayName,String roleDesc,Set roleParticipant){		
		Role newRole=ActivityComponentFactory.createRole(this.activitySpaceName,roleName);
		newRole.setDisplayName(roleDisplayName);
		newRole.setDescription(roleDesc);		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);			
		try {
			boolean addResult=activitySpace.addRole(newRole);
			if(addResult){
				if(roleParticipant!=null&&roleParticipant.size()>0){					
					String[] newParticipantArray=new String[roleParticipant.size()];
					roleParticipant.toArray(newParticipantArray);					
					for(String participantName:newParticipantArray){
						newRole.addParticipant(participantName);
					}
				}
			}else{
				//show error message
			}
			String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_SussMsg1")+" "+roleName+" "+
					this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_SussMsg2")+
					" "+this.activitySpaceName;
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
			removeAddRoleWindow();
			this.userClientInfo.getEventBlackboard().fire(new RolesChangeEvent(this.activitySpaceName));
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
}