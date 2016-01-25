package com.smokeTest;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/smokeTestService")  
@Produces("application/json")
public class TestService {
	@GET
    @Path("/ping/")
    @Produces("application/json")
	public TestReturnVO participantLoginVerify(){		
		TestReturnVO testReturnVO=new TestReturnVO();
		testReturnVO.setVoName("TestVO");
		testReturnVO.setTiemStamp(new Date().getTime());
		return testReturnVO;		
	}
}