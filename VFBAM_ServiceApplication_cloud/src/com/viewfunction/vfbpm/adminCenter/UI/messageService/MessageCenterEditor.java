package com.viewfunction.vfbpm.adminCenter.UI.messageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jcr.PropertyType;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.viewfunction.activityEngine.activityBureauImpl.CCRActivityEngineConstant;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.ConsoleTitle;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class MessageCenterEditor extends VerticalLayout{
	private static final long serialVersionUID = 1012385973906786801L;
	
	private UserClientInfo userClientInfo;
	private Panel containerPanel;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ConsoleTitle consoleTitle;
	public MessageCenterEditor(ConsoleTitle consoleTitle,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;	
		this.consoleTitle=consoleTitle;
		containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		VerticalLayout containPanelLayot=((VerticalLayout)containerPanel.getContent());		
		containPanelLayot.setMargin(false);
		containPanelLayot.setSpacing(false);		
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();			
		this.addComponent(containerPanel);	
		loadParticipantMessages();
	}
	
	private void loadParticipantMessages(){
		ContentSpace activityContentSpace = null;		
		try {			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.userClientInfo.getUserActivitySpace());			
			RootContentObject messageStoreRoot=activityContentSpace.getRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore);
			if(messageStoreRoot==null){
				return;								
			}			
			BaseContentObject participantMessageBoxRoot=messageStoreRoot.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
					.getSubContentObject(this.userClientInfo.getUserParticipant().getParticipantName());
			if(participantMessageBoxRoot==null){
				return;			
			}			
			List<BaseContentObject> messagesObjList=participantMessageBoxRoot.getSubContentObjects(null);			
			for(BaseContentObject baseContentObject:messagesObjList){				
				final String messageObjectName=baseContentObject.getContentObjectName();				
				Embedded typeIcon=null;	
				Object ifUnReadedObj=baseContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Status_UnreadFlag).getPropertyValue();
				if(((Boolean)ifUnReadedObj).booleanValue()){
					typeIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messageEntryUnRead);
					typeIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageCenterEditor_unreadMessageDesc"));
				}else{
					typeIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messageEntry);
					typeIcon.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageCenterEditor_readMessageDesc"));
				}
				
				String messageType=baseContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_MessageType).getPropertyValue().toString();
				String messageTitle=baseContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle).getPropertyValue().toString();				
				//String messageContent=baseContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent).getPropertyValue().toString();
				long messageSentTime=(Long)baseContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageSentTime).getPropertyValue();
				//String[] messageGroupArray=null;
				//ContentObjectProperty messageGroupObj=baseContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageGroups);				
				//if(messageGroupObj!=null){
				//	messageGroupArray=(String[])(messageGroupObj.getPropertyValue());
				//}				
				HorizontalLayout valueContainerLayout=new HorizontalLayout();				
				valueContainerLayout.addListener(new LayoutClickListener(){
					private static final long serialVersionUID = -5281589215955260254L;

					public void layoutClick(LayoutClickEvent event) {					
						if(event.getButton()==event.BUTTON_RIGHT){
							return;
						}									
						if (event.isDoubleClick()) {					
							openMessageDetail(messageObjectName);
						}				
					}});
				
				Label messageTypeLabel=new Label("<span style='margin-left: 10px;margin-right:5px;color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;cursor:pointer;'>"+
						messageType+"</span>" ,Label.CONTENT_XHTML);
				valueContainerLayout.addComponent(messageTypeLabel);	
				Label messageTitleLabel=new Label("<span style='margin-left: 10px;margin-right:5px;color:#1360a8;text-shadow: 1px 1px 1px #eeeeee;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;cursor:pointer;'>"+
						messageTitle+"</span>" ,Label.CONTENT_XHTML);
				valueContainerLayout.addComponent(messageTitleLabel);				
				Label messageSentDateLabel=new Label("<span style='color:#666666;text-shadow: 1px 1px 1px #eeeeee;font-style: italic;font-size:7pt;-webkit-text-size-adjust:none;-webkit-user-select: none;-khtml-user-select: none;-moz-user-select: none;-o-user-select: none;user-select: none;cursor:pointer;'>"
						+formatter.format(new Date(messageSentTime))+"</span>" ,Label.CONTENT_XHTML);
				valueContainerLayout.addComponent(messageSentDateLabel);				
				
				HorizontalLayout messageEntryActionContainer=new HorizontalLayout();				
				messageEntryActionContainer.setWidth("60px");
				Button openMessageButton=new Button();
				openMessageButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageCenterEditor_showMessageButton"));
				openMessageButton.setCaption(null);
				openMessageButton.setIcon(UICommonElementDefination.ICON_userClient_download);
				openMessageButton.setStyleName(BaseTheme.BUTTON_LINK);
				messageEntryActionContainer.addComponent(openMessageButton);
				openMessageButton.addListener(new Button.ClickListener() {	
					private static final long serialVersionUID = -3259041555328927578L;

					public void buttonClick(ClickEvent event) {	 
						openMessageDetail(messageObjectName);
		            }
		        });		
				
				Button deleteMessageButton=new Button();
				deleteMessageButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageCenterEditor_deleteMessageButton"));
				deleteMessageButton.setCaption(null);
				deleteMessageButton.setIcon(UICommonElementDefination.AppPanel_deleteActionbarIcon);
				deleteMessageButton.setStyleName(BaseTheme.BUTTON_LINK);
				messageEntryActionContainer.addComponent(deleteMessageButton);
				deleteMessageButton.addListener(new Button.ClickListener() {
					private static final long serialVersionUID = -4431284222506521046L;

					public void buttonClick(ClickEvent event) {	 
						deleteMessage(messageObjectName);
		            }
		        });		
				PropertyItem messagePropertyItem=new PropertyItem(PropertyItem.POSTION_ODD,typeIcon,valueContainerLayout,messageEntryActionContainer);		
				containerPanel.addComponent(messagePropertyItem);
			}				
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
	
	private void openMessageDetail(String objectName){
		ContentSpace activityContentSpace = null;		
		try {			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.userClientInfo.getUserActivitySpace());			
			BaseContentObject messageContentObject=activityContentSpace.getRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore)
					.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
					.getSubContentObject(this.userClientInfo.getUserParticipant().getParticipantName())
					.getSubContentObject(objectName);			
			ContentObjectProperty messageNotReadProperty=ContentComponentFactory.createContentObjectProperty();
			messageNotReadProperty.setMultiple(false);
			messageNotReadProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Status_UnreadFlag);
			messageNotReadProperty.setPropertyType(PropertyType.BOOLEAN);
			messageNotReadProperty.setPropertyValue(new Boolean(false));	
			messageContentObject.updateProperty(messageNotReadProperty, false);
			containerPanel.removeAllComponents();
			loadParticipantMessages();			
			
			String messageType=messageContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_MessageType).getPropertyValue().toString();
			String messageTitle=messageContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle).getPropertyValue().toString();				
			String messageContent=messageContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent).getPropertyValue().toString();
			long messageSentTime=(Long)messageContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageSentTime).getPropertyValue();
			//String[] messageGroupArray=null;
			//ContentObjectProperty messageGroupObj=messageContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Property_MessageGroups);				
			//if(messageGroupObj!=null){
			//	messageGroupArray=(String[])(messageGroupObj.getPropertyValue());
			//}			
			Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messageEntry);			
			Label propertyNameLable = new Label(" <b style='color:#333333;'> "+	messageType				
					+"</b> <b style='color:#ce0000;'>" + messageTitle+ "</b>", Label.CONTENT_XHTML);					
			VerticalLayout notificationContainerLayout=new VerticalLayout();
			notificationContainerLayout.setHeight("350px");				
			HorizontalLayout noticePropertyLayout=new HorizontalLayout();				
			notificationContainerLayout.addComponent(noticePropertyLayout);	
			notificationContainerLayout.setComponentAlignment(noticePropertyLayout, Alignment.MIDDLE_RIGHT);			
			Label noticeTimeStamp=new Label("<span style='color:#333333;'>"+
					this.userClientInfo.getI18NProperties().getProperty("SystemConfig_UserClientRefreshListener_noticeSendTimeLabel")				
					+"</span> <span style='color:#ce0000;'>" + formatter.format(new Date(messageSentTime))+ "</span>", Label.CONTENT_XHTML);				
			noticePropertyLayout.addComponent(noticeTimeStamp);				
			noticePropertyLayout.setComponentAlignment(noticeTimeStamp, Alignment.MIDDLE_RIGHT);		
			Panel noticeTextPanel=new Panel();
			noticeTextPanel.setStyleName(Reindeer.PANEL_LIGHT);
			noticeTextPanel.setScrollable(true);
			noticeTextPanel.setSizeFull();				
			String noticeContent=messageContent;
			Label noticeText=new Label(noticeContent, Label.CONTENT_XHTML);
			noticeTextPanel.addComponent(noticeText);
			notificationContainerLayout.addComponent(noticeTextPanel);				
			notificationContainerLayout.setExpandRatio(noticeTextPanel, 1.0F);				
			LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,notificationContainerLayout,"500px");
			this.getApplication().getMainWindow().addWindow(lightContentWindow);
			this.consoleTitle.refreshUnreadMessageNumber();
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}		
	}
	
	private void deleteMessage(String objectName){
		ContentSpace activityContentSpace = null;		
		try {			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.userClientInfo.getUserActivitySpace());			
			BaseContentObject rootMessageContentObject=activityContentSpace.getRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore)
					.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
					.getSubContentObject(this.userClientInfo.getUserParticipant().getParticipantName());
			rootMessageContentObject.removeSubContentObject(objectName, false);			
			containerPanel.removeAllComponents();
			loadParticipantMessages();			
			this.consoleTitle.refreshUnreadMessageNumber();			
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}	
	}
}