package com.viewfunction.vfbpm.adminCenter.UI.processManagement;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Tree;
import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.processRepository.processBureau.ProcessSpace;
import com.viewfunction.vfbpm.adminCenter.UI.UICommonElementDefination;
import com.viewfunction.vfbpm.adminCenter.util.UserClientInfo;

public class ProcessSpaceDetailTree extends Tree{
	private static final long serialVersionUID = 1000540796627568647L;
	
	private String PROCESS_SPACE_ELEMENT_ROOT_NAME="PROCESS_SPACE_ELEMENT_ROOT_NAME";
	private String PROCESS_SPACE_ELEMENT_PROCESSTYPE_PERFIX="PROCESS_SPACE_ELEMENT_PROCESSTYPE_";
	private String PROCESS_SPACE_PROPERTY_DISPLAYNAME="PROCESS_SPACE_PROPERTY_DISPLAYNAME";
	private String PROCESS_SPACE_PROPERTY_PROCESSSPACENAME="PROCESS_SPACE_PROPERTY_PROCESSSPACENAME";	
	private String PROCESS_SPACE_ELEMENT_RUNNINGPROCESS_NAME="PROCESS_SPACE_ELEMENT_RUNNINGPROCESS_NAME";
	private String PROCESS_SPACE_ELEMENT_FINISHEDPROCESS_NAME="PROCESS_SPACE_ELEMENT_FINISHEDPROCESS_NAME";	
	private String PROCESS_SPACE_ELEMENT_ALLPROCESS_NAME="PROCESS_SPACE_ELEMENT_ALLPROCESS_NAME";	
	private String PROCESS_SPACE_STATUS_PROCESSSTATUS="PROCESS_SPACE_STATUS_PROCESSSTATUS";
	private String PROCESS_SPACE_STATUS_PROCESSTYPE="PROCESS_SPACE_STATUS_PROCESSTYPE";
	
	private ActivitySpace activitySpace;
	private HierarchicalContainer processSpaceObjectTreeContainer;
	private UserClientInfo userClientInfo;
	public ProcessSpaceDetailTree(ActivitySpace activitySpace,UserClientInfo userClientInfo){
		this.activitySpace=activitySpace;
		this.userClientInfo=userClientInfo;
		String processSpaceName=this.activitySpace.getActivitySpaceName();
		this.processSpaceObjectTreeContainer = new HierarchicalContainer();     
		processSpaceObjectTreeContainer.addContainerProperty(PROCESS_SPACE_PROPERTY_DISPLAYNAME, String.class, null);
		processSpaceObjectTreeContainer.addContainerProperty(PROCESS_SPACE_PROPERTY_PROCESSSPACENAME, String.class, null);
		processSpaceObjectTreeContainer.addContainerProperty(PROCESS_SPACE_STATUS_PROCESSSTATUS, String.class, null);
		processSpaceObjectTreeContainer.addContainerProperty(PROCESS_SPACE_STATUS_PROCESSTYPE, String.class, null);
	    Item rootItem=processSpaceObjectTreeContainer.addItem(PROCESS_SPACE_ELEMENT_ROOT_NAME); 
		rootItem.getItemProperty(PROCESS_SPACE_PROPERTY_DISPLAYNAME).setValue(processSpaceName);
		//rootItem.getItemProperty(PROCESS_SPACE_PROPERTY_PROCESSSPACENAME).setValue(processSpaceName);
		this.setContainerDataSource(processSpaceObjectTreeContainer);
		this.setItemCaptionPropertyId(PROCESS_SPACE_PROPERTY_DISPLAYNAME);
		this.setItemIcon(PROCESS_SPACE_ELEMENT_ROOT_NAME, UICommonElementDefination.AppTree_rootItemIcon);			
		try {
			String[] activityTypeArray=this.activitySpace.getBusinessActivityTypes();
			if(activityTypeArray!=null){
				for(int i=0;i<activityTypeArray.length;i++){
					String activityType=activityTypeArray[i];
					Item currentProcessItem=processSpaceObjectTreeContainer.addItem(PROCESS_SPACE_ELEMENT_PROCESSTYPE_PERFIX+i);
					currentProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_DISPLAYNAME).setValue(activityType);
					currentProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_PROCESSSPACENAME).setValue(processSpaceName);	
					
					processSpaceObjectTreeContainer.setParent(PROCESS_SPACE_ELEMENT_PROCESSTYPE_PERFIX+i, PROCESS_SPACE_ELEMENT_ROOT_NAME);
					this.setItemIcon(PROCESS_SPACE_ELEMENT_PROCESSTYPE_PERFIX+i, UICommonElementDefination.ICON_processManagement_processTypeIcon);					
					
					Item runningProcessItem=processSpaceObjectTreeContainer.addItem(PROCESS_SPACE_ELEMENT_RUNNINGPROCESS_NAME+i);
					runningProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_DISPLAYNAME).setValue(
							this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectDetail_processType_runningLabel"));
					runningProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_PROCESSSPACENAME).setValue(processSpaceName);	
					runningProcessItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSSTATUS).setValue("RUNNING");
					runningProcessItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSTYPE).setValue(activityType);
					processSpaceObjectTreeContainer.setParent(PROCESS_SPACE_ELEMENT_RUNNINGPROCESS_NAME+i, PROCESS_SPACE_ELEMENT_PROCESSTYPE_PERFIX+i);
					processSpaceObjectTreeContainer.setChildrenAllowed(PROCESS_SPACE_ELEMENT_RUNNINGPROCESS_NAME+i, false);	
					this.setItemIcon(PROCESS_SPACE_ELEMENT_RUNNINGPROCESS_NAME+i, UICommonElementDefination.ICON_processManagement_runningProcessIcon);
					
					Item finishedProcessItem=processSpaceObjectTreeContainer.addItem(PROCESS_SPACE_ELEMENT_FINISHEDPROCESS_NAME+i);
					finishedProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_DISPLAYNAME).setValue(
							this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectDetail_processType_finishedLabel"));
					finishedProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_PROCESSSPACENAME).setValue(processSpaceName);
					finishedProcessItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSSTATUS).setValue("FINISHED");
					finishedProcessItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSTYPE).setValue(activityType);
					processSpaceObjectTreeContainer.setParent(PROCESS_SPACE_ELEMENT_FINISHEDPROCESS_NAME+i, PROCESS_SPACE_ELEMENT_PROCESSTYPE_PERFIX+i);
					processSpaceObjectTreeContainer.setChildrenAllowed(PROCESS_SPACE_ELEMENT_FINISHEDPROCESS_NAME+i, false);	
					this.setItemIcon(PROCESS_SPACE_ELEMENT_FINISHEDPROCESS_NAME+i, UICommonElementDefination.ICON_processManagement_finishedProcessIcon);
					
					Item allProcessItem=processSpaceObjectTreeContainer.addItem(PROCESS_SPACE_ELEMENT_ALLPROCESS_NAME+i);
					allProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_DISPLAYNAME).setValue(
							this.userClientInfo.getI18NProperties().getProperty("ProcessManage_ProcessObjectDetail_processType_allLabel"));
					allProcessItem.getItemProperty(PROCESS_SPACE_PROPERTY_PROCESSSPACENAME).setValue(processSpaceName);
					allProcessItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSSTATUS).setValue("ALL");
					allProcessItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSTYPE).setValue(activityType);
					processSpaceObjectTreeContainer.setParent(PROCESS_SPACE_ELEMENT_ALLPROCESS_NAME+i, PROCESS_SPACE_ELEMENT_PROCESSTYPE_PERFIX+i);
					processSpaceObjectTreeContainer.setChildrenAllowed(PROCESS_SPACE_ELEMENT_ALLPROCESS_NAME+i, false);	
					this.setItemIcon(PROCESS_SPACE_ELEMENT_ALLPROCESS_NAME+i, UICommonElementDefination.ICON_processManagement_allProcessIcon);
				}				
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
		this.setImmediate(true);
		this.addListener(new ProcessSpaceDetailTreeOnClickListener(this));		
	}
	
	public void handleTreeItemClick(Object itemId){
		Item clickedItem=this.processSpaceObjectTreeContainer.getItem(itemId);		
		Property processSpaceNameProperty=clickedItem.getItemProperty(PROCESS_SPACE_PROPERTY_PROCESSSPACENAME);
		Property processStatusProperty=clickedItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSSTATUS);		
		if(processSpaceNameProperty.getValue()!=null&&processStatusProperty.getValue()!=null){
			String processSpaceName=processSpaceNameProperty.getValue().toString();
			String processStatus=processStatusProperty.getValue().toString();			
			String processType=clickedItem.getItemProperty(PROCESS_SPACE_STATUS_PROCESSTYPE).getValue().toString();
			ProcessObjectQueryEvent processObjectQueryEvent;
			if(processStatus.equals("RUNNING")){
				processObjectQueryEvent=new ProcessObjectQueryEvent(processSpaceName,processType,ProcessSpace.PROCESS_STATUS_UNFINISHED);
			}else if(processStatus.equals("FINISHED")){
				processObjectQueryEvent=new ProcessObjectQueryEvent(processSpaceName,processType,ProcessSpace.PROCESS_STATUS_FINISHED);
			}else{
				processObjectQueryEvent=new ProcessObjectQueryEvent(processSpaceName,processType,ProcessSpace.PROCESS_STATUS_ALL);
			}
			this.userClientInfo.getEventBlackboard().fire(processObjectQueryEvent);
		}
	}
}