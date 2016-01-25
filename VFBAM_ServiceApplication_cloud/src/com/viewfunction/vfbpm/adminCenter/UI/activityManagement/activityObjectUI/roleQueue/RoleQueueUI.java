package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue;

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
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ExposedDataFieldsEditor;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesTable;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseForm;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.MainTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
public class RoleQueueUI  extends VerticalLayout{
	private static final long serialVersionUID = -369129368853403693L;
	private RoleQueue roleQueue;
	private UserClientInfo userClientInfo;
	private DialogWindow editRoleQueueWindow;
	private RoleQueuePropertyList roleQueuePropertyList;
	private String propertyName_RoleName="propertyName_RoleName";
	private String propertyName_RoleDisplayName="propertyName_RoleDisplayName";
	private LightContentWindow roleSelectWindow;
	private RolesTable rolesTable;
	private VerticalLayout activityStepssUIContainer;	
	public RoleQueueUI(final RoleQueue currentRoleQueue, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail){
		this.roleQueue=currentRoleQueue;
		this.userClientInfo=userClientInfo;
		
		Button parentObjectLinkButton=new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_roleQueuesLabel"));
		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);		
		parentObjectLinkButton.addListener(new ClickListener(){ 
			private static final long serialVersionUID = 5236177496807958633L;

			public void buttonClick(ClickEvent event) {				
				renderRoleQueuesUI(currentRoleQueue.getActivitySpaceName());
			}}); 		
			
		MainTitleBar mainTitleBar=new MainTitleBar(this.roleQueue.getActivitySpaceName(),parentObjectLinkButton,this.roleQueue.getQueueName());
		this.addComponent(mainTitleBar);
		
		//render RoleQueue data		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);	
		// header part		
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleQueueIcon);
		
		HorizontalLayout roleQueueActionbarContainer=new HorizontalLayout();			
		roleQueueActionbarContainer.setWidth("30px");		
		Button editRoleQueueButton=new Button();
		editRoleQueueButton.setCaption(null);
		editRoleQueueButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		editRoleQueueButton.setStyleName(BaseTheme.BUTTON_LINK);
		editRoleQueueButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_editRoleQueueButton"));
		editRoleQueueButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 1343363957445040291L;

			public void buttonClick(ClickEvent event) {							
				renderEditRoleQueueFormUI();
			}});		
		roleQueueActionbarContainer.addComponent(editRoleQueueButton);		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,roleQueue.getQueueName(),SectionTitleBar.BIGFONT,roleQueueActionbarContainer);
		containerPanel.addComponent(sectionTitleBar);
		
		roleQueuePropertyList=new RoleQueuePropertyList(this.roleQueue,this.userClientInfo);
		containerPanel.addComponent(roleQueuePropertyList);		
		//RoleQueue detail
		
		HorizontalLayout divHorizontalLayout_2=new HorizontalLayout();
		divHorizontalLayout_2.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout_2);		
		
		Embedded rolesIconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleTitleIcon);			
		HorizontalLayout rolesActionbarContainer=new HorizontalLayout();			
		rolesActionbarContainer.setWidth("30px");		
		Button modifyRoleButton=new Button();
		modifyRoleButton.setCaption(null);
		modifyRoleButton.setIcon(UICommonElementDefination.ICON_Button_config_light);
		modifyRoleButton.setStyleName(BaseTheme.BUTTON_LINK);
		modifyRoleButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_editRolesButton"));	
		modifyRoleButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = 6874978270235233076L;

			public void buttonClick(ClickEvent event) {							
				renderModifyRoleFormUI();
			}});	
		rolesActionbarContainer.addComponent(modifyRoleButton);			
		
		SectionTitleBar rolesSectionTitleBar=new SectionTitleBar(rolesIconEmbedded,	this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_relatedRolesSectionTitle"),SectionTitleBar.MIDDLEFONT,rolesActionbarContainer);		
		containerPanel.addComponent(rolesSectionTitleBar);		
		Role[] roleArray=null;		
		try {
			roleArray=roleQueue.getRelatedRoles();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
		
		rolesTable=new RolesTable(roleArray,this.userClientInfo,activityObjectDetail);
		containerPanel.addComponent(rolesTable);
		
		HorizontalLayout divHorizontalLayout=new HorizontalLayout();
		divHorizontalLayout.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout);	
		
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.ICON_dataFieldDefine_DataFields_light);
		SectionTitleBar datafieldSectionTitleBar=new SectionTitleBar(expDataFieldsIcon,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_dataFieldsSectionTitle"),SectionTitleBar.MIDDLEFONT,null);		
		containerPanel.addComponent(datafieldSectionTitleBar);	
			
		DataFieldDefinition[] dataFieldDefinations=null;
		try {
			dataFieldDefinations=roleQueue.getExposedDataFields();					
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		}	
		ExposedDataFieldsEditor exposedDataFieldsEditor=new ExposedDataFieldsEditor(roleQueue,dataFieldDefinations,userClientInfo,false,null);
		containerPanel.addComponent(exposedDataFieldsEditor);	
		
		HorizontalLayout divHorizontalLayout_3=new HorizontalLayout();
		divHorizontalLayout_3.setHeight("20px");		
		containerPanel.addComponent(divHorizontalLayout_3);
		
		
		Embedded activityStepsIcon=new Embedded(null, UICommonElementDefination.ICON_activityStep_light);		
		HorizontalLayout activityStepsActionbarContainer=new HorizontalLayout();			
		activityStepsActionbarContainer.setWidth("30px");		
		Button fetchActivityStepsButton=new Button();
		fetchActivityStepsButton.setCaption(null);
		fetchActivityStepsButton.setIcon(UICommonElementDefination.Appaction_fetchDataIcon);
		fetchActivityStepsButton.setStyleName(BaseTheme.BUTTON_LINK);
		fetchActivityStepsButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_fetchStepsButton"));		
		activityStepsActionbarContainer.addComponent(fetchActivityStepsButton);		
		fetchActivityStepsButton.addListener(new ClickListener(){ 
			private static final long serialVersionUID = -4415846318682480051L;

			public void buttonClick(ClickEvent event) {							
				renderActivityStepsUI();
			}});
		
		SectionTitleBar activityStepsSectionTitleBar=new SectionTitleBar(activityStepsIcon,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_activityStepSectionTitle"),SectionTitleBar.MIDDLEFONT,activityStepsActionbarContainer);		
		containerPanel.addComponent(activityStepsSectionTitleBar);			
		activityStepssUIContainer=new VerticalLayout();
		containerPanel.addComponent(activityStepssUIContainer);
	}
	
	public void renderRoleQueuesUI(String activitySpaceName){
		ActivityObjectDetail activityObjectDetail =(ActivityObjectDetail)(this.getParent().getParent());
		activityObjectDetail.renderRoleQueuesUI(activitySpaceName);
	}
	
	private void renderEditRoleQueueFormUI(){
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm editRoleQueueDataForm=new BaseForm();			
		final TextField roleQueueNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueNameField"));		
		roleQueueNameField.setValue(this.roleQueue.getQueueName());
		roleQueueNameField.setEnabled(false);
		roleQueueNameField.setWidth("250px");
		roleQueueNameField.setRequired(true);	
		editRoleQueueDataForm.addField("roleQueueName", roleQueueNameField);
		
		final TextField roleQueueDisplayNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDisNameField"));		
		roleQueueDisplayNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDisNameFieldDesc"));
		roleQueueDisplayNameField.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDisNameFieldErrMsg"));
		roleQueueDisplayNameField.setValue(this.roleQueue.getDisplayName());
		roleQueueDisplayNameField.setWidth("250px");
		roleQueueDisplayNameField.setRequired(true);		
		editRoleQueueDataForm.addField("roleQueueDisplayName", roleQueueDisplayNameField);
		
		final TextArea roleQueueDescTextArea=new TextArea(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDescField"));
		roleQueueDescTextArea.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDescFieldDesc"));
		roleQueueDescTextArea.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDescFieldErrMsg"));
		roleQueueDescTextArea.setValue(this.roleQueue.getDescription());
		roleQueueDescTextArea.setRows(2);
		roleQueueDescTextArea.setColumns(20);
		roleQueueDescTextArea.setRequired(true);
		editRoleQueueDataForm.addField("roleQueueDescription", roleQueueDescTextArea);	
		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_editRoleQueue_confirmEditButtonLabel"));
		okButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = 6874978270235233076L;

			public void buttonClick(ClickEvent event) {
				
				editRoleQueueDataForm.setValidationVisible(true);
				editRoleQueueDataForm.setComponentError(null);
	            boolean validateResult = editRoleQueueDataForm.isValid();		            
	            if(validateResult){  
	            	String newRoleQueueDisplayName=roleQueueDisplayNameField.getValue().toString();
					String newRoleQueueDesc=roleQueueDescTextArea.getValue().toString();													
					doUpdateRoleQueue(newRoleQueueDisplayName,newRoleQueueDesc);	            	    	
	            }		           
			}
		  });
		
	    Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_editRoleQueue_cancelEditButtonLabel"),new Button.ClickListener() {
			private static final long serialVersionUID = -4415846318682480051L;

			public void buttonClick(ClickEvent event) {               
            	removeEditRoleQueueWindow();
            }
        });
	    cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	    
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar editRoleQueueButtonBar = new BaseButtonBar(280, 30, Alignment.BOTTOM_LEFT, buttonList);
	    editRoleQueueDataForm.getLayout().addComponent(editRoleQueueButtonBar);
	    windowContent.addComponent(editRoleQueueDataForm);
		String windowTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRoleQueueWindowTitle");
		String subTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRoleQueueWindowDesc")+" "+" <span style='color:#ce0000; font-weight:bold;'>"+this.roleQueue.getQueueName()+"</span>";
		editRoleQueueWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(editRoleQueueWindow);
	}
	
	private void removeEditRoleQueueWindow(){
		this.getApplication().getMainWindow().removeWindow(editRoleQueueWindow);
		editRoleQueueWindow=null;
	}
	
	private void doUpdateRoleQueue(String newDisplayName,String newDesc){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.roleQueue.getActivitySpaceName());		
		try {
			this.roleQueue=activitySpace.updateRoleQueue(this.roleQueue.getQueueName(), newDisplayName, newDesc);
			this.roleQueuePropertyList.reloadContent(this.roleQueue);			
			this.userClientInfo.getEventBlackboard().fire(new RoleQueuesChangeEvent(this.roleQueue.getActivitySpaceName()));
			String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRoleQueueSucess_msg1")+" "+
			this.roleQueue.getQueueName()+"  "+
			this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRoleQueueSucess_msg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
			removeEditRoleQueueWindow();			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
	
	private void renderModifyRoleFormUI(){
		Embedded roleQueueOfActivitySpaceIcon=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);		
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRoleWindowTitle")+" </b> ", Label.CONTENT_XHTML);
		final OptionGroup roleSelect = new OptionGroup("");
		roleSelect.setMultiSelect(true);
		roleSelect.setNullSelectionAllowed(true);		
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.roleQueue.getActivitySpaceName());
		
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(propertyName_RoleName, String.class,null);
        container.addContainerProperty(propertyName_RoleDisplayName, String.class,null);
        roleSelect.setContainerDataSource(container);
        roleSelect.setItemCaptionPropertyId(propertyName_RoleDisplayName);		
        try {			
			Role[] roleArray=activitySpace.getRoles();
			Role[] alreadyRelatedRoles=this.roleQueue.getRelatedRoles();			
			for(int i=0;i<roleArray.length;i++){				
				Role currentRole=roleArray[i];
				String id = ""+i;
				Item item = container.addItem(id);	
				String roleName=currentRole.getRoleName();
				String roleCaption=currentRole.getDisplayName()+" ( "+roleName+" )";
				item.getItemProperty(propertyName_RoleName).setValue(roleName);  
				item.getItemProperty(propertyName_RoleDisplayName).setValue(roleCaption);		
				if(alreadyRelatedToRole(roleName,alreadyRelatedRoles)){
					roleSelect.select(id);					
				}
			}				
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		}
        
        VerticalLayout rolesSelectContainerLayout=new VerticalLayout();		
        rolesSelectContainerLayout.addComponent(roleSelect);		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRole_confirmButton"));		
		okButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -7392581946653495557L;

			public void buttonClick(ClickEvent event) {				
				Set selectedRoleIdxSet=(Set)roleSelect.getValue();				
				Object[] idxArry=selectedRoleIdxSet.toArray();	
				String[] newRoleNameArray=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					newRoleNameArray[i]=selecteItem.getItemProperty(propertyName_RoleName).getValue().toString();					
				}
				doUpdateRole(newRoleNameArray);				
				removeEditRoleWindow();
			}
		  });		
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRole_cancelButton"),new Button.ClickListener() {		
			private static final long serialVersionUID = -3888479227433859341L;

			public void buttonClick(ClickEvent event) {               
				removeEditRoleWindow();			
	        }
	    });
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		buttonList.add(okButton);
		buttonList.add(cancelAddbutton);
		BaseButtonBar selectParticipantsButtonBar = new BaseButtonBar(100, 30, Alignment.BOTTOM_RIGHT, buttonList);		
		rolesSelectContainerLayout.addComponent(selectParticipantsButtonBar);		
		roleSelectWindow=new LightContentWindow(roleQueueOfActivitySpaceIcon,propertyNameLable,rolesSelectContainerLayout,"500px");		
		roleSelectWindow.center();
		this.getApplication().getMainWindow().addWindow(roleSelectWindow);		
	}
	
	private boolean alreadyRelatedToRole(String roleName,Role[] roleArray){
		if(roleArray==null){
			return false;
		}
		for(Role role:roleArray){
			if(role.getRoleName().equals(roleName)){
				return true;
			}
		}		
		return false;
	}
	
	private void removeEditRoleWindow(){
		this.getApplication().getMainWindow().removeWindow(roleSelectWindow);
		roleSelectWindow=null;
	}
	
	private void doUpdateRole(String[] roleNames){		
		try {
			Role[] orgRelatedRoles=this.roleQueue.getRelatedRoles();
			if(orgRelatedRoles!=null&&orgRelatedRoles.length!=0){
				for(Role role:orgRelatedRoles){
					if(!ifContainsInRoleArray(role.getRoleName(),roleNames)){
						this.roleQueue.removeRole(role.getRoleName()) ;
					}
				}				
			}else{
				if(orgRelatedRoles!=null){
					for(Role role:orgRelatedRoles){
						this.roleQueue.removeRole(role.getRoleName());
					}
				}				
			}
			for(String roleName:roleNames){
				this.roleQueue.addRole(roleName);				
			}
			rolesTable.reloadContent(this.roleQueue.getRelatedRoles());
			String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRoleSucess_msg1")+" "+
			this.roleQueue.getQueueName()+"  "+
			this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueueUI_updateRoleSucess_msg2");
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		}		
	}
	
	private boolean ifContainsInRoleArray(String roleName,String[] roleNameArray){
		if(roleNameArray==null){
			return false;
		}
		for(String curRoleName:roleNameArray){
			if(curRoleName.equals(roleName)){
				return true;
			}
		}		
		return false;
	}	
	
	private void renderActivityStepsUI(){
		activityStepssUIContainer.removeAllComponents();
		RoleQueueElementsTable roleQueueElementsTable=new RoleQueueElementsTable(roleQueue,userClientInfo,null);
		activityStepssUIContainer.addComponent(roleQueueElementsTable);		
	}
}