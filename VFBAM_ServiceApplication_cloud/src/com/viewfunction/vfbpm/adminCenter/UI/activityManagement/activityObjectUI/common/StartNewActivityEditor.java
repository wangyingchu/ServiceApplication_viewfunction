package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.DataFieldDefinition;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class StartNewActivityEditor extends VerticalLayout{
	private static final long serialVersionUID = 1500457092163099270L;	

	private UserClientInfo userClientInfo;
	private BusinessActivityDefinition businessActivityDefinition;	
	private Map<String,ActivityData> activityDataMap;	
	private Button okButton;
	private Button cancelAddbutton;
	private Label operationResult;
	private List<Button> buttonList;
	
	public StartNewActivityEditor(BusinessActivityDefinition businessActivityDefinition,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		this.businessActivityDefinition=businessActivityDefinition;	
		this.activityDataMap=new HashMap<String,ActivityData>();		
		
		DataFieldDefinition[] dataFieldDefinitionArry=this.businessActivityDefinition.getActivityDataFields();			
		DataFieldValueEditor dataFieldValueEditor=new DataFieldValueEditor(dataFieldDefinitionArry,this.userClientInfo,this.activityDataMap);
		this.addComponent(dataFieldValueEditor);
		operationResult=new Label("",Label.CONTENT_XHTML);
		this.addComponent(operationResult);
		
		String launchDecisionPointAttribute=businessActivityDefinition.getLaunchDecisionPointAttributeName();
		String[] launchDecisionPointOptionList=businessActivityDefinition.getLaunchDecisionPointChoiseList();		
		buttonList = new ArrayList<Button>();
		if(launchDecisionPointAttribute!=null&&launchDecisionPointOptionList!=null){			
			for(final String option:launchDecisionPointOptionList){
				Button optionButton=new Button(option);
				optionButton.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = -7749827930028765174L;

					public void buttonClick(ClickEvent event) {	 
						startNewActivity(option);
		            }
		        });				
				buttonList.add(optionButton);				
			}
		}else{
			okButton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_StartNewActivityEditor_startActivityButton"));	
			okButton.addListener(new Button.ClickListener() {				
				private static final long serialVersionUID = -7749827930028765174L;

				public void buttonClick(ClickEvent event) {	 
					startNewActivity(null);
	            }
	        });	
			 buttonList.add(okButton);
		}		
			
		cancelAddbutton = new Button(userClientInfo.getI18NProperties().getProperty("ActivityManage_StartNewActivityEditor_cancelStartActivityButton"));		
		cancelAddbutton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -3816272036133989826L;

			public void buttonClick(ClickEvent event) {	 
            	closeWindow();
            }
        });		
		cancelAddbutton.setStyleName(BaseTheme.BUTTON_LINK);
	    buttonList.add(cancelAddbutton);
	    
	    int buttonBarWidth=270;
	    if(launchDecisionPointAttribute!=null){
	    	buttonBarWidth=600;
	    }
	    BaseButtonBar startNewActivityButtonBar = new BaseButtonBar(buttonBarWidth, 45, Alignment.MIDDLE_RIGHT, buttonList);
	    this.addComponent(startNewActivityButtonBar);
	}
	
	private void closeWindow(){	
		LightContentWindow ContainerWindow=(LightContentWindow)(this.getParent().getParent());
		this.getApplication().getMainWindow().removeWindow(ContainerWindow);
		ContainerWindow=null;		
	}
	
	private void setOperationResultMessage(String activityType,String activityId){	
		for(Button button:buttonList){
			button.setEnabled(false);
		}
		cancelAddbutton.setEnabled(true);		
		cancelAddbutton.setCaption(userClientInfo.getI18NProperties().getProperty("ActivityManage_StartNewActivityEditor_closeStartActivityResultButton"));
		String resultMessage=userClientInfo.getI18NProperties().getProperty("ActivityManage_StartNewActivityEditor_startActivitySussessMsg1")+
				" "+activityType+"("+activityId+") "+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_StartNewActivityEditor_startActivitySussessMsg2");
		operationResult.setValue("<span style='color:#2779c7;font-weight:bold;'>"+resultMessage+"</span>");		
		getWindow().showNotification(resultMessage,Notification.TYPE_HUMANIZED_MESSAGE);		
	}
	
	private void startNewActivity(String decisionPointOption){
		String activitySpaceName=this.businessActivityDefinition.getActivitySpaceName();
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);			
		Collection<ActivityData> activityDataCollection=activityDataMap.values();
		ActivityData[] startDataArray=new ActivityData[activityDataMap.size()];
		activityDataCollection.toArray(startDataArray);	
		String[] launchProcessVariableList=businessActivityDefinition.getLaunchProcessVariableList();
		String launchUserIdnAttrName=businessActivityDefinition.getLaunchUserIdentityAttributeName();		
		BusinessActivity resultBusinessActivity;
		//for system admin launch process from admin console,process started by admin can't be track in client UI
		String participantName=userClientInfo.getUserParticipant()!=null?userClientInfo.getUserParticipant().getParticipantName():null;
		try {			
			if(decisionPointOption!=null||launchProcessVariableList!=null||launchUserIdnAttrName!=null){
				 Map<String,Object> variables=new HashMap<String,Object>();
				 if(decisionPointOption!=null){
					 String launchDecisionPointAttribute=businessActivityDefinition.getLaunchDecisionPointAttributeName();
					 variables.put(launchDecisionPointAttribute, decisionPointOption);
				 }
				 if(launchUserIdnAttrName!=null){					 
					 variables.put(launchUserIdnAttrName, userClientInfo.getUserParticipant().getParticipantName());
				 }
				 if(launchProcessVariableList!=null){
					 for(String variableName:launchProcessVariableList){						 
						if( activityDataMap.get(variableName)!=null){							
							 variables.put(variableName,  activityDataMap.get(variableName).getDatFieldValue());
						}						 
					 }
				 }				 
				 resultBusinessActivity=activitySpace.launchBusinessActivity(this.businessActivityDefinition.getActivityType(), startDataArray,variables,participantName);	
			}else{
				resultBusinessActivity=activitySpace.launchBusinessActivity(this.businessActivityDefinition.getActivityType(), startDataArray,participantName);
			}				
			String activityId=resultBusinessActivity.getActivityId();			
			String activityType=this.businessActivityDefinition.getActivityType();	
			setOperationResultMessage(activityType,activityId);						
		} catch (ActivityEngineRuntimeException e) {
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {
			e.printStackTrace();
		}			
	}	
}