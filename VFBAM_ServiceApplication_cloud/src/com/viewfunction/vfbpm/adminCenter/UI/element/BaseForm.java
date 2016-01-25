/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
/**
 *
 * @author WYC
 */
public class BaseForm extends Form {
   
	private static final long serialVersionUID = 2859792321447096592L;

	public BaseForm() {
        getLayout().setStyleName(UICommonElementDefination.FORM_INPUT_AREA_LAYOUT_STYLE);
        setWriteThrough(false);
        setInvalidCommitted(false);
        setImmediate(true);
        setFooter(new VerticalLayout());       
    }
}
