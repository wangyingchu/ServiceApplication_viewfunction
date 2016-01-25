package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

public class PropertyItem extends HorizontalLayout{	
	private static final long serialVersionUID = -252242216545645312L;
	
	public static final String POSTION_ODD="POSTION_ODD";
	public static final String POSTION_EVEN="POSTION_EVEN";
	
	public PropertyItem(String postion,Embedded typeIcon,String propertyName,String propertyValue,HorizontalLayout propertyActionContainer){
		this.setWidth("100%");
		this.setHeight("22px");	
		if(postion.equals(POSTION_ODD)){
			this.setStyleName("ui_contentManagementPropertyList_2");			
		}else if(postion.equals(POSTION_EVEN)){
			this.setStyleName("ui_contentManagementPropertyList");
		}		
		HorizontalLayout propertyInfoContainer=new HorizontalLayout();
		if(typeIcon!=null){
			propertyInfoContainer.addComponent(typeIcon);	
		}else{
			propertyInfoContainer.addComponent(new Embedded(null, UICommonElementDefination.AppPropertyList_DefaultHeaderIcon));
		}		
		Label propertyNameLabel=new Label("<span style='margin-left: 10px;margin-right: 15px;color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;'>"+propertyName+"</span>" ,Label.CONTENT_XHTML);
		propertyInfoContainer.addComponent(propertyNameLabel);		
		Label propertyValueLabel=new Label("<span style='color:#1360a8;font-style: italic;text-shadow: 1px 1px 1px #eeeeee;'>"+propertyValue+"</span>" ,Label.CONTENT_XHTML);
		propertyInfoContainer.addComponent(propertyValueLabel);				
		
		this.addComponent(propertyInfoContainer);
		this.setComponentAlignment(propertyInfoContainer, Alignment.MIDDLE_LEFT);
		
		if(propertyActionContainer!=null){
			this.addComponent(propertyActionContainer);
			this.setComponentAlignment(propertyActionContainer, Alignment.MIDDLE_RIGHT);	
		}	
	}	
	
	public PropertyItem(String postion,Embedded typeIcon,String propertyName,Label propertyValueLabel,HorizontalLayout propertyActionContainer){
		this.setWidth("100%");
		this.setHeight("22px");	
		if(postion.equals(POSTION_ODD)){
			this.setStyleName("ui_contentManagementPropertyList_2");			
		}else if(postion.equals(POSTION_EVEN)){
			this.setStyleName("ui_contentManagementPropertyList");
		}		
		HorizontalLayout propertyInfoContainer=new HorizontalLayout();
		if(typeIcon!=null){
			propertyInfoContainer.addComponent(typeIcon);	
		}else{
			propertyInfoContainer.addComponent(new Embedded(null, UICommonElementDefination.AppPropertyList_DefaultHeaderIcon));
		}		
		Label propertyNameLabel=new Label("<span style='margin-left: 10px;margin-right: 15px;color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;'>"+propertyName+"</span>" ,Label.CONTENT_XHTML);
		propertyInfoContainer.addComponent(propertyNameLabel);			
		propertyInfoContainer.addComponent(propertyValueLabel);			
		this.addComponent(propertyInfoContainer);
		this.setComponentAlignment(propertyInfoContainer, Alignment.MIDDLE_LEFT);		
		if(propertyActionContainer!=null){
			this.addComponent(propertyActionContainer);
			this.setComponentAlignment(propertyActionContainer, Alignment.MIDDLE_RIGHT);	
		}	
	}
	
	public PropertyItem(String postion,Embedded typeIcon,String propertyName,String propertyDescription,HorizontalLayout propertyValue,HorizontalLayout propertyActionContainer){
		this.setWidth("100%");
		this.setHeight("22px");	
		if(postion.equals(POSTION_ODD)){
			this.setStyleName("ui_contentManagementPropertyList_2");			
		}else if(postion.equals(POSTION_EVEN)){
			this.setStyleName("ui_contentManagementPropertyList");
		}		
		HorizontalLayout propertyInfoContainer=new HorizontalLayout();
		if(typeIcon!=null){
			propertyInfoContainer.addComponent(typeIcon);	
		}else{
			propertyInfoContainer.addComponent(new Embedded(null, UICommonElementDefination.AppPropertyList_DefaultHeaderIcon));
		}		
		Label propertyNameLabel=new Label("<span style='margin-left: 10px;margin-right: 15px;color:#ce0000;text-shadow: 1px 1px 1px #eeeeee;'>"+propertyName+"</span>" ,Label.CONTENT_XHTML);
		if(propertyDescription!=null){
			propertyNameLabel.setDescription(propertyDescription);
		}		
		propertyInfoContainer.addComponent(propertyNameLabel);
		propertyInfoContainer.addComponent(propertyValue);			
		
		this.addComponent(propertyInfoContainer);
		this.setComponentAlignment(propertyInfoContainer, Alignment.MIDDLE_LEFT);
		
		if(propertyActionContainer!=null){
			this.addComponent(propertyActionContainer);
			this.setComponentAlignment(propertyActionContainer, Alignment.MIDDLE_RIGHT);	
		}	
	}
	
	public PropertyItem(String postion,Embedded typeIcon,HorizontalLayout propertyValue,HorizontalLayout propertyActionContainer){
		this.setWidth("100%");
		this.setHeight("22px");	
		if(postion.equals(POSTION_ODD)){
			this.setStyleName("ui_contentManagementPropertyList_2");			
		}else if(postion.equals(POSTION_EVEN)){
			this.setStyleName("ui_contentManagementPropertyList");
		}		
		HorizontalLayout propertyInfoContainer=new HorizontalLayout();
		if(typeIcon!=null){
			propertyInfoContainer.addComponent(typeIcon);	
		}else{
			propertyInfoContainer.addComponent(new Embedded(null, UICommonElementDefination.AppPropertyList_DefaultHeaderIcon));
		}		
		propertyInfoContainer.addComponent(propertyValue);			
		
		this.addComponent(propertyInfoContainer);
		this.setComponentAlignment(propertyInfoContainer, Alignment.MIDDLE_LEFT);
		
		if(propertyActionContainer!=null){
			this.addComponent(propertyActionContainer);
			this.setComponentAlignment(propertyActionContainer, Alignment.MIDDLE_RIGHT);	
		}	
	}
	
	public PropertyItem(String postion,HorizontalLayout propertyValue){		
		if(postion.equals(POSTION_ODD)){
			this.setStyleName("ui_contentManagementPropertyList_2");			
		}else if(postion.equals(POSTION_EVEN)){
			this.setStyleName("ui_contentManagementPropertyList");
		}else{
			this.setStyleName("ui_contentManagementPropertyList_3");
		}		
		HorizontalLayout propertyInfoContainer=new HorizontalLayout();				
		propertyInfoContainer.addComponent(propertyValue);
		propertyValue.setWidth("100%");
		this.addComponent(propertyInfoContainer);
		this.setComponentAlignment(propertyInfoContainer, Alignment.MIDDLE_LEFT);		
	}
}