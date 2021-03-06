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
package com.ibm.sbt.services.client.connections.forums;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.NamespaceContext;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;


/**
 * Forum model object
 * 
 * @author Manish Kataria 
 * @author Swati Singh
 */


public class Forum extends BaseForumEntity {
	
	/**
	 * Constructor
	 *  
	 * @param ForumService
	 * @param ForumId
	 */
	public Forum(ForumService forumsService, String id) {
		super(forumsService,id);
	}
	
	/**
     * Constructor
     *
     * @param ForumService
     */
    public Forum(ForumService forumsService) {
            super(forumsService);
    }

	public Forum(BaseService service, Node node, NamespaceContext namespaceCtx, XPathExpression xpathExpression) {
		super(service, node, namespaceCtx, xpathExpression);
	}
	

	public void setForumUuid(String forumUuid) {
        setAsString(ForumsXPath.uid, forumUuid);
    }
	
	
	/*
	 * This method returns uid of forum
	 */
	public String getForumUuid() {
		return super.getUid();
	}
	
	/**
     * Return the moderation of the IBM Connections forum from
     * forum ATOM entry document.
     * 
     * @method getModeration
     * @return {String} Moderation of the forum
     */
	public String getModeration() {
        return getAsString(ForumsXPath.moderation);
    }
	
	  /**
     * Return the thread count of the IBM Connections forum from
     * forum ATOM entry document.
     * 
     * @method getThreadCount
     * @return {int} Thread count of the forum
     */
	public int getThreadCount() {
        return getAsInt(ForumsXPath.threadCount);
    }
    
	public EntityList<ForumTopic> getTopics() throws ClientServicesException{
    	return getTopics(null);
    }
    /**
     * Get a list for forum topics that includes the topics in the specified forum.
     * 
     * @method getTopics
     * @param parameters
     */
    public EntityList<ForumTopic> getTopics(Map<String, String> parameters) throws ClientServicesException{
    	return getService().getForumTopics(getUid(), parameters);
    }
    /**
     * Return the url of the IBM Connections forum from
     * forum ATOM entry document.
     * 
     * @method getForumUrl
     * @return {String} Url of the forum
     */
    public String getForumUrl() {
        return getAsString(ForumsXPath.alternateUrl);
    }
	
	/**
	 * This method updates the forum on the server
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	public Forum save() throws ClientServicesException{
		if(StringUtil.isEmpty(getUid())){
			return getService().createForum(this);
		}else{
			getService().updateForum(this);
			return getService().getForum(getUid());
		}
	}
	
	
	/**
	 * This method deletes the forum on the server
	 * 
	 * @return
	 * @throws ClientServicesException
	 */

	public void remove() throws ClientServicesException {
	   	getService().deleteForum(getUid());
	}
	
	/**
	 * This method loads the forum 
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	
	public Forum load() throws ClientServicesException
    {
		return getService().getForum(getUid());
    }


}
