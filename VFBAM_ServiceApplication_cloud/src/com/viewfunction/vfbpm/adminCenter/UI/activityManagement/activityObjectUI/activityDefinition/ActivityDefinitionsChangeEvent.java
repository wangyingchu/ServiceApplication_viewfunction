package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ActivityDefinitionsChangeEvent  implements Event{
	private final String activitySpaceName;
	
	public ActivityDefinitionsChangeEvent(final String message) {
		this.activitySpaceName = message;
	}
	
	public String getActivitySpaceName() {
	    return activitySpaceName;
	}
	
	public interface ActivityDefinitionsChangeListener extends Listener {
	    public void receiveActivityDefinitionsChange(final ActivityDefinitionsChangeEvent event);
	}
}
