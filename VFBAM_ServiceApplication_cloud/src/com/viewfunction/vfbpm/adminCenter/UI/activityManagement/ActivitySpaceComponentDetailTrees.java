package com.viewfunction.vfbpm.adminCenter.UI.activityManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
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
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree.ActivityDefinitionTree;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree.ActivityParticipantTree;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree.ActivityProcessQueueTree;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree.ActivityRoleQueueTree;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree.ActivityRoleTree;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree.ActivityRosterTree;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.ActivityDefinitionsChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.activityDefinition.ActivityDefinitionsChangeEvent.ActivityDefinitionsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.participants.ParticipantsChangeEvent.ParticipantsChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roleQueue.RoleQueuesChangeEvent.RoleQueuesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roles.RolesChangeEvent.RolesChangeListener;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RostersChangeEvent;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.roster.RostersChangeEvent.RostersChangeListener;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivitySpaceComponentDetailTrees extends VerticalLayout implements ParticipantsChangeListener,RolesChangeListener,RoleQueuesChangeListener,RostersChangeListener,ActivityDefinitionsChangeListener{
	private static final long serialVersionUID = -7026315529696614656L;
	public static final String ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME="ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME";
	public static final String ACTIVITY_SPACE_NAME="ACTIVITY_SPACE_NAME";
	public static final String ACTIVITY_SPACE_PARTICIPANT="ACTIVITY_SPACE_PARTICIPANT";
	public static final String ACTIVITY_SPACE_PARTICIPANTS="ACTIVITY_SPACE_PARTICIPANTS";
	public static final String ACTIVITY_SPACE_ROLE="ACTIVITY_SPACE_ROLE";
	public static final String ACTIVITY_SPACE_ROLES="ACTIVITY_SPACE_ROLES";
	public static final String ACTIVITY_SPACE_ROLEQUEUE="ACTIVITY_SPACE_ROLEQUEUE";
	public static final String ACTIVITY_SPACE_ROLEQUEUES="ACTIVITY_SPACE_ROLEQUEUES";
	public static final String ACTIVITY_SPACE_ROSTER="ACTIVITY_SPACE_ROSTER";
	public static final String ACTIVITY_SPACE_ROSTERS="ACTIVITY_SPACE_ROSTERS";
	public static final String ACTIVITY_SPACE_ADEFINE="ACTIVITY_SPACE_ADEFINE";
	public static final String ACTIVITY_SPACE_ADEFINES="ACTIVITY_SPACE_ADEFINES";	
	
	private ActivityParticipantTree activityParticipantTree;
	private ActivityRoleTree activityRoleTree;
	private ActivityDefinitionTree activityDefinitionTree;
	private ActivityRosterTree activityRosterTree;
	private ActivityRoleQueueTree activityRoleQueueTree;
	private ActivityProcessQueueTree activityProcessQueueTree;
	private ActivitySpace activitySpace;
	
	public ActivitySpaceComponentDetailTrees(UserClientInfo userClientInfo,ActivitySpace activitySpace){
		this.activitySpace=activitySpace;
		userClientInfo.getEventBlackboard().addListener(this);		
		
		activityParticipantTree=new ActivityParticipantTree(getActivityParticipantDataContainer(activitySpace),userClientInfo);
		this.addComponent(activityParticipantTree);		
		
		activityRoleTree=new ActivityRoleTree(getActivityRoleDataContainer(activitySpace),userClientInfo);
		this.addComponent(activityRoleTree);
		
		activityDefinitionTree=new ActivityDefinitionTree(getActivityDefineQueueDataContainer(activitySpace),userClientInfo);
		this.addComponent(activityDefinitionTree);
		
		activityRosterTree=new ActivityRosterTree(getActivityRosterQueueDataContainer(activitySpace),userClientInfo);
		this.addComponent(activityRosterTree);
		
		activityRoleQueueTree=new ActivityRoleQueueTree(getActivityRoleQueueDataContainer(activitySpace),userClientInfo);
		this.addComponent(activityRoleQueueTree);		
		
		activityProcessQueueTree=new ActivityProcessQueueTree(getActivityProcessQueueDataContainer(),userClientInfo);
		this.addComponent(activityProcessQueueTree);
	}
	
	private  HierarchicalContainer getActivityParticipantDataContainer(ActivitySpace activitySpace){		
        HierarchicalContainer activityParticipantTreeContainer = new HierarchicalContainer();     
        activityParticipantTreeContainer.addContainerProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME, String.class, null);        
        activityParticipantTreeContainer.addContainerProperty(ACTIVITY_SPACE_NAME, String.class, null);
        activityParticipantTreeContainer.addContainerProperty(ACTIVITY_SPACE_PARTICIPANT , Participant.class, null); 
        activityParticipantTreeContainer.addContainerProperty(ACTIVITY_SPACE_PARTICIPANTS , Participant[].class, null); 
        try {
			Participant[] participantArray=activitySpace.getParticipants();
			Item rootItem=activityParticipantTreeContainer.addItem(ActivityParticipantTree.participantTreeRootElementId); 
			rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			rootItem.getItemProperty(ACTIVITY_SPACE_PARTICIPANTS).setValue(participantArray);			
		    activityParticipantTreeContainer.setChildrenAllowed(ActivityParticipantTree.participantTreeRootElementId, true);     
		    String baseItemId=activitySpace.getActivitySpaceName()+"_Participant_";
			if(participantArray!=null){				
			    for(int i=0;i<participantArray.length;i++){
			    	Participant Participant=participantArray[i];
			    	String partId=baseItemId+i;
			    	Item currentItem=activityParticipantTreeContainer.addItem(partId);  
			    	currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(Participant.getParticipantName());
			    	currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			    	currentItem.getItemProperty(ACTIVITY_SPACE_PARTICIPANT).setValue(Participant);
			    	activityParticipantTreeContainer.setChildrenAllowed(partId, false);				
			    	activityParticipantTreeContainer.setParent(partId, ActivityParticipantTree.participantTreeRootElementId);		    	
			    }
		    }
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			// add error message dialog
		}                  
		return activityParticipantTreeContainer;
	}
	
	private  HierarchicalContainer getActivityRoleDataContainer(ActivitySpace activitySpace){
		// Create new container
        HierarchicalContainer activityRoleTreeContainer = new HierarchicalContainer();
        // Create containerproperty for name
        activityRoleTreeContainer.addContainerProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME, String.class, null);        
        activityRoleTreeContainer.addContainerProperty(ACTIVITY_SPACE_NAME, String.class, null);
        activityRoleTreeContainer.addContainerProperty(ACTIVITY_SPACE_ROLE , Role.class, null); 
        activityRoleTreeContainer.addContainerProperty(ACTIVITY_SPACE_ROLES , Role[].class, null);        
        try {
			Role[] roleArray=activitySpace.getRoles();			
			Item rootItem=activityRoleTreeContainer.addItem(ActivityRoleTree.roleTreeRootElementId);					
			rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			rootItem.getItemProperty(ACTIVITY_SPACE_ROLES).setValue(roleArray);			
		    activityRoleTreeContainer.setChildrenAllowed(ActivityRoleTree.roleTreeRootElementId, true);			
			String baseItemId=activitySpace.getActivitySpaceName()+"_Role_";
			if(roleArray!=null){
				for(int i=0;i<roleArray.length;i++){
					Role currentRole=roleArray[i];
					String roleItemId=baseItemId+i;	
					Item currentItem=activityRoleTreeContainer.addItem(roleItemId);  				
			    	currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(currentRole.getRoleName());
			    	currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			    	currentItem.getItemProperty(ACTIVITY_SPACE_ROLE).setValue(currentRole);					
			        activityRoleTreeContainer.setChildrenAllowed(roleItemId, false);				
			        activityRoleTreeContainer.setParent(roleItemId, ActivityRoleTree.roleTreeRootElementId);				
				}
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			// add error message dialog
		}
		return activityRoleTreeContainer;
	}
	
	private  HierarchicalContainer getActivityRoleQueueDataContainer(ActivitySpace activitySpace){		
        HierarchicalContainer activityRoleQueueTreeContainer = new HierarchicalContainer();       
        activityRoleQueueTreeContainer.addContainerProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME, String.class, null);        
        activityRoleQueueTreeContainer.addContainerProperty(ACTIVITY_SPACE_NAME, String.class, null);
        activityRoleQueueTreeContainer.addContainerProperty(ACTIVITY_SPACE_ROLEQUEUE , RoleQueue.class, null); 
        activityRoleQueueTreeContainer.addContainerProperty(ACTIVITY_SPACE_ROLEQUEUES , RoleQueue[].class, null);        
        try {
			RoleQueue[] roleQueueArray=activitySpace.getRoleQueues();
			Item rootItem=activityRoleQueueTreeContainer.addItem(ActivityRoleQueueTree.roleQueueTreeRootElementId);   
			rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			rootItem.getItemProperty(ACTIVITY_SPACE_ROLEQUEUES).setValue(roleQueueArray);			
	        activityRoleQueueTreeContainer.setChildrenAllowed(ActivityRoleQueueTree.roleQueueTreeRootElementId, true); 
	        String baseItemId=activitySpace.getActivitySpaceName()+"_RoleQueue_";
			for(int i=0;i<roleQueueArray.length;i++){
				RoleQueue currentRoleQueue=roleQueueArray[i];
				String roleQueueId=baseItemId+i;
				Item currentItem=activityRoleQueueTreeContainer.addItem(roleQueueId);
				currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(currentRoleQueue.getQueueName());
				currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
		    	currentItem.getItemProperty(ACTIVITY_SPACE_ROLEQUEUE).setValue(currentRoleQueue);					
				activityRoleQueueTreeContainer.setChildrenAllowed(roleQueueId, false);				
				activityRoleQueueTreeContainer.setParent(roleQueueId, ActivityRoleQueueTree.roleQueueTreeRootElementId);				
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			// add error message dialog
		}                
		return activityRoleQueueTreeContainer;
	}
	
	private  HierarchicalContainer getActivityRosterQueueDataContainer(ActivitySpace activitySpace){		
        HierarchicalContainer activityRosterTreeContainer = new HierarchicalContainer();     
        activityRosterTreeContainer.addContainerProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME, String.class, null);        
        activityRosterTreeContainer.addContainerProperty(ACTIVITY_SPACE_NAME, String.class, null);
        activityRosterTreeContainer.addContainerProperty(ACTIVITY_SPACE_ROSTER , Roster.class, null); 
        activityRosterTreeContainer.addContainerProperty(ACTIVITY_SPACE_ROSTERS , Roster[].class, null);         
        try {
			Roster[] rosterArray=activitySpace.getRosters();
			Item rootItem=activityRosterTreeContainer.addItem(ActivityRosterTree.rosterTreeRootElementId);
			rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			rootItem.getItemProperty(ACTIVITY_SPACE_ROSTERS).setValue(rosterArray);	
	        activityRosterTreeContainer.setChildrenAllowed(ActivityRosterTree.rosterTreeRootElementId, true);  
	        String baseItemId=activitySpace.getActivitySpaceName()+"_Roster_";	
	        if(rosterArray!=null){
	        	for(int i=0;i<rosterArray.length;i++){
					Roster currentRoster=rosterArray[i];
					String rosterId=baseItemId+i;
					Item currentItem=activityRosterTreeContainer.addItem(rosterId);
					currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(currentRoster.getRosterName());
					currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			    	currentItem.getItemProperty(ACTIVITY_SPACE_ROSTER).setValue(currentRoster);				
					activityRosterTreeContainer.setChildrenAllowed(rosterId, false);				
					activityRosterTreeContainer.setParent(rosterId, ActivityRosterTree.rosterTreeRootElementId);			
				}
	        }			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			// add error message dialog
		}        
		return activityRosterTreeContainer;
	}
	
	private  HierarchicalContainer getActivityDefineQueueDataContainer(ActivitySpace activitySpace){		
        HierarchicalContainer activityDefineTreeContainer = new HierarchicalContainer();     
        activityDefineTreeContainer.addContainerProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME, String.class, null);        
        activityDefineTreeContainer.addContainerProperty(ACTIVITY_SPACE_NAME, String.class, null);
        activityDefineTreeContainer.addContainerProperty(ACTIVITY_SPACE_ADEFINE, BusinessActivityDefinition.class, null); 
        activityDefineTreeContainer.addContainerProperty(ACTIVITY_SPACE_ADEFINES, BusinessActivityDefinition[].class, null);            
        try {
			BusinessActivityDefinition[] businessActivityDefinitionArray=activitySpace.getBusinessActivityDefinitions();
			Item rootItem=activityDefineTreeContainer.addItem(ActivityDefinitionTree.defineTreeRootElementId); 
			rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
			rootItem.getItemProperty(ACTIVITY_SPACE_ADEFINES).setValue(businessActivityDefinitionArray);	
		    activityDefineTreeContainer.setChildrenAllowed(ActivityDefinitionTree.defineTreeRootElementId, true); 
		    String baseItemId=activitySpace.getActivitySpaceName()+"_ActivityDefinition_";
			for(int i=0;i<businessActivityDefinitionArray.length;i++){
				BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[i];
				String bdfId=baseItemId+i;
				Item currentItem=activityDefineTreeContainer.addItem(bdfId);
				currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(businessActivityDefinition.getActivityType());
				currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
		    	currentItem.getItemProperty(ACTIVITY_SPACE_ADEFINE).setValue(businessActivityDefinition);
				activityDefineTreeContainer.setChildrenAllowed(bdfId, false);				
				activityDefineTreeContainer.setParent(bdfId, ActivityDefinitionTree.defineTreeRootElementId);				
			}		
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();			
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();			
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();			
		}       
		return activityDefineTreeContainer;
	}
	
	private  HierarchicalContainer getActivityProcessQueueDataContainer(){		
        HierarchicalContainer activityProcessQueueTreeContainer = new HierarchicalContainer();     
        activityProcessQueueTreeContainer.addContainerProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME, String.class, null);        
        //activityRosterTreeContainer.addContainerProperty("ACTIVITY_SPACE_NAME", String.class, null);         
        activityProcessQueueTreeContainer.addItem(ActivityProcessQueueTree.processQueueTreeRootElementId);      
        activityProcessQueueTreeContainer.setChildrenAllowed(ActivityProcessQueueTree.processQueueTreeRootElementId, true);        
		return activityProcessQueueTreeContainer;
	}
	
	private ActivityObjectDetail getActivityObjectDetail(){
		ActivityManagementPanel activityManagementPanel=((ActivityObjectBrowser)
				(this.getParent().getParent().getParent().getParent())).getActivityManagementPanel();		
		return activityManagementPanel.activityObjectDetail;
	}
	
	public void renderParticipantsUI(Participant[] participantArray,String activitySpaceName){
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderParticipantsUI(participantArray,activitySpaceName);
	}	
	public void renderParticipantUI(Participant participant){
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderParticipantUI(participant);
	}	
	public void renderRolesUI(Role[] roleArray,String activitySpaceName){
		activityParticipantTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderRolesUI(roleArray,activitySpaceName);		
	}	
	public void renderRoleUI(Role currentRole){
		activityParticipantTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderRoleUI(currentRole);		
	}	
	public void renderBusinessActivityDefinitionsUI(BusinessActivityDefinition[] activityDefinitionArray,String activitySpaceName){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderBusinessActivityDefinitionsUI(activityDefinitionArray,activitySpaceName);
	}
	public void renderBusinessActivityDefinitionUI(BusinessActivityDefinition currentBusinessActivityDefinition){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderBusinessActivityDefinitionUI(currentBusinessActivityDefinition);
	}	
	public void renderRostersUI(Roster[] rosterArray,String activitySpaceName){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderRostersUI(rosterArray,activitySpaceName);
	}
	public void renderRosterUI(Roster currentRoster){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRoleQueueTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderRosterUI(currentRoster);
	}	
	public void renderRoleQueuesUI(RoleQueue[] roleQueueArray,String activitySpaceName){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderRoleQueuesUI(roleQueueArray,activitySpaceName);
	}
	public void renderRoleQueueUI(RoleQueue currentRoleQueue){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityProcessQueueTree.select(null);
		getActivityObjectDetail().renderRoleQueueUI(currentRoleQueue);
	}	
	public void renderProcessQueuesUI(ProcessQueue[] processQueueArray){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		getActivityObjectDetail().renderProcessQueuesUI(processQueueArray);
	}
	public void renderProcessQueueUI(ProcessQueue currentProcessQueue){
		activityParticipantTree.select(null);
		activityRoleTree.select(null);
		activityDefinitionTree.select(null);
		activityRosterTree.select(null);
		activityRoleQueueTree.select(null);
		getActivityObjectDetail().renderProcessQueueUI(currentProcessQueue);
	}

	public void receiveParticipantsChange(ParticipantsChangeEvent event) {
		if(this.activitySpace.getActivitySpaceName().equals(event.getActivitySpaceName())){			
			HierarchicalContainer activityParticipantContainer=(HierarchicalContainer)activityParticipantTree.getContainerDataSource();
			activityParticipantContainer.removeAllItems();			
			try {
				Participant[] participantArray=activitySpace.getParticipants();
				if(participantArray!=null){
					Item rootItem=activityParticipantContainer.addItem(ActivityParticipantTree.participantTreeRootElementId); 
					rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
					rootItem.getItemProperty(ACTIVITY_SPACE_PARTICIPANTS).setValue(participantArray);			
					activityParticipantContainer.setChildrenAllowed(ActivityParticipantTree.participantTreeRootElementId, true);     
				    String baseItemId=activitySpace.getActivitySpaceName()+"_Participant_";
				    for(int i=0;i<participantArray.length;i++){
				    	Participant Participant=participantArray[i];
				    	String partId=baseItemId+i;
				    	Item currentItem=activityParticipantContainer.addItem(partId);  
				    	currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(Participant.getParticipantName());
				    	currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				    	currentItem.getItemProperty(ACTIVITY_SPACE_PARTICIPANT).setValue(Participant);
				    	activityParticipantContainer.setChildrenAllowed(partId, false);				
				    	activityParticipantContainer.setParent(partId, ActivityParticipantTree.participantTreeRootElementId);		    	
				    }
				   }
			} catch (ActivityEngineRuntimeException e) {			
				e.printStackTrace();
				// add error message dialog
			}          
			activityParticipantTree.formatTree();			
		}		
	}

	public void receiveRolesChange(RolesChangeEvent event) {
		if(this.activitySpace.getActivitySpaceName().equals(event.getActivitySpaceName())){	
			HierarchicalContainer activityRoleTreeContainer =(HierarchicalContainer)activityRoleTree.getContainerDataSource();
			activityRoleTreeContainer.removeAllItems();     
		    try {
				Role[] roleArray=activitySpace.getRoles();			
				Item rootItem=activityRoleTreeContainer.addItem(ActivityRoleTree.roleTreeRootElementId);					
				rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				rootItem.getItemProperty(ACTIVITY_SPACE_ROLES).setValue(roleArray);			
			    activityRoleTreeContainer.setChildrenAllowed(ActivityRoleTree.roleTreeRootElementId, true);			
				String baseItemId=activitySpace.getActivitySpaceName()+"_Role_";
				if(roleArray!=null){
					for(int i=0;i<roleArray.length;i++){
						Role currentRole=roleArray[i];
						String roleItemId=baseItemId+i;	
						Item currentItem=activityRoleTreeContainer.addItem(roleItemId);  				
				    	currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(currentRole.getRoleName());
				    	currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				    	currentItem.getItemProperty(ACTIVITY_SPACE_ROLE).setValue(currentRole);					
				        activityRoleTreeContainer.setChildrenAllowed(roleItemId, false);				
				        activityRoleTreeContainer.setParent(roleItemId, ActivityRoleTree.roleTreeRootElementId);				
						}
					}			
			} catch (ActivityEngineRuntimeException e) {			
				e.printStackTrace();
				// add error message dialog
			}
		    activityRoleTree.formatTree();
		}		
	}

	public void receiveRoleQueuesChange(RoleQueuesChangeEvent event) {		
		if(this.activitySpace.getActivitySpaceName().equals(event.getActivitySpaceName())){	
			HierarchicalContainer activityRoleQueueTreeContainer =(HierarchicalContainer)activityRoleQueueTree.getContainerDataSource();
			activityRoleQueueTreeContainer.removeAllItems();  		
			try {
				RoleQueue[] roleQueueArray=activitySpace.getRoleQueues();
				Item rootItem=activityRoleQueueTreeContainer.addItem(ActivityRoleQueueTree.roleQueueTreeRootElementId);   
				rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				rootItem.getItemProperty(ACTIVITY_SPACE_ROLEQUEUES).setValue(roleQueueArray);			
			    activityRoleQueueTreeContainer.setChildrenAllowed(ActivityRoleQueueTree.roleQueueTreeRootElementId, true); 
			    String baseItemId=activitySpace.getActivitySpaceName()+"_RoleQueue_";
			    if(roleQueueArray!=null){
			    	for(int i=0;i<roleQueueArray.length;i++){
						RoleQueue currentRoleQueue=roleQueueArray[i];
						String roleQueueId=baseItemId+i;
						Item currentItem=activityRoleQueueTreeContainer.addItem(roleQueueId);
						currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(currentRoleQueue.getQueueName());
						currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
					   	currentItem.getItemProperty(ACTIVITY_SPACE_ROLEQUEUE).setValue(currentRoleQueue);					
						activityRoleQueueTreeContainer.setChildrenAllowed(roleQueueId, false);				
						activityRoleQueueTreeContainer.setParent(roleQueueId, ActivityRoleQueueTree.roleQueueTreeRootElementId);				
					}			    	
			    }				
			} catch (ActivityEngineRuntimeException e) {			
				e.printStackTrace();
				// add error message dialog
			}                
			activityRoleQueueTree.formatTree();			
		}
	}

	public void receiveRostersChange(RostersChangeEvent event) {		
		if(this.activitySpace.getActivitySpaceName().equals(event.getActivitySpaceName())){	
			HierarchicalContainer activityRosterTreeContainer =(HierarchicalContainer)activityRosterTree.getContainerDataSource();
			activityRosterTreeContainer.removeAllItems();  
			try {
				Roster[] rosterArray=activitySpace.getRosters();
				Item rootItem=activityRosterTreeContainer.addItem(ActivityRosterTree.rosterTreeRootElementId);
				rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				rootItem.getItemProperty(ACTIVITY_SPACE_ROSTERS).setValue(rosterArray);	
		        activityRosterTreeContainer.setChildrenAllowed(ActivityRosterTree.rosterTreeRootElementId, true);  
		        String baseItemId=activitySpace.getActivitySpaceName()+"_Roster_";	
		        if(rosterArray!=null){
		        	for(int i=0;i<rosterArray.length;i++){
						Roster currentRoster=rosterArray[i];
						String rosterId=baseItemId+i;
						Item currentItem=activityRosterTreeContainer.addItem(rosterId);
						currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(currentRoster.getRosterName());
						currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				    	currentItem.getItemProperty(ACTIVITY_SPACE_ROSTER).setValue(currentRoster);				
						activityRosterTreeContainer.setChildrenAllowed(rosterId, false);				
						activityRosterTreeContainer.setParent(rosterId, ActivityRosterTree.rosterTreeRootElementId);			
					}
		        }			
			} catch (ActivityEngineRuntimeException e) {			
				e.printStackTrace();
				// add error message dialog
			}        
			activityRosterTree.formatTree();			
		}
	}

	public void receiveActivityDefinitionsChange(ActivityDefinitionsChangeEvent event) {
		if(this.activitySpace.getActivitySpaceName().equals(event.getActivitySpaceName())){				
			HierarchicalContainer activityDefineTreeContainer =(HierarchicalContainer)activityDefinitionTree.getContainerDataSource(); 
			activityDefineTreeContainer.removeAllItems(); 
			try {
				BusinessActivityDefinition[] businessActivityDefinitionArray=activitySpace.getBusinessActivityDefinitions();
				Item rootItem=activityDefineTreeContainer.addItem(ActivityDefinitionTree.defineTreeRootElementId); 
				rootItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				rootItem.getItemProperty(ACTIVITY_SPACE_ADEFINES).setValue(businessActivityDefinitionArray);	
			    activityDefineTreeContainer.setChildrenAllowed(ActivityDefinitionTree.defineTreeRootElementId, true); 
			    String baseItemId=activitySpace.getActivitySpaceName()+"_ActivityDefinition_";
			    if(businessActivityDefinitionArray!=null){
			    	for(int i=0;i<businessActivityDefinitionArray.length;i++){
						BusinessActivityDefinition businessActivityDefinition=businessActivityDefinitionArray[i];
						String bdfId=baseItemId+i;
						Item currentItem=activityDefineTreeContainer.addItem(bdfId);
						currentItem.getItemProperty(ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(businessActivityDefinition.getActivityType());
						currentItem.getItemProperty(ACTIVITY_SPACE_NAME).setValue(activitySpace.getActivitySpaceName());
				    	currentItem.getItemProperty(ACTIVITY_SPACE_ADEFINE).setValue(businessActivityDefinition);
						activityDefineTreeContainer.setChildrenAllowed(bdfId, false);				
						activityDefineTreeContainer.setParent(bdfId, ActivityDefinitionTree.defineTreeRootElementId);				
					}				    	
			    }				
			} catch (ActivityEngineRuntimeException e) {			
				e.printStackTrace();			
			} catch (ActivityEngineActivityException e) {			
				e.printStackTrace();			
			} catch (ActivityEngineDataException e) {			
				e.printStackTrace();			
			}
			activityDefinitionTree.formatTree();
		}		
	}
}