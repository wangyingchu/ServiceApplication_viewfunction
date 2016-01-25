package com.viewfunction.vfbpm.adminCenter.UI.messageService;

import java.util.Queue;

import com.viewfunction.messageEngine.messageService.ObjectMessageEntry;
import com.viewfunction.messageEngine.messageService.RealTimeNotificationListener;
import com.viewfunction.messageEngine.messageService.TextMessageEntry;

public class ActivityNotificationListener implements RealTimeNotificationListener{	
	
	private Queue<TextMessageEntry> textMessageQueue;
	private Queue<ObjectMessageEntry> objectMessageQueue;
	
	public ActivityNotificationListener(Queue<TextMessageEntry> textMessageQueue,Queue<ObjectMessageEntry> objectMessageQueue){		
		this.textMessageQueue=textMessageQueue;
		this.objectMessageQueue=objectMessageQueue;		
	}	

	public void receivedObjectMessage(ObjectMessageEntry message) {
		this.objectMessageQueue.add(message);	
	}

	public void receivedTextMessage(TextMessageEntry message) {			
		this.textMessageQueue.add(message);			
	}
}