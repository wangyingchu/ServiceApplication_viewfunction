package com.viewfunction.vfbpm.adminCenter.UI.messageService;

import java.util.Queue;

import com.viewfunction.messageEngine.messageService.ObjectMessageEntry;
import com.viewfunction.messageEngine.messageService.RealTimeNotificationListener;
import com.viewfunction.messageEngine.messageService.TextMessageEntry;

public class ActivityMessageListener implements RealTimeNotificationListener{
	private Queue<ObjectMessageEntry> objectMessageQueue;
	
	public ActivityMessageListener(Queue<ObjectMessageEntry> objectMessageQueue){
		this.objectMessageQueue=objectMessageQueue;
	}	
	
	public void receivedObjectMessage(ObjectMessageEntry message) {
		this.objectMessageQueue.add(message);			
	}

	public void receivedTextMessage(TextMessageEntry arg0) {}
}