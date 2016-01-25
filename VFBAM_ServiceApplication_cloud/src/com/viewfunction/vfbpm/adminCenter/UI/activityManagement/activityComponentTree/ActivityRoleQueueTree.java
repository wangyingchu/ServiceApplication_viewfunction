package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivitySpaceComponentDetailTrees;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityRoleQueueTree extends Tree{
	private static final long serialVersionUID = 3928906033040508773L;
	private HierarchicalContainer activitySpace_RoleQueueDataContainer;
	private UserClientInfo userClientInfo;
	
	public static final String roleQueueTreeRootElementId="ACTIVITYSPACE_ROLEQUEUETREE_ROOT";
	
	public ActivityRoleQueueTree(HierarchicalContainer activitySpaceDataContainer,UserClientInfo userClientInfo){		
		this.activitySpace_RoleQueueDataContainer=activitySpaceDataContainer;
		this.userClientInfo=userClientInfo;
		this.setContainerDataSource(this.activitySpace_RoleQueueDataContainer);
	    this.setItemCaptionPropertyId(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME);
		this.setItemIcon(roleQueueTreeRootElementId, UICommonElementDefination.AppPanel_activitySpaceRoleQueueRootIcon);
		formatTree();		
		// Cause valueChange immediately when the user selects
		this.setImmediate(true);
		this.addListener(new RoleQueueTreeOnClickListener(this));
	}	
	
	public void handleTreeItemClick(Object itemId){
		Item clickedItem=activitySpace_RoleQueueDataContainer.getItem(itemId);
		//String roleQueueName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).getValue().toString();		
		String activitySpaceName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_NAME).getValue().toString();		
		if(itemId.toString().equals(roleQueueTreeRootElementId)){
			RoleQueue[] roleQueueArray=(RoleQueue[])clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ROLEQUEUES).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderRoleQueuesUI(roleQueueArray,activitySpaceName);
		}else{
			RoleQueue currentRoleQueue=(RoleQueue)clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ROLEQUEUE).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderRoleQueueUI(currentRoleQueue);
		}	
	}
	
	public void formatTree(){
		Collection<?> dataItemCollection=this.activitySpace_RoleQueueDataContainer.getChildren(roleQueueTreeRootElementId);
		if(dataItemCollection!=null){
			Iterator<?> childIter=dataItemCollection.iterator();
			while(childIter.hasNext()){			
				this.setItemIcon(childIter.next(), UICommonElementDefination.AppTree_subItemIcon);			
			}		
			//Add name property for content space item
			int childElementNum=dataItemCollection.size();
			this.activitySpace_RoleQueueDataContainer.getItem(roleQueueTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_roleQueueLabel")+
					" ("+childElementNum+") :");
		}else{
			this.activitySpace_RoleQueueDataContainer.getItem(roleQueueTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_roleQueueLabel")+
					" (0) :");			
		}		
	}
}