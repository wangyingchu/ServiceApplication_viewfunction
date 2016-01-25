package com.viewfunction.vfbpm.adminCenter.UI.messageService;

public interface MessageServiceConstant {
	String MESSAGESERVICE_SystemMessageTopicName="MSGS_SystemMessageTopic";
	String MESSAGESERVICE_SystemMessageTopicConfig="{create: always,node: {type:topic,durable: false}}";	
	String MESSAGESERVICE_PublicMessageQueueConfig="{create:always,mode:browse}";
	String MESSAGESERVICE_PersonalMessageQueueConfig="{create:always,mode:consume}";
	
	String MESSAGESERVICE_MessageType="MSGS_MessageType";
	String MESSAGESERVICE_MessageType_NOTICE="MSGS_MessageType_NOTICE";
	String MESSAGESERVICE_MessageType_MESSAGE="MSGS_MessageType_MESSAGE";
	
	String MESSAGESERVICE_Property_MessageTitle="MSGS_Property_MessageTitle";
	String MESSAGESERVICE_Property_MessageContent="MSGS_Property_MessageContent";
	String MESSAGESERVICE_Property_MessageSentTime="MSGS_Property_MessageSentTime";
	String MESSAGESERVICE_Property_MessageType="MSGS_Property_MessageType";
	String MESSAGESERVICE_Property_MessageGroups="MSGS_Property_MessageGroups";
	
	String MESSAGESERVICE_Status_UnreadFlag="MSGS_Status_MessageUnRead";	
}