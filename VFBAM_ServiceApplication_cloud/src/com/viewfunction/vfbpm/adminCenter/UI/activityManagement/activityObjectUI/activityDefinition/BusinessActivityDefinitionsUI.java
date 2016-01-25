package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.MainTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class BusinessActivityDefinitionsUI extends VerticalLayout{
	private static final long serialVersionUID = -4818026413945443511L;

	private UserClientInfo userClientInfo;
	private String activitySpaceName;	
	private LightContentWindow lightContentWindow;
	
	public BusinessActivityDefinitionsUI(BusinessActivityDefinition[] activityDefinitionArray, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail,String activitySpaceName){
		this.userClientInfo=userClientInfo;
		this.activitySpaceName=activitySpaceName;
		//render title bar		
		MainTitleBar mainTitleBar=new MainTitleBar(this.activitySpaceName,
				userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_businessActivityDefinitionsLabel"));
		this.addComponent(mainTitleBar);		
		//render Roster list		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);				
		Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceActivityDefineTitleIcon);		
		
		HorizontalLayout activityDefinitionsActionbarContainer=new HorizontalLayout();			
		activityDefinitionsActionbarContainer.setWidth("30px");		
		Button addNewDefinitionButton=new Button();
		addNewDefinitionButton.setCaption(null);
		addNewDefinitionButton.setIcon(UICommonElementDefination.ICON_addActivityDefine);
		addNewDefinitionButton.setStyleName(BaseTheme.BUTTON_LINK);
		addNewDefinitionButton.setDescription(userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsUI_addNewActivityTypeButton"));		
		activityDefinitionsActionbarContainer.addComponent(addNewDefinitionButton);
		
		addNewDefinitionButton.addListener(new ClickListener(){			
			private static final long serialVersionUID = 6801823384177534773L;

			public void buttonClick(ClickEvent event) {							
				renderAddNewActivityDefinitionUI();
			}});			
		
		SectionTitleBar sectionTitleBar=new SectionTitleBar(iconEmbedded,userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsUI_businessActivitysLabel"),SectionTitleBar.MIDDLEFONT,activityDefinitionsActionbarContainer);
		containerPanel.addComponent(sectionTitleBar);
		BusinessActivityDefinitionsTable businessActivityDefinitionsTable=new BusinessActivityDefinitionsTable(activityDefinitionArray,userClientInfo,activityObjectDetail);
		containerPanel.addComponent(businessActivityDefinitionsTable);		
		this.userClientInfo.getEventBlackboard().addListener(businessActivityDefinitionsTable);
		activityObjectDetail.activityDefinitionsChangeListenerList.add(businessActivityDefinitionsTable);
	}
	
	private void renderAddNewActivityDefinitionUI(){
		Embedded expDataFieldsIcon=new Embedded(null, UICommonElementDefination.AppPanel_activitySpaceBusinessActivity_blackIcon);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsUI_addNewActivityTypeLabel")+" "				
				+"</b> <b style='color:#ce0000;'>" + this.activitySpaceName+ "</b>", Label.CONTENT_XHTML);		
		final NewBusinessActivityDefinitionEditor newBusinessActivityDefinitionEditor=new NewBusinessActivityDefinitionEditor(this.activitySpaceName,this.userClientInfo);		
		VerticalLayout containerLayout=new VerticalLayout();
		containerLayout.addComponent(newBusinessActivityDefinitionEditor);		
		
		Button confirmButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsUI_addNewActivityType_confirmButton"));
		confirmButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 204487876444255273L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
				doAddActivityTypeDefinition(newBusinessActivityDefinitionEditor);
			}
	     });  				
	    
	    Button cancelButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsUI_addNewActivityType_cancelButton"));        
	    cancelButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -4597034005810823101L;

			public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {	 
				removeLightWindow();
	           }
	     });	
	    cancelButton.setStyleName(BaseTheme.BUTTON_LINK);
        List<Button> buttonList = new ArrayList<Button>();
        buttonList.add(confirmButton);
        buttonList.add(cancelButton); 
        containerLayout.addComponent(new BaseButtonBar(300, 50, Alignment.MIDDLE_RIGHT, buttonList));		
		lightContentWindow=new LightContentWindow(expDataFieldsIcon,propertyNameLable,containerLayout,"1000px");	
		lightContentWindow.center();		
		lightContentWindow.setModal(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);			
	}	
	
	private void removeLightWindow(){
		this.getApplication().getMainWindow().removeWindow(lightContentWindow);
		lightContentWindow=null;
	}
	
	private void doAddActivityTypeDefinition(NewBusinessActivityDefinitionEditor newBusinessActivityDefinitionEditor){
		if(newBusinessActivityDefinitionEditor.activityType!=null&&newBusinessActivityDefinitionEditor.activityDefinitionResourceFile!=null){			
			String[] exposedSteps=new String[0];			
			BusinessActivityDefinition bsd=ActivityComponentFactory.createBusinessActivityDefinition(newBusinessActivityDefinitionEditor.activityType, this.activitySpaceName,exposedSteps);
			FileInputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(newBusinessActivityDefinitionEditor.activityDefinitionResourceFile);
				bsd.setDefinitionResource(fileInputStream);	
			} catch (FileNotFoundException e) {				
				e.printStackTrace();
			}
			if(newBusinessActivityDefinitionEditor.launchUserIdentityAttributeName!=null){
				bsd.setLaunchUserIdentityAttributeName(newBusinessActivityDefinitionEditor.launchUserIdentityAttributeName);
			}
			if(newBusinessActivityDefinitionEditor.launchDecisionPointAttributeName!=null&&newBusinessActivityDefinitionEditor.launchDecisionPointChoiseList!=null){
				bsd.setLaunchDecisionPointAttributeName(newBusinessActivityDefinitionEditor.launchDecisionPointAttributeName);
				bsd.setLaunchDecisionPointChoiseList(newBusinessActivityDefinitionEditor.launchDecisionPointChoiseList);
			}
			ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(this.activitySpaceName);	
			try {
				boolean result=activitySpace.addBusinessActivityDefinition(bsd);
				if(result){
					String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsUI_addNewActivityType_addSuccessmsg1")+" "+newBusinessActivityDefinitionEditor.activityType+" "+userClientInfo.getI18NProperties().getProperty("ActivityManage_BusinessActivityDefinitionsUI_addNewActivityType_addSuccessmsg2");
					getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);							
					this.userClientInfo.getEventBlackboard().fire(new ActivityDefinitionsChangeEvent(this.activitySpaceName));					
					removeLightWindow();
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
		}else{
			return;
		}	
	}
}