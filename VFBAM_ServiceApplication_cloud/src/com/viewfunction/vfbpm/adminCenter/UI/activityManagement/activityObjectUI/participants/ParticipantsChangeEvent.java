package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants;

import com.github.wolfie.blackboard.Event;
import com.github.wolfie.blackboard.Listener;

public class ParticipantsChangeEvent implements Event{
	private final String activitySpaceName;
	
	public ParticipantsChangeEvent(final String message) {
		this.activitySpaceName = message;
	}
	
	public String getActivitySpaceName() {
	    return activitySpaceName;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + activitySpaceName;
	}
	
	public interface ParticipantsChangeListener extends Listener {
	    public void receiveParticipantsChange(final ParticipantsChangeEvent event);
	}
}
