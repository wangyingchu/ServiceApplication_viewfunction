package com.viewfunction.vfbpm.adminCenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.jcr.PropertyType;

import com.viewfunction.activityEngine.activityBureauImpl.CCRActivityEngineConstant;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.messageEngine.messageService.ObjectMessageEntry;
import com.viewfunction.messageEngine.messageService.TextMessageEntry;
import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.UI.ConsoleTitle;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.UI.messageService.MessageServiceConstant;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class UserClientRefreshListener implements RefreshListener{
	private static final long serialVersionUID = 6047815469611636000L;		
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public int unReadedMessageNumber=0;

	private UserClientInfo userClientInfo;
	private ConsoleTitle consoleTitle;
	private Queue<TextMessageEntry> systemTextMessageQueue;
	private Queue<ObjectMessageEntry> systemObjectMessageQueue;
	private Queue<ObjectMessageEntry> publicObjectMessageQueue;
	private Queue<ObjectMessageEntry> personalObjectMessageQueue;
	
	private String[] participantRoles;
	
	public UserClientRefreshListener(ConsoleTitle consoleTitle,UserClientInfo userClientInfo,Queue<TextMessageEntry> systemTextMessageQueue,
			Queue<ObjectMessageEntry> systemObjectMessageQueue,Queue<ObjectMessageEntry> publicObjectMessageQueue,Queue<ObjectMessageEntry> personalObjectMessageQueue,int unReadedMessageNumber){	
		this.userClientInfo=userClientInfo;
		this.systemTextMessageQueue=systemTextMessageQueue;
		this.systemObjectMessageQueue=systemObjectMessageQueue;
		this.publicObjectMessageQueue=publicObjectMessageQueue;
		this.personalObjectMessageQueue=personalObjectMessageQueue;
		this.consoleTitle=consoleTitle;
		this.unReadedMessageNumber=unReadedMessageNumber;
		try {
			Role[] participantRelatedRole=userClientInfo.getUserParticipant().getRoles();						
			participantRoles=new String[participantRelatedRole.length];
			for(int i=0;i<participantRelatedRole.length;i++){
				participantRoles[i]=participantRelatedRole[i].getRoleName();			
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} 	
	}
	
	public void refresh(Refresher source) {			
		while(!this.systemTextMessageQueue.isEmpty()){			
			TextMessageEntry message=this.systemTextMessageQueue.poll();
			showSystemNotification(message);			
		}		
		while(!this.systemObjectMessageQueue.isEmpty()){			
			ObjectMessageEntry message=this.systemObjectMessageQueue.poll();
			showUserNotification(message);			
		}
		while(!this.publicObjectMessageQueue.isEmpty()){			
			ObjectMessageEntry message=this.publicObjectMessageQueue.poll();
			savePublicActivityMessage(message);			
		}
		while(!this.personalObjectMessageQueue.isEmpty()){			
			ObjectMessageEntry message=this.personalObjectMessageQueue.poll();
			savePersonalActivityMessage(message);			
		}		
		
		List<ReloadableUIElement> refreshUIElementsList=this.userClientInfo.getRefreshUIElementsList();		
		for(ReloadableUIElement reloadableUIElement:refreshUIElementsList){
			reloadableUIElement.reloadContent();			
		}		
	}
	
	private void showSystemNotification(TextMessageEntry message){		
		String notificationCaption=message.getMessageText();		
		String notificationTimeStamp=formatter.format(new Date(message.getMessageTimeStamp()));
		String messageReply=message.getMessageReplyToAddress();
		String notificationDesc=null;		
		if(messageReply!=null){
			notificationDesc=messageReply+"/"+notificationTimeStamp;
		}else{
			notificationDesc=notificationTimeStamp;
		}		
		Notification notification=new Window.Notification(notificationCaption,notificationDesc,Notification.TYPE_TRAY_NOTIFICATION);		
		notification.setPosition(Notification.POSITION_TOP_RIGHT);
		notification.setDelayMsec(-1);		
		this.consoleTitle.getWindow().showNotification(notification);	
	}
	
	private void showUserNotification(ObjectMessageEntry objectMessageEntry){		
		Map<String,Object> messageData=objectMessageEntry.getMessageProperty();		
		if(messageData.get(MessageServiceConstant.MESSAGESERVICE_MessageType)==null){
			return;
		}else{					
			String messageType=messageData.get(MessageServiceConstant.MESSAGESERVICE_MessageType).toString();
			if(messageType.equals(MessageServiceConstant.MESSAGESERVICE_MessageType_NOTICE)){
				//icon for notice
				Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messageNotice);
				String messageTitle=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle).toString();
				Label propertyNameLable = new Label(" <b style='color:#333333;'>"+
						this.userClientInfo.getI18NProperties().getProperty("SystemConfig_UserClientRefreshListener_noticeLabel")				
						+"</b> <b style='color:#ce0000;'>" + messageTitle+ "</b>", Label.CONTENT_XHTML);					
				VerticalLayout notificationContainerLayout=new VerticalLayout();
				notificationContainerLayout.setHeight("350px");				
				HorizontalLayout noticePropertyLayout=new HorizontalLayout();				
				notificationContainerLayout.addComponent(noticePropertyLayout);	
				notificationContainerLayout.setComponentAlignment(noticePropertyLayout, Alignment.MIDDLE_RIGHT);				
				Label noticeTimeStamp=new Label("<span style='color:#333333;'>"+
						this.userClientInfo.getI18NProperties().getProperty("SystemConfig_UserClientRefreshListener_noticeSendTimeLabel")				
						+"</span> <span style='color:#ce0000;'>" + formatter.format(new Date(objectMessageEntry.getMessageTimeStamp()))+ "</span>", Label.CONTENT_XHTML);				
				noticePropertyLayout.addComponent(noticeTimeStamp);				
				noticePropertyLayout.setComponentAlignment(noticeTimeStamp, Alignment.MIDDLE_RIGHT);		
				Panel noticeTextPanel=new Panel();
				noticeTextPanel.setStyleName(Reindeer.PANEL_LIGHT);
				noticeTextPanel.setScrollable(true);
				noticeTextPanel.setSizeFull();				
				String noticeContent=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent).toString();
				Label noticeText=new Label(noticeContent, Label.CONTENT_XHTML);
				noticeTextPanel.addComponent(noticeText);
				notificationContainerLayout.addComponent(noticeTextPanel);				
				notificationContainerLayout.setExpandRatio(noticeTextPanel, 1.0F);				
				LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,notificationContainerLayout,"500px");				
				this.consoleTitle.getApplication().getMainWindow().addWindow(lightContentWindow);				
			}if(messageType.equals(MessageServiceConstant.MESSAGESERVICE_MessageType_MESSAGE)){					
				//icon for notice
				Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messageNotice);
				String messageTitle=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle).toString();
				Label propertyNameLable = new Label(" <b style='color:#333333;'>"+
						this.userClientInfo.getI18NProperties().getProperty("SystemConfig_UserClientRefreshListener_messageLabel")				
						+"</b> <b style='color:#ce0000;'>" + messageTitle+ "</b>", Label.CONTENT_XHTML);					
				VerticalLayout notificationContainerLayout=new VerticalLayout();
				notificationContainerLayout.setHeight("350px");				
				HorizontalLayout noticePropertyLayout=new HorizontalLayout();				
				notificationContainerLayout.addComponent(noticePropertyLayout);	
				notificationContainerLayout.setComponentAlignment(noticePropertyLayout, Alignment.MIDDLE_RIGHT);				
				Label noticeTimeStamp=new Label("<span style='color:#333333;'>"+
						this.userClientInfo.getI18NProperties().getProperty("SystemConfig_UserClientRefreshListener_noticeSendTimeLabel")				
						+"</span> <span style='color:#ce0000;'>" + formatter.format(new Date(objectMessageEntry.getMessageTimeStamp()))+ "</span>", Label.CONTENT_XHTML);				
				noticePropertyLayout.addComponent(noticeTimeStamp);				
				noticePropertyLayout.setComponentAlignment(noticeTimeStamp, Alignment.MIDDLE_RIGHT);		
				Panel noticeTextPanel=new Panel();
				noticeTextPanel.setStyleName(Reindeer.PANEL_LIGHT);
				noticeTextPanel.setScrollable(true);
				noticeTextPanel.setSizeFull();				
				String noticeContent=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent).toString();
				Label noticeText=new Label(noticeContent, Label.CONTENT_XHTML);
				noticeTextPanel.addComponent(noticeText);
				notificationContainerLayout.addComponent(noticeTextPanel);				
				notificationContainerLayout.setExpandRatio(noticeTextPanel, 1.0F);				
				LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,notificationContainerLayout,"500px");				
				this.consoleTitle.getApplication().getMainWindow().addWindow(lightContentWindow);
			}			
		}		
	}
	
	private void savePublicActivityMessage(ObjectMessageEntry objectMessageEntry){
		if(this.participantRoles==null){
			return;
		}
		Map<String,Object> messageData=objectMessageEntry.getMessageProperty();	
		Map<String,Object> messageContentData=objectMessageEntry.getMessageData();
		Object messageGroupObj=messageContentData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageGroups);
		if(messageGroupObj==null){
			return;
		}else{
			List<String> messageGroupList=(List<String>)messageGroupObj;
			boolean participantRoleInGroup=false;
			for(String roleName:this.participantRoles){
				if(messageGroupList.contains(roleName)){
					participantRoleInGroup=true;
					break;
				}
			}
			if(participantRoleInGroup){				
				long messageSendDateStamp=objectMessageEntry.getMessageTimeStamp();
				String messageTitle=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle).toString();
				String messageContent=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent).toString();
				String messageType=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageType).toString();
				List<String> messageGroup=(List<String>)messageContentData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageGroups);
				String[] messageGroupStringArray=new String[messageGroup.size()];
				messageGroup.toArray(messageGroupStringArray);				
				String paticipantName=this.userClientInfo.getUserParticipant().getParticipantName();		
				ContentSpace activityContentSpace = null;		
				try {			
					activityContentSpace=ContentComponentFactory.connectContentSpace(this.userClientInfo.getUserActivitySpace());			
					RootContentObject messageStoreRoot=activityContentSpace.getRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore);
					if(messageStoreRoot==null){
						RootContentObject rootObj=ContentComponentFactory.createRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore);
						messageStoreRoot=activityContentSpace.addRootContentObject(rootObj);
						messageStoreRoot.addSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore,null,false);				
					}			
					BaseContentObject participantMessageBoxRoot=messageStoreRoot.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
							.getSubContentObject(paticipantName);
					if(participantMessageBoxRoot==null){
						participantMessageBoxRoot=messageStoreRoot.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
								.addSubContentObject(paticipantName, null, false);				
					}					
					if(participantMessageBoxRoot.getSubContentObject(messageType+"_"+messageSendDateStamp)!=null){
						return;
					}					
					List<ContentObjectProperty> paramLst=new ArrayList<ContentObjectProperty>();					
					ContentObjectProperty messageTypeProperty=ContentComponentFactory.createContentObjectProperty();
					messageTypeProperty.setMultiple(false);
					messageTypeProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_MessageType);
					messageTypeProperty.setPropertyType(PropertyType.STRING);
					messageTypeProperty.setPropertyValue(messageType);	
					paramLst.add(messageTypeProperty);
					
					ContentObjectProperty messageTitleProperty=ContentComponentFactory.createContentObjectProperty();
					messageTitleProperty.setMultiple(false);
					messageTitleProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle);
					messageTitleProperty.setPropertyType(PropertyType.STRING);
					messageTitleProperty.setPropertyValue(messageTitle);	
					paramLst.add(messageTitleProperty);
					
					ContentObjectProperty messageContentProperty=ContentComponentFactory.createContentObjectProperty();
					messageContentProperty.setMultiple(false);
					messageContentProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent);
					messageContentProperty.setPropertyType(PropertyType.STRING);
					messageContentProperty.setPropertyValue(messageContent);	
					paramLst.add(messageContentProperty);
					
					ContentObjectProperty messageSentTimeProperty=ContentComponentFactory.createContentObjectProperty();
					messageSentTimeProperty.setMultiple(false);
					messageSentTimeProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Property_MessageSentTime);
					messageSentTimeProperty.setPropertyType(PropertyType.LONG);
					messageSentTimeProperty.setPropertyValue(new Long(messageSendDateStamp));	
					paramLst.add(messageSentTimeProperty);
					
					ContentObjectProperty messageNotReadProperty=ContentComponentFactory.createContentObjectProperty();
					messageNotReadProperty.setMultiple(false);
					messageNotReadProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Status_UnreadFlag);
					messageNotReadProperty.setPropertyType(PropertyType.BOOLEAN);
					messageNotReadProperty.setPropertyValue(new Boolean(true));	
					paramLst.add(messageNotReadProperty);	
					
					ContentObjectProperty messageGroupProperty=ContentComponentFactory.createContentObjectProperty();
					messageGroupProperty.setMultiple(true);
					messageGroupProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Property_MessageGroups);
					messageGroupProperty.setPropertyType(PropertyType.STRING);
					messageGroupProperty.setPropertyValue(messageGroupStringArray);	
					paramLst.add(messageGroupProperty);					
					participantMessageBoxRoot.addSubContentObject(messageType+"_"+messageSendDateStamp, paramLst, false);	
					this.unReadedMessageNumber++;
					this.consoleTitle.applicationMessageLabel.setValue("<span style='cursor:pointer;'>"+
							userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageStatusTitle")+" ( "+"<span style='color:#ce0000;font-weight: bold'>"+this.unReadedMessageNumber+"</span>"+" )</span>");
				}catch (ContentReposityException e) {			
					e.printStackTrace();			
				}finally{
					activityContentSpace.closeContentSpace();			
				}					
			}			
		}	
	}
	
	private void savePersonalActivityMessage(ObjectMessageEntry objectMessageEntry){
		this.unReadedMessageNumber++;
		this.consoleTitle.applicationMessageLabel.setValue("<span style='cursor:pointer;'>"+
				userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageStatusTitle")+" ( "+"<span style='color:#ce0000;font-weight: bold'>"+this.unReadedMessageNumber+"</span>"+" )</span>");
		Map<String,Object> messageData=objectMessageEntry.getMessageProperty();
		long messageSendDateStamp=objectMessageEntry.getMessageTimeStamp();
		String messageTitle=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle).toString();
		String messageContent=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent).toString();
		String messageType=messageData.get(MessageServiceConstant.MESSAGESERVICE_Property_MessageType).toString();
		
		String paticipantName=this.userClientInfo.getUserParticipant().getParticipantName();		
		ContentSpace activityContentSpace = null;		
		try {			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.userClientInfo.getUserActivitySpace());			
			RootContentObject messageStoreRoot=activityContentSpace.getRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore);
			if(messageStoreRoot==null){
				RootContentObject rootObj=ContentComponentFactory.createRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore);
				messageStoreRoot=activityContentSpace.addRootContentObject(rootObj);
				messageStoreRoot.addSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore,null,false);				
			}			
			BaseContentObject participantMessageBoxRoot=messageStoreRoot.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
					.getSubContentObject(paticipantName);
			if(participantMessageBoxRoot==null){
				participantMessageBoxRoot=messageStoreRoot.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
						.addSubContentObject(paticipantName, null, false);				
			}		
					
			List<ContentObjectProperty> paramLst=new ArrayList<ContentObjectProperty>();
			
			ContentObjectProperty messageTypeProperty=ContentComponentFactory.createContentObjectProperty();
			messageTypeProperty.setMultiple(false);
			messageTypeProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_MessageType);
			messageTypeProperty.setPropertyType(PropertyType.STRING);
			messageTypeProperty.setPropertyValue(messageType);	
			paramLst.add(messageTypeProperty);
			
			ContentObjectProperty messageTitleProperty=ContentComponentFactory.createContentObjectProperty();
			messageTitleProperty.setMultiple(false);
			messageTitleProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle);
			messageTitleProperty.setPropertyType(PropertyType.STRING);
			messageTitleProperty.setPropertyValue(messageTitle);	
			paramLst.add(messageTitleProperty);
			
			ContentObjectProperty messageContentProperty=ContentComponentFactory.createContentObjectProperty();
			messageContentProperty.setMultiple(false);
			messageContentProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent);
			messageContentProperty.setPropertyType(PropertyType.STRING);
			messageContentProperty.setPropertyValue(messageContent);	
			paramLst.add(messageContentProperty);
			
			ContentObjectProperty messageSentTimeProperty=ContentComponentFactory.createContentObjectProperty();
			messageSentTimeProperty.setMultiple(false);
			messageSentTimeProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Property_MessageSentTime);
			messageSentTimeProperty.setPropertyType(PropertyType.LONG);
			messageSentTimeProperty.setPropertyValue(new Long(messageSendDateStamp));	
			paramLst.add(messageSentTimeProperty);
			
			ContentObjectProperty messageNotReadProperty=ContentComponentFactory.createContentObjectProperty();
			messageNotReadProperty.setMultiple(false);
			messageNotReadProperty.setPropertyName(MessageServiceConstant.MESSAGESERVICE_Status_UnreadFlag);
			messageNotReadProperty.setPropertyType(PropertyType.BOOLEAN);
			messageNotReadProperty.setPropertyValue(new Boolean(true));	
			paramLst.add(messageNotReadProperty);			
			participantMessageBoxRoot.addSubContentObject(messageType+"_"+messageSendDateStamp, paramLst, false);				
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}		
	}
}