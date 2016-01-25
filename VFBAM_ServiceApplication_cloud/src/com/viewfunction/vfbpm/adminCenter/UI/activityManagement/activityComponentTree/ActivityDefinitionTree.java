package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityComponentTree;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivitySpaceComponentDetailTrees;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ActivityDefinitionTree extends Tree{	
	private static final long serialVersionUID = 667647717998474704L;

	private HierarchicalContainer activitySpace_ActivityDefineDataContainer;
	private UserClientInfo userClientInfo;
	public static final String defineTreeRootElementId="ACTIVITYSPACE_ACTIVITYDEFINETREE_ROOT";
	
	public ActivityDefinitionTree(HierarchicalContainer activitySpaceDataContainer,UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		this.activitySpace_ActivityDefineDataContainer=activitySpaceDataContainer;		
		this.setContainerDataSource(this.activitySpace_ActivityDefineDataContainer);
	    this.setItemCaptionPropertyId(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME);
		this.setItemIcon(defineTreeRootElementId, UICommonElementDefination.AppPanel_activitySpaceActivityDefineRootIcon);	
		
		formatTree();
		// Cause valueChange immediately when the user selects
		this.setImmediate(true);
		this.addListener(new DefinitionTreeOnClickListener(this));
	}
	
	public void handleTreeItemClick(Object itemId){	
		Item clickedItem=activitySpace_ActivityDefineDataContainer.getItem(itemId);
		//String defineName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).getValue().toString();		
		String activitySpaceName=clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_NAME).getValue().toString();		
		if(itemId.toString().equals(defineTreeRootElementId)){
			BusinessActivityDefinition[] activityDefinitionArray=(BusinessActivityDefinition[])clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ADEFINES).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderBusinessActivityDefinitionsUI(activityDefinitionArray,activitySpaceName);
		}else{
			BusinessActivityDefinition currentBusinessActivityDefinition=(BusinessActivityDefinition)clickedItem.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ADEFINE).getValue();
			((ActivitySpaceComponentDetailTrees)this.getParent()).renderBusinessActivityDefinitionUI(currentBusinessActivityDefinition);
		}	
	}
	
	public void formatTree(){
		Collection<?> dataItemCollection=this.activitySpace_ActivityDefineDataContainer.getChildren(defineTreeRootElementId);
		if(dataItemCollection!=null){
			Iterator<?> childIter=dataItemCollection.iterator();
			while(childIter.hasNext()){			
				this.setItemIcon(childIter.next(), UICommonElementDefination.AppTree_subItemIcon);			
			}		
			//Add name property for content space item
			int childElementNum=dataItemCollection.size();
			this.activitySpace_ActivityDefineDataContainer.getItem(defineTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_definitionLabel")+
					" ("+childElementNum+") :");			
		}else{
			this.activitySpace_ActivityDefineDataContainer.getItem(defineTreeRootElementId)
			.getItemProperty(ActivitySpaceComponentDetailTrees.ACTIVITY_SPACE_ELEMENT_TREEITEM_NAME).setValue(
					userClientInfo.getI18NProperties().getProperty("ActivityManage_object_definitionLabel")+
					" (0) :");			
		}	
	}
}
