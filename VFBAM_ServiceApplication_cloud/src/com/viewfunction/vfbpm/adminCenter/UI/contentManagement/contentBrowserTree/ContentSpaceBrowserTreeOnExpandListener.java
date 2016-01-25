/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.contentBrowserTree;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;
import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.ContentOperationHelper;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentSpaceDataProvider;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wangychu
 */
public class ContentSpaceBrowserTreeOnExpandListener implements Tree.ExpandListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3212585192342828673L;
	private ContentSpaceBrowserTree contentSpaceBrowserTree;
    private HierarchicalContainer contentSpace_DataContainer;

    public ContentSpaceBrowserTreeOnExpandListener(ContentSpaceBrowserTree tree, HierarchicalContainer dataContainer) {
        this.contentSpaceBrowserTree = tree;
        this.contentSpace_DataContainer = dataContainer;
    }

    public void nodeExpand(ExpandEvent event) {
        Object currentParentTreeNodeID = event.getItemId();
        Item currentParentDataItem = contentSpace_DataContainer.getItem(currentParentTreeNodeID);
        ContentOperationHelper coh = ContentComponentFactory.getContentOperationHelper();
        String currentContentType;
        if (currentParentTreeNodeID.toString().startsWith(ContentSpaceDataProvider.contentSpaceIdPerfix)) {
            String ContentSpaceName = currentParentDataItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).toString();
            String contentSpaceIDString = currentParentTreeNodeID.toString();
            Object currentContentSpaceIDNumber = contentSpaceIDString.substring(ContentSpaceDataProvider.contentSpaceIdPerfix.length(), contentSpaceIDString.length());
            ContentSpace cs = null;
            try {
                cs = ContentComponentFactory.connectContentSpace(ContentSpaceName);
                List<RootContentObject> rootContentObjectList = cs.getRootContentObjects();
                RootContentObject currentRootContentObject;
                Item currentRootContentObjectItem;
                for (int j = 0; j < rootContentObjectList.size(); j++) {
                    currentRootContentObject = rootContentObjectList.get(j);
                    String currentRootContentObjectItemId = ContentSpaceDataProvider.rootContentObjectIdPerfix + currentContentSpaceIDNumber + j;
                    // Add new root content object item
                    currentRootContentObjectItem = contentSpace_DataContainer.addItem(currentRootContentObjectItemId);
                    currentRootContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(currentRootContentObject.getRootContentObjectID());
                    currentRootContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(ContentSpaceName);
                    contentSpaceBrowserTree.setItemIcon(currentRootContentObjectItemId, UICommonElementDefination.AppTree_nodeItemIcon);
                    // setParetn node to current content space item
                    contentSpace_DataContainer.setParent(currentRootContentObjectItemId, currentParentTreeNodeID);
                    currentContentType = coh.getContentObjectType(currentRootContentObject);
                    if (currentContentType.equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)) {
                        contentSpace_DataContainer.setChildrenAllowed(currentRootContentObjectItemId, true);
                        contentSpaceBrowserTree.collapseItemsRecursively(currentRootContentObjectItemId);
                    } else {
                        contentSpace_DataContainer.setChildrenAllowed(currentRootContentObjectItemId, false);
                    }
                }
            } catch (ContentReposityException ex) {
                Logger.getLogger(ContentSpaceBrowserTreeOnExpandListener.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (cs != null) {
                    cs.closeContentSpace();
                }
            }
            return;
        }

        String parentContentSpaceName = currentParentDataItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).toString();
        String currentParentContentObjectABSPath = this.contentSpaceBrowserTree.getContentObjectABSPathByItemId(currentParentTreeNodeID);
        ContentSpace cs = null;
        try {
            cs = ContentComponentFactory.connectContentSpace(parentContentSpaceName);
            BaseContentObject parentContentObject = cs.getContentObjectByAbsPath(currentParentContentObjectABSPath);
            List<BaseContentObject> childrenContentObjectList = parentContentObject.getSubContentObjects(null);
            BaseContentObject currentContentObject;
            String currentContentObjectName;

            String subContentObjectID;
            for (int i = 0; i < childrenContentObjectList.size(); i++) {
                currentContentObject = childrenContentObjectList.get(i);
                currentContentObjectName = currentContentObject.getContentObjectName();
                currentContentType = coh.getContentObjectType(currentContentObject);
                // Add new sub content object item
                subContentObjectID = currentParentTreeNodeID + ContentSpaceDataProvider.subContentObjectIdPerfix + i;
                Item subContentObjectItem = contentSpace_DataContainer.addItem(subContentObjectID);
                if(subContentObjectItem!=null){
                	subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).setValue(currentContentObjectName);
                	subContentObjectItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).setValue(parentContentSpaceName);
                }        
                contentSpaceBrowserTree.setItemIcon(subContentObjectID, UICommonElementDefination.AppTree_nodeItemIcon);    
                // setParetn node to current content space item
                contentSpace_DataContainer.setParent(subContentObjectID, currentParentTreeNodeID);
                if (currentContentType.equals(ContentOperationHelper.CONTENTTYPE_FOLDEROBJECT)) {
                    contentSpace_DataContainer.setChildrenAllowed(subContentObjectID, true);
                    contentSpaceBrowserTree.collapseItemsRecursively(subContentObjectID);
                } else {
                    contentSpace_DataContainer.setChildrenAllowed(subContentObjectID, false);
                }
            }
        } catch (ContentReposityException ex) {
            Logger.getLogger(ContentSpaceBrowserTreeOnExpandListener.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (cs != null) {
                cs.closeContentSpace();
            }
        }
    }
}
