package com.viewfunction.vfbpm.adminCenter.UI.activityManagement;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.DialogWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.UIComponentCreator;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityObjectBrowser extends VerticalLayout{	
	
	private static final long serialVersionUID = 5846781058678999443L;
	private ActivityManagementPanel activityManagementPanel;
	public UserClientInfo userClientInfo;
	private DialogWindow addActivitySpaceWindow;
	private DialogWindow confirmAddActivitySpaceWindow;
	private Accordion activitySpaceAccordion;
	public ActivityObjectBrowser(UserClientInfo userClientInfo){		
		this.setStyleName(Reindeer.LAYOUT_BLACK);			
		this.setSizeFull();
		this.userClientInfo=userClientInfo;
		
		MenuBar contentOperationMenubar = new MenuBar();
		contentOperationMenubar.setWidth("100%");
		MenuBar.MenuItem operationItems = contentOperationMenubar.addItem(
				userClientInfo.getI18NProperties().getProperty("ActivityManage_operationMenuBar"),UICommonElementDefination.AppMenu_operationMenuIcon, null);
		
		MenuItem createSpaceItem=operationItems.addItem(userClientInfo.getI18NProperties().getProperty("ActivityManage_operationMenuItem_createActivitySpace"),null);
		createSpaceItem.setIcon(UICommonElementDefination.AppMenu_createSpaceMenuIcon);
		createSpaceItem.setCommand(new Command(){
			private static final long serialVersionUID = 7257629598961229366L;

			public void menuSelected(MenuItem selectedItem) {
				renderAddActivitySpaceUI();				
			}});		
		
		//How to implement delete ActivitySpace?
		//MenuItem deleteSpaceItem=operationItems.addItem(userClientInfo.getI18NProperties().getProperty("ActivityManage_operationMenuItem_deleteActivitySpace"),null);		
		//deleteSpaceItem.setIcon(UICommonElementDefination.AppMenu_deleteSpaceMenuIcon);
		
		this.addComponent(contentOperationMenubar);	
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		
		Label treeTitleLabel=new Label("-&nbsp;&nbsp;"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_activityObjectTree"),Label.CONTENT_XHTML);
		treeTitleLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_TreeTitleText);		
		containerPanel.addComponent(treeTitleLabel);		
		
		this.activitySpaceAccordion= new Accordion();	   
		activitySpaceAccordion.setStyleName(Reindeer.LAYOUT_BLACK);		
		try {
			ActivitySpace[] activitySpaceArray=ActivityComponentFactory.getActivitySpaces();
			if(activitySpaceArray!=null){
				for(int i=0;i<activitySpaceArray.length;i++){					
					ActivitySpace currentActivitySpace=activitySpaceArray[i];
					ActivitySpaceComponentDetailTrees activitySpaceTree=new ActivitySpaceComponentDetailTrees(userClientInfo,currentActivitySpace);
					activitySpaceAccordion.addTab(activitySpaceTree,currentActivitySpace.getActivitySpaceName());					
				}
			}						
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
			// add error message dialog 
		}
		
	    containerPanel.addComponent(activitySpaceAccordion);		
		this.addComponent(containerPanel);
		setExpandRatio(containerPanel, 1.0F);
	}
	
	public void setActivityManagementPanel(ActivityManagementPanel activityManagementPanel){
		this.activityManagementPanel=activityManagementPanel;
	}
	
	public ActivityManagementPanel getActivityManagementPanel(){
		return activityManagementPanel;
	}
	
	public void renderAddActivitySpaceUI(){
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_addAcvitivySpaceWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_addAcvitivySpaceWindowDesc");		
        VerticalLayout setStatusLayout = new VerticalLayout();        
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);		
		final TextField activitySpaceNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_acvitivySpaceNameField"));	
		activitySpaceNameField.setWidth("250px");
		activitySpaceNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_acvitivySpaceNameFieldPrompt"));	
		inputFieldListLayout.addComponent(activitySpaceNameField);		
		setStatusLayout.addComponent(inputFieldListLayout);	
		
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = 5250048551843829937L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				if(activitySpaceNameField.getValue()==null||activitySpaceNameField.getValue().toString().equals("")){
					return;
				}else{
					String activitySpaceName=activitySpaceNameField.getValue().toString();					
					doAddActivitySpace(activitySpaceName);
				}				
			}			
	     }); 		
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionUI_addStep_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 576678508116648621L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				removeAddActivitySpaceWindow();
	           }
	     });	
	    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmButton);
        buttonList.add(cancelButton); 
        setStatusLayout.addComponent(new BaseButtonBar(120, 30, Alignment.MIDDLE_RIGHT, buttonList));
        addActivitySpaceWindow=UIComponentCreator.createDialogWindow_AddData(windowTitle, windowDesc, 420,setStatusLayout);
        addActivitySpaceWindow.setResizable(false);
        addActivitySpaceWindow.center();
        addActivitySpaceWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(addActivitySpaceWindow);			
	}
	
	private void removeAddActivitySpaceWindow(){
		this.getApplication().getMainWindow().removeWindow(addActivitySpaceWindow);
		addActivitySpaceWindow=null;
	}
	
	private void removeAddActivitySpaceConfirmWindow(){
		this.getApplication().getMainWindow().removeWindow(confirmAddActivitySpaceWindow);
		confirmAddActivitySpaceWindow=null;
	}
	
	private void doAddActivitySpace(final String activitySpaceName){	
		String windowTitle =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_confirmAddAcvitivySpaceWindowTitle");
        String windowDesc =this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_confirmAddAcvitivySpaceWindowDesc");		
        VerticalLayout setStatusLayout = new VerticalLayout();        
		HorizontalLayout inputFieldListLayout=new HorizontalLayout();	
		HorizontalLayout leftSpaceLayout=new HorizontalLayout();	
		leftSpaceLayout.setWidth("20px");
		inputFieldListLayout.addComponent(leftSpaceLayout);		
		Label confirmText=new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_confirmMessage1")+
				" <span style='color:#ce0000; font-weight:bold;'>"+activitySpaceName+"</span>. "
				+this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_confirmMessage2"),Label.CONTENT_XHTML);
		inputFieldListLayout.addComponent(confirmText);		
		setStatusLayout.addComponent(inputFieldListLayout);	
		
		HorizontalLayout spaceDivLayout=new HorizontalLayout();
		spaceDivLayout.setHeight("15px");
		setStatusLayout.addComponent(spaceDivLayout);			
		
		Button confirmButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_confirmAddButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5615138835604783060L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				removeAddActivitySpaceWindow();
				removeAddActivitySpaceConfirmWindow();				
				ActivitySpace activitySpace=null;
				ActivitySpace[] activitySpaceArray=null;
				try {
					activitySpaceArray = ActivityComponentFactory.getActivitySpaces();
				} catch (ActivityEngineException e1) {				
					e1.printStackTrace();
				}
				if(activitySpaceArray==null||activitySpaceArray.length==0){										
					try {						
						activitySpace=ActivityComponentFactory.createInitActivitySpace(activitySpaceName);
					} catch (ActivityEngineException e) {
						System.out.println("Create Init ActivitySpace: "+activitySpaceName);
						//e.printStackTrace();
						try {							
							activitySpace=ActivityComponentFactory.createActivitySpace(activitySpaceName);
						} catch (ActivityEngineException e1) {							
							e1.printStackTrace();
						}						
					}
				}else{					
					try {
						activitySpace=ActivityComponentFactory.createActivitySpace(activitySpaceName);
					} catch (ActivityEngineException e) {						
						e.printStackTrace();
					}	
				}				
				if(activitySpace!=null){		
					String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_addedMessage1")+" "+activitySpaceName+" "+
							userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_addedMessage2");
					getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
					ActivitySpaceComponentDetailTrees activitySpaceTree=new ActivitySpaceComponentDetailTrees(userClientInfo,activitySpace);
				    activitySpaceAccordion.addTab(activitySpaceTree,activitySpace.getActivitySpaceName());			   
				}					
			}			
	     }); 		
	    
	    Button cancelButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityObjectBrowser_cancelAddButton"));        
	    cancelButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 7004362243436392097L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {					
				removeAddActivitySpaceConfirmWindow();
				return;
	           }
	     });	
	    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmButton);
        buttonList.add(cancelButton); 
        setStatusLayout.addComponent(new BaseButtonBar(450, 30, Alignment.MIDDLE_RIGHT, buttonList));
        confirmAddActivitySpaceWindow=UIComponentCreator.createDialogWindow_AddData_Confirm(windowTitle, windowDesc, setStatusLayout);
        confirmAddActivitySpaceWindow.setWidth("800px");
        confirmAddActivitySpaceWindow.setResizable(false);
        confirmAddActivitySpaceWindow.center();
        confirmAddActivitySpaceWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(confirmAddActivitySpaceWindow);
	}
}