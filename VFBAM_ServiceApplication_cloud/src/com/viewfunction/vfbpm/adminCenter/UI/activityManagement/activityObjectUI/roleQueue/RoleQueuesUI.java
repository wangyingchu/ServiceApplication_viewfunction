package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
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
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
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

public class RoleQueuesUI  extends VerticalLayout{
	private static final long serialVersionUID = 8427605482436645025L;
	private UserClientInfo userClientInfo;
	private DialogWindow addRoleQueueWindow;	
	private String activitySpaceName;
	private String propertyName_RoleQueueName="propertyName_RoleQueueName";
	private String propertyName_RoleQueueDisplayName="propertyName_RoleQueueDisplayName";	
	private LightContentWindow roleQueueSelectWindow;
	
	public RoleQueuesUI(RoleQueue[] roleQueueArray, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail,String activitySpaceName){
		this.userClientInfo=userClientInfo;	
		this.activitySpaceName=activitySpaceName;
		//render title bar		
		MainTitleBar mainTitleBar=new MainTitleBar(this.activitySpaceName,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_roleQueuesLabel"));
		this.addComponent(mainTitleBar);		
		//render RoleQueues list		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);					
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleQueueTitleIcon);	
		
		HorizontalLayout roleQueuesActionbarContainer=new HorizontalLayout();			
		roleQueuesActionbarContainer.setWidth("60px");		
		Button addNewRoleQueueButton=new Button();
		addNewRoleQueueButton.setCaption(null);
		addNewRoleQueueButton.setIcon(UICommonElementDefination.ICON_addRoleQueue);
		addNewRoleQueueButton.setStyleName(BaseTheme.BUTTON_LINK);
		addNewRoleQueueButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueueButton"));		
		roleQueuesActionbarContainer.addComponent(addNewRoleQueueButton);		
		addNewRoleQueueButton.addListener(new ClickListener(){				
			private static final long serialVersionUID = 3662983740049180495L;

			public void buttonClick(ClickEvent event) {							
				renderAddRoleQueueFormUI();
			}});		
		
		Button removeRoleQueuesButton=new Button();
		removeRoleQueuesButton.setCaption(null);
		removeRoleQueuesButton.setIcon(UICommonElementDefination.ICON_removeRoleQueue);
		removeRoleQueuesButton.setStyleName(BaseTheme.BUTTON_LINK);
		removeRoleQueuesButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_removeRoleQueueButton"));		
		roleQueuesActionbarContainer.addComponent(removeRoleQueuesButton);		
		removeRoleQueuesButton.addListener(new ClickListener(){				
			private static final long serialVersionUID = 6599940742254659537L;

			public void buttonClick(ClickEvent event) {							
				renderRemoveRoleQueuesFormUI();
			}});				
		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_roleQueuesLabel"),SectionTitleBar.MIDDLEFONT,roleQueuesActionbarContainer);		
		containerPanel.addComponent(sectionTitleBar);			
		RoleQueuesTable roleQueuesTable=new RoleQueuesTable(roleQueueArray,this.userClientInfo,activityObjectDetail);
		containerPanel.addComponent(roleQueuesTable);
		this.userClientInfo.getEventBlackboard().addListener(roleQueuesTable);
		activityObjectDetail.roleQueuesChangeListenerList.add(roleQueuesTable);
	}
	
	private void renderAddRoleQueueFormUI(){
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm newRoleQueuetDataForm=new BaseForm();
		final TextField roleQueueNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueNameField"));		
		roleQueueNameField.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueNameFieldDesc"));
		roleQueueNameField.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueNameFieldErrMsg"));
		roleQueueNameField.setWidth("250px");
		roleQueueNameField.setRequired(true);		
		newRoleQueuetDataForm.addField("roleQueueName", roleQueueNameField);
	
		final TextField roleQueueDisplayNameField=new TextField(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDisNameField"));		
		roleQueueDisplayNameField.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDisNameFieldDesc"));
		roleQueueDisplayNameField.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDisNameFieldErrMsg"));
		roleQueueDisplayNameField.setWidth("250px");
		roleQueueDisplayNameField.setRequired(true);		
		newRoleQueuetDataForm.addField("roleQueueDisplayName", roleQueueDisplayNameField);	
		
		final TextArea roleQueueDescTextArea=new TextArea(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDescField"));
		roleQueueDescTextArea.setInputPrompt(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDescFieldDesc"));
		roleQueueDescTextArea.setRequiredError(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueDescFieldErrMsg"));
		roleQueueDescTextArea.setRows(2);
		roleQueueDescTextArea.setColumns(20);
		roleQueueDescTextArea.setRequired(true);
		newRoleQueuetDataForm.addField("roleDescription", roleQueueDescTextArea);
		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_confirmButton"));
		final ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		okButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7030420157948993106L;

			public void buttonClick(ClickEvent event) {				
				newRoleQueuetDataForm.setValidationVisible(true);
				newRoleQueuetDataForm.setComponentError(null);
	            boolean validateResult = newRoleQueuetDataForm.isValid();		            
	            if(validateResult){  
	            	String newRoleQueueName=roleQueueNameField.getValue().toString();
	            	try {	            		
	            		RoleQueue targetRoleQueue=activitySpace.getRoleQueue(newRoleQueueName);	            								
						if(targetRoleQueue!=null){
							newRoleQueuetDataForm.setComponentError(new UserError(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_roleQueueNameExistErrMsg")));
							return;								
						}else{
							newRoleQueuetDataForm.setComponentError(null);
							String newRoleQueueDisplayName=roleQueueDisplayNameField.getValue().toString();
							String newRoleQueueDesc=roleQueueDescTextArea.getValue().toString();													
							doAddNewRoleQueue(newRoleQueueName,newRoleQueueDisplayName,newRoleQueueDesc);								
						}							
					} catch (ActivityEngineRuntimeException e) {							
						e.printStackTrace();
					}		            	
	            }		            
			}
		  });
		
	    Button cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_cancelButton"),new Button.ClickListener() {
			private static final long serialVersionUID = -2607577127632723116L;

			public void buttonClick(ClickEvent event) {               
            	removeAddRoleQueueWindow();
            }
        });
	    cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	    
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(280, 30, Alignment.BOTTOM_LEFT, buttonList);
	    newRoleQueuetDataForm.getLayout().addComponent(addParticipantButtonBar);
		
	    windowContent.addComponent(newRoleQueuetDataForm);
	    String windowTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_windowTitle");
		String subTitle=userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_windowDesc")+" <span style='color:#ce0000; font-weight:bold;'>"+this.activitySpaceName+"</span>";
		addRoleQueueWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(addRoleQueueWindow);	
	}
	
	private void removeAddRoleQueueWindow(){
		this.getApplication().getMainWindow().removeWindow(addRoleQueueWindow);
		addRoleQueueWindow=null;		
	}
	
	private void doAddNewRoleQueue(String newRoleQueueName,String newRoleQueueDisplayName,String newRoleQueueDesc){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);
		RoleQueue _RoleQueue=ActivityComponentFactory.createRoleQueue(newRoleQueueName, this.activitySpaceName, newRoleQueueDisplayName, newRoleQueueDesc);
		try {
			boolean addResult=activitySpace.addRoleQueue(_RoleQueue);
			if(addResult){				
				String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_successMsg1")+" "+newRoleQueueName+" "+
						userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_addRoleQueue_successMsg2")+
						" "+this.activitySpaceName;
				getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
				removeAddRoleQueueWindow();
				this.userClientInfo.getEventBlackboard().fire(new RoleQueuesChangeEvent(this.activitySpaceName));				
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
	}
	
	private void renderRemoveRoleQueuesFormUI(){
		Embedded roleQueueIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoleQueue_blackIcon);		
		Label removeRoleQueuesWindowLable = new Label(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_removeRoleQueue_windowTitle"), Label.CONTENT_XHTML);
		final OptionGroup roleQueuesSelect = new OptionGroup("");
		roleQueuesSelect.setMultiSelect(true);
		roleQueuesSelect.setNullSelectionAllowed(true);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(propertyName_RoleQueueName, String.class,null);
        container.addContainerProperty(propertyName_RoleQueueDisplayName, String.class,null);
        roleQueuesSelect.setContainerDataSource(container);
        roleQueuesSelect.setItemCaptionPropertyId(propertyName_RoleQueueDisplayName);
		try {
			RoleQueue[] roleQueuesInSpace=activitySpace.getRoleQueues();			
			for(int i=0;i<roleQueuesInSpace.length;i++){				
				RoleQueue currentRoleQueue=roleQueuesInSpace[i];
				String id = ""+i;
				Item item = container.addItem(id);	
				String roleQueueName=currentRoleQueue.getQueueName();
				String roleQueueCaption=currentRoleQueue.getDisplayName()+" ( "+currentRoleQueue.getQueueName()+" )";
				item.getItemProperty(propertyName_RoleQueueName).setValue(roleQueueName);  
				item.getItemProperty(propertyName_RoleQueueDisplayName).setValue(roleQueueCaption);		
				
			}						
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		}
		
		VerticalLayout roleQueuesSelectContainerLayout=new VerticalLayout();		
		roleQueuesSelectContainerLayout.addComponent(roleQueuesSelect);		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_removeRoleQueue_deleteButton"));		
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -890832831798011824L;

			public void buttonClick(ClickEvent event) {					
				Set selectedQueueIdxSet=(Set)roleQueuesSelect.getValue();				
				Object[] idxArry=selectedQueueIdxSet.toArray();	
				String[] selectedQueueNameArray=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					selectedQueueNameArray[i]=selecteItem.getItemProperty(propertyName_RoleQueueName).getValue().toString();					
				}
				doRemoveRoleQueues(selectedQueueNameArray);	
			}
		  });
		
		Button cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_removeRoleQueue_cancelButton"),new Button.ClickListener() {			
			private static final long serialVersionUID = -5889191990762784305L;

			public void buttonClick(ClickEvent event) {               
				removeRemoveRoleQueueWindow();			
	        }
	    });
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		buttonList.add(okButton);
		buttonList.add(cancelAddbutton);
		BaseButtonBar selectParticipantsButtonBar = new BaseButtonBar(100, 30, Alignment.BOTTOM_RIGHT, buttonList);		
		roleQueuesSelectContainerLayout.addComponent(selectParticipantsButtonBar);		
		roleQueueSelectWindow=new LightContentWindow(roleQueueIcon,removeRoleQueuesWindowLable,roleQueuesSelectContainerLayout,"500px");		
		roleQueueSelectWindow.center();
		this.getApplication().getMainWindow().addWindow(roleQueueSelectWindow);
	}
	
	private void removeRemoveRoleQueueWindow(){
		this.getApplication().getMainWindow().removeWindow(roleQueueSelectWindow);
		roleQueueSelectWindow=null;		
	}
	
	private void doRemoveRoleQueues(String[] roleQueueNames){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		boolean removeResult=true;
		for(String roleQueueName:roleQueueNames){
			try {
				removeResult=activitySpace.removeRoleQueue(roleQueueName)&removeResult;
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			}
		}
		if(removeResult){
			String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_roleQueuesUI_removeRoleQueue_successMsg")+" "+this.activitySpaceName;
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
			removeRemoveRoleQueueWindow();
			this.userClientInfo.getEventBlackboard().fire(new RoleQueuesChangeEvent(this.activitySpaceName));
		}		
	}
}