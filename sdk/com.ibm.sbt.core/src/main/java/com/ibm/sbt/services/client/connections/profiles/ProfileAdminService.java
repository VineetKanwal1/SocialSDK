/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client.connections.profiles;

import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.FORMAT;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.FULL;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.OUTPUT;
import static com.ibm.sbt.services.client.connections.profiles.utils.ProfilesConstants.VCARD;

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.CommonConstants.HTTPCode;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.profiles.utils.Messages;
import com.ibm.sbt.services.client.smartcloud.profiles.ProfileServiceException;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * ProfileAdminService can be used to perform Admin operations related to Connection Profiles. 
 * 
 * @Represents Connections ProfileAdminService
 * @author Swati Singh
 * @author Carlos Manias
 * <pre>
 * Sample Usage
 * {@code
 *  ProfileAdminService _service = new ProfileAdminService();
 *  Profile profile = _service.getProfile("testUser61@renovations.com", false);
 *  profile.set("uid", "testUser");
 *  boolean success = service.createProfile(profile);
 * }
 * </pre>
 */
public class ProfileAdminService extends ProfileService {

	private static final long serialVersionUID = 3976235045185720867L;

	/**
	 * Constructor Creates ProfileAdminService Object with default endpoint and default cache size
	 */
	public ProfileAdminService() {
		this(DEFAULT_ENDPOINT_NAME, DEFAULT_CACHE_SIZE);
    }
	
	/**
	 * Constructor 
	 * 
	 * @param endpoint
	 *            Creates ProfileAdminService with specified endpoint and a default CacheSize
	 */
	public ProfileAdminService(Endpoint endpoint) {
		super(endpoint);
	}
	
	/**
	 * Constructor 
	 * 
	 * @param endpoint
	 *            Creates ProfileAdminService with specified endpoint and a default CacheSize
	 */
	public ProfileAdminService(String endpoint) {
		this(endpoint, DEFAULT_CACHE_SIZE);
	}
	
	/**
	 * Constructor 
	 * 
	 * @param endpoint
	 * @param cacheSize
	 *            Creates ProfileAdminService with specified endpoint and CacheSize
	 */
	public ProfileAdminService(String endpoint, int cacheSize) {
		super(endpoint, cacheSize);
	}
	
	/**
	 * Wrapper method to delete a User's profile
	 * <p>
	 * User should be logged in as a administrator to call this method
	 * 
	 * @param id
	 * 			unique identifier of user whose profile is to be deleted, it can either be a email or userid
	 * @throws ProfileServiceException 
	 */
	public void deleteProfile(String id) throws ClientServicesException
	{	
		String deleteUrl = ProfileUrls.ADMIN_PROFILE_ENTRY.format(this, ProfileParams.userId.get(id));
		Response response = deleteData(deleteUrl, null, ProfileParams.userId.getParamName(id));
		checkResponseCode(response, HTTPCode.OK);
	}

	/**
	 * Wrapper method to create a User's profile
	 * <p>
	 * User should be logged in as a administrator to call this method
	 * 
	 * @param Profile
	 * @throws ProfileServiceException 
	 */
	public void createProfile(Profile profile) throws ClientServicesException {
		if (profile == null) {
			throw new ClientServicesException(null, Messages.InvalidArgument_3);
		}		
		Map<String, String> parameters = new HashMap<String,String>();
		String id = profile.getUserid();
		parameters.put(ProfileParams.userId.getParamName(id), id);
		Object createPayload = constructCreateRequestBody(profile);
		
		String createUrl = ProfileUrls.ADMIN_PROFILES.format(this);
		Response response = createData(createUrl, parameters, createPayload, ClientService.FORMAT_CONNECTIONS_OUTPUT);
		checkResponseCode(response, HTTPCode.OK);
	}

	/**
	 * 
	 * @param <b>parameters</b> a map of search keys<br/>
	 * <b>email</b>	Internet email address. Returns all profiles that contain a matching email address.<br/>
	 * <b>key</b>	An ID generated by Profiles to identify a person.<br/>
	 * <b>ps</b>	Page size. Specifies the number of entries to return per page. Use the rel="next" link to retrieve additional pages.<br/>
	 * <b>uid</b>	An organizationally specific unique identifier for the user<br/>
	 * <b>userid</b>	Unique ID that represents a specific person. <br/>
	 * @return a list or a list with matching profiles
	 * @throws ClientServicesException 
	 */
	@Override
	public EntityList<Profile> searchProfiles(Map<String,String> parameters) throws ClientServicesException {
		String url = ProfileUrls.ADMIN_PROFILES.format(this);
		return getProfileEntityList(url, parameters);
	}

	/**
	 * Wrapper method to update a User's profile
	 * 
	 * @param Profile
	 * @throws ProfileServiceException
	 */
	@Override
	public void updateProfile(Profile profile) throws ClientServicesException {
		if (profile == null) {
			throw new ClientServicesException(null, Messages.InvalidArgument_3);
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(OUTPUT, VCARD);
		parameters.put(FORMAT, FULL);
		String id = profile.getUserid();
		Object updateProfilePayload = constructUpdateRequestBody(profile);
		String updateUrl = ProfileUrls.ADMIN_PROFILE_ENTRY.format(this, ProfileParams.userId.get(id));
		Response response = updateData(updateUrl, parameters,updateProfilePayload, ProfileParams.userId.getParamName(profile.getAsString("uid")));
		checkResponseCode(response, HTTPCode.OK);
		profile.clearFieldsMap();
	}

	/**
	 * This method returns a feed of profile as opposed to retrieving the Atom entry of the profile.<br>
	 * If you want to retrieve an Atom entry document, see Retrieving a profile entry.<br>
	 * The content element of each returned entry includes the vcard information for the person being represented by the entry.<br>
	 * In addition, it provides a list of the fully qualified URLs for each IBM® Connections application link displayed in the business card.
	 * 
	 * @param id
	 *             unique identifier of User , it can either be email or id 				
	 * @param parameters 
	 * 				list of query string parameters to pass to API
	 * @return Profile
	 * @throws ClientServicesException 
	 */
	@Override
	public Profile getProfile(String id, Map<String, String> parameters) throws ClientServicesException {
		// TODO: Do a cache lookup first. If cache miss, make a network call to get profile
		if (StringUtil.isEmpty(id)){
			throw new ClientServicesException(null, Messages.InvalidArgument_1);
		}
		String url = ProfileUrls.ADMIN_GET_PROFILES.format(this, ProfileParams.userId.get(id));
		return getProfileEntity(url, parameters);
	}
}
