/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

/**
 *
 * @author wangychu
 */
public class LightContentPanel extends VerticalLayout {   
	private static final long serialVersionUID = -7138977500941882951L;
	
	private VerticalLayout contentContainerLayout;

    public LightContentPanel(String panelTitle, ThemeResource panelTitleIcon) {
        this.setWidth("90%");
        HorizontalLayout messageLabelLayout = new HorizontalLayout();
        if (panelTitleIcon != null) {
            Label iconLabel = new Label();
            iconLabel.setWidth("20px");
            iconLabel.setIcon(panelTitleIcon);
            messageLabelLayout.addComponent(iconLabel);
        }
        if (panelTitle != null) {
            Label messageLable = new Label(panelTitle, Label.CONTENT_XHTML);
            messageLabelLayout.addComponent(messageLable);
        }
        this.addComponent(messageLabelLayout);
        contentContainerLayout = new VerticalLayout();
        contentContainerLayout.setWidth("100%");
        contentContainerLayout.setStyleName(UICommonElementDefination.INNER_PANEL_BRIGHT_BACKGROUND_STYLE);
        this.addComponent(contentContainerLayout);
        VerticalLayout contentFooterLayout = new VerticalLayout();
        contentFooterLayout.setHeight("15px");
        this.addComponent(contentFooterLayout);
    }

    public void setContent(Component com) {
        contentContainerLayout.addComponent(com);
    }

    public void erasureContent() {      
        contentContainerLayout.removeAllComponents();
    }
}
