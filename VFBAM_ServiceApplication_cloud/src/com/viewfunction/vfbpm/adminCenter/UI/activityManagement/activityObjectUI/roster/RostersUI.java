package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster;

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
import com.viewfunction.activityEngine.activityView.Roster;
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

public class RostersUI  extends VerticalLayout{
	private static final long serialVersionUID = 2025950037644049679L;
	private UserClientInfo userClientInfo;
	private String activitySpaceName;
	private DialogWindow addRosterWindow;	
	private String propertyName_RosterName="propertyName_RosterName";
	private String propertyName_RosterDisplayName="propertyName_RosterDisplayName";
	private LightContentWindow rosterSelectWindow;
	public RostersUI(Roster[] rosterArray, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail,String activitySpaceName){
		this.userClientInfo=userClientInfo;	
		this.activitySpaceName=activitySpaceName;
		//render title bar		
		MainTitleBar mainTitleBar=new MainTitleBar(this.activitySpaceName,userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_rostersLabel"));
		this.addComponent(mainTitleBar);	
		
		//render Roster list		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);		
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRosterTitleIcon);			
		HorizontalLayout rostersActionbarContainer=new HorizontalLayout();			
		rostersActionbarContainer.setWidth("60px");		
		Button addNewRosterButton=new Button();
		addNewRosterButton.setCaption(null);
		addNewRosterButton.setIcon(UICommonElementDefination.ICON_addRoster);
		addNewRosterButton.setStyleName(BaseTheme.BUTTON_LINK);
		addNewRosterButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRosterButton"));		
		rostersActionbarContainer.addComponent(addNewRosterButton);		
		addNewRosterButton.addListener(new ClickListener(){							
			private static final long serialVersionUID = 3629890609353359755L;

			public void buttonClick(ClickEvent event) {							
				renderAddRoosterFormUI();
			}});		
		
		Button removeRostersButton=new Button();
		removeRostersButton.setCaption(null);
		removeRostersButton.setIcon(UICommonElementDefination.ICON_removeRoster);
		removeRostersButton.setStyleName(BaseTheme.BUTTON_LINK);
		removeRostersButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_removeRosterButton"));		
		rostersActionbarContainer.addComponent(removeRostersButton);		
		removeRostersButton.addListener(new ClickListener(){
			private static final long serialVersionUID = 802028560811525362L;

			public void buttonClick(ClickEvent event) {							
				renderRemoveRostersFormUI();
			}});				
		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_rostersLabel"),SectionTitleBar.MIDDLEFONT,rostersActionbarContainer);
		containerPanel.addComponent(sectionTitleBar);
		RostersTable rostersTable=new RostersTable(rosterArray,this.userClientInfo,activityObjectDetail);
		containerPanel.addComponent(rostersTable);
		this.userClientInfo.getEventBlackboard().addListener(rostersTable);
		activityObjectDetail.rostersChangeListenerList.add(rostersTable);		
	}
	
	private void renderAddRoosterFormUI(){
		VerticalLayout windowContent = new VerticalLayout();		
		final BaseForm newRosterDataForm=new BaseForm();
		final TextField rosterNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterNameField"));		
		rosterNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterNameFieldDesc"));
		rosterNameField.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterNameFieldErrMsg"));
		rosterNameField.setWidth("250px");
		rosterNameField.setRequired(true);		
		newRosterDataForm.addField("rosterName", rosterNameField);
	
		final TextField rosterDisplayNameField=new TextField(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterDisplayNameField"));		
		rosterDisplayNameField.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterDisplayNameFieldDesc"));
		rosterDisplayNameField.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterDisplayNameFieldErrMsg"));
		rosterDisplayNameField.setWidth("250px");
		rosterDisplayNameField.setRequired(true);		
		newRosterDataForm.addField("rosterDisplayName", rosterDisplayNameField);	
		
		final TextArea rosterDescTextArea=new TextArea(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterDescField"));
		rosterDescTextArea.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterDescFieldDesc"));
		rosterDescTextArea.setRequiredError(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterDescFieldErrMsg"));
		rosterDescTextArea.setRows(2);
		rosterDescTextArea.setColumns(20);
		rosterDescTextArea.setRequired(true);
		newRosterDataForm.addField("roleDescription", rosterDescTextArea);
		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_confirmButton"));
		final ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		okButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = 186922200281575785L;

			public void buttonClick(ClickEvent event) {				
				newRosterDataForm.setValidationVisible(true);
				newRosterDataForm.setComponentError(null);
	            boolean validateResult = newRosterDataForm.isValid();		            
	            if(validateResult){  
	            	String newRosterName=rosterNameField.getValue().toString();
	            	try {	            		
	            		Roster targetRoster=activitySpace.getRoster(newRosterName); 	            			            								
						if(targetRoster!=null){
							newRosterDataForm.setComponentError(new UserError(userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_rosterExistErrMsg")));
							return;								
						}else{
							newRosterDataForm.setComponentError(null);
							String newRosterDisplayName=rosterDisplayNameField.getValue().toString();
							String newRosterDesc=rosterDescTextArea.getValue().toString();													
							doAddNewRoster(newRosterName,newRosterDisplayName,newRosterDesc);								
						}							
					} catch (ActivityEngineRuntimeException e) {							
						e.printStackTrace();
					}		            	
	            }		           
			}
		  });
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_cancelButton"),new Button.ClickListener() {			
			private static final long serialVersionUID = -3817357566564230916L;

			public void buttonClick(ClickEvent event) {               
            	removeAddRosterWindow();
            }
        });
	    cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	    
	    buttonList.add(okButton);
	    buttonList.add(cancelAddbutton);
	    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(280, 30, Alignment.BOTTOM_LEFT, buttonList);
	    newRosterDataForm.getLayout().addComponent(addParticipantButtonBar);
		
	    windowContent.addComponent(newRosterDataForm);
	    String windowTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_windowtitle");
		String subTitle=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_windowtitleDesc")+" "+" <span style='color:#ce0000; font-weight:bold;'>"+this.activitySpaceName+"</span>";
		addRosterWindow= UIComponentCreator.createDialogWindow_AddData(windowTitle,subTitle, windowContent);		
		this.getApplication().getMainWindow().addWindow(addRosterWindow);			
	}
	
	private void removeAddRosterWindow(){
		this.getApplication().getMainWindow().removeWindow(addRosterWindow);
		addRosterWindow=null;		
	}
	
	private void doAddNewRoster(String rosterName,String rosterDisplayName,String rosterDesc){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);
		Roster roster=ActivityComponentFactory.createRoster(this.activitySpaceName, rosterName);
		roster.setDescription(rosterDesc);
		roster.setDisplayName(rosterDisplayName);			
		try {
			boolean addResult=activitySpace.addRoster(roster);
			if(addResult){				
				String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_addSuccessmsg1")+" "+rosterName+" "+
						this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_addRoster_addSuccessmsg2")+
						" "+this.activitySpaceName;
				getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
				removeAddRosterWindow();
				this.userClientInfo.getEventBlackboard().fire(new RostersChangeEvent(this.activitySpaceName));				
			}				
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}	
	
	private void renderRemoveRostersFormUI(){
		Embedded rosterIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceRoster_blackIcon);		
		Label removeRosterWindowLable = new Label(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_deleteRoster_windowtitle"), Label.CONTENT_XHTML);
		final OptionGroup rostersSelect = new OptionGroup("");
		rostersSelect.setMultiSelect(true);
		rostersSelect.setNullSelectionAllowed(true);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty(propertyName_RosterName, String.class,null);
        container.addContainerProperty(propertyName_RosterDisplayName, String.class,null);
        rostersSelect.setContainerDataSource(container);
        rostersSelect.setItemCaptionPropertyId(propertyName_RosterDisplayName);
		try {
			Roster[] rostersInSpace=activitySpace.getRosters();	
			if(rostersInSpace!=null){
				for(int i=0;i<rostersInSpace.length;i++){				
					Roster currentRoster=rostersInSpace[i];
					String id = ""+i;
					Item item = container.addItem(id);	
					String rosterName=currentRoster.getRosterName();
					String rosterCaption=currentRoster.getDisplayName()+" ( "+rosterName+" )";
					item.getItemProperty(propertyName_RosterName).setValue(rosterName);  
					item.getItemProperty(propertyName_RosterDisplayName).setValue(rosterCaption);				
				}			
			}							
		} catch (ActivityEngineRuntimeException e) {		
			e.printStackTrace();
		}
		
		VerticalLayout rostersSelectContainerLayout=new VerticalLayout();		
		rostersSelectContainerLayout.addComponent(rostersSelect);		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_deleteRoster_deleteButton"));		
		okButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 5172916522228531284L;

			public void buttonClick(ClickEvent event) {					
				Set selectedQosterIdxSet=(Set)rostersSelect.getValue();				
				Object[] idxArry=selectedQosterIdxSet.toArray();	
				String[] selectedRosterNameArray=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					selectedRosterNameArray[i]=selecteItem.getItemProperty(propertyName_RosterName).getValue().toString();					
				}
				doRemoveRosters(selectedRosterNameArray);	
			}
		  });
		Button cancelAddbutton = new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_deleteRoster_cancelButton"),new Button.ClickListener() {			
			private static final long serialVersionUID = -6151632765915445728L;

			public void buttonClick(ClickEvent event) {               
				removeRemoveRosterWindow();			
	        }
	    });
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
		    
		buttonList.add(okButton);
		buttonList.add(cancelAddbutton);
		BaseButtonBar selectRostersButtonBar = new BaseButtonBar(150, 30, Alignment.BOTTOM_RIGHT, buttonList);		
		rostersSelectContainerLayout.addComponent(selectRostersButtonBar);		
		rosterSelectWindow=new LightContentWindow(rosterIcon,removeRosterWindowLable,rostersSelectContainerLayout,"500px");		
		rosterSelectWindow.center();
		this.getApplication().getMainWindow().addWindow(rosterSelectWindow);
	}
	
	private void removeRemoveRosterWindow(){
		this.getApplication().getMainWindow().removeWindow(rosterSelectWindow);
		rosterSelectWindow=null;		
	}
	
	private void doRemoveRosters(String[] rosterNames){
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
		boolean removeResult=true;
		for(String rosterName:rosterNames){
			try {
				removeResult=activitySpace.removeRoster(rosterName)&removeResult;
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			}
		}
		if(removeResult){
			String resultMessage=this.userClientInfo.getI18NProperties().getProperty("ActivityManage_rostersUI_deleteRoster_deleteSuccessMsg")+" "+this.activitySpaceName;
			getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);
			removeRemoveRosterWindow();
			this.userClientInfo.getEventBlackboard().fire(new RostersChangeEvent(this.activitySpaceName));
		}		
	}
}