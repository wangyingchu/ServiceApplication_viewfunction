package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RostersChangeEvent implements Event{
	private final String activitySpaceName;
	
	public RostersChangeEvent(final String message) {
		this.activitySpaceName = message;
	}	
	public String getActivitySpaceName() {
	    return activitySpaceName;
	}	
	public interface RostersChangeListener extends Listener {
	    public void receiveRostersChange(final RostersChangeEvent event);
	}
}