/*
* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.contentBrowserTree;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;

/**
 *
 * @author wangychu
 */
public class ContentSpaceBrowserTreeOnClickListener implements Property.ValueChangeListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1355246321356239796L;
	
	private ContentSpaceBrowserTree contentSpaceBrowserTree;

    public ContentSpaceBrowserTreeOnClickListener(ContentSpaceBrowserTree tree) {
        this.contentSpaceBrowserTree = tree;      
    }

    public void valueChange(ValueChangeEvent event) {
        //the second time click on the same tree node will get a null event.getProperty().getValue()
        //this means unselect current tree node       
        if (event.getProperty().getValue() != null) {     
        	this.contentSpaceBrowserTree.loadContentObjectDetailInfo(event.getProperty().getValue());          
        }
    }
}