package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI;
import com.vaadin.ui.Alignment;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.activityEngine.activityView.ProcessQueue;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.ActivityObjectDetail;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;
public class ProcessQueuesUI  extends VerticalLayout{
	private static final long serialVersionUID = -4733760417049064279L;

	public ProcessQueuesUI(ProcessQueue[] processQueueArray, UserClientInfo userClientInfo, ActivityObjectDetail activityObjectDetail){
		//render title bar
		HorizontalLayout actionBarHorizontalLayout=new HorizontalLayout();
		actionBarHorizontalLayout.setWidth("100%");	
		actionBarHorizontalLayout.setHeight("23px");		
		actionBarHorizontalLayout.setStyleName(UICommonElementDefination.APPLICATION_GLOBAL_STYLE_ContentManagementNavTitleText);				
		this.addComponent(actionBarHorizontalLayout);	
		

		HorizontalLayout linksHorizontalLayout=new HorizontalLayout();			
		Label activitySpaceName;		
		if(processQueueArray!=null&&processQueueArray.length>0){
			activitySpaceName=new Label("<span style='color:#333333;margin-left: 10px;'>"+processQueueArray[0].getActivitySpaceName()+"</span>", Label.CONTENT_XHTML);
		}
		else{
			activitySpaceName=new Label("<span style='color:#333333;margin-left: 10px;'>"+
					userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_activitySpaceName")+"</span>", Label.CONTENT_XHTML);
		}		
		linksHorizontalLayout.addComponent(activitySpaceName);
		linksHorizontalLayout.setComponentAlignment(activitySpaceName, Alignment.MIDDLE_LEFT);
		
		Label activitySpaceNameDiv=new Label("<span style='margin-left: 5px;margin-right: 5px;'>: ></span>" ,Label.CONTENT_XHTML);			
		linksHorizontalLayout.addComponent(activitySpaceNameDiv);
		linksHorizontalLayout.setComponentAlignment(activitySpaceNameDiv, Alignment.MIDDLE_LEFT);
		
		Label activityObjecteName=new Label("<b style='color:#ce0000;'>"+
				userClientInfo.getI18NProperties().getProperty("ActivityManage_titlebar_processQueuesLabel")+"</b>", Label.CONTENT_XHTML);		
		linksHorizontalLayout.addComponent(activityObjecteName);
		linksHorizontalLayout.setComponentAlignment(activityObjecteName, Alignment.MIDDLE_LEFT);
		
		actionBarHorizontalLayout.addComponent(linksHorizontalLayout);
		actionBarHorizontalLayout.setComponentAlignment(linksHorizontalLayout, Alignment.MIDDLE_LEFT);	
		
		
		//render Participants list		
		
		Label label=new Label("ProcessQueuesUI");
		this.addComponent(label);
	}

}
