package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsTable;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesTable;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.MainTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class RoleUI  extends VerticalLayout{
	private static final long serialVersionUID = -504305239541113535L;
	
	private Role role;
	private UserClientInfo userClientInfo;
	private DialogWindow editRoleWindow;
	private RolePropertyList rolePropertyList;
	private LightContentWindow participantSelectWindow;
	private LightContentWindow roleQueueSelectWindow;
	private String propertyName_RoleQueueName="propertyName_RoleQueueName";
	private String propertyName_RoleQueueDisplayName="propertyName_RoleQueueDisplayName";	
	private String propertyName_ParticipantName="propertyName_ParticipantName";
	private String propertyName_ParticipantDisplayName="propertyName_ParticipantDisplayName";
	private ParticipantsTable participantsTable;
	private RoleQueuesTable roleQueuesTable;
	
	public RoleUI(final Role currentRole, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail){
		this.role=currentRole;
		this.userClientInfo=userClientInfo;
		//render title bar		
		Button parentObjectLinkButton=new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_rolesLabel"));
		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);		
		parentObjectLinkButton.addListener(new ClickListener(){ 			
			private static final long serialVersionUID = -3605756437918727779L;

			public void buttonClick(ClickEvent event) {				
				renderRolesUI(currentRole.getActivitySpaceName());
			}}); 				
		MainTitleBar mainTitleBar=new MainTitleBar(this.role.getActivitySpaceName(),parentObjectLinkButton,this.role.getRoleName());
		this.addComponent(mainTitleBar);
		
		//render Role data		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);			
		// header part		
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleIcon);
		iconEmbedded.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_object_participantLabel"));		
		
		HorizontalLayout roleActionbarContainer=new HorizontalLayout();			
		roleActionbarContainer.setWidth("30px");		
		Button editRoleButton=new Button();
		editRoleButton.setCaption(null);
		editRoleButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		editRoleButton.setStyleName(BaseTheme.BUTTON_LINK);
		editRoleButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRole_button"));
		editRoleButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -2683548836708568307L;

			public void buttonClick(ClickEvent event) {							
				renderEditRoleFormUI();
			}});		
		roleActionbarContainer.addComponent(editRoleButton);
		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,currentRole.getRoleName(),SectionTitleBar.BIGFONT,roleActionbarContainer);
		containerPanel.addComponent(sectionTitleBar);			
		//Role detail
		rolePropertyList=new RolePropertyList(currentRole,this.userClientInfo);
		containerPanel.addComponent(rolePropertyList);
		
		HorizontalLayout divHorizontalLayout=new HorizontalLayout();
		divHorizontalLayout.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout);
		
		Embedded roleQueueIconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleQueueTitleIcon);			
		HorizontalLayout roleQueueActionbarContainer=new HorizontalLayout();			
		roleQueueActionbarContainer.setWidth("30px");		
		Button modifyRoleQueueButton=new Button();
		modifyRoleQueueButton.setCaption(null);
		modifyRoleQueueButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		modifyRoleQueueButton.setStyleName(BaseTheme.BUTTON_LINK);
		modifyRoleQueueButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRoleQueue_button"));
		modifyRoleQueueButton.addListener(new ClickListener(){		
			private static final long serialVersionUID = 6502570844059766053L;

			public void buttonClick(ClickEvent event) {							
				renderEditRoleQueuesUI();
			}});	
		roleQueueActionbarContainer.addComponent(modifyRoleQueueButton);		
		
		SectionTitleBar roleQueueSectionTitleBar=new SectionTitleBar(roleQueueIconEmbedded,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_roleQueuesLabel"),SectionTitleBar.MIDDLEFONT,roleQueueActionbarContainer);		
		containerPanel.addComponent(roleQueueSectionTitleBar);
		
		RoleQueue[] roleQueueArray=null;
		try {
			roleQueueArray=currentRole.getRelatedRoleQueues();
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		}		
		roleQueuesTable=new RoleQueuesTable(roleQueueArray,userClientInfo,activityObjectDetail);
		containerPanel.addComponent(roleQueuesTable);			
		
		HorizontalLayout divHorizontalLayout_2=new HorizontalLayout();
		divHorizontalLayout_2.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout_2);		
		
		Embedded participantIconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceParticipantTitleIcon);			
		HorizontalLayout participantsActionbarContainer=new HorizontalLayout();			
		participantsActionbarContainer.setWidth("30px");		
		Button modifyParticipantButton=new Button();
		modifyParticipantButton.setCaption(null);
		modifyParticipantButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		modifyParticipantButton.setStyleName(BaseTheme.BUTTON_LINK);
		modifyParticipantButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editParticipant_button"));	
		modifyParticipantButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = 6502570844059766053L;

			public void buttonClick(ClickEvent event) {							
				renderEditRoleParticipantsUI();
			}});		
		
		participantsActionbarContainer.addComponent(modifyParticipantButton);		
		
		SectionTitleBar participantsSectionTitleBar=new SectionTitleBar(participantIconEmbedded,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_participantsLabel"),SectionTitleBar.MIDDLEFONT,participantsActionbarContainer);		
		containerPanel.addComponent(participantsSectionTitleBar);
		
		Participant[] participantArray=null;
		try {
			participantArray=currentRole.getParticipants();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		participantsTable=new ParticipantsTable(participantArray,userClientInfo,activityObjectDetail);
		containerPanel.addComponent(participantsTable);
	}
	
	public void renderRolesUI(String activitySpaceName){
		ActivityObjectDetail activityObjectDetail =(ActivityObjectDetail)(this.getParent().getParent());
		activityObjectDetail.renderRolesUI(activitySpaceName);
	}
	
	private void renderEditRoleFormUI(){
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm editRoleDataForm=new BaseForm();			
		final TextField roleNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleNameField"));		
		roleNameField.setValue(this.role.getRoleName());
		roleNameField.setEnabled(false);
		roleNameField.setWidth("250px");
		roleNameField.setRequired(true);	
		editRoleDataForm.addField("roleName", roleNameField);
		
		final TextField roleDisplayNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDisNameField"));		
		roleDisplayNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDisNameFieldDesc"));
		roleDisplayNameField.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDisNameFieldErrM"));
		roleDisplayNameField.setValue(this.role.getDisplayName());
		roleDisplayNameField.setWidth("250px");
		roleDisplayNameField.setRequired(true);		
		editRoleDataForm.addField("roleDisplayName", roleDisplayNameField);
		
		final TextArea roleDescTextArea=new TextArea(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDescField"));
		roleDescTextArea.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDescFieldDesc"));
		roleDescTextArea.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRole_roleDescFieldErrM"));
		roleDescTextArea.setValue(this.role.getDescription());
		roleDescTextArea.setRows(2);
		roleDescTextArea.setColumns(20);
		roleDescTextArea.setRequired(true);
		editRoleDataForm.addField("roleDescription", roleDescTextArea);	
		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRole_confirmEditButtonLabel"));
		okButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = -7296670512588753726L;

			public void buttonClick(ClickEvent event) {				
				editRoleDataForm.setValidationVisible(true);
				editRoleDataForm.setComponentError(null);
	            boolean validateResult = editRoleDataForm.isValid();		            
	            if(validateResult){  
	            	String newRoleDisplayName=roleDisplayNameField.getValue().toString();
					String newRoleDesc=roleDescTextArea.getValue().toString();													
					doUpdateRole(newRoleDisplayName,newRoleDesc);	            	    	
	            }	           
			}
		  });
		
	    Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRole_cancelEditButtonLabel"),new Button.ClickListener() {
			private static final long serialVersionUID = 1892815092139876592L;

			public void buttonClick(ClickEvent event) {               
            	removeEditRoleWindow();
            }
        });
	    cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	    
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(280, 30, Alignment.BOTTOM_LEFT, buttonList);
	    editRoleDataForm.getLayout().addComponent(addParticipantButtonBar);
		
	    windowContent.addComponent(editRoleDataForm);
		String windowTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRole_windowTitle");
		String subTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRole_windowTitleDesc")+" "+" <span style='color:#ce0000; font-weight:bold;'>"+this.role.getRoleName()+"</span>";
		editRoleWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(editRoleWindow);			
	}
	
	private void removeEditRoleWindow(){
		this.getApplication().getMainWindow().removeWindow(editRoleWindow);
		editRoleWindow=null;
	}
	
	private void doUpdateRole(String newDisplayName,String newDesc){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.role.getActivitySpaceName());		
		try {
			this.role=activitySpace.updateRole(this.role.getRoleName(), newDisplayName, newDesc);
			rolePropertyList.reloadContent(this.role);
			this.userClientInfo.getEventBlackboard().fire(new RolesChangeEvent(this.role.getActivitySpaceName()));
			String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRole_SussMsg1")+" "+this.role.getRoleName()+"  "+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRole_SussMsg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
			removeEditRoleWindow();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
	
	private void renderEditRoleParticipantsUI(){
		Embedded participantOfActivitySpaceIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceParticipant_blackIcon);		
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_windowTitle")+" </b> ", Label.CONTENT_XHTML);
		final OptionGroup participantsSelect = new OptionGroup("");
		participantsSelect.setMultiSelect(true);
		participantsSelect.setNullSelectionAllowed(true);		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.role.getActivitySpaceName());
		
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(propertyName_ParticipantName, String.class,null);
        container.addContainerProperty(propertyName_ParticipantDisplayName, String.class,null);
        participantsSelect.setContainerDataSource(container);
        participantsSelect.setItemCaptionPropertyId(propertyName_ParticipantDisplayName);		
		try {
			Participant[] participantsInSpace=activitySpace.getParticipants();			
			Participant[] containsParticipant=this.role.getParticipants();			
			for(int i=0;i<participantsInSpace.length;i++){				
				Participant currentParticipant=participantsInSpace[i];
				String id = ""+i;
				Item item = container.addItem(id);	
				String participantName=currentParticipant.getParticipantName();
				String participantCaption=currentParticipant.getDisplayName()+" ( "+currentParticipant.getParticipantName()+" )";
				item.getItemProperty(propertyName_ParticipantName).setValue(participantName);  
				item.getItemProperty(propertyName_ParticipantDisplayName).setValue(participantCaption);		
				if(alreadyContainsParticipant(participantName,containsParticipant)){
					participantsSelect.select(id);					
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
			private static final long serialVersionUID = 2743268623070177993L;

			public void buttonClick(ClickEvent event) {				
				Set selectedParticipantIdxSet=(Set)participantsSelect.getValue();				
				Object[] idxArry=selectedParticipantIdxSet.toArray();	
				String[] newParticipantNameArray=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					newParticipantNameArray[i]=selecteItem.getItemProperty(propertyName_ParticipantName).getValue().toString();					
				}
				doUpdateParticipant(newParticipantNameArray);				
				removeParticipantSelectWindow();
			}
		  });		
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_cancelButtonLabel"),new Button.ClickListener() {		
			private static final long serialVersionUID = -1409606015454227125L;

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
	
	private void doUpdateParticipant(String[] newParticipantArry){
		try {
			Participant[] orgContainsParticipant=this.role.getParticipants();
			
			if(orgContainsParticipant!=null&&orgContainsParticipant.length!=0){
				for(Participant participant:orgContainsParticipant){
					if(!ifContainsInRoleQueueArray(participant.getParticipantName(),newParticipantArry)){
						this.role.removeParticipant(participant.getParticipantName());
					}
				}					
			}else{
				if(orgContainsParticipant!=null){
					for(Participant participant:orgContainsParticipant){
						this.role.removeFromRoleQueue(participant.getParticipantName());
					}
				}				
			}
			for(String participantName:newParticipantArry){
				this.role.addParticipant(participantName);						
			}
			participantsTable.reloadContent(this.role.getParticipants());
			String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editParticipant_SussMsg1")+" "+this.role.getRoleName()+"  "+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editParticipant_SussMsg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);	
			removeParticipantSelectWindow();			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		}	
	}
	
	private boolean alreadyContainsParticipant(String participantName,Participant[] participantArray){
		if(participantArray==null){
			return false;
		}
		for(Participant participant:participantArray){
			if(participant.getParticipantName().equals(participantName)){
				return true;
			}
		}		
		return false;
	}
	
	private void renderEditRoleQueuesUI(){
		Embedded roleQueueOfActivitySpaceIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleQueue_blackIcon);		
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRoleQueue_windowTitle")+" </b> ", Label.CONTENT_XHTML);
		final OptionGroup roleQueuesSelect = new OptionGroup("");
		roleQueuesSelect.setMultiSelect(true);
		roleQueuesSelect.setNullSelectionAllowed(true);		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.role.getActivitySpaceName());	
		
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(propertyName_RoleQueueName, String.class,null);
        container.addContainerProperty(propertyName_RoleQueueDisplayName, String.class,null);
        roleQueuesSelect.setContainerDataSource(container);
        roleQueuesSelect.setItemCaptionPropertyId(propertyName_RoleQueueDisplayName);
		try {			
			RoleQueue[] roleQueueArray=activitySpace.getRoleQueues();
			RoleQueue[] alreadyInRoleQueues=this.role.getRelatedRoleQueues();			
			for(int i=0;i<roleQueueArray.length;i++){				
				RoleQueue currentRoleQueue=roleQueueArray[i];
				String id = ""+i;
				Item item = container.addItem(id);	
				String roleQueueName=currentRoleQueue.getQueueName();
				String roleQueueCaption=currentRoleQueue.getDisplayName()+" ( "+currentRoleQueue.getQueueName()+" )";
				item.getItemProperty(propertyName_RoleQueueName).setValue(roleQueueName);  
				item.getItemProperty(propertyName_RoleQueueDisplayName).setValue(roleQueueCaption);		
				if(alreadyRelatedToQueue(roleQueueName,alreadyInRoleQueues)){
					roleQueuesSelect.select(id);					
				}
			}				
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		}
		VerticalLayout participantsSelectContainerLayout=new VerticalLayout();		
		participantsSelectContainerLayout.addComponent(roleQueuesSelect);		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_confirmButtonLabel"));		
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 2743268623070177993L;

			public void buttonClick(ClickEvent event) {									
				Set selectedQueueIdxSet=(Set)roleQueuesSelect.getValue();				
				Object[] idxArry=selectedQueueIdxSet.toArray();	
				String[] newQueueNameArray=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					newQueueNameArray[i]=selecteItem.getItemProperty(propertyName_RoleQueueName).getValue().toString();					
				}
				doUpdateRoleQueue(newQueueNameArray);				
				removeEditRoleQueueWindow();
			}
		  });		
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rolesUI_addRoleParticipant_cancelButtonLabel"),new Button.ClickListener() {		
			private static final long serialVersionUID = -1409606015454227125L;

			public void buttonClick(ClickEvent event) {               
				removeEditRoleQueueWindow();			
	        }
	    });
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		buttonList.add(okButton);
		buttonList.add(cancelAddbutton);
		BaseButtonBar selectParticipantsButtonBar = new BaseButtonBar(100, 30, Alignment.BOTTOM_RIGHT, buttonList);		
		participantsSelectContainerLayout.addComponent(selectParticipantsButtonBar);		
		roleQueueSelectWindow=new LightContentWindow(roleQueueOfActivitySpaceIcon,propertyNameLable,participantsSelectContainerLayout,"500px");		
		roleQueueSelectWindow.center();
		this.getApplication().getMainWindow().addWindow(roleQueueSelectWindow);
	}
	
	private void removeEditRoleQueueWindow(){
		this.getApplication().getMainWindow().removeWindow(roleQueueSelectWindow);
		roleQueueSelectWindow=null;
	}
	
	private boolean alreadyRelatedToQueue(String queueName,RoleQueue[] queueArray){
		if(queueArray==null){
			return false;
		}
		for(RoleQueue roleQueue:queueArray){
			if(roleQueue.getQueueName().equals(queueName)){
				return true;
			}
		}		
		return false;
	}
	
	private boolean ifContainsInRoleQueueArray(String queueName,String[] queueNameArray){
		if(queueNameArray==null){
			return false;
		}
		for(String roleQueueName:queueNameArray){
			if(roleQueueName.equals(queueName)){
				return true;
			}
		}		
		return false;
	}
	
	private void doUpdateRoleQueue(String[] newQueueArray){		
		try {			
			RoleQueue[] orgRelatedRoleQueue=this.role.getRelatedRoleQueues();				
			if(orgRelatedRoleQueue!=null&&orgRelatedRoleQueue.length!=0){
				for(RoleQueue roleQueue:orgRelatedRoleQueue){
					if(!ifContainsInRoleQueueArray(roleQueue.getQueueName(),newQueueArray)){
						this.role.removeFromRoleQueue(roleQueue.getQueueName());
					}
				}					
			}else{
				if(orgRelatedRoleQueue!=null){
					for(RoleQueue roleQueue:orgRelatedRoleQueue){
						this.role.removeFromRoleQueue(roleQueue.getQueueName());
					}
				}				
			}
			for(String queueName:newQueueArray){
				this.role.addInRoleQueue(queueName);						
			}
			roleQueuesTable.reloadContent(this.role.getRelatedRoleQueues());
			String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRoleQueue_SussMsg1")+" "+this.role.getRoleName()+"  "+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleUI_editRoleQueue_SussMsg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		}		
	}
}