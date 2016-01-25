/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.contentBrowserTree;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.contentManagement.ContentObjectBrowser;
import com.viewfunction.vfbpm.adminCenter.util.contentRepository.ContentSpaceDataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author wangychu
 */
public class ContentSpaceBrowserTree extends Tree {
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -6466906926885412730L;
	
	public HierarchicalContainer contentSpace_DataContainer;
	public ContentObjectBrowser contentObjectBrowser;

    public ContentSpaceBrowserTree(HierarchicalContainer contentData,ContentObjectBrowser contentObjectBrowser) {
        this.contentSpace_DataContainer = contentData;
        this.contentObjectBrowser=contentObjectBrowser;
        
        if (this.contentSpace_DataContainer == null) {
        	this.contentSpace_DataContainer = ContentSpaceDataProvider.getRegisteredContentSpace_DataContainer();
        }
        this.setContainerDataSource(this.contentSpace_DataContainer);
        this.setItemCaptionPropertyId(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME);
        for (Iterator<?> it = this.rootItemIds().iterator(); it.hasNext();) {        	
        	Object itemId=it.next();
        	this.setItemIcon(itemId, UICommonElementDefination.AppTree_rootItemIcon);	
            this.expandItem(itemId); 
            Collection<?> dataItemCollection=this.contentSpace_DataContainer.getChildren(itemId);			
    		if(dataItemCollection!=null){
    			Iterator<?> childIter=dataItemCollection.iterator();
    			while(childIter.hasNext()){			
    				this.setItemIcon(childIter.next(), UICommonElementDefination.AppTree_nodeItemIcon);			
    			}		
    		}            
        }
        this.setStyleName("complexLayout_leftPanel_Element");
        //this.addListener(new ContentSpaceBrowserTreeItemClickListener());
        this.addListener(new ContentSpaceBrowserTreeOnClickListener(this));
        this.addListener(new ContentSpaceBrowserTreeOnExpandListener(this, this.contentSpace_DataContainer));
        this.addListener(new ContentSpaceBrowserTreeOnCollapseListener(this, this.contentSpace_DataContainer));
        this.addActionHandler(new ContentSpaceBrowserTreeContextMenuActionHandler(this, this.contentSpace_DataContainer));        
  
        // Cause valueChange immediately when the user selects
        this.setImmediate(true);
    }

    public HierarchicalContainer getcontentSpaceDataContainer() {
        return  this.contentSpace_DataContainer;
    }

    public String getContentObjectABSPathByItemId(Object itemID) {
        //Current clicked item
        Item currentItem = this.getItem(itemID);
        if (itemID.toString().startsWith(ContentSpaceDataProvider.contentSpaceIdPerfix)) {
            //clicked ContentSpace,take no action            
            return null;
        } else {
            //clicked content object
            String contentSpace = currentItem.getItemProperty(ContentSpaceDataProvider.CONTENT_SPACE_NAME).toString();
            String currentContentObjectName = currentItem.getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME).toString();
            List<String> contentObjectPath = new ArrayList<String>();
            contentObjectPath.add(currentContentObjectName);
            Object parentItemID = this.contentSpace_DataContainer.getParent(itemID);
            while (parentItemID != null) {
                Object contentRepositoryElementName = this.contentSpace_DataContainer.getItem(parentItemID).getItemProperty(ContentSpaceDataProvider.CONTENT_REPOSITORY_ELEMENT_NAME);
                if (contentRepositoryElementName != null && !contentRepositoryElementName.toString().equals(contentSpace)) {
                    contentObjectPath.add(contentRepositoryElementName.toString());
                }
                parentItemID = this.contentSpace_DataContainer.getParent(parentItemID);
            }
            Collections.reverse(contentObjectPath);
            String contentObjectABsPath = "/";
            for (String str : contentObjectPath) {
                contentObjectABsPath = contentObjectABsPath + str + "/";
            }
            return contentObjectABsPath.substring(0, contentObjectABsPath.length() - 1);
        }
    }
    
    public void loadContentObjectDetailInfo(Object itemValue){
    	if (!itemValue.toString().startsWith(ContentSpaceDataProvider.contentSpaceIdPerfix)) {
            //clicked ContentSpace,take no action            
    		this.contentObjectBrowser.getContentManagementPanel().contentObjectDetail.loadContentObjectDetailInfo(itemValue);
        }     	
	} 
}
