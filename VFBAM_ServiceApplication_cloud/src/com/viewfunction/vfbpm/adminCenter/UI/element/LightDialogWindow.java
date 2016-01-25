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
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

/**
 *
 * @author wangychu
 */
public class LightDialogWindow extends Window {   
	private static final long serialVersionUID = 537693716115370420L;
	
	private ThemeResource windowDescriptionIcon;
    private String windowDescriptionMessage;
    private HorizontalLayout messageLabelLayout;
    private Label messageLable;
    private Component windowContentComponent;
    private int windowWidth = 500;
    private VerticalLayout windowContentContainerLayut;

    public LightDialogWindow() {
        this.setStyleName(Reindeer.WINDOW_LIGHT);
    }

    public void assembleDialogWindow() {       
        messageLabelLayout = new HorizontalLayout();
        getMessageLabelLayout().setStyleName(UICommonElementDefination.DIALOG_ITEM_DESCRIPTION_LABLE_STYLE);
        getMessageLabelLayout().setWidth(getWindowWidth() - 50 + "px");
        if (getWindowDescriptionIcon() != null) {
            Label iconLable = new Label();
            iconLable.setIcon(getWindowDescriptionIcon());
            getMessageLabelLayout().addComponent(iconLable);
        }
        if (getWindowDescriptionMessage() != null) {
            messageLable = new Label(getWindowDescriptionMessage(), Label.CONTENT_XHTML);
            getMessageLable().setWidth(getWindowWidth() - 80 + "px");
            getMessageLabelLayout().addComponent(getMessageLable());
        }
        this.addComponent(getMessageLabelLayout());
        if (getWindowContentComponent() != null) {
            this.windowContentContainerLayut = new VerticalLayout();
            getWindowContentContainerLayut().setStyleName(UICommonElementDefination.DIALOG_CONTAINER_LAYOUT_STYLE);
            getWindowContentContainerLayut().addComponent(getWindowContentComponent());
            this.addComponent(getWindowContentContainerLayut());
        }
        this.setWidth(getWindowWidth() + "px");
    }

    /**
     * @return the windowTitleIcon
     */
    public ThemeResource getWindowDescriptionIcon() {
        return windowDescriptionIcon;
    }

    /**
     * @param windowTitleIcon the windowTitleIcon to set
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
     * @return the messageLabelLayout
     */
    public HorizontalLayout getMessageLabelLayout() {
        return messageLabelLayout;
    }

    /**
     * @param messageLabelLayout the messageLabelLayout to set
     */
    public void setMessageLabelLayout(HorizontalLayout messageLabelLayout) {
        this.messageLabelLayout = messageLabelLayout;
    }

    /**
     * @return the messageLable
     */
    public Label getMessageLable() {
        return messageLable;
    }

    /**
     * @param messageLable the messageLable to set
     */
    public void setMessageLable(Label messageLable) {
        this.messageLable = messageLable;
    }

    /**
     * @return the windowWidth
     */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * @param windowWidth the windowWidth to set
     */
    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
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
     * @return the windowContentContainerLayut
     */
    public VerticalLayout getWindowContentContainerLayut() {
        return this.windowContentContainerLayut;
    }
}
