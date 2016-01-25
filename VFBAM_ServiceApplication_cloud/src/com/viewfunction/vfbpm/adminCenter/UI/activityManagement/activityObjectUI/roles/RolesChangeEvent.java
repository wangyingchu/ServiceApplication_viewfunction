package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RolesChangeEvent  implements Event{
	private final String activitySpaceName;
	
	public RolesChangeEvent(final String message) {
		this.activitySpaceName = message;
	}
	
	public String getActivitySpaceName() {
	    return activitySpaceName;
	}
	
	public interface RolesChangeListener extends Listener {
	    public void receiveRolesChange(final RolesChangeEvent event);
	}
}
