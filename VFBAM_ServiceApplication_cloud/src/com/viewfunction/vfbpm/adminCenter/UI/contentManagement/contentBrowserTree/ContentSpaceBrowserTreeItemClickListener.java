/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viewfunction.vfbpm.adminCenter.UI.contentManagement.contentBrowserTree;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

/**
 *
 * @author wangychu
 */
public class ContentSpaceBrowserTreeItemClickListener implements ItemClickListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6149967766069359639L;

	public void itemClick(ItemClickEvent event) {
         // Indicate which modifier keys are pressed
        String modifiers = "";
        if (event.isAltKey()) {
            modifiers += "Alt ";
        }
        if (event.isCtrlKey()) {
            modifiers += "Ctrl ";
        }
        if (event.isMetaKey()) {
            modifiers += "Meta ";
        }
        if (event.isShiftKey()) {
            modifiers += "Shift ";
        }
        if (modifiers.length() > 0) {
            modifiers = "Modifiers: " + modifiers;
        } else {
            modifiers = "Modifiers: none";
        }
        System.out.println(modifiers);
        switch (event.getButton()) {
        case ItemClickEvent.BUTTON_LEFT:
            // Left button click updates the 'selected' Label
            //getWindow().showNotification("Selected item: " + event.getItem(), modifiers);
              System.out.println("BUTTON_LEFT");
            break;
        case ItemClickEvent.BUTTON_MIDDLE:
            // Middle button click removes the item
             System.out.println("BUTTON_MIDDLE");
            break;
        case ItemClickEvent.BUTTON_RIGHT:
            // Right button click creates a new child item
        	System.out.println("BUTTON_RIGHT");
            break;
        }
    }
   

}
