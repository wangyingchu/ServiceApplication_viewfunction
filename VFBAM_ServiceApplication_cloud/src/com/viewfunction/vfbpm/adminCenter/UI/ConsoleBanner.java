package com.viewfunction.vfbpm.adminCenter.UI;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.viewfunction.messageEngine.exception.MessageEngineException;
import com.viewfunction.messageEngine.messageService.RealTimeNotificationReceiver;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ConsoleBanner extends HorizontalLayout {	
	private static final long serialVersionUID = -6798358853364731724L;
	
	private UserClientInfo userClientInfo;
	public ConsoleBanner(UserClientInfo userClientInfo){
		 this.userClientInfo=userClientInfo;
		 setHeight("45px");
	     setWidth("100%");
	     setSpacing(true);
	     setMargin(false, true, false, false);	
	     this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppBanner);	     
	     HorizontalLayout leftElementContainer=new HorizontalLayout();	     
	     HorizontalLayout rightElementContainer=new HorizontalLayout();	     
	     
	     this.addComponent(leftElementContainer);
	     this.addComponent(rightElementContainer); 	     
	     this.setComponentAlignment(leftElementContainer, Alignment.MIDDLE_LEFT);
	     this.setComponentAlignment(rightElementContainer, Alignment.MIDDLE_RIGHT);
	     
	     Embedded logoEmbedded = new Embedded(null, UICommonElementDefination.Logo_Viewfunction_Main_Title);
	     leftElementContainer.addComponent(logoEmbedded);	     
	     HorizontalLayout spacingHorizontalLayout_0=new HorizontalLayout();
	     spacingHorizontalLayout_0.setWidth("30px");
	     leftElementContainer.addComponent(spacingHorizontalLayout_0);
	     
	     Embedded userShotsEmbedded = new Embedded(null, UICommonElementDefination.User_profile_admin_shots);//user headshots need change at runtime
	     userShotsEmbedded.setHeight("30px");
	     userShotsEmbedded.setWidth("30px");
	     userShotsEmbedded.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppBanner_userShots);
	     	     
	     leftElementContainer.addComponent(userShotsEmbedded);
	     leftElementContainer.setComponentAlignment(userShotsEmbedded, Alignment.MIDDLE_LEFT);
	     
	     HorizontalLayout spacingHorizontalLayout_1=new HorizontalLayout();
	     spacingHorizontalLayout_1.setWidth("10px");
	     leftElementContainer.addComponent(spacingHorizontalLayout_1);	     
	     
	     Label userNameLabel;
	     if(userClientInfo.getUserParticipant()!=null){
	    	 String userNameString=userClientInfo.getUserParticipant().getDisplayName()!=null?
	    			userClientInfo.getUserParticipant().getDisplayName():userClientInfo.getUserParticipant().getParticipantName();	    			
	    	 userNameLabel=new Label(userNameString);
	     }else{
	    	 userNameLabel =new Label(userClientInfo.getI18NProperties().getProperty("administratorUserName"));
	     }     
	    
	     userNameLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppBanner_userNameText);
	     leftElementContainer.addComponent(userNameLabel);
	     leftElementContainer.setComponentAlignment(userNameLabel, Alignment.MIDDLE_LEFT);
	     
	     Embedded appBannerDivEmbedded = new Embedded(null, UICommonElementDefination.AppBanner_Div); 
	     leftElementContainer.addComponent(appBannerDivEmbedded);
	     leftElementContainer.setComponentAlignment(appBannerDivEmbedded, Alignment.MIDDLE_LEFT);
	     
	     HorizontalLayout signOutLinkLayout=new HorizontalLayout();
	   
	     signOutLinkLayout.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = 2229567320313460397L;

				public void layoutClick(LayoutClickEvent event) {					
					signOutApplication();
				}});	     
	     Label signOutLabel=new Label(userClientInfo.getI18NProperties().getProperty("signOutLink"));
	     signOutLabel.setStyleName("ui_appBanner_commonStyleText");
	     signOutLinkLayout.addComponent(signOutLabel);
	     leftElementContainer.addComponent(signOutLinkLayout);
	     leftElementContainer.setComponentAlignment(signOutLinkLayout, Alignment.MIDDLE_LEFT);
	     
	     HorizontalLayout spacingHorizontalLayout_2=new HorizontalLayout();
	     spacingHorizontalLayout_2.setWidth("10px");
	     leftElementContainer.addComponent(spacingHorizontalLayout_2);
	     
	     Label underLineLabel=new Label("-");
	     leftElementContainer.addComponent(underLineLabel);
	     leftElementContainer.setComponentAlignment(underLineLabel, Alignment.MIDDLE_LEFT);
	   
	     HorizontalLayout spacingHorizontalLayout_3=new HorizontalLayout();
	     spacingHorizontalLayout_3.setWidth("10px");
	     leftElementContainer.addComponent(spacingHorizontalLayout_3);
	     
	     Label editProfileLabel=new Label(userClientInfo.getI18NProperties().getProperty("editProfileLink"));
	     editProfileLabel.setStyleName("ui_appBanner_commonStyleText");
	     leftElementContainer.addComponent(editProfileLabel);
	     leftElementContainer.setComponentAlignment(editProfileLabel, Alignment.MIDDLE_LEFT);	  	     
	     
	     Label aboutLabel=new Label(userClientInfo.getI18NProperties().getProperty("aboutViewfuctionLink"));
	     aboutLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppBanner_boldStyleText);	     
	     rightElementContainer.addComponent(aboutLabel);
	     
	     HorizontalLayout spacingHorizontalLayout_4=new HorizontalLayout();
	     spacingHorizontalLayout_4.setWidth("40px");
	     rightElementContainer.addComponent(spacingHorizontalLayout_4);
	     
	     Label supportLabel=new Label(userClientInfo.getI18NProperties().getProperty("supportLink"));
	     supportLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppBanner_boldStyleText);	     
	     rightElementContainer.addComponent(supportLabel);
	}
	
	private void signOutApplication(){
		RealTimeNotificationReceiver notificationReceiver=this.userClientInfo.getNotificationReceiver();		
		if(notificationReceiver!=null){
			try {				
				notificationReceiver.stopReceive();
			} catch (MessageEngineException e) {				
				e.printStackTrace();
			}			
		}
		RealTimeNotificationReceiver personalMessageReceiver=this.userClientInfo.getPersonalMessageReceiver();
		if(personalMessageReceiver!=null){
			try {				
				personalMessageReceiver.stopReceive();
			} catch (MessageEngineException e) {				
				e.printStackTrace();
			}			
		}
		RealTimeNotificationReceiver publicMessageReceiver=this.userClientInfo.getPublicMessageReceiver();
		if(publicMessageReceiver!=null){
			try {				
				publicMessageReceiver.stopReceive();
			} catch (MessageEngineException e) {				
				e.printStackTrace();
			}			
		}		
		this.getApplication().close();
	}
}