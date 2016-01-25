package com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common;

import java.util.Map;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.activityEngine.activityView.common.ActivityData;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class BasicStepDataEditor extends VerticalLayout{
	private static final long serialVersionUID = 8822481426886535469L;
	
	private ActivityStep activityStep;
	private UserClientInfo userClientInfo;
	private Map<String,ActivityData> activityDataMap;
	
	public BasicStepDataEditor(ActivityStep activityStep,UserClientInfo userClientInfo,Map<String,ActivityData> activityDataMap){
		this.activityStep=activityStep;
		this.userClientInfo=userClientInfo;
		this.activityDataMap=activityDataMap;
		this.setWidth("100%");			
		Label stepDataFieldLabel=new Label("<span style='color:#555555;text-shadow: 1px 1px 1px #eeeeee;font-weight: bold;'>"+
				this.userClientInfo.getI18NProperties().getProperty("ActivityManage_BasicStepDataEditor_taskDataField")+"</span>",Label.CONTENT_XHTML);
		this.addComponent(stepDataFieldLabel);			
		try {
			ActivityData[] stepActivityData=this.activityStep.getActivityStepData();
			if(stepActivityData!=null&&stepActivityData.length!=0){								
				for(ActivityData activityData:stepActivityData){					
					this.activityDataMap.put(activityData.getDataFieldDefinition().getFieldName(), activityData);					
				}				
				DataFieldValueEditor dataFieldValueEditor=new DataFieldValueEditor(stepActivityData,this.userClientInfo,this.activityDataMap);				
				this.addComponent(dataFieldValueEditor);				
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {		
			e.printStackTrace();
		} catch (ActivityEngineProcessException e) {			
			e.printStackTrace();
		}
	}
}