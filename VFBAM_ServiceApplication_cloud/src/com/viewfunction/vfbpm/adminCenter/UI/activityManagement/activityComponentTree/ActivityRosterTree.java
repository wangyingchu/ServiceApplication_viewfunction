package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivitySpaceComponentDetailTrees;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityRosterTree extends Tree{
	private static final long serialVersionUID = -3160263475963807288L;
	private HierarchicalContainer activitySpace_RosterDataContainer;
	private UserClientInfo userClientInfo;
	public static final String rosterTreeRootElementId="ACTIVITYSPACE_ROSTERTREE_ROOT";
	
	public ActivityRosterTree(HierarchicalContainer activitySpaceDataContainer,UserClientInfo userClientInfo){		
		this.activitySpace_RosterDataContainer=activitySpaceDataContainer;	
		this.userClientInfo=userClientInfo;
		this.setContainerDataSource(this.activitySpace_RosterDataContainer);
	    this.setItemCaptionPropertyId(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME);
		this.setItemIcon(rosterTreeRootElementId, UICommonElementDefination.AppPanel_activitySpaceRosterRootIcon);
		 formatTree();		
		// Cause valueChange immediately when the user selects
		this.setImmediate(true);
		this.addListener(new RosterTreeOnClickListener(this));
	}	
	
	public void handleTreeItemClick(Object itemId){
		Item clickedItem=activitySpace_RosterDataContainer.getItem(itemId);
		//String rosterName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).getValue().toString();		
		String activitySpaceName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_NAME).getValue().toString();		
		if(itemId.toString().equals(rosterTreeRootElementId)){
			Roster[] rosterArray=(Roster[])clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ROSTERS).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderRostersUI(rosterArray,activitySpaceName);
		}else{
			Roster currentRoster=(Roster)clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ROSTER).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderRosterUI(currentRoster);
		}	
	}
	public void formatTree(){
		Collection<?> dataItemCollection=this.activitySpace_RosterDataContainer.getChildren(rosterTreeRootElementId);
		if(dataItemCollection!=null){
			Iterator<?> childIter=dataItemCollection.iterator();
			while(childIter.hasNext()){			
				this.setItemIcon(childIter.next(), UICommonElementDefination.AppTree_subItemIcon);			
			}		
			//Add name property for content space item
			int childElementNum=dataItemCollection.size();
			this.activitySpace_RosterDataContainer.getItem(rosterTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_rosterLabel")+
					" ("+childElementNum+") :");
		}else{
			this.activitySpace_RosterDataContainer.getItem(rosterTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_rosterLabel")+
					" (0) :");
		}
	}
}