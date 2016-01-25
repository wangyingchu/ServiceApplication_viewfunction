package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivitySpaceComponentDetailTrees;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityParticipantTree extends Tree {
	private static final long serialVersionUID = 6000314552487262766L;
	private HierarchicalContainer activitySpace_ParticipantDataContainer;	
	public static final String participantTreeRootElementId="ACTIVITYSPACE_PARTICIPANTTREE_ROOT";
	private UserClientInfo userClientInfo;
	
	public ActivityParticipantTree(HierarchicalContainer activitySpaceDataContainer,UserClientInfo userClientInfo){		
		this.activitySpace_ParticipantDataContainer=activitySpaceDataContainer;		
		this.userClientInfo=userClientInfo;
		this.setContainerDataSource(this.activitySpace_ParticipantDataContainer);
	    this.setItemCaptionPropertyId(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME);
		this.setItemIcon(participantTreeRootElementId, UICommonElementDefination.AppPanel_activitySpaceParticipantRootIcon);		
		formatTree();
		// Cause valueChange immediately when the user selects
		this.setImmediate(true);	
		this.addListener(new ParticipantTreeOnClickListener(this));
	}	
	
	public void handleTreeItemClick(Object itemId){		
		Item clickedItem=activitySpace_ParticipantDataContainer.getItem(itemId);
		//String participantName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).getValue().toString();		
		String activitySpaceName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_NAME).getValue().toString();		
		if(itemId.toString().equals(participantTreeRootElementId)){
			Participant[] participantArray=(Participant[])clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_PARTICIPANTS).getValue();				
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderParticipantsUI(participantArray,activitySpaceName);		
		}else{
			Participant	currentParticipant=(Participant)clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_PARTICIPANT).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderParticipantUI(currentParticipant);
		}
	}
	
	public void formatTree(){
		Collection<?> dataItemCollection=this.activitySpace_ParticipantDataContainer.getChildren(participantTreeRootElementId);
		if(dataItemCollection!=null){
			Iterator<?> childIter=dataItemCollection.iterator();
			while(childIter.hasNext()){			
				this.setItemIcon(childIter.next(), UICommonElementDefination.AppTree_subItemIcon);			
			}		
			//Add name property for content space item
			int childElementNum=dataItemCollection.size();
			this.activitySpace_ParticipantDataContainer.getItem(participantTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_participantLabel")+
					" ("+childElementNum+") :");
		}else{
			this.activitySpace_ParticipantDataContainer.getItem(participantTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_participantLabel")+
					" (0) :");
		}
	}
}