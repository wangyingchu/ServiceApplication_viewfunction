package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.BaseTheme;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

public class MainTitleBar extends HorizontalLayout{
	private static final long serialVersionUID = -7891269441776945163L;
	
	public MainTitleBar(String headTitle,String tailTitle){
		this.setWidth("100%");	
		this.setHeight("23px");		
		this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);
		HorizontalLayout linksHorizontalLayout=new HorizontalLayout();			
		Label mainTitleLable;
		mainTitleLable=new Label("<span style='color:#333333;margin-left: 10px;'>"+headTitle+"</span>", Label.CONTENT_XHTML);		
		linksHorizontalLayout.addComponent(mainTitleLable);
		linksHorizontalLayout.setComponentAlignment(mainTitleLable, Alignment.MIDDLE_LEFT);		
		Label activitySpaceNameDiv=new Label("<span style='margin-left: 5px;margin-right: 5px;'>: ></span>" ,Label.CONTENT_XHTML);			
		linksHorizontalLayout.addComponent(activitySpaceNameDiv);
		linksHorizontalLayout.setComponentAlignment(activitySpaceNameDiv, Alignment.MIDDLE_LEFT);
		
		Label activityObjecteName=new Label("<b style='color:#ce0000;'>"+tailTitle+"</b>", Label.CONTENT_XHTML);		
		linksHorizontalLayout.addComponent(activityObjecteName);
		linksHorizontalLayout.setComponentAlignment(activityObjecteName, Alignment.MIDDLE_LEFT);
		
		this.addComponent(linksHorizontalLayout);
		this.setComponentAlignment(linksHorizontalLayout, Alignment.MIDDLE_LEFT);		
	}
	
	public MainTitleBar(String headTitle,Button parentObjectLinkButton,String tailTitle){		
		this.setWidth("100%");	
		this.setHeight("23px");		
		this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);
		
		HorizontalLayout linksHorizontalLayout=new HorizontalLayout();			
		Label headTitleLabel=new Label("<span style='color:#333333;margin-left: 10px;'>"+headTitle+"</span>", Label.CONTENT_XHTML);			
		linksHorizontalLayout.addComponent(headTitleLabel);
		linksHorizontalLayout.setComponentAlignment(headTitleLabel, Alignment.MIDDLE_LEFT);
		
		Label headTitleDiv=new Label("<span style='margin-left: 5px;margin-right: 5px;'> : </span>" ,Label.CONTENT_XHTML);			
		linksHorizontalLayout.addComponent(headTitleDiv);
		linksHorizontalLayout.setComponentAlignment(headTitleDiv, Alignment.MIDDLE_LEFT);	

		parentObjectLinkButton.setStyleName(BaseTheme.BUTTON_LINK);	
		linksHorizontalLayout.addComponent(parentObjectLinkButton);
		linksHorizontalLayout.setComponentAlignment(parentObjectLinkButton, Alignment.MIDDLE_LEFT);	
		
		Label activitySpaceNameDiv_2=new Label("<span style='margin-left: 5px;margin-right: 5px;'> > </span>" ,Label.CONTENT_XHTML);			
		linksHorizontalLayout.addComponent(activitySpaceNameDiv_2);
		linksHorizontalLayout.setComponentAlignment(activitySpaceNameDiv_2, Alignment.MIDDLE_LEFT);		
		
		Label currentActivityObjecteName=new Label("<b style='color:#ce0000;'>"+tailTitle+"</b>", Label.CONTENT_XHTML);		
		linksHorizontalLayout.addComponent(currentActivityObjecteName);
		linksHorizontalLayout.setComponentAlignment(currentActivityObjecteName, Alignment.MIDDLE_LEFT);	
		
		this.addComponent(linksHorizontalLayout);
		this.setComponentAlignment(linksHorizontalLayout, Alignment.MIDDLE_LEFT);
	}
}