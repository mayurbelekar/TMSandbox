package org.tmsandbox.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Reporter;
import org.testng.annotations.Test;
import org.tmsandbox.JerseyClientUtils;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import junit.framework.Assert;

public class TMSandboxTest {
	JerseyClientUtils clientUtils = new JerseyClientUtils();
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testCategoryTMSandbox() {
		Map<String, String> headers = new HashMap<String, String>();
		MultivaluedMap<String, String> queryParams=new MultivaluedMapImpl();
		queryParams.add("catalogue", "false");
		
		ClientResponse response = clientUtils.getOperation(TMSandboxConstants.BASE_URI, TMSandboxConstants.PATH, queryParams, headers);
		String responseBody = clientUtils.getResponseBody(response);
		
		JSONObject object = new JSONObject(responseBody);
		String name = object.getString(TMSandboxConstants.NAME);
		boolean canRelist = object.getBoolean(TMSandboxConstants.CAN_RELIST);
		String promotionDescription =  null;
		
		JSONArray promotionsArray =  object.getJSONArray(TMSandboxConstants.PROMOTIONS);
		List<Map<String, Object>> promotions = clientUtils.getJsonArrayValues(promotionsArray.toString());
		for(int i=0;i < promotions.size(); i++) {
			if(promotions.get(i).get(TMSandboxConstants.NAME).toString().equals(TMSandboxConstants.GALLERY)) {
				promotionDescription = promotions.get(i).get(TMSandboxConstants.DESCRIPTION).toString();
				break;
			}
		}
		
		Reporter.log("Acceptance Criteria:", true);
		Reporter.log("Verifing the Name: "+TMSandboxConstants.CARBON_CREDITS, true);
		Assert.assertEquals(TMSandboxConstants.CARBON_CREDITS, name);
		
		Reporter.log("Verifing the "+TMSandboxConstants.CAN_RELIST, true);
		Assert.assertTrue(canRelist);
		
		Reporter.log("Verifing the Description contains: "+TMSandboxConstants.DESCRIPTION_LARGER_IMAGE, true);
		Assert.assertTrue(promotionDescription.contains(TMSandboxConstants.DESCRIPTION_LARGER_IMAGE));
		
	}
}
