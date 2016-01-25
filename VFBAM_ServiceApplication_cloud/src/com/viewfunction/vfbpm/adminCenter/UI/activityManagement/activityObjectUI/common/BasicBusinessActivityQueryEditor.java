package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.util.List;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.processRepository.exception.ProcessRepositoryRuntimeException;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class BasicBusinessActivityQueryEditor extends VerticalLayout{
	private static final long serialVersionUID = 6528203967857088308L;
	
	private UserClientInfo userClientInfo;	
	public BasicBusinessActivityQueryEditor(UserClientInfo userClientInfo){
		this.userClientInfo=userClientInfo;
		this.setSizeFull();			
		TabSheet activityQueryTabsheet=new TabSheet();  		    
		activityQueryTabsheet.setStyleName(Reindeer.TABSHEET_SMALL);	    
		this.addComponent(activityQueryTabsheet);	     
		activityQueryTabsheet.setSizeFull();		
		
		VerticalLayout unfinishedActivityContainerLayout=new VerticalLayout();
		unfinishedActivityContainerLayout.setSizeFull();	  
		    
		VerticalLayout finishedActivityContainerLayout=new VerticalLayout();
		finishedActivityContainerLayout.setSizeFull();
		
		activityQueryTabsheet.addTab(unfinishedActivityContainerLayout,userClientInfo.getI18NProperties().getProperty("ActivityManage_BasicBusinessActivityQueryEditor_unFinishedActivityTab"),null);		    
		activityQueryTabsheet.addTab(finishedActivityContainerLayout,userClientInfo.getI18NProperties().getProperty("ActivityManage_BasicBusinessActivityQueryEditor_finishedActivityTab"),null);		
		
		ActivitySpace userActivitySpace=ActivityComponentFactory.getActivitySpace(this.userClientInfo.getUserActivitySpace());		
		try {
			List<BusinessActivity> unFinishedBusinessActivityList=userActivitySpace.getBusinessActivitiesByStartUserId(userClientInfo.getUserParticipant().getParticipantName(), ActivitySpace.ACTIVITY_STATUS_UNFINISHED);
			unfinishedActivityContainerLayout.addComponent(new UnfinishedActivityTable(this.userClientInfo,unFinishedBusinessActivityList));			
			List<BusinessActivity> finishedBusinessActivityList=userActivitySpace.getBusinessActivitiesByStartUserId(userClientInfo.getUserParticipant().getParticipantName(), ActivitySpace.ACTIVITY_STATUS_FINISHED);
			finishedActivityContainerLayout.addComponent(new FinishedActivityTable(this.userClientInfo,finishedBusinessActivityList));
		} catch (ProcessRepositoryRuntimeException e) {			
			e.printStackTrace();
		}	
	}
}