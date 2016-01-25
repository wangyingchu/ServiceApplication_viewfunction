/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

/**
 *
 * @author WYC
 */
public class DialogWindow extends Window {    
	private static final long serialVersionUID = -4494508108814984723L;
	
	private String windowTitleMessage;
    private ThemeResource windowTitleIcon;
    private String windowDescriptionMessage;
    private ThemeResource windowDescriptionIcon;
    private Component windowContentComponent;
    private HorizontalLayout messageLabelLayout;
    private Label messageLable;
    private int windowWidth = 500;   

    public void assembleDialogWindow() {
        if (getWindowTitleIcon() != null) {
            this.setIcon(getWindowTitleIcon());
        }
        if (getWindowTitleMessage() != null) {
            this.setCaption(getWindowTitleMessage());
        }
        VerticalLayout layout = (VerticalLayout) this.getContent();
        layout.setStyleName(UICommonElementDefination.DIALOG_CONTAINER_LAYOUT_STYLE);
        //layout.setMargin(true);
        //layout.setSpacing(true);
        if (getWindowDescriptionMessage() != null || getWindowDescriptionIcon() != null) {
            messageLabelLayout = new HorizontalLayout();
            getMessageLabelLayout().setStyleName(UICommonElementDefination.DIALOG_ITEM_DESCRIPTION_LABLE_STYLE);
            getMessageLabelLayout().setWidth(getWindowWidth()-50 + "px");
            if (getWindowDescriptionIcon() != null) {
                Label iconLable = new Label();
                iconLable.setIcon(getWindowDescriptionIcon());
                getMessageLabelLayout().addComponent(iconLable);
            }
            if (getWindowDescriptionMessage() != null) {
                messageLable = new Label(getWindowDescriptionMessage(), Label.CONTENT_XHTML);
                getMessageLable().setWidth(getWindowWidth()-80 + "px");
                getMessageLabelLayout().addComponent(getMessageLable());
            }
            this.addComponent(getMessageLabelLayout());
        }
        if (getWindowContentComponent() != null) {
            this.addComponent(getWindowContentComponent());  
        }
        this.setWidth(getWindowWidth() + "px");
    }

    public void refreshDialogWindowContent(Component com,Alignment alig) {   
        this.removeComponent(getWindowContentComponent());       
        this.addComponent(com);
        ((VerticalLayout)this.getContent()).setComponentAlignment(com, alig);
         this.setWindowContentComponent(com);       
    }

    public void addWindowContentComponent(Component windowContentComponent) {
        this.addComponent(windowContentComponent);
    }

    /**
     * @return the windowTitleMessage
     */
    public String getWindowTitleMessage() {
        return windowTitleMessage;
    }

    /**
     * @param windowTitleMessage the windowTitleMessage to set
     */
    public void setWindowTitleMessage(String windowTitleMessage) {
        this.windowTitleMessage = windowTitleMessage;
    }

    /**
     * @return the windowTitleIcon
     */
    public ThemeResource getWindowTitleIcon() {
        return windowTitleIcon;
    }

    /**
     * @param windowTitleIcon the windowTitleIcon to set
     */
    public void setWindowTitleIcon(ThemeResource windowTitleIcon) {
        this.windowTitleIcon = windowTitleIcon;
    }

    /**
     * @return the windowDescriptionIcon
     */
    public ThemeResource getWindowDescriptionIcon() {
        return windowDescriptionIcon;
    }

    /**
     * @param windowDescriptionIcon the windowDescriptionIcon to set
     */
    public void setWindowDescriptionIcon(ThemeResource windowDescriptionIcon) {
        this.windowDescriptionIcon = windowDescriptionIcon;
    }

    /**
     * @return the windowDescriptionMessage
     */
    public String getWindowDescriptionMessage() {
        return windowDescriptionMessage;
    }

    /**
     * @param windowDescriptionMessage the windowDescriptionMessage to set
     */
    public void setWindowDescriptionMessage(String windowDescriptionMessage) {
        this.windowDescriptionMessage = windowDescriptionMessage;
    }

    /**
     * @return the windowContentComponent
     */
    public Component getWindowContentComponent() {
        return windowContentComponent;
    }

    /**
     * @param windowContentComponent the windowContentComponent to set
     */
    public void setWindowContentComponent(Component windowContentComponent) {
        this.windowContentComponent = windowContentComponent;
    }

    /**
     * @return the messageLabelLayout
     */
    public HorizontalLayout getMessageLabelLayout() {
        return messageLabelLayout;
    }

    /**
     * @return the messageLable
     */
    public Label getMessageLable() {
        return messageLable;
    }

    /**
     * @return the windowWidth
     */
    public int getWindowWidth() {
        return this.windowWidth;
    }

    /**
     * @param windowWidth the windowWidth to set
     */
    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }
}
