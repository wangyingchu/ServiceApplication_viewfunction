package com.viewfunction.vfmab.restfulClient;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;

import com.viewfunction.vfmab.restful.userManagement.UserBasicInfoVOList;

public class UserManagementServiceRESTClient {	
	public static UserBasicInfoVOList getUserUnitsInfoOfRole(String applicationSpaceName,String roleName){
		WebClient client = WebClient.create(RESTClientConfigUtil.getREST_baseURLValue());
		client.path("userManagementService/usersInfoOfRole/"+applicationSpaceName+"/"+roleName+"/");		
		client.type("application/xml").accept("application/xml");
		Response response =client.get();		
		UserBasicInfoVOList userBasicInfoVOList= response.readEntity(UserBasicInfoVOList.class);
		return userBasicInfoVOList;
	}	
	
	public static void main(String[] args){
		UserBasicInfoVOList userBasicInfoVOList=UserManagementServiceRESTClient.getUserUnitsInfoOfRole("aaaa", "Manufacturing Department");
		System.out.println(userBasicInfoVOList.getUserBasicInfoVOList());
	}
}