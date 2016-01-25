/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import java.util.List;

/**
 *
 * @author WYC
 */
public class BaseButtonBar extends HorizontalLayout {
    
	private static final long serialVersionUID = 2083779939077644782L;

	public BaseButtonBar(int barWidth, Alignment buttonAlig, List<Button> buttonList) {        
        this.setSizeFull();        
        HorizontalLayout buttonsContainerLayout=new HorizontalLayout();
        buttonsContainerLayout.setWidth(barWidth + "px");
        for (int i = 0; i < buttonList.size(); i++) {
            Button currentButton = buttonList.get(i);
            buttonsContainerLayout.addComponent(currentButton);            
            buttonsContainerLayout.setComponentAlignment(currentButton, Alignment.MIDDLE_CENTER);
        }
        this.addComponent(buttonsContainerLayout);
        this.setComponentAlignment(buttonsContainerLayout, buttonAlig);
    }

    public BaseButtonBar(int barWidth,int barHeight, Alignment buttonAlig, List<Button> buttonList) {
        this.setSizeFull();
        this.setHeight(barHeight+"px");
        HorizontalLayout buttonsContainerLayout=new HorizontalLayout();
        buttonsContainerLayout.setWidth(barWidth + "px");
        for (int i = 0; i < buttonList.size(); i++) {
            Button currentButton = buttonList.get(i);
            buttonsContainerLayout.addComponent(currentButton);
            buttonsContainerLayout.setComponentAlignment(currentButton, Alignment.MIDDLE_CENTER);
        }
        this.addComponent(buttonsContainerLayout);
        this.setComponentAlignment(buttonsContainerLayout, buttonAlig);
    }

    public BaseButtonBar(int barWidth, Alignment buttonAlig, List<Component> buttonList,boolean complex) {
        this.setSizeFull();
        HorizontalLayout buttonsContainerLayout=new HorizontalLayout();
        buttonsContainerLayout.setWidth(barWidth + "px");
        for (int i = 0; i < buttonList.size(); i++) {
            Component currentButton = buttonList.get(i);
            buttonsContainerLayout.addComponent(currentButton);
            buttonsContainerLayout.setComponentAlignment(currentButton, Alignment.MIDDLE_CENTER);
        }
        this.addComponent(buttonsContainerLayout);
        this.setComponentAlignment(buttonsContainerLayout, buttonAlig);
    }

    public BaseButtonBar(int barWidth,int barHeight, Alignment buttonAlig, List<Component> buttonList,boolean complex) {
        this.setSizeFull();
        this.setHeight(barHeight+"px");
        HorizontalLayout buttonsContainerLayout=new HorizontalLayout();
        buttonsContainerLayout.setWidth(barWidth + "px");
        for (int i = 0; i < buttonList.size(); i++) {
            Component currentButton = buttonList.get(i);
            buttonsContainerLayout.addComponent(currentButton);
            buttonsContainerLayout.setComponentAlignment(currentButton, Alignment.MIDDLE_CENTER);
        }
        this.addComponent(buttonsContainerLayout);
        this.setComponentAlignment(buttonsContainerLayout, buttonAlig);
    }
}