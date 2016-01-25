package com.viewfunction.vfbpm.adminCenter.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.github.wolfie.refresher.Refresher;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureauImpl.CCRActivityEngineConstant;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.messageEngine.exception.MessageEngineException;
import com.viewfunction.messageEngine.messageService.MessageUtil;
import com.viewfunction.messageEngine.messageService.ObjectMessageEntry;
import com.viewfunction.messageEngine.messageService.RealTimeNotificationReceiver;
import com.viewfunction.messageEngine.messageService.TextMessageEntry;
import com.viewfunction.messageEngine.messageService.util.factory.MessageComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UserClientRefreshListener;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.messageService.ActivityMessageListener;
import com.viewfunction.vfbpm.adminCenter.UI.messageService.ActivityNotificationListener;
import com.viewfunction.vfbpm.adminCenter.UI.messageService.MessageCenterEditor;
import com.viewfunction.vfbpm.adminCenter.UI.messageService.MessageServiceConstant;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ConsoleTitle  extends HorizontalLayout{
	private static final long serialVersionUID = -2420239096263803661L;	

	private UserClientInfo userClientInfo;
	public  Label applicationMessageLabel;
	private UserClientRefreshListener UserClientRefreshListener; 
	private HorizontalLayout messageStatusContainer;
	private  HorizontalLayout sendMessageContainer;	
	
	private OptionGroup receiverTypeSelect;
	private TextField messgeTitle;
	private TextField messgeType;
	private RichTextArea messageEditor;		
	private Label selectedRecevers;
	private String[] selectedReceiverIDArray;
	
	public ConsoleTitle(UserClientInfo userClientInfo){
		setHeight("30px");
	    setWidth("100%");
	    setSpacing(true);
	    setMargin(false, true, false, false);
	    this.userClientInfo=userClientInfo;
	    this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppTitlebar);	     
	    Label applicationTitleLabel=new Label(this.userClientInfo.getI18NProperties().getProperty("applicationTitle"));	     
	    applicationTitleLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppTitlebar_appTitleText);
	    this.addComponent(applicationTitleLabel);		
	}
	
	public ConsoleTitle(final UserClientInfo userClientInfo,String consoleTitle){
		setHeight("30px");
	    setWidth("100%");
	    setSpacing(true);
	    setMargin(false, true, false, false);
	    this.userClientInfo=userClientInfo;
	    this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppTitlebar);	 	    
	    HorizontalLayout leftElementContainer=new HorizontalLayout();	     
	    HorizontalLayout rightElementContainer=new HorizontalLayout();     
	    this.addComponent(leftElementContainer);
	    this.addComponent(rightElementContainer); 	     
	    this.setComponentAlignment(leftElementContainer, Alignment.MIDDLE_LEFT);
	    this.setComponentAlignment(rightElementContainer, Alignment.MIDDLE_RIGHT);	    
	    
	    Label applicationTitleLabel=new Label(consoleTitle);	     
	    applicationTitleLabel.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_AppTitlebar_appTitleText);	    
	    leftElementContainer.addComponent(applicationTitleLabel);	   
	    int unReadedMessageNumber=getUnReadMessageNumber();	    
	    String numberColore=unReadedMessageNumber>0?"#ce0000":"#444444";	    
	    Embedded messageIconEmbedded = new Embedded(null, UICommonElementDefination.ICON_systemConfig_messagesEditor);	    
	    applicationMessageLabel=new Label("<span style='cursor:pointer;'>"+
	    		this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageStatusTitle")+" ( "+"<span style='color:"+numberColore+";font-weight:bold';>"+unReadedMessageNumber+"</span>"+" )</span>",Label.CONTENT_XHTML);	
    
	    sendMessageContainer=new HorizontalLayout(); 
	    sendMessageContainer.addComponent(new Embedded(null, UICommonElementDefination.ICON_systemConfig_sendMessage));
	    
	    HorizontalLayout textSpaceDiv_1=new HorizontalLayout();
	    textSpaceDiv_1.setWidth("5px");
	    sendMessageContainer.addComponent(textSpaceDiv_1);
	    
	    sendMessageContainer.addComponent(new Label("<span style='cursor:pointer;'>"+
	    		this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_sendMessageTitle")+"</span>",Label.CONTENT_XHTML));	   
	    sendMessageContainer.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = -2196324638150996211L;

			public void layoutClick(LayoutClickEvent event) {				
				renderSendMessageUI();
			}});	    
	    rightElementContainer.addComponent(sendMessageContainer); 
	    
	    HorizontalLayout spaceDiv_1=new HorizontalLayout();
	    spaceDiv_1.setWidth("30px");
	    rightElementContainer.addComponent(spaceDiv_1);
	    
	    messageStatusContainer=new HorizontalLayout(); 
	    messageStatusContainer.addComponent(messageIconEmbedded);
	    HorizontalLayout textSpaceDiv_2=new HorizontalLayout();
	    textSpaceDiv_2.setWidth("5px");
	    messageStatusContainer.addComponent(textSpaceDiv_2);
	    messageStatusContainer.addComponent(applicationMessageLabel);	   
	    messageStatusContainer.addListener(new LayoutClickListener(){
			private static final long serialVersionUID = -2196324638150996211L;

			public void layoutClick(LayoutClickEvent event) {				
				renderMessageCenterUI();
			}});	    
	    rightElementContainer.addComponent(messageStatusContainer);
	    
	    final Refresher refresher = new Refresher(); 
	    Queue<TextMessageEntry> systemTextMessageQueue = new LinkedList<TextMessageEntry>();
	    Queue<ObjectMessageEntry> systemObjectMessageQueue = new LinkedList<ObjectMessageEntry>();	    
	    Queue<ObjectMessageEntry> personalObjectMessageQueue = new LinkedList<ObjectMessageEntry>();
	    Queue<ObjectMessageEntry> publicObjectMessageQueue = new LinkedList<ObjectMessageEntry>();
	    //refresh every 10 seconds to redraw userclients for messages and notifications
		refresher.setRefreshInterval(10000); 
		this.UserClientRefreshListener=new UserClientRefreshListener(this,this.userClientInfo,systemTextMessageQueue,systemObjectMessageQueue,publicObjectMessageQueue,personalObjectMessageQueue,unReadedMessageNumber);
		refresher.addListener(this.UserClientRefreshListener);
		rightElementContainer.addComponent(refresher);			
		initNotificatonService(this.userClientInfo,systemTextMessageQueue,systemObjectMessageQueue);
		initPersonalMessageService(this.userClientInfo,personalObjectMessageQueue);
		initPublicMessageService(this.userClientInfo,publicObjectMessageQueue);
	}	
	
	private void initNotificatonService(UserClientInfo userClientInfo,Queue<TextMessageEntry> textMessageQueue,Queue<ObjectMessageEntry> objectMessageQueue){		
		try {					
			ActivityNotificationListener activityNotificationListener=new ActivityNotificationListener(textMessageQueue,objectMessageQueue);			
			RealTimeNotificationReceiver notificationReceiver= MessageComponentFactory.
					createRealTimeNotificationReceiver(MessageServiceConstant.MESSAGESERVICE_SystemMessageTopicName,MessageServiceConstant.MESSAGESERVICE_SystemMessageTopicConfig, activityNotificationListener);
			userClientInfo.setNotificationReceiver(notificationReceiver);
			notificationReceiver.startReceive();
		}catch (MessageEngineException e) {			
			e.printStackTrace();
		}		
	}
	
	private void initPersonalMessageService(UserClientInfo userClientInfo, Queue<ObjectMessageEntry> personalObjectMessageQueue){
		String activitySpacName=userClientInfo.getUserActivitySpace();
		String participantName=userClientInfo.getUserParticipant().getParticipantName();
		String queueName=activitySpacName+"_"+participantName;
		try {
			ActivityMessageListener activityPersonalMessageListener=new ActivityMessageListener(personalObjectMessageQueue);
			RealTimeNotificationReceiver personalMessageReceiver= MessageComponentFactory.
					createRealTimeNotificationReceiver(queueName,MessageServiceConstant.MESSAGESERVICE_PersonalMessageQueueConfig, activityPersonalMessageListener);
			userClientInfo.setPersonalMessageReceiver(personalMessageReceiver);
			personalMessageReceiver.startReceive();		
		}catch (MessageEngineException e) {			
			e.printStackTrace();
		}	
	}
	
	private void initPublicMessageService(UserClientInfo userClientInfo, Queue<ObjectMessageEntry> publicObjectMessageQueue){
		String activitySpacName=userClientInfo.getUserActivitySpace();
		try {
			ActivityMessageListener activityPublicMessageListener=new ActivityMessageListener(publicObjectMessageQueue);
			RealTimeNotificationReceiver publicMessageReceiver= MessageComponentFactory.
					createRealTimeNotificationReceiver(activitySpacName,MessageServiceConstant.MESSAGESERVICE_PublicMessageQueueConfig, activityPublicMessageListener);
			userClientInfo.setPublicMessageReceiver(publicMessageReceiver);
			publicMessageReceiver.startReceive();		
		}catch (MessageEngineException e) {			
			e.printStackTrace();
		}		
	}
	
	private int getUnReadMessageNumber(){
		ContentSpace activityContentSpace = null;		
		try {			
			activityContentSpace=ContentComponentFactory.connectContentSpace(this.userClientInfo.getUserActivitySpace());			
			RootContentObject messageStoreRoot=activityContentSpace.getRootContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_MessageStore);
			if(messageStoreRoot==null){
				return 0;								
			}			
			BaseContentObject participantMessageBoxRoot=messageStoreRoot.getSubContentObject(CCRActivityEngineConstant.ACTIVITYSPACE_ParticipantMessageStore)
					.getSubContentObject(this.userClientInfo.getUserParticipant().getParticipantName());
			if(participantMessageBoxRoot==null){
				return 0;			
			}			
			List<BaseContentObject> messagesObjList=participantMessageBoxRoot.getSubContentObjects(null);
			int unReadedMessageNumber=0;
			for(BaseContentObject BaseContentObject:messagesObjList){				
				Object ifUnReadedObj=BaseContentObject.getProperty(MessageServiceConstant.MESSAGESERVICE_Status_UnreadFlag).getPropertyValue();
				if(((Boolean)ifUnReadedObj).booleanValue()){
					unReadedMessageNumber++;					
				}				
			}
			return unReadedMessageNumber;		
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		}finally{
			activityContentSpace.closeContentSpace();			
		}		
		return 0;
	}
	

	private void renderMessageCenterUI(){
		Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messagesEditor_small);		
		Label propertyNameLable = new Label(" <b style='color:#ce0000;'>"+" "+
				this.userClientInfo.getI18NProperties().getProperty("userApplicationMessageCenterLinkLabel")+"</b>", Label.CONTENT_XHTML);			
		MessageCenterEditor messageCenterEditor=new MessageCenterEditor(this,this.userClientInfo);				
		LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,messageCenterEditor,"500px");				
		lightContentWindow.setWidth("60%");
		lightContentWindow.setHeight("60%");
		lightContentWindow.center();		
		CloseListener closeListener=new CloseListener(){
			private static final long serialVersionUID = 8881115339975903687L;

			public void windowClose(CloseEvent e) {
				 messageStatusContainer.setEnabled(true);				
			}};
		lightContentWindow.addListener(closeListener);		
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
		messageStatusContainer.setEnabled(false);
	}

	public void refreshUnreadMessageNumber(){
		int unReadMessageNumber=getUnReadMessageNumber();
		this.applicationMessageLabel.setValue("<span style='cursor:pointer;'>"+
				userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageStatusTitle")+" ( "+"<span style='color:#ce0000;font-weight: bold'>"+unReadMessageNumber+"</span>"+" )</span>");
		this.UserClientRefreshListener.unReadedMessageNumber=unReadMessageNumber;
	}	
	
	private void renderSendMessageUI(){
		Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_sendMessage);		
		Label propertyNameLable = new Label(" <b style='color:#ce0000;'>"+" "+
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_sendMessageWindowTitle")+"</b>", Label.CONTENT_XHTML);			
		VerticalLayout windowContent = new VerticalLayout();
		
		receiverTypeSelect = new OptionGroup(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_receiverTypeField"));	
		receiverTypeSelect.addItem(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_Department"));
		receiverTypeSelect.addItem(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_People"));		
		receiverTypeSelect.select(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_Department"));		
		receiverTypeSelect.setImmediate(true);
		windowContent.addComponent(receiverTypeSelect);
		
		receiverTypeSelect.addListener(new Property.ValueChangeListener(){
			private static final long serialVersionUID = -6782971614912398749L;

			public void valueChange(ValueChangeEvent event) {
				 selectedRecevers.setValue("");
				 selectedReceiverIDArray=null;					
			}			
		});
		
		HorizontalLayout divSpace_1Layout=new HorizontalLayout();
		divSpace_1Layout.setHeight("10px");
		windowContent.addComponent(divSpace_1Layout);
		
		HorizontalLayout targetListContainer=new HorizontalLayout();		
		Button selectReceiversButton=new Button();
		selectReceiversButton.setCaption(null);
		selectReceiversButton.setIcon(UICommonElementDefination.ICON_addRoleParticipant);
		selectReceiversButton.setStyleName(BaseTheme.BUTTON_LINK);
		selectReceiversButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_selectReceiverTypeButton"));			
		targetListContainer.addComponent(selectReceiversButton);
		selectReceiversButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 920007604408465042L;

			public void buttonClick(ClickEvent event) {
				renderSelectReceiversUI();
			}
		  });
		
		Label receiverList=new Label(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_messageRceiverLabel"));
		targetListContainer.addComponent(receiverList);			
		selectedRecevers=new Label("",Label.CONTENT_XHTML);
		targetListContainer.addComponent(selectedRecevers);	
		windowContent.addComponent(targetListContainer);
		
		HorizontalLayout divSpace_2Layout=new HorizontalLayout();
		divSpace_2Layout.setHeight("10px");
		windowContent.addComponent(divSpace_2Layout);
		
		messgeTitle=new TextField(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_messageTitleField"));
		messgeTitle.setWidth("100%");
		messgeTitle.setRequired(true);
		windowContent.addComponent(messgeTitle);
		
		messgeType=new TextField(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_messageTypeField"));			
		Participant currentParticipant=this.userClientInfo.getUserParticipant();		
		String userDisplayName=currentParticipant.getDisplayName()!=null?currentParticipant.getDisplayName():currentParticipant.getParticipantName();		
		messgeType.setValue(userDisplayName+this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_onesMessageLabel"));		
		
		messageEditor = new RichTextArea(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_messageContentField"));
		messageEditor.setRequired(true);
		windowContent.addComponent(messageEditor);
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_sendMessageButton"));
			
		okButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = 4634500936983452936L;

			public void buttonClick(ClickEvent event) {				
				boolean isSystemLevelMessage=false;							
				String activitySpace=userClientInfo.getUserActivitySpace();
				String receiverType=receiverTypeSelect.getValue().toString();				
				if(selectedReceiverIDArray==null||selectedReceiverIDArray.length==0){
					if(!isSystemLevelMessage){
						return;
					}
				}
				String messageTitleStr=null;
				if(messgeTitle.getValue()!=null&&!messgeTitle.getValue().toString().equals("")){
					messageTitleStr=messgeTitle.getValue().toString();					
				}else{
					return;
				}
				String messageTypeStr=null;
				if(messgeType.getValue()!=null&&!messgeType.getValue().toString().equals("")){
					messageTypeStr=messgeType.getValue().toString();					
				}else{
					if(!isSystemLevelMessage){
						return;
					}
				}
				String messageContentStr=null;
				if(messageEditor.getValue()!=null&&!messageEditor.getValue().toString().equals("")){
					messageContentStr=messageEditor.getValue().toString();					
				}else{
					return;
				}				
				if(receiverType.equals(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_Department"))){
					HashMap<String,Object> datamap=new HashMap();									    
					HashMap<String,Object> propertyMap=new HashMap();
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle, messageTitleStr);	
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageType, messageTypeStr);					
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent, messageContentStr);											
					List<String> roles = new ArrayList<String>();
					for(String role:selectedReceiverIDArray){
						roles.add(role);						
					}									
					datamap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageGroups, roles);	
					MessageUtil messageUtil=MessageComponentFactory.createMessageUtil();
					try {
						messageUtil.sendObjectMessage(activitySpace, MessageServiceConstant.MESSAGESERVICE_PublicMessageQueueConfig,datamap,propertyMap);
						getWindow().showNotification(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_sendDepartmentMessageSucMsg"),Notification.TYPE_HUMANIZED_MESSAGE);
					} catch (MessageEngineException e) {						
						e.printStackTrace();
					}
				}
				if(receiverType.equals(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_People"))){
					HashMap<String,Object> datamap=new HashMap();									    
					HashMap<String,Object> propertyMap=new HashMap();
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle, messageTitleStr);	
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageType, messageTypeStr);					
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent, messageContentStr);						
					MessageUtil messageUtil=MessageComponentFactory.createMessageUtil();					
					for(String people:selectedReceiverIDArray){
						try {
							messageUtil.sendObjectMessage(activitySpace+"_"+people,  MessageServiceConstant.MESSAGESERVICE_PersonalMessageQueueConfig,datamap,propertyMap);							
						} catch (MessageEngineException e) {							
							e.printStackTrace();
						}												
					}
					getWindow().showNotification(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_sendPeopleMessageSucMsg"),Notification.TYPE_HUMANIZED_MESSAGE);
				}
			}
		  });	    
	    buttonList.add(okButton);	  
	    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(750, 30, Alignment.BOTTOM_LEFT, buttonList);		
	    windowContent.addComponent(addParticipantButtonBar);
		
		LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,windowContent,"500px");		
		CloseListener closeListener=new CloseListener(){
			private static final long serialVersionUID = -7416016536145176318L;

			public void windowClose(CloseEvent e) {
				sendMessageContainer.setEnabled(true);				
			}};			
		lightContentWindow.addListener(closeListener);
		lightContentWindow.setWidth("540px");
		lightContentWindow.setHeight("520px");
		lightContentWindow.center();		
		lightContentWindow.setResizable(false);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
		sendMessageContainer.setEnabled(false);
	}
	
	private void renderSelectReceiversUI(){		
		Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_sendMessage);		
		Label propertyNameLable = new Label(" <b style='color:#ce0000;'>"+" "+
				userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_selectReceiverWindowTitle")+"</b>", Label.CONTENT_XHTML);		
		VerticalLayout windowContent = new VerticalLayout();			
		final LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,windowContent,"300px");		
		final OptionGroup receiversSelect = new OptionGroup(userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_selectReceiverField"));
		receiversSelect.setMultiSelect(true);
		receiversSelect.setNullSelectionAllowed(false);			
		final IndexedContainer container = new IndexedContainer();		
		container.addContainerProperty("ReceiverID", String.class,null);
        container.addContainerProperty("ReceiverDisplayName", String.class,null);
        receiversSelect.setContainerDataSource(container);
        receiversSelect.setItemCaptionPropertyId("ReceiverDisplayName");		
		
			try {				
				String activitySpace=userClientInfo.getUserActivitySpace();
				String receiverType=receiverTypeSelect.getValue().toString();
				ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpace);
				if(receiverType.equals(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_Department"))){					
					Role[] roles=targetActivitySpace.getRoles();
					if(roles!=null){
						for(int i=0;i<roles.length;i++){							
							Role currentRole=roles[i];						
							String roleName=currentRole.getDisplayName()!=null?currentRole.getDisplayName():currentRole.getRoleName();							
							String id = ""+i;
							Item item = container.addItem(id);								
							item.getItemProperty("ReceiverID").setValue(currentRole.getRoleName());  
							item.getItemProperty("ReceiverDisplayName").setValue(roleName);	
						}	
					}									
				}
				if(receiverType.equals(userClientInfo.getI18NProperties().getProperty("SystemConfig_ConsoleTitle_messageReceiverType_People"))){
					Participant[] participants=targetActivitySpace.getParticipants();
					if(participants!=null){
						for(int i=0;i<participants.length;i++){
							Participant currentParticipant=participants[i];								
							String participentName=currentParticipant.getDisplayName()!=null?currentParticipant.getDisplayName():currentParticipant.getParticipantName();
							String id = ""+i;
							Item item = container.addItem(id);								
							item.getItemProperty("ReceiverID").setValue(currentParticipant.getParticipantName());  
							item.getItemProperty("ReceiverDisplayName").setValue(participentName);
						}
					}
				}			
			} catch (ActivityEngineRuntimeException e) {				
				e.printStackTrace();
			}
		
		windowContent.addComponent(receiversSelect);
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_selectReceiverButton"));
			
		okButton.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -4920873721672636180L;

			public void buttonClick(ClickEvent event) {				
				Set selectedReceiverIdxSet=(Set)receiversSelect.getValue();				
				Object[] idxArry=selectedReceiverIdxSet.toArray();	
				selectedReceiverIDArray=new String[idxArry.length];
				String[] selectedReceverName=new String[idxArry.length];
				for(int i=0;i<idxArry.length;i++){
					Object obj=idxArry[i];
					Item selecteItem=container.getItem(obj);					
					selectedReceiverIDArray[i]=selecteItem.getItemProperty("ReceiverID").getValue().toString();
					selectedReceverName[i]=selecteItem.getItemProperty("ReceiverDisplayName").getValue().toString();
				}
				StringBuffer sb=new StringBuffer();
				for(String receiverName:selectedReceverName){
					sb.append("<span style='color:#ce0000;'>"+receiverName+"</span>");
					sb.append(";");						
				}		
				selectedRecevers.setValue(sb.toString());
				getApplication().getMainWindow().removeWindow(lightContentWindow);				
			}
		  });	    
	    buttonList.add(okButton);	  
	    BaseButtonBar addReceiverButtonBar = new BaseButtonBar(400, 30, Alignment.BOTTOM_LEFT, buttonList);		
	    windowContent.addComponent(addReceiverButtonBar);					
		lightContentWindow.center();
		lightContentWindow.setModal(true);
		lightContentWindow.setResizable(false);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);		
	}
}