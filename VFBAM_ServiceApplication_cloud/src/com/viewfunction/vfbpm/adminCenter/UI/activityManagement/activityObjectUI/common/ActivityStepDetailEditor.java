package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityStepDetailEditor extends VerticalLayout{
	private static final long serialVersionUID = 2114312585437387850L;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Role relatedRole;
	private Map<String,ActivityData> activityDataMap;	
	private UserClientInfo userClientInfo;
	private ActivityStep activityStep;
	private ReloadableUIElement[] reloadableUIElements;
	private ActivitySpace activitySpace;
	public ActivityStepDetailEditor(ActivityStep activityStep,UserClientInfo userClientInfo,ReloadableUIElement[] reloadableUIElements){
		this.reloadableUIElements=reloadableUIElements;
		this.userClientInfo=userClientInfo;		
		if(this.userClientInfo.getUserActivitySpace()!=null){
			this.activitySpace=ActivityComponentFactory.getActivitySpace(this.userClientInfo.getUserActivitySpace());	
		}else{
			try {
				String activitySpaceName=activityStep.getBusinessActivity().getActivityDefinition().getActivitySpaceName();
				this.activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			} catch (ActivityEngineActivityException e) {				
				e.printStackTrace();
			} catch (ActivityEngineDataException e) {				
				e.printStackTrace();
			}			
		}		
		this.activityStep=activityStep;
		HorizontalLayout activityInfoLayout=new HorizontalLayout();
		activityInfoLayout.setWidth("100%");		
		
		HorizontalLayout activityStepBaseInfoLayout=new HorizontalLayout();		
		String activityStepNameStr=activityStep.getActivityStepName();
		Label activityStepName=new Label("<span style='color:#2984c8;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold; font-size: 14pt;'>"+activityStepNameStr+"</span>",Label.CONTENT_XHTML);		
		String stepDesc=activityStep.getStepDescription();
		activityStepName.setDescription(stepDesc);			
		activityStepBaseInfoLayout.addComponent(activityStepName);		
		
		String activityID=activityStep.getActivityId();		
		Label activityIDLabel=new Label("<span style='color:#444444;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;'> ("+activityID+")</span>",Label.CONTENT_XHTML);
		activityStepBaseInfoLayout.addComponent(activityIDLabel);
		
		String activityType=activityStep.getActivityType();		
		Label activityTypeLabel=new Label("<span style='color:#333333;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;padding-left:15px;font-size: 11pt;'>"+" - "+activityType+"</span>",Label.CONTENT_XHTML);
		activityStepBaseInfoLayout.addComponent(activityTypeLabel);			
		
		activityInfoLayout.addComponent(activityStepBaseInfoLayout);
		activityInfoLayout.setComponentAlignment(activityStepBaseInfoLayout, Alignment.MIDDLE_LEFT);		
		
		HorizontalLayout activityStepUserInfoLayout=new HorizontalLayout();		
		HorizontalLayout startDateInfoLayout=new HorizontalLayout();		
		Embedded startDateiconEmbedded=new Embedded(null, UICommonElementDefination.ICON_stepDetail_clock);
		startDateiconEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_activityCreateTimeLabel"));
		Label activityStartDateLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+formatter.format(activityStep.getCreateTime())+"</span>",Label.CONTENT_XHTML);			
		startDateInfoLayout.addComponent(startDateiconEmbedded);
		startDateInfoLayout.setComponentAlignment(startDateiconEmbedded, Alignment.MIDDLE_LEFT);
		startDateInfoLayout.addComponent(activityStartDateLabel);
		activityStepUserInfoLayout.addComponent(startDateInfoLayout);	
		HorizontalLayout spaceDivLayout_01=new HorizontalLayout();
		spaceDivLayout_01.setWidth("10px");
		activityStepUserInfoLayout.addComponent(spaceDivLayout_01);			
		try {					
			this.relatedRole=activityStep.getRelatedRole();	
			if(this.relatedRole!=null){				
				HorizontalLayout roleInfoLayout=new HorizontalLayout();			
				Embedded iconEmbedded=new Embedded(null, UICommonElementDefination.ICON_Button_participant_getRoles);
				iconEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_taskRoleLabel"));
				String roleDisplayName=this.relatedRole.getDisplayName()!=null?this.relatedRole.getDisplayName():this.relatedRole.getRoleName();
				Label roleNameLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+roleDisplayName+"</span>",Label.CONTENT_XHTML);			
				roleNameLabel.setDescription(this.relatedRole.getDescription());
				roleInfoLayout.addComponent(iconEmbedded);
				roleInfoLayout.setComponentAlignment(iconEmbedded, Alignment.MIDDLE_LEFT);
				roleInfoLayout.addComponent(roleNameLabel);
				activityStepUserInfoLayout.addComponent(roleInfoLayout);
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
		activityInfoLayout.addComponent(activityStepUserInfoLayout);
		activityInfoLayout.setComponentAlignment(activityStepUserInfoLayout, Alignment.MIDDLE_RIGHT);		
		this.addComponent(activityInfoLayout);	
		
		HorizontalLayout vspaceDivLayout=new HorizontalLayout();
		vspaceDivLayout.setHeight("5px");
		this.addComponent(vspaceDivLayout);	
		
		HorizontalLayout activityStepStatusLayout=new HorizontalLayout();
		HorizontalLayout spaceDivLayout_h=new HorizontalLayout();
		spaceDivLayout_h.setWidth("10px");
		activityStepStatusLayout.addComponent(spaceDivLayout_h);
		
		HorizontalLayout stepAssigneeLayout=new HorizontalLayout();
		Embedded iconAssigneeEmbedded=new Embedded(null, UICommonElementDefination.ICON_stepDetail_stepAssignee);
		iconAssigneeEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_taskAssigneeLabel"));
		stepAssigneeLayout.addComponent(iconAssigneeEmbedded);
		Label assigneeNameLabel;
		if(activityStep.getStepAssignee()!=null){
			String assigneeName=null;
			try {
				Participant participant=this.activitySpace.getParticipant(activityStep.getStepAssignee());
				assigneeName=participant.getDisplayName()!=null?participant.getDisplayName():participant.getParticipantName();
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			}
			assigneeNameLabel=new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;'>"+assigneeName+"</span>",Label.CONTENT_XHTML);
		}else{
			assigneeNameLabel=new Label("<span style='color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;'>"+"-"+"</span>",Label.CONTENT_XHTML);
		}
		stepAssigneeLayout.addComponent(assigneeNameLabel);		
		activityStepStatusLayout.addComponent(stepAssigneeLayout);
		
		HorizontalLayout spaceDivLayout_02=new HorizontalLayout();
		spaceDivLayout_02.setWidth("10px");
		activityStepStatusLayout.addComponent(spaceDivLayout_02);
		
		HorizontalLayout stepOwnerLayout=new HorizontalLayout();
		Embedded iconOwnerEmbedded=new Embedded(null, UICommonElementDefination.ICON_stepDetail_stepOwner);
		iconOwnerEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_taskOwnerLabel"));
		stepOwnerLayout.addComponent(iconOwnerEmbedded);
		Label ownerNameLabel;
		if(activityStep.getStepOwner()!=null){
			String ownerName=null;
			try {
				Participant participant=this.activitySpace.getParticipant(activityStep.getStepOwner());
				ownerName=participant.getDisplayName()!=null?participant.getDisplayName():participant.getParticipantName();
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			}
			ownerNameLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+ownerName+"</span>",Label.CONTENT_XHTML);
		}else{
			ownerNameLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+"-"+"</span>",Label.CONTENT_XHTML);
		}
		stepOwnerLayout.addComponent(ownerNameLabel);
		if(activityStep.getStepOwner()!=null){
			activityStepStatusLayout.addComponent(stepOwnerLayout);
		}		
		
		HorizontalLayout spaceDivLayout_03=new HorizontalLayout();
		spaceDivLayout_03.setWidth("10px");
		activityStepStatusLayout.addComponent(spaceDivLayout_03);
		
		HorizontalLayout stepDueDateLayout=new HorizontalLayout();
		Embedded iconDuedateEmbedded=new Embedded(null, UICommonElementDefination.ICON_stepDetail_stepDueDate);
		iconDuedateEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_taskDueDateLabel"));
		stepDueDateLayout.addComponent(iconDuedateEmbedded);
		Label duedateLabel;
		if(activityStep.getDueDate()!=null){
			duedateLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+formatter.format(activityStep.getDueDate())+"</span>",Label.CONTENT_XHTML);
		}else{
			duedateLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;'>"+"-"+"</span>",Label.CONTENT_XHTML);
		}
		stepDueDateLayout.addComponent(duedateLabel);
		if(activityStep.getDueDate()!=null){
			activityStepStatusLayout.addComponent(stepDueDateLayout);
		}		
		
		HorizontalLayout commentSpaceDiv=new HorizontalLayout();
		commentSpaceDiv.setWidth("40px");
		activityStepStatusLayout.addComponent(commentSpaceDiv);
		
		HorizontalLayout commentLayout=new HorizontalLayout();		
		commentLayout.addStyleName("ui_clickableObject");
		Embedded iconCommentEmbedded=new Embedded(null, UICommonElementDefination.ICON_stepDetail_comment);
		iconCommentEmbedded.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_commentLabel"));
		commentLayout.addComponent(iconCommentEmbedded);
		commentLayout.addComponent(new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;cursor:pointer;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_commentLabel")+"</span>",Label.CONTENT_XHTML));		
		commentLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = 7308188814908535925L;

			public void layoutClick(LayoutClickEvent event) {
				renderCommentsUI();
				
			}});		
		activityStepStatusLayout.addComponent(commentLayout);		
		this.addComponent(activityStepStatusLayout);
		
		HorizontalLayout activityStepActionButtonLayout=new HorizontalLayout();
		activityStepActionButtonLayout.setWidth("100%");		
		HorizontalLayout stepResponseActionButtonLayout=new HorizontalLayout();	
		if(activityStep.getStepAssignee()!=null){
			try {
				BusinessActivityDefinition bad= activityStep.getBusinessActivity().getActivityDefinition();			
				String stepDecisionPointAttribute=bad.getStepDecisionPointAttributeName(activityStep.getActivityStepDefinitionKey());			
				String[] stepDecisionPointChoiseOptionList=bad.getStepDecisionPointChoiseList(activityStep.getActivityStepDefinitionKey());				
				if(stepDecisionPointAttribute!=null&&stepDecisionPointChoiseOptionList!=null){				
					for(final String option:stepDecisionPointChoiseOptionList){					
						Button optionButton=new Button(option);
						optionButton.addListener(new Button.ClickListener() {						
							private static final long serialVersionUID = 6054405400675315545L;
	
							public void buttonClick(ClickEvent event) {	 
								completeStep(option);								
				            }
				        });						
						stepResponseActionButtonLayout.addComponent(optionButton);
					}
				}else{
					Button completeStepButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_completeCurrentTaskButton"));
					completeStepButton.addListener(new Button.ClickListener() {						
						private static final long serialVersionUID = 763600888443076582L;
	
						public void buttonClick(ClickEvent event) {	 
							completeStep(null);
			            }
			        });				
					stepResponseActionButtonLayout.addComponent(completeStepButton);
				}			
			} catch (ActivityEngineRuntimeException e1) {			
				e1.printStackTrace();
			} catch (ActivityEngineActivityException e1) {			
				e1.printStackTrace();
			} catch (ActivityEngineDataException e1) {			
				e1.printStackTrace();
			}		
		}
		activityStepActionButtonLayout.addComponent(stepResponseActionButtonLayout);
		activityStepActionButtonLayout.setComponentAlignment(stepResponseActionButtonLayout, Alignment.MIDDLE_LEFT);
		
		int commonActionButtonBarWidth=250;
		List<Button> buttonList = new ArrayList<Button>();
		if(activityStep.getStepAssignee()==null){
			commonActionButtonBarWidth=350;
			Button handleStepActionButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_handleTaskButton"));
			handleStepActionButton.addListener(new Button.ClickListener() {			
				private static final long serialVersionUID = -1285501871498185076L;

				public void buttonClick(ClickEvent event) {	 
					handleStep();
	            }
	        });	
			buttonList.add(handleStepActionButton);
		}	
		
		Button saveDataActionButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_saveTaskDataButton"));
		saveDataActionButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -1285501871498185076L;

			public void buttonClick(ClickEvent event) {	 
				saveStepData();
            }
        });	
		
		Button closeStepDetailUIButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_closeTaskButton"));
		closeStepDetailUIButton.setStyleName(BaseTheme.BUTTON_LINK);	
		closeStepDetailUIButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = -1285501871498185076L;

			public void buttonClick(ClickEvent event) {	 
            	closeWindow();
            }
        });			
		
	    buttonList.add(saveDataActionButton);
	    buttonList.add(closeStepDetailUIButton);	   
	    
	    BaseButtonBar stepCommonActionButtonBar = new BaseButtonBar(commonActionButtonBarWidth, 45, Alignment.MIDDLE_RIGHT, buttonList);		
		activityStepActionButtonLayout.addComponent(stepCommonActionButtonBar);
		activityStepActionButtonLayout.setComponentAlignment(stepCommonActionButtonBar, Alignment.MIDDLE_RIGHT);		
		this.addComponent(activityStepActionButtonLayout);	
		
		HorizontalLayout heightDivLayout=new HorizontalLayout();
		//heightDivLayout.setWidth("100%");
		heightDivLayout.setHeight("10px");
		//heightDivLayout.addStyleName("ui_contentManagementPropertyList_3");	
		this.addComponent(heightDivLayout);	

		HorizontalLayout activityStepDataLayout=new HorizontalLayout();
		activityStepDataLayout.setWidth("100%");		
		
		this.activityDataMap=new HashMap<String,ActivityData>();		
		VerticalLayout stepDatFieldsEditor=new BasicStepDataEditor(activityStep,userClientInfo,activityDataMap);			
		activityStepDataLayout.addComponent(stepDatFieldsEditor);		
		
		VerticalLayout spaceDivLayout=new VerticalLayout();
		spaceDivLayout.setWidth("1px");
		spaceDivLayout.setHeight("100%");		
		spaceDivLayout.addStyleName("ui_userClient_middleDiv");		
		activityStepDataLayout.addComponent(spaceDivLayout);
		
		VerticalLayout spaceDivLayout_2=new VerticalLayout();
		spaceDivLayout_2.setWidth("15px");
		activityStepDataLayout.addComponent(spaceDivLayout_2);
		
		VerticalLayout stepAttachmentLayout=new VerticalLayout();
		stepAttachmentLayout.setWidth("500px");		
		
		ActivityAttachmentEditor activityAttachmentEditor=new ActivityAttachmentEditor(activityStep.getBusinessActivity(),this.userClientInfo);
		stepAttachmentLayout.addComponent(activityAttachmentEditor);		
		activityStepDataLayout.addComponent(stepAttachmentLayout);		
		
		activityStepDataLayout.setExpandRatio(stepDatFieldsEditor, 1.0F);
		this.addComponent(activityStepDataLayout);	
	}
	
	private void saveStepData(){
		if(this.activityDataMap!=null){
			ActivityData[] activityDataArry=new ActivityData[this.activityDataMap.size()];		
			Collection<ActivityData> activityDataCollection=this.activityDataMap.values();		
			activityDataCollection.toArray(activityDataArry);		
			try {
				this.activityStep.getBusinessActivity().setActivityData(activityDataArry);
				//this.activityStep.saveActivityStep("wangychu");			
				if(this.reloadableUIElements!=null){
					for(ReloadableUIElement reloadableUIElement:this.reloadableUIElements){
						reloadableUIElement.reloadContent();					
					}
				}			
			} catch (ActivityEngineRuntimeException e) {			
				e.printStackTrace();
			} catch (ActivityEngineDataException e) {			
				e.printStackTrace();
			}					
		}
	}
	
	private void handleStep(){
		try {			
			String stepAssigne=this.userClientInfo.getUserParticipant()!=null?this.userClientInfo.getUserParticipant().getParticipantName():
				this.userClientInfo.getI18NProperties().getProperty("administratorUserName");
			this.activityStep.handleActivityStep(stepAssigne);
			if(this.reloadableUIElements!=null){
				for(ReloadableUIElement reloadableUIElement:this.reloadableUIElements){
					reloadableUIElement.reloadContent();					
				}
			}	
			saveStepData();
			closeWindow();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}		
	}
	
	private void completeStep(String option){		
		try {
			String stepAssigne=this.userClientInfo.getUserParticipant()!=null?this.userClientInfo.getUserParticipant().getParticipantName():
				this.userClientInfo.getI18NProperties().getProperty("administratorUserName");
			String stepDecisionPointAttribute=this.activityStep.getBusinessActivity().getActivityDefinition()
					.getStepDecisionPointAttributeName(this.activityStep.getActivityStepDefinitionKey());
			String[] stepProcessVariableList=this.activityStep.getBusinessActivity().getActivityDefinition()
					.getStepProcessVariableList(this.activityStep.getActivityStepDefinitionKey());			
			String stepUserIdentityAttributeName=this.activityStep.getBusinessActivity().getActivityDefinition()
					.getStepUserIdentityAttributeName(this.activityStep.getActivityStepDefinitionKey());
			if(option!=null||stepProcessVariableList!=null||stepUserIdentityAttributeName!=null){				
				Map<String,Object> variables=new HashMap<String,Object>();
				if(option!=null){					
					variables.put(stepDecisionPointAttribute, option);
				}
				if(stepUserIdentityAttributeName!=null){					
					variables.put(stepUserIdentityAttributeName, stepAssigne);
				}				
				if(stepProcessVariableList!=null){
					for(String variableName:stepProcessVariableList){						 
						if( activityDataMap.get(variableName)!=null){							
							variables.put(variableName,activityDataMap.get(variableName).getDatFieldValue());
						}						 
					 }
				}				
				this.activityStep.completeActivityStep(stepAssigne,variables);				
			}else{
				this.activityStep.completeActivityStep(stepAssigne);
			}			
			if(this.reloadableUIElements!=null){
				for(ReloadableUIElement reloadableUIElement:this.reloadableUIElements){
					reloadableUIElement.reloadContent();					
				}
			}			
			closeWindow();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}
	}
	
	private void closeWindow(){	
		LightContentWindow ContainerWindow=(LightContentWindow)(this.getParent().getParent());
		this.getApplication().getMainWindow().removeWindow(ContainerWindow);
		ContainerWindow=null;		
	}
	
	private void renderCommentsUI(){			
		Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_stepDetail_comment);
		Label propertyNameLable = new Label("<b style='color:#333333;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityStepDetailEditor_commentWindowTitle")				
				+"</b> <b style='color:#ce0000;'>" + this.activityStep.getActivityStepName()+ "</b>", Label.CONTENT_XHTML);			
		ActivityCommentEditor activityCommentEditor=new ActivityCommentEditor(this.userClientInfo,this.activityStep,this.relatedRole);
		LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,activityCommentEditor,"810px");		
		lightContentWindow.center();	
		lightContentWindow.setHeight("500px");
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
}