package com.viewfunction.vfmab.restful.userManagement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jcr.PropertyType;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityView.common.CustomAttribute;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.activityEngine.util.factory.ActivityComponentFactory;
import com.viewfunction.participantManagement.operation.restful.ParticipantDetailInfoVO;
import com.viewfunction.participantManagement.operation.restful.ParticipantDetailInfoVOsList;
import com.viewfunction.participantManagement.operation.restful.ParticipantDetailInfosQueryVO;
import com.viewfunction.participantManagement.operation.restfulClient.ParticipantOperationServiceRESTClient;
import com.viewfunction.vfmab.restful.activityManagement.RoleVO;
import com.viewfunction.vfmab.restful.util.BooleanOperationResultVO;

@Path("/userManagementService")  
@Produces("application/json")
public class UserManagementService {
	private final String USER_TYPE_PARTICIPANT="PARTICIPANT";
	private final String USER_TYPE_ROLE="ROLE";
	
	private static final String CustomAttributeType_String="STRING";
	private static final String CustomAttributeType_Long="LONG";
	private static final String CustomAttributeType_Date="DATE";
	private static final String CustomAttributeType_Double="DOUBLE";
	private static final String CustomAttributeType_Boolean="BOOLEAN";
	private static final String CustomAttributeType_Decimal="DECIMAL";	
	
	@GET
    @Path("/userUnitsInfo/{applicationSpaceName}")
	@Produces("application/json")
	public List<UserBasicInfoVO> getUserUnitsInfo(@PathParam("applicationSpaceName")String applicationSpaceName) { 		
		ArrayList<UserBasicInfoVO> userBasicInfoVOList=new ArrayList<UserBasicInfoVO>();		
		String activitySpaceName=applicationSpaceName;			
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {			
			Role[] roleList=activitySpace.getRoles();			
			for(Role currentRole:roleList){
				UserBasicInfoVO roleInfoVO=new UserBasicInfoVO();
				roleInfoVO.setUserDisplayName(currentRole.getDisplayName());
				roleInfoVO.setUserId(currentRole.getRoleName());			
				roleInfoVO.setUserType(USER_TYPE_ROLE);		
				userBasicInfoVOList.add(roleInfoVO);					
			}			
			Participant[] participantList=activitySpace.getParticipants();			
			
			List<String> participantsIdList=new ArrayList<String>();
			ParticipantDetailInfosQueryVO participantDetailInfosQueryVO=new ParticipantDetailInfosQueryVO();					
			participantDetailInfosQueryVO.setParticipantsUserUidList(participantsIdList);		
			participantDetailInfosQueryVO.setParticipantScope(applicationSpaceName);			
			for(Participant currentParticipant:participantList){				
				participantsIdList.add(currentParticipant.getParticipantName());
			}	
			//get users' detail info from ParticipantOperationService
			ParticipantDetailInfoVOsList participantDetailInfoVOsList=ParticipantOperationServiceRESTClient.getUsersDetailInfo(participantDetailInfosQueryVO);
			List<ParticipantDetailInfoVO> participantDetailInfoVOsResultList=participantDetailInfoVOsList.getParticipantDetailInfoVOsList();			
			for(ParticipantDetailInfoVO participantDetailInfoVO:participantDetailInfoVOsResultList){
				if(participantDetailInfoVO!=null){
					UserBasicInfoVO participantVO=new UserBasicInfoVO();				
					participantVO.setUserDisplayName(participantDetailInfoVO.getDisplayName());
					participantVO.setUserId(participantDetailInfoVO.getUserId());			
					participantVO.setUserType(USER_TYPE_PARTICIPANT);		
					userBasicInfoVOList.add(participantVO);
				}							
			}			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
	  	return userBasicInfoVOList;
	}
	
	@GET
    @Path("/usersInfoOfRole/{applicationSpaceName}/{roleName}")
	@Produces({"application/xml", "application/json"})
	public UserBasicInfoVOList getUserUnitsInfoOfRole(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName) {
		ArrayList<UserBasicInfoVO> userBasicInfoVOList=new ArrayList<UserBasicInfoVO>();
		String activitySpaceName=applicationSpaceName;			
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Role targetRole=activitySpace.getRole(roleName);			
			Participant[] participantList=targetRole.getParticipants();
			for(Participant currentParticipant:participantList){
				UserBasicInfoVO participantVO=new UserBasicInfoVO();
				participantVO.setUserDisplayName(currentParticipant.getDisplayName());
				participantVO.setUserId(currentParticipant.getParticipantName());			
				participantVO.setUserType(USER_TYPE_PARTICIPANT);		
				userBasicInfoVOList.add(participantVO);					
			}					
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}				
		UserBasicInfoVOList userBasicInfoVOsList=new UserBasicInfoVOList();		
		userBasicInfoVOsList.setUserBasicInfoVOList(userBasicInfoVOList);		
		return userBasicInfoVOsList;
	}
	
	@GET
    @Path("/participantsOfRole/{applicationSpaceName}/{roleName}")
	@Produces("application/json")
	public List<UserBasicInfoVO> getParticipantsOfRole(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName) {
		ArrayList<UserBasicInfoVO> userBasicInfoVOList=new ArrayList<UserBasicInfoVO>();	
		String activitySpaceName=applicationSpaceName;			
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Role targetRole=activitySpace.getRole(roleName);			
			Participant[] participantList=targetRole.getParticipants();			
			List<String> participantsIdList=new ArrayList<String>();
			ParticipantDetailInfosQueryVO participantDetailInfosQueryVO=new ParticipantDetailInfosQueryVO();					
			participantDetailInfosQueryVO.setParticipantsUserUidList(participantsIdList);		
			participantDetailInfosQueryVO.setParticipantScope(applicationSpaceName);			
			for(Participant currentParticipant:participantList){				
				participantsIdList.add(currentParticipant.getParticipantName());
			}				
			//get users' detail info from ParticipantOperationService
			ParticipantDetailInfoVOsList participantDetailInfoVOsList=ParticipantOperationServiceRESTClient.getUsersDetailInfo(participantDetailInfosQueryVO);
			List<ParticipantDetailInfoVO> participantDetailInfoVOsResultList=participantDetailInfoVOsList.getParticipantDetailInfoVOsList();			
			for(ParticipantDetailInfoVO participantDetailInfoVO:participantDetailInfoVOsResultList){
				if(participantDetailInfoVO!=null){
					UserBasicInfoVO participantVO=new UserBasicInfoVO();				
					participantVO.setUserDisplayName(participantDetailInfoVO.getDisplayName());
					participantVO.setUserId(participantDetailInfoVO.getUserId());			
					participantVO.setUserType(USER_TYPE_PARTICIPANT);		
					userBasicInfoVOList.add(participantVO);
				}							
			}	
			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}
		return userBasicInfoVOList;
	}
	
	@GET
    @Path("/colleaguesOfUser/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public ParticipantDetailInfoVOsList getUsersColleagueInfo(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName) {
		String activitySpaceName=applicationSpaceName;				
		ParticipantDetailInfosQueryVO participantDetailInfosQueryVO=new ParticipantDetailInfosQueryVO();
		List<String> userList=new ArrayList<String>();			
		participantDetailInfosQueryVO.setParticipantsUserUidList(userList);		
		participantDetailInfosQueryVO.setParticipantScope(applicationSpaceName);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);			
			Role[] targetRoles=userParticipant.getRoles();			
			for(Role targetRole:targetRoles){
				Participant[] participantList=targetRole.getParticipants();
				for(Participant currentParticipant:participantList){					
					String currentParticipantName=currentParticipant.getParticipantName();
					if(!userList.contains(currentParticipantName)&&!participantName.equals(currentParticipantName)){
						userList.add(currentParticipantName);						
					}					
				}						
			}					
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}				
		ParticipantDetailInfoVOsList participantDetailInfoVOsList=ParticipantOperationServiceRESTClient.getUsersDetailInfo(participantDetailInfosQueryVO);			
		return participantDetailInfoVOsList;				
	}	
	
	@GET
    @Path("/roleColleaguesOfUser/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public List<RoleParticipantsVO> getUsersRoleColleagueInfo(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName) {
		String activitySpaceName=applicationSpaceName;				
		ParticipantDetailInfosQueryVO participantDetailInfosQueryVO=new ParticipantDetailInfosQueryVO();
		List<String> userList=new ArrayList<String>();			
		participantDetailInfosQueryVO.setParticipantsUserUidList(userList);		
		participantDetailInfosQueryVO.setParticipantScope(applicationSpaceName);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);			
		Map<String,RoleParticipantsVO> roleParticipantsMap=new HashMap<String,RoleParticipantsVO>();
		Map<String,List<String>> roleParticipantsIdListMap=new HashMap<String,List<String>>();		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);			
			Role[] targetRoles=userParticipant.getRoles();			
			for(Role targetRole:targetRoles){					
				RoleParticipantsVO currentRoleParticipantsVO=new RoleParticipantsVO();				
				currentRoleParticipantsVO.setRoleName(targetRole.getRoleName());
				currentRoleParticipantsVO.setRoleDisplayName(targetRole.getDisplayName());					
				ParticipantDetailInfoVOsList currentRoleParticipantDetailInfoVOList=new ParticipantDetailInfoVOsList();				
				List<ParticipantDetailInfoVO> currentParticipantDetailInfoVOList=new ArrayList<ParticipantDetailInfoVO>();
				currentRoleParticipantDetailInfoVOList.setParticipantDetailInfoVOsList(currentParticipantDetailInfoVOList);				
				currentRoleParticipantsVO.setRoleParticipants(currentRoleParticipantDetailInfoVOList);				
				roleParticipantsMap.put(currentRoleParticipantsVO.getRoleName(), currentRoleParticipantsVO);				
				List<String> currentParticipantIdList=new ArrayList<String>();
				roleParticipantsIdListMap.put(currentRoleParticipantsVO.getRoleName(), currentParticipantIdList);				
				Participant[] participantList=targetRole.getParticipants();
				if(participantList!=null){
					for(Participant currentParticipant:participantList){					
						String currentParticipantName=currentParticipant.getParticipantName();
						currentParticipantIdList.add(currentParticipantName);					
						if(!userList.contains(currentParticipantName)&&!participantName.equals(currentParticipantName)){
							userList.add(currentParticipantName);						
						}					
					}		
				}								
			}					
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}							
		ParticipantDetailInfoVOsList participantDetailInfoVOsList=ParticipantOperationServiceRESTClient.getUsersDetailInfo(participantDetailInfosQueryVO);
		List<ParticipantDetailInfoVO> fullUserParticipantDetailInfoList= participantDetailInfoVOsList.getParticipantDetailInfoVOsList();		
		for(ParticipantDetailInfoVO currentParticipantDetailInfoVO:fullUserParticipantDetailInfoList){
			String participantId=currentParticipantDetailInfoVO.getUserId();			
			Set<String> roleNameKeys = roleParticipantsIdListMap.keySet( );
		    	if(roleNameKeys != null){
		    		for(String currentRoleName : roleNameKeys){ 
		    			List<String> roleParticipantsIdList=roleParticipantsIdListMap.get(currentRoleName);
		    			if(roleParticipantsIdList.contains(participantId)){
		    				RoleParticipantsVO currentRoleParticipantsVO=roleParticipantsMap.get(currentRoleName);
		    				currentRoleParticipantsVO.getRoleParticipants().getParticipantDetailInfoVOsList().add(currentParticipantDetailInfoVO);		    			   
		    			}			    		   
		    		}		    	   
		    	}
		}		
		List<RoleParticipantsVO> roleParticipantsList = new ArrayList<RoleParticipantsVO>(roleParticipantsMap.values());		
		return roleParticipantsList;		
	}		
	
	@GET
    @Path("/roleColleaguesOfApplicationSpace/{applicationSpaceName}/")
	@Produces("application/json")
	public List<RoleParticipantsVO> getApplicationSpacesRoleColleagueInfo(@PathParam("applicationSpaceName")String applicationSpaceName) {
		String activitySpaceName=applicationSpaceName;				
		ParticipantDetailInfosQueryVO participantDetailInfosQueryVO=new ParticipantDetailInfosQueryVO();
		List<String> userList=new ArrayList<String>();			
		participantDetailInfosQueryVO.setParticipantsUserUidList(userList);		
		participantDetailInfosQueryVO.setParticipantScope(applicationSpaceName);
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);			
		Map<String,RoleParticipantsVO> roleParticipantsMap=new HashMap<String,RoleParticipantsVO>();
		Map<String,List<String>> roleParticipantsIdListMap=new HashMap<String,List<String>>();		
		try {						
			Role[] targetRoles=	activitySpace.getRoles();		
			for(Role targetRole:targetRoles){					
				RoleParticipantsVO currentRoleParticipantsVO=new RoleParticipantsVO();				
				currentRoleParticipantsVO.setRoleName(targetRole.getRoleName());
				currentRoleParticipantsVO.setRoleDisplayName(targetRole.getDisplayName());					
				ParticipantDetailInfoVOsList currentRoleParticipantDetailInfoVOList=new ParticipantDetailInfoVOsList();				
				List<ParticipantDetailInfoVO> currentParticipantDetailInfoVOList=new ArrayList<ParticipantDetailInfoVO>();
				currentRoleParticipantDetailInfoVOList.setParticipantDetailInfoVOsList(currentParticipantDetailInfoVOList);				
				currentRoleParticipantsVO.setRoleParticipants(currentRoleParticipantDetailInfoVOList);				
				roleParticipantsMap.put(currentRoleParticipantsVO.getRoleName(), currentRoleParticipantsVO);				
				List<String> currentParticipantIdList=new ArrayList<String>();
				roleParticipantsIdListMap.put(currentRoleParticipantsVO.getRoleName(), currentParticipantIdList);				
				Participant[] participantList=targetRole.getParticipants();
				if(participantList!=null){
					for(Participant currentParticipant:participantList){					
						String currentParticipantName=currentParticipant.getParticipantName();
						currentParticipantIdList.add(currentParticipantName);						
						if(!userList.contains(currentParticipantName)){
							userList.add(currentParticipantName);
						}																	
					}		
				}				
			}					
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}							
		ParticipantDetailInfoVOsList participantDetailInfoVOsList=ParticipantOperationServiceRESTClient.getUsersDetailInfo(participantDetailInfosQueryVO);
		List<ParticipantDetailInfoVO> fullUserParticipantDetailInfoList= participantDetailInfoVOsList.getParticipantDetailInfoVOsList();		
		for(ParticipantDetailInfoVO currentParticipantDetailInfoVO:fullUserParticipantDetailInfoList){
			String participantId=currentParticipantDetailInfoVO.getUserId();			
			Set<String> roleNameKeys = roleParticipantsIdListMap.keySet( );
		    	if(roleNameKeys != null){
		    		for(String currentRoleName : roleNameKeys){ 
		    			List<String> roleParticipantsIdList=roleParticipantsIdListMap.get(currentRoleName);
		    			if(roleParticipantsIdList.contains(participantId)){
		    				RoleParticipantsVO currentRoleParticipantsVO=roleParticipantsMap.get(currentRoleName);
		    				currentRoleParticipantsVO.getRoleParticipants().getParticipantDetailInfoVOsList().add(currentParticipantDetailInfoVO);		    			   
		    			}			    		   
		    		}		    	   
		    	}
		}		
		List<RoleParticipantsVO> roleParticipantsList = new ArrayList<RoleParticipantsVO>(roleParticipantsMap.values());		
		return roleParticipantsList;		
	}	
		
	@GET
    @Path("/participantActivitySpaceInfo/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public ParticipantActivitySpaceInfoVO getParticipantDetailsInfo(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName) {
		String activitySpaceName=applicationSpaceName;				
		ParticipantActivitySpaceInfoVO participantActivitySpaceInfoVO=new ParticipantActivitySpaceInfoVO();
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);			
			Role[] targetRoles=userParticipant.getRoles();			
			RoleVO[] participantRoleVOArray=new RoleVO[targetRoles.length];			
			for(int i=0;i<targetRoles.length;i++){
				Role currentRole=targetRoles[i];
				RoleVO currentRoleVO=new RoleVO();				
				currentRoleVO.setActivitySpaceName(currentRole.getActivitySpaceName());
				currentRoleVO.setDescription(currentRole.getDescription());
				currentRoleVO.setDisplayName(currentRole.getDisplayName());
				currentRoleVO.setRoleName(currentRole.getRoleName());
				participantRoleVOArray[i]=currentRoleVO;
			}			
			participantActivitySpaceInfoVO.setParticipantRoles(participantRoleVOArray);			
			participantActivitySpaceInfoVO.setParticipantName(userParticipant.getDisplayName());
			participantActivitySpaceInfoVO.setParticipantId(userParticipant.getParticipantName());			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}								
		return participantActivitySpaceInfoVO;				
	}	
	
	@POST
	@Path("/addParticipantCustomAttribute/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public static BooleanOperationResultVO addParticipantCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName,CustomAttributeVO customAttributeVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);				
			CustomAttribute targetCustomAttribute=ActivityComponentFactory.createCustomAttribute();			
			targetCustomAttribute.setArrayAttribute(customAttributeVO.getArrayAttribute());
			targetCustomAttribute.setAttributeName(customAttributeVO.getAttributeName());			
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_String)){				
				targetCustomAttribute.setAttributeType(PropertyType.STRING);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Long)){				
				targetCustomAttribute.setAttributeType(PropertyType.LONG);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Date)){				
				targetCustomAttribute.setAttributeType(PropertyType.DATE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Double)){				
				targetCustomAttribute.setAttributeType(PropertyType.DOUBLE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Boolean)){				
				targetCustomAttribute.setAttributeType(PropertyType.BOOLEAN);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Decimal)){				
				targetCustomAttribute.setAttributeType(PropertyType.DECIMAL);
			}			
			Object attributeValue=generateCustomAttributeValueFromRowData(customAttributeVO.getAttributeType(),customAttributeVO.getArrayAttribute(),customAttributeVO.getAttributeRowValue());				
			targetCustomAttribute.setAttributeValue(attributeValue);			
			boolean result=userParticipant.addCustomAttribute(targetCustomAttribute);
			booleanOperationResultVO.setOperationResult(result);				
		} catch (ActivityEngineRuntimeException | ActivityEngineDataException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}
	
	@POST
	@Path("/updateParticipantCustomAttribute/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public static BooleanOperationResultVO updateParticipantCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName,CustomAttributeVO customAttributeVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);				
			CustomAttribute targetCustomAttribute=ActivityComponentFactory.createCustomAttribute();			
			targetCustomAttribute.setArrayAttribute(customAttributeVO.getArrayAttribute());
			targetCustomAttribute.setAttributeName(customAttributeVO.getAttributeName());			
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_String)){				
				targetCustomAttribute.setAttributeType(PropertyType.STRING);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Long)){
				
				targetCustomAttribute.setAttributeType(PropertyType.LONG);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Date)){				
				targetCustomAttribute.setAttributeType(PropertyType.DATE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Double)){				
				targetCustomAttribute.setAttributeType(PropertyType.DOUBLE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Boolean)){				
				targetCustomAttribute.setAttributeType(PropertyType.BOOLEAN);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Decimal)){				
				targetCustomAttribute.setAttributeType(PropertyType.DECIMAL);
			}			
			Object attributeValue=generateCustomAttributeValueFromRowData(customAttributeVO.getAttributeType(),customAttributeVO.getArrayAttribute(),customAttributeVO.getAttributeRowValue());				
			targetCustomAttribute.setAttributeValue(attributeValue);			
			boolean result=userParticipant.updateCustomAttribute(targetCustomAttribute);
			booleanOperationResultVO.setOperationResult(result);				
		} catch (ActivityEngineRuntimeException | ActivityEngineDataException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}
	
	@GET
	@Path("/getParticipantCustomAttribute/{applicationSpaceName}/{participantName}/{attributeName}")
	@Produces("application/json")
	public static CustomAttributeVO getParticipantCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName,@PathParam("attributeName")String attributeName){
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);	
		try {
			Participant userParticipant = activitySpace.getParticipant(participantName);
			CustomAttribute targetCustomAttribute=userParticipant.getCustomAttribute(attributeName);
			if(targetCustomAttribute==null){
				return null;
			}
			CustomAttributeVO targetCustomAttributeVO=new CustomAttributeVO();			
			targetCustomAttributeVO.setAttributeName(targetCustomAttribute.getAttributeName());
			targetCustomAttributeVO.setArrayAttribute(targetCustomAttribute.isArrayAttribute());			
			if(targetCustomAttribute.getAttributeType()==PropertyType.STRING){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_String);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.LONG){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Long);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DATE){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Date);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DOUBLE){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Double);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.BOOLEAN){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Boolean);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DECIMAL){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Decimal);
			}			
			String[] attributeRowValue=generateCustomAttributeRowValue(targetCustomAttributeVO.getAttributeType(),targetCustomAttributeVO.getArrayAttribute(),targetCustomAttribute.getAttributeValue());				
			targetCustomAttributeVO.setAttributeRowValue(attributeRowValue);					
			return targetCustomAttributeVO;
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	@GET
	@Path("/getParticipantCustomAttributes/{applicationSpaceName}/{participantName}/")
	@Produces("application/json")
	public static List<CustomAttributeVO> getParticipantCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName){
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);	
		List<CustomAttributeVO> customAttributeVOList=new ArrayList<CustomAttributeVO>();
		try {
			Participant userParticipant = activitySpace.getParticipant(participantName);			
			List<CustomAttribute> targetCustomAttributeList=userParticipant.getCustomAttributes();				
			for(CustomAttribute targetCustomAttribute:targetCustomAttributeList){				
				CustomAttributeVO targetCustomAttributeVO=new CustomAttributeVO();			
				targetCustomAttributeVO.setAttributeName(targetCustomAttribute.getAttributeName());
				targetCustomAttributeVO.setArrayAttribute(targetCustomAttribute.isArrayAttribute());			
				if(targetCustomAttribute.getAttributeType()==PropertyType.STRING){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_String);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.LONG){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Long);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DATE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Date);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DOUBLE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Double);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.BOOLEAN){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Boolean);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DECIMAL){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Decimal);
				}			
				String[] attributeRowValue=generateCustomAttributeRowValue(targetCustomAttributeVO.getAttributeType(),targetCustomAttributeVO.getArrayAttribute(),targetCustomAttribute.getAttributeValue());				
				targetCustomAttributeVO.setAttributeRowValue(attributeRowValue);
				customAttributeVOList.add(targetCustomAttributeVO);
			}								
			return customAttributeVOList;
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	@DELETE
	@Path("/deleteParticipantCustomAttribute/{applicationSpaceName}/{participantName}/{attributeName}")
	@Produces("application/json")
	public static BooleanOperationResultVO deleteParticipantCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName,@PathParam("attributeName")String attributeName){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);	
		try {
			Participant userParticipant = activitySpace.getParticipant(participantName);			
			boolean deleteResult=userParticipant.deleteCustomAttribute(attributeName);			
			booleanOperationResultVO.setOperationResult(deleteResult);			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		}		
		return booleanOperationResultVO;
	}		
	
	@POST
	@Path("/addRoleCustomAttribute/{applicationSpaceName}/{roleName}")
	@Produces("application/json")
	public static BooleanOperationResultVO addRoleCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName,CustomAttributeVO customAttributeVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {			
			Role targetRole=activitySpace.getRole(roleName);			
			CustomAttribute targetCustomAttribute=ActivityComponentFactory.createCustomAttribute();			
			targetCustomAttribute.setArrayAttribute(customAttributeVO.getArrayAttribute());
			targetCustomAttribute.setAttributeName(customAttributeVO.getAttributeName());			
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_String)){				
				targetCustomAttribute.setAttributeType(PropertyType.STRING);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Long)){				
				targetCustomAttribute.setAttributeType(PropertyType.LONG);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Date)){				
				targetCustomAttribute.setAttributeType(PropertyType.DATE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Double)){				
				targetCustomAttribute.setAttributeType(PropertyType.DOUBLE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Boolean)){				
				targetCustomAttribute.setAttributeType(PropertyType.BOOLEAN);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Decimal)){				
				targetCustomAttribute.setAttributeType(PropertyType.DECIMAL);
			}			
			Object attributeValue=generateCustomAttributeValueFromRowData(customAttributeVO.getAttributeType(),customAttributeVO.getArrayAttribute(),customAttributeVO.getAttributeRowValue());				
			targetCustomAttribute.setAttributeValue(attributeValue);			
			boolean result=targetRole.addCustomAttribute(targetCustomAttribute);
			booleanOperationResultVO.setOperationResult(result);				
		} catch (ActivityEngineRuntimeException | ActivityEngineDataException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}
	
	@POST
	@Path("/updateRoleCustomAttribute/{applicationSpaceName}/{roleName}")
	@Produces("application/json")
	public static BooleanOperationResultVO updateRoleCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName,CustomAttributeVO customAttributeVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Role targetRole=activitySpace.getRole(roleName);				
			CustomAttribute targetCustomAttribute=ActivityComponentFactory.createCustomAttribute();			
			targetCustomAttribute.setArrayAttribute(customAttributeVO.getArrayAttribute());
			targetCustomAttribute.setAttributeName(customAttributeVO.getAttributeName());			
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_String)){				
				targetCustomAttribute.setAttributeType(PropertyType.STRING);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Long)){
				
				targetCustomAttribute.setAttributeType(PropertyType.LONG);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Date)){				
				targetCustomAttribute.setAttributeType(PropertyType.DATE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Double)){				
				targetCustomAttribute.setAttributeType(PropertyType.DOUBLE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Boolean)){				
				targetCustomAttribute.setAttributeType(PropertyType.BOOLEAN);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Decimal)){				
				targetCustomAttribute.setAttributeType(PropertyType.DECIMAL);
			}			
			Object attributeValue=generateCustomAttributeValueFromRowData(customAttributeVO.getAttributeType(),customAttributeVO.getArrayAttribute(),customAttributeVO.getAttributeRowValue());				
			targetCustomAttribute.setAttributeValue(attributeValue);			
			boolean result=targetRole.updateCustomAttribute(targetCustomAttribute);
			booleanOperationResultVO.setOperationResult(result);				
		} catch (ActivityEngineRuntimeException | ActivityEngineDataException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}
	
	@GET
	@Path("/getRoleCustomAttribute/{applicationSpaceName}/{roleName}/{attributeName}/")
	@Produces("application/json")
	public static CustomAttributeVO getRoleCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName,@PathParam("attributeName")String attributeName){
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);	
		try {
			Role targetRole=activitySpace.getRole(roleName);
			CustomAttribute targetCustomAttribute=targetRole.getCustomAttribute(attributeName);
			if(targetCustomAttribute==null){
				return null;
			}
			CustomAttributeVO targetCustomAttributeVO=new CustomAttributeVO();			
			targetCustomAttributeVO.setAttributeName(targetCustomAttribute.getAttributeName());
			targetCustomAttributeVO.setArrayAttribute(targetCustomAttribute.isArrayAttribute());			
			if(targetCustomAttribute.getAttributeType()==PropertyType.STRING){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_String);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.LONG){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Long);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DATE){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Date);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DOUBLE){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Double);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.BOOLEAN){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Boolean);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DECIMAL){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Decimal);
			}			
			String[] attributeRowValue=generateCustomAttributeRowValue(targetCustomAttributeVO.getAttributeType(),targetCustomAttributeVO.getArrayAttribute(),targetCustomAttribute.getAttributeValue());				
			targetCustomAttributeVO.setAttributeRowValue(attributeRowValue);					
			return targetCustomAttributeVO;
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	@GET
	@Path("/getRoleCustomAttributes/{applicationSpaceName}/{roleName}/")
	@Produces("application/json")
	public static List<CustomAttributeVO> getRoleCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName){
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);	
		List<CustomAttributeVO> customAttributeVOList=new ArrayList<CustomAttributeVO>();
		try {
			Role targetRole=activitySpace.getRole(roleName);			
			List<CustomAttribute> targetCustomAttributeList=targetRole.getCustomAttributes();				
			for(CustomAttribute targetCustomAttribute:targetCustomAttributeList){				
				CustomAttributeVO targetCustomAttributeVO=new CustomAttributeVO();			
				targetCustomAttributeVO.setAttributeName(targetCustomAttribute.getAttributeName());
				targetCustomAttributeVO.setArrayAttribute(targetCustomAttribute.isArrayAttribute());			
				if(targetCustomAttribute.getAttributeType()==PropertyType.STRING){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_String);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.LONG){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Long);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DATE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Date);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DOUBLE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Double);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.BOOLEAN){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Boolean);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DECIMAL){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Decimal);
				}			
				String[] attributeRowValue=generateCustomAttributeRowValue(targetCustomAttributeVO.getAttributeType(),targetCustomAttributeVO.getArrayAttribute(),targetCustomAttribute.getAttributeValue());				
				targetCustomAttributeVO.setAttributeRowValue(attributeRowValue);
				customAttributeVOList.add(targetCustomAttributeVO);
			}								
			return customAttributeVOList;
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	@DELETE
	@Path("/deleteRoleCustomAttribute/{applicationSpaceName}/{roleName}/{attributeName}")
	@Produces("application/json")
	public static BooleanOperationResultVO deleteRoleCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName,@PathParam("attributeName")String attributeName){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);	
		try {
			Role targetRole=activitySpace.getRole(roleName);			
			boolean deleteResult=targetRole.deleteCustomAttribute(attributeName);			
			booleanOperationResultVO.setOperationResult(deleteResult);			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		}		
		return booleanOperationResultVO;
	}		
	
	@POST
	@Path("/addParticipantCustomStructure/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public static BooleanOperationResultVO addParticipantCustomStructure(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName,CustomStructureVO customStructureVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);			
			boolean result=userParticipant.addSubCustomStructure(customStructureVO.getStructureName());
			booleanOperationResultVO.setOperationResult(result);					
		} catch (ActivityEngineRuntimeException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}	
	
	@GET
	@Path("/getParticipantCustomStructures/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public static List<CustomStructureVO> getParticipantCustomStructures(@PathParam("applicationSpaceName")String applicationSpaceName,
			@PathParam("participantName")String participantName,@PathParam("structureName")String structureName){		
		List<CustomStructureVO> targetCustomStructureVOList=new ArrayList<CustomStructureVO>();		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);				
			List<CustomStructure> parentCustomStructureList=userParticipant.getSubCustomStructures();
			if(parentCustomStructureList!=null){
				for(CustomStructure targetCustomStructure:parentCustomStructureList){
					CustomStructureVO targetCustomStructureVO=loadCustomStructure(targetCustomStructure);
					targetCustomStructureVOList.add(targetCustomStructureVO);				
				}	
			}							
		} catch (ActivityEngineRuntimeException e) {				
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		return targetCustomStructureVOList;
	}
	
	@GET
	@Path("/getParticipantCustomStructure/{applicationSpaceName}/{participantName}/{structureName}")
	@Produces("application/json")
	public static CustomStructureVO getParticipantCustomStructureByName(@PathParam("applicationSpaceName")String applicationSpaceName,
			@PathParam("participantName")String participantName,@PathParam("structureName")String structureName){			
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
		CustomStructureVO targetCustomStructureVO=null;
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);				
			CustomStructure targetCustomStructure=userParticipant.getSubCustomStructure(structureName);				
			targetCustomStructureVO=loadCustomStructure(targetCustomStructure);
			return targetCustomStructureVO;				
		} catch (ActivityEngineRuntimeException e) {				
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		return targetCustomStructureVO;
	}	
	
	@DELETE
	@Path("/deleteParticipantCustomStructure/{applicationSpaceName}/{participantName}")
	@Produces("application/json")
	public static BooleanOperationResultVO deleteParticipantCustomStructure(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("participantName")String participantName,CustomStructureVO customStructureVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Participant userParticipant=activitySpace.getParticipant(participantName);				
			boolean result=userParticipant.deleteSubCustomStructure(customStructureVO.getStructureName());			
			booleanOperationResultVO.setOperationResult(result);					
		} catch (ActivityEngineRuntimeException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}	
	
	@POST
	@Path("/addRoleCustomStructure/{applicationSpaceName}/{roleName}")
	@Produces("application/json")
	public static BooleanOperationResultVO addRoleCustomStructure(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName,CustomStructureVO customStructureVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Role targetRole=activitySpace.getRole(roleName);						
			boolean result=targetRole.addSubCustomStructure(customStructureVO.getStructureName());
			booleanOperationResultVO.setOperationResult(result);					
		} catch (ActivityEngineRuntimeException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}	
	
	@GET
	@Path("/getRoleCustomStructures/{applicationSpaceName}/{roleName}")
	@Produces("application/json")
	public static List<CustomStructureVO> getRoleCustomStructures(@PathParam("applicationSpaceName")String applicationSpaceName,
			@PathParam("roleName")String roleName,@PathParam("structureName")String structureName){		
		List<CustomStructureVO> targetCustomStructureVOList=new ArrayList<CustomStructureVO>();		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Role targetRole=activitySpace.getRole(roleName);				
			List<CustomStructure> roleCustomStructureList=targetRole.getSubCustomStructures();
			if(roleCustomStructureList!=null){
				for(CustomStructure targetCustomStructure:roleCustomStructureList){
					CustomStructureVO targetCustomStructureVO=loadCustomStructure(targetCustomStructure);
					targetCustomStructureVOList.add(targetCustomStructureVO);				
				}	
			}							
		} catch (ActivityEngineRuntimeException e) {				
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		return targetCustomStructureVOList;
	}	
	
	@GET
	@Path("/getRoleCustomStructure/{applicationSpaceName}/{roleName}/{structureName}")
	@Produces("application/json")
	public static CustomStructureVO getRoleCustomStructureByName(@PathParam("applicationSpaceName")String applicationSpaceName,
			@PathParam("roleName")String roleName,@PathParam("structureName")String structureName){			
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);
		CustomStructureVO targetCustomStructureVO=null;
		try {
			Role targetRole=activitySpace.getRole(roleName);				
			CustomStructure targetCustomStructure=targetRole.getSubCustomStructure(structureName);				
			targetCustomStructureVO=loadCustomStructure(targetCustomStructure);
			return targetCustomStructureVO;				
		} catch (ActivityEngineRuntimeException e) {				
			e.printStackTrace();
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
		}		
		return targetCustomStructureVO;
	}	
	
	@DELETE
	@Path("/deleteRoleCustomStructure/{applicationSpaceName}/{roleName}")
	@Produces("application/json")
	public static BooleanOperationResultVO deleteRoleCustomStructure(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("roleName")String roleName,CustomStructureVO customStructureVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());		
		String activitySpaceName=applicationSpaceName;
		ActivitySpace activitySpace=ActivityComponentFactory.getActivitySpace(activitySpaceName);		
		try {
			Role targetRole=activitySpace.getRole(roleName);				
			boolean result=targetRole.deleteSubCustomStructure(customStructureVO.getStructureName());			
			booleanOperationResultVO.setOperationResult(result);					
		} catch (ActivityEngineRuntimeException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}		
	
	@POST
	@Path("/customStructure/addSubCustomStructure/{applicationSpaceName}/{structureName}")
	@Produces("application/json")
	public static CustomStructureVO getSubCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("structureName")String structureName,CustomStructureVO customStructureVO){
		try {
			CustomStructure customStructure=ActivityComponentFactory.getCustomStructure(applicationSpaceName, customStructureVO.getStructureId());	
			CustomStructure targetStructure=customStructure.getSubCustomStructure(structureName);
			CustomStructureVO resultCustomStructureVO=null;
			if(targetStructure!=null){
				resultCustomStructureVO= loadCustomStructure(targetStructure);
			}else{
				resultCustomStructureVO=new CustomStructureVO();
				customStructure.addSubCustomStructure(structureName);
				targetStructure=customStructure.getSubCustomStructure(structureName);
				resultCustomStructureVO.setStructureId(targetStructure.getStructureId());
				resultCustomStructureVO.setStructureName(targetStructure.getStructureName());
			}
			return resultCustomStructureVO;
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	@DELETE
	@Path("/customStructure/deleteSubCustomStructure/{applicationSpaceName}/{structureName}")
	@Produces("application/json")
	public static BooleanOperationResultVO deleteStructureSubCustomStructure(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("structureName")String structureName,CustomStructureVO customStructureVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());			
		try {
			CustomStructure customStructure=ActivityComponentFactory.getCustomStructure(applicationSpaceName, customStructureVO.getStructureId());
			boolean deleteResult=customStructure.deleteSubCustomStructure(structureName);	
			booleanOperationResultVO.setOperationResult(deleteResult);			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		}		
		return booleanOperationResultVO;
	}		
	
	@POST
	@Path("/customStructure/addCustomAttribute/{applicationSpaceName}/")
	@Produces("application/json")
	public static BooleanOperationResultVO addStructureCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,StructureAttributrOperationVO structureAttributrOperationVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());					
		try {						
			CustomStructureVO customStructureVO=structureAttributrOperationVO.getCustomStructure();
			CustomAttributeVO customAttributeVO=structureAttributrOperationVO.getCustomAttribute();			
			CustomStructure customStructure=ActivityComponentFactory.getCustomStructure(applicationSpaceName, customStructureVO.getStructureId());			
			CustomAttribute targetCustomAttribute=ActivityComponentFactory.createCustomAttribute();			
			targetCustomAttribute.setArrayAttribute(customAttributeVO.getArrayAttribute());
			targetCustomAttribute.setAttributeName(customAttributeVO.getAttributeName());			
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_String)){				
				targetCustomAttribute.setAttributeType(PropertyType.STRING);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Long)){				
				targetCustomAttribute.setAttributeType(PropertyType.LONG);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Date)){				
				targetCustomAttribute.setAttributeType(PropertyType.DATE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Double)){				
				targetCustomAttribute.setAttributeType(PropertyType.DOUBLE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Boolean)){				
				targetCustomAttribute.setAttributeType(PropertyType.BOOLEAN);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Decimal)){				
				targetCustomAttribute.setAttributeType(PropertyType.DECIMAL);
			}			
			Object attributeValue=generateCustomAttributeValueFromRowData(customAttributeVO.getAttributeType(),customAttributeVO.getArrayAttribute(),customAttributeVO.getAttributeRowValue());				
			targetCustomAttribute.setAttributeValue(attributeValue);			
			boolean result=customStructure.addCustomAttribute(targetCustomAttribute);
			booleanOperationResultVO.setOperationResult(result);				
		} catch (ActivityEngineRuntimeException | ActivityEngineDataException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		} catch (ActivityEngineException e) {
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}	
	
	@POST
	@Path("/customStructure/updateCustomAttribute/{applicationSpaceName}/")
	@Produces("application/json")
	public static BooleanOperationResultVO updateStructureCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,StructureAttributrOperationVO structureAttributrOperationVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());				
		try {
			CustomStructureVO customStructureVO=structureAttributrOperationVO.getCustomStructure();
			CustomAttributeVO customAttributeVO=structureAttributrOperationVO.getCustomAttribute();	
			CustomStructure customStructure=ActivityComponentFactory.getCustomStructure(applicationSpaceName, customStructureVO.getStructureId());						
			CustomAttribute targetCustomAttribute=ActivityComponentFactory.createCustomAttribute();			
			targetCustomAttribute.setArrayAttribute(customAttributeVO.getArrayAttribute());
			targetCustomAttribute.setAttributeName(customAttributeVO.getAttributeName());			
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_String)){				
				targetCustomAttribute.setAttributeType(PropertyType.STRING);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Long)){
				
				targetCustomAttribute.setAttributeType(PropertyType.LONG);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Date)){				
				targetCustomAttribute.setAttributeType(PropertyType.DATE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Double)){				
				targetCustomAttribute.setAttributeType(PropertyType.DOUBLE);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Boolean)){				
				targetCustomAttribute.setAttributeType(PropertyType.BOOLEAN);
			}
			if(customAttributeVO.getAttributeType().equals(CustomAttributeType_Decimal)){				
				targetCustomAttribute.setAttributeType(PropertyType.DECIMAL);
			}			
			Object attributeValue=generateCustomAttributeValueFromRowData(customAttributeVO.getAttributeType(),customAttributeVO.getArrayAttribute(),customAttributeVO.getAttributeRowValue());				
			targetCustomAttribute.setAttributeValue(attributeValue);			
			boolean result=customStructure.updateCustomAttribute(targetCustomAttribute);
			booleanOperationResultVO.setOperationResult(result);				
		} catch (ActivityEngineRuntimeException | ActivityEngineDataException e) {	
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		} catch (ActivityEngineException e) {
			booleanOperationResultVO.setOperationResult(false);
			e.printStackTrace();
		}		
		return booleanOperationResultVO;
	}
	
	@POST
	@Path("/customStructure/getCustomAttribute/{applicationSpaceName}/{attributeName}")
	@Produces("application/json")
	public static CustomAttributeVO getStructureCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("attributeName")String attributeName,CustomStructureVO customStructureVO){
		try {
			CustomStructure customStructure=ActivityComponentFactory.getCustomStructure(applicationSpaceName, customStructureVO.getStructureId());				
			CustomAttribute targetCustomAttribute=customStructure.getCustomAttribute(attributeName);
			if(targetCustomAttribute==null){
				return null;
			}
			CustomAttributeVO targetCustomAttributeVO=new CustomAttributeVO();			
			targetCustomAttributeVO.setAttributeName(targetCustomAttribute.getAttributeName());
			targetCustomAttributeVO.setArrayAttribute(targetCustomAttribute.isArrayAttribute());			
			if(targetCustomAttribute.getAttributeType()==PropertyType.STRING){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_String);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.LONG){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Long);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DATE){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Date);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DOUBLE){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Double);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.BOOLEAN){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Boolean);
			}
			if(targetCustomAttribute.getAttributeType()==PropertyType.DECIMAL){
				targetCustomAttributeVO.setAttributeType(CustomAttributeType_Decimal);
			}			
			String[] attributeRowValue=generateCustomAttributeRowValue(targetCustomAttributeVO.getAttributeType(),targetCustomAttributeVO.getArrayAttribute(),targetCustomAttribute.getAttributeValue());				
			targetCustomAttributeVO.setAttributeRowValue(attributeRowValue);					
			return targetCustomAttributeVO;
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	@POST
	@Path("/customStructure/getCustomAttributes/{applicationSpaceName}/")
	@Produces("application/json")
	public static List<CustomAttributeVO> getStructureCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,CustomStructureVO customStructureVO){			
		List<CustomAttributeVO> customAttributeVOList=new ArrayList<CustomAttributeVO>();
		try {
			CustomStructure customStructure=ActivityComponentFactory.getCustomStructure(applicationSpaceName, customStructureVO.getStructureId());				
			List<CustomAttribute> targetCustomAttributeList=customStructure.getCustomAttributes();				
			for(CustomAttribute targetCustomAttribute:targetCustomAttributeList){				
				CustomAttributeVO targetCustomAttributeVO=new CustomAttributeVO();			
				targetCustomAttributeVO.setAttributeName(targetCustomAttribute.getAttributeName());
				targetCustomAttributeVO.setArrayAttribute(targetCustomAttribute.isArrayAttribute());			
				if(targetCustomAttribute.getAttributeType()==PropertyType.STRING){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_String);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.LONG){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Long);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DATE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Date);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DOUBLE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Double);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.BOOLEAN){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Boolean);
				}
				if(targetCustomAttribute.getAttributeType()==PropertyType.DECIMAL){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Decimal);
				}			
				String[] attributeRowValue=generateCustomAttributeRowValue(targetCustomAttributeVO.getAttributeType(),targetCustomAttributeVO.getArrayAttribute(),targetCustomAttribute.getAttributeValue());				
				targetCustomAttributeVO.setAttributeRowValue(attributeRowValue);
				customAttributeVOList.add(targetCustomAttributeVO);
			}								
			return customAttributeVOList;
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	@DELETE
	@Path("/customStructure/deleteCustomAttribute/{applicationSpaceName}/{attributeName}")
	@Produces("application/json")
	public static BooleanOperationResultVO deleteStructureCustomAttribute(@PathParam("applicationSpaceName")String applicationSpaceName,@PathParam("attributeName")String attributeName,CustomStructureVO customStructureVO){
		BooleanOperationResultVO booleanOperationResultVO=new BooleanOperationResultVO();
		booleanOperationResultVO.setTiemStamp(new Date().getTime());			
		try {
			CustomStructure customStructure=ActivityComponentFactory.getCustomStructure(applicationSpaceName, customStructureVO.getStructureId());							
			boolean deleteResult=customStructure.deleteCustomAttribute(attributeName);			
			booleanOperationResultVO.setOperationResult(deleteResult);			
		} catch (ActivityEngineRuntimeException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		} catch (ActivityEngineDataException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		} catch (ActivityEngineException e) {			
			e.printStackTrace();
			booleanOperationResultVO.setOperationResult(false);
		}		
		return booleanOperationResultVO;
	}		
	
	private static CustomStructureVO loadCustomStructure(CustomStructure targetCustomStructure) throws ActivityEngineRuntimeException, ActivityEngineDataException{
		CustomStructureVO targetCustomStructureVO=new CustomStructureVO();				
		targetCustomStructureVO.setStructureName(targetCustomStructure.getStructureName());
		targetCustomStructureVO.setStructureId(targetCustomStructure.getStructureId());	
		
		List<CustomAttribute> subCustomAttributesList=targetCustomStructure.getCustomAttributes();	
		List<CustomAttributeVO> customAttributeVOList=new ArrayList<CustomAttributeVO>();	
		if(subCustomAttributesList!=null){
			for(CustomAttribute currentCustomAttribute:subCustomAttributesList){
				CustomAttributeVO targetCustomAttributeVO=new CustomAttributeVO();			
				targetCustomAttributeVO.setAttributeName(currentCustomAttribute.getAttributeName());
				targetCustomAttributeVO.setArrayAttribute(currentCustomAttribute.isArrayAttribute());			
				if(currentCustomAttribute.getAttributeType()==PropertyType.STRING){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_String);
				}
				if(currentCustomAttribute.getAttributeType()==PropertyType.LONG){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Long);
				}
				if(currentCustomAttribute.getAttributeType()==PropertyType.DATE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Date);
				}
				if(currentCustomAttribute.getAttributeType()==PropertyType.DOUBLE){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Double);
				}
				if(currentCustomAttribute.getAttributeType()==PropertyType.BOOLEAN){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Boolean);
				}
				if(currentCustomAttribute.getAttributeType()==PropertyType.DECIMAL){
					targetCustomAttributeVO.setAttributeType(CustomAttributeType_Decimal);
				}			
				String[] attributeRowValue=generateCustomAttributeRowValue(targetCustomAttributeVO.getAttributeType(),targetCustomAttributeVO.getArrayAttribute(),currentCustomAttribute.getAttributeValue());				
				targetCustomAttributeVO.setAttributeRowValue(attributeRowValue);				
				customAttributeVOList.add(targetCustomAttributeVO);
			}
		}		
		targetCustomStructureVO.setSubCustomAttributes(customAttributeVOList);			
		
		List<CustomStructure> subCustomStructureList=targetCustomStructure.getSubCustomStructures();
		List<CustomStructureVO> customStructureVOList=new ArrayList<CustomStructureVO>();	
		if(subCustomStructureList!=null){
			for(CustomStructure currentCustomStructure:subCustomStructureList){
				CustomStructureVO currentCustomStructureVO=loadCustomStructure(currentCustomStructure);			
				customStructureVOList.add(currentCustomStructureVO);
			}
		}		
		targetCustomStructureVO.setSubCustomStructures(customStructureVOList);			
		return targetCustomStructureVO;
	}
	
	private static String[] generateCustomAttributeRowValue(String attributeType,boolean arrayAttribute,Object attributeValue){
		if(attributeType.equals(CustomAttributeType_String)){				
			if(arrayAttribute){	
				String[] currentData=(String[])attributeValue;				
				return currentData;				
			}else{						
				String currentData=(String)attributeValue;				
				return new String[]{currentData};	
			}		
		}
		if(attributeType.equals(CustomAttributeType_Long)){
			if(arrayAttribute){	
				long[] currentData=(long[])attributeValue;				
				String[] arrayData=new String[currentData.length];
				for(int i=0;i<currentData.length;i++){
					long currentValue=currentData[i];					
					arrayData[i]=""+currentValue;					
				}
				return arrayData;			
			}else{
				Long currentData=(Long)attributeValue;				
				return new String[]{""+currentData.longValue()};
			}					
		}
		if(attributeType.equals(CustomAttributeType_Date)){				
			if(arrayAttribute){	
				Calendar[] currentData=(Calendar[])attributeValue;				
				String[] arrayData=new String[currentData.length];
				for(int i=0;i<currentData.length;i++){
					Calendar currentValue=currentData[i];					
					arrayData[i]=""+currentValue.getTimeInMillis();					
				}				
				return arrayData;	
			}else{	
				Calendar currentData=(Calendar)attributeValue;
				return new String[]{""+currentData.getTimeInMillis()};			
			}	
		}
		if(attributeType.equals(CustomAttributeType_Double)){				
			if(arrayAttribute){	
				double[] currentData=(double[])attributeValue;				
				String[] arrayData=new String[currentData.length];
				for(int i=0;i<currentData.length;i++){
					double currentValue=currentData[i];					
					arrayData[i]=""+currentValue;					
				}
				return arrayData;
			}else{		
				Double currentData=(Double)attributeValue;				
				return new String[]{""+currentData.doubleValue()};
			}	
		}
		if(attributeType.equals(CustomAttributeType_Boolean)){				
			if(arrayAttribute){	
				boolean[] currentData=(boolean[])attributeValue;				
				String[] arrayData=new String[currentData.length];
				for(int i=0;i<currentData.length;i++){
					boolean currentValue=currentData[i];					
					arrayData[i]=""+currentValue;					
				}
				return arrayData;
			}else{		
				Boolean currentData=(Boolean)attributeValue;				
				return new String[]{""+currentData.booleanValue()};
			}	
		}
		if(attributeType.equals(CustomAttributeType_Decimal)){				
			if(arrayAttribute){	
				BigDecimal[] currentData=(BigDecimal[])attributeValue;				
				String[] arrayData=new String[currentData.length];
				for(int i=0;i<currentData.length;i++){
					BigDecimal currentValue=currentData[i];					
					arrayData[i]=""+currentValue.toString();					
				}
				return arrayData;
			}else{
				BigDecimal currentData=(BigDecimal)attributeValue;				
				return new String[]{""+currentData.toString()};
			}	
		}
		return null;
	}	
	
	private static Object generateCustomAttributeValueFromRowData(String attributeType,boolean arrayAttribute,String[] attributeRowValue){
		if(attributeType.equals(CustomAttributeType_String)){				
			if(arrayAttribute){	
				return attributeRowValue;				
			}else{		
				String rowValueString=attributeRowValue[0];
				return rowValueString;
			}		
		}
		if(attributeType.equals(CustomAttributeType_Long)){
			if(arrayAttribute){	
				long[] arrayData=new long[attributeRowValue.length];
				for(int i=0;i<attributeRowValue.length;i++){
					String currentValue=attributeRowValue[i];
					long currentLongValue=Long.parseLong(currentValue);
					arrayData[i]=currentLongValue;					
				}
				return arrayData;			
			}else{
				String rowValueString=attributeRowValue[0];
				return new Long(rowValueString);
			}					
		}
		if(attributeType.equals(CustomAttributeType_Date)){				
			if(arrayAttribute){	
				Calendar[] arrayData=new Calendar[attributeRowValue.length];
				for(int i=0;i<attributeRowValue.length;i++){
					String currentValue=attributeRowValue[i];
					long timeStamp=Long.parseLong(currentValue);
					Date date = new Date(timeStamp);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);						
					arrayData[i]=calendar;					
				}
				return arrayData;	
			}else{		
				String rowValueString=attributeRowValue[0];				
				long timeStamp=Long.parseLong(rowValueString);
				Date date = new Date(timeStamp);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				return calendar;
			}	
		}
		if(attributeType.equals(CustomAttributeType_Double)){				
			if(arrayAttribute){	
				double[] arrayData=new double[attributeRowValue.length];
				for(int i=0;i<attributeRowValue.length;i++){
					String currentValue=attributeRowValue[i];
					double currentDoubleValue=Double.parseDouble(currentValue);
					arrayData[i]=currentDoubleValue;					
				}
				return arrayData;	
			}else{		
				String rowValueString=attributeRowValue[0];
				return new Double(rowValueString);
			}	
		}
		if(attributeType.equals(CustomAttributeType_Boolean)){				
			if(arrayAttribute){	
				boolean[] arrayData=new boolean[attributeRowValue.length];
				for(int i=0;i<attributeRowValue.length;i++){
					String currentValue=attributeRowValue[i];
					boolean currentBooleanValue=Boolean.parseBoolean(currentValue);
					arrayData[i]=currentBooleanValue;					
				}
				return arrayData;
			}else{		
				String rowValueString=attributeRowValue[0];
				return Boolean.valueOf(rowValueString);
			}	
		}
		if(attributeType.equals(CustomAttributeType_Decimal)){				
			if(arrayAttribute){	
				BigDecimal[] arrayData=new BigDecimal[attributeRowValue.length];
				for(int i=0;i<attributeRowValue.length;i++){
					String currentValue=attributeRowValue[i];
					BigDecimal currentDecimalValue=new BigDecimal(currentValue);
					arrayData[i]=currentDecimalValue;					
				}
				return arrayData;
			}else{		
				String rowValueString=attributeRowValue[0];
				return new BigDecimal(rowValueString);
			}	
		}			
		return null;
	}	
}