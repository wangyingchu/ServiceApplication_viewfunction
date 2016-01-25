package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivitySpaceComponentDetailTrees;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityRoleTree extends Tree{
	private static final long serialVersionUID = 8256277220807899239L;

	private HierarchicalContainer activitySpace_RoleDataContainer;	
	public static final String roleTreeRootElementId="ACTIVITYSPACE_ROLETREE_ROOT";
	private UserClientInfo userClientInfo;
	
	public ActivityRoleTree(HierarchicalContainer activitySpace_RoleDataContainer,UserClientInfo userClientInfo){		
		this.activitySpace_RoleDataContainer=activitySpace_RoleDataContainer;	
		this.userClientInfo=userClientInfo;
		this.setContainerDataSource(this.activitySpace_RoleDataContainer);		
	    this.setItemCaptionPropertyId(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME);
		this.setItemIcon(roleTreeRootElementId, UICommonElementDefination.AppPanel_activitySpaceRoleRootIcon);	
		formatTree();		
		// Cause valueChange immediately when the user selects
		this.setImmediate(true);
		this.addListener(new RoleTreeOnClickListener(this));
	}
	
	public void handleTreeItemClick(Object itemId){
		Item clickedItem=activitySpace_RoleDataContainer.getItem(itemId);
		//String roleName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).getValue().toString();		
		String activitySpaceName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_NAME).getValue().toString();		
		if(itemId.toString().equals(roleTreeRootElementId)){
			Role[] roleArray=(Role[])clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ROLES).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderRolesUI(roleArray,activitySpaceName);
		}else{
			Role currentRole=(Role)clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ROLE).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderRoleUI(currentRole);
		}	
	}
	
	public void formatTree(){
		Collection<?> dataItemCollection=this.activitySpace_RoleDataContainer.getChildren(roleTreeRootElementId);
		if(dataItemCollection!=null){
			Iterator<?> childIter=dataItemCollection.iterator();
			while(childIter.hasNext()){			
				this.setItemIcon(childIter.next(), UICommonElementDefination.AppTree_subItemIcon);			
			}		
			//Add name property for content space item
			int childElementNum=dataItemCollection.size();
			this.activitySpace_RoleDataContainer.getItem(roleTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_roleLabel")+
					" ("+childElementNum+") :");	
		}else{
			this.activitySpace_RoleDataContainer.getItem(roleTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_roleLabel")+
					" (0) :");	
		}
	}
}