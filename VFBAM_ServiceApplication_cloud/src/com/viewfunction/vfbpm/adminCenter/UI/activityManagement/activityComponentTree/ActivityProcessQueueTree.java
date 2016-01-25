package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.activityEngine.activityView.ProcessQueue;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivitySpaceComponentDetailTrees;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityProcessQueueTree extends Tree{
	private static final long serialVersionUID = 2420650414779755346L;
	private HierarchicalContainer activitySpace_ProcessQueueDataContainer;
	
	public static final String processQueueTreeRootElementId="ACTIVITYSPACE_PROCESSQUEUETREE_ROOT";
	
	public ActivityProcessQueueTree(HierarchicalContainer activitySpaceDataContainer,UserClientInfo userClientInfo){		
		this.activitySpace_ProcessQueueDataContainer=activitySpaceDataContainer;		
		this.setContainerDataSource(this.activitySpace_ProcessQueueDataContainer);
	    this.setItemCaptionPropertyId(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME);
		this.setItemIcon(processQueueTreeRootElementId, UICommonElementDefination.AppPanel_activitySpaceProcessQueueRootIcon);
		
		Collection<?> dataItemCollection=this.activitySpace_ProcessQueueDataContainer.getChildren(processQueueTreeRootElementId);			
		if(dataItemCollection!=null){
			Iterator<?> childIter=dataItemCollection.iterator();
			while(childIter.hasNext()){			
				this.setItemIcon(childIter.next(), UICommonElementDefination.AppTree_subItemIcon);			
			}		
			//Add name property for content space item
			int childElementNum=dataItemCollection.size();
			this.activitySpace_ProcessQueueDataContainer.getItem(processQueueTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_processQueueLabel")+
					" ("+childElementNum+") :");	
		}else{
			this.activitySpace_ProcessQueueDataContainer.getItem(processQueueTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_processQueueLabel")+
					" (0) :");			
		}		
		// Cause valueChange immediately when the user selects
		this.setImmediate(true);
		this.addListener(new ProcessQueueTreeOnClickListener(this));
	}	
	
	public void handleTreeItemClick(Object itemId){
		Item clickedItem=activitySpace_ProcessQueueDataContainer.getItem(itemId);
		//String processQueueName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).getValue().toString();		
		//String activitySpaceName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_NAME).getValue().toString();		
		if(itemId.toString().equals(processQueueTreeRootElementId)){
			ProcessQueue[] processQueueArray=(ProcessQueue[])clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ADEFINES).getValue();			
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderProcessQueuesUI(processQueueArray);
		}else{
			ProcessQueue currentProcessQueue=(ProcessQueue)clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ADEFINE).getValue();		
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderProcessQueueUI(currentProcessQueue);
		}	
	}
}
