package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.text.SimpleDateFormat;
import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityView.common.ActivityComment;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityCommentEditor extends VerticalLayout{	
	private static final long serialVersionUID = -8151270556683143339L;
	
	private UserClientInfo userClientInfo;
	private ActivityStep activityStep;
	private Role relatedRole;
	private VerticalLayout stepCommentMessageContainerLayout;
	private VerticalLayout activityCommentMessageContainerLayout;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private TextArea stepCommentTextArea;
	private TextArea activityCommentTextArea;
	
	public ActivityCommentEditor(UserClientInfo userClientInfo,ActivityStep activityStep,Role relatedRole){
		this.userClientInfo=userClientInfo;
		this.activityStep=activityStep;
		this.relatedRole=relatedRole;		
		this.setSizeFull();		
	    TabSheet commentTabsheet=new TabSheet();    
	    
	    commentTabsheet.setStyleName(Reindeer.TABSHEET_SMALL);	    
	    this.addComponent(commentTabsheet);	     
	    commentTabsheet.setSizeFull();	
	    
	    VerticalLayout stepCommentContainerLayout=new VerticalLayout();
	    stepCommentContainerLayout.setSizeFull();	  
	    
	    VerticalLayout activityCommentContainerLayout=new VerticalLayout();
	    commentTabsheet.addTab(activityCommentContainerLayout,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_activityCommentTab"),null);		    
	    commentTabsheet.addTab(stepCommentContainerLayout,this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_taskCommentTab"),null);
	    
	    Panel stepCommentPanel=new Panel();
	    stepCommentPanel.setSizeFull();
	    stepCommentPanel.setHeight("350px");
	    stepCommentContainerLayout.addComponent(stepCommentPanel);
	    
	    HorizontalLayout stepCommentFormLayout=new HorizontalLayout();	    
	    stepCommentTextArea=new TextArea();
	    stepCommentTextArea.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_taskCommentPrompt"));
	    stepCommentTextArea.setRows(2);
	    stepCommentTextArea.setColumns(50);
	    stepCommentFormLayout.addComponent(stepCommentTextArea);	    
	    Button addStepCommentButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_addTaskCommentButton"));	
	    addStepCommentButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = 4737815666369904043L;

			public void buttonClick(ClickEvent event) {
				addStepComment();				
			}
        });	
	    
	    stepCommentFormLayout.addComponent(addStepCommentButton);	
	    stepCommentFormLayout.setComponentAlignment(stepCommentTextArea, Alignment.MIDDLE_LEFT);
	    stepCommentFormLayout.setComponentAlignment(addStepCommentButton, Alignment.MIDDLE_LEFT);	   
	    stepCommentPanel.addComponent(stepCommentFormLayout);
	    
	    HorizontalLayout spaceDivLayout=new HorizontalLayout();
	    spaceDivLayout.setWidth("100%");
	    spaceDivLayout.setHeight("20px");	   	    
	    spaceDivLayout.addStyleName("ui_contentManagementPropertyList_3");
	    stepCommentPanel.addComponent(spaceDivLayout);
	    
	    HorizontalLayout spaceDivLayout_01=new HorizontalLayout();
	    spaceDivLayout_01.setHeight("10px");
	    stepCommentPanel.addComponent(spaceDivLayout_01);
	    
	    HorizontalLayout spaceDivLayout2=new HorizontalLayout();		    
	    spaceDivLayout2.setWidth("100%");	    
	    Button refreshStepCommentButton=new Button();		
	    refreshStepCommentButton.setCaption(null);
	    refreshStepCommentButton.setIcon(UICommonElementDefination.ICON_userClient_ReloadContent);
	    refreshStepCommentButton.setStyleName(BaseTheme.BUTTON_LINK);
	    refreshStepCommentButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_reloadTaskCommentButton"));
	    refreshStepCommentButton.addListener(new Button.ClickListener() {			
			private static final long serialVersionUID = -3865886226566633482L;

			public void buttonClick(ClickEvent event) {	 
				loadStepCommentMessage();
            }
        });		    
	    spaceDivLayout2.addComponent(refreshStepCommentButton);
	    spaceDivLayout2.setComponentAlignment(refreshStepCommentButton, Alignment.MIDDLE_RIGHT);	    
	    stepCommentPanel.addComponent(spaceDivLayout2);	  	    
	    
	    stepCommentMessageContainerLayout=new VerticalLayout();
	    stepCommentMessageContainerLayout.setWidth("100%");
	    stepCommentPanel.addComponent(stepCommentMessageContainerLayout);	    
	    loadStepCommentMessage();
	    
	    Panel activityCommentPanel=new Panel();
	    activityCommentPanel.setSizeFull();
	    activityCommentPanel.setHeight("350px");
	    activityCommentContainerLayout.addComponent(activityCommentPanel);
	    
	    HorizontalLayout activityCommentFormLayout=new HorizontalLayout();	    
	    activityCommentTextArea=new TextArea();
	    activityCommentTextArea.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_activityCommentPrompt"));
	    activityCommentTextArea.setRows(2);
	    activityCommentTextArea.setColumns(50);	 	    
	    Button addActivityCommentButton=new Button(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_addActivityCommentButton"));	
	    addActivityCommentButton.addListener(new Button.ClickListener() {		
			private static final long serialVersionUID = -3865886226566633482L;

			public void buttonClick(ClickEvent event) {
				addActivityComment();				
			}
        });	
	    
	    activityCommentFormLayout.addComponent(activityCommentTextArea);
	    activityCommentFormLayout.addComponent(addActivityCommentButton);
	    activityCommentFormLayout.setComponentAlignment(activityCommentTextArea, Alignment.MIDDLE_LEFT);
	    activityCommentFormLayout.setComponentAlignment(addActivityCommentButton, Alignment.MIDDLE_LEFT);	   
	    activityCommentPanel.addComponent(activityCommentFormLayout);
	    
	    HorizontalLayout spaceDivLayout_2=new HorizontalLayout();
	    spaceDivLayout_2.setWidth("100%");
	    spaceDivLayout_2.setHeight("20px");	   	    
	    spaceDivLayout_2.addStyleName("ui_contentManagementPropertyList_3");
	    activityCommentPanel.addComponent(spaceDivLayout_2);	    
	    
	    HorizontalLayout spaceDivLayout_02=new HorizontalLayout();
	    spaceDivLayout_02.setHeight("10px");
	    activityCommentPanel.addComponent(spaceDivLayout_02);
	    
	    HorizontalLayout spaceDivLayout21=new HorizontalLayout();		    
	    spaceDivLayout21.setWidth("100%");	    
	    Button refreshActivityCommentButton=new Button();		
	    refreshActivityCommentButton.setCaption(null);
	    refreshActivityCommentButton.setIcon(UICommonElementDefination.ICON_userClient_ReloadContent);
	    refreshActivityCommentButton.setStyleName(BaseTheme.BUTTON_LINK);
	    refreshActivityCommentButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("ActivityManage_ActivityCommentEditor_reloadActivityCommentButton"));
	    refreshActivityCommentButton.addListener(new Button.ClickListener() {				
			private static final long serialVersionUID = 1500991631055806936L;

			public void buttonClick(ClickEvent event) {	 
				loadActivityCommentMessage();
            }
        });		    
	    spaceDivLayout21.addComponent(refreshActivityCommentButton);
	    spaceDivLayout21.setComponentAlignment(refreshActivityCommentButton, Alignment.MIDDLE_RIGHT);	    
	    activityCommentPanel.addComponent(spaceDivLayout21);	    
	    
	    HorizontalLayout spaceDivLayout3=new HorizontalLayout();	 
	    spaceDivLayout3.setHeight("20px");	
	    activityCommentPanel.addComponent(spaceDivLayout3);	    
	    
	    activityCommentMessageContainerLayout=new VerticalLayout();
	    activityCommentMessageContainerLayout.setWidth("100%");
	    activityCommentPanel.addComponent(activityCommentMessageContainerLayout);	    
	    loadActivityCommentMessage();	    
	}	
	
	private void loadStepCommentMessage(){
		stepCommentMessageContainerLayout.removeAllComponents();
		List<ActivityComment> commentList;
		try {
			commentList = this.activityStep.getComments();
			if(commentList!=null){
				for(ActivityComment activityComment:commentList){			
					HorizontalLayout propertyValue=new HorizontalLayout();		
					propertyValue.setWidth("710px");				
					propertyValue.addStyleName("ui_userClient_commentMessage");
					
					HorizontalLayout commentMessageLayout=new HorizontalLayout();
					
					commentMessageLayout.setWidth("500px");
					Label commentMessageLabel=new Label("<span style='word-wrap: break-word;word-break: normal;width:445px;'>"+activityComment.getCommentContent()+"</span>",Label.CONTENT_XHTML);
					commentMessageLayout.addComponent(commentMessageLabel);			
					propertyValue.addComponent(commentMessageLayout);				
					
					HorizontalLayout commentInfoLayout=new HorizontalLayout();				
					String dateFormatStrr="<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size:8pt;-webkit-text-size-adjust:none;'>";			
					Label commentDateLabel=new Label(dateFormatStrr+formatter.format(activityComment.getAddDate())+"</span>",Label.CONTENT_XHTML);
					commentInfoLayout.addComponent(commentDateLabel);
					
					HorizontalLayout spaceDivLayout=new HorizontalLayout();	
					spaceDivLayout.setWidth("10px");
					commentInfoLayout.addComponent(spaceDivLayout);
					
					String userFormatStrr="<span style='color:#0099ff;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size:8pt;-webkit-text-size-adjust:none;'>";				
					Label commentAdderLabel=new Label(userFormatStrr+activityComment.getParticipant().getDisplayName()+"</span>",Label.CONTENT_XHTML);
					if(activityComment.getRole()!=null){
						commentAdderLabel.setDescription(activityComment.getRole().getDisplayName());
					}					
					commentInfoLayout.addComponent(commentAdderLabel);			
					
					propertyValue.addComponent(commentInfoLayout);			
					propertyValue.setComponentAlignment(commentMessageLayout, Alignment.MIDDLE_LEFT);
					propertyValue.setComponentAlignment(commentInfoLayout, Alignment.MIDDLE_RIGHT);			
					stepCommentMessageContainerLayout.addComponent(propertyValue);			
				}		
			}		
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
	}	
	
	private void addStepComment(){
		if(stepCommentTextArea.getValue()==null||stepCommentTextArea.getValue().toString().equals("")){			
			return;
		}else{
			String commentTxt=stepCommentTextArea.getValue().toString();			
			ActivityComment newComment=new ActivityComment();		
			newComment.setCommentContent(commentTxt);
			newComment.setRole(this.relatedRole);
			newComment.setParticipant(this.userClientInfo.getUserParticipant());		
			this.activityStep.addComment(newComment);
			stepCommentTextArea.setValue("");
			loadStepCommentMessage();
		}		
	}
	
	private void loadActivityCommentMessage(){
		activityCommentMessageContainerLayout.removeAllComponents();
		List<ActivityComment> commentList;
		try {
			commentList = this.activityStep.getBusinessActivity().getComments();
			if(commentList!=null){
				for(ActivityComment activityComment:commentList){			
					HorizontalLayout propertyValue=new HorizontalLayout();		
					propertyValue.setWidth("710px");					
					propertyValue.addStyleName("ui_userClient_commentMessage");
					
					HorizontalLayout commentMessageLayout=new HorizontalLayout();					
					commentMessageLayout.setWidth("500px");
					Label commentMessageLabel=new Label("<span style='word-wrap: break-word;word-break: normal;width:445px;'>"+activityComment.getCommentContent()+"</span>",Label.CONTENT_XHTML);
					commentMessageLayout.addComponent(commentMessageLabel);			
					propertyValue.addComponent(commentMessageLayout);				
					
					HorizontalLayout commentInfoLayout=new HorizontalLayout();				
					String dateFormatStrr="<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size:8pt;-webkit-text-size-adjust:none;'>";			
					Label commentDateLabel=new Label(dateFormatStrr+formatter.format(activityComment.getAddDate())+"</span>",Label.CONTENT_XHTML);
					commentInfoLayout.addComponent(commentDateLabel);
					
					HorizontalLayout spaceDivLayout=new HorizontalLayout();	
					spaceDivLayout.setWidth("10px");
					commentInfoLayout.addComponent(spaceDivLayout);
					
					String userFormatStrr="<span style='color:#0099ff;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size:8pt;-webkit-text-size-adjust:none;'>";				
					Label commentAdderLabel=new Label(userFormatStrr+activityComment.getParticipant().getDisplayName()+"</span>",Label.CONTENT_XHTML);
					if(activityComment.getRole()!=null){
						commentAdderLabel.setDescription(activityComment.getRole().getDisplayName());
					}					
					commentInfoLayout.addComponent(commentAdderLabel);			
					
					propertyValue.addComponent(commentInfoLayout);			
					propertyValue.setComponentAlignment(commentMessageLayout, Alignment.MIDDLE_LEFT);
					propertyValue.setComponentAlignment(commentInfoLayout, Alignment.MIDDLE_RIGHT);			
					activityCommentMessageContainerLayout.addComponent(propertyValue);			
				}		
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
	}
	
	private void addActivityComment(){
		if(activityCommentTextArea.getValue()==null||activityCommentTextArea.getValue().toString().equals("")){			
			return;
		}else{
			String commentTxt=activityCommentTextArea.getValue().toString();			
			ActivityComment newComment=new ActivityComment();		
			newComment.setCommentContent(commentTxt);
			newComment.setRole(this.relatedRole);
			newComment.setParticipant(this.userClientInfo.getUserParticipant());		
			try {
				this.activityStep.getBusinessActivity().addComment(newComment);
			} catch (ActivityEngineProcessException e) {				
				e.printStackTrace();
			}
			activityCommentTextArea.setValue("");
			loadActivityCommentMessage();
		}		
	}
}