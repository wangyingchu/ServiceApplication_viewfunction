package com.viewfunction.vfbpm.adminCenter.UI.activityManagement;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.ProcessQueue;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.ProcessQueueUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.ProcessQueuesUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.ActivityDefinitionsChangeEvent.ActivityDefinitionsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.BusinessActivityDefinitionUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.BusinessActivityDefinitionsUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsChangeEvent.ParticipantsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueueUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesChangeEvent.RoleQueuesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RoleUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesChangeEvent.RolesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RosterUI;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RostersChangeEvent.RostersChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RostersUI;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityObjectDetail extends VerticalLayout{
	private static final long serialVersionUID = -602377599621557724L;
	public UserClientInfo userClientInfo;
	public ActivityManagementPanel activityManagementPanel;
	public VerticalLayout activityObjectContainer;
	public List<ParticipantsChangeListener> participantsChangeListenerList;
	public List<RolesChangeListener> rolesChangeListenerList;
	public List<RoleQueuesChangeListener> roleQueuesChangeListenerList;
	public List<RostersChangeListener> rostersChangeListenerList;
	public List<ActivityDefinitionsChangeListener> activityDefinitionsChangeListenerList;
	
	public ActivityObjectDetail(UserClientInfo userClientInfo){
		activityObjectContainer=new VerticalLayout();		
		this.userClientInfo=userClientInfo;
		//render title bar
		HorizontalLayout actionBarHorizontalLayout=new HorizontalLayout();
		actionBarHorizontalLayout.setWidth("100%");	
		actionBarHorizontalLayout.setHeight("23px");		
		actionBarHorizontalLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);
		activityObjectContainer.addComponent(actionBarHorizontalLayout);
		this.participantsChangeListenerList=new ArrayList<ParticipantsChangeListener>();
		this.rolesChangeListenerList=new ArrayList<RolesChangeListener>();
		this.roleQueuesChangeListenerList=new ArrayList<RoleQueuesChangeListener>();
		this.rostersChangeListenerList=new ArrayList<RostersChangeListener>();
		this.activityDefinitionsChangeListenerList=new ArrayList<ActivityDefinitionsChangeListener>();
		this.addComponent(activityObjectContainer);
	}
	
	public void setActivityManagementPanel(ActivityManagementPanel activityManagementPanel){
		this.activityManagementPanel=activityManagementPanel;
	}

	public ActivityManagementPanel getActivityManagementPanel(){
		return activityManagementPanel;
	}
	
	public void renderParticipantsUI(Participant[] participantArray,String activitySpaceName){
		activityObjectContainer.removeAllComponents();	
		for(ParticipantsChangeListener participantsChangeListener:participantsChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(participantsChangeListener);			
		}
		participantsChangeListenerList.clear();		
		activityObjectContainer.addComponent(new ParticipantsUI(participantArray,this.userClientInfo,this,activitySpaceName));	
	}
	
	public void renderParticipantsUI(String activitySpaceName){
		activityObjectContainer.removeAllComponents();
		for(ParticipantsChangeListener participantsChangeListener:participantsChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(participantsChangeListener);			
		}
		participantsChangeListenerList.clear();	
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant[] participantArray=currentActivitySpace.getParticipants();
			activityObjectContainer.addComponent(new ParticipantsUI(participantArray,this.userClientInfo,this,activitySpaceName));
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
	}
	
	public void renderParticipantUI(Participant participant){		
		activityObjectContainer.removeAllComponents();	
		activityObjectContainer.addComponent(new ParticipantUI(participant,this.userClientInfo,this));
	}	
	
	public void renderRolesUI(Role[] roleArray,String activitySpaceName){
		activityObjectContainer.removeAllComponents();
		for(RolesChangeListener rolesChangeListener:rolesChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(rolesChangeListener);			
		}
		rolesChangeListenerList.clear();		
		activityObjectContainer.addComponent(new RolesUI(roleArray,this.userClientInfo,this,activitySpaceName));
	}
	
	public void renderRolesUI(String activitySpaceName){
		activityObjectContainer.removeAllComponents();
		for(RolesChangeListener rolesChangeListener:rolesChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(rolesChangeListener);			
		}
		rolesChangeListenerList.clear();
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Role[] roleArray=currentActivitySpace.getRoles();
			activityObjectContainer.addComponent(new RolesUI(roleArray,this.userClientInfo,this,activitySpaceName));
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}	
	
	public void renderRoleUI(Role currentRole){
		activityObjectContainer.removeAllComponents();
		activityObjectContainer.addComponent(new RoleUI(currentRole,this.userClientInfo,this));
	}
	
	public void renderBusinessActivityDefinitionsUI(BusinessActivityDefinition[] activityDefinitionArray,String activitySpaceName){
		activityObjectContainer.removeAllComponents();	
		activityObjectContainer.addComponent(new BusinessActivityDefinitionsUI(activityDefinitionArray,this.userClientInfo,this,activitySpaceName));
	}
	
	public void renderBusinessActivityDefinitionsUI(String activitySpaceName){
		activityObjectContainer.removeAllComponents();		
		for(ActivityDefinitionsChangeListener activityDefinitionsChangeListener:activityDefinitionsChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(activityDefinitionsChangeListener);			
		}
		activityDefinitionsChangeListenerList.clear();	
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
		try {
			BusinessActivityDefinition[] activityDefinitionArray=currentActivitySpace.getBusinessActivityDefinitions();
			activityObjectContainer.addComponent(new BusinessActivityDefinitionsUI(activityDefinitionArray,this.userClientInfo,this,activitySpaceName));
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}			
	}	
	
	public void renderBusinessActivityDefinitionUI(BusinessActivityDefinition currentBusinessActivityDefinition){
		activityObjectContainer.removeAllComponents();	
		activityObjectContainer.addComponent(new BusinessActivityDefinitionUI(currentBusinessActivityDefinition,this.userClientInfo,this));
	}
	
	public void renderRostersUI(Roster[] rosterArray,String activitySpaceName){
		activityObjectContainer.removeAllComponents();	
		for(RostersChangeListener rostersChangeListener:rostersChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(rostersChangeListener);			
		}
		rostersChangeListenerList.clear();
		activityObjectContainer.addComponent(new RostersUI(rosterArray,this.userClientInfo,this,activitySpaceName));
	}
	
	public void renderRostersUI(String activitySpaceName){
		activityObjectContainer.removeAllComponents();
		for(RostersChangeListener rostersChangeListener:rostersChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(rostersChangeListener);			
		}
		rostersChangeListenerList.clear();
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Roster[] rosterArray=currentActivitySpace.getRosters();
			activityObjectContainer.addComponent(new RostersUI(rosterArray,this.userClientInfo,this,activitySpaceName));
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
	
	public void renderRosterUI(Roster currentRoster){
		activityObjectContainer.removeAllComponents();
		activityObjectContainer.addComponent(new RosterUI(currentRoster,this.userClientInfo,this));
	}
	
	public void renderRoleQueuesUI(RoleQueue[] roleQueueArray,String activitySpaceName){
		activityObjectContainer.removeAllComponents();
		for(RoleQueuesChangeListener roleQueuesChangeListener:roleQueuesChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(roleQueuesChangeListener);			
		}
		roleQueuesChangeListenerList.clear();
		activityObjectContainer.addComponent(new RoleQueuesUI(roleQueueArray,this.userClientInfo,this,activitySpaceName));
	}
	
	public void renderRoleQueuesUI(String activitySpaceName){
		activityObjectContainer.removeAllComponents();
		activityObjectContainer.removeAllComponents();
		for(RoleQueuesChangeListener roleQueuesChangeListener:roleQueuesChangeListenerList){
			this.userClientInfo.getEventBlackboard().removeListener(roleQueuesChangeListener);			
		}
		roleQueuesChangeListenerList.clear();
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			RoleQueue[] roleQueueArray=currentActivitySpace.getRoleQueues();
			activityObjectContainer.addComponent(new RoleQueuesUI(roleQueueArray,this.userClientInfo,this,activitySpaceName));
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	}
	
	public void renderRoleQueueUI(RoleQueue currentRoleQueue){
		activityObjectContainer.removeAllComponents();	
		activityObjectContainer.addComponent(new RoleQueueUI(currentRoleQueue,this.userClientInfo,this));
	}
	
	public void renderProcessQueuesUI(ProcessQueue[] processQueueArray){
		activityObjectContainer.removeAllComponents();	
		activityObjectContainer.addComponent(new ProcessQueuesUI(processQueueArray,this.userClientInfo,this));
	}
	
	public void renderProcessQueuesUI(String activitySpaceName){
		activityObjectContainer.removeAllComponents();	
		ActivitySpace currentActivitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		ProcessQueue[] processQueueArray=currentActivitySpace.getProcessQueues(null,null);
		activityObjectContainer.addComponent(new ProcessQueuesUI(processQueueArray,this.userClientInfo,this));	
	}
	
	public void renderProcessQueueUI(ProcessQueue currentProcessQueue){
		activityObjectContainer.removeAllComponents();	
		activityObjectContainer.addComponent(new ProcessQueueUI(currentProcessQueue,this.userClientInfo,this));
	}
}