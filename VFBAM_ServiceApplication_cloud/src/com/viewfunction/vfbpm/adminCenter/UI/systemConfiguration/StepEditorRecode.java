package com.viewfunction.vfbpm.adminCenter.UI.systemConfiguration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureau.ContentObjectProperty;
import com.viewfunction.contentRepository.contentBureau.ContentSpace;
import com.viewfunction.contentRepository.contentBureau.RootContentObject;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;

public class StepEditorRecode implements Serializable{
	private static final long serialVersionUID = 1098646510392613098L;
	
	Map<String,Object> activitySpacesRecodeMap;
	
	public StepEditorRecode(){		
		activitySpacesRecodeMap=new HashMap<String,Object>();	
		ContentSpace systemConfigSpace=null;
		try {
			List<String> registeredContentSpaceList=ContentComponentFactory.getRegisteredContentSpace();			
			if(registeredContentSpaceList.contains(SystemConfigurationConstant.SYSTEMCONFIG_CONTENTSPACE)){
				systemConfigSpace=ContentComponentFactory.connectContentSpace(SystemConfigurationConstant.SYSTEMCONFIG_CONTENTSPACE);				
			}else{
				systemConfigSpace=ContentComponentFactory.createContentSpace(SystemConfigurationConstant.SYSTEMCONFIG_CONTENTSPACE);
				systemConfigSpace=ContentComponentFactory.connectContentSpace(SystemConfigurationConstant.SYSTEMCONFIG_CONTENTSPACE);				
			}					
			RootContentObject stepEditorConfigRoot=systemConfigSpace.getRootContentObject(SystemConfigurationConstant.SYSTEMCONFIG_ActivityMetaDataConfig);				
			if(stepEditorConfigRoot!=null){
				List<BaseContentObject> activitySpaceList=stepEditorConfigRoot.getSubContentObjects(null);
				if(activitySpaceList!=null){
					for(BaseContentObject activitySpaceobj:activitySpaceList){
						addActivitySpaceData(activitySpaceobj.getContentObjectName());
						List<BaseContentObject> activityTypeList=activitySpaceobj.getSubContentObjects(null);
						if(activityTypeList!=null){
							for(BaseContentObject activityTypeObj:activityTypeList){
								addActivityTypeData(activitySpaceobj.getContentObjectName(), activityTypeObj.getContentObjectName());	
								List<BaseContentObject> activityStepList=activityTypeObj.getSubContentObjects(null);
								if(activityStepList!=null){
									for(BaseContentObject activityStepObj:activityStepList){
										ContentObjectProperty stepEditorProperty=activityStepObj.getProperty(SystemConfigurationConstant.SYSTEMCONFIG_StepEditorName);
										if(stepEditorProperty!=null){
											addActivityStepData(activitySpaceobj.getContentObjectName(), activityTypeObj.getContentObjectName(),activityStepObj.getContentObjectName(),stepEditorProperty.getPropertyValue().toString());									
										}										
									}									
								}								
							}							
						}						
					}					
				}				
			}			
			if(stepEditorConfigRoot==null){			
				RootContentObject stepConfigRootObj=ContentComponentFactory.createRootContentObject(SystemConfigurationConstant.SYSTEMCONFIG_ActivityMetaDataConfig);			
				stepEditorConfigRoot=systemConfigSpace.addRootContentObject(stepConfigRootObj);	
			}
		}catch (ContentReposityException e) {			
			e.printStackTrace();			
		} finally{
			if(systemConfigSpace!=null){
				systemConfigSpace.closeContentSpace();
			}
		}			
	}	
	
	public void addActivitySpaceData(String activitySpaceName){		
		Map<String, Object> activitySpaceRecord=new HashMap<String,Object>();		
		this.activitySpacesRecodeMap.put(activitySpaceName, activitySpaceRecord);
	}
	
	public void addActivityTypeData(String activitySpaceName,String activityType){		
		Object activitySpaceMap=this.activitySpacesRecodeMap.get(activitySpaceName);
		if(activitySpaceMap==null){
			addActivitySpaceData(activitySpaceName);
			activitySpaceMap=this.activitySpacesRecodeMap.get(activitySpaceName);
		}
		Map<String, Object> activityTypeRecord=	new HashMap<String,Object>();			
		((Map)activitySpaceMap).put(activityType,activityTypeRecord);		
	}
	
	public void addActivityStepData(String activitySpaceName,String activityType,String activityStepName,String activityStepEditor){
		Object activitySpaceMap=this.activitySpacesRecodeMap.get(activitySpaceName);
		if(activitySpaceMap==null){
			addActivitySpaceData(activitySpaceName);
			activitySpaceMap=this.activitySpacesRecodeMap.get(activitySpaceName);
		}		
		Object activityTypeMap=((Map)activitySpaceMap).get(activityType);	
		if(activityTypeMap==null){
			addActivityTypeData(activitySpaceName,activityType);
		}
		activityTypeMap=((Map)activitySpaceMap).get(activityType);
		((Map)activityTypeMap).put(activityStepName,activityStepEditor);		
	}
	
	public void removeActivityStepData(String activitySpaceName,String activityType,String activityStepName){
		Object activitySpaceMap=this.activitySpacesRecodeMap.get(activitySpaceName);		
		Object activityTypeMap=((Map)activitySpaceMap).get(activityType);		
		((Map)activityTypeMap).remove(activityStepName);		
	}
	
	public String getStepEditorName(String activitySpaceName,String activityType,String activityStepName){
		Object activitySpaceMap=this.activitySpacesRecodeMap.get(activitySpaceName);
		if(activitySpaceMap==null){
			return null;
		}else{
			Object activityTypeMap=((Map)activitySpaceMap).get(activityType);
			if(activityTypeMap==null){
				return null;
			}else{
				Object stepEditor=((Map)activityTypeMap).get(activityStepName);
				if(stepEditor!=null){
					return stepEditor.toString();
				}				
			}			
		}	
		return null;
	}	
}