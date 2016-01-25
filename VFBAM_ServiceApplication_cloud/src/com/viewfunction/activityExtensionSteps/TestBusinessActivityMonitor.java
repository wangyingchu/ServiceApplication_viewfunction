package com.viewfunction.activityExtensionSteps;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityBureau.BusinessActivity;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.extension.ActivityStepContext;
import com.viewfunction.activityEngine.extension.BusinessActivityMonitor;
import com.viewfunction.processRepository.exception.ProcessRepositoryRuntimeException;
import com.viewfunction.processRepository.processBureau.ProcessObject;

public class TestBusinessActivityMonitor extends BusinessActivityMonitor{

	private static final long serialVersionUID = 982106107550950678L;

	@Override
	public void executeBusinessActivityEndMonitorLogic(ActivityStepContext activityStepContext) {
		System.out.println("TestBusinessActivityMonitor- "+"END "+activityStepContext.getProcessStepId());		
	}

	@Override
	public void executeBusinessActivityGeneralMonitorLogic(ActivityStepContext activityStepContext) {
		/*
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("ActivityStepContext Params:");
		System.out.println("*******************************");
		System.out.println(activityStepContext.getActivitySpaceName());
		System.out.println(activityStepContext.getApplicationSpaceDocumentFolderPath());
		System.out.println(activityStepContext.getCurrentBusinessActivityDocumentFolder());
		System.out.println(activityStepContext.getParticipantDocumentFolderPath("ManagerA"));
		System.out.println(activityStepContext.getRoleDocumentFolderPath("Financial Department"));
		System.out.println("*******************************");
		System.out.println("StepContext Params:");
		System.out.println("*******************************");
		System.out.println(activityStepContext.getProcessDefinitionId());
		System.out.println(activityStepContext.getProcessObjectId());
		System.out.println(activityStepContext.getProcessSpaceName());
		System.out.println(activityStepContext.getProcessStepId());
		System.out.println(activityStepContext.getProcessStepName());
		System.out.println(activityStepContext.getProcessType());		
		System.out.println("*******************************");
		System.out.println("getProcessObject>>>>>>>");
		System.out.println("*******************************");		
		ProcessObject po;
		try {
			po = activityStepContext.getProcessObject();
			if(po!=null){
				System.out.println(po.getProcessDefinitionId());
				System.out.println(po.getProcessStartUserId());
				System.out.println(po.getProcessObjectId());
				System.out.println(po.isFinished());
				System.out.println(po.getProcessDurationInMillis());
				System.out.println(po.getProcessEndTime());
				System.out.println(po.getProcessStartTime());			
				System.out.println(po.getCurrentProcessSteps());
				System.out.println(po.getFinishedProcessSteps());
				System.out.println(po.getNextProcessSteps());
			}
		} catch (ProcessRepositoryRuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		System.out.println("*******************************");	
		System.out.println("getActivitySpace>>>>>>>");
		System.out.println("*******************************");		
		ActivitySpace activitySpace= activityStepContext.getActivitySpace();		
		if(activitySpace!=null){
			activitySpace.getActivitySpaceName();
			try {
				System.out.println(activitySpace.getBusinessActivityTypes());				
				System.out.println(activitySpace.getParticipant("ManagerA"));				
				System.out.println(activitySpace.getRosters());				
			} catch (ActivityEngineRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		System.out.println("*******************************");
		System.out.println("getCurrentBusinessActivityDefinition>>>>>>>");
		System.out.println("*******************************");
		BusinessActivityDefinition bd;
		try {
			bd = activityStepContext.getCurrentBusinessActivityDefinition();
			if(bd!=null){
				System.out.println(bd.getActivityType());
				System.out.println(bd.getActivityDataFields().length);				
				System.out.println(bd.getActivityStepsExposedDataField());
				System.out.println(bd.getExposedSteps().length);
				System.out.println(bd.getActivityType());				
			}			
			System.out.println("*******************************");
			System.out.println("getCurrentActivityStep>>>>>>>");
			System.out.println("*******************************");
			ActivityStep stepFromActivityStepContext =activityStepContext.getCurrentActivityStep();
			if(stepFromActivityStepContext!=null){				
				String currentActivityStepName=stepFromActivityStepContext.getActivityStepName();
				System.out.println(currentActivityStepName);				
				String stepDefinitionKey=stepFromActivityStepContext.getActivityStepDefinitionKey();				
				if(bd.containsExposedActivityStep(stepDefinitionKey)){
					System.out.println(stepFromActivityStepContext.getActivityStepData().length);
					System.out.println(stepFromActivityStepContext.getActivityProcessObject());
					System.out.println(stepFromActivityStepContext.getCreateTime());
					System.out.println(stepFromActivityStepContext.getFinishTime());
					System.out.println(stepFromActivityStepContext.getRelatedRole());
					System.out.println(stepFromActivityStepContext.getStepAssignee());					
				}								
			}			
		} catch (ActivityEngineRuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ActivityEngineActivityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ActivityEngineDataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ActivityEngineProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("*******************************");
		System.out.println("getCurrentBusinessActivity>>>>>>>");
		System.out.println("*******************************");
		try {
			BusinessActivity businessActivity=activityStepContext.getCurrentBusinessActivity();
			if(businessActivity!=null){
				System.out.println(businessActivity.getActivityData());
				System.out.println(businessActivity.getActivityDefinition());
				System.out.println(businessActivity.getActivityId());
				System.out.println(businessActivity.getActivityProcessObject());
				System.out.println(businessActivity.getDocumentsFolderPath());
				System.out.println(businessActivity.getRosterName());
				System.out.println(businessActivity.getComments());				
			}			
		} catch (ActivityEngineProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActivityEngineRuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ActivityEngineActivityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");		
		*/
	}

	@Override
	public void executeBusinessActivityStartMonitorLogic(ActivityStepContext activityStepContext) {
		System.out.println("TestBusinessActivityMonitor- "+"START "+activityStepContext.getProcessStepId());		
	}

	@Override
	public void executeBusinessActivityTransitionMonitorLogic(ActivityStepContext activityStepContext) {
		System.out.println("TestBusinessActivityMonitor- "+"LEAVE "+activityStepContext.getProcessStepId());		
	}

}
