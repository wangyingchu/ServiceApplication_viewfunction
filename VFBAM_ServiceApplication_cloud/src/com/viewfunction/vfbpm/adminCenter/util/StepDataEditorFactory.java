package com.viewfunction.vfbpm.adminCenter.util;

import com.vaadin.ui.VerticalLayout;

import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.vfbpm.adminCenter.UI.activityManagement.activityObjectUI.common.ActivityStepDetailEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.BaseActivityStepEditor;
import com.viewfunction.vfbpm.adminCenter.UI.element.ReloadableUIElement;
import com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration.StepEditorRecode;

public class StepDataEditorFactory {

	public static VerticalLayout buildStepDataEditor(ActivityStep activityStep,UserClientInfo userClientInfo,ReloadableUIElement[] reloadableUIElements){	
		StepEditorRecode stepEditorRecode;
		if(userClientInfo==null||userClientInfo.getStepEditorRecode()==null){
			stepEditorRecode=new StepEditorRecode();
		}else{
			stepEditorRecode=userClientInfo.getStepEditorRecode();
		}		
		try {
			String activitySpaceName=activityStep.getBusinessActivity().getActivityDefinition().getActivitySpaceName();
			String currentStepName=activityStep.getActivityStepDefinitionKey();
			String activityType=activityStep.getActivityType();			
			String stepEditor=stepEditorRecode.getStepEditorName(activitySpaceName, activityType, currentStepName);
			if(stepEditor!=null){
				//System.out.println(stepEditor);				
				Class stepEditorClass = Class.forName(stepEditor);	
				BaseActivityStepEditor currentStepEditor=(BaseActivityStepEditor)stepEditorClass.newInstance();
				currentStepEditor.setActivityStep(activityStep);
				currentStepEditor.setUserClientInfo(userClientInfo);
				currentStepEditor.setReloadableUIElements(reloadableUIElements);
				currentStepEditor.assembleStepEditor();
				return currentStepEditor;
			}else{
				return new ActivityStepDetailEditor(activityStep,userClientInfo,reloadableUIElements);	
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {			
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		}		
		return new ActivityStepDetailEditor(activityStep,userClientInfo,reloadableUIElements);	
	}	
}