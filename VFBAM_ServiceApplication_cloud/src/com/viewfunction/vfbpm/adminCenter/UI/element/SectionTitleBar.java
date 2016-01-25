package com.viewfunction.vfbpm.adminCenter.UI.element;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;

public class SectionTitleBar extends HorizontalLayout{
	private static final long serialVersionUID = 472338496505172325L;
	public static final String MIDDLEFONT="MIDDLEFONT";
	public static final String BIGFONT="BIGFONT";
	
	public SectionTitleBar(Embedded sectionIcon,String sectionTitle,String type,HorizontalLayout sectionTitleActionbarContainer){
		this.setWidth("100%");
		if(type.equals(BIGFONT)){
			this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_SectionTitleText_bigfont);
		}else{
			this.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_SectionTitleText);
		}		
		HorizontalLayout sectionTitleTextContainer=new HorizontalLayout();		
		sectionTitleTextContainer.addComponent(sectionIcon);		
		sectionTitleTextContainer.setComponentAlignment(sectionIcon, Alignment.MIDDLE_LEFT);		
		Label contentObjectPropertyLabel=new Label("<span style='margin-left: 3px;'>"+sectionTitle+"</span>",Label.CONTENT_XHTML);			
		sectionTitleTextContainer.addComponent(contentObjectPropertyLabel);
		sectionTitleTextContainer.setComponentAlignment(contentObjectPropertyLabel, Alignment.MIDDLE_LEFT);			
		this.addComponent(sectionTitleTextContainer);
		this.setComponentAlignment(sectionTitleTextContainer, Alignment.MIDDLE_LEFT);	
		
		if(sectionTitleActionbarContainer!=null){
			this.addComponent(sectionTitleActionbarContainer);
			this.setComponentAlignment(sectionTitleActionbarContainer, Alignment.MIDDLE_RIGHT);	
		}		
	}
}