package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.messageEngine.exception.MessageEngineException;
import com.viewfunction.messageEngine.messageService.MessageUtil;
import com.viewfunction.messageEngine.messageService.util.factory.MessageComponentFactory;
import com.viewfunction.messageEngine.util.PerportyHandler;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseButtonBar;
import com.viewfunction.vfbpm.adminCenter.UI.element.LightContentWindow;
import com.viewfunction.vfbpm.adminCenter.UI.element.PropertyItem;
import com.viewfunction.vfbpm.adminCenter.UI.element.SectionTitleBar;
import com.viewfunction.vfbpm.adminCenter.UI.messageService.MessageServiceConstant;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class MessageServiceConfigurationUI extends VerticalLayout{
	private static final long serialVersionUID = -4100148722098968011L;
	
	private UserClientInfo userClientInfo;
	
	private ComboBox activitySpaceChoise;
	private OptionGroup receiverTypeSelect;
	private TextField messgeTitle;
	private TextField messgeType;
	private RichTextArea messageEditor;		
	private Label selectedRecevers;
	private String[] selectedReceiverIDArray;
	
	public MessageServiceConfigurationUI(UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		
		Panel containerPanel=new Panel();
		containerPanel.setStyleName(Reindeer.PANEL_LIGHT);
		containerPanel.setScrollable(true);
		containerPanel.setSizeFull();		
		this.addComponent(containerPanel);
		
		HorizontalLayout messageServiceActionbarContainer=new HorizontalLayout();			
		messageServiceActionbarContainer.setWidth("60px");	
		
		Button sendBusinessMessageButton=new Button();
		sendBusinessMessageButton.setCaption(null);
		sendBusinessMessageButton.setIcon(UICommonElementDefination.ICON_systemConfig_sendBusinessMessage);
		sendBusinessMessageButton.setStyleName(BaseTheme.BUTTON_LINK);
		sendBusinessMessageButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendBusinessMessageButton"));		
		messageServiceActionbarContainer.addComponent(sendBusinessMessageButton);		
		sendBusinessMessageButton.addListener(new ClickListener(){
			private static final long serialVersionUID = -2753547868851380691L;

			public void buttonClick(ClickEvent event) {							
				renderSendBusinessMessageUI();
			}});
		
		Button sendSystemNotificationButton=new Button();
		sendSystemNotificationButton.setCaption(null);
		sendSystemNotificationButton.setIcon(UICommonElementDefination.ICON_systemConfig_sendSystemMessage);
		sendSystemNotificationButton.setStyleName(BaseTheme.BUTTON_LINK);
		sendSystemNotificationButton.setDescription(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendSystemNofiyButton"));		
		messageServiceActionbarContainer.addComponent(sendSystemNotificationButton);		
		sendSystemNotificationButton.addListener(new ClickListener(){	
			private static final long serialVersionUID = 8909383976787547738L;

			public void buttonClick(ClickEvent event) {							
				renderSendSystemNotificationUI();
			}});			
		
		Embedded messageServiceIconEmbedded=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messageService);
		SectionTitleBar messageServiceConfigSectionTitleBar=new SectionTitleBar(messageServiceIconEmbedded,
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_ConfigOptionSelectionBrowser_messageServiceConfig"),SectionTitleBar.MIDDLEFONT,messageServiceActionbarContainer);
		containerPanel.addComponent(messageServiceConfigSectionTitleBar);		
		try {
			PropertyItem AMQP_VIRTUALHOST_PropertyItem = new PropertyItem(PropertyItem.POSTION_EVEN,null,"AMQP_VIRTUALHOST",PerportyHandler.getPerportyValue(PerportyHandler.AMQP_VIRTUALHOST),null);
			containerPanel.addComponent(AMQP_VIRTUALHOST_PropertyItem);		
			PropertyItem AMQP_USERNAME_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"AMQP_USERNAME",PerportyHandler.getPerportyValue(PerportyHandler.AMQP_USERNAME),null);		
			containerPanel.addComponent(AMQP_USERNAME_PropertyItem);		
			PropertyItem AMQP_USERPWD_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"AMQP_USERPWD",PerportyHandler.getPerportyValue(PerportyHandler.AMQP_USERPWD),null);		
			containerPanel.addComponent(AMQP_USERPWD_PropertyItem);		
			PropertyItem AMQP_CLIENTID_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"AMQP_CLIENTID",PerportyHandler.getPerportyValue(PerportyHandler.AMQP_CLIENTID),null);		
			containerPanel.addComponent(AMQP_CLIENTID_PropertyItem);		
			PropertyItem AMQP_BROKERLIST_PropertyItem=new PropertyItem(PropertyItem.POSTION_EVEN,null,"AMQP_BROKERLIST",PerportyHandler.getPerportyValue(PerportyHandler.AMQP_BROKERLIST),null);		
			containerPanel.addComponent(AMQP_BROKERLIST_PropertyItem);
		} catch (MessageEngineException e) {			
			e.printStackTrace();
		}				
	}
	
	private void renderSendSystemNotificationUI(){
		Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messagesEditor_small);		
		Label propertyNameLable = new Label(" <b style='color:#ce0000;'>"+" "+
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendSystemNofiyLabel")+"</b>", Label.CONTENT_XHTML);			
		VerticalLayout windowContent = new VerticalLayout();
		final TextArea noticicationContentTextArea=new TextArea(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_nofiyContentField"));
		noticicationContentTextArea.setInputPrompt(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_nofiyContentFieldPrompt"));		
		noticicationContentTextArea.setRows(10);
		noticicationContentTextArea.setColumns(40);
		noticicationContentTextArea.setRequired(false);
		windowContent.addComponent(noticicationContentTextArea);		
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendSystemNofiyConfirmButton"));
			
		okButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = -2866539051553122025L;

			public void buttonClick(ClickEvent event) {				
				if(noticicationContentTextArea.getValue()!=null&&!noticicationContentTextArea.getValue().toString().equals("")){
					String notificationContentString=noticicationContentTextArea.getValue().toString();
					MessageUtil messageUtil=MessageComponentFactory.createMessageUtil();
					try {
						messageUtil.sendTextMessage(notificationContentString,MessageServiceConstant.MESSAGESERVICE_SystemMessageTopicName, MessageServiceConstant.MESSAGESERVICE_SystemMessageTopicConfig);
						getWindow().showNotification(userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendSystemNofiySussMsg"),Notification.TYPE_HUMANIZED_MESSAGE);
					} catch (MessageEngineException e) {			
						e.printStackTrace();
					}
				}				
			}
		  });	    
	    buttonList.add(okButton);	  
	    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(700, 30, Alignment.BOTTOM_LEFT, buttonList);		
	    windowContent.addComponent(addParticipantButtonBar);
		LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,windowContent,"500px");				
		lightContentWindow.setWidth("540px");
		lightContentWindow.setHeight("340px");
		lightContentWindow.center();
		lightContentWindow.setResizable(false);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);
	}
	
	private void renderSendBusinessMessageUI(){
		Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messagesEditor_small);		
		Label propertyNameLable = new Label(" <b style='color:#ce0000;'>"+" "+
				this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendBusinessMessageLabel")+"</b>", Label.CONTENT_XHTML);			
		VerticalLayout windowContent = new VerticalLayout();		
		
		activitySpaceChoise = new ComboBox(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_activitySpaceField"));	
		activitySpaceChoise.setNullSelectionAllowed(false);
		activitySpaceChoise.setWidth("100%");		
		try {
			ActivitySpace[] activitySpaceArray=ActivityComponentFactory.getActivitySpaces();
			if(activitySpaceArray!=null){
				for(int i=0;i<activitySpaceArray.length;i++){
					ActivitySpace currentActivitySpace=activitySpaceArray[i];				
					activitySpaceChoise.addItem(currentActivitySpace.getActivitySpaceName());
				}	
			}					
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
		}
		activitySpaceChoise.setFilteringMode(Filtering.FILTERINGMODE_STARTSWITH);
		activitySpaceChoise.setImmediate(true);	
		windowContent.addComponent(activitySpaceChoise);
		
		receiverTypeSelect = new OptionGroup(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_receiverTypeField"));	
		receiverTypeSelect.addItem("Roles");
		receiverTypeSelect.addItem("Participants");
		receiverTypeSelect.addItem("All People In System");
		receiverTypeSelect.select("Roles");		
		receiverTypeSelect.setImmediate(true);
		windowContent.addComponent(receiverTypeSelect);
		
		receiverTypeSelect.addListener(new Property.ValueChangeListener(){
			private static final long serialVersionUID = 7434954075801969985L;

			public void valueChange(ValueChangeEvent event) {				
				 String newReceiverType=event.getProperty().toString();
				 if(newReceiverType.equals("All People In System")){
					 selectedRecevers.setValue("<span style='color:#ce0000;'>"+"All People In System"+"</span>");
				 }else{
					 selectedRecevers.setValue("");
					 selectedReceiverIDArray=null;
				 }				
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
			private static final long serialVersionUID = 4015630343806118929L;

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
		messgeType.setWidth("100%");		
		windowContent.addComponent(messgeType);
		
		messageEditor = new RichTextArea(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_messageContentField"));
		messageEditor.setRequired(true);
		windowContent.addComponent(messageEditor);
		List<Button> buttonList = new ArrayList<Button>();			
		Button okButton = new Button(this.userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendBusinessMessageConfirmButton"));
			
		okButton.addListener(new Button.ClickListener() {	
			private static final long serialVersionUID = 4015630343806118929L;

			public void buttonClick(ClickEvent event) {				
				boolean isSystemLevelMessage=false;
				if(receiverTypeSelect.getValue().toString().equals("All People In System")){
					isSystemLevelMessage=true;					
				}				
				String activitySpace=null;
				String receiverType=receiverTypeSelect.getValue().toString();				
				if(activitySpaceChoise.getValue()!=null){
					activitySpace=activitySpaceChoise.getValue().toString();					
				}else{
					if(!isSystemLevelMessage){
						return;
					}					
				}
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
				if(receiverType.equals("All People In System")){					
					HashMap<String,Object> datamap=new HashMap();									    
					HashMap<String,Object> propertyMap=new HashMap();
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_MessageType, MessageServiceConstant.MESSAGESERVICE_MessageType_NOTICE);
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageTitle, messageTitleStr);	
									
					propertyMap.put(MessageServiceConstant.MESSAGESERVICE_Property_MessageContent, messageContentStr);					
					MessageUtil messageUtil=MessageComponentFactory.createMessageUtil();
					try {						
						messageUtil.sendObjectMessage(MessageServiceConstant.MESSAGESERVICE_SystemMessageTopicName, MessageServiceConstant.MESSAGESERVICE_SystemMessageTopicConfig,datamap,propertyMap);
						getWindow().showNotification(userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendBusinessMessageSussMsg"),Notification.TYPE_HUMANIZED_MESSAGE);
					} catch (MessageEngineException e) {						
						e.printStackTrace();
					}
				}
				if(receiverType.equals("Roles")){
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
						getWindow().showNotification(userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendBusinessMessageSussMsg"),Notification.TYPE_HUMANIZED_MESSAGE);
					} catch (MessageEngineException e) {						
						e.printStackTrace();
					}
				}
				if(receiverType.equals("Participants")){
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
					getWindow().showNotification(userClientInfo.getI18NProperties().getProperty("SystemConfig_MessageServiceConfigurationUI_sendBusinessMessageSussMsg"),Notification.TYPE_HUMANIZED_MESSAGE);
				}
			}
		  });	    
	    buttonList.add(okButton);	  
	    BaseButtonBar addParticipantButtonBar = new BaseButtonBar(700, 30, Alignment.BOTTOM_LEFT, buttonList);		
	    windowContent.addComponent(addParticipantButtonBar);
		
		LightContentWindow lightContentWindow=new LightContentWindow(commentsIcon,propertyNameLable,windowContent,"500px");				
		lightContentWindow.setWidth("540px");
		lightContentWindow.setHeight("604px");
		lightContentWindow.center();
		lightContentWindow.setResizable(true);
		this.getApplication().getMainWindow().addWindow(lightContentWindow);	
	}
	
	private void renderSelectReceiversUI(){
		if(receiverTypeSelect.getValue().toString().equals("All People In System")){			
			return;
		}
		Embedded commentsIcon=new Embedded(null, UICommonElementDefination.ICON_systemConfig_messagesEditor_small);		
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
		if(activitySpaceChoise.getValue()!=null){
			try {				
				String activitySpace=activitySpaceChoise.getValue().toString();
				String receiverType=receiverTypeSelect.getValue().toString();
				ActivitySpace targetActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpace);
				if(receiverType.equals("Roles")){					
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
				if(receiverType.equals("Participants")){
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