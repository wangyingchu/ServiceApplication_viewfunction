package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class RoleQueuesChangeEvent implements Event{
	private final String activitySpaceName;
	
	public RoleQueuesChangeEvent(final String message) {
		this.activitySpaceName = message;
	}
	
	public String getActivitySpaceName() {
	    return activitySpaceName;
	}
	
	public interface RoleQueuesChangeListener extends Listener {
	    public void receiveRoleQueuesChange(final RoleQueuesChangeEvent event);
	}
}
