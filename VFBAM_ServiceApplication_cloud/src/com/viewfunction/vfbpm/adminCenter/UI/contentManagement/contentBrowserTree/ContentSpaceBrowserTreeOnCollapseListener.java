/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.contentBrowserTree;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.CollapseEvent;
import java.util.Collection;

/**
 *
 * @author wangychu
 */
public class ContentSpaceBrowserTreeOnCollapseListener implements Tree.CollapseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6525634686327831749L;
	private ContentSpaceBrowserTree contentSpaceBrowserTree;
    private HierarchicalContainer contentSpace_DataContainer;

    public ContentSpaceBrowserTreeOnCollapseListener(ContentSpaceBrowserTree tree, HierarchicalContainer dataContainer) {
        this.contentSpaceBrowserTree = tree;
        this.contentSpace_DataContainer = dataContainer;
    }

    public void nodeCollapse(CollapseEvent event) {
        //remove all children element from current tree node
        Collection childrenCollection = contentSpace_DataContainer.getChildren(event.getItemId());
        if (childrenCollection != null) {
            Object[] keyArray = childrenCollection.toArray();
            childrenCollection = null;
            for (int i = 0; i < keyArray.length; i++) {
                contentSpace_DataContainer.removeItemRecursively(keyArray[i]);
            }
        }
    }
}